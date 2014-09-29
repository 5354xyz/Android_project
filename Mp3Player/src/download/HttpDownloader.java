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
			//创建一个URL对象
			URL url=new URL(urlStr);
			//创建一个HTTP链接
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
	 * 这个函数下载任何文件，返回整形，1代表已存在，0代表下载成功，-1代表下载失败
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
	 * 封装根据URL获得IputStream方法，
	 * 参数为：url字符串
	 * 抛出这一异常(MalformedURLException)指示出现了错误的 URL。或者在规范字符串中找不到任何合法协议，或者无法分析字符串。
	 * */
	
	public InputStream getInputStream(String urlStr)throws MalformedURLException,IOException
	{
		URL url=new URL(urlStr);
		HttpURLConnection hURLc= (HttpURLConnection) url.openConnection();
		InputStream in=hURLc.getInputStream();
		return in;
	}
}
