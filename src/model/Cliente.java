package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

	//Direccion ip local
	public static final String LOCAL_HOST = "localhost";

	//Puerto donde se establece la conexión
	public static final int PORT = 25000;

	//Socket que permite la conexión con el Servidor
	private static Socket socket;

	public static void main(String[] args) throws IOException {

		Scanner scanner = new Scanner(System.in);
		socket = new Socket(LOCAL_HOST,PORT);
		DataInputStream in = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());

		// Hilo necesario para enviar mensajes
		Thread envioMensaje = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {

					// Guarda el mensaje que escriba el cliente
					String mensaje = scanner.nextLine();

					try {
						// Escribe el mensaje utilizando el Output Stream
						out.writeUTF(mensaje);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		// Hilo necesario para leer mensajes
		Thread lecturaMensaje = new Thread(new Runnable() {
			@Override
			public void run() {

				while (true) {
					try {
						// Lee el mensaje recibido por el cliente y posteriormente lo imprime
						String msg = in.readUTF();
						System.out.println(msg);
					} catch (IOException e) {

						e.printStackTrace();
					}
				}
			}
		});

		envioMensaje.start();
		lecturaMensaje.start();

	}
	
	/**
	 * Este método se encarga de descifrar la palabra recibida utilizando cifrado Cesar inverso
	 * 
	 * @param cipherText  es la palabra que se desea cifrar, enviada por un cliente
	 * @param shift es la llave de cifrado entre 1 y 20 que se va a utilizar, el
	 *              número de posiciones que se mueven
	 * @return retorna un String con la palabra descifrada
	 */
	private static String descifrarCesar(String cipherText, int shift) {
		//La palabra descifrada que se retornará después
		String decryptedText = "";
		// Se utiliza una variable tipo char para cifrar la palabra letra por letra
		char letter;

		for (int i = 0; i < cipherText.length(); i++) {
			letter = cipherText.charAt(i);

			// Primero se comprueba si la letra en la posición i está en minúscula
			if (letter >= 'a' && letter <= 'z') {
				// Se corre de posición hacia la izquierda la letra según la llave de cifrado
				letter = (char) (letter - shift);

				// Si el valor en ASCII de letter es menor a 'a' significa que se desbordó del abecedario en minúscula
				// Por consiguiente, se debe reiniciar la cuenta, haciendo que 'z' sea anterior a 'a'
				if (letter < 'a') {
					letter = (char) (letter - 'a' + 'z' + 1);
				}
				cipherText = cipherText + letter;
			}

			// Se comprueba si la letra en la posición i está en mayúscula
			else if (letter >= 'A' && letter <= 'Z') {
				// Se corre de posición hacia la izquierda la letra según la llave de cifrado
				letter = (char) (letter - shift);

				// Si el valor en ASCII de letter es menor a 'A' significa que se desbordó del abecedario en mayúscula
				// Por consiguiente, se debe reiniciar la cuenta, haciendo que 'Z' sea anterior a 'A'
				if (letter < 'A') {
					letter = (char) (letter - 'A' + 'Z' + 1);
				}
				cipherText = cipherText + letter;
			}

			// Si letter no es una letra del abecedario, entonces simplemente se concatena a la palabra descifrada
			else {
				cipherText = cipherText + letter;
			}
		}

		return decryptedText;
	}
}
