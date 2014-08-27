package bp.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class ServerSideSocket
{
	private SSLServerSocket sslserverSocket;						//The server side socket
	private SSLSocket sslclientSocket;							//The client socket that the serverSocket communicates with
	private PrintWriter clientPrinter;						//Output stream to the client
	private BufferedReader clientReader;					//Input stream from the client
	private String clientMessage;							//message obtained from the client
	
	/**
	 * 
	 * Default constructor for ServerSideSocket
	 * @param portNumber | port to bind the server side socket to
	 */
	public ServerSideSocket(int portNumber)
	{
		try
		{
			sslserverSocket = (SSLServerSocket) SSLServerSocketFactory.getDefault().createServerSocket(portNumber);
			sslclientSocket = (SSLSocket) sslserverSocket.accept();//waits at this point till a client connects
			clientPrinter = new PrintWriter(sslclientSocket.getOutputStream(), true);
			clientReader = new BufferedReader(new InputStreamReader(sslclientSocket.getInputStream()));
		}catch (IOException e){
			System.out.println("The server could not bind the socket to that port number");
			System.out.println("ERROR: "+e.getMessage());
		}
	}
	
	/**
	 * 
	 * Returns the client message
	 * @return String | clientMessage
	 */
	public String getClientMessage()
	{
		return clientMessage;
	}
	
	/**
	 * 
	 * Returns the print writer from the client
	 * @return PrintWriter | clientPrinter
	 */
	public PrintWriter getClientPrinter()
	{
		return clientPrinter;
	}

	/**
	 * 
	 * Returns the buffered reader from the client
	 * @return BufferedReader | clientReader
	 */
	public BufferedReader getClientReader()
	{
		return clientReader;
	}

	/**
	 * 
	 * Returns the client socket
	 * @return Socket | clientSocket
	 */
	public Socket getClientSocket()
	{
		return sslclientSocket;
	}

	/**
	 * 
	 * Returns the server side socket
	 * @return ServerSocket | serverSocket
	 */
	public ServerSocket getServerSocket()
	{
		return sslserverSocket;
	}

	/**
	 * 
	 * Attempts to continually set the client message if the client exist
	 */
	public void run()
	{
		try{
			String inputLine;
			while ((inputLine = clientReader.readLine()) != null)
			{
				clientMessage = inputLine;//will fail if there is no client
                                //System.out.println(clientMessage);
                                clientPrinter.println(clientMessage);
			}
		} catch (IOException e){
			System.out.println("There is no client at this point in time");
			System.out.println("ERROR: "+e.getMessage());
		}
	}
}
