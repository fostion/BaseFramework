package com.ipmacro.ppcore;

import com.ipmacro.ppcore.TsApple;

public class TsDownload extends AppleDownload {
	public void start(String playUrl) {
		mApple = new TsApple(playUrl, mHeader);
	}
}
