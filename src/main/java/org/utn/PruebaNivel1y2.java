package org.utn;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.utn.consultas.ConsultasNivel1y2;
import org.utn.entity.venta.FacturaVenta;

import java.util.List;

public class PruebaNivel1y2 {

    public static void main(String[] args) {
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
        } finally {
            em.close();
            emf.close();
        }
    }
}