package org.utn.Pruebas;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.utn.consultas.ConsultasNivel3;
import org.utn.entity.venta.FacturaVenta;
import org.utn.entity.venta.FacturaVentaDetalle;

import java.util.List;

public class PruebaNivel3 {

    public static void ejecutar() {
        System.out.println("\n==================================================");
        System.out.println(">>> EJECUTANDO NIVELES 3 (Consultas 10 a 15)");
        System.out.println("==================================================");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("FacturacionPU");
        EntityManager em = emf.createEntityManager();

        try {
            ConsultasNivel3 consultas = new ConsultasNivel3(em);

            System.out.println("--- Ejercicio 10: Facturas por usuario de carga ('ana') ---");
            List<FacturaVenta> facturasUsuario = consultas.findByUsuarioCarga("ana");
            for (FacturaVenta f : facturasUsuario) {
                System.out.println("Factura N°: " + f.getNumero() +
                        " | Total: $" + f.getImporteTotal() +
                        " | Estado: " + f.getEstado());
            }

            System.out.println("--- Ejercicio 11: Detalles de factura por Punto de Venta (ID: 1) ---");
            List<FacturaVentaDetalle> detallesPV = consultas.findByPuntoVenta(1L);
            for (FacturaVentaDetalle d : detallesPV) {
                System.out.println("Detalle ID: " + d.getId() +
                        " | Cantidad: " + d.getCantidad() +
                        " | Subtotal: $" + d.getImporteSubtotal());
            }

            System.out.println("--- Ejercicio 12: Todos los artículos con su marca (LEFT JOIN) ---");
            List<Object[]> articulosConMarca = consultas.findAllWithMarca();
            for (Object[] fila : articulosConMarca) {
                String articulo = (String) fila[0];
                String marca = (String) fila[1];
                System.out.println("Artículo: " + articulo +
                        " | Marca: " + (marca != null ? marca : "Sin marca"));
            }

            System.out.println("--- Ejercicio 13: Facturas que contienen artículos de Marca (ID: 1) ---");
            List<FacturaVenta> facturasMarca = consultas.findByMarcaArticulo(1L);
            for (FacturaVenta f : facturasMarca) {
                System.out.println("Factura N°: " + f.getNumero() +
                        " | Total: $" + f.getImporteTotal());
            }

            System.out.println("--- Ejercicio 14: Facturas con importe mayor al promedio general ---");
            List<FacturaVenta> facturasMayoresPromedio = consultas.findByImporteMayorAlPromedio();
            for (FacturaVenta f : facturasMayoresPromedio) {
                System.out.println("Factura N°: " + f.getNumero() +
                        " | Importe Total: $" + f.getImporteTotal());
            }

            System.out.println("--- Ejercicio 15: Facturas filtradas por CUIT/CUIL de Cliente ('20-12345678-9') ---");
            List<FacturaVenta> facturasCliente = consultas.findByClienteCuitCuil("20-12345678-9");
            for (FacturaVenta f : facturasCliente) {
                System.out.println("Factura N°: " + f.getNumero() +
                        " | Cliente: " + f.getCliente().getDenominacion() +
                        " | Total: $" + f.getImporteTotal());
            }

        } finally {
            em.close();
            emf.close();
        }
    }
}
