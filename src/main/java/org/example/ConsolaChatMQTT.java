package org.example;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.util.Scanner;

public class ConsolaChatMQTT {
    public static void main(String[] args) {
        // Define la ruta del escritorio de Windows para guardar los archivos de chat
        String rutaEscritorio = System.getProperty("user.home") + "\\Desktop\\chatMqtt\\";

        // Crea una instancia del cliente de chat MQTT con el usuario 'adrian' y la ruta del escritorio
        ChatClienteMQTT clienteChat = new ChatClienteMQTT("tcp://3.83.123.224:1883", "chat", "adrian", rutaEscritorio);
        try {
            // Conectar al servidor MQTT
            clienteChat.conectar();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }


        // Inicializar el comando para el bucle
        String comando = "";
        Scanner scanner = new Scanner(System.in);

        // Bucle principal para manejar comandos
        while (!comando.equals("exit")) {
            System.out.print("Ingrese comando: ");
            String comandoCrudo = scanner.nextLine() + " ";
            String[] partesComando = comandoCrudo.split(" ", 3);
            comando = partesComando[0];

            // Manejar comando 'send' para enviar mensajes
            if (comando.equals("send")) {
                try {
                    clienteChat.enviarMensaje(partesComando[1], partesComando[2]);
                    System.out.printf("Enviando %s a %s\r\n", partesComando[2], partesComando[1]);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            // Manejar comando 'chat' para ver el contenido del chat
            else if (comando.equals("chat")) {
                try {
                    System.out.println(clienteChat.obtenerChat(partesComando[1]));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // Cerrar la conexión MQTT al salir del programa
        try {
            clienteChat.cerrar();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}

