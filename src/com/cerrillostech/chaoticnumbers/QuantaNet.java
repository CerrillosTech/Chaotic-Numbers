package com.cerrillostech.chaoticnumbers;
import java.util.prefs.*;
import java.util.UUID;
import com.cerrillostech.chaoticnumbers.quantanet.ClientBroadcast;
import com.cerrillostech.chaoticnumbers.quantanet.ServerBroadcast;

public class QuantaNet {
	private static Preferences pref;
	private String thisUUID;
	private Thread server;
	private Thread client;
	public QuantaNet(boolean check){
		try{
			pref = Preferences.userRoot();
		} catch(Exception e){
			
		}
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
		String tempUUID = null;
		try{
			tempUUID = pref.get("QuantaNet.UUID", null);
		} catch (Exception e){
			
		}
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
		client = new ClientBroadcast();
		client.start();
	}
	public void serverMode(){
		server = new ServerBroadcast();
		server.start();
	}
	public void closeClient(){
	//	client.stop();
	//	client.destroy();
	}
	public void closeServer(){
	//	server.stop();
	//	server.destroy();
	}
}
