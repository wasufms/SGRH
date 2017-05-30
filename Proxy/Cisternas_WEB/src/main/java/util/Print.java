package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Print {

	
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
	
	public static String getDec( byte [] raw ) {
		if ( raw == null ) {
			return null;
		}
		final StringBuilder hex = new StringBuilder( 2 * raw.length );
		for ( final byte b : raw ) {
	    	hex.append(String.format("0x%02X", b)).append(",");
		}
		return hex.toString();
	}
	
	public static void log(String body){
	    FileWriter arq;
		try {
			arq = new FileWriter("/tmp/file.txt");
			PrintWriter gravarArq = new PrintWriter(arq);
			
			gravarArq.printf(body);
			arq.close();
	    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(stringToHex("411123-321"));
		
		
	}

	public static String stringToHex(String te) {
		
		System.out.println(te);
		int tam=te.length();
		int j=0;
		String retorno="";
		char[] arr=te.toCharArray();
		for(int i=0;i<tam/2;i++){
			String fa="0x"+arr[j]+arr[j+1];
			retorno+=Integer.decode(fa);
			j+=2;
		}
		return retorno;
	}
	public static byte[] stringToHexByte(String te) {
        int tam=te.length();
        int j=0;
        char[] arr=te.toCharArray();
        byte[] signature =new byte [tam];
        for(int i=0;i<tam/2;i++){
            String fa="0x"+arr[j]+arr[j+1];
            int iq=Integer.decode(fa);
            signature[i]=(byte)iq;
            j+=2;
        }
        return signature;
    }
	
	
	

}
