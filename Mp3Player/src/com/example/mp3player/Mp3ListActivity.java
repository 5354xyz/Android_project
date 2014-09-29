package com.example.mp3player;
//发现的一个问题：xml文件的命名不能有大写，否则不能将id或者layout添加到R资源文件中
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

	//点击菜单时显示
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mp3_list, menu);
		menu.add(0, UPDATE, 1, R.string.updateList);//群id，item的id，顺序，名称
		menu.add(0, ABOUT, 2, R.string.about);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		if(item.getItemId()==UPDATE)
		{
			//用户点击了更新按钮
			updateListView();
			
		}else if(item.getItemId()==ABOUT)
		{
			//用户点击了关于按钮
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	//更新列表函数
	private void  updateListView()
	{
		//下载xml文件
		String XML=downloadXML("http://10.0.2.2/mp3/resources.xml");
		//解析xml文件，放到Mp3Info对像中，并返回list中
		mp3infos=parse(XML);
		SimpleAdapter simpleAdapter = buildSimpleAdapter(mp3infos);
		//将SimpleAdapter设置到listActivity中
		setListAdapter(simpleAdapter);
		
	}
	
	//用户点击了某一个mp3的列，开始下载
	//下载：1）创建service，在service里面运行
	//2）开启另外一个线程，否则在主线程中运行（没下载完，返回的时候），activity会被卡住
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		//根据用户点击的列表中的位置来得到MP3info对象
		Mp3Info mp3info= mp3infos.get(position);
		//System.out.println("mp3info--->"+mp3info);
		
		
		Intent intent =new Intent();
		//将mp3info存入到intent当中
		intent.putExtra("mp3info", mp3info);
		intent.setClass(this, DownloadService.class);
		//启动startService
		startService(intent);
		super.onListItemClick(l, v, position, id);
	}

	//创建一个SimpleAdapter
	private SimpleAdapter buildSimpleAdapter(List<Mp3Info> mp3infos)
	{
		//迭代出来
				List<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
				for (Iterator iterator = mp3infos.iterator(); iterator.hasNext();) {
					Mp3Info mp3info = (Mp3Info)iterator.next();
					HashMap<String,String> hashMap=new HashMap<String,String>();
					hashMap.put("mp3_name", mp3info.getMp3Name());
					hashMap.put("mp3_size", mp3info.getMp3Size());
					list.add(hashMap);
					
					
				}
				//创建SimpleAdapter
				
				SimpleAdapter simpleAdapter=new SimpleAdapter(this, list,R.layout.mp3info_item, new String[]{"mp3_name","mp3_size"}, new int [] {R.id.mp3_name,R.id.mp3_size});
				
		return simpleAdapter;
	}
	//下载XML的函数
	private String downloadXML(String strUrl)
	{
		HttpDownloader downloader=new HttpDownloader();
		String result=downloader.download(strUrl);
		
		
		return result;
	}
	
	//解析XML文件函数
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
