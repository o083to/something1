package utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonUtils {

	public static boolean stringIsEmpty(String str) {
		return (str == null || str.trim().length() == 0);
	}
	
	public static String getSha1HashCode(String arg) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			messageDigest.reset();
			messageDigest.update(arg.getBytes("UTF-8"));
			return new BigInteger(1, messageDigest.digest()).toString(16);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			//TODO: do something
			e.printStackTrace();
			return arg;
		}
	}
	
}
