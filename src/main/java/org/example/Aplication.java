package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Aplication {
    public static void main(String[] args) {
        Mensajeria mensajeria = new Mensajeria();
        Scanner scanner = new Scanner(System.in);

        // Ejemplo de cómo inicializar la lista de alumnos
        List<String> alumnos = Arrays.asList("Juan", "David", "Maria", "Jose");

        // Suscribir alumnos
        mensajeria.suscribirAlumnos(alumnos);

        while (true) {
            // Lógica para manejar comandos de la consola
        }
    }
}

