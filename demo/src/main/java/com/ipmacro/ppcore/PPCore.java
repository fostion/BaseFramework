package com.ipmacro.ppcore;

import android.content.Context;
import android.util.Log;
import java.util.Date;


public class PPCore {

	public static boolean isLoadSo = false;

	private PPCore() {

	}

	public static int init(Context Ctx) {// ���ǩ��
		int ret = nativeInit(Ctx);
		if (ret != 0) {
			Log.e("PPCore", "init=" + ret);
		}
		return ret;
	}

	public static int login4(Context Ctx, int SN) {// ɽկ����
		int ret = nativeLogin4(Ctx, SN);
		if (ret != 0) {
			Log.e("PPCore", "login4=" + ret);
			return ret;
		}
		start();
		return 0;
	}

	public static byte[] mMutex = new byte[1];
	private static boolean mRun;
	public static int mSleepTime = 10;

	private static class TaskThread extends Thread {
		public void run() {
			while (mRun) {
				synchronized (mMutex) {
					oneClick();
				}
				try {
					sleep(mSleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			nativeLogout();

		}
	};

	private static TaskThread mThread;

	private static void start() {
		if (mRun) {
			return;
		}
		mRun = true;
		Log.i("PPCore", "Login OK");
		Log.i("SN", "" + getSN());
		Log.i("Life", "" + getLife());
		Date d = new Date();
		d.setTime(getExpire() * (long) 1000);
		Log.i("Expire", d.toString());
		Log.i("HttpUrl", getHttpUrl());
		Log.i("RtspUrl", getRtspUrl());
		oneClick();
		mThread = new TaskThread();
		mThread.setName("PPCore");
		mThread.start();
	}

	public static void DbgMsg(String Msg) {
		nativeDbgMsg(Msg);
	}

	public static long getSN() {
		if (!mRun) {
			return 0;
		}
		int a = nativeGetSN();
		if(a >=0) {
			return a;
		}
		return 0x100000000l + a;	
	}
	

	public static int getLife() {
		if (!mRun) {
			return 0;
		}
		return nativeGetLife();
	}

	public static int getTimer() {
//		return nativeGetTimer();
		return 0;
	}

	public static String getLiveKey() {// Fire
		if (!mRun) {
			return null;
		}
		return nativeGetLiveKey();
	}

	public static String getIptvKey() {// yison
		return nativeGetIptvKey();
	}

	public static String getMacKey() {// yison
		return nativeGetMacKey();
	}

	public static String getLetvKey(String stream_id, int tm, int splatid) {
		if (!mRun) {
			return null;
		}
		return nativeGetLetvKey(stream_id, tm, splatid);
	}

	public static String getLinkShell(int tm) {
		if (!mRun) {
			return null;
		}
		return nativeGetLinkShell(tm);
	}

	public static int getExpire() {
		if (!mRun) {
			return 0;
		}
		return nativeGetExpire();
	}

	public static String getRtspUrl() {
		if (!mRun) {
			return "";
		}
		return nativeGetRtspUrl();
	}

	public static String getHttpUrl() {
		if (!mRun) {
			return "";
		}
		return nativeGetHttpUrl();
	}

	public static boolean logout() {
		if (!mRun) {
			return false;
		}
		Log.i("PPCore", "Logout Pending");
		mRun = false;
		try {
			mThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (mThread.isAlive()) {
			Log.e("PPCore", "Thread Still Running");
		}
		mThread = null;
		return true;
	}




	public static void oneClick() {
		nativeOneClick();

	}

	public static void setLog(boolean Enable) {
		nativeSetLog(Enable);
	}


	private static native int nativeInit(Context Ctx);

	private static native int nativeInit2(Context Ctx);

	// private static native boolean nativeCheckCdKey(String CdKey);

	// private static native String nativeGetPwd();

	private static native int nativeLogin3(Context Ctx, String Token);

	private static native int nativeLogin4(Context Ctx, int SN);

	private static native int nativeLogin5(Context Ctx, String Token, int SN);

	private static native boolean nativeLogout();

	private static native boolean nativeOneClick();

	private static native int nativeGetSN();

	private static native int nativeGetLife();

	private static native int nativeGetExpire();

	private static native String nativeGetRtspUrl();

	private static native String nativeGetHttpUrl();

	private static native void nativeDbgMsg(String Msg);

	private static native void nativeSetLog(boolean Enable);

	private static native String nativeGetLiveKey();

	private static native String nativeGetIptvKey();

	private static native String nativeGetMacKey();

//	private static native int nativeGetTimer();

	private static native String nativeGetLetvKey(String stream_id, int tm,
                                                  int splatid);

	private static native String nativeGetLinkShell(int tm);

	static {
		System.loadLibrary("stlport_shared");
		System.loadLibrary("PPCoreJni");
		System.loadLibrary("rtmp-1");
		System.loadLibrary("avutil");
		System.loadLibrary("avcodec");
		System.loadLibrary("avformat");
		System.loadLibrary("AvAppleJni");
		isLoadSo = true;
	}

	public static void loadLib() {
	}
}
