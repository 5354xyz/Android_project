package com.example.mp3player;
//���ֵ�һ�����⣺xml�ļ������������д�д�������ܽ�id����layout��ӵ�R��Դ�ļ���
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import model.Mp3Info;
import mp3playerservice.DownloadService;

import download.HttpDownloader;
import XML.Mp3ListContentHandler;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Mp3ListActivity extends ListActivity {
	
	private static final int  UPDATE =1;
	private static final int  ABOUT =2;
	private List<Mp3Info> mp3infos=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp3_list);
		updateListView();
	}

	//����˵�ʱ��ʾ
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mp3_list, menu);
		menu.add(0, UPDATE, 1, R.string.updateList);//Ⱥid��item��id��˳������
		menu.add(0, ABOUT, 2, R.string.about);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		if(item.getItemId()==UPDATE)
		{
			//�û�����˸��°�ť
			updateListView();
			
		}else if(item.getItemId()==ABOUT)
		{
			//�û�����˹��ڰ�ť
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	//�����б���
	private void  updateListView()
	{
		//����xml�ļ�
		String XML=downloadXML("http://10.0.2.2/mp3/resources.xml");
		//����xml�ļ����ŵ�Mp3Info�����У�������list��
		mp3infos=parse(XML);
		SimpleAdapter simpleAdapter = buildSimpleAdapter(mp3infos);
		//��SimpleAdapter���õ�listActivity��
		setListAdapter(simpleAdapter);
		
	}
	
	//�û������ĳһ��mp3���У���ʼ����
	//���أ�1������service����service��������
	//2����������һ���̣߳����������߳������У�û�����꣬���ص�ʱ�򣩣�activity�ᱻ��ס
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		//�����û�������б��е�λ�����õ�MP3info����
		Mp3Info mp3info= mp3infos.get(position);
		//System.out.println("mp3info--->"+mp3info);
		
		
		Intent intent =new Intent();
		//��mp3info���뵽intent����
		intent.putExtra("mp3info", mp3info);
		intent.setClass(this, DownloadService.class);
		//����startService
		startService(intent);
		super.onListItemClick(l, v, position, id);
	}

	//����һ��SimpleAdapter
	private SimpleAdapter buildSimpleAdapter(List<Mp3Info> mp3infos)
	{
		//��������
				List<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
				for (Iterator iterator = mp3infos.iterator(); iterator.hasNext();) {
					Mp3Info mp3info = (Mp3Info)iterator.next();
					HashMap<String,String> hashMap=new HashMap<String,String>();
					hashMap.put("mp3_name", mp3info.getMp3Name());
					hashMap.put("mp3_size", mp3info.getMp3Size());
					list.add(hashMap);
					
					
				}
				//����SimpleAdapter
				
				SimpleAdapter simpleAdapter=new SimpleAdapter(this, list,R.layout.mp3info_item, new String[]{"mp3_name","mp3_size"}, new int [] {R.id.mp3_name,R.id.mp3_size});
				
		return simpleAdapter;
	}
	//����XML�ĺ���
	private String downloadXML(String strUrl)
	{
		HttpDownloader downloader=new HttpDownloader();
		String result=downloader.download(strUrl);
		
		
		return result;
	}
	
	//����XML�ļ�����
	private List<Mp3Info> parse (String xmlStr)
	{
		List <Mp3Info> infos=new ArrayList<Mp3Info>();
		SAXParserFactory saxParFactory=SAXParserFactory.newInstance();
		try 
		{
			XMLReader xmlReader=saxParFactory.newSAXParser().getXMLReader();
			
			Mp3ListContentHandler mp3ListContentHandler=new Mp3ListContentHandler(infos);
			xmlReader.setContentHandler(mp3ListContentHandler);
			xmlReader.parse(new InputSource(new StringReader(xmlStr)));
			for (Iterator iterator = infos.iterator(); iterator.hasNext();) {
				Mp3Info mp3Info = (Mp3Info) iterator.next();
				System.out.println(mp3Info);
				
			}
		}catch(Exception e )
		{
			e.printStackTrace();
		}
		return infos;
		
	}

}
