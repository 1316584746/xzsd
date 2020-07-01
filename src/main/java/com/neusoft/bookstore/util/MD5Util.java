package com.neusoft.bookstore.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

	
	private static final String salt = "1a2b3c4d";

	/**
	 * 第一次md5
	 * @param inputPass
	 * @return
	 */
	public static String inputPassToFormPass(String inputPass) {
		return DigestUtils.md5Hex(inputPass);
	}

	/**
	 * 第二次md5
	 * @param formPass
	 * @param salt
	 * @return
	 */
	public static String formPassToDBPass(String formPass, String salt) {
		String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5) + salt.charAt(4);
		return DigestUtils.md5Hex(str);
	}
	
	public static void main(String[] args) {
		System.out.println(inputPassToFormPass("123456"));//d3b1294a61a07da9b49b6e22b2cbd7f9
		System.out.println(inputPassToFormPass("123456"));
	}






}