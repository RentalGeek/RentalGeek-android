package com.app.rentalgeek;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Application;
/**
 * @author george
 * 
 */
public class UtilsGcm extends Application {
	public static String GCMSenderId = "640488877122";

	public static boolean notificationReceived;
 
	public static String warningId = "", msg = "", registrationId = "", postid="",type="";
	
	
//	public static String Url = "https://alpha.drinqsmart.com:8043/ajax/";
	        
	public static String changestatus = "";

	public static String hash256(String data)  { 
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(data.getBytes());
			return bytesToHex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuffer result = new StringBuffer();
		for (byte byt : bytes)
			result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(
					1));
		return result.toString();
	}

}