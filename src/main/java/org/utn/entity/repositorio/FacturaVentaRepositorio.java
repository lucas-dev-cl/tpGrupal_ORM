package org.utn.entity.repositorio;

import jakarta.persistence.EntityManager;
import org.utn.entity.venta.FacturaVenta;

import java.util.List;

public class FacturaVentaRepositorio {

    private EntityManager em;

    public FacturaVentaRepositorio(EntityManager em) {
        this.em = em;
    }

    // El nombre de FacturaVenta.findByUsuarioCarga es el nombre de la consulta, esto es totalmente convencional y buenas prácticas
    public List<FacturaVenta> findByUsuarioCarga(String nombreUsuario) {
        return em.createNamedQuery("FacturaVenta.findByUsuarioCarga", FacturaVenta.class)
                .setParameter("nombreUsuario", nombreUsuario) // Acá le pasamos el valor a la variable dentro de la consulta que se llama 'nombreUsuario'
                .getResultList();
    }

    /**
     * Navegación multinivel: FacturaVenta -> detalles -> listaPrecioArticulo -> articulo -> marca
     * Usa DISTINCT para evitar facturas duplicadas si tienen múltiples detalles de la misma marca
     */
    public List<FacturaVenta> findByMarcaArticulo(Long marcaId) {
        return em.createNamedQuery("FacturaVenta.findByMarcaArticulo", FacturaVenta.class)
                .setParameter("marcaId", marcaId)
                .getResultList();
    }

    /**
     * Subconsulta en WHERE: busca facturas con importeTotal mayor al promedio de todas las facturas
     * La subconsulta calcula AVG(importeTotal) y se compara con cada factura
     */
    public List<FacturaVenta> findByImporteMayorAlPromedio() {
        return em.createNamedQuery("FacturaVenta.findByImporteMayorAlPromedio", FacturaVenta.class)
                .getResultList();
    }

    /**
     * Filtrado por cliente: busca facturas asociadas a un cliente específico por su CUIT/CUIL
     * Navega por la relación FacturaVenta -> cliente -> cuitCuil
     */
    public List<FacturaVenta> findByClienteCuitCuil(String cuitCuil) {
        return em.createNamedQuery("FacturaVenta.findByClienteCuitCuil", FacturaVenta.class)
                .setParameter("cuitCuil", cuitCuil)
                .getResultList();
    }
}