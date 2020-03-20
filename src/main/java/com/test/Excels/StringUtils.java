package com.test.Excels;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     * 判断是否为空值 * @return 
     */
    public static boolean isEmpty(String str) {
        return (str == null || str == "" || str.length() == 0);
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * 判断是否为合法IP * @return the ip
     */
    public static boolean isboolIp(String ipAddress) {
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    
    /**
     * 判断是否为合法DateFormat * @return 
     */
    public static boolean isboolDate(String dateFormat){
    	
    	String eL = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
    	Pattern pattern = Pattern.compile(eL);
    	Matcher matcher = pattern.matcher(dateFormat);
    	return matcher.matches();
    }
    
}
