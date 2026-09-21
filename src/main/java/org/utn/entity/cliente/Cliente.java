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

    // Dato obligatorio para emitir comprobantes validos ante AFIP.
    // Sin cascada: muchos clientes comparten la misma CondicionIva,
    // no le pertenece a un cliente en particular.
    @ManyToOne
    @JoinColumn(name = "condicion_iva_id", nullable = false)
    private CondicionIva condicionIva;

    // Moneda en la que opera habitualmente el cliente. Sin cascada
    // por el mismo motivo que CondicionIva (catalogo compartido).
    @ManyToOne
    @JoinColumn(name = "tipo_moneda_id")
    private TipoMoneda tipoMoneda;
}
