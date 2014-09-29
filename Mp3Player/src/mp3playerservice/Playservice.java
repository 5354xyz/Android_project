package mp3playerservice;

import java.io.File;

import com.example.mp3player.Mp3playerConstant;

import model.Mp3Info;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

public class Playservice extends Service
{

	private MediaPlayer mediaPlayer=null;
	
	private boolean isPlaying =false;
	private boolean isPause =true;
	private boolean isRelease =false;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		Mp3Info  mp3Info=(Mp3Info)intent.getSerializableExtra("mp3Info");
		int MSG =(int )intent.getIntExtra("MSG", 0);
		System.out.println("playerservice-->msg"+MSG);
		if(mp3Info!=null){
			if(MSG==Mp3playerConstant.PlayMSG.PLAY_MSG)
			{
				play(mp3Info);
			}
			else if(MSG==Mp3playerConstant.PlayMSG.STOP_MSG)
			{
				stop();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
/*	
	public void start(Mp3Info  mp3Info)
	{
		System.out.println("playerService-->play");
		if(!isPlaying)
		{
			try{
			//mediaPlayer.reset();
			String path=getMp3Path(mp3Info);
			mediaPlayer = MediaPlayer.create(this, Uri.parse("file://"+path));
			
			//mediaPlayer.prepare();
			mediaPlayer.setLooping(false);
			mediaPlayer.start();
			isPlaying=true;
			isRelease=false;
			System.out.println("player--->play");
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	
	}*/
	
	public void play(Mp3Info  mp3Info)
	{
		try{
			if(mediaPlayer == null)
			{
				String path=getMp3Path(mp3Info);
				mediaPlayer = MediaPlayer.create(this, Uri.parse("file://"+path));
				mediaPlayer.setLooping(false);
				//mediaPlayer.start();
			}
			if(mediaPlayer !=null)
			{
				if(!isRelease)
				{
					if(!isPause)
					{
						mediaPlayer.pause();
						isPlaying=false;
						isPause=true;
					}else
					{
						mediaPlayer.start();
						isPlaying=true;
						isPause=false;
					}
				}
			}
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void stop()
	{
		if(mediaPlayer !=null)
		{
			if(!isRelease)
			{
				mediaPlayer.stop();
				mediaPlayer.release();
				isRelease=true;
				isPlaying=false;
				isPause=false;
			}
		}
	}
	private String getMp3Path(Mp3Info  mp3Info)
	{
		String SDCardRoot =Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator;
		String path=SDCardRoot+"mp3"+File.separator+mp3Info.getMp3Name();
		return path;
	}

}
