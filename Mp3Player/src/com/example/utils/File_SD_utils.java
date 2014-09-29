package com.example.utils;
/*
 * android ����java.io.IOException: Permission denied
	���ȼ�����·���ǲ��ǶԵġ�Ӧ����Environment.getExternalStorageDirectory().getAbsolutePath()���Ŀ¼��д������
	���·���ǶԵģ��Ǿͼ���ǲ��Ǹ����application�����Ȩ�ޡ�

	���û�У���manifest�����<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	�������Ķ�����ȷ��������Ȼ������Ĵ���
	�������avd�ڴ�����ʱ����û������size�����û�����õĻ������´���һ����size��avd
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
		//��õ�ǰ�ⲿ�����豸��Ŀ¼
		SDRoot=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator;
	}
	
	//���SD����·��
	public String getPath()
	{
		return SDRoot;
	}
	
	/*
	 * ��SD���ϴ����ļ�
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
	 * ��SD�����洴��Ŀ¼
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
	 * �ж�SD���ϵ��ļ����Ƿ����
	 * 
	 * */
	public boolean isExitFile(String dirName,String fileName)
	{
		File file=new File(SDRoot+dirName+File.separator+dirName);
		return file.exists();
	}
	
    /**
     * ��һ��InputStream�е�����д����SD����
     */
  public File writeStreamToSDCard(String dirpath,String filename,InputStream input) {
            File file = null;
            OutputStream output=null;
             try {
                 //�жϻ��ߴ���Ŀ¼������Ҫ�Ƚ�ע�⣬���Ŀ¼���ˣ����洴���ļ���ʱ��ͽ��в���
            	if(!isExitFile(dirpath,filename)){
            		 creatSDDir(dirpath);
            		 System.out.println("����sd���ļ���������");
            	 
            		 //�ڴ��� ��Ŀ¼�ϴ����ļ���
            		 file = creatSDFile(filename,dirpath);
                
            		 output=new FileOutputStream(file);
            		 //1024*4 ��ʾ��������ֽ�����ĳ��ȡ�����1024����4����˼������������Լ������,һ�㳤��Ϊ1024���������ȽϺá�
            		 byte[]bt=new byte[2*1024];
            		 int length = 0;
            		 while ((length = input.read(bt)) != -1) {
            			//input.read(bt)��һ�����ö���4*1024���ֽڣ����Ժ��ֺ�����һ�ζ���buffer���󲿷�ʱ����1440�ֽڣ�
            			 output.write(bt,0,length);
            			 System.out.println(length);
            		 }
            		 //ˢ�»��棬
            		 output.flush();
            	}else 
                {
            		System.out.println("�ļ��Ѿ�����");
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
   * ��ȡĿ¼�е�MP3�ļ������ֺʹ�С
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
		  System.out.println("�ļ��в�����");
		  return null;
	  }
	  return mp3infos;
  }
	
}