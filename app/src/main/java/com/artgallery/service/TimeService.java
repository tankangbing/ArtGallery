package com.artgallery.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class TimeService extends Service {

    private int time = 120;
    private int settime = 120;
    private Message timemsg;
    private ReSetTimeReceiver receiver;

    private Handler timehandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    time--;
                    if (time % 20 == 0) {
                        System.out.println("time==" + time);
                    }
                    if (time == 0) {
                        time = settime;
                        // 跳转
                        SharedPreferences sp = getSharedPreferences("SP",
                                MODE_PRIVATE);
                        ActivityManager manager = (ActivityManager) getApplicationContext()
                                .getSystemService(Context.ACTIVITY_SERVICE);
                        List<RunningTaskInfo> task_info = manager
                                .getRunningTasks(20);
                        String cN1 = "com.fy.activity.MsgActivity";
                        Log.e("TimeService", "Boolean:" + sp.getBoolean("NULL", false));
                        if (!sp.getBoolean("NULL", false)) // 全屏有数据时
                        {
                            if (task_info.get(0).topActivity.getClassName().equals(
                                    cN1)) {

                                if (sp.getBoolean("msgjinji", false)) {
                                    // 判断是否在紧急任务
                                    Log.e("TimeService", "在紧急任务");
                                } else {

                                    Log.e("TimeService", "不在紧急任务");
//								if (sp.getBoolean("fullChange", false)) {
//									// 任务内容改变 跳至主页 等下轮加载
//									System.out.println("任务id改变跳回主页");
//									Editor a = sp.edit();
//									a.putBoolean("fullChange", false);
//									a.commit();
//									Intent intent = new Intent();
//									intent.setClass(getApplicationContext(),
//											MainActivity.class);
//									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//									startActivity(intent);
//								} else {
//									System.out.println("fullChange = flase");
//								}
                                }
                            } else {
                                Log.e("TimeService", "全屏跳转：进入数据判断");
                                Intent open = new Intent();
                                open.setAction("com.fy.Open");
                                sendBroadcast(open);
                            }
                        } else {
                            Log.e("TimeService", "跳转计时完成：此时全屏任务为空");
                            if (cN1.equals(task_info.get(0).topActivity
                                    .getClassName())) {
                                Log.e("TimeService", "全屏任务为空：当前在MsgActivity上，进行判断");
                                if (sp.getBoolean("msgjinji", false)) {
                                    Log.e("TimeService", "正在进行紧急任务");
                                } else {
                                    Log.e("TimeService", "进行跳转");
                                    Intent closeFULL = new Intent();
                                    closeFULL.setAction("com.fy.closefull");
                                    sendBroadcast(closeFULL);
                                }
                            } else {
                                Log.e("TimeService", "全屏任务为空：当前不在MsgActivity上，不操作");
                            }
                        }
                    }

                    timemsg = timehandler.obtainMessage();
                    timemsg.what = 1;
                    timehandler.sendMessageDelayed(timemsg, 1000);

                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        SharedPreferences sp = getSharedPreferences("SP", MODE_PRIVATE);
        settime = Integer.parseInt(sp.getString("JumpTime", "2")) * 60;
        time = settime;
        timemsg = timehandler.obtainMessage();
        timemsg.what = 1;
        if (!timehandler.hasMessages(1)) {
            timehandler.sendMessageDelayed(timemsg, 1000);
        }
        receiver = new ReSetTimeReceiver();
        IntentFilter filter = new IntentFilter("com.fy.RESET_TIME");
        registerReceiver(receiver, filter);
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_REDELIVER_INTENT;
        return super.onStartCommand(intent, flags, startId);
    }

    //
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        // System.out.println("time onDestroy");
         unregisterReceiver(receiver);
        timehandler.removeMessages(1);
        super.onDestroy();

    }

    private class ReSetTimeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            System.out.println("重置时间");
            time = settime;
        }

    }
}
