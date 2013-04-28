package com.cerrillostech.chaoticnumbers;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.prefs.*;
import java.util.Enumeration;
import java.util.UUID;
public class QuantaNet {
	private static Preferences pref = Preferences.userRoot();
	private String thisUUID;
	public QuantaNet(boolean check){
		if(check){
			if(checkUUID()){
				thisUUID = fetchUUID();
			} else {
				setUUID(generateUUID());
			}
		}
		System.out.println("UUID: " + thisUUID);
	}
	public boolean checkUUID(){
		String tempUUID = pref.get("QuantaNet.UUID", null);
		if(tempUUID==null){
			return false;
		} else {
			return true;
		}
	}
	private static String generateUUID(){
		return UUID.randomUUID().toString();
	}
	private String fetchUUID(){
		return pref.get("QuantaNet.UUID", null);
	}
	private void setUUID(String tempUUID){
		pref.put("QuantaNet.UUID", tempUUID);
		this.thisUUID = tempUUID;
	}
	public static void removeUUID(){
		pref.remove("QuantaNet.UUID");
	}
	public void clientMode(){
		Thread asdf = new BroadcastClient();
	//	asdf.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Thread asdfff2 = new BroadcastServer();
	//	asdfff2.start();
	}
	public class BroadcastClient extends Thread {
		private DatagramSocket sock;
		private InetAddress foundServer;
		public void run(){
			System.setProperty("java.net.preferIPv4Stack" , "true");
			openSocket();
			broadcastDiscovery();
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
		}
		public void broadcastDiscovery(){
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
			} catch (Exception e){
				e.printStackTrace();
			}
			try{
				byte[] buffer = new byte[16000];
				DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
				sock.receive(receivePacket);
				System.out.println("Broadcast response from server: " + receivePacket.getAddress().getHostAddress());
				String message = new String(receivePacket.getData()).trim();
				if(message.equals("QUANTANET_CHAOTICNUMBERS_CLIENT_RESPONSE")){
					System.out.println("Server: " + receivePacket.getAddress().getHostAddress() + " has been validated!");
					this.foundServer = receivePacket.getAddress();
					handshake();
				}
			} catch (Exception e){
				System.out.println("Response Time Out! Retrying!");
				broadcastDiscovery();
			}
			
		}
	}
	public class BroadcastServer extends Thread {
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
					System.out.println("Data: " + new String(packet.getData()));
					String message = new String(packet.getData()).trim();
					if(message.equals("QUANTANET_CHAOTICNUMBERS_CLIENT_REQUEST")){
						byte[] data = "QUANTANET_CHAOTICNUMBERS_CLIENT_RESPONSE".getBytes();
						DatagramPacket responsePacket = new DatagramPacket(data, data.length, packet.getAddress(), packet.getPort());
						sock.send(responsePacket);
						System.out.println("Sent response to: " + responsePacket.getAddress().getHostAddress());
						closeSocket();
						break;
					}
				}
			} catch (Exception e){
				
			}
		}
	}
}
