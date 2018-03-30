package com.ipmacro.ppcore;

import android.util.Log;

public class TsApple extends Apple {

	public TsApple(String Url, String Header) {
		Log.i("test_log", "url: "+Url+"  header:"+Header);

		synchronized (PPCore.mMutex) {
			nativeSetup(Url, Header);
		}
	}

	private native boolean nativeSetup(String Url, String Header);
}
