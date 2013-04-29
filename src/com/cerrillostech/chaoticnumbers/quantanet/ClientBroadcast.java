package com.cerrillostech.chaoticnumbers.quantanet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class ClientBroadcast extends Thread {
	private DatagramSocket sock;
	private InetAddress foundServer;
	public void run(){
		System.setProperty("java.net.preferIPv4Stack" , "true");
		openSocket();
		broadcast();
		closeSocket();
	}
	public void openSocket(){
		try{
			sock = new DatagramSocket();
			sock.setBroadcast(true);
			sock.setSoTimeout(5000);
			System.out.println("Socket has been opened.");
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	public void closeSocket(){
		sock.close();
		System.out.println("Socket has been closed!");
	}
	public void handshake(){
		System.out.println("Attempting handshake with: "+foundServer.getHostAddress());
		//Send challenge
		// :SH:C_A:EH:
	}
	public void broadcast(){
		byte[] data = "QUANTANET_CHAOTICNUMBERS_CLIENT_REQUEST".getBytes();
		try{
			DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName("255.255.255.255"), 50411);
			sock.send(packet);
			System.out.println("Request packet sent to: 255.255.255.255");
		} catch (Exception e){
			e.printStackTrace();
		}
		try{
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()){
				NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
				if(networkInterface.isLoopback() || !networkInterface.isUp()){
					continue;
				}
				for(InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()){
					InetAddress broadcast = interfaceAddress.getBroadcast();
					if(broadcast == null){
						continue;
					}
					DatagramPacket packet = new DatagramPacket(data, data.length, broadcast, 50411);
					sock.send(packet);
					System.out.println("Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
				}
			}
			System.out.println("Finished broadcasting on all interfaces.\nWaiting for reply...");
			listen();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public void listen(){
		try{
			byte[] buffer = new byte[16000];
			DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
			sock.receive(receivePacket);
			System.out.println("Broadcast response from server: " + receivePacket.getAddress().getHostAddress());
			String message = new String(receivePacket.getData()).trim();
			respondToReply(message, receivePacket, 0);
		} catch (Exception e){
			System.out.println("Response Time Out! Retrying!");
			broadcast();
		}
	}
	public void respondToReply(String message, DatagramPacket receivePacket, int count){
		if(count!=3){
			if(message.equals("QUANTANET_CHAOTICNUMBERS_CLIENT_RESPONSE")){
				System.out.println("Server: " + receivePacket.getAddress().getHostAddress() + " has been validated!");
				this.foundServer = receivePacket.getAddress();
				handshake();
			} else {
				System.out.println("Server: " + receivePacket.getAddress().getHostAddress() + " has sent INVALID DATA!\nResending Reply " + (3-count) + " more times.");
				respondToReply(message, receivePacket, count+1);
			}
		} else {
			System.out.println("Retrying broadcast!");
			broadcast();
		}
	}
}
