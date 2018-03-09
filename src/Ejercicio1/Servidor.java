/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ejercicio1;

/**
 *
 * @author yohovani
 */
import java.net.*;
import java.io.*;
import java.util.Scanner;


public class Servidor {
	public static void main(String args[]){
		Scanner reader = new Scanner(System.in);
		DatagramPacket s = recibirRespuesta();
		enviarRespuesta(reader.nextLine(),s);
	}
	static DatagramSocket servidor;
	public static DatagramPacket recibirRespuesta(){
		try {
			servidor = new DatagramSocket(1234);
			DatagramPacket paqueteRecibir;
			byte buffer[] = new byte[1024];
			paqueteRecibir = new DatagramPacket(buffer,buffer.length);
			//REcibimos lo que el cliente nos va a enviarr
			servidor.receive(paqueteRecibir);
			String recibe = new String(paqueteRecibir.getData(),0,paqueteRecibir.getLength());
			System.out.println(recibe);
			return paqueteRecibir;
		} catch (SocketException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		
		return null;
	}
	
	public static void enviarRespuesta(String respuesta,DatagramPacket x){
		try {
			DatagramPacket paqueteEnviar;
			byte buffer[] = respuesta.getBytes();
			paqueteEnviar = new DatagramPacket(buffer,buffer.length,x.getAddress(),x.getPort());
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