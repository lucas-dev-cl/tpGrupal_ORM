package org.utn.entity.venta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.utn.entity.base.AuditoriaApp;

@Entity
@Table(name = "punto_venta")
@Getter
@Setter
@NoArgsConstructor
public class PuntoVenta extends AuditoriaApp {

    @Column(nullable = false)
    private int numero;

    private String descripcion;
    private String tipoEmision;
    private String domicilioComercial;
}
