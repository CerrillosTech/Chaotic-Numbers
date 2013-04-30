package com.cerrillostech.chaoticnumbers.quantanet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Random;

import com.cerrillostech.chaoticnumbers.quantanet.QuantaPacket.Header;
import com.cerrillostech.chaoticnumbers.quantanet.QuantaPacket.Type;

public class ClientBroadcast extends Thread {
	private DatagramSocket sock;
	private InetAddress foundServer;
	private int chaAns;
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
		Random ran = new Random();
		chaAns = ran.nextInt(50000);
		int cha = ((chaAns+42)*24);
		QuantaPacket chapac = new QuantaPacket();
		chapac.setHeader(Header.CHALLENGE);
		chapac.setType(Type.REQUEST);
		chapac.setDataType(Type.INTEGER);
		chapac.setData(cha+"");
		chapac.setFromType(Type.CLIENT);
		chapac.setToType(Type.SERVER);
		sendChallenge(chapac);
	//	QuantaPacket
		//Send challenge
		// :SH:C_A:EH:
	}
	public void sendChallenge(QuantaPacket qp){
		byte[] data = qp.toString().getBytes();
		try{
			DatagramPacket packet = new DatagramPacket(data, data.length, this.foundServer, 50411);
			sock.send(packet);
			System.out.println("Challenge Request sent to: "+this.foundServer.getHostAddress());
		} catch (Exception e){
			e.printStackTrace();
		}
		try{
			byte[] buffer = new byte[16000];
			DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
			sock.receive(receivePacket);
			System.out.println("Challenge response from server: " + receivePacket.getAddress().getHostAddress());
			String message = new String(receivePacket.getData()).trim();
			QuantaPacket cr = new QuantaPacket(message);
			if(cr.isChallenge() && cr.typeIs(Type.RESPONSE) && cr.fromTypeIs(Type.SERVER) && cr.toTypeIs(Type.CLIENT) && cr.getDataType().equals(Type.INTEGER)){
				int response = Integer.parseInt(cr.getData());
				if(response==chaAns){
					System.out.println("Challenge completed!");
				} else {
					System.out.println("Challege answered incorrectly!!");
				}
			}
		} catch (Exception e){
			System.out.println("Challenge Time out! Sending new challenge!");
			handshake();
		}
	}
	public void broadcast(){
		QuantaPacket reqpac = new QuantaPacket();
		reqpac.setHeader(Header.DISCOVERY);
		reqpac.setType(Type.REQUEST);
		reqpac.setFromType(Type.CLIENT);
		reqpac.setToType(Type.SERVER);
		byte[] data = reqpac.toString().getBytes();
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
					if(broadcast != null){
						DatagramPacket packet = new DatagramPacket(data, data.length, broadcast, 50411);
						sock.send(packet);
						System.out.println("Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
					}
					
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
			QuantaPacket qpp = new QuantaPacket();
			qpp.setHeader(Header.DISCOVERY);
			qpp.setType(Type.RESPONSE);
			qpp.setFromType(Type.SERVER);
			qpp.setToType(Type.CLIENT);
			if(message.equals(qpp.toString())){
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
