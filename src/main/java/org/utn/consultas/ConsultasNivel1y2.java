package org.utn.consultas;

import jakarta.persistence.EntityManager;
import org.utn.entity.venta.FacturaVenta;

import java.util.List;

/**
 * TP Grupal JPQL - Niveles 1 y 2 (consultas 1 a 9).
 */
public class ConsultasNivel1y2 {

    /**
     * 1. Consulta de Entidades Completas.
     * Obtiene la lista completa de facturas de venta.
     */
    public static List<FacturaVenta> todasLasFacturas(EntityManager em) {
        String jpql = "SELECT f FROM FacturaVenta f";
        return em.createQuery(jpql, FacturaVenta.class).getResultList();
    }
    /**
     * 2. Proyección de Atributos Específicos.
     * Selecciona únicamente el número, la fecha de emisión y el importe total de las facturas.
     */
    public static List<Object[]> proyeccionFacturas(EntityManager em) {
        String jpql = "SELECT f.numero, f.fechaEmision, f.importeTotal FROM FacturaVenta f";
        return em.createQuery(jpql, Object[].class).getResultList();
    }
    
}