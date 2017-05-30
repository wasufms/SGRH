package util;



public class Config {
    public static String HOST="130.206.125.57";
    public static String PORT="1026";
   // public static String sk_String= "9d61b19deffd5a60ba844af492ec2cc44449c5697b326919703bac031cae7f60";



   // public static byte[]SK=javax.xml.bind.DatatypeConverter.parseHexBinary(sk_String);

    //public static byte[]SK=sk_String.getBytes();
    //public static byte[]SK=org.apache.commons.codec.binary.Hex.;


    public static String SERVIDOR="http://"+Config.HOST+":"+ Config.PORT;



	/*public static void  main(String args[]) {
		System.out.println(Config.HOST);
		System.out.println(Config.PORT);
		System.out.println(Config.SERVIDOR);
	}*/
	/*public static byte[] sk = new byte[]{(byte)0x9d,(byte)0x61,(byte) 0xb1, (byte)0x9d,
	   (byte) 0xef,(byte) 0xfd,(byte) 0x5a, (byte)0x60,
	   (byte)0xba,(byte) 0x84,(byte) 0x4a,(byte) 0xf4,
	   (byte) 0x92,(byte) 0xec,(byte) 0x2c,(byte) 0xc4,
	   (byte)0x44,(byte) 0x49,(byte) 0xc5,(byte) 0x69,
	   (byte) 0x7b, (byte)0x32,(byte) 0x69,(byte) 0x19,
	   (byte)0x70, (byte)0x3b,(byte) 0xac, (byte)0x03,
	   (byte) 0x1c, (byte)0xae, (byte)0x7f,(byte) 0x60};*/

}
