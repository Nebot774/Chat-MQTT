package org.example;

import java.time.LocalDateTime;

public class Mensaje {
    private String emisor;
    private String texto;
    private LocalDateTime timestamp;

    // Constructor
    public Mensaje(String emisor, String texto, LocalDateTime timestamp) {
        this.emisor = emisor;
        this.texto = texto;
        this.timestamp = timestamp;
    }

    // Getter para el emisor
    public String getEmisor() {
        return emisor;
    }

    // Setter para el emisor
    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    // Getter para el texto
    public String getTexto() {
        return texto;
    }

    // Setter para el texto
    public void setTexto(String texto) {
        this.texto = texto;
    }

    // Getter para el timestamp
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Setter para el timestamp
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

