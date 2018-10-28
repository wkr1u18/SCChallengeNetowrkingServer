/** 
 * Server code for simple chat over sockets program
 * @author Wojciech Rozowski (wkr1u18)
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread{
	private Socket clientSocket = null;
	private ServerMain myServer = null;
	private int threadID = -1;
	private BufferedReader in = null;
	private PrintWriter out = null;
	
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
				myServer.handle(threadID, inputLine);
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
		out = new PrintWriter(clientSocket.getOutputStream(), true);
	}
	
	//Getter for threadID field
	public int getID() {
		return threadID;
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
		
		if (out != null) {
			out.close();
		}
	}
	
	//Sends message to host
	public void send(String msg) {
		try {
			out.println(msg);
		}
		catch (Exception ioe) {
			System.out.println(ioe);
		}
	}
}
