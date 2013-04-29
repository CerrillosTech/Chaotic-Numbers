package com.cerrillostech.chaoticnumbers.quantanet;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerBroadcast extends Thread {
	private DatagramSocket sock;
	public void run(){
		openSocket();
		clientListener();
		closeSocket();
	}
	public void openSocket(){
		try{
			sock = new DatagramSocket(50411, InetAddress.getByName("0.0.0.0"));
			sock.setBroadcast(true);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	public void closeSocket(){
		sock.close();
		System.out.println("Socket has been closed!");
	}
	public void clientListener(){
		try{
			while(true){
				System.out.println("Waiting for client.");
				byte[] buffer = new byte[16000];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				sock.receive(packet);
				
				System.out.println("Received packet from: " + packet.getAddress().getHostAddress());
				System.out.println("Data: " + new String(packet.getData()).trim());
				String message = new String(packet.getData()).trim();
				if(message.equals("QUANTANET_CHAOTICNUMBERS_CLIENT_REQUEST")){
					byte[] data = "QUANTANET_CHAOTICNUMBERS_CLIENT_RESPONSE".getBytes();
					DatagramPacket responsePacket = new DatagramPacket(data, data.length, packet.getAddress(), packet.getPort());
					sock.send(responsePacket);
					System.out.println("Sent response to: " + responsePacket.getAddress().getHostAddress());
					
				}
			}
		//	closeSocket();
		//	break;
		} catch (Exception e){
			
		}
	}
}
