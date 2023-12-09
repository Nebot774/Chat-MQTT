package org.example;

import org.eclipse.paho.client.mqttv3.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.stream.Collectors;

public class ChatClienteMQTT {
    // Cliente MQTT para manejar la conexión y las operaciones
    MqttClient clienteMQTT;
    String nombreUsuario;
    String baseUrl;
    String rutaChat;

    // Constructor para inicializar el cliente MQTT
    public ChatClienteMQTT(String urlServidor, String baseURL, String nombreUsuario, String rutaChat) {
        this.nombreUsuario = nombreUsuario;
        this.baseUrl = baseURL;
        this.rutaChat = rutaChat;
        String idPublicador = UUID.randomUUID().toString();
        try {
            this.clienteMQTT = new MqttClient(urlServidor, idPublicador);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    // Método para conectar al servidor MQTT
    public void conectar() throws MqttException {
        MqttConnectOptions opciones = new MqttConnectOptions();
        opciones.setAutomaticReconnect(true);
        opciones.setCleanSession(true);
        opciones.setConnectionTimeout(5);
        clienteMQTT.connect(opciones);
        establecerCallbacks();
    }

    // Método para cerrar la conexión MQTT
    public void cerrar() throws MqttException {
        clienteMQTT.disconnect();
        clienteMQTT.close();
    }

    // Método para enviar un mensaje a un usuario específico
    public void enviarMensaje(String usuario, String mensaje) throws MqttException {
        if (clienteMQTT.isConnected()) {
            String tema = obtenerTemaChat(usuario);
            byte[] payload = mensaje.getBytes();
            MqttMessage msg = new MqttMessage(payload);
            msg.setQos(0);
            msg.setRetained(true);
            clienteMQTT.publish(tema, msg);
        }
    }

    // Método para obtener el chat de un usuario específico
    public String obtenerChat(String usuario) throws IOException {
        Path archivo = Path.of(rutaChat, "chat" + usuario);
        BufferedReader br = Files.newBufferedReader(archivo);
        return br.lines().collect(Collectors.joining("\r\n"));
    }

    // Establecer los callbacks para el cliente MQTT
    private void establecerCallbacks() throws MqttException {
        clienteMQTT.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                // Aquí se manejaría la pérdida de conexión
            }

            @Override
            public void messageArrived(String tema, MqttMessage mensajeMQTT) throws Exception {
                escribirMensajeRecibido(tema, mensajeMQTT);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken tokenEntrega) {
                try {
                    String tema = tokenEntrega.getTopics()[0];
                    MqttMessage mensajeMQTT = tokenEntrega.getMessage();
                    escribirMensajeEnviado(tema, mensajeMQTT);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // Suscribirse a los temas necesarios
        String[] temas = new String[]{"/%s/todos".formatted(baseUrl), "/%s/+/%s".formatted(baseUrl, nombreUsuario)};
        clienteMQTT.subscribe(temas, new int[]{0, 0});
    }

    // Obtener el tema para enviar un chat
    private String obtenerTemaChat(String otroUsuario) {
        String tema;
        if (otroUsuario.equals("todos")) {
            tema = String.format("/%s/%s", baseUrl, otroUsuario);
        } else {
            tema = String.format("/%s/%s/%s", baseUrl, nombreUsuario, otroUsuario);
        }
        return tema;
    }

    // Método para escribir un mensaje recibido en un archivo
    private void escribirMensajeRecibido(String tema, MqttMessage mensajeMQTT) throws IOException {
        // Cambios aquí para usar el nombre 'chatMqtt'
        String[] divisionTema = tema.split("/");
        Path archivo = Path.of(rutaChat, "chatMqtt_" + divisionTema[1] + "_" + divisionTema[2] + ".txt");
        BufferedWriter bw = Files.newBufferedWriter(archivo, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        bw.write("%s (%s): %s".formatted(divisionTema[2], LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM HH:mm")), mensajeMQTT.toString()));
        bw.newLine();
        bw.close();
    }

    // Método para escribir un mensaje enviado en un archivo
    private void escribirMensajeEnviado(String tema, MqttMessage mensajeMQTT) throws IOException {
        // Cambios similares aquí para usar el nombre 'chatMqtt'
        String[] divisionTema = tema.split("/");
        if (!divisionTema[2].equals("todos")) {
            Path archivo = Path.of(rutaChat, "chatMqtt_" + divisionTema[1] + "_" + divisionTema[3] + ".txt");
            BufferedWriter bw = Files.newBufferedWriter(archivo, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            bw.write("%s (%s): %s".formatted(nombreUsuario, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM HH:mm")), mensajeMQTT.toString()));
            bw.newLine();
            bw.close();
        }
    }
}