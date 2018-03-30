package com.ipmacro.ppcore;

public class M3U8Download extends TsDownload {
	public void start(String playUrl) {
		super.start(playUrl.replace("http://", "m3u8://"));
	}
}
