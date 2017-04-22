
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;


public class Client {
	
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;
	private static boolean reciving = false;
	
	public static void waitForConnection() throws IOException{
		serverSocket = new ServerSocket(12345);
		clientSocket = serverSocket.accept();
	}
	public static void connectToClient(String ip , int port) throws IOException{
		clientSocket = new Socket(ip,port);
	}
	public static void send(String msg) throws IOException, NoConnectionException{
		if(clientSocket==null)
			throw new NoConnectionException();
		PrintStream printStream = new PrintStream(clientSocket.getOutputStream());
		printStream.println(msg);
		printStream.flush();
	}
	public static void startReciving(CopyOnWriteArrayList<String> msgList) throws IOException, NoConnectionException{
		if(clientSocket==null)
			throw new NoConnectionException();
		reciving = true;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(reciving){
						try {
							if(bufferedReader.ready()){
								String msg = bufferedReader.readLine();
								if(msgList!=null)
									msgList.add(msg);
								System.out.println(msg);
							}
						} catch (IOException e) {}
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
						}
					}
				}
			}).start();
	}
	public static void stopReciving(){
		reciving = false;
	}
}
