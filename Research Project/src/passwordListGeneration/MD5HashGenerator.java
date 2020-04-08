package passwordListGeneration;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class MD5HashGenerator implements HashGenerator  {
	
	public String generateHash(String word) {
	    String md5Hash = "";
	    	  MessageDigest md;
			try {
				md = MessageDigest.getInstance("MD5");
				  md.update(word.getBytes());
				    byte[] digest = md.digest();
				    md5Hash = DatatypeConverter
				      .printHexBinary(digest).toUpperCase();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			 
	    
		return md5Hash;

	}
	
    

}
