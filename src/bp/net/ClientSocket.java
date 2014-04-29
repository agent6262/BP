package bp.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientSocket
{
	private Socket client;
	private PrintWriter serverPrinter;
	private BufferedReader serverReader;
	
	/**
	 * 
	 * Default constructor for ClientSocket
	 * @param host
	 * @param portNumber
	 */
	public ClientSocket(String host, int portNumber)
	{
		try
		{
			client = new Socket(host, portNumber);
			serverPrinter = new PrintWriter(client.getOutputStream(), true);
                        serverReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e)
		{
			System.out.println("The host | The host at the spceified port does not exist");
			System.out.println(e.getMessage());
		}
        
	}
        
        public void close()
        {
            try {
                
                serverReader.close();
                serverPrinter.close();
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
	
	/**
	 * 
	 * Returns the current client
	 * @return Socket | client
	 */
	public Socket getClient()
	{
		return client;
	}
        

	/**
	 * 
	 * Returns the print writer to the server
	 * @return PrintWriter | serverPrinter
	 */
	public PrintWriter getServerPrinter()
	{
		return serverPrinter;
	}

	/**
	 * 
	 * Returns the buffered reader from the server
	 * @return BufferedReader | serverReader
	 */
	public BufferedReader getServerReader()
	{
		return serverReader;
	}

	/**
	 * 
	 * Attempts to send the message to the ServerSideSocket
	 */
	public void sendMessage(String message)
	{
                serverPrinter.println(message);
	}
	
	
}
