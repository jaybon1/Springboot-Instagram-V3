package com.cos.instagram.util;

import java.util.Arrays;
import java.util.List;

public class Utils {
	
	public static List<String> tagParse(String tags) {		
		String temp[] = tags.replace("#", "").split(" ");
		List<String> list = Arrays.asList(temp);
		
		return list;

	}
}
