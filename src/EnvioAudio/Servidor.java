/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
	Envio de Archivos de audio entre dos clientes y un servidor de intermediario utilizando datagramas
	Nancy Rubí Briseño Serrano
	Bryan García Silva
	Yohovani Vargas Pacheco
	Aplicaciones para Comunicación en red
	09/03/2018
	Orden de ejecución: servidor,cliente2,cliente1
	Nota: Si quiere enviar con el cliente 1 y recibir con el cliente 2 debe de cambiar los metodos para hacer
		que estos cambien de función.
	PD: El archivo que se envía se guarda en la carpeta del proyecto con el nombre "Archivo Recibido.mp3"
*/

package EnvioAudio;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yohovani
 */
public class Servidor {
	
	private DatagramSocket servidor;

	public Servidor() {
		try {
			servidor = new DatagramSocket(20011);
		} catch (SocketException ex) {
			Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public static void main(String[] args){
		Servidor s = new Servidor();
		DatagramPacket x = s.recibirRespuesta();
		System.out.println(new String(x.getData()));
		DatagramPacket y = s.recibirRespuesta();
		System.out.println(new String(y.getData()));
		s.enviarAudio(x, y);
//		s.enviarAudio(y, x);
		
	}
	
	public DatagramPacket recibirRespuesta(){
		try {
			DatagramPacket paqueteRecibir;
			byte buffer[] = new byte[1024];
			paqueteRecibir = new DatagramPacket(buffer,buffer.length);

			servidor.receive(paqueteRecibir);

			return paqueteRecibir;
		} catch (IOException ex) {
			Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	
	public void enviarAudio(DatagramPacket x,DatagramPacket y){
		try{
			byte[] buffer = new byte[1024];
			DatagramPacket paqueteRecibir = new DatagramPacket(buffer,buffer.length);;
			
			servidor.receive(paqueteRecibir);
            String paq = new String(paqueteRecibir.getData());
			double paquetes = Double.parseDouble(paq);
			
                        
            byte[] n = paq.getBytes();
            DatagramPacket paqueteNumero= new DatagramPacket(n,n.length,y.getAddress(),y.getPort());
			servidor.send(paqueteNumero);
                        
            DatagramPacket paqueteEnviar= new DatagramPacket(buffer,buffer.length,y.getAddress(),y.getPort());
			                     
			System.out.println("Total de paquetes a recibir: "+paquetes);
			            
			for(int i=0;i<paquetes;i++){
				servidor.receive(paqueteRecibir);
				System.out.println("Recibiendo paquete: "+(i+1)+"/"+paquetes);			
				buffer = new byte[paqueteRecibir.getData().length];
				buffer = paqueteRecibir.getData();
				paqueteEnviar.setData(buffer);
				servidor.send(paqueteEnviar);
				System.out.println("Enviando paquete: "+(i+1)+"/"+paquetes);			
			}

			System.out.println("Se Recibio el Archivo de manera correcta");

			}catch(NumberFormatException ex){
				System.out.println("Error: "+ex.getMessage());
			} catch (IOException ex) {
			Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}