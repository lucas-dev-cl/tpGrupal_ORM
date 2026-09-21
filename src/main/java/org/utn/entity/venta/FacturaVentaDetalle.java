package org.utn.entity.venta;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.utn.entity.base.EntityId;
import org.utn.entity.articulo.ListaPrecioArticulo;

@Entity
@Table(name = "factura_venta_detalle")
@NamedQuery(
    name = "FacturaVentaDetalle.findByPuntoVenta",
    query = "SELECT d FROM FacturaVentaDetalle d INNER JOIN d.factura f INNER JOIN f.puntoVenta p WHERE p.id = :puntoVentaId"
)
@Getter
@Setter
@NoArgsConstructor
public class FacturaVentaDetalle extends EntityId {

    @ManyToOne
    @JoinColumn(name = "factura_id", nullable = false)
    private FacturaVenta factura;

    @ManyToOne
    @JoinColumn(name = "lista_precio_articulo_id", nullable = false)
    private ListaPrecioArticulo listaPrecioArticulo;

    private String descripcion;

    @Column(nullable = false)
    private double cantidad;

    @Column(nullable = false)
    private double precioUnitario;

    private double porcentajeBonificacion;
    private double importeNeto;
    private double importeIva;

    @Column(nullable = false)
    private double importeSubtotal;
}
