package mp3playerservice;

import com.example.mp3player.Mp3ListActivity;
import com.example.mp3player.R;
import com.example.mp3player.Mp3playerConstant;
import download.HttpDownloader;
import model.Mp3Info;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class DownloadService extends Service
{

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	//每点击listview的时候调用此方法
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		Mp3Info mp3info=(Mp3Info)intent.getSerializableExtra("mp3info");
		System.out.println("Service ---->"+mp3info);
		//将mp3info作为参数传递
		DownloadThread downloadThread=new DownloadThread(mp3info);
		Thread thread=new Thread(downloadThread);
		thread.start();//开启线程
		return super.onStartCommand(intent, flags, startId);
	}
	
	class DownloadThread implements Runnable
	{
		private NotificationManager mNotificationManager=null;
		private Mp3Info mp3info=null;
		public DownloadThread(Mp3Info mp3info)
		{
			this.mp3info=mp3info;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			String mp3Url= Mp3playerConstant.URL.BASE_URL+mp3info.getMp3Name();
			String lrcUrl= Mp3playerConstant.URL.BASE_URL+mp3info.getLrcName();
			HttpDownloader httpDownloader =new HttpDownloader();
			int resultMp3 =httpDownloader.downFile(mp3Url, "mp3/", mp3info.getMp3Name());
			int	resultLrc =httpDownloader.downFile(lrcUrl, "mp3/lrc/", mp3info.getLrcName());
			//1代表已存在，0代表下载成功，-1代表下载失败
			String downloadMessage=null;
			if(resultMp3== -1)
			{
				downloadMessage="下载失败";
			}else if(resultMp3== 0)
			{
				downloadMessage="文件已经存在，不需重复下载";
			}else if (resultMp3== 1)
			{
				downloadMessage="文件下载成功";
			}
			
			//使用notification提示用户
		}
		
		private void showNotification(String text){
			/*//初始化notification
			mNotificationManager = (NotificationManager)
					getSystemService(Context.NOTIFICATION_SERVICE);
			
			
			
			 * 导入一个图像资源
			 * 
			 * 可以直接在工程的\res\drawable文件夹里拷贝一个图片文件过去。
			 * 然后在eclipse里右键点工程->刷新。在 res – drawable 标签下就会多出来你添加的图片。
			 * R.java中也会自动添加一个以图片文件名为变量名的整型变量。
			 * 注意：文件名必须全为小写。如果有大写的话，R.java中不会生成新的变量，这样程序里也就没办法调用。
			 * 
			//Notification的滚动提示
			//图标
			int icon=R.drawable.notification;
			//notification标题
			String contentTitle="下载提示";
			String contentText=text;
			//
			//Notification的Intent，即点击后转向的Activity
			//Intent notificationIntent = new Intent(Mp3ListActivity.this, Mp3ListActivity.class);
			//创建Notifcation
			@SuppressWarnings("deprecation")
			Notification notification = new Notification();//icon, contentText, System.currentTimeMillis()
			notification.icon=icon;
			// 当当前的notification被放到状态栏上的时候，提示内容  
	        notification.tickerText = "注意了，我被扔到状态栏了"; 
			
			 * notification.contentIntent:一个PendingIntent对象，当用户点击了状态栏上的图标时，该Intent会被触发 
			 * notification.contentView:我们可以不在状态栏放图标而是放一个view 
			 * notification.deleteIntent 当当前notification被移除时执行的intent 
			 * notification.vibrate 当手机震动时，震动周期设置 
			  
	        // 添加声音提示  
	        notification.defaults=Notification.DEFAULT_SOUND;  
	        // audioStreamType的值必须AudioManager中的值，代表着响铃的模式  
	        notification.audioStreamType= android.media.AudioManager.ADJUST_LOWER;  
	          
	        //下边的两个方式可以添加音乐  
	        //notification.sound = Uri.parse("file:///sdcard/notification/ringer.mp3");   
	        //notification.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6")
			//显示这个notification
	     // 点击状态栏的图标出现的提示信息设置  
	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, Mp3ListActivity.class), 0);//该语句的作用是定义了一个不是当即显示的activity，只有当用户拉下notify显示
	        notification.setLatestEventInfo(Mp3ListActivity.this, "下载提示", text, null);  
	        mNotificationManager.notify(1, notification);  
*/

			
			
		}
	}

}
