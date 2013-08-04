package main.util;

public class StringUtil {

	public static synchronized String getLeftSubString(String value, int indent) {
		return value.substring(0, value.length() - indent);
	}

	public static synchronized String getRightSubString(String value, int indent) {
		return value.substring(value.length() - indent, value.length());
	}

	public static synchronized String trimFileExt(String value) {
		return value.substring(0, value.lastIndexOf('.'));
	}

	public static synchronized int getFileExtlength(String value) {
		return value.length() - trimFileExt(value).length();
	}

}
