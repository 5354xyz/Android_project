package com.example.mp3player;

//播放是在

import model.Mp3Info;
import mp3playerservice.Playservice;
import LRCProcessed.LRCProcessed;
import LRCTextView.VerticalScrollTextView;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;


public class PlayerActivity extends Activity {
	private ImageButton playButton=null;//开始播放按钮
	private ImageButton stopButton=null;//停止播放按钮
	private ImageButton downButton=null;//下一首按钮
	private ImageButton upButton=null;//上一首按钮
	private ArrayList<List> queues=null; //用于存放歌词处理后的队列
	private boolean isPlaying=false;	//是否播放
	private List<Mp3Info> mp3infos=null; //存放播放列表
	private VerticalScrollTextView verticalScrollTextView;
	private Mp3Info mp3Info=null;
	private int position=0;//当前播放的歌曲在列表中的位置
	Intent intent =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		
		playButton=(ImageButton)findViewById(R.id.play);
		//pauseButton=(ImageButton)findViewById(R.id.pause);
		stopButton=(ImageButton)findViewById(R.id.stop);
		//lrcView=(TextView)findViewById(R.id.lrcview);
		downButton=(ImageButton)findViewById(R.id.down);
		upButton=(ImageButton)findViewById(R.id.up);
		 verticalScrollTextView=(VerticalScrollTextView)findViewById(R.id.lrcview);
		 
		 
		


		playButton.setOnClickListener(new playButtonListener());
		//pauseButton.setOnClickListener(new pauseButtonListener());
		stopButton.setOnClickListener(new stopButtonListener());
		downButton.setOnClickListener(new downButtonListener());
		upButton.setOnClickListener(new upButtonListener());
		
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
		
		intent  = getIntent();
		ArrayList list = new ArrayList();
		list = intent.getStringArrayListExtra("mp3infos");
		mp3infos=(List<Mp3Info>) list.get(0);
		System.out.println("xxxxxx");
		position=(int)intent.getIntExtra("position", position);
		mp3Info = mp3infos.get(position);
		
		System.out.println(" mp3info-->"+mp3Info );

		//准备歌词
		prepareLRC(mp3Info.getLrcName());
	}


	//根据歌词的文件名，读取歌词的信息
	private void prepareLRC(String lrcName)
	{
		try
		{
			
			 
				 InputStream lrcInputStream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"mp3"+File.separator+"lrc"+File.separator+lrcName);
				 LRCProcessed lrcProcessed =new LRCProcessed();
			
				 queues=lrcProcessed.process(lrcInputStream);
				 verticalScrollTextView.setQueues(queues);
				 
				 //System.out.println("player____test"+lrcName+"  "+queues.get(0).poll().toString());
				
			 
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	//开始播放MP3方法
	class playButtonListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			
			//将mp3info存入到intent当中
			intent.putExtra("mp3Info", mp3Info);
		
			intent.putExtra("MSG", Mp3playerConstant.PlayMSG.PLAY_MSG);
			intent.setClass(PlayerActivity.this, Playservice.class);
			//启动startService
			startService(intent);

			
			if(isPlaying)
			{
				playButton.setImageDrawable(getResources().getDrawable(R.drawable.play));
				verticalScrollTextView.setPlaying();
				verticalScrollTextView.updateUI(Mp3playerConstant.PlayMSG.PLAY_MSG);
			}else
			{
				
				playButton.setImageDrawable(getResources().getDrawable(R.drawable.pause));
				verticalScrollTextView.setPlaying();
				verticalScrollTextView.updateUI(Mp3playerConstant.PlayMSG.PLAY_MSG);
			}
			isPlaying = isPlaying?false:true;
		}
		
	}

	
	//停止播放MP3的方法
	class stopButtonListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			//将mp3info存入到intent当中
			intent.putExtra("mp3Info", mp3Info);
			intent.putExtra("MSG", Mp3playerConstant.PlayMSG.STOP_MSG);
			intent.setClass(PlayerActivity.this, Playservice.class);
			//启动startService
			startService(intent);
			stopService(intent);
			//停止更新歌词
			//handler.removeCallbacks(updateLrcRunable);
		}
		
	}

	class downButtonListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			intent.putExtra("mp3Info", mp3Info);
			intent.putExtra("MSG", Mp3playerConstant.PlayMSG.STOP_MSG);//停止player播放
			intent.setClass(PlayerActivity.this, Playservice.class);
			//启动startService
			startService(intent);
			stopService(intent);
			if(position<mp3infos.size()-1)
				position+=1;
			else
				position=0;
			
			mp3Info = mp3infos.get(position);
			//准备歌词
			prepareLRC(mp3Info.getLrcName());
			
			//先处理歌词，在播放
			playButton.setImageDrawable(getResources().getDrawable(R.drawable.pause));
			verticalScrollTextView.setPlaying();
			verticalScrollTextView.updateUI(Mp3playerConstant.PlayMSG.DOWN_MSG);
			
			//将mp3info存入到intent当中
			intent.putExtra("mp3Info", mp3Info);
		
			intent.putExtra("MSG", Mp3playerConstant.PlayMSG.PLAY_MSG);
			intent.setClass(PlayerActivity.this, Playservice.class);
			//启动startService
			startService(intent);
			
			
		}
		
	}
	class upButtonListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(position>0)
				position-=1;
			else
				position=0;
			
			mp3Info = mp3infos.get(position);
			//准备歌词
			prepareLRC(mp3Info.getLrcName());
			
			
			//将mp3info存入到intent当中
			intent.putExtra("mp3Info", mp3Info);
		
			intent.putExtra("MSG", Mp3playerConstant.PlayMSG.PLAY_MSG);
			intent.setClass(PlayerActivity.this, Playservice.class);
			//启动startService
			startService(intent);
			
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.player, menu);
		return true;
	}
	


}
