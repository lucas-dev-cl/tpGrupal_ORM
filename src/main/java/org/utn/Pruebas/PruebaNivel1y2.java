package org.utn.Pruebas;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.utn.consultas.ConsultasNivel1y2;
import org.utn.entity.venta.FacturaVenta;
import org.utn.entity.catalogo.Articulo;
import org.utn.entity.cliente.Cliente;
import org.utn.entity.venta.PuntoVenta;
import java.util.Arrays;

import java.util.List;

public class PruebaNivel1y2 {

    public static void ejecutar() {
        System.out.println("\n==================================================");
        System.out.println(">>> EJECUTANDO NIVELES 1 Y 2 (Consultas 1 a 9)");
        System.out.println("==================================================");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("FacturacionPU");
        EntityManager em = emf.createEntityManager();

        try {
            System.out.println("--- Punto 1: Lista completa de facturas ---");
            List<FacturaVenta> facturas = ConsultasNivel1y2.todasLasFacturas(em);
            
            for (FacturaVenta f : facturas) {
                System.out.println("Factura N°: " + f.getNumero() + 
                                   " | Fecha: " + f.getFechaEmision() + 
                                   " | Total: $" + f.getImporteTotal() + 
                                   " | Estado: " + f.getEstado());
            }
            System.out.println("--- Punto 2: Proyección de Atributos Específicos ---");
            List<Object[]> proyecciones = ConsultasNivel1y2.proyeccionFacturas(em);
            
            for (Object[] fila : proyecciones) {
                Long numero = (Long) fila[0];
                java.util.Date fecha = (java.util.Date) fila[1];
                Double total = (Double) fila[2];
                
                System.out.println("N° Factura: " + numero + 
                                   " | Fecha: " + fecha + 
                                   " | Importe Total: $" + total);
            }
            System.out.println("--- Punto 3: Artículos por Rubro ('Electrónica') ---");
            List<Articulo> articulos = ConsultasNivel1y2.articulosPorRubro(em, "Electrónica");
            
            for (Articulo a : articulos) {
                System.out.println("Código: " + a.getCodigo() + 
                                   " | Denominación: " + a.getDenominacion() + 
                                   " | Rubro: " + a.getRubro().getDenominacion());
            }
            System.out.println("--- Punto 4: Facturas entre rango de fechas ---");
            // Definimos un rango amplio (ejemplo: todo el año actual) para que capture las facturas de prueba
            java.util.Calendar cal = java.util.Calendar.getInstance();
            
            cal.set(2025, java.util.Calendar.JANUARY, 1, 0, 0, 0);
            java.util.Date desde = cal.getTime();
            
            cal.set(2027, java.util.Calendar.DECEMBER, 31, 23, 59, 59);
            java.util.Date hasta = cal.getTime();

            List<FacturaVenta> facturasRango = ConsultasNivel1y2.facturasEntreFechas(em, desde, hasta);
            
            for (FacturaVenta f : facturasRango) {
                System.out.println("Factura N°: " + f.getNumero() + 
                                   " | Fecha: " + f.getFechaEmision() + 
                                   " | Total: $" + f.getImporteTotal());
            }
            System.out.println("--- Punto 5: Facturas EMITIDAS > $10.000 y NO anuladas ---");
            List<FacturaVenta> facturasFiltradas = ConsultasNivel1y2.facturasEmitidasMayoresANoAnuladas(em, 10000.0);
            
            for (FacturaVenta f : facturasFiltradas) {
                System.out.println("N° Factura: " + f.getNumero() + 
                                   " | Estado: " + f.getEstado() + 
                                   " | Total: $" + f.getImporteTotal() + 
                                   " | Fecha Anulación: " + f.getFechaAnulacion());
            }
            System.out.println("--- Punto 6: Clientes por texto parcial ('juan') o CUIT ('20-') ---");
            List<Cliente> clientes = ConsultasNivel1y2.buscarClientesPorNombreOCuit(em, "juan", "20-");
            
            for (Cliente c : clientes) {
                System.out.println("Denominación: " + c.getDenominacion() + 
                                   " | CUIT/CUIL: " + c.getCuitCuil());
            }
            System.out.println("--- Punto 7: Estados únicos de facturas ---");
            List<String> estados = ConsultasNivel1y2.estadosUnicosFacturas(em);
            for (String est : estados) {
                System.out.println("Estado: " + est);
            }

            System.out.println("--- Punto 8: Resumen Agregado (COUNT, SUM, AVG) ---");
            Object[] resumen = ConsultasNivel1y2.resumenAgregadoFacturasEmitidas(em, "EMITIDA");
            Long cantidad = (Long) resumen[0];
            Double suma = (Double) resumen[1];
            Double promedio = (Double) resumen[2];
            
            System.out.println("Cantidad Facturas: " + cantidad + 
                               " | Suma Total: $" + suma + 
                               " | Importe Promedio: $" + promedio);

            System.out.println("--- Punto 9: Puntos de Venta por lista [1, 2, 5] ---");
            List<PuntoVenta> puntosVenta = ConsultasNivel1y2.puntosVentaPorNumeros(em, Arrays.asList(1, 2, 5));
            for (PuntoVenta pv : puntosVenta) {
                System.out.println("N° PV: " + pv.getNumero() + 
                                   " | Descripción: " + pv.getDescripcion());
            }
        } finally {
            em.close();
            emf.close();
        }
    }
}