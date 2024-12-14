package com.ugex.savelar.texttovoice;

import java.util.Locale;


import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class BGSpeechWhenCopyService extends Service {
	private boolean needToVoice=true;
	private ClipboardManager clipboardManager;
	private TextToSpeech txtToSpeech;
	class MyTextToSpeechInitListener implements TextToSpeech.OnInitListener{

		@Override
		public void onInit(int status) {
			if(status==TextToSpeech.SUCCESS)
			{
				int res=txtToSpeech.setLanguage(Locale.getDefault());
				if(res==TextToSpeech.LANG_MISSING_DATA||res==TextToSpeech.LANG_NOT_SUPPORTED)
				{
					//Toast.makeText(MainActivity.this, "语言设置不成功，默认为英文模式！", Toast.LENGTH_SHORT).show();
					Log.i("TestDbg", "语言设置不成功，默认为英文模式！");
				}else
				{
					txtToSpeech.setLanguage(Locale.US);
				}
			}
		}

	}
	private void InitService(){
		txtToSpeech=new TextToSpeech(this, new MyTextToSpeechInitListener());
		clipboardManager=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
		clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
			@Override
			public void onPrimaryClipChanged() {
				Log.i("TestDbg", "剪切板内容改变");
				if (needToVoice
						&&clipboardManager.hasPrimaryClip()
						&& clipboardManager.getPrimaryClip().getItemCount() > 0) {
					CharSequence addedText = clipboardManager.getPrimaryClip().getItemAt(0).getText();
					String addedTextString = String.valueOf(addedText);
					Log.i("TestDbg", "剪切板内容朗读");
					txtToSpeech.speak(addedTextString, TextToSpeech.QUEUE_FLUSH, null);
				}


			}
		});
	}
	@Override
	public void onCreate() {
		InitService();
		super.onCreate();
	}
	@Override
	public void onDestroy() {
		txtToSpeech.stop();
		txtToSpeech.shutdown();
		super.onDestroy();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

}
