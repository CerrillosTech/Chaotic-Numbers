package com.cerrillostech.chaoticnumbers.quantanet;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.cerrillostech.chaoticnumbers.quantanet.QuantaPacket.Header;
import com.cerrillostech.chaoticnumbers.quantanet.QuantaPacket.Type;

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
				QuantaPacket rpac = new QuantaPacket(message);
				if(rpac.isDiscovery() && rpac.fromTypeIs(Type.CLIENT) && rpac.toTypeIs(Type.SERVER) && rpac.typeIs(Type.REQUEST)){
					QuantaPacket qp = new QuantaPacket();
					qp.setHeader(Header.DISCOVERY);
					qp.setType(Type.RESPONSE);
					qp.setFromType(Type.SERVER);
					qp.setToType(Type.CLIENT);
					byte[] data = qp.toString().getBytes();
					DatagramPacket responsePacket = new DatagramPacket(data, data.length, packet.getAddress(), packet.getPort());
					sock.send(responsePacket);
					System.out.println("Sent response to: " + responsePacket.getAddress().getHostAddress());
					System.out.println("Data: "+qp.toString());
				}
				if(rpac.isChallenge() && rpac.typeIs(Type.REQUEST) && rpac.fromTypeIs(Type.CLIENT) && rpac.toTypeIs(Type.SERVER) && rpac.dataTypeIs(Type.INTEGER)){
					int challenge = Integer.parseInt(rpac.getData());
					int response = (int) ((challenge/24)-42);
					QuantaPacket resp = new QuantaPacket();
					resp.setHeader(Header.CHALLENGE);
					resp.setType(Type.RESPONSE);
					resp.setFromType(Type.SERVER);
					resp.setToType(Type.CLIENT);
					resp.setDataType(Type.INTEGER);
					resp.setData(response+"");
					byte[] data = resp.toString().getBytes();
					DatagramPacket respacket = new DatagramPacket(data, data.length, packet.getAddress(), packet.getPort());
					sock.send(respacket);
					System.out.println("Sent Challenge response to: " + respacket.getAddress().getHostAddress());
					System.out.println("Data: "+resp.toString());
				}
			}
		//	closeSocket();
		//	break;
		} catch (Exception e){
			
		}
	}
}
