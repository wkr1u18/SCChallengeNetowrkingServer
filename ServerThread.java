/** 
 * Server code for simple chat over sockets program
 * @author Wojciech Rozowski (wkr1u18)
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThread extends Thread{
	private Socket clientSocket = null;
	private ServerMain myServer = null;
	private int threadID = -1;
	private BufferedReader in = null;
	
	public ServerThread(ServerMain masterServer, Socket initialClientSocket) {
		myServer = masterServer;
		clientSocket = initialClientSocket;
		threadID = clientSocket.getPort();
	}
	
	//Main server thread
	public void run() {
		System.out.println("Server thread with id: " + threadID + " is running.");
		String inputLine;
		Boolean isDone = false;
		try {
			while(!isDone) {
				inputLine = in.readLine();
				System.out.println("Received messsage: " + inputLine + " from: " + clientSocket.toString());
				isDone = inputLine.equals(".quit");
			}
			close();	
		}
		catch(IOException ioe) {
			System.out.println(ioe);
		}
	}
	
	//Opens BufferedReader
	public void open() throws IOException {
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}
	
	//Closes the clientSocket
	public void close() throws IOException {
		System.out.println("Closing client socket");
		if(clientSocket != null) {
			clientSocket.close();
		}
		if (in != null) {
			in.close();
		}
	}
}
