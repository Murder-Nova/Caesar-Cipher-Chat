package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

	/**
	 * Puerto por donde el servidor atendera a los clientes
	 */
	public static final int PORT = 8000;
	/**
	 * El servidor dispone de un serversocket, para permitir la conexion a los
	 * clientes
	 */
	private static ServerSocket serverSocket;
	/**
	 * El servidor dispone de un socket para atender a cada cliente por individual
	 */
	private static Socket socket;

	/**
	 * Llave usada para cifrar el mensaje del cliente
	 */
	private static int cipherKey = (int) Math.random() * 21;

	public static void main(String[] args) {

		DataInputStream in;
		DataOutputStream out;

		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("::: Servidor atendiendo a los posibles clientes :::");

			while (true) {

				socket = serverSocket.accept();
				System.out.println("El cliente se ha conectado!");
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
				String mensajeObtenidoCliente = in.readUTF();
				System.out.println("El mensaje enviado por el cliente fue : " + mensajeObtenidoCliente);
				String respuestaServer = metodoServicioServer(mensajeObtenidoCliente);
				out.writeUTF(respuestaServer);
				socket.close();
				System.out.println("::El cliente fue desconectado del server::");

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String metodoServicioServer(String mensajeObtenidoCliente) {

		String[] cadena = mensajeObtenidoCliente.split(";");

		return Integer.parseInt(cadena[0]) + Integer.parseInt(cadena[1]) + "";
	}

	/**
	 * Este método se encarga de cifrar la palabra recibida utilizando cifrado Cesar
	 * 
	 * @param text  es la palabra que se desea cifrar, enviada por un cliente
	 * @param shift es la llave de cifrado entre 1 y 20 que se va a utilizar, el
	 *              número de posiciones que se mueven
	 * @return retorna un String con la palabra cifrada
	 */
	private static String cifrarCesar(String text, int shift) {
		// La palabra cifrada que se retornará después
		String cipherText = "";
		// Se utiliza una variable tipo char para cifrar la palabra letra por letra
		char letter;

		for (int i = 0; i < text.length(); i++) {
			letter = text.charAt(i);

			// Primero se comprueba si la letra en la posición i está en minúscula
			if (letter >= 'a' && letter <= 'z') {
				// Se corre de posición hacia la derecha la letra según la llave de cifrado
				letter = (char) (letter + shift);

				// Si el valor en ASCII de letter es mayor a 'z' significa que se desbordó del abecedario en minúscula
				// Por consiguiente, se debe reiniciar la cuenta, haciendo que 'a' siga después de 'z'
				if (letter > 'z') {
					letter = (char) (letter + 'a' - 'z' - 1);
				}
				cipherText = cipherText + letter;
			}

			// Se comprueba si la letra en la posición i está en mayúscula
			else if (letter >= 'A' && letter <= 'Z') {
				// Se corre de posición hacia la derecha la letra según la llave de cifrado
				letter = (char) (letter + shift);

				// Si el valor en ASCII de letter es mayor a 'Z' significa que se desbordó del abecedario en mayúscula
				// Por consiguiente, se debe reiniciar la cuenta, haciendo que 'A' siga después de 'Z'
				if (letter > 'Z') {
					letter = (char) (letter + 'A' - 'Z' - 1);
				}
				cipherText = cipherText + letter;
			}

			// Si letter no es una letra del abecedario, entonces simplemente se concatena a la palabra cifrada
			else {
				cipherText = cipherText + letter;
			}
		}

		return cipherText;
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