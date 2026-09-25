package org.utn.entity.articulo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.utn.entity.base.AuditoriaApp;

@Entity
@Table(name = "articulo")
@NamedQuery(
    name = "Articulo.findAllWithMarca",
    query = "SELECT a.denominacion, m.denominacion FROM Articulo a LEFT JOIN a.marca m"
    /**
     * LEFT JOIN: Retorna todos los artículos, incluso aquellos que no tienen marca asignada.
     * Si un artículo no tiene marca, el campo de marca será NULL en el resultado.
     * A diferencia del INNER JOIN que solo retorna registros con coincidencias en ambas tablas.
     */
)
@Getter
@Setter
@NoArgsConstructor
public class Articulo extends AuditoriaApp {

    @ManyToOne
    @JoinColumn(name = "rubro_id")
    private Rubro rubro;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String denominacion;

    @ManyToOne
    @JoinColumn(name = "marca_id")
    private Marca marca;
}
