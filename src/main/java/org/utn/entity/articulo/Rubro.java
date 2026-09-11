package org.utn.entity.articulo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.utn.entity.base.AuditoriaApp;

@Entity
@Table(name = "rubro")
@Getter
@Setter
@NoArgsConstructor
public class Rubro extends AuditoriaApp {

    @Column(nullable = false)
    private String denominacion;

    @Column(nullable = false)
    private Integer codigo;
}
