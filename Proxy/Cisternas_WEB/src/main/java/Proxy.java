import java.nio.charset.Charset;

import ed25519.ed25519;
import util.Print;

public class Proxy {

	static final String HEXES = "0123456789abcdef";
	
	public static String getHex( byte [] raw ) {
		if ( raw == null ) {
			return null;
		}
		final StringBuilder hex = new StringBuilder( 2 * raw.length );
		for ( final byte b : raw ) {
	    	hex.append(HEXES.charAt((b & 0xF0) >> 4))
	    	.append(HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}
	
	public static String getHexN( byte [] raw ) {
		if ( raw == null ) {
			return null;
		}
		final StringBuilder hex = new StringBuilder( 2 * raw.length );
		for ( final byte b : raw ) {
	    	hex.append(String.format("0x%02X", b)).append(",");
		}
		return hex.toString();
	}
	
	
	
	
	public static void main(String[] args) {
		/*byte[] sk = new byte[]{(byte)0x9d,(byte)0x61,(byte) 0xb1, (byte)0x9d,
							   (byte) 0xef,(byte) 0xfd,(byte) 0x5a, (byte)0x60,
							   (byte)0xba,(byte) 0x84,(byte) 0x4a,(byte) 0xf4,
							   (byte) 0x92,(byte) 0xec,(byte) 0x2c,(byte) 0xc4,
							   (byte)0x44,(byte) 0x49,(byte) 0xc5,(byte) 0x69,
							   (byte) 0x7b, (byte)0x32,(byte) 0x69,(byte) 0x19,
							   (byte)0x70, (byte)0x3b,(byte) 0xac, (byte)0x03,
							   (byte) 0x1c, (byte)0xae, (byte)0x7f,(byte) 0x60};*/
	
		
		
		
		String skString="9d61b19deffd5a60ba844af492ec2cc44449c5697b326919703bac031cae7f60";
		byte[] sk=javax.xml.bind.DatatypeConverter.parseHexBinary(skString);
		
		//System.out.println(Print.getHex(sk));
		
		long tempoInicial = System.currentTimeMillis();
		byte[] pk = ed25519.publickey(sk);
		
		long tempoFinal= System.currentTimeMillis()-tempoInicial;
		System.out.println("Derivar chave publica durou: " + tempoFinal);
		System.out.println("private for 0 is \"" + getHexN(sk) + "\"");
		System.out.println("publickey for 0 is \"" + getHexN(pk) + "\"");

		String msg="nao";
		byte[] message = msg.getBytes();
		
		tempoInicial = System.currentTimeMillis();
		byte[] signature = ed25519.signature(message, sk, pk);
		tempoFinal= System.currentTimeMillis()-tempoInicial;
		System.out.println("Assinar durou: " + tempoFinal);
		System.out.println("signature(\""+msg+"\") = "+getHex(signature));
		//System.out.println("signature(\""+msg+"\") = "+getHexN(signature));
		
		
		
		
		
		try {
			tempoInicial = System.currentTimeMillis();
			System.out.println("check signature result:\n"+ed25519.checkvalid(signature,message,pk));
			tempoFinal= System.currentTimeMillis()-tempoInicial;
			System.out.println("Verificar durou: " + tempoFinal);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
