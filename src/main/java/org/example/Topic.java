package org.example;

public class Topic {
    private String nombre;
    private Chat chat;

    public Topic(String nombre) {
        this.nombre = nombre;
        this.chat = new Chat(nombre + ".txt");
    }

    // Métodos para manejar mensajes
}

