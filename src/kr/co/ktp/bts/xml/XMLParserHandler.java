package kr.co.ktp.bts.xml;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParserHandler extends DefaultHandler {
	
	
	private String elementName = "";
	private StringBuffer sBuffer = new StringBuffer();
	
	private ArrayList<String> tagList  = new ArrayList<String>();
	
	// XML ������ ������ �νĵǾ��� �� �߻��ϴ� �̺�Ʈ�� ó��
	public void startDocument() {
		//System.out.println("Start Document");
	}
	
	// XML ������ ���� �νĵǾ��� �� �߻��ϴ� �̺�Ʈ�� ó��
	public void endDocument() {
		//System.out.println("End Document");
	}
	
	// ������Ʈ�� ������ �ν��߾��� �� �߻��ϴ� �̺�Ʈ�� ó��
	public void startElement(String uri, String localName, String qname, Attributes attr) {
		System.out.println("[startElement] uri : " + uri + ", localName : " + localName + ", qname : " + qname + ", attr.getLength() : " + attr.getLength());
		
		elementName = qname;    //element���� ��� ������ �־� �д�.
		
		for(int i=0;i<attr.getLength();i++){
			
			String attrName=attr.getQName(i); // ù��° �±� ���� ����
			String attrValue=attr.getValue(attrName);
			
			System.out.println("�Ӽ���============= attr.getQName("+i+") : " + attrName + ", attr.getValue(attrName) : " + attrValue);
			
		}
		
		sBuffer.setLength(0);    // buffer �ʱ�ȭ
	}
	
	// ������Ʈ�� ���� �ν��߾��� �� �߻��ϴ� �̺�Ʈ�� ó��
	public void endElement(String uri, String localName, String qname) {
		System.out.println("[endElement] uri : " + uri + ", localName : " + localName + ", qname : " + qname);
	}
	
	// �� element�� �� (�νĵ� ������ �� ���׸�Ʈ�� ���ؼ� ȣ��)
	public void characters(char[] ch, int start, int length) throws SAXException {
		String strValue = "";
		
		// element�� ���� ���ϱ� ���ؼ��� buffer�� �νĵ� �� ���ڸ� start���� length��ŭ append�Ѵ�.
		sBuffer.append(new String(ch, start, length));
		strValue = sBuffer.toString().trim();
		
		if (strValue != null && strValue.length() != 0 && !strValue.equals("\n")) {
			System.out.println("elementName: " + elementName + ", strValue: "+ strValue);
		}
	}
	
	public ArrayList getTagList() {
		return tagList;
	}

}
