/** 
 * Server code for simple chat over sockets program
 * @author Wojciech Rozowski (wkr1u18)
 */


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.Object;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.ConnectException;
import java.util.Scanner;
import java.util.Vector;

public class ServerMain implements Runnable {
	private Thread t = null;
	private ServerSocket serverSocket = null;
	private ServerThread clientThread = null;
	private Vector<ServerThread> myClientThreads;
	
	public ServerMain(int portNumber) {
		
		try {
			//Opens ServerSocket on specified port
			System.out.println("Opening server on port " + portNumber + "...");
			serverSocket = new ServerSocket(portNumber);
			System.out.println("Server started.");
			//Calls thread start method
			start();
		}
		catch (IOException ioe) {
			System.out.println(ioe);
		}
	}
	
	//Thread run method
	public void run() {
		while (t!=null) {
			try {
				System.out.println("Waiting for connection from client...");
				addNewThread(serverSocket.accept());
			}
			catch (IOException ioe){
				System.out.println(ioe);
			}
		}
	}
	
	public void addNewThread(Socket clientSocket) {
		System.out.println("Connected to client.");
		clientThread = new ServerThread(this, clientSocket);
		try {
			clientThread.open();
			clientThread.start();
		}
		catch(IOException ioe) {
			System.out.println(ioe);
		}
	}
	
	
	public synchronized void handle(int ID, String message) {
		System.out.println("Handling message: " + message + " from " + ID);
		if(message.equals(".quit")) {
			
		}
		else {
			
		}
	}
	
	//Starts the thread
	public void start() {
		if(t == null) {
			t = new Thread(this);
			t.start();
		}
	}

	public static void main(String[] args) {
		System.out.println("hello");
		ServerMain sm = new ServerMain(1027);
	}

}
/*
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
*/