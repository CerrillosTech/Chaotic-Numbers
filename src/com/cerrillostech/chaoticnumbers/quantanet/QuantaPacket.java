package com.cerrillostech.chaoticnumbers.quantanet;

public class QuantaPacket {
	public enum Header {
		ACCEPT("ACC"), DECLINE("DEC"), RESEND("RES"), CHALLENGE("CHA"), INVALID("INV"), BROADCAST("BRO"), DISCOVERY("DIS");
		private final String value;
		private Header(String value){
			this.value = value;
		}
		public String toString(){
			return this.value;
		}
		public static Header parse(String in){
			if(in.equals("ACC")){
				return Header.ACCEPT;
			} else if(in.equals("DEC")){
				return Header.DECLINE;
			} else if(in.equals("RES")){
				return Header.RESEND;
			} else if(in.equals("CHA")){
				return Header.CHALLENGE;
			} else if(in.equals("BRO")){
				return Header.BROADCAST;
			} else if(in.equals("DIS")){
				return Header.DISCOVERY;
			}
			return Header.INVALID;
		}
		public boolean isValid(){
			if(this.value.equals("INV")){
				return false;
			} else {
				return true;
			}
		}
	}
	public enum Type {
		CLIENT("CLI"), SERVER("SER"), REQUEST("REQ"), RESPONSE("RES"), INVALID("INV");
		private final String value;
		private Type(String value){
			this.value = value;
		}
		public String toString(){
			return this.value;
		}
		public static Type parse(String in){
			if(in.equals("REQ")){
				return Type.REQUEST;
			} else if(in.equals("RES")){
				return Type.RESPONSE;
			} else if(in.equals("CLI")){
				return Type.CLIENT;
			} else if(in.equals("SER")){
				return Type.SERVER;
			}
			return Type.INVALID;
		}
		public boolean isValid(){
			if(this.value.equals("INV")){
				return false;
			} else {
				return true;
			}
		}
	}
	public enum Prefix {
		HEADER("HEA"), FROMTYPE("FROT"), TOTYPE("TOT"), TYPE("TYP"), BLANK(" ");
		private final String value;
		private Prefix(String value){
			this.value = value;
		}
		public String toString(){
			return this.value;
		}
		public static Prefix parse(String in){
			if(in.equals("HEA")){
				
			} else if(in.equals("FROT")){
				
			}
			return Prefix.BLANK;
		}
	}
	private final Header header;
	private final Type type, fromType, toType;
	public QuantaPacket(Header header, Type type){
		this.header = header;
		this.type = type;
		this.fromType = Type.INVALID;
		this.toType = Type.INVALID;
	}
	public QuantaPacket(String packetData){
		Header header = Header.INVALID;
		Type type = Type.INVALID;
		Type fromType = Type.INVALID;
		Type toType = Type.INVALID;
		String[] splitStrings = packetData.split("\\|");
		for(int x = 0; x < splitStrings.length; x++){
			if(splitStrings[x].equals("HEA")){
				header = Header.parse(splitStrings[x+1]);
			}
			if(splitStrings[x].equals("FROT")){
				fromType = Type.parse(splitStrings[x+1]);
			}
			if(splitStrings[x].equals("TOT")){
				toType = Type.parse(splitStrings[x+1]);
			}
			if(splitStrings[x].equals("CT")){
				
			}
			if(splitStrings[x].equals("TYP")){
				type = Type.parse(splitStrings[x+1]);
			}
		}
		this.header = header;
		this.type = type;
		this.fromType = fromType;
		this.toType = toType;
	}
	
	public Header getHeader(){
		return this.header;
	}
	public Type getType(){
		return this.type;
	}
	public Type getFromType(){
		return this.fromType;
	}
	public Type getToType(){
		return this.toType;
	}
	public String toString(){
		String output = "|";
		if(header.isValid()){
			output = output+Prefix.HEADER.toString()+"|"+this.header.toString()+"|";
		}
		if(type.isValid()){
			output = output+Prefix.TYPE.toString()+"|"+this.type.toString()+"|";
		}
		if(fromType.isValid()){
			output = output+Prefix.FROMTYPE.toString()+"|"+this.fromType.toString()+"|";
		}
		if(toType.isValid()){
			output = output+Prefix.TOTYPE.toString()+"|"+this.toType.toString()+"|";
		}
		return output;
	}
	public boolean isBroadcast(){
		if(this.header.equals(Header.BROADCAST)){
			return true;
		} else {
			return false;
		}
	}
}
