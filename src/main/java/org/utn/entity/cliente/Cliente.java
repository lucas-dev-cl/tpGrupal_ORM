package org.utn.entity.cliente;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.utn.entity.base.AuditoriaApp;
import org.utn.entity.venta.TipoMoneda;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
public class Cliente extends AuditoriaApp {


    @Column(nullable = false)
    private String cuitCuil;

    @Column(nullable = false)
    private String denominacion;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contacto_id", nullable = false)
    private Contacto contacto;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "domicilio_id", nullable = false)
    private Domicilio domicilio;

    @ManyToOne
    @JoinColumn(name = "condicion_iva_id", nullable = false)
    private CondicionIva condicionIva;
}
