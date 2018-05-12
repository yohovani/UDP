/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicast;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yohovani
 */
public class multicast {
	private final int puerto=55400;
	private final String group = "225.3.4.5";
	private MulticastSocket ms;

	public static void main(String[] args){
		multicast c = new multicast();
		Scanner reader = new Scanner(System.in);
	//	c.enviar(reader.nextLine());
		c.recibirRespuesta();
	}
	
	public multicast() {
		try {
			ms = new MulticastSocket(puerto);
			ms.joinGroup(InetAddress.getByName(group));
		} catch (IOException ex) {
			Logger.getLogger(multicast.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void enviar(String mensaje){
		try {
			DatagramPacket paqueteEnviar;
			byte buffer[] = mensaje.getBytes();
			paqueteEnviar = new DatagramPacket(buffer,buffer.length,InetAddress.getByName(group),puerto);
			ms.send(paqueteEnviar);
		} catch (IOException ex) {
			Logger.getLogger(multicast.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void recibirRespuesta(){
		try {
			DatagramPacket paqueteRecibir;
			byte buffer[] = new byte[1024];
			paqueteRecibir = new DatagramPacket(buffer,buffer.length);
			ms.receive(paqueteRecibir);
			String recibe = new String(buffer);
			System.out.println(recibe);
		} catch (SocketException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}	
}