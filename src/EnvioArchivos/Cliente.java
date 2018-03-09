/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EnvioArchivos;

/*
	Envio de Archivos entre cliente y servidor utilizando datagramas
	2016670074
	Yohovani Vargas Pacheco
	Aplicaciones para Comunicación en red
	07/03/2018
	Orden de ejecución: servidor, cliente
*/
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yohovani
 */
public class Cliente{
	private DatagramSocket cliente;
	int contador=0;
	public Cliente(){
		try {
			cliente =new DatagramSocket();
		} catch (SocketException ex) {
			Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public static void main(String args[]) throws IOException{
		Cliente c = new Cliente();
		c.enviarRespuesta();
		c.recibirRespuesta();
	}
	
	public void recibirRespuesta(){
		try {
			DatagramPacket paqueteRecibir;
			byte buffer[] = new byte[5120];
			paqueteRecibir = new DatagramPacket(buffer,buffer.length);

			File f = new File("Archivo Recibido.mp3");
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));

			cliente.receive(paqueteRecibir);
			String x = new String(paqueteRecibir.getData());

			try{
				double paquetes = Double.parseDouble(x);
				
				System.out.println("Total de paquetes a recibir: "+paquetes);
				
				
			
				for(int i=0;i<paquetes;i++){
					
						cliente.receive(paqueteRecibir);
						contador++;
						buffer = new byte[paqueteRecibir.getData().length];
						buffer = paqueteRecibir.getData();
						bos.write(buffer);
						System.out.println("Recibiendo paquete: "+(i+1)+"/"+paquetes);
					

				}
				bos.close();
				cliente.close();
				System.out.println("Se Recibio el Archivo de manera correcta");
//				FileReader fr = new FileReader(f);
//				BufferedReader br = new BufferedReader(fr);
//				System.out.println("Se recibio el archivo");
//				String linea;
//				while((linea=br.readLine()) != null){
//					System.out.println(linea);
//				}

			}catch(NumberFormatException ex){ // handle your exception
				System.out.println("Error: "+ex.getMessage());
			}

		
		} catch (SocketException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void enviarRespuesta() throws IOException{
		cliente = new DatagramSocket();
		DatagramPacket paqueteEnviar;
		byte buffer[] = "Enviame el archivo a esta dirección".getBytes();
		paqueteEnviar = new DatagramPacket(buffer,buffer.length,InetAddress.getLocalHost(),1234);
		System.out.println("Conectando con el servidor...");
		cliente.send(paqueteEnviar);
	}

}
