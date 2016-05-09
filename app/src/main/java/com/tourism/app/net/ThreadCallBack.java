package com.tourism.app.net;

import java.io.InputStream;
import java.io.Serializable;

public interface ThreadCallBack extends Serializable {
	public void onCallbackFromThread(String resultJson);
	public void onCallbackFromThread(InputStream is, int resultCode);
	public void onCallbackFromThread(String resultJson, int resultCode);
}
