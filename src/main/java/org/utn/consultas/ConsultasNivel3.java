package org.utn.consultas;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.utn.entity.venta.FacturaVenta;
import org.utn.entity.venta.FacturaVentaDetalle;

import java.util.List;

public class ConsultasNivel3 {

    private final EntityManager em;

    public ConsultasNivel3(EntityManager em) {
        this.em = em;
    }

    /**
     * Ejercicio 10: Busca y retorna la lista de facturas de venta registradas por un usuario específico.
     * Utiliza un parámetro nombrado (:nombreUsuario) para prevenir inyecciones SQL.
     *
     * @param nombreUsuario Nombre de usuario encargado de la carga.
     * @return Lista de entidades FacturaVenta cargadas por dicho usuario.
     */
    public List<FacturaVenta> findByUsuarioCarga(String nombreUsuario) {
        String jpql = "SELECT f FROM FacturaVenta f WHERE f.usuarioCarga.usuario = :nombreUsuario";

        TypedQuery<FacturaVenta> query = em.createQuery(jpql, FacturaVenta.class);
        query.setParameter("nombreUsuario", nombreUsuario);

        return query.getResultList();
    }

    /**
     * Ejercicio 11: Busca y retorna todos los detalles de facturas de venta asociados a un punto de venta específico.
     * Utiliza una consulta JPQL con INNER JOINs para navegar desde el detalle de la factura (d)
     * hacia la factura (f) y su correspondiente punto de venta (p).
     */
    public List<FacturaVentaDetalle> findByPuntoVenta(Long puntoVentaId) {
        String jpql = "SELECT d FROM FacturaVentaDetalle d " +
                "INNER JOIN d.factura f " +
                "INNER JOIN f.puntoVenta p " +
                "WHERE p.id = :puntoVentaId";

        TypedQuery<FacturaVentaDetalle> query = em.createQuery(jpql, FacturaVentaDetalle.class);
        query.setParameter("puntoVentaId", puntoVentaId);

        return query.getResultList();
    }

    /**
     * Ejercicio 12: Retorna lista de arrays de Object donde cada posición contiene:
     * [0] = denominación del artículo (String)
     * [1] = denominación de la marca (String, o null si no posee)
     */
    public List<Object[]> findAllWithMarca() {
        String jpql = "SELECT a.denominacion, m.denominacion FROM Articulo a LEFT JOIN a.marca m";

        TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);

        return query.getResultList();
    }

    /**
     * Ejercicio 13: Navegación multinivel: FacturaVenta -> detalles -> listaPrecioArticulo -> articulo -> marca.
     * Busca facturas que contengan artículos de una marca específica.
     * Usa DISTINCT para evitar facturas duplicadas si contienen múltiples detalles vinculados a la misma marca.
     *
     * @param marcaId Identificador único de la marca a filtrar.
     * @return Lista de entidades FacturaVenta únicas asociadas a la marca.
     */
    public List<FacturaVenta> findByMarcaArticulo(Long marcaId) {
        String jpql = "SELECT DISTINCT f FROM FacturaVenta f " +
                "JOIN f.detalles d " +
                "JOIN d.listaPrecioArticulo lpa " +
                "JOIN lpa.articulo a " +
                "JOIN a.marca m " +
                "WHERE m.id = :marcaId";

        TypedQuery<FacturaVenta> query = em.createQuery(jpql, FacturaVenta.class);
        query.setParameter("marcaId", marcaId);

        return query.getResultList();
    }

    /**
     * Ejercicio 14: Subconsulta en WHERE: busca facturas cuyo importeTotal sea mayor al promedio general de todas las facturas.
     * La subconsulta calcula AVG(importeTotal) y se compara dinámicamente con cada registro.
     *
     * @return Lista de entidades FacturaVenta que superan el importe promedio.
     */
    public List<FacturaVenta> findByImporteMayorAlPromedio() {
        String jpql = "SELECT f FROM FacturaVenta f " +
                "WHERE f.importeTotal > (SELECT AVG(f2.importeTotal) FROM FacturaVenta f2)";

        TypedQuery<FacturaVenta> query = em.createQuery(jpql, FacturaVenta.class);

        return query.getResultList();
    }

    /**
     * Ejercicio 15: Filtrado por cliente: busca facturas asociadas a un cliente específico mediante su CUIT/CUIL.
     * Navega por la relación FacturaVenta -> cliente -> cuitCuil utilizando un parámetro nombrado (:cuitCuil).
     *
     * @param cuitCuil CUIT o CUIL del cliente a consultar.
     * @return Lista de entidades FacturaVenta pertenecientes al cliente indicado.
     */
    public List<FacturaVenta> findByClienteCuitCuil(String cuitCuil) {
        String jpql = "SELECT f FROM FacturaVenta f WHERE f.cliente.cuitCuil = :cuitCuil";

        TypedQuery<FacturaVenta> query = em.createQuery(jpql, FacturaVenta.class);
        query.setParameter("cuitCuil", cuitCuil);

        return query.getResultList();
    }
}
