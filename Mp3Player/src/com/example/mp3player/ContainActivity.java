package com.example.mp3player;

import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.widget.TabHost;


	@SuppressWarnings("deprecation")
	public class ContainActivity extends TabActivity {
		 @Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_contain);
			
			//得到一TabHost，对TabActivity都是通过这个对象完成
			TabHost tabHost=getTabHost();
			//生成一个intent对象指向activity
			Intent remoteIntent=new Intent();
			remoteIntent.setClass(this, Mp3ListActivity.class);
			//生成一个TabSpec对象，代表了一个页
			TabHost.TabSpec remoteSpec=tabHost.newTabSpec("remote");
			//系统资源
			Resources res= getResources();
			//设置一个Indicator
			remoteSpec.setIndicator("remote", res.getDrawable(android.R.drawable.stat_sys_download));
			//指示内容
			remoteSpec.setContent(remoteIntent);
			//将设置好的TabSpec加入到tabHost中R.drawable.local
			tabHost.addTab(remoteSpec);
			
			Intent localIntent=new Intent();
			localIntent.setClass(this, LocalMp3Activity.class);
			TabHost.TabSpec localSpec=tabHost.newTabSpec("local");
			localSpec.setIndicator("local",res.getDrawable(R.drawable.local));
			localSpec.setContent(localIntent);
			tabHost.addTab(localSpec);
		 }
		 
	}

