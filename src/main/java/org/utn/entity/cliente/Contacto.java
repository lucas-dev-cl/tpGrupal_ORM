package org.utn.entity.cliente;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.utn.entity.base.EntityId;

@Entity
@Table(name = "contacto", schema = "clientes")
@Getter
@Setter
@NoArgsConstructor
public class Contacto extends EntityId {
    private String email;
    private String telefono;
    private String celular;
}
