package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

	//Direccion ip local
	public static final String LOCAL_HOST = "localhost";

	//Puerto donde se establece la conexi�n
	public static final int PORT = 25000;

	//Socket que permite la conexi�n con el Servidor
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
	 * Este m�todo se encarga de descifrar la palabra recibida utilizando cifrado Cesar inverso
	 * 
	 * @param cipherText  es la palabra que se desea cifrar, enviada por un cliente
	 * @param shift es la llave de cifrado entre 1 y 20 que se va a utilizar, el
	 *              n�mero de posiciones que se mueven
	 * @return retorna un String con la palabra descifrada
	 */
	private static String descifrarCesar(String cipherText, int shift) {
		//La palabra descifrada que se retornar� despu�s
		String decryptedText = "";
		// Se utiliza una variable tipo char para cifrar la palabra letra por letra
		char letter;

		for (int i = 0; i < cipherText.length(); i++) {
			letter = cipherText.charAt(i);

			// Primero se comprueba si la letra en la posici�n i est� en min�scula
			if (letter >= 'a' && letter <= 'z') {
				// Se corre de posici�n hacia la izquierda la letra seg�n la llave de cifrado
				letter = (char) (letter - shift);

				// Si el valor en ASCII de letter es menor a 'a' significa que se desbord� del abecedario en min�scula
				// Por consiguiente, se debe reiniciar la cuenta, haciendo que 'z' sea anterior a 'a'
				if (letter < 'a') {
					letter = (char) (letter - 'a' + 'z' + 1);
				}
				cipherText = cipherText + letter;
			}

			// Se comprueba si la letra en la posici�n i est� en may�scula
			else if (letter >= 'A' && letter <= 'Z') {
				// Se corre de posici�n hacia la izquierda la letra seg�n la llave de cifrado
				letter = (char) (letter - shift);

				// Si el valor en ASCII de letter es menor a 'A' significa que se desbord� del abecedario en may�scula
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
