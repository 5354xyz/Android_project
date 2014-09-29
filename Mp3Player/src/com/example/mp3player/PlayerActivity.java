package com.example.mp3player;

//��������

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
	private ImageButton playButton=null;//��ʼ���Ű�ť
	private ImageButton stopButton=null;//ֹͣ���Ű�ť
	private ImageButton downButton=null;//��һ�װ�ť
	private ImageButton upButton=null;//��һ�װ�ť
	private ArrayList<List> queues=null; //���ڴ�Ÿ�ʴ����Ķ���
	private boolean isPlaying=false;	//�Ƿ񲥷�
	private List<Mp3Info> mp3infos=null; //��Ų����б�
	private VerticalScrollTextView verticalScrollTextView;
	private Mp3Info mp3Info=null;
	private int position=0;//��ǰ���ŵĸ������б��е�λ��
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

		//׼�����
		prepareLRC(mp3Info.getLrcName());
	}


	//���ݸ�ʵ��ļ�������ȡ��ʵ���Ϣ
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

	//��ʼ����MP3����
	class playButtonListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			
			//��mp3info���뵽intent����
			intent.putExtra("mp3Info", mp3Info);
		
			intent.putExtra("MSG", Mp3playerConstant.PlayMSG.PLAY_MSG);
			intent.setClass(PlayerActivity.this, Playservice.class);
			//����startService
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

	
	//ֹͣ����MP3�ķ���
	class stopButtonListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			//��mp3info���뵽intent����
			intent.putExtra("mp3Info", mp3Info);
			intent.putExtra("MSG", Mp3playerConstant.PlayMSG.STOP_MSG);
			intent.setClass(PlayerActivity.this, Playservice.class);
			//����startService
			startService(intent);
			stopService(intent);
			//ֹͣ���¸��
			//handler.removeCallbacks(updateLrcRunable);
		}
		
	}

	class downButtonListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			intent.putExtra("mp3Info", mp3Info);
			intent.putExtra("MSG", Mp3playerConstant.PlayMSG.STOP_MSG);//ֹͣplayer����
			intent.setClass(PlayerActivity.this, Playservice.class);
			//����startService
			startService(intent);
			stopService(intent);
			if(position<mp3infos.size()-1)
				position+=1;
			else
				position=0;
			
			mp3Info = mp3infos.get(position);
			//׼�����
			prepareLRC(mp3Info.getLrcName());
			
			//�ȴ����ʣ��ڲ���
			playButton.setImageDrawable(getResources().getDrawable(R.drawable.pause));
			verticalScrollTextView.setPlaying();
			verticalScrollTextView.updateUI(Mp3playerConstant.PlayMSG.DOWN_MSG);
			
			//��mp3info���뵽intent����
			intent.putExtra("mp3Info", mp3Info);
		
			intent.putExtra("MSG", Mp3playerConstant.PlayMSG.PLAY_MSG);
			intent.setClass(PlayerActivity.this, Playservice.class);
			//����startService
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
			//׼�����
			prepareLRC(mp3Info.getLrcName());
			
			
			//��mp3info���뵽intent����
			intent.putExtra("mp3Info", mp3Info);
		
			intent.putExtra("MSG", Mp3playerConstant.PlayMSG.PLAY_MSG);
			intent.setClass(PlayerActivity.this, Playservice.class);
			//����startService
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
