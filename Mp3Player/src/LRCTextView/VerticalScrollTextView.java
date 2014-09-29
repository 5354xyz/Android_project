package LRCTextView;
//����Ҫ��ͨ��handler���߳�������UI����
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.example.mp3player.Mp3playerConstant;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

public class VerticalScrollTextView extends TextView
{
	public int index = 0;//��ǰ�������е�����
	private Paint uLRC; //�ǵ�ǰ�����ʵĻ���
	private Paint fLRC; //��ǰ�����ʵĻ���
	private float viewH;//Lrc��ʵ�view�ĸ߶�
	private float viewW;//Lrc��ʵ�view�Ŀ��
	private float middleY;//���
	private float middleX;
	private Handler uiHandler = new Handler();
	private Handler lrcHandler = new Handler();
	private static final int Intervals = 30; // ÿһ�еļ��
	private ArrayList<List> queues=null; //���ڴ�Ÿ�ʴ����Ķ���
	private List<Long> times=null;//ʱ�����
	private List<String> lrcs=null;//�����Ϣ����
	private long begin = 0;				//��ʼ���ŵ�ʱ��
	private long nextTimeMill=0;		//������ʾ��ʵ���һ��ʱ���
	private UpdateLrcRunable updateLrcRunable=null;//���¸�ʵķ���
	private long pauseTimeMills=0;		//��ͣ��ʱ�䳤��
	private boolean isPlaying=false;	//�Ƿ񲥷�

	/***********************���췽��******************************/
	//���췽����ʵ��
	public VerticalScrollTextView(Context context) {
		super(context);
		
	}
	public VerticalScrollTextView(Context context, AttributeSet attr) {
		super(context, attr);
	}
	public VerticalScrollTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	private void init() {
			setFocusable(true);//setFocusable�������Ƿ�����˿ؼ��л�ý��������;��requestFocus�������ȡ����
			
			//������һ�׵�ʱ��������ΪĬ��ֵ
			begin = 0;
			isPlaying=false;
			pauseTimeMills=0;
			index = 0;
			System.out.println("init()  index--->"+index);
			nextTimeMill=0;
			//�����Ϣ����Ϊ�գ�����Ĭ������
			if(queues == null)
			{
				setDefaultQueues();
			}
			else
			{
				times=queues.get(0);
				lrcs=queues.get(1);
			}
			
			//����Ĭ����ʽ
			setDefaultLRCStyle();
			 
			 //begin=System.currentTimeMillis();

		}
	/***********************onDraw()������ʵ��******************************/
	
	//ֻ�ǻ���һ����
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawColor(0x66698B69);//���û�����ɫ͸����
		//������ǰ��������
		canvas.drawText(lrcs.get(index), middleX, middleY, fLRC);
		System.out.println("lrcs.get(index)  "+lrcs.get(index)+" index "+index);
		//����֮ǰ����
		float tempY = middleY;//tempYΪ��ǰ�е�����
		for (int i = index - 1; i >= 0; i--) {			
			tempY = tempY - Intervals;
			if (tempY < 0) {
					break;
				}
			canvas.drawText(lrcs.get(i), middleX, tempY, uLRC);
		}
		
		//����֮�����
		tempY = middleY;
		
