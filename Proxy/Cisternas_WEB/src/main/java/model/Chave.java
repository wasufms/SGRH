package model;

public class Chave {
	static final String HEXES = "0123456789abcdef";
	
	private String id;
	private String publicKey;
	
	public Chave(String id, String publicKey) {
		super();
		this.id = id;
		this.publicKey = publicKey;
	}
	public Chave() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public byte[] toByteArray(){
		return javax.xml.bind.DatatypeConverter.parseHexBinary(this.publicKey);
	}
	public String toHex( ) {
		byte [] raw=javax.xml.bind.DatatypeConverter.parseHexBinary(this.publicKey);
		final StringBuilder hex = new StringBuilder( 2 * raw.length );
		for ( final byte b : raw ) {
	    	hex.append(HEXES.charAt((b & 0xF0) >> 4))
	    	.append(HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}
	
	public String toDec() {
		byte [] raw=javax.xml.bind.DatatypeConverter.parseHexBinary(this.publicKey);
		final StringBuilder dec = new StringBuilder( 2 * raw.length );
		for ( final byte b : raw ) {
	    	dec.append(String.format("0x%02X", b)).append(",");
		}
		String decimal=dec.toString().substring(0, dec.toString().length()-1);
		
		
		return decimal;
	}
	@Override
	public String toString() {
		return "Chave [id=" + id + ", publicKey=" + publicKey + "]";
	}
	
}
