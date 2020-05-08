package csvJson.Cliente;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConexaoCliente {
	public Socket connectar(String pathCsv, String pathJson) {
		try {
			Socket cliente = new Socket("127.0.0.1", 12345);
			
			PrintWriter destino = new PrintWriter(cliente.getOutputStream(), true);
			destino.println(pathCsv);
			destino.println(pathJson);
			
			return cliente;
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
