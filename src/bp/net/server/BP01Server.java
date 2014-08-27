package bp.net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.net.ssl.SSLServerSocket;

import bp.net.server.ServerSideSocket;

public class BP01Server
{
	public static ArrayList<ServerSideSocket> socketList = new ArrayList<ServerSideSocket>();
	public static ArrayList<String> socketNameList = new ArrayList<String>();
	private static SSLServerSocket sslserverSocket;
	
	static Thread socketThread = new Thread(
			new Runnable(){
				public void run(){
					while(true)
					{
						socketList.add(new ServerSideSocket(sslserverSocket));
						socketNameList.add(socketList.get(socketList.size()-1).getClientName());
					}
				}});
	
	static Thread socketRunThread = new Thread(
			new Runnable(){
				public void run(){
					while(true)
						for(int i=0;i<socketList.size();i++)
							socketList.get(i).run();

				}});
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args)
	{
		//System.setProperty("javax.net.ssl.keyStore", "/bp/net/agent6262KeyStore");java.security.KeyStore
		//System.setProperty("javax.net.ssl.keyStorePassword", "223196522419553181997");s
		
		System.out.print("Starting Server Side Socket On Port 27331: ");
		try{
			sslserverSocket = (SSLServerSocket) javax.net.ssl.SSLServerSocketFactory.getDefault().createServerSocket(139);
			sslserverSocket.setEnabledCipherSuites(sslserverSocket.getEnabledCipherSuites());
		} catch (IOException e){
			System.err.println("(ERROR: "+e.getMessage()+")");
		}finally{
			if(sslserverSocket != null)
				System.out.println("Done");
			else{
				System.err.println("Shutting Down Due To: No Open Ports for the ServerSocket");
				System.exit(1);
			}
		}
		
		System.out.print("Starting Server Socket Thread: ");
		socketThread.start();
		System.out.println("Done");
		
		System.out.print("Starting Socket Run Thread: ");
		socketRunThread.start();
		System.out.println("Done");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		while(true)
		{
			try{
				if(br.ready())
					switch(br.readLine())
					{
					case "EXIT":
						System.exit(0);
					default:
						System.out.println("Type /help or /? for a list of commands");
					}
			} catch (IOException e){
				e.printStackTrace();
			}
			
			for(int i=0;i<socketList.size();i++)
				if(socketList.get(i).newClientMessage())
					for(int j=0;j<socketList.size();j++)
						socketList.get(j).getClientPrinter().println(socketList.get(i).getClientName()+": "+socketList.get(i).getClientMessage());
		}
		

	}

}
