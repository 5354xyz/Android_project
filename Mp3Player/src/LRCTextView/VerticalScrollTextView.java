package LRCTextView;
//最主要是通过handler和线程来更新UI界面
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
	public int index = 0;//当前高亮的行的索引
	private Paint uLRC; //非当前焦点歌词的画笔
	private Paint fLRC; //当前焦点歌词的画笔
	private float viewH;//Lrc歌词的view的高度
	private float viewW;//Lrc歌词的view的宽度
	private float middleY;//半高
	private float middleX;
	private Handler uiHandler = new Handler();
	private Handler lrcHandler = new Handler();
	private static final int Intervals = 30; // 每一行的间隔
	private ArrayList<List> queues=null; //用于存放歌词处理后的队列
	private List<Long> times=null;//时间队列
	private List<String> lrcs=null;//歌词信息队列
	private long begin = 0;				//开始播放的时间
	private long nextTimeMill=0;		//更新显示歌词的下一个时间点
	private UpdateLrcRunable updateLrcRunable=null;//更新歌词的方法
	private long pauseTimeMills=0;		//暂停的时间长度
	private boolean isPlaying=false;	//是否播放

	/***********************构造方法******************************/
	//构造方法的实现
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
			setFocusable(true);//setFocusable是设置是否允许此控件有获得焦点的能力;而requestFocus是请求获取焦点
			
			//播放下一首的时候重新设为默认值
			begin = 0;
			isPlaying=false;
			pauseTimeMills=0;
			index = 0;
			System.out.println("init()  index--->"+index);
			nextTimeMill=0;
			//如果消息队列为空，设置默认内容
			if(queues == null)
			{
				setDefaultQueues();
			}
			else
			{
				times=queues.get(0);
				lrcs=queues.get(1);
			}
			
			//设置默认样式
			setDefaultLRCStyle();
			 
			 //begin=System.currentTimeMillis();

		}
	/***********************onDraw()方法的实现******************************/
	
	//只是画好一个面
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawColor(0x66698B69);//设置画布颜色透明度
		//画出当前高亮的行
		canvas.drawText(lrcs.get(index), middleX, middleY, fLRC);
		System.out.println("lrcs.get(index)  "+lrcs.get(index)+" index "+index);
		//画出之前的行
		float tempY = middleY;//tempY为当前行的坐标
		for (int i = index - 1; i >= 0; i--) {			
			tempY = tempY - Intervals;
			if (tempY < 0) {
					break;
				}
			canvas.drawText(lrcs.get(i), middleX, tempY, uLRC);
		}
		
		//画出之后的行
		tempY = middleY;
		
		for (int i = index + 1; i < lrcs.size(); i++) {
			// 往下推移
			tempY = tempY + Intervals;
		
			if (tempY > viewH) {
				
				break;
			}
			
			canvas.drawText(lrcs.get(i), middleX, tempY, uLRC);	
			
		}
		
	}
	
	//在View第一次加载时会首先调用onSizeChanged
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		viewW = w;
		viewH = h;
		middleY =h*0.5f;
		middleX= w*0.5f;
	}

	/***********************textview正文内容和时间******************************/
	//如果消息队列为空，设置默认内容
	private void setDefaultQueues()
	{
		queues=new ArrayList<List>();
		times=new LinkedList<Long>();//这里是queue<E>其中E是泛型，必须是object的子类
		lrcs=new LinkedList<String>();
		Long e=0L;
		times.add(e);
		lrcs.add("没有歌词信息");
		queues.add(times);
		queues.add(lrcs);
	}
	//设置歌词显示的默认样式
	public void  setDefaultLRCStyle()
	{
		// 非高亮部分
		uLRC = new Paint();
		uLRC.setAntiAlias(true);//设置画笔的锯齿效果。 
		uLRC.setTextAlign(Paint.Align.CENTER);
		uLRC.setTextSize(16);
		uLRC.setColor(0x1e000000);//设置为70%透明浅灰色.
		//0xffff00ff是int类型的数据，分组一下0x|ff|ff00ff，0x是代表颜色整数的标记，ff是表示透明度，00 表示完全透明，ff 表示完全不透明。
		//ff00ff表示颜色，注意：这里ffff00ff必须是8个的颜色表示，不接受ff00ff这种6个的颜色表示。
		uLRC.setTypeface(Typeface.SERIF);//设置字体
		
		// 高亮部分 当前歌词
		fLRC = new Paint();
		fLRC.setAntiAlias(true);
		fLRC.setTextAlign(Paint.Align.CENTER);
		fLRC.setColor(0xff33ffff);//不透明蓝色
		fLRC.setTextSize(18);
		fLRC.setTypeface(Typeface.SANS_SERIF);
	}
	//得到歌词队列
	public ArrayList<List> getQueues() {
		return queues;
	}
	//初始化歌词队列（包括时间和歌词的正文两个队列）
	public void setQueues(ArrayList<List> queues) {
		this.queues = queues;
		//System.out.println(" test lrc "+queues.get(1).get(0));
		init();//初始化队列和歌词默认样式
		
	}
	//更新当前的高亮index
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
			pauseTimeMills=System.currentTimeMillis();//记录当前停止的时间
			}
		else{
			lrcHandler.postDelayed(updateLrcRunable, 5);
			begin=System.currentTimeMillis()-pauseTimeMills+begin;
			}
		isPlaying = isPlaying?false:true;
	}
	
	//*************************handler使用*******************//
	
	//何时更新在这里实现
	class UpdateLrcRunable implements Runnable
	{
		 //int index=0;//当前高亮的歌词
		
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
				updateIndex(index);//更新index
				System.out.println("BvertiS index "+index);
				uiHandler.postDelayed(updateResults,0);//System.out.println("更新视图");
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
				index++;//焦点行向下移动一行
				System.out.println("++" +index);
				if( index < times.size())
					nextTimeMill = (Long) times.get(index);
				
				
			}
			
			lrcHandler.postDelayed(updateLrcRunable, 10);//调用自身，延迟10毫秒
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
			System.out.println(" 更新视图");
			invalidate(); // 更新视图
		}
	};
	
	
}
