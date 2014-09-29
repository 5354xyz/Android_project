package com.example.mp3player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import model.Mp3Info;

import com.example.utils.File_SD_utils;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class LocalMp3Activity extends ListActivity {

	List<Mp3Info> mp3infos=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_local_mp3);
		

	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		ArrayList list = new ArrayList();
		if(mp3infos != null)
		{
			list.add(mp3infos);
			//Mp3Info mp3Info = mp3infos.get(position);
			Intent intent =new Intent();
			intent.putExtra("mp3infos", list);
			intent.putExtra("position", position);
			intent.setClass(this, PlayerActivity.class);
			startActivity(intent);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		File_SD_utils file_SD_utils = new File_SD_utils();
		
		mp3infos = file_SD_utils.getMp3Files("mp3/");
		List<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
		if(mp3infos != null){
			for (Iterator iterator = mp3infos.iterator(); iterator.hasNext();) {
				Mp3Info mp3info = (Mp3Info)iterator.next();
				HashMap<String,String> hashMap=new HashMap<String,String>();
				hashMap.put("mp3_name", mp3info.getMp3Name());
				hashMap.put("mp3_size", mp3info.getMp3Size());
				list.add(hashMap);

			}
		}else
		{
			System.out.println("没有文件");
		}
		//创建SimpleAdapter
		
		SimpleAdapter simpleAdapter=new SimpleAdapter(this, list,R.layout.mp3info_item, new String[]{"mp3_name","mp3_size"}, new int [] {R.id.mp3_name,R.id.mp3_size});
		setListAdapter(simpleAdapter);
	}

}
