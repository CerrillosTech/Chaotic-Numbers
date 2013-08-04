package com.cerrillostech.chaoticnumbers.quantanet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerTCP extends Thread {
	public enum Task{
		REGISTRATION("REG");
		private final String value;
		private Task(String value){
			this.value = value;
		}
		public String getString(){
			return this.value;
		}
	}
	private Socket sock;
	private final Task task;
	private static String LogPrefix = "[TCP Handler] ";
	public ServerTCP(Socket sock, Task task) throws IOException {
		this.sock = sock;
		this.task = task;
	}
	public void run(){
		if(this.task.equals(Task.REGISTRATION)){
			Reg();
		}
	}
	private void Reg(){
		try{
			DataInputStream in = new DataInputStream(sock.getInputStream());
			System.out.println(LogPrefix+"Data: " + in.readUTF());
			DataOutputStream out = new DataOutputStream(sock.getOutputStream());
			out.writeUTF(LogPrefix+"Message sent to " + sock.getLocalSocketAddress() + " has been received!");
			System.out.println(LogPrefix+"Disconnecting: " + sock.getRemoteSocketAddress());
			sock.close();
		} catch (Exception e){
			
		}
	}
}
