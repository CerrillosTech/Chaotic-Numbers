package com.cerrillostech.chaoticnumbers.quantanet;

public class QuantaPacket {
	public enum Header {
		ACCEPT("ACC"), DECLINE("DEC"), RESEND("RES"), REGISTRATION("REG"), CHALLENGE("CHA"), INVALID("INV"), BROADCAST("BRO"), DISCOVERY("DIS");
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
			} else if(in.equals("REG")){
				return Header.REGISTRATION;
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
		CLIENT("CLI"), SERVER("SER"), REQUEST("REQ"), RESPONSE("RES"), INVALID("INV"), INTEGER("INT"), STRING("STR"), DOUBLE("DOU");
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
			} else if(in.equals("INT")){
				return Type.INTEGER;
			} else if(in.equals("STR")){
				return Type.STRING;
			} else if(in.equals("DOU")){
				return Type.DOUBLE;
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
		HEADER("HEA"), FROMTYPE("FROT"), TOTYPE("TOT"), TYPE("TYP"), BLANK(" "), DATA("DAT");
		private final String value;
		private Prefix(String value){
			this.value = value;
		}
		public String toString(){
			return this.value;
		}
		public static Prefix parse(String in){
			if(in.equals("HEA")){
			//	return 
			} else if(in.equals("FROT")){
				
			}
			return Prefix.BLANK;
		}
	}
	/** end enums */
	private Header header;
	private Type type, fromType, toType, dataType;
	private String data;
	public QuantaPacket(){
		this.header = Header.INVALID;
		this.type = Type.INVALID;
		this.fromType = Type.INVALID;
		this.toType = Type.INVALID;
		this.dataType = Type.INVALID;
	}
	public QuantaPacket(Header header, Type type){
		this.header = header;
		this.type = type;
		this.fromType = Type.INVALID;
		this.toType = Type.INVALID;
		this.dataType = Type.INVALID;
	}
	public QuantaPacket(String packetData){
		Header header = Header.INVALID;
		Type type = Type.INVALID;
		Type fromType = Type.INVALID;
		Type toType = Type.INVALID;
		Type dataType = Type.INVALID;
		String[] splitStrings = packetData.split("\\|");
		for(int x = 0; x < splitStrings.length; x++){
			if(splitStrings[x].equals(Prefix.HEADER.value)){
				header = Header.parse(splitStrings[x+1]);
			}
			if(splitStrings[x].equals(Prefix.FROMTYPE.value)){
				fromType = Type.parse(splitStrings[x+1]);
			}
			if(splitStrings[x].equals(Prefix.TOTYPE.value)){
				toType = Type.parse(splitStrings[x+1]);
			}
			if(splitStrings[x].equals("CT")){
				
			}
			if(splitStrings[x].equals(Prefix.TYPE.value)){
				type = Type.parse(splitStrings[x+1]);
			}
			if(splitStrings[x].equals(Prefix.DATA.value)){
				dataType = Type.parse(splitStrings[x+1]);
				this.data = splitStrings[x+2];
			}
		}
		this.header = header;
		this.type = type;
		this.fromType = fromType;
		this.toType = toType;
		this.dataType = dataType;
	}
	public void setDataType(Type type){
		this.dataType = type;
	}
	public Type getDataType(){
		return this.dataType;
	}
	public void setData(String data){
		this.data = data;
	}
	public String getData(){
		return this.data;
	}
	public void setHeader(Header head){
		this.header = head;
	}
	public Header getHeader(){
		return this.header;
	}
	public void setType(Type type){
		this.type = type;
	}
	public Type getType(){
		return this.type;
	}
	public void setFromType(Type type){
		this.fromType = type;
	}
	public Type getFromType(){
		return this.fromType;
	}
	public void setToType(Type type){
		this.toType = type;
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
		if(dataType.isValid()){
			output = output+Prefix.DATA.toString()+"|"+this.dataType.toString()+"|"+this.data+"|";
		}
		return output;
	}
	/** Generic boolean checks */
	public boolean toTypeIs(Type type){
		if(this.toType.equals(type)){
			return true;
		} else {
			return false;
		}
	}
	public boolean fromTypeIs(Type type){
		if(this.fromType.equals(type)){
			return true;
		} else {
			return false;
		}
	}
	public boolean typeIs(Type type){
		if(this.type.equals(type)){
			return true;
		} else {
			return false;
		}
	}
	public boolean headerIs(Header head){
		if(this.header.equals(head)){
			return true;
		}
		return false;
	}
	public boolean dataTypeIs(Type type){
		if(this.dataType.equals(type)){
			return true;
		}
		return false;
	}
	/** is boolean checks */
	public boolean isBroadcast(){
		if(this.header.equals(Header.BROADCAST)){
			return true;
		} else {
			return false;
		}
	}
	public boolean isDiscovery(){
		if(this.header.equals(Header.DISCOVERY)){
			return true;
		} else {
			return false;
		}
	}
	public boolean isAccept(){
		if(this.header.equals(Header.ACCEPT)){
			return true;
		}
		return false;
	}
	public boolean isDecline(){
		if(this.header.equals(Header.DECLINE)){
			return true;
		}
		return false;
	}
	public boolean isResend(){
		if(this.header.equals(Header.RESEND)){
			return true;
		}
		return false;
	}
	public boolean isChallenge(){
		if(this.header.equals(Header.CHALLENGE)){
			return true;
		}
		return false;
	}
	public boolean isInvalid(){
		if(this.header.equals(Header.INVALID)){
			return true;
		}
		return false;
	}
	public boolean isRegistration(){
		if(this.headerIs(Header.REGISTRATION)){
			return true;
		}
		return false;
	}
}
