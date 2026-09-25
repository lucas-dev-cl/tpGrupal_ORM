package org.utn.entity.repositorio;

import jakarta.persistence.EntityManager;
import java.util.List;

public class ArticuloRepositorio {

    private EntityManager em;

    public ArticuloRepositorio(EntityManager em) {
        this.em = em;
    }

    /**
     * Retorna lista de arrays donde cada array contiene:
     * [0] = denominación del artículo
     * [1] = denominación de la marca (puede ser null)
     */
    public List<Object[]> findAllWithMarca() {
        return em.createNamedQuery("Articulo.findAllWithMarca")
                .getResultList();
    }
}
