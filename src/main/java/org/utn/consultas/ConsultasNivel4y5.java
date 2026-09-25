package org.utn.consultas;

import jakarta.persistence.EntityManager;
import org.utn.entity.catalogo.Articulo;
import org.utn.entity.catalogo.Marca;

import java.util.List;

/**
 * TP Grupal JPQL - Niveles 4 y 5 (consultas 16 a 22).
 * Todas las consultas usan nombres de entidades y atributos Java, no de tablas.
 * Las consultas con varias columnas devuelven Object[] (una fila = un arreglo).
 */
public class ConsultasNivel4y5 {

    // ===================== NIVEL 4: GROUP BY / HAVING =====================

    /** 16. Descripción del punto de venta, cantidad de facturas y total facturado. */
    public static List<Object[]> facturacionPorPuntoVenta(EntityManager em) {
        String jpql =
                "SELECT pv.descripcion, COUNT(f), SUM(f.importeTotal) " +
                        "FROM FacturaVenta f " +
                        "JOIN f.puntoVenta pv " +
                        "GROUP BY pv.id, pv.descripcion";
        return em.createQuery(jpql, Object[].class).getResultList();
    }

    /** 17. Usuarios de carga con más de :minimo facturas (la consigna pide 5). */
    public static List<String> usuariosConMasDeNFacturas(EntityManager em, long minimo) {
        String jpql =
                "SELECT u.usuario " +
                        "FROM FacturaVenta f " +
                        "JOIN f.usuarioCarga u " +          // usuarioCarga viene heredado de AuditoriaApp
                        "GROUP BY u.id, u.usuario " +
                        "HAVING COUNT(f) > :minimo";
        return em.createQuery(jpql, String.class)
                .setParameter("minimo", minimo)
                .getResultList();
    }

    /**
     * 18. Por marca: unidades vendidas y subtotal acumulado.
     * El detalle NO apunta directo al artículo: pasa por ListaPrecioArticulo.
     * Con JOIN interno quedan afuera los artículos sin marca.
     */
    public static List<Object[]> ventasPorMarca(EntityManager em) {
        String jpql =
                "SELECT m.denominacion, SUM(d.cantidad), SUM(d.importeSubtotal) " +
                        "FROM FacturaVentaDetalle d " +
                        "JOIN d.listaPrecioArticulo lpa " +
                        "JOIN lpa.articulo a " +
                        "JOIN a.marca m " +
                        "GROUP BY m.id, m.denominacion";
        return em.createQuery(jpql, Object[].class).getResultList();
    }

    /**
     * 19. Total facturado por condición de IVA.
     * ATENCIÓN: con el modelo actual esta consulta NO se puede ejecutar, porque
     * FacturaVenta no tiene 'cliente' y Cliente no tiene 'condicionIva'.
     * Está escrita para el modelo corregido:
     *   FacturaVenta -> @ManyToOne Cliente cliente
     *   Cliente      -> @ManyToOne CondicionIva condicionIva
     */
    public static List<Object[]> facturacionPorCondicionIva(EntityManager em) {
        String jpql =
                "SELECT ci.denominacion, SUM(f.importeTotal) " +
                        "FROM FacturaVenta f " +
                        "JOIN f.cliente c " +
                        "JOIN c.condicionIva ci " +
                        "GROUP BY ci.id, ci.denominacion";
        return em.createQuery(jpql, Object[].class).getResultList();
    }

    // ===================== NIVEL 5: EXISTS / NOT EXISTS / CASE =====================

    /** 20. Marcas con al menos un artículo facturado. */
    public static List<Marca> marcasConArticulosFacturados(EntityManager em) {
        String jpql =
                "SELECT m FROM Marca m " +
                        "WHERE EXISTS (" +
                        "  SELECT d.id FROM FacturaVentaDetalle d " +
                        "  JOIN d.listaPrecioArticulo lpa " +
                        "  JOIN lpa.articulo a " +
                        "  WHERE a.marca = m)";
        return em.createQuery(jpql, Marca.class).getResultList();
    }

    /** 21. Artículos que nunca aparecen en un detalle de factura. */
    public static List<Articulo> articulosNuncaFacturados(EntityManager em) {
        String jpql =
                "SELECT a FROM Articulo a " +
                        "WHERE NOT EXISTS (" +
                        "  SELECT d.id FROM FacturaVentaDetalle d " +
                        "  JOIN d.listaPrecioArticulo lpa " +
                        "  WHERE lpa.articulo = a)";
        return em.createQuery(jpql, Articulo.class).getResultList();
    }

    /** 22. Número, importe y categoría calculada, de mayor a menor importe. */
    public static List<Object[]> facturasPorCategoria(EntityManager em) {
        String jpql =
                "SELECT f.numero, f.importeTotal, " +
                        "  CASE WHEN f.importeTotal > 50000 THEN 'ALTO VALOR' " +
                        "       WHEN f.importeTotal >= 10000 THEN 'MEDIO VALOR' " +
                        "       ELSE 'BAJO VALOR' END AS categoria " +
                        "FROM FacturaVenta f " +
                        "ORDER BY f.importeTotal DESC";
        return em.createQuery(jpql, Object[].class).getResultList();
    }

    // ===================== Prueba rápida desde el Main =====================

    public static void ejecutarTodas(EntityManager em) {
        System.out.println("--- 16. Por punto de venta");
        imprimir(facturacionPorPuntoVenta(em));

        System.out.println("--- 17. Usuarios con más de 5 facturas");
        usuariosConMasDeNFacturas(em, 5).forEach(System.out::println);

        System.out.println("--- 18. Por marca");
        imprimir(ventasPorMarca(em));

        System.out.println("--- 19. Por condición de IVA");
        imprimir(facturacionPorCondicionIva(em));

        System.out.println("--- 20. Marcas con artículos facturados");
        marcasConArticulosFacturados(em).forEach(m -> System.out.println(m.getDenominacion()));

        System.out.println("--- 21. Artículos nunca facturados");
        articulosNuncaFacturados(em).forEach(a -> System.out.println(a.getDenominacion()));

        System.out.println("--- 22. Facturas por categoría");
        imprimir(facturasPorCategoria(em));
    }

    private static void imprimir(List<Object[]> filas) {
        for (Object[] fila : filas) {
            StringBuilder sb = new StringBuilder();
            for (Object col : fila) sb.append(col).append(" | ");
            System.out.println(sb);
        }
    }
}
