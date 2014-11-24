package com.example.sockettest;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;

/**
 * 
 * 完整的退出应用.
 * @author juanqiang
 */
public class MainApplication extends Application {
	private Ntest ntest;// 客户端
	private boolean isClientStart;// 客户端连接是否启动 
	private NotificationManager mNotificationManager; 
	private boolean isStart=false;// 是否在后台运行标记
	private List<Activity> activityList = new LinkedList<Activity>(); 
	@Override
	public void onCreate() {  
		ntest=new Ntest("192.168.200.133",40000);
		super.onCreate();
	}
	// 是否在后台运行标记
	public void setIsStart(boolean isStart) {
		this.isStart=isStart;
	}

	public boolean getIsStart() {
		return isStart;
	}
	

	public Ntest getNtest() {
		return ntest;
	}
	public void setNtest(Ntest ntest) {
		this.ntest = ntest;
	}
	public boolean isClientStart() {
		return isClientStart;
	}

	public void setClientStart(boolean isClientStart) {
		this.isClientStart = isClientStart;
	}
 
	public NotificationManager getmNotificationManager() {
		return mNotificationManager;
	}

	public void setmNotificationManager(NotificationManager mNotificationManager) {
		this.mNotificationManager = mNotificationManager;
	}
	 
	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	 
	 

	// 遍历所有Activity并finish
	public void exit() { 
		for (Activity activity : activityList) {
			activity.finish();
		} 
		System.exit(0);
	}
}
