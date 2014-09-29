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
			
			//�õ�һTabHost����TabActivity����ͨ������������
			TabHost tabHost=getTabHost();
			//����һ��intent����ָ��activity
			Intent remoteIntent=new Intent();
			remoteIntent.setClass(this, Mp3ListActivity.class);
			//����һ��TabSpec���󣬴�����һ��ҳ
			TabHost.TabSpec remoteSpec=tabHost.newTabSpec("remote");
			//ϵͳ��Դ
			Resources res= getResources();
			//����һ��Indicator
			remoteSpec.setIndicator("remote", res.getDrawable(android.R.drawable.stat_sys_download));
			//ָʾ����
			remoteSpec.setContent(remoteIntent);
			//�����úõ�TabSpec���뵽tabHost��R.drawable.local
			tabHost.addTab(remoteSpec);
			
			Intent localIntent=new Intent();
			localIntent.setClass(this, LocalMp3Activity.class);
			TabHost.TabSpec localSpec=tabHost.newTabSpec("local");
			localSpec.setIndicator("local",res.getDrawable(R.drawable.local));
			localSpec.setContent(localIntent);
			tabHost.addTab(localSpec);
		 }
		 
	}

