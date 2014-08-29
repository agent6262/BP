package bp.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class ClientSocket
{
	private SSLSocket sslclient;
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
			sslclient = (SSLSocket) SSLSocketFactory.getDefault().createSocket(host, portNumber);
			serverPrinter = new PrintWriter(sslclient.getOutputStream(), true);
                        serverReader = new BufferedReader(new InputStreamReader(sslclient.getInputStream()));
		} catch (IOException e)
		{
			System.out.println("The host | The host at the spceified port does not exist");
			System.out.println(e.getMessage());
		}
                System.out.println("done with object cretion");
	}
        
        public void close()
        {
            try {
                
                serverReader.close();
                serverPrinter.close();
                sslclient.close();
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
		return sslclient;
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
         * @param message the message to be sent
	 */
	public void sendMessage(String message)
	{
                serverPrinter.println(message);
                System.out.println("done with message");
	}
	
	
}
