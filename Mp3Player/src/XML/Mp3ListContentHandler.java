package XML;

import java.util.List;

import model.Mp3Info;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Mp3ListContentHandler  extends DefaultHandler{
	
	private List <Mp3Info> infos=null;
	private Mp3Info mp3info=null;
	private String tagName=null;
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		String temp=new String(ch,start ,length);
		if(tagName.equals("id"))
		{
			mp3info.setId(temp);
		}else if(tagName.equals("mp3.name"))
		{
			mp3info.setMp3Name(temp);
		}
		else if(tagName.equals("mp3.size"))
		{
			mp3info.setMp3Size(temp);
		}
		else if(tagName.equals("lrc.name"))
		{
			mp3info.setLrcName(temp);
		}
		else if(tagName.equals("lrc.size"))
		{
			mp3info.setLrcSize(temp);
		}
		
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		if(qName.equals("resource"))
		{
			infos.add(mp3info);
		}
		tagName="";
	}

	public Mp3ListContentHandler(List<Mp3Info> infos) {
		super();
		this.infos = infos;
	}

	public List<Mp3Info> getInfos() {
		return infos;
	}

	public void setInfos(List<Mp3Info> infos) {
		this.infos = infos;
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		this.tagName=localName;
		if(tagName.equals("resource"))
		{
			mp3info=new Mp3Info();
		}
	}

	
}
