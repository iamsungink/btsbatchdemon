package kr.co.ktp.bts.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import kr.co.ktp.bts.dto.BillInfoDTO;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BillInfoHandler extends DefaultHandler{
	
	//This is the list which shall be populated while parsing the XML.
	private List<Map<String, Object>> billInfoList = null;
	//As we complete one billInfo block in XML
	private Map<String, Object> billInfoMap = null;
	
	private ArrayList<Map<String, Object>> chageList = new ArrayList<Map<String, Object>>();
	private Map<String, Object> chageMap = null;
	
	//As we read any XML element we will push that in this stack
	private Stack elementStack = new Stack();
	
	//As we complete one billInfo block in XML, we will push the BillInfoDTO instance in billInfoList
	private Stack objectStack = new Stack();
	
	//XML 문서의 시작이 인식되었을 때 발생하는 이벤트를 처리
	public void startDocument() {
		//System.out.println("Start Document");
	}
	
	//XML 문서의 끝이 인식되었을 때 발생하는 이벤트를 처리
	public void endDocument() {
		//System.out.println("End Document");
	}
	
	// 엘리먼트의 시작을 인식했었을 때 발생하는 이벤트를 처리
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		//System.out.println("[startElement] uri : " + uri + ", localName : " + localName + ", qName : " + qName + ", attributes.getLength() : " + attributes.getLength());
		
		Vector v = new Vector();
		v.add(0, qName);
		v.add(1, attributes.getValue("cd"));
		
		this.elementStack.push(v);
		
		if(qName.equalsIgnoreCase("BILLINFO")){
			
			billInfoMap = new HashMap<String, Object>();
			
			if(attributes != null && attributes.getLength() == 1){
				billInfoMap.put("billInfoNo", attributes.getValue("no"));
			}
			
			if(billInfoList == null)
				billInfoList = new ArrayList<Map<String, Object>>();
			
			this.objectStack.push(billInfoMap);
			
		}else if(qName.equalsIgnoreCase("DATA") && currentAttributeValue().equalsIgnoreCase("DATA") && prevElement(-2).equalsIgnoreCase("CHAGE")){
			
			chageMap = new HashMap<String, Object>();
			
		}
		
	}
	
	// 엘리먼트의 끝을 인식했었을 때 발생하는 이벤트를 처리
	public void endElement(String uri, String localName, String qName) {
		//System.out.println("[endElement] uri : " + uri + ", localName : " + localName + ", qName : " + qName);
		
		if(qName.equalsIgnoreCase("BILLINFO")){
			Map<String, Object> stackPopMap = (Map) this.objectStack.pop();
			
			ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			
			list.addAll(chageList);
			chageList.clear();
			
			stackPopMap.put("chageList", list);
			this.billInfoList.add(stackPopMap);
			
		}else if(qName.equalsIgnoreCase("DATA") && currentAttributeValue().equalsIgnoreCase("DATA") && prevElement(-2).equalsIgnoreCase("CHAGE")){
			this.chageList.add(chageMap);
		}
		
		this.elementStack.pop();
	}
	
	// 각 element의 값 (인식된 문자의 각 세그먼트에 대해서 호출)
	public void characters(char[] ch, int start, int length) throws SAXException {
		//System.out.println("grandElement >> " + prevElement(-3) + ", parentElement >> " + prevElement(-2) +", currentElement() >> " + currentElement() + ", currentAttributeValue() >> " + currentAttributeValue() + ", new String(ch, start, length) : " + new String(ch, start, length));
		
		String value = new String(ch, start, length).trim();
		
		if (value.length() == 0){
			return; // ignore white space
		}
		
		//BILL_REF > TI:cd="REP_SVC_CONT_ID"
		//BILL_REF > TI:cd="MOBILE_NO"
		if(prevElement(-2).equalsIgnoreCase("BILL_REF")){
			if(currentElement().equalsIgnoreCase("TI")){
				if(currentAttributeValue().equalsIgnoreCase("REP_SVC_CONT_ID")){
					Map<String, Object> stackPeekMap = (Map) this.objectStack.peek();
					stackPeekMap.put("billRefRepSvcContId", value);
				}else if(currentAttributeValue().equalsIgnoreCase("MOBILE_NO")){
					Map<String, Object> stackPeekMap = (Map) this.objectStack.peek();
					stackPeekMap.put("billRefMobileNo", value);
				}
			}
		}
		
		//CHAGE > DATA:cd="DATA" > TI:cd="ACRND_ITEM_ID"
		//CHAGE > DATA:cd="DATA" > TI:cd="DC_BEF_AMT"
		if(prevElement(-3).equalsIgnoreCase("CHAGE")){
			if(prevElement(-2).equalsIgnoreCase("DATA")){
				if(prevAttributeValue(-2).equalsIgnoreCase("DATA")){
					if(currentElement().equalsIgnoreCase("TI")){
						if(currentAttributeValue().equalsIgnoreCase("ACRND_ITEM_ID")){
							chageMap.put("chageAcrndItemId", value);
						}else if(currentAttributeValue().equalsIgnoreCase("DC_BEF_AMT")){
							chageMap.put("chageDcBefAmt", value);
						}
					}
				}
			}
		}
		
	}
	
	private String currentElement(){
		Vector v = (Vector) this.elementStack.peek();
		return (String) v.get(0);
	}
	
	private String prevElement(int pos){
		Vector v = (Vector) this.elementStack.elementAt(elementStack.size()+pos);
		return (String) v.get(0);
		
	}
	
	private String currentAttributeValue(){
		Vector v = (Vector) this.elementStack.peek();
		return (String) v.get(1);
	}
	
	private String prevAttributeValue(int pos){
		Vector v = (Vector) this.elementStack.elementAt(elementStack.size()+pos);
		return (String) v.get(1);
	}
	
	public List<Map<String, Object>> getBillInfoList(){
		return billInfoList;
	}

}