		for (int i = index + 1; i < lrcs.size(); i++) {
			// ��������
			tempY = tempY + Intervals;
		
			if (tempY > viewH) {
				
				break;
			}
			
			canvas.drawText(lrcs.get(i), middleX, tempY, uLRC);	
			
		}
		
	}
	
	//��View��һ�μ���ʱ�����ȵ���onSizeChanged
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		viewW = w;
		viewH = h;
		middleY =h*0.5f;
		middleX= w*0.5f;
	}

	/***********************textview�������ݺ�ʱ��******************************/
	//�����Ϣ����Ϊ�գ�����Ĭ������
	private void setDefaultQueues()
	{
		queues=new ArrayList<List>();
		times=new LinkedList<Long>();//������queue<E>����E�Ƿ��ͣ�������object������
		lrcs=new LinkedList<String>();
		Long e=0L;
		times.add(e);
		lrcs.add("û�и����Ϣ");
		queues.add(times);
		queues.add(lrcs);
	}
	//���ø����ʾ��Ĭ����ʽ
	public void  setDefaultLRCStyle()
	{
		// �Ǹ�������
		uLRC = new Paint();
		uLRC.setAntiAlias(true);//���û��ʵľ��Ч���� 
		uLRC.setTextAlign(Paint.Align.CENTER);
		uLRC.setTextSize(16);
		uLRC.setColor(0x1e000000);//����Ϊ70%͸��ǳ��ɫ.
		//0xffff00ff��int���͵����ݣ�����һ��0x|ff|ff00ff��0x�Ǵ�����ɫ�����ı�ǣ�ff�Ǳ�ʾ͸���ȣ�00 ��ʾ��ȫ͸����ff ��ʾ��ȫ��͸����
		//ff00ff��ʾ��ɫ��ע�⣺����ffff00ff������8������ɫ��ʾ��������ff00ff����6������ɫ��ʾ��
		uLRC.setTypeface(Typeface.SERIF);//��������
		
		// �������� ��ǰ���
		fLRC = new Paint();
		fLRC.setAntiAlias(true);
		fLRC.setTextAlign(Paint.Align.CENTER);
		fLRC.setColor(0xff33ffff);//��͸����ɫ
		fLRC.setTextSize(18);
		fLRC.setTypeface(Typeface.SANS_SERIF);
	}
	//�õ���ʶ���
	public ArrayList<List> getQueues() {
		return queues;
	}
	//��ʼ����ʶ��У�����ʱ��͸�ʵ������������У�
	public void setQueues(ArrayList<List> queues) {
		this.queues = queues;
		//System.out.println(" test lrc "+queues.get(1).get(0));
		init();//��ʼ�����к͸��Ĭ����ʽ
		
	}
	//���µ�ǰ�ĸ���index
	public long updateIndex(int index) {	
		if (index == -1)
			return -1;
		this.index=index;		
		return index;
	}
	
	public void setPlaying()
	{
		if(isPlaying)
		{
			lrcHandler.removeCallbacks(updateLrcRunable);
			pauseTimeMills=System.currentTimeMillis();//��¼��ǰֹͣ��ʱ��
			}
		else{
			lrcHandler.postDelayed(updateLrcRunable, 5);
			begin=System.currentTimeMillis()-pauseTimeMills+begin;
			}
		isPlaying = isPlaying?false:true;
	}
	
	//*************************handlerʹ��*******************//
	
	//��ʱ����������ʵ��
	class UpdateLrcRunable implements Runnable
	{
		 //int index=0;//��ǰ�����ĸ��
		
		 public UpdateLrcRunable()
		 {
			 System.out.println(index);
		 }

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			long offset=System.currentTimeMillis()-begin;
			//System.out.println("offset  "+offset+"  nextTimeMill " +nextTimeMill );
			
			if(offset >nextTimeMill  && index < times.size())
			{
				updateIndex(index);//����index
				System.out.println("BvertiS index "+index);
				uiHandler.postDelayed(updateResults,0);//System.out.println("������ͼ");
				//uiHandler.
				int i = 0;
				while(i< 100)
				{
					i++;
					i++;
					i++;
					i++;
					i++;
					i++;
					i++;
					i++;
					i++;
					i++;
					i++;
					i++;
				}
				index++;//�����������ƶ�һ��
				System.out.println("++" +index);
				if( index < times.size())
					nextTimeMill = (Long) times.get(index);
				
				
			}
			
			lrcHandler.postDelayed(updateLrcRunable, 10);//���������ӳ�10����
		}

			
		
	}
	
	
	public void updateUI(int msg){
		updateLrcRunable=new UpdateLrcRunable();
		System.out.println(isPlaying);
		if(isPlaying)
			lrcHandler.post(updateLrcRunable);
		if(msg == Mp3playerConstant.PlayMSG.DOWN_MSG)
		{
			
		}
	}
	
	Runnable updateResults = new Runnable() {
		public void run() {
			System.out.println(" ������ͼ");
			invalidate(); // ������ͼ
		}
	};
	
	
}
