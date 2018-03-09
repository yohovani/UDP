/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ejercicio1;

import java.net.*;
import java.io.*;

/**
 *
 * @author yohovani
 */
public class Cliente {
	public static void main(String arg[]){
		enviarRespuesta();
		recibirRespuesta();
	}
	
	static DatagramSocket cliente;
	public static void recibirRespuesta(){
		try {
//			cliente = new DatagramSocket(1235);
			DatagramPacket paqueteRecibir;
			byte buffer[] = new byte[1024];
			paqueteRecibir = new DatagramPacket(buffer,buffer.length);
			//REcibimos lo que el cliente nos va a enviar
			cliente.receive(paqueteRecibir);
			String recibe = new String(buffer);
			System.out.println(recibe);
		} catch (SocketException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public static void enviarRespuesta(){
		try {
			cliente = new DatagramSocket();
			DatagramPacket paqueteEnviar;
			byte buffer[] = "Hola servidorcito .... :P".getBytes();
			paqueteEnviar = new DatagramPacket(buffer,buffer.length,InetAddress.getLocalHost(),1234);
			System.out.println("Enviando al servidor...");
			cliente.send(paqueteEnviar);
			//cliente.close();
		} catch (SocketException | UnknownHostException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
