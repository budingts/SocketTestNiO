package com.example.sockettest;



import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.widget.RemoteViews;
import android.widget.Toast;



/**
 * 消息服务
 * 
 * @author way
 * 
 */
@SuppressLint("NewApi")
public class MessageService extends Service {
	private static final int MSG =1;
	private static final int AGAIN =2;
	private Ntest client;
	private NotificationManager mNotificationManager;
	private boolean isStart = false;// 是否与服务器连接上
	private Notification mNotification;
	private Context mContext = this;
	//private MessageDB messageDB;
	private MainApplication application;
	
	private Thread starThread;
	
	// 用来更新通知栏消息的handler
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG:	
				String msjson=msg.getData().getString("msg");
				if (msjson != null) {
			

					// 更新通知栏
					int icon = R.drawable.ic_launcher;
					CharSequence tickerText =msjson;
					long when = System.currentTimeMillis();
					mNotification = new Notification(icon, tickerText, when);

					mNotification.flags = Notification.FLAG_NO_CLEAR;
					// 设置默认声音
					mNotification.defaults |= Notification.DEFAULT_SOUND;
					// 设定震动(需加VIBRATE权限)
					mNotification.defaults |= Notification.DEFAULT_VIBRATE;
					mNotification.contentView = null;

					Intent intent = new Intent(mContext, MainActivity.class);
					PendingIntent contentIntent = PendingIntent.getActivity(
							mContext, 0, intent, 0);
					mNotification.setLatestEventInfo(mContext,  msjson, msjson, contentIntent);
				}
				mNotificationManager.notify(2065, mNotification);// 通知一下才会生效哦
				break;
			case AGAIN:
				startConnect();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {// 在onCreate方法里面注册广播接收者
		super.onCreate();
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		application = (MainApplication) this.getApplicationContext();
		client = application.getNtest();
		application.setmNotificationManager(mNotificationManager);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		application.setClientStart(isStart);
	
		
		client.setMessageListener(new MessageListener() {
			
			@Override
			public void Message(String msgJson) {
				Message message = handler.obtainMessage();
				message.what = MSG;
				message.getData().putString("msg", msgJson);
				handler.sendMessage(message);
				//Toast.makeText(MessageService.this, msgJson, Toast.LENGTH_SHORT);
				
			}
		});
		startConnect();
	    
	}

	@Override
	// 在服务被摧毁时，做一些事情
	public void onDestroy() {
		super.onDestroy();
		
		
		// 给服务器发送下线消息
		if (isStart) {
			
		}
	}
	public void startConnect(){
		starThread=new Thread(new Runnable() {
						
						@Override
						public void run() {		
							try {
								((MainApplication)getApplication()).getNtest().init();
							} catch (IOException e) {
								
								Message message = handler.obtainMessage();
								message.what = AGAIN;
								handler.sendMessage(message);
								starThread.destroy();
							
							}
						}
					});
		starThread.start();
		
	}

}
