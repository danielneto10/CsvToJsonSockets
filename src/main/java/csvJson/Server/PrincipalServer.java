package csvJson.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PrincipalServer {

	
	
	public static void main(String[] args) {
		ServerSocket server = null;
		Socket cliente = null;
		try {
			server = new ServerSocket(12345);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(true) {
			try {
				cliente = server.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			new Controladora(cliente).iniciar();;
		}
		
		
	}

}
