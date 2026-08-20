/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.nodo2;

/**
 *
 * @author luchop
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.InetAddress;

/**
 *
 * @author luchop
 */
public class Nodo2 {
    public static void main(String args[]) { 
        int port = 5001;  
        
        String ipND3 = "10.25.75.46";
        int puertoND3=5002;
        try {
            DatagramSocket socketUDP = new DatagramSocket(port);
            byte[] bufer = new byte[1000];

            System.out.println("Nodo 2 iniciado en el puerto " + port);

            while (true) {
                DatagramPacket peticion = new DatagramPacket(bufer, bufer.length);
                socketUDP.receive(peticion);
                
                System.out.print("Datagrama recibido del host: " + peticion.getAddress());
                System.out.println(" desde el puerto del Nodo1: " + peticion.getPort());

                // Se extrae ÚNICAMENTE el fragmento de bytes recibido
                String cadena = new String(peticion.getData(), 0, peticion.getLength()).trim();
                String cadenaProcesada = procesar(cadena);
                byte[] mensaje = cadenaProcesada.getBytes();
                InetAddress destinoNodo3 = InetAddress.getByName(ipND3);

                // Usamos mensaje.length para los bytes exactos
                DatagramPacket respuestaND3 = new DatagramPacket(
                    mensaje, 
                    mensaje.length,
                    destinoNodo3, 
                    puertoND3
                );

                // Enviamos la respuesta
                socketUDP.send(respuestaND3);
                System.out.println("Informacion procesada y enviada al nodo 3");
            }

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }

    public static String procesar(String cadena) {
        // Separamos la información que viene del Nodo 1 usando '|'
        // Se usa "\\|" porque en Java '|' es un carácter especial de regex
        String[] partes = cadena.split("\\|");

        // Validación si no llega en el formato esperado
        if (partes.length < 2) {
            return "ERROR|0|0|Indeterminado";
        }

        String cadenaOriginal = partes[0];
        int numCaracteres = Integer.parseInt(partes[1]);

        // A. Contar cantidad de palabras
        int numPalabras = 0;
        if (!cadenaOriginal.trim().isEmpty()) {
            numPalabras = cadenaOriginal.trim().split("\\s+").length;
        }

        // B. Determinar si la cantidad de caracteres es Par o Impar
        String paridad = (numCaracteres % 2 == 0) ? "Par" : "Impar";

        // C. Unir todo para el Nodo 3: textoOriginal | numCaracteres | numPalabras | paridad
        return cadenaOriginal + "|" + numCaracteres + "|" + numPalabras + "|" + paridad;
    }
}

