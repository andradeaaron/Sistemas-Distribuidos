/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.nado1;

/**
 *
 * @author andradeaaron39
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Nado1 {

    public static void main(String[] args) {

        int puertoNodo1 = 5000;

        int puertoNodo2 = 5001;

        Scanner sc = new Scanner(System.in);

        try {

            DatagramSocket socketUDP =
                    new DatagramSocket(puertoNodo1);

            System.out.println("Nodo1");

            System.out.print("Introduzca una palabra o frase: ");

            String texto = sc.nextLine();

            int cantidadCaracteres = texto.length();

            System.out.println("Cantidad de caracteres: "
                    + cantidadCaracteres);

            String mensaje =
                    texto + "|" + cantidadCaracteres;

            byte[] datos =
                    mensaje.getBytes();

            InetAddress hostNodo2 =
                    InetAddress.getByName("localhost");

            DatagramPacket peticion =
                    new DatagramPacket(
                            datos,
                            datos.length,
                            hostNodo2,
                            puertoNodo2
                    );

            socketUDP.send(peticion);

            System.out.println(
                    "\nInformacion enviada Nodo 2..."
            );

            byte[] buffer = new byte[2000];

            DatagramPacket respuesta =
                    new DatagramPacket(
                            buffer,
                            buffer.length
                    );

            System.out.println(
                    "Esperando respuesta Nodo 3..."
            );

            socketUDP.receive(respuesta);

            String resultado =
                    new String(
                            respuesta.getData(),
                            0,
                            respuesta.getLength()
                    );

            System.out.println("\n El resultado es...");

            System.out.println(resultado);

            socketUDP.close();

        } catch (IOException e) {

            System.out.println(
                    "Error" + e.getMessage()
            );
        }
    }
}