/**
 * Server code for simple chat over sockets program
 * @author Wojciech Rozowski (wkr1u18)
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.Object;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.ConnectException;
import java.util.Scanner;

public class ServerMain {
	
	public static void main(String[] args) {
		ServerSocket myServerSocket;
		Scanner myScanner = new Scanner(System.in);
		//Gets initial port number
		System.out.println("Enter port number: ");
		int portNumber = myScanner.nextInt();
		try {
			//Opens socket on specified port
			myServerSocket = new ServerSocket(portNumber);
			System.out.println("Opened connection on port: " + portNumber);
			while(true) {
				//Waits for connection from client
				System.out.println("Waiting...");
				Socket clientSocket = myServerSocket.accept();
				System.out.println("Client connected to server");
				//When connected sets up PrintWriter and BufferedReader using Socket methods
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String inputLine;
				//While server receives a message from client
				while((inputLine = in.readLine()) != null) {
					//Puts message on screen
					System.out.println("Received messsage: " + inputLine + " from: " + clientSocket.toString());
					//Sends line to host
					out.println(inputLine);
				}
				
				
			}
		}
		catch (IOException e) {
			System.out.println("Couldn't open server on port: " + portNumber);
		}
	}

}
