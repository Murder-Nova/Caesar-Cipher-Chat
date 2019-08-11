package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Regulador implements Runnable {

	Scanner scanner = new Scanner(System.in);
	private String name;
	final DataInputStream in;
	final DataOutputStream out;
	Socket socket;
	boolean isActive;

	// Constructor
	public Regulador(Socket socket, String name, DataInputStream dis, DataOutputStream dos) {
		this.in = dis;
		this.out = dos;
		this.name = name;
		this.socket = socket;
		this.isActive = true;
	}

	@Override
	public void run() {

		String received;
		while (true) {
			try {
				// Recibe el texto
				received = in.readUTF();

				System.out.println(received);

				if (received.equals("logout")) {
					this.isActive = false;
					this.socket.close();
					break;
				}

				// Divide el texto en un mensaje y un recipiente, utilizado para facilitar el envío a los clientes
				StringTokenizer st = new StringTokenizer(received, "#");
				String MsgToSend = st.nextToken();
				String recipient = st.nextToken();

				// Busca el recipiente en el vector que almacena los clientes activos
				for (Regulador mc : Servidor.activeClients) {
					// Si el recipiente es encontrado, escribe el mensaje en el Output Stream
					if (mc.name.equals(recipient) && mc.isActive == true) {
						mc.out.writeUTF(this.name + " : " + MsgToSend);
						break;
					}
				}
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
		try {
			this.in.close();
			this.out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
