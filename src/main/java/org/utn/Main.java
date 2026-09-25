package org.utn;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.utn.entity.articulo.*;
import org.utn.entity.usuario.Usuario;
import org.utn.entity.venta.FacturaVenta;
import org.utn.entity.venta.FacturaVentaDetalle;
import org.utn.entity.venta.PuntoVenta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   EJECUCIÓN GENERAL - TRABAJO PRÁCTICO JPQL");
        System.out.println("==================================================");

        // 1. Ejecutar consultas y pruebas de los Niveles 1 y 2
        System.out.println("\n==================================================");
        System.out.println(">>> EJECUTANDO NIVELES 1 Y 2 (Consultas 1 a 9)");
        System.out.println("==================================================");
        PruebaNivel1y2.main(args);

        // 2. Ejecutar consultas y pruebas de los Niveles 4 y 5
        System.out.println("\n==================================================");
        System.out.println(">>> EJECUTANDO NIVELES 4 Y 5 (Consultas 16 a 22)");
        System.out.println("==================================================");
        PruebaNivel4y5.main(args);
    }
}