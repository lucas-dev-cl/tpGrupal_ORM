package org.utn;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.utn.entity.articulo.*;
import org.utn.entity.usuario.Usuario;
import org.utn.entity.venta.FacturaVenta;
import org.utn.entity.venta.FacturaVentaDetalle;
import org.utn.entity.venta.PuntoVenta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1) Arranca el contenedor de JPA y obtiene el EntityManager
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("FacturacionPU");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // ==========================================
            // Usuario (quien opera el sistema)
            // ==========================================
            Usuario usuario = new Usuario();
            usuario.setUsuario("jperez");
            usuario.setClave("1234");
            usuario.setNombre("Juan");
            usuario.setApellido("Perez");
            em.persist(usuario);

            // ==========================================
            // Catalogo de productos: Rubro, Marca, Articulo
            // ==========================================
            Rubro rubro = new Rubro();
            rubro.setCodigo(1);
            rubro.setDenominacion("Almacen");
            rubro.setFechaAlta(new Date());
            rubro.setFechaModificacion(new Date());
            rubro.setUsuarioCarga(usuario);
            rubro.setUsuarioModificacion(usuario);
            em.persist(rubro);

            Marca marca = new Marca();
            marca.setCodigo(10);
            marca.setDenominacion("Coca-Cola");
            marca.setFechaAlta(new Date());
            marca.setFechaModificacion(new Date());
            marca.setUsuarioCarga(usuario);
            marca.setUsuarioModificacion(usuario);
            em.persist(marca);

            Articulo articulo = new Articulo();
            articulo.setCodigo("ART-001");
            articulo.setDenominacion("Coca-Cola 500ml");
            articulo.setRubro(rubro);
            articulo.setMarca(marca);
            articulo.setFechaAlta(new Date());
            articulo.setFechaModificacion(new Date());
            articulo.setUsuarioCarga(usuario);
            articulo.setUsuarioModificacion(usuario);
            em.persist(articulo);

            // ==========================================
            // Lista de precios y su relacion con el articulo
            // ==========================================
            ListaPrecio listaPrecio = new ListaPrecio();
            listaPrecio.setCodigo("LP-MIN");
            listaPrecio.setDenominacion("Lista Minorista");
            listaPrecio.setFechaAlta(new Date());
            listaPrecio.setFechaModificacion(new Date());
            listaPrecio.setUsuarioCarga(usuario);
            listaPrecio.setUsuarioModificacion(usuario);
            em.persist(listaPrecio);

            ListaPrecioArticulo listaPrecioArticulo = new ListaPrecioArticulo();
            listaPrecioArticulo.setListaPrecio(listaPrecio);
            listaPrecioArticulo.setArticulo(articulo);
            listaPrecioArticulo.setPrecioVenta(1500.0);
            listaPrecioArticulo.setFechaAlta(new Date());
            listaPrecioArticulo.setFechaModificacion(new Date());
            listaPrecioArticulo.setUsuarioCarga(usuario);
            listaPrecioArticulo.setUsuarioModificacion(usuario);
            em.persist(listaPrecioArticulo);

            // ==========================================
            // Punto de venta
            // ==========================================
            PuntoVenta puntoVenta = new PuntoVenta();
            puntoVenta.setNumero(1);
            puntoVenta.setDescripcion("Sucursal Central");
            puntoVenta.setTipoEmision("Electronica");
            puntoVenta.setDomicilioComercial("Av. Siempre Viva 123");
            puntoVenta.setFechaAlta(new Date());
            puntoVenta.setFechaModificacion(new Date());
            puntoVenta.setUsuarioCarga(usuario);
            puntoVenta.setUsuarioModificacion(usuario);
            em.persist(puntoVenta);

            // ==========================================
            // Cabecera de la factura
            // ==========================================
            FacturaVenta facturaVenta = new FacturaVenta();
            facturaVenta.setNumero(1L);
            facturaVenta.setFechaEmision(new Date());
            facturaVenta.setPuntoVenta(puntoVenta);
            facturaVenta.setImporteTotal(4500.0);
            facturaVenta.setImporteCobrado(4500.0);
            facturaVenta.setImporteSaldo(0.0);
            facturaVenta.setEstado("EMITIDA");
            facturaVenta.setFechaAlta(new Date());
            facturaVenta.setFechaModificacion(new Date());
            facturaVenta.setUsuarioCarga(usuario);
            facturaVenta.setUsuarioModificacion(usuario);

            // ==========================================
            // Detalles de la factura (relacion bidireccional)
            // ==========================================
            List<FacturaVentaDetalle> detalles = new ArrayList<>();

            FacturaVentaDetalle detalle1 = new FacturaVentaDetalle();
            detalle1.setFactura(facturaVenta);
            detalle1.setListaPrecioArticulo(listaPrecioArticulo);
            detalle1.setDescripcion("Coca-Cola 500ml");
            detalle1.setCantidad(3);
            detalle1.setPrecioUnitario(1500.0);
            detalle1.setPorcentajeBonificacion(0.0);
            detalle1.setImporteNeto(4500.0);
            detalle1.setImporteIva(0.0);
            detalle1.setImporteSubtotal(4500.0);
            detalles.add(detalle1);

            facturaVenta.setDetalles(detalles);

            // Requisito clave: un unico em.persist() para la cabecera.
            // Gracias a cascade = CascadeType.ALL (+ orphanRemoval) en
            // FacturaVenta.detalles, Hibernate inserta en cascada tambien
            // los FacturaVentaDetalle asociados.
            em.persist(facturaVenta);

            em.getTransaction().commit();

            System.out.println("Factura persistida con id: " + facturaVenta.getId());
            System.out.println("Cantidad de detalles insertados en cascada: " + facturaVenta.getDetalles().size());

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}