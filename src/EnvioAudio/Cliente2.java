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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author yohov
 */
public class Cliente2 {
	private DatagramSocket cliente;

	public Cliente2() {
		try {
			cliente = new DatagramSocket();
		} catch (SocketException ex) {
			Logger.getLogger(Cliente2.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public static void main(String[] args){
		try {
			Cliente2 c = new Cliente2();
			InetAddress serv = InetAddress.getByName("192.168.0.45");
			c.enviarIP(serv);
			c.enviarArchivo(serv, 20011);
		} catch (UnknownHostException ex) {
			Logger.getLogger(Cliente2.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Cliente2.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void enviarArchivo(InetAddress x,int p) {
		try {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de Audio","mp3","wav","mp4");
			chooser.addChoosableFileFilter(filtro);
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.showDialog(chooser, null);
			File f = chooser.getSelectedFile();
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));

			DatagramPacket paqueteEnviar;
			int tamanno=1024;
			byte buffer[] = new byte[tamanno];

			paqueteEnviar = new DatagramPacket(buffer,buffer.length,x,p);
			System.out.println("Enviando al Cliente...");
			
			double npaq = f.length();

			System.out.println();
			
			if(npaq < tamanno){
				npaq = 1;
			}else{
				if((npaq%tamanno) != 0){
					int aux = (int) (npaq/tamanno);
					npaq=aux+1;
				}else
					npaq/=tamanno;
				
			}
			String q =""+npaq;
			byte[] paquetes = q.getBytes();
			
			
			paqueteEnviar = new DatagramPacket(paquetes,paquetes.length,x,p);
			cliente.send(paqueteEnviar);
			Thread.sleep(2000);
			for(int i=0;i<npaq;i++){
				buffer = new byte[tamanno];
				bis.read(buffer);
				paqueteEnviar.setData(buffer);
				cliente.send(paqueteEnviar);
				System.out.println("Enviando paquete: "+(i+1)+"/"+q);
			}
			System.out.println(npaq+"");
		} catch (FileNotFoundException ex) {
			Logger.getLogger(EnvioArchivos.Servidor.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(EnvioArchivos.Servidor.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InterruptedException ex) {
			Logger.getLogger(Cliente2.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void recibirArchivo(){
		try {
			DatagramPacket paqueteRecibir;
			byte buffer[] = new byte[1024];
			paqueteRecibir = new DatagramPacket(buffer,buffer.length);

			File f = new File("Archivo Recibido.mp3");
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));

			cliente.receive(paqueteRecibir);
			String x = new String(paqueteRecibir.getData());

			try{
				double paquetes = Double.parseDouble(x);
				
				System.out.println("Total de paquetes a recibir: "+paquetes);
				
				
			
				for(int i=0;i<paquetes;i++){
						cliente.receive(paqueteRecibir);
						buffer = new byte[paqueteRecibir.getData().length];
						buffer = paqueteRecibir.getData();
						bos.write(buffer);
						System.out.println("Recibiendo paquete: "+(i+1)+"/"+paquetes);
					

				}
				bos.close();
				cliente.close();
				System.out.println("Se Recibio el Archivo de manera correcta");

			}catch(NumberFormatException ex){
				System.out.println("Error: "+ex.getMessage());
			}

		
		} catch (SocketException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void enviarIP(InetAddress x) throws IOException{
		DatagramPacket paqueteEnviar;
		byte buffer[] = "Te envio mi ip: Yohovani".getBytes();
		paqueteEnviar = new DatagramPacket(buffer,buffer.length,x,20011);
		System.out.println("Conectando con el servidor...");
		cliente.send(paqueteEnviar);
		System.out.println("Se envio a: "+new String(paqueteEnviar.getAddress().toString()));
	}
}
