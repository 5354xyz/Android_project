package LRCProcessed;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LRCProcessed {

	public void getLRC()
	{
		
	}
	
	public ArrayList<List> process(InputStream inputStream)
	{
		List<Long> timeMlills=new LinkedList<Long>();//������queue<E>����E�Ƿ��ͣ�������object������
		List<String> lyric=new LinkedList<String>();
		ArrayList<List> queues=new ArrayList<List>();
		
		
		try
		{
			//��ֹ���룬����������
			InputStreamReader inputReader=  new InputStreamReader(inputStream,"gb2312");
			BufferedReader bufReader=new BufferedReader(inputReader);
			String temp=null;
			Pattern p_lrc=Pattern.compile("^\\[[0-9]{2}:[0-9]{2}.[0-9]{2}\\]");
			Pattern p_ar=Pattern.compile("\\[ar:.*\\]");
			Pattern p_ti=Pattern.compile("\\[ti:.*\\]");
			int i=0;
			String result=null;
			boolean b=true;
			
			while((temp=bufReader.readLine())!=null)
			{
				
				Matcher m_lrc=p_lrc.matcher(temp);
				Matcher m_ar=p_ar.matcher(temp);
				Matcher m_ti=p_ti.matcher(temp);
				//ƥ��������
				if(m_lrc.find())//���ƥ�䵽��������У�ƥ�䲻����֤����һ��Ҳ�Ǹ�ʵ�һ����
				{
					//�����ʲ��ǿյ�
					if(result !=null)
					{
						lyric.add(result);
						//System.out.println(result);
					}
					//group()���ص�ǰ���Ҷ���õ�����ƥ��������Ӵ����� ,����ֻ��һ���ַ����������Ƿ���group(0)
					String timeStr =m_lrc.group();
					Long timeMill=time2long(timeStr.substring(1, timeStr.length()-1));//ȥ��[00:00.67]������������
					//System.out.println(timeMill);
					if(b)
					{
						timeMlills.add(timeMill);
					}
					String msg= temp.substring(10);//ȥ��ǰ���ʱ��
					result=""+msg;
					
					
				}else if(m_ar.find())
				{
					Long e=0L;
					timeMlills.add(e);
					lyric.add("���֣�"+temp.subSequence(4, temp.length()-1));
					System.out.println("���֣�"+temp.subSequence(4, temp.length()-1));
				}else if(m_ti.find())
				{
					Long e=100L;
					timeMlills.add(e);
					lyric.add("������"+temp.subSequence(4, temp.length()-1));
				}
			}
			lyric.add(result);
			
			queues.add(timeMlills);
			queues.add(lyric);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return queues;
	}
	
	public Long time2long(String timeStr)
	{
		String s[] =timeStr.split(":");
		int min =Integer.parseInt(s[0]);
		String ss[] = s[1].split("\\.");//ע��java�е�ת��Ϊ����\\
		int sec =Integer.parseInt(ss[0]);
		int mill=Integer.parseInt(ss[1]);
		return  min*60*1000+sec*1000+mill*10L;
	}
}
