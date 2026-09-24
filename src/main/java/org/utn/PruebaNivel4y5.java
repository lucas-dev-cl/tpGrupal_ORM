package org.utn;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.utn.consultas.ConsultasNivel4y5;
import org.utn.entity.articulo.Articulo;
import org.utn.entity.articulo.ListaPrecio;
import org.utn.entity.articulo.ListaPrecioArticulo;
import org.utn.entity.articulo.Marca;
import org.utn.entity.articulo.Rubro;
import org.utn.entity.base.AuditoriaApp;
import org.utn.entity.cliente.Cliente;
import org.utn.entity.cliente.CondicionIva;
import org.utn.entity.cliente.Contacto;
import org.utn.entity.cliente.Domicilio;
import org.utn.entity.usuario.Usuario;
import org.utn.entity.venta.FacturaVenta;
import org.utn.entity.venta.FacturaVentaDetalle;
import org.utn.entity.venta.PuntoVenta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PruebaNivel4y5 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("FacturacionPU");
        EntityManager em = emf.createEntityManager();
        try {
            cargarDatosDePrueba(em);
            ConsultasNivel4y5.ejecutarTodas(em);
        } finally {
            em.close();
            emf.close();
        }
    }

    // Completa los campos de auditoría que son obligatorios (nullable = false)
    private static void auditar(AuditoriaApp e, Usuario u) {
        Date ahora = new Date();
        e.setFechaAlta(ahora);
        e.setFechaModificacion(ahora);
        e.setUsuarioCarga(u);
        e.setUsuarioModificacion(u);
    }

    private static void cargarDatosDePrueba(EntityManager em) {
        // Evita duplicar datos si el Main se ejecuta más de una vez
        Long usuarios = em.createQuery("SELECT COUNT(u) FROM Usuario u", Long.class).getSingleResult();
        if (usuarios > 0) {
            System.out.println(">> Ya hay datos cargados, no se vuelven a insertar.");
            return;
        }

        em.getTransaction().begin();
        try {
            // ---- Usuarios ----
            Usuario ana = new Usuario();
            ana.setUsuario("ana"); ana.setClave("1234"); ana.setNombre("Ana"); ana.setApellido("Perez");
            em.persist(ana);

            Usuario beto = new Usuario();
            beto.setUsuario("beto"); beto.setClave("1234"); beto.setNombre("Beto"); beto.setApellido("Gomez");
            em.persist(beto);

            // ---- Condiciones de IVA ----
            CondicionIva respInscripto = new CondicionIva();
            respInscripto.setCodigoAfip(1); respInscripto.setDenominacion("Responsable Inscripto");
            auditar(respInscripto, ana); em.persist(respInscripto);

            CondicionIva consFinal = new CondicionIva();
            consFinal.setCodigoAfip(5); consFinal.setDenominacion("Consumidor Final");
            auditar(consFinal, ana); em.persist(consFinal);

            // ---- Rubro y marcas ----
            Rubro rubro = new Rubro();
            rubro.setDenominacion("Electrónica"); rubro.setCodigo(1);
            auditar(rubro, ana); em.persist(rubro);

            Marca samsung = nuevaMarca("Samsung", 1, ana, em);
            Marca logitech = nuevaMarca("Logitech", 2, ana, em);
            Marca sony = nuevaMarca("Sony", 3, ana, em);   // su artículo nunca se factura

            // ---- Artículos ----
            Articulo notebook = nuevoArticulo("A001", "Notebook", rubro, samsung, ana, em);
            Articulo mouse = nuevoArticulo("A002", "Mouse", rubro, logitech, ana, em);
            Articulo auricular = nuevoArticulo("A003", "Auricular", rubro, sony, ana, em);  // nunca facturado
            Articulo cable = nuevoArticulo("A004", "Cable HDMI", rubro, null, ana, em);     // sin marca

            // ---- Lista de precios ----
            ListaPrecio lista = new ListaPrecio();
            lista.setCodigo("LP1"); lista.setDenominacion("Lista general");
            auditar(lista, ana); em.persist(lista);

            ListaPrecioArticulo lpaNotebook = nuevoPrecio(lista, notebook, 100000, ana, em);
            ListaPrecioArticulo lpaMouse = nuevoPrecio(lista, mouse, 5000, ana, em);
            nuevoPrecio(lista, auricular, 20000, ana, em);
            ListaPrecioArticulo lpaCable = nuevoPrecio(lista, cable, 1500, ana, em);

            // ---- Puntos de venta ----
            PuntoVenta pv1 = nuevoPuntoVenta(1, "Casa Central", ana, em);
            PuntoVenta pv2 = nuevoPuntoVenta(2, "Sucursal Mendoza", ana, em);

            // ---- Clientes ----
            Cliente cliente1 = nuevoCliente("20-11111111-1", "Juan Lopez", respInscripto, ana, em);
            Cliente cliente2 = nuevoCliente("27-22222222-2", "Maria Diaz", consFinal, ana, em);

            // ---- Facturas ----
            // Ana carga 7 (para que la consulta 17 con "más de 5" la devuelva), Beto carga 2.
            // Los importes cubren las tres categorías de la consulta 22.
            double[] importes = {5000, 8000, 12000, 25000, 45000, 60000, 90000, 15000, 3000};
            ListaPrecioArticulo[] articulosFacturados = {lpaNotebook, lpaMouse, lpaCable};

            for (int i = 0; i < importes.length; i++) {
                Usuario cargador = (i < 7) ? ana : beto;
                PuntoVenta pv = (i % 2 == 0) ? pv1 : pv2;
                Cliente cliente = (i % 2 == 0) ? cliente1 : cliente2;
                ListaPrecioArticulo lpa = articulosFacturados[i % articulosFacturados.length];

                FacturaVenta f = new FacturaVenta();
                f.setNumero((long) (i + 1));
                f.setFechaEmision(new Date());
                f.setPuntoVenta(pv);
                f.setCliente(cliente);
                f.setImporteTotal(importes[i]);
                f.setImporteCobrado(0);
                f.setImporteSaldo(importes[i]);
                f.setEstado("EMITIDA");
                auditar(f, cargador);

                FacturaVentaDetalle d = new FacturaVentaDetalle();
                d.setFactura(f);
                d.setListaPrecioArticulo(lpa);
                d.setDescripcion(lpa.getArticulo().getDenominacion());
                d.setCantidad(2);
                d.setPrecioUnitario(importes[i] / 2);
                d.setImporteNeto(importes[i]);
                d.setImporteSubtotal(importes[i]);

                List<FacturaVentaDetalle> detalles = new ArrayList<>();
                detalles.add(d);
                f.setDetalles(detalles);

                em.persist(f);   // el detalle se guarda por cascade
            }

            em.getTransaction().commit();
            System.out.println(">> Datos de prueba cargados.");
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    // ---------- helpers ----------

    private static Marca nuevaMarca(String nombre, int codigo, Usuario u, EntityManager em) {
        Marca m = new Marca();
        m.setDenominacion(nombre); m.setCodigo(codigo);
        auditar(m, u); em.persist(m);
        return m;
    }

    private static Articulo nuevoArticulo(String codigo, String nombre, Rubro rubro, Marca marca,
                                          Usuario u, EntityManager em) {
        Articulo a = new Articulo();
        a.setCodigo(codigo); a.setDenominacion(nombre); a.setRubro(rubro); a.setMarca(marca);
        auditar(a, u); em.persist(a);
        return a;
    }

    private static ListaPrecioArticulo nuevoPrecio(ListaPrecio lista, Articulo art, double precio,
                                                   Usuario u, EntityManager em) {
        ListaPrecioArticulo lpa = new ListaPrecioArticulo();
        lpa.setListaPrecio(lista); lpa.setArticulo(art); lpa.setPrecioVenta(precio);
        auditar(lpa, u); em.persist(lpa);
        return lpa;
    }

    private static PuntoVenta nuevoPuntoVenta(int numero, String descripcion, Usuario u, EntityManager em) {
        PuntoVenta pv = new PuntoVenta();
        pv.setNumero(numero); pv.setDescripcion(descripcion);
        auditar(pv, u); em.persist(pv);
        return pv;
    }

    private static Cliente nuevoCliente(String cuit, String nombre, CondicionIva iva, Usuario u, EntityManager em) {
        Contacto contacto = new Contacto();
        contacto.setEmail(nombre.toLowerCase().replace(" ", ".") + "@mail.com");
        contacto.setTelefono("261-4000000"); contacto.setCelular("261-15000000");
        em.persist(contacto);

        Domicilio domicilio = new Domicilio();
        domicilio.setNombreCalle("San Martin"); domicilio.setNumeroCalle("123");
        em.persist(domicilio);

        Cliente c = new Cliente();
        c.setCuitCuil(cuit); c.setDenominacion(nombre);
        c.setContacto(contacto); c.setDomicilio(domicilio); c.setCondicionIva(iva);
        auditar(c, u); em.persist(c);
        return c;
    }
}
