package org.example;

import java.io.*;
import java.util.*;

public class Chat {
    private String nombreArchivo;

    public Chat(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public void guardarMensaje(Mensaje mensaje) {
        // Implementar lógica para guardar el mensaje en el archivo
    }

    public List<Mensaje> leerMensajes() {
        // Implementar lógica para leer mensajes del archivo
        return new ArrayList<>();
    }
}

