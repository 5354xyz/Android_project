package model;

import java.io.Serializable;

//很多方法可以eclipse生成，get方法set方法：Source-->generate getters and setters
//toString方法：Source-->generate toString
//带参数构造方法：Source-->generate constructors u..
//不带参数构造方法：Source-->generate constructors super...


//实现序列化接口，方便service传值的时候放到intent对象中
public class Mp3Info implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String mp3Name;
	private String mp3Size;
	private String lrcName;
	private String lrcSize;
	public String getId() {
		return id;
	}
	public Mp3Info(String id, String mp3Name, String mp3Size,
			String lrcName, String lrcSize) {
		super();
		this.id = id;
		this.mp3Name = mp3Name;
		this.mp3Size = mp3Size;
		this.lrcName = lrcName;
		this.lrcSize = lrcSize;
	}
	@Override
	public String toString() {
		return "Mp3Info [id=" + id + ", mp3Name=" + mp3Name + 
				", mp3Size=" + mp3Size + ", lrcName=" + lrcName
				+ ", lrcSize=" + lrcSize + "]";
	}
	public Mp3Info() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMp3Name() {
		return mp3Name;
	}
	public void setMp3Name(String mp3Name) {
		this.mp3Name = mp3Name;
	}


	public String getMp3Size() {
		return mp3Size;
	}
	public void setMp3Size(String mp3Size) {
		this.mp3Size = mp3Size;
	}
	public String getLrcName() {
		return lrcName;
	}
	public void setLrcName(String lrcName) {
		this.lrcName = lrcName;
	}
	public String getLrcSize() {
		return lrcSize;
	}
	public void setLrcSize(String lrcSize) {
		this.lrcSize = lrcSize;
	}
	
}
