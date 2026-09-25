package org.utn;

import org.utn.Pruebas.PruebaNivel1y2;
import org.utn.Pruebas.PruebaNivel3;
import org.utn.Pruebas.PruebaNivel4y5;

public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   EJECUCIÓN GENERAL - TRABAJO PRÁCTICO JPQL");
        System.out.println("==================================================");

        // 1. Ejecutar consultas y pruebas de los Niveles 1 y 2
        // PruebaNivel1y2.ejecutar();

        // 3. Ejecutar consultas y pruebas del Nivel 3
        PruebaNivel3.ejecutar();

        // 2. Ejecutar consultas y pruebas de los Niveles 4 y 5
        // PruebaNivel4y5.ejecutar();
    }
}