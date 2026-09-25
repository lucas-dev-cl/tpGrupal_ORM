package org.utn.consultas;

import jakarta.persistence.EntityManager;
import org.utn.entity.venta.FacturaVenta;
import org.utn.entity.catalogo.Articulo;
import org.utn.entity.cliente.Cliente;
import org.utn.entity.venta.PuntoVenta;

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
    /**
     * 3. Filtrado por Igualdad (WHERE).
     * Obtiene los artículos que pertenecen a un rubro por su denominación.
     */
    public static List<Articulo> articulosPorRubro(EntityManager em, String denominacionRubro) {
        String jpql = "SELECT a FROM Articulo a WHERE a.rubro.denominacion = :denominacion";
        return em.createQuery(jpql, Articulo.class)
                 .setParameter("denominacion", denominacionRubro)
                 .getResultList();
    }
    /**
     * 4. Filtrado por Rango de Fechas (BETWEEN).
     * Listar facturas de venta emitidas dentro de un rango determinado.
     */
    public static List<FacturaVenta> facturasEntreFechas(EntityManager em, java.util.Date fechaDesde, java.util.Date fechaHasta) {
        String jpql = "SELECT f FROM FacturaVenta f WHERE f.fechaEmision BETWEEN :fechaDesde AND :fechaHasta";
        return em.createQuery(jpql, FacturaVenta.class)
                 .setParameter("fechaDesde", fechaDesde)
                 .setParameter("fechaHasta", fechaHasta)
                 .getResultList();
    }
    /**
     * 5. Condicionales Complejos y Verificación de Nulos (AND, OR, IS NULL).
     * Facturas en estado "EMITIDA", con importeTotal > $10,000 y no anuladas.
     */
    public static List<FacturaVenta> facturasEmitidasMayoresANoAnuladas(EntityManager em, double montoMinimo) {
        String jpql = "SELECT f FROM FacturaVenta f " +
                      "WHERE f.estado = :estado " +
                      "  AND f.importeTotal > :montoMinimo " +
                      "  AND f.fechaAnulacion IS NULL";
        
        return em.createQuery(jpql, FacturaVenta.class)
                 .setParameter("estado", "EMITIDA")
                 .setParameter("montoMinimo", montoMinimo)
                 .getResultList();
    }
    /**
     * 6. Búsqueda por Patrón de Texto (LIKE y LOWER).
     * Busca clientes por denominación parcial (case-insensitive) o cuyo CUIT empiece por un prefijo.
     */
    public static List<Cliente> buscarClientesPorNombreOCuit(EntityManager em, String textoNombre, String prefijoCuit) {
        String jpql = "SELECT c FROM Cliente c " +
                      "WHERE LOWER(c.denominacion) LIKE LOWER(:denominacion) " +
                      "   OR c.cuitCuil LIKE :cuitPrefix";
        
        return em.createQuery(jpql, Cliente.class)
                 .setParameter("denominacion", "%" + textoNombre + "%")
                 .setParameter("cuitPrefix", prefijoCuit + "%")
                 .getResultList();
    }
    /**
     * 7. Valores Distintos y Ordenamiento (DISTINCT y ORDER BY).
     * Obtiene los estados únicos registrados en las facturas, ordenados alfabéticamente.
     */
    public static List<String> estadosUnicosFacturas(EntityManager em) {
        String jpql = "SELECT DISTINCT f.estado FROM FacturaVenta f ORDER BY f.estado ASC";
        return em.createQuery(jpql, String.class).getResultList();
    }

    /**
     * 8. Funciones de Agregación Simples (COUNT, SUM, AVG).
     * Devuelve cantidad, suma y promedio de importes totales en un único arreglo Object[].
     */
    public static Object[] resumenAgregadoFacturasEmitidas(EntityManager em, String estado) {
        String jpql = "SELECT COUNT(f), SUM(f.importeTotal), AVG(f.importeTotal) " +
                      "FROM FacturaVenta f WHERE f.estado = :estado";
        
        return em.createQuery(jpql, Object[].class)
                 .setParameter("estado", estado)
                 .getSingleResult();
    }

    /**
     * 9. Operador de Inclusión (IN).
     * Obtiene los puntos de venta cuyo número coincida con la lista recibida por parámetro.
     */
    public static List<PuntoVenta> puntosVentaPorNumeros(EntityManager em, List<Integer> numeros) {
        String jpql = "SELECT pv FROM PuntoVenta pv WHERE pv.numero IN :numeros";
        return em.createQuery(jpql, PuntoVenta.class)
                 .setParameter("numeros", numeros)
                 .getResultList();
    }
}