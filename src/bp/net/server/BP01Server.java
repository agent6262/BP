package bp.net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLServerSocket;
import javax.swing.JTextArea;


public class BP01Server
{
	public static ArrayList<ServerSideSocket> socketList = new ArrayList<>();
	public static ArrayList<String> socketNameList = new ArrayList<>();
	private static SSLServerSocket sslserverSocket;
        public static JTextArea jTextArea;
        public static int port;
	
	static Thread socketThread = new Thread(
			new Runnable(){
                                @Override
				public void run(){
					while(true)
					{
						socketList.add(new ServerSideSocket(sslserverSocket));
						socketNameList.add(socketList.get(socketList.size()-1).getClientName());
                                                System.out.println(socketList.size());
					}
				}});
	
	static Thread socketRunThread = new Thread(
			new Runnable(){
                                @Override
				public void run(){
                                    System.out.println("i started");
					while(true){
                                            try {
                                                Thread.sleep(1);
                                        } catch (InterruptedException ex) {
                                            Logger.getLogger(BP01Server.class.getName()).log(Level.SEVERE, null, ex);
                                        }
						for(int i=0;i<socketList.size();i++){
                                                    socketList.get(i).run();
                                                }
                                        }
				}});
	
        public BP01Server(JTextArea jTextArea, int port){
            BP01Server.jTextArea = jTextArea;
            BP01Server.port = port;
        }
        
	public void run()
	{
		jTextArea.append("Starting Server Side Socket On Port 27331: ");
		try{
			sslserverSocket = (SSLServerSocket) javax.net.ssl.SSLServerSocketFactory.getDefault().createServerSocket(port);
			sslserverSocket.setEnabledCipherSuites(sslserverSocket.getEnabledCipherSuites());
		} catch (IOException e){
			jTextArea.append("(ERROR: "+e.getMessage()+")\n");
		}finally{
			if(sslserverSocket != null)
				jTextArea.append("Done\n");
			else{
				jTextArea.append("Shutting Down Due To: No Open Ports for the ServerSocket\n");
				return;
			}
		}
		
		jTextArea.append("Starting Server Socket Thread: ");
		socketThread.start();
		jTextArea.append("Done\n");
		
		jTextArea.append("Starting Socket Run Thread: ");
		socketRunThread.start();
		jTextArea.append("Done\n");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		while(true)
		{
			try{
				if(br.ready())
					switch(br.readLine())
					{
					case "EXIT":
						return;
					default:
						jTextArea.append("Type /help or /? for a list of commands\n");
					}
			} catch (IOException e){
                            //FIXME
			}
			
			for(int i=0;i<socketList.size();i++)
				if(socketList.get(i).newClientMessage())
					for(int j=0;j<socketList.size();j++){
						socketList.get(j).getClientPrinter().println(socketList.get(i).getClientName()+": "+socketList.get(i).getClientMessage());
                                                System.out.println("new message sent");
                                        }
		}
		
	}

}
