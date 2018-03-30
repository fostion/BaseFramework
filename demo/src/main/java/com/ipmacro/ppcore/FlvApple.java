package com.ipmacro.ppcore;

public class FlvApple extends Apple {

	public FlvApple(String Url, String Header) {
		synchronized (PPCore.mMutex) {
			nativeSetup(Url, Header);
		}
	}

	private native boolean nativeSetup(String Url, String Header);
}
