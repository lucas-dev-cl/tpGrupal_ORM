package org.utn.entity.venta;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.utn.entity.base.AuditoriaApp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "factura_venta")
@NamedQueries({
    @NamedQuery(
        name = "FacturaVenta.findByUsuarioCarga",
        query = "SELECT f FROM FacturaVenta f WHERE f.usuarioCarga.usuario = :nombreUsuario"
    ),
    @NamedQuery(
        name = "FacturaVenta.findByMarcaArticulo",
        query = "SELECT DISTINCT f FROM FacturaVenta f JOIN f.detalles d JOIN d.listaPrecioArticulo lpa JOIN lpa.articulo a JOIN a.marca m WHERE m.id = :marcaId"
    ),
    @NamedQuery(
        name = "FacturaVenta.findByImporteMayorAlPromedio",
        query = "SELECT f FROM FacturaVenta f WHERE f.importeTotal > (SELECT AVG(f2.importeTotal) FROM FacturaVenta f2)"
    ),
    @NamedQuery(
        name = "FacturaVenta.findByClienteCuitCuil",
        query = "SELECT f FROM FacturaVenta f WHERE f.cliente.cuitCuil = :cuitCuil"
    )
})
@Getter
@Setter
@NoArgsConstructor
public class FacturaVenta extends AuditoriaApp {

    private Long numero;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;

    @ManyToOne
    @JoinColumn(name = "punto_venta_id", nullable = false)
    private PuntoVenta puntoVenta;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private org.utn.entity.cliente.Cliente cliente;

    private double importeCobrado;
    private double importeSaldo;

    @Column(nullable = false)
    private double importeTotal;

    private String cae;

    @Temporal(TemporalType.TIMESTAMP)
    private Date caeFechaVencimiento;

    private String resultadoAfip;
    private String motivoRechazo;

    @Column(nullable = false)
    private String estado;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnulacion;

    private String observaciones;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacturaVentaDetalle> detalles;
}
