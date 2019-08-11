package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Servidor {

	// Puerto por donde el servidor atendera a los clientes
	public static final int PORT = 8000;

	// Vector para almacenar a los clientes activos
	static Vector<Regulador> activeClients = new Vector<>();

	// El servidor dispone de un serversocket, para permitir la conexion a los clientes
	private static ServerSocket serverSocket;

	// El servidor dispone de un socket para atender a cada cliente por individual
	private static Socket socket;

	// Llave usada para cifrar el mensaje del cliente
	private static int cipherKey = (int) Math.random() * 21;

	// Contador de clientes
	static int count = 0;

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

				// Se crea un nuevo regulador que se encargara de la solicitud del nuevo cliente
				Regulador cliente = new Regulador(socket, "Cliente " + count, in, out);

				// Se crea un hilo para permitir la conexion de varios clientes a la vez
				Thread hiloCliente = new Thread(cliente);

				// Se agrega el cliente a la lista de clientes activos
				activeClients.add(cliente);
				hiloCliente.start();
				count++;

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
}