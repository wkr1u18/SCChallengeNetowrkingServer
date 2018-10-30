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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class ServerMain implements Runnable {
	private Thread t = null;
	private ServerSocket serverSocket = null;
	private ServerThread clientThread = null;
	private HashMap<Integer, ServerThread> connectedClients = new HashMap<Integer, ServerThread>();
	private HashMap<Integer, String> nickMap = new HashMap<Integer,String>(); 
	
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
		Integer portNumber = new Integer(clientSocket.getPort());
		connectedClients.put(portNumber, new ServerThread(this, clientSocket));
		
		nickMap.put(portNumber, "user" + portNumber.toString());
		try {
			
			connectedClients.get(portNumber).open();
			connectedClients.get(portNumber).start();

		}
		catch(IOException ioe) {
			System.out.println(ioe);
		}
	}
	
	
	public synchronized void handle (int ID, String message) {
		System.out.println("Handling message: " + message + " from " + ID);
		if(message.equals(".quit")) {
			deliver(ID, "has disconnected");
			connectedClients.get(ID).send(".quitconfirm");
			connectedClients.remove(ID);
			nickMap.remove(ID);
		}
		else {
			
			if(message.length()>=8 && message.substring(0, 8).equals(".setnick")) {
				String[] command = message.split(" ",2);
				nickMap.put(ID, command[1]);
				System.out.println("Set nick: " + command[1] + " for user with id: " + ID);
			}
			else {
				deliver(ID, message);
			}
		}
	}
	
	public synchronized void deliver (int orginatorID, String message) {
		Iterator it = connectedClients.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			if(!pair.getKey().equals(orginatorID)) {
				connectedClients.get(pair.getKey()).send("["+ nickMap.get(orginatorID) + "] " + message);
			}
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
		System.out.println("Welcome to server! Please give the port number: ");
		Scanner textIn = new Scanner(System.in);
		ServerMain sm = new ServerMain(textIn.nextInt());
	}

}
/*
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
*/