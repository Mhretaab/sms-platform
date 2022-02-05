package com.haud.smsgateway.utils;

public final class StringUtils {

	public static boolean isNullOrEmpty(String str){
		if(str == null) return true;
		if(str.trim().isEmpty()) return true;

		return false;
	}
}
