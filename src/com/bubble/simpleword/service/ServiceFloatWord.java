package com.bubble.simpleword.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;

import com.bubble.simpleword.view.MyWindowManager;

/**
 * <p>Title: ServiceFloatingWord</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-30 上午10:48:53
 */
public class ServiceFloatWord extends Service {
	/** 
     * 用于在线程中创建或移除悬浮窗。 
     */  
    private Handler handler = new Handler();  
  
    /**
     * <p>Description: </p>
     * @author bubble
     * @date 2015-8-31 下午10:51:24
     */
    @Override
    public void onCreate() {
    	super.onCreate();
    	if ( ! MyWindowManager.isFloatWindowShowing() ) {
    		handler.post(new Runnable() {  
              @Override  
              public void run() {  
                  MyWindowManager.createSmallWindow(getApplicationContext());  
              }  
          }); 
    	}
    }
    
    @Override  
    public IBinder onBind(Intent intent) {  
        return null;  
    }  
  
    @Override  
    public int onStartCommand(Intent intent, int flags, int startId) {  
        handler.post(new Runnable() {  
            @Override  
            public void run() {  
                MyWindowManager.updateWordClass(getApplicationContext());  
            }  
        });
        return super.onStartCommand(intent, flags, startId);  
    }  
  
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
    }  
  
}
