package bp.net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

import bp.net.server.BP01Server;

public class ServerSideSocket
{
	private SSLSocket sslclientSocket;							//The client socket that the serverSocket communicates with
	private PrintWriter clientPrinter;						//Output stream to the client
	private BufferedReader clientReader;					//Input stream from the client
	private String clientMessage = "";							//message obtained from the client
	private String newMessage = "";							//newest message obtained from the client
	private String clientName;
	private ObjectOutputStream objectPrinter;
	
	/**
	 * 
	 * Default constructor for ServerSideSocket
	 * @param portNumber | port to bind the server side socket to
	 */
	public ServerSideSocket(SSLServerSocket sslserverSocket)
	{
		try
		{
			//sslserverSocket.setNeedClientAuth(true);
			sslclientSocket = (SSLSocket) sslserverSocket.accept();//waits at this point till a client connects
			BP01Server.jTextArea.append("New client connected: PORT[27331]\n");
			clientPrinter = new PrintWriter(sslclientSocket.getOutputStream(), true);
			clientReader = new BufferedReader(new InputStreamReader(sslclientSocket.getInputStream()));
			objectPrinter = new ObjectOutputStream(sslclientSocket.getOutputStream());
			setClientname(clientReader.readLine());
			objectPrinter.writeObject(BP01Server.socketNameList);
		}catch (IOException e){
			BP01Server.jTextArea.append("The server could not bind the socket to that port number\n");
			BP01Server.jTextArea.append("ERROR: "+e.getMessage()+"\n");
		}
                System.out.println("done with client");
	}
	
	private void setClientname(String clientName)
	{
		this.clientName = clientName;
	}
	
	public String getClientName()
	{
		return clientName;
	}

	public ObjectOutputStream getObjectPrinter()
	{
		return objectPrinter;
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
	
	public boolean newClientMessage()
	{
		if(clientMessage.equals(newMessage))
			return false;
		clientMessage = newMessage;
		return true;
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
	 * Attempts to set the client message if the client exist
	 */
	public void run()
	{
		try{
			if(clientReader.ready())
			{
                            System.out.println("client reader ready");
				String inputLine;
				if((inputLine = clientReader.readLine()) != null)
				{
					if(inputLine.equals("SOCKET_TO_SERVER_CLOSE"))
					{
						BP01Server.jTextArea.append("The Client ["+clientName+"] Has Disconnected\n");
						BP01Server.socketNameList.remove(clientName);
						BP01Server.socketList.remove(this);
					}
                                        else{
                                                System.out.println("message recieved");
						newMessage = inputLine;//will fail if there is no client
                                        }
				}
			}
		} catch (IOException e){
			BP01Server.jTextArea.append("Fatel error\n");
		}
	}
}
