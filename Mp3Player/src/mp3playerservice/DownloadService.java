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

	//ÿ���listview��ʱ����ô˷���
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		Mp3Info mp3info=(Mp3Info)intent.getSerializableExtra("mp3info");
		System.out.println("Service ---->"+mp3info);
		//��mp3info��Ϊ��������
		DownloadThread downloadThread=new DownloadThread(mp3info);
		Thread thread=new Thread(downloadThread);
		thread.start();//�����߳�
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
			//1�����Ѵ��ڣ�0�������سɹ���-1��������ʧ��
			String downloadMessage=null;
			if(resultMp3== -1)
			{
				downloadMessage="����ʧ��";
			}else if(resultMp3== 0)
			{
				downloadMessage="�ļ��Ѿ����ڣ������ظ�����";
			}else if (resultMp3== 1)
			{
				downloadMessage="�ļ����سɹ�";
			}
			
			//ʹ��notification��ʾ�û�
		}
		
		private void showNotification(String text){
			/*//��ʼ��notification
			mNotificationManager = (NotificationManager)
					getSystemService(Context.NOTIFICATION_SERVICE);
			
			
			
			 * ����һ��ͼ����Դ
			 * 
			 * ����ֱ���ڹ��̵�\res\drawable�ļ�������һ��ͼƬ�ļ���ȥ��
			 * Ȼ����eclipse���Ҽ��㹤��->ˢ�¡��� res �C drawable ��ǩ�¾ͻ���������ӵ�ͼƬ��
			 * R.java��Ҳ���Զ����һ����ͼƬ�ļ���Ϊ�����������ͱ�����
			 * ע�⣺�ļ�������ȫΪСд������д�д�Ļ���R.java�в��������µı���������������Ҳ��û�취���á�
			 * 
			//Notification�Ĺ�����ʾ
			//ͼ��
			int icon=R.drawable.notification;
			//notification����
			String contentTitle="������ʾ";
			String contentText=text;
			//
			//Notification��Intent���������ת���Activity
			//Intent notificationIntent = new Intent(Mp3ListActivity.this, Mp3ListActivity.class);
			//����Notifcation
			@SuppressWarnings("deprecation")
			Notification notification = new Notification();//icon, contentText, System.currentTimeMillis()
			notification.icon=icon;
			// ����ǰ��notification���ŵ�״̬���ϵ�ʱ����ʾ����  
	        notification.tickerText = "ע���ˣ��ұ��ӵ�״̬����"; 
			
			 * notification.contentIntent:һ��PendingIntent���󣬵��û������״̬���ϵ�ͼ��ʱ����Intent�ᱻ���� 
			 * notification.contentView:���ǿ��Բ���״̬����ͼ����Ƿ�һ��view 
			 * notification.deleteIntent ����ǰnotification���Ƴ�ʱִ�е�intent 
			 * notification.vibrate ���ֻ���ʱ������������ 
			  
	        // ���������ʾ  
	        notification.defaults=Notification.DEFAULT_SOUND;  
	        // audioStreamType��ֵ����AudioManager�е�ֵ�������������ģʽ  
	        notification.audioStreamType= android.media.AudioManager.ADJUST_LOWER;  
	          
	        //�±ߵ�������ʽ�����������  
	        //notification.sound = Uri.parse("file:///sdcard/notification/ringer.mp3");   
	        //notification.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6")
			//��ʾ���notification
	     // ���״̬����ͼ����ֵ���ʾ��Ϣ����  
	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, Mp3ListActivity.class), 0);//�����������Ƕ�����һ�����ǵ�����ʾ��activity��ֻ�е��û�����notify��ʾ
	        notification.setLatestEventInfo(Mp3ListActivity.this, "������ʾ", text, null);  
	        mNotificationManager.notify(1, notification);  
*/

			
			
		}
	}

}
