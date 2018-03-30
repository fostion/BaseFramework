package com.ipmacro.ppcore;

public class AppleDownload extends BaseDownload {

	Apple mApple;

	@Override
	public boolean prepare() {
		if (mApple == null) {
			return false;
		}
		return mApple.getDuration() > 0;
	}

	@Override
	public int getProgress() {
		if (mApple != null) {
			return mApple.getProgress();
		}
		return 0;
	}

	@Override
	public int getRate() {
		if (mApple != null) {
			return mApple.getRate();
		}
		return 0;
	}

	@Override
	public int getDuration() {
		if (mApple != null) {
			return mApple.getDuration();
		}
		return 0;
	}

	@Override
	public boolean isEof() {
		if (mApple != null) {
			return mApple.isEof();
		}
		return false;
	}

	@Override
	public boolean isFinish() {
		if (mApple != null) {
			return mApple.isFinish();
		}
		return false;
	}

	@Override
	public void stop() {
		if (mApple != null) {
			mApple.stop();
		}
	}

	@Override
	public void release() {
		if (mApple != null) {
			mApple.release();
			mApple = null;
		}
	}

	@Override
	public String getPlayUrl(String Ext) {
		if (mApple != null) {
			return mApple.getUrl(Ext);
		}
		return null;
	}

	public String getPlayUrl() {
		if (mApple != null) {
			return mApple.getUrl();
		}
		return null;
	}

	@Override
	public void setParam(int Type, int Value) {
		if (mApple != null) {
			mApple.setParam(Type, Value);
		}
	}

}
