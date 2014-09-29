package download;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.utils.File_SD_utils;

public class HttpDownloader {

	/**
	 * @param args
	 */
	public String download(String urlStr)
	{
		StringBuffer sb=new StringBuffer();
		String line =null;
		BufferedReader buffer=null;
		try
		{
			//����һ��URL����
			URL url=new URL(urlStr);
			//����һ��HTTP����
			HttpURLConnection hURLc= (HttpURLConnection) url.openConnection();
			
			buffer=new BufferedReader(new InputStreamReader(hURLc.getInputStream()));
			while((line=buffer.readLine())  !=null)
			{
				sb.append(line);
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		finally
		{
			try
			{
				buffer.close();
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	/*
	 * ������������κ��ļ����������Σ�1�����Ѵ��ڣ�0�������سɹ���-1��������ʧ��
	 * 
	 * */
	
	public int downFile(String urlStr, String path, String fileName)
	{
		InputStream in=null;
		try
		{
			File_SD_utils fileUtils=new File_SD_utils();
			
			if(fileUtils.isExitFile(path,fileName))
			{
				return 1;
			}else
			{
				in =getInputStream(urlStr);
				File resultFile=fileUtils.writeStreamToSDCard(path, fileName, in);
				if(resultFile==null)
				{
					return -1;
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			return -1;
		}finally
		{
			try
			{
				in.close();
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
		return 0;
	}
	
	/**
	 * ��װ����URL���IputStream������
	 * ����Ϊ��url�ַ���
	 * �׳���һ�쳣(MalformedURLException)ָʾ�����˴���� URL�������ڹ淶�ַ������Ҳ����κκϷ�Э�飬�����޷������ַ�����
	 * */
	
	public InputStream getInputStream(String urlStr)throws MalformedURLException,IOException
	{
		URL url=new URL(urlStr);
		HttpURLConnection hURLc= (HttpURLConnection) url.openConnection();
		InputStream in=hURLc.getInputStream();
		return in;
	}
}
