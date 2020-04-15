package passwordListGeneration;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SHA1HashGenerator implements HashGenerator {

	@Override
	public String generateHash(String plainTextPassword) {

		String hashedPassword = "";
		try {
			MessageDigest msdDigest = MessageDigest.getInstance("SHA-1");
			msdDigest.update(plainTextPassword.getBytes("UTF-8"), 0, plainTextPassword.length());
			hashedPassword = SHA1HashGenerator.bytesToHex((msdDigest.digest()));
		} catch (Exception e) {
			System.out.println("HASHING FAILED");
		}
		return hashedPassword;
	}
	
	//https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
	private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes();
	public static String bytesToHex(byte[] bytes) {
	    byte[] hexChars = new byte[bytes.length * 2];
	    for (int j = 0; j < bytes.length; j++) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	    }
	    return new String(hexChars, StandardCharsets.UTF_8);
	}

}
