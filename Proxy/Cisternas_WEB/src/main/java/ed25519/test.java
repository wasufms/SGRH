package ed25519;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import util.Print;

public class test {

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
	  
	public static void main(String[] args) throws IOException {
		Date hoje = new Date();
		SimpleDateFormat df;
		df = new SimpleDateFormat("dd-MM-yyyy");
		String data = new String(df.format(hoje));
		System.out.println(data);
			
	}

}