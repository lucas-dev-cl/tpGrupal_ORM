package org.utn.entity.cliente;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.utn.entity.base.EntityId;

@Entity
@Table(name = "domicilio")
@Getter
@Setter
@NoArgsConstructor
public class Domicilio extends EntityId {
    private String nombreCalle;
    private String numeroCalle;
}
