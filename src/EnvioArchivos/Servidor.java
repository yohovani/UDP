/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
	Envio de Archivos entre cliente y servidor utilizando datagramas
	2016670074
	Yohovani Vargas Pacheco
	Aplicaciones para Comunicación en red
	07/03/2018
	Orden de ejecución: servidor, cliente
*/

package EnvioArchivos;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author yohovani
 */
public class Servidor {
	private DatagramSocket servidor;
	
	public Servidor(){
		try {
			servidor = new DatagramSocket(1234);
		} catch (SocketException ex) {
			Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public static void main(String args[]){
		Servidor s = new Servidor();
		DatagramPacket x = s.recibirRespuesta();
		s.enviarArchivo(x.getAddress(),x.getPort());
	};
	
	public void enviarArchivo(InetAddress x,int p) {
		try {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de texto","txt");
			chooser.addChoosableFileFilter(filtro);
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.showDialog(chooser, null);
			File f = chooser.getSelectedFile();
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));

			DatagramPacket paqueteEnviar;
			byte buffer[] = new byte[5120];
			paqueteEnviar = new DatagramPacket(buffer,buffer.length,x,p);
			System.out.println("Enviando al Cliente...");
			
			double npaq = f.length();

			System.out.println();
			if(npaq < 512){
				npaq = 1;
			}else{
				if((npaq%512) != 0){
					int aux = (int) (npaq/512);
					npaq=aux+1;
				}else
					npaq/=512;
				
			}
			String q =""+npaq;
			byte[] paquetes = q.getBytes();
			
			
			paqueteEnviar = new DatagramPacket(paquetes,paquetes.length,x,p);
			servidor.send(paqueteEnviar);
			
			for(int i=0;i<npaq;i++){
				buffer = new byte[512];

				bis.read(buffer);
				paqueteEnviar.setData(buffer);
				servidor.send(paqueteEnviar);
				System.out.println("Enviando paquete: "+(i+1)+"/"+q);
			}
			System.out.println(npaq+"");
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public DatagramPacket recibirRespuesta(){
		try {
			DatagramPacket paqueteRecibir;
			byte buffer[] = new byte[512];
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
}
