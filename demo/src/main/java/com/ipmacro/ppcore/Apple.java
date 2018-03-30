package com.ipmacro.ppcore;

public class Apple {

	public int getProgress() {
		if (!PPCore.isLoadSo){
			return 0;
		}

		synchronized (PPCore.mMutex) {
			return nativeGetProgress();
		}
	}

	public int getDuration() {
		if (!PPCore.isLoadSo){
			return 0;
		}

		synchronized (PPCore.mMutex) {
			return nativeGetDuration();
		}
	}

	public int getRate() {
		if (!PPCore.isLoadSo){
			return 0;
		}

		synchronized (PPCore.mMutex) {
			return nativeGetRate();
		}
	}

	public void setParam(int Type, int Value) {
		if (!PPCore.isLoadSo){
			return;
		}
		synchronized (PPCore.mMutex) {
			nativeSetParam(Type, Value);
		}
	}

	public boolean isEof() {
		if (!PPCore.isLoadSo){
			return true;
		}
		synchronized (PPCore.mMutex) {
			return nativeIsEof();
		}
	}

	public boolean isFinish() {
		if (!PPCore.isLoadSo){
			return true;
		}
		synchronized (PPCore.mMutex) {
			return nativeIsFinish();
		}
	}

	public void stop() {
		if (!PPCore.isLoadSo){
			return;
		}
		synchronized (PPCore.mMutex) {
			// Log.i("Apple", "stop " + mNativeContext);
			nativeStop();
		}
	}

	public void finalize() {
		release();
	}

	public void release() {
		if (!PPCore.isLoadSo){
			return;
		}
		if (mNativeContext == 0) {
			return;
		}
		synchronized (PPCore.mMutex) {
			// if (mNativeContext != 0) {
			// Log.i("Apple", "release " + mNativeContext);
			// }
			try{
				nativeRelease();
			}catch (UnsatisfiedLinkError e){
				e.printStackTrace();
			}
		}
	}

	public String getUrl(String Ext) {
		synchronized (PPCore.mMutex) {
			long a = mNativeContext;
			if (a < 0) {
				a += 0x100000000L;
			}
			return PPCore.getHttpUrl() + "Apple/" + a + Ext;
		}
	}

	public String getUrl() {
		return getUrl("/playlist.m3u8");
	}

	private int mNativeContext;

	private native void nativeStop();

	private native void nativeRelease();

	private native int nativeGetProgress();

	private native int nativeGetDuration();

	private native int nativeGetRate();

	private native void nativeSetParam(int Type, int Value);

	private native boolean nativeIsEof();

	private native boolean nativeIsFinish();

	private static native void nativeInit();

	static {
		nativeInit();
	}

	public static void loadLib(){

	}
}