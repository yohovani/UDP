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
 */
package Ejercicio2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author yohov
 */
public class Servidor {
	static DatagramSocket servidor;
	
	public static void main(String args[]) throws InterruptedException{
		System.out.println("Servidor");
		try {
			servidor = new DatagramSocket(1234);
			DatagramPacket cliente1 = recibirRespuesta();
		DatagramPacket cliente2 = recibirRespuesta();
		//Thread.sleep(10000);
		enviarRespuesta(cliente1,cliente2);
		enviarRespuesta(cliente2,cliente1);
		} catch (SocketException ex) {
			Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}
	
	public static DatagramPacket recibirRespuesta(){
		try {
			
			DatagramPacket paqueteRecibir;
			byte buffer[] = new byte[1024];
			paqueteRecibir = new DatagramPacket(buffer,buffer.length);
			servidor.receive(paqueteRecibir);
			String recibe = new String(paqueteRecibir.getData(),0,paqueteRecibir.getLength());
		//	System.out.println(recibe);
			return paqueteRecibir;
		} catch (SocketException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		
		return null;
	}
	
	public static void enviarRespuesta(DatagramPacket x,DatagramPacket y){
		try {
			DatagramPacket paqueteEnviar;
			
			byte buffer[] = x.getData();
			paqueteEnviar = new DatagramPacket(buffer,buffer.length,y.getAddress(),y.getPort());
			System.out.println("Enviando al Cliente...");
			servidor.send(paqueteEnviar);
//			cliente.close();
		} catch (SocketException | UnknownHostException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
