package org.utn.entity.repositorio;

import jakarta.persistence.EntityManager;
import org.utn.entity.venta.FacturaVentaDetalle;

import java.util.List;

public class FacturaVentaDetalleRepositorio {

    private EntityManager em;

    public FacturaVentaDetalleRepositorio(EntityManager em) {
        this.em = em;
    }

    public List<FacturaVentaDetalle> findByPuntoVenta(Long puntoVentaId) {
        return em.createNamedQuery("FacturaVentaDetalle.findByPuntoVenta", FacturaVentaDetalle.class)
                .setParameter("puntoVentaId", puntoVentaId)
                .getResultList();
    }
}
