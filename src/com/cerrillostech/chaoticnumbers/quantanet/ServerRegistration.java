package com.cerrillostech.chaoticnumbers.quantanet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.cerrillostech.chaoticnumbers.quantanet.ServerTCP.Task;

public class ServerRegistration extends Thread {
	private ServerSocket sock;
	private static String LogPrefix = "[Registration] ";
	public ServerRegistration(int port) throws IOException {
		sock = new ServerSocket(50111);
		sock.setSoTimeout(0);
	}
	public void run(){
		while(true){
			try{
				System.out.println(LogPrefix+"Waiting for client on port " + sock.getLocalPort() + "...");
				Socket server = sock.accept();
				System.out.println(LogPrefix+"Connected: " + server.getRemoteSocketAddress());
				ServerTCP newTCPHandler = new ServerTCP(server, Task.REGISTRATION);
				newTCPHandler.run();
			} catch (Exception e){
				
			}	
		}
	}
}
