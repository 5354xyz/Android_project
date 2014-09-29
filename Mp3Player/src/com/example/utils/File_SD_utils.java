package com.example.utils;
/*
 * android 报错java.io.IOException: Permission denied
	首先检查你的路径是不是对的。应该在Environment.getExternalStorageDirectory().getAbsolutePath()这个目录下写东西。
	如果路径是对的，那就检查是不是给你的application添加了权限。

	如果没有，在manifest中添加<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	如果上面的都有正确，但是仍然会上面的错误。
	请检查你的avd在创建的时候有没有设置size，如果没有设置的话就重新创建一个有size的avd
 * */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import model.Mp3Info;

import android.os.Environment;

public class File_SD_utils
{
	private String SDRoot;
	
	public File_SD_utils()
	{
		//获得当前外部储存设备的目录
		SDRoot=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator;
	}
	
	//获得SD卡的路径
	public String getPath()
	{
		return SDRoot;
	}
	
	/*
	 * 在SD卡上创建文件
	 * 
	 * */
	public File creatSDFile(String fileName,String dir) throws IOException 
	{
		File file=new File(SDRoot+dir+File.separator+fileName);
		 //System.out.println(SDRoot+fileName);
		file.createNewFile();
		return file;
	}
	
	/*
	 * 在SD卡上面创键目录
	 * 
	 * */
	
	public File creatSDDir(String dirName)throws IOException
	{
		File dir=new File(SDRoot+dirName+File.separator);
		 System.out.println(dir.mkdir()+"xxxx");
		//dir.mkdir();
		return dir;
	}
	
	/*
	 * 判断SD卡上的文件夹是否存在
	 * 
	 * */
	public boolean isExitFile(String dirName,String fileName)
	{
		File file=new File(SDRoot+dirName+File.separator+dirName);
		return file.exists();
	}
	
    /**
     * 将一个InputStream中的数据写入至SD卡中
     */
  public File writeStreamToSDCard(String dirpath,String filename,InputStream input) {
            File file = null;
            OutputStream output=null;
             try {
                 //判断或者创建目录；这里要比较注意，如果目录错了，下面创建文件的时候就进行不了
            	if(!isExitFile(dirpath,filename)){
            		 creatSDDir(dirpath);
            		 System.out.println("创建sd卡文件，输入流");
            	 
            		 //在创建 的目录上创建文件；
            		 file = creatSDFile(filename,dirpath);
                
            		 output=new FileOutputStream(file);
            		 //1024*4 表示的是这个字节数组的长度。就是1024乘以4的意思。这个长度是自己定义的,一般长度为1024的整数倍比较好。
            		 byte[]bt=new byte[2*1024];
            		 int length = 0;
            		 while ((length = input.read(bt)) != -1) {
            			//input.read(bt)不一定正好读入4*1024个字节，测试后发现很少能一次读满buffer，大部分时候是1440字节，
            			 output.write(bt,0,length);
            			 System.out.println(length);
            		 }
            		 //刷新缓存，
            		 output.flush();
            	}else 
                {
            		System.out.println("文件已经存在");
               	 	return null;
                }
             } catch (IOException e) {
                 e.printStackTrace();
             }
             finally{

                 try{
                	 output.close();
                 }
         catch (Exception e) {
                    e.printStackTrace();
                 }
                 
             }

            return file;
           
   }
  
  /*
   * 
   * 读取目录中的MP3文件的名字和大小
   * 
   * */
  public List<Mp3Info> getMp3Files(String path)
  {
	  List<Mp3Info> mp3infos=new ArrayList<Mp3Info>();
	  
	  System.out.println(SDRoot +path);
	  
	  
	  File file =new File(SDRoot + File.separator+path);
	  File [] files=file.listFiles();
	  
	  if(file.isDirectory() && file.exists()){
		  for (int i = 0; i < files.length; i++) {
			  if(files[i].getName().endsWith("mp3"))
			  {
				  Mp3Info mp3Info=new Mp3Info();
				  mp3Info.setId(String.valueOf(i));
				  mp3Info.setMp3Name(files[i].getName());
				  mp3Info.setMp3Size(files[i].length()+ "");
				  mp3Info.setLrcName(mp3Info.getMp3Name().substring(0, mp3Info.getMp3Name().length()-3)+"lrc");
				  mp3infos.add(mp3Info);
			  }
		
		  }
	  }else 
	  {
		  System.out.println("文件夹不存在");
		  return null;
	  }
	  return mp3infos;
  }
	
}