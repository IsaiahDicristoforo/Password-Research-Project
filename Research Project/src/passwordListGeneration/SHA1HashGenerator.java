package passwordListGeneration;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class SHA1HashGenerator implements HashGenerator {

	@Override
	public String generateHash(String plainTextPassword) {

		String hashedPassword = "";
		try {
			MessageDigest msdDigest = MessageDigest.getInstance("SHA-1");
			msdDigest.update(plainTextPassword.getBytes("UTF-8"), 0, plainTextPassword.length());
			hashedPassword = DatatypeConverter.printHexBinary(msdDigest.digest());
		} catch (Exception e) {
			System.out.println("HASHING FAILED");
		}
		return hashedPassword;
	}

}
