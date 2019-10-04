package com.alex.tool.cript;

public class FucoAESUtil extends MCrypt {

	private static volatile FucoAESUtil instance;

	public FucoAESUtil(String sKey, String iv) {
//		super("tqhYgN4Xe0XFOSdn", "DyDgD3HPbZWxqAo4");
		super(sKey, iv);
	}

	public static FucoAESUtil getInstance(String sKey, String iv) {
		if (instance == null) {
			synchronized (FucoAESUtil.class) {
				instance = new FucoAESUtil(sKey,iv);
			}
		}
		return instance;
	}

}
