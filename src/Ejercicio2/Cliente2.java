/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 /*
	Yohovani Vargas Pacheco
	2016670074
	Envio de Mensajes entre dos Clientes mediante un servidor
	Ejecutar Primero el servidor los clientes pueden ser ejecutados en cualquier orden
	Escribir el mensaje que se desea enviar al otro cliente
 */
 
package Ejercicio2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yohov
 */
public class Cliente2 {
		static DatagramSocket cliente;
	
	public static void main(String arg[]){
		System.out.println("Escriba el mensaje para el Cliente 1");
		Scanner reader = new Scanner(System.in);
			try {
				cliente = new DatagramSocket();
				enviarRespuesta(reader.nextLine());
				recibirRespuesta();
		

			} catch (SocketException ex) {
				Logger.getLogger(Cliente2.class.getName()).log(Level.SEVERE, null, ex);
			}
		
	}
	
	public static void recibirRespuesta(){
		try {
			DatagramPacket paqueteRecibir;
			byte buffer[] = new byte[1024];
			paqueteRecibir = new DatagramPacket(buffer,buffer.length);
			cliente.receive(paqueteRecibir);
			String recibe = new String(buffer);
			System.out.println(recibe);
		} catch (SocketException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public static void enviarRespuesta(String mensaje){
		try {
			DatagramPacket paqueteEnviar;
			byte buffer[] = mensaje.getBytes();
			paqueteEnviar = new DatagramPacket(buffer,buffer.length,InetAddress.getLocalHost(),1234);
			System.out.println("Enviando al Cliente 1...");
			cliente.send(paqueteEnviar);
		} catch (SocketException | UnknownHostException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
