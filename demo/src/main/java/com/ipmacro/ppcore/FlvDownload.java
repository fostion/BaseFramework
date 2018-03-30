package com.ipmacro.ppcore;

public class FlvDownload extends AppleDownload {
	public void start(String playUrl) {
		mApple = new FlvApple(playUrl, mHeader);
	}
}
