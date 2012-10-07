package com.ycs.fe;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.dom.DOMDocumentFactory;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
import com.ycs.fe.dto.ResultDTO;

public class HTMLProcessorJsoupImpl extends HTMLProcessor {

private boolean templateprocessed = false;
	private Logger logger = Logger.getLogger(this.getClass());
 
	@Override
	public boolean getLastResult(){
		return templateprocessed;
	}

	 
	public   void appendXmlFragment(DocumentBuilder docBuilder, Node parent, List<?> fragment) throws IOException, SAXException {
//		logger.debug("still coming here");
		//Document doc = parent.getOwnerDocument();
//		Node fragmentNode = docBuilder.parse(new InputSource(new StringReader("<root>"+fragment+"</root>"))).getDocumentElement();
		@SuppressWarnings("rawtypes")
		List nl = fragment;
		if(((Node) fragment.get(0)).getNodeType() == Node.CDATA_SECTION_NODE){
			appendXmlFragment( docBuilder,  parent,  ((Element) fragment.get(0)).getText());
			return;
		}
		Element elmparent = (Element) parent;
		for (int i = 0; i < nl.size(); i++) {
			Element n = (Element) nl.get(i);			
				//Node node = doc.importNode(n, true);
				elmparent.appendContent(n);
		}
	}
 
	 
	public   void appendXmlFragment(DocumentBuilder docBuilder, Node parent, String fragment) throws IOException, SAXException {
//		logger.debug("still coming here");
//		Document doc = parent.getOwnerDocument();
//		Node fragmentNode = docBuilder.parse(new InputSource(new StringReader("<root>"+fragment+"</root>"))).getDocumentElement();
//		List nl = fragmentNode.getChildNodes();
//		for (int i = 0; i < nl.size(); i++) {
//			Node n = nl.get(i);			
//				Node node = doc.importNode(n, true);
//				parent.appendContent(node);
//		}
		try {
			Document domdoc = DocumentHelper.parseText("<root>"+fragment+"</root>");
			Element elmparent = (Element) parent;
			elmparent.appendContent(domdoc.getRootElement());
		} catch (DocumentException e) {
			logger.debug("appendXmlFragment Exception",e);
		}
		
	}

	public void processInputElm(Node xmlelmNode, org.jsoup.nodes.Document dochtml, org.jsoup.nodes.Element headNode){
		@SuppressWarnings("unchecked")
		List<Node> nl =  xmlelmNode.selectNodes("//fields/field/input");// xp.evaluate("//fields/field/input", xmlelmNode, XPathConstants.NODESET);
		for (int i = 0; i < nl.size(); i++) {
			Element inputElm = (Element) nl.get(i);
//		    logger.debug(" .. found input type = ..");
		    String type = inputElm.attributeValue("type");
		    String htmlid = inputElm.attributeValue("forid");
			org.jsoup.nodes.Element n = dochtml.getElementById(htmlid);//("//*[@id=\""+htmlid+"\"]");//(Node) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
			String replace=inputElm.attributeValue("replace");
			
			logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
			if(n != null){
				if(replace.equals("append")){
				//if(!n.tagName().equalsIgnoreCase("input")){	
					if(type.equalsIgnoreCase("text") || type.equalsIgnoreCase("password")){
						org.jsoup.nodes.Element element = n.appendElement("input");
						element.attr("name", inputElm.attributeValue("name"));
						element.attr("value", inputElm.attributeValue("value"));
						element.attr("type", inputElm.attributeValue("type"));
						element.attr("class", inputElm.attributeValue("class"));
						element.attr("id", inputElm.attributeValue("id"));
						//n.appendContent(element);
				    }else if(type.equalsIgnoreCase("radio") || type.equalsIgnoreCase("checkbox")){
				    	String listValue = inputElm.attributeValue("value");
						if(listValue != null && listValue != ""){
							listValue = listValue.replace("{", " ");
							listValue = listValue.replace("}", " ");
							String[] list = listValue.split(",");
							for(int j=list.length-1;j>=0;j--){
								String val = list[j].trim();
								String[] key = val.split("=");
								org.jsoup.nodes.Element element = n.appendElement("input");
								element.attr("name", inputElm.attributeValue("name"));
								element.attr("value", key[0]);
								element.attr("type", inputElm.attributeValue("type"));
								element.attr("class", inputElm.attributeValue("class"));
								element.attr("id", inputElm.attributeValue("id")+(j+1));
								element.appendText(key[1]);
								//n.appendContent(element);
							}
							
						}
				    }
				//}
				}else{ //this is an input element we just need to set the values
					if(type.equalsIgnoreCase("text") || type.equalsIgnoreCase("password")){
						n.attr("value", inputElm.attributeValue("value"));
					}else if(type.equalsIgnoreCase("radio") || type.equalsIgnoreCase("checkbox")){
						n.attr("value", inputElm.attributeValue("value"));
					}
					
				}
//				n.attr("value", inputElm.attributeValue("value"));
//				n.setTextContent(inputElm.attributeValue("value"));

			}else{
				//TODO: We need to insert in custom fields
			}
		}
	}
	public void processCustomElm(Node xmlelmNode, org.jsoup.nodes.Document dochtml, org.jsoup.nodes.Element headNode){
		@SuppressWarnings("unchecked")
		List<Node> nl = xmlelmNode.selectNodes("//fields/field/customfield");//(List) xp.evaluate("//fields/field/customfield", xmlelmNode, XPathConstants.NODESET);
		for (int i = 0; i < nl.size(); i++) {
			Element inputElm = (Element) nl.get(i);
			String htmlid = inputElm.attributeValue("forid");
			org.jsoup.nodes.Element n =   dochtml.getElementById(htmlid);//.selectSingleNode("//*[@id=\""+htmlid+"\"]");//(Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
			logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
			//String replace=inputElm.attributeValue("replace");
			if(n != null){
//				List textnodenl = inputElm.selectNodes("text");
				Element textelement = inputElm.element("text");
//				logger.debug("TO Remove"+(String)textelement.getData());
				n.append((String)textelement.getData());
				//appendXmlFragment(dbuild,n, (String)textelement.getData());
				
			}else{ //replace="modify" should not come
				//TODO: We need to insert in custom fields 
			}
			
		}
	}
	public void processLabelField(Node xmlelmNode, org.jsoup.nodes.Document dochtml, org.jsoup.nodes.Element headNode){
		@SuppressWarnings("unchecked")
		List<Node> nl = xmlelmNode.selectNodes("//fields/field/label");//(List) xp.evaluate("//fields/field/display", xmlelmNode, XPathConstants.NODESET);
		for (int i = 0; i < nl.size(); i++) {
			Element inputElm = (Element) nl.get(i);
			String htmlid = inputElm.attributeValue("forname");
			org.jsoup.select.Elements lblele =	dochtml.getElementsByAttributeValue("name", htmlid);
			String replace=inputElm.attributeValue("replace");
			String prop = inputElm.attributeValue("key");
			String key = null;
			//String resourceBundle = ActionContext.getContext().getValueStack().findString("resourceBundle");
			String resourceBundle = (String) ActionContext.getContext().getSession().get("resourceBundle");
			if (resourceBundle != null){
				ResourceBundle labels = ResourceBundle.getBundle(resourceBundle, ActionContext.getContext().getLocale());
				if (prop != null && prop.trim() != "") {
					key = labels.getString(prop);
				}
			}
			logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
			for (org.jsoup.nodes.Element n : lblele) {
				if (n != null) {
					if (replace.equals("append")) {
						if (key != null && key.trim() != "") {
							n.appendText(key);
						} else {
							n.appendText(inputElm.attributeValue("value"));
						}
					} else {
						n.empty();
						if (key != null && key.trim() != "") {
							n.appendText(key);
						} else {
							n.appendText(inputElm.attributeValue("value"));
						}
					}
				} else {
					// TODO: We need to insert in custom fields
				}
			}
		}
	}
	
	public void processDisplayField(Node xmlelmNode, org.jsoup.nodes.Document dochtml, org.jsoup.nodes.Element headNode){
		@SuppressWarnings("unchecked")
		List<Node> nl = xmlelmNode.selectNodes("//fields/field/display");//(List) xp.evaluate("//fields/field/display", xmlelmNode, XPathConstants.NODESET);
		for (int i = 0; i < nl.size(); i++) {
			Element inputElm = (Element) nl.get(i);
			String htmlid = inputElm.attributeValue("forid");
			org.jsoup.nodes.Element n =  dochtml.getElementById(htmlid);//.selectSingleNode("//*[@id=\""+htmlid+"\"]");//(Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
			String replace=inputElm.attributeValue("replace");
			logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
			if(n != null  ){
				if( replace.equals("append"))
				n.appendText(inputElm.attributeValue("value"));
				else{
					n.getAllElements().remove();
					n.appendText(inputElm.attributeValue("value"));
				}
			}else{
				//TODO: We need to insert in custom fields
			}
			
		}
	}
	/**
	 * Processes &lt;select/>. 
	 * @param xmlelmNode
	 * @param dochtml
	 * @param headNode
	 * 
	 */
	public void processSelectElm(Node xmlelmNode, org.jsoup.nodes.Document dochtml, org.jsoup.nodes.Element headNode){
		 
		@SuppressWarnings("unchecked")
		List<Node> nl =xmlelmNode.selectNodes("//fields/field/select");// (List) xp.evaluate("//fields/field/select", xmlelmNode, XPathConstants.NODESET);
		for (int i = 0; i < nl.size(); i++) {
			Element inputElm = (Element) nl.get(i);
			String htmlid = inputElm.attributeValue("forid");
			String id = inputElm.attributeValue("id");
			org.jsoup.nodes.Element n =   dochtml.getElementById(htmlid);// dochtml.selectSingleNode("//*[@id=\""+htmlid"\"]");//(Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
			String replace=inputElm.attributeValue("replace");
			
			logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
			if(replace == null || replace.equals("modify")){
				//List textnodenl = inputElm.selectNodes("text");
				Element textelement = inputElm.element("text");
//				if(textnodenl != null){
//					textelement = (Element) textnodenl.get(0);
//				}
//				CDATASection cdata = dochtml.createCDATASection(textelement.getText());
//				element.appendContent(cdata);
//				logger.debug("To remove:"+textelement.getText());
				if(n.tagName().equalsIgnoreCase("select")  && textelement !=null){
					//do nothing as this is the select element, but remove the option tags and append options of template <text>
					n.empty();
//1. Fill from <Text>
					org.jsoup.nodes.Document selecttext = Jsoup.parse("<fakeroot>"+textelement.getText()+"</fakeroot>");
					 
					n.append(selecttext.select("option").outerHtml());
					
//				}else{
//					n.append(textelement.getText());
					
				}
			}else{ //replace="append"
				Element textelement = inputElm.element("text");
				if(textelement == null){
					n.append("<select />").child(0).attr("id", id); //append but text is empty
				}else{
					org.jsoup.nodes.Document selecttext = Jsoup.parse("<fakeroot>"+textelement.getText()+"</fakeroot>");
					if(selecttext.select("select").size() == 0){
						//CDATA must contain <option/>
						n.append("<select />").child(0).attr("id", id).append(selecttext.select("option").outerHtml());
					}else{
						//CDATA must contain <select><option/></select>
						n.append(selecttext.select("select").outerHtml());  //append with data from <text>
					}
				}
			}
				//appendXmlFragment(dbuild,n,textelement.getText());
			logger.debug("To Remove:"+n.outerHtml());
				
//2. Fill from list 
				String listValue = inputElm.attributeValue("value");
//				if(listValue != null && "".equals(listValue)){
				if(listValue != null) { 
				    listValue = listValue.replace("{", " ");
					listValue = listValue.replace("}", " ");
					String[] list = listValue.split(",");
//					List List = dochtml.selectNodes("select");
//					Node node = List.get(0);
//					logger.debug("To remove:"+"//select[@id=\""+htmlid+"\"]");
					org.jsoup.nodes.Node node = dochtml.getElementById(id);//.selectSingleNode("//select[@id=\""+htmlid+"\"]");
					
					for(String val:list){
						val = val.trim();
						String[] key = val.split("=");
						if(val.indexOf("=") > -1){
							org.jsoup.nodes.Element element =  ((org.jsoup.nodes.Element)node).appendElement("option");
							element.attr("value", key[0]);
							element.text(key[1]);
						}
//						Element nodelm = (Element) node;
						//nodelm.appendContent(element);
					}
		        }
//				}else{ //if hard coded values are not there then look for filling up from action context
//3. Fill from ValueStack
					ValueStack stack = ActionContext.getContext().getValueStack();
					@SuppressWarnings("unchecked")
					Map<String,String>opts = (Map<String, String>) stack.findValue(id);
					
					if(opts != null && opts.size() != 0){
						logger.debug("Populating <select/> from ValueStack");
//						List list = dochtml.selectNodes("select");
//						Node node = list.get(0);
						org.jsoup.nodes.Node node2 = dochtml.getElementById(id);//.selectSingleNode("//select[@id=\""+htmlid+"\"]");
						for (Entry<String, String> option : opts.entrySet()) {
							org.jsoup.nodes.Element element = ((org.jsoup.nodes.Element) node2).appendElement("option");
							element.attr("value", option.getKey());
							element.text(option.getValue());
							//Element nodelm = (Element) node2;
							//nodelm.appendContent(element);
						}
// from selectonload query
					}else{ 
					ResultDTO resultdto = null;
					List optlist = null;
					if( stack.getContext().containsKey("resultDTO")){
						 resultdto =  (ResultDTO) stack.getContext().get("resultDTO");
					}
					if(resultdto != null){
						 HashMap<String, Object> datamap = resultdto.getData();
						 if(datamap.containsKey(id)){
							 Object data = datamap.get(id);
							if(data instanceof List){
								optlist = (List)data;
							}
						 }
					}
					
					if(optlist != null){
						logger.debug("Populating <select/> from selectonload query");
						org.jsoup.nodes.Node node2 = dochtml.getElementById(id);//.selectSingleNode("//select[@id=\""+htmlid+"\"]");
						for(int x=0; x < optlist.size();x++){
							Map<String,String> option = (Map<String,String>)optlist.get(x);
							org.jsoup.nodes.Element element = ((org.jsoup.nodes.Element) node2).appendElement("option");
							element.attr("value", option.get(inputElm.attributeValue("key")));
							element.text(option.get(inputElm.attributeValue("value")));
						}
					}
				}

					
			
			
		}
	}
	public void processScripts(Node xmlelmNode, org.jsoup.nodes.Document dochtml, org.jsoup.nodes.Element headNode){
		///multiple scriptincludes and scripts tags
		Iterator<?> scriptsnl =   ((Element)xmlelmNode.selectSingleNode("//scripts")).nodeIterator();
		
		for (int i = 0;  scriptsnl.hasNext(); i++) {
			Node scriptnode = (Node) scriptsnl.next();
			if(scriptnode.getNodeType() == Node.ELEMENT_NODE && "text".equals(scriptnode.getName())){
//				logger.debug("To Remove:script:"+scriptnode.getText());
				headNode.append(scriptnode.getText());
				//appendXmlFragment(dbuild,headNode,scriptnode.getText());
			}
			if(scriptnode.getNodeType() == Node.ELEMENT_NODE && "scriptinclude".equals(scriptnode.getName())){
				if(scriptnode.getText().trim().length() == 0) continue;
				String[] includeScripts = scriptnode.getText().split(",");
				for (String val : includeScripts) {
					org.jsoup.nodes.Element e = headNode.appendElement("script");
					e.attr("src", "../js/"+val);
					e.attr("language", "JavaScript");
					e.attr("type", "text/javascript");
					 // headNode.appendContent(e);
					headNode.appendText("\n");
				}
			}
		}
		
		
		///multiple scriptincludes and scripts tags
		
		///multiple styleincludes and stylesheet tags
		/*String stylesheets = (String)xp.evaluate("//stylesheets/text()", xmlelmNode, XPathConstants.STRING);
		if(stylesheets != null){
			appendXmlFragment(dbuild,headNode,stylesheets);
		}
		
		String styleInclude = (String)xp.evaluate("//styleinclude/text()", xmlelmNode, XPathConstants.STRING);
		if (styleInclude != null) {
			String[] includeStyles = styleInclude.split(",");
			for (String val : includeStyles) {
				Element e = document.getRootElement().addElement("link");
				e.attr("href", val);
				e.attr("rel", "stylesheet");
				e.attr("type", "text/css");
				headNode.appendContent(e);
			}
		}*/
	}
	public void processStyles(Node xmlelmNode, org.jsoup.nodes.Document dochtml, org.jsoup.nodes.Element headNode){
	//////
		Iterator<?> stylenl =   ((Element) xmlelmNode.selectSingleNode("//stylesheets")).nodeIterator();		
		for (int i = 0; stylenl.hasNext(); i++) {
			Node scriptnode = (Node) stylenl.next();
//			logger.debug("Adding styles"+scriptnode.getText());
			if(scriptnode.getNodeType() == Node.CDATA_SECTION_NODE){
				headNode.append(scriptnode.getText());
				//appendXmlFragment(dbuild,headNode,scriptnode.getText());
			}
			if(scriptnode.getNodeType() == Node.ELEMENT_NODE && "styleinclude".equals(scriptnode.getName())){
				if(scriptnode.getText().trim().length() == 0) continue;
				String[] includeScripts = scriptnode.getText().split(",");
				for (String val : includeScripts) {
					org.jsoup.nodes.Element e = headNode.appendElement("link");
					e.attr("href", "../css/"+ val);
					e.attr("rel", "stylesheet");
					e.attr("type", "text/css");
					//headNode.appendContent(e);
					headNode.appendText("\n");
				}
			}
		}
		///multiple styleincludes and stylesheet tags
	}
	public void processDivElm(Node xmlelmNode, org.jsoup.nodes.Document dochtml, org.jsoup.nodes.Element headNode){

		@SuppressWarnings("unchecked")
		List<Node> nl = xmlelmNode.selectNodes("//fields/field/div");// xp.evaluate("//fields/field/div", xmlelmNode, XPathConstants.NODESET);
		for (int i = 0; i < nl.size(); i++) {
			Element inputElm = (Element) nl.get(i);
			String htmlid = inputElm.attributeValue("forid");
			org.jsoup.nodes.Element n =     dochtml.getElementById(htmlid);//.selectSingleNode("//*[@id=\""+htmlid+"\"]");//(Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
			logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
			String replace=inputElm.attributeValue("replace");
			if(replace.equals("append")){
				
				//TODO: We need to insert in custom fields
				org.jsoup.nodes.Element body = dochtml.getElementsByTag("body").first();
				org.jsoup.nodes.Element div = dochtml.createElement("div");
//				logger.debug("To Remove"+inputElm.attributeValue("id"));
				div.attr("id",inputElm.attributeValue("id"));
				body.prependChild(div);
			}else{
				n.text(inputElm.attributeValue("value"));
			  
			}
			
		}
	}
	public void processCompositeElm(Node xmlelmNode, org.jsoup.nodes.Document dochtml, org.jsoup.nodes.Element headNode) throws JSONException{
		//compositefield for DataElements
		@SuppressWarnings("unchecked")
		List<Node> nl = xmlelmNode.selectNodes("//fields/field/compositefield");//(List) xp.evaluate("//fields/field/compositefield", xmlelmNode, XPathConstants.NODESET);
		for (int i = 0; i < nl.size(); i++) {
			Element compositefield = (Element) nl.get(i);
			Element datafield = (Element) compositefield.selectNodes("datafield").get(0);
			String dfid=datafield.attributeValue("id");
			String dfforid=datafield.attributeValue("forid");
			String dfname=datafield.attributeValue("name");
			String dfvalue=datafield.attributeValue("value");
			String dftype=datafield.attributeValue("type");
			String replace=datafield.attributeValue("replace");
			
			if(dfvalue.length() == 0){
				dfvalue="{}";
			}
			JSONObject dfjson = new JSONObject(dfvalue);
			@SuppressWarnings("unchecked")
			List<Node> displayfield = compositefield.selectNodes("displayfield");
			org.jsoup.nodes.Element datafldhtm = dochtml.createElement("input");
			datafldhtm.attr("type", dftype);
			datafldhtm.attr("id", dfid);
			datafldhtm.attr("name", dfname);
			datafldhtm.attr("value", dfvalue);
			org.jsoup.nodes.Element dfnode =   (org.jsoup.nodes.Element)dochtml.getElementById(dfforid);//.selectSingleNode("//*[@id=\""+dfforid+"\"]");// xp.evaluate("//*[@id=\""+dfforid+"\"]", dochtml, XPathConstants.NODE);
			boolean dfforidfound = false;
			if(dfforid == null && replace != null && replace.equals("append")){
				dfforidfound = true;
				dfnode.appendChild(datafldhtm);
			}
			for (int j = 0; j < displayfield.size(); j++) {
				Element dispelmxml = (Element)displayfield.get(j);
				String dename = dispelmxml.attributeValue("name");
				String deforid = dispelmxml.attributeValue("forid");
				String detype = dispelmxml.attributeValue("type");
				org.jsoup.nodes.Element n = (org.jsoup.nodes.Element)dochtml.getElementById(deforid);//.selectSingleNode("//*[@id=\""+deforid+"\"]");// xp.evaluate("//*[@id=\""+deforid+"\"]", dochtml, XPathConstants.NODE);
				org.jsoup.nodes.Element difldhtm = dochtml.createElement("input");
				difldhtm.attr("type", detype);
				difldhtm.attr("id", dename);
				difldhtm.attr("name", dename);
				if(dfjson.has(dename))
				difldhtm.attr("value", dfjson.getString(dename));
				difldhtm.attr("onblur", "updateCompositeField(this,'#"+dfid+"')");
				if(dfforidfound == false && n!=null){
					dfforidfound = true; //appending to first display element
					n.appendChild(datafldhtm);
				}
				if(n!=null)
				n.appendChild(difldhtm);
			}
			
		}
		//compositefield for DataElements end
	}
	
	public void processButtonElm(Node xmlelmNode, org.jsoup.nodes.Document dochtml, org.jsoup.nodes.Element headNode){

		@SuppressWarnings("unchecked")
		List<Node> nl = xmlelmNode.selectNodes("//button");// xp.evaluate("//fields/field/div", xmlelmNode, XPathConstants.NODESET);
		for (int i = 0; i < nl.size(); i++) {
			Element inputElm = (Element) nl.get(i);
			String htmlid = inputElm.attributeValue("forid");
			org.jsoup.nodes.Element n =     dochtml.getElementById(htmlid);//.selectSingleNode("//*[@id=\""+htmlid+"\"]");//(Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
			logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
			String replace=inputElm.attributeValue("replace");
			if(replace.equals("append")){
				org.jsoup.nodes.Element button =   n.appendElement("button");
			    if(inputElm.attributeValue("id") == null){
			    	logger.info("Id is required");
			    }else{
			    	button.attr("id",inputElm.attributeValue("id"));
			    }
			    if(inputElm.attributeValue("type") == null){
			    	logger.info("Type is required and it should be either (button or submit)");
			    }else{
			    	button.attr("type",inputElm.attributeValue("type"));
			    }
			    
			    if(inputElm.attributeValue("onclick") != null){
			    	button.attr("onclick", inputElm.attributeValue("onclick")); 
			    }
			    if(inputElm.getText() == null){
			    	logger.info("Button Text is required");
			    }else{
			    	button.append(inputElm.getText());
			    }
			}else{//replace=modify
				if(inputElm.getText() == null){
			    	logger.info("Button Text is required");
			    }else{
			    	n.html(inputElm.getText());
			    }
				if(inputElm.attributeValue("onclick") != null)
			    n.attr("onclick", inputElm.attributeValue("onclick")); 
			}
			
		}
	}
	
	public void processValidation(Node xmlelmNode, org.jsoup.nodes.Document dochtml, org.jsoup.nodes.Element headNode, StringBuffer globaljs){
		//Append all validations
		@SuppressWarnings("unchecked")
		List<Node> nl =xmlelmNode.selectNodes("//validation");// (List) xp.evaluate("//validation", xmlelmNode, XPathConstants.NODESET);
		for (int i = 0; i < nl.size(); i++) {
			String validation = ((Element) nl.get(i)).getText();
			
			if(validation != null && validation.length() > 1){
				globaljs.append( validation +"\n");
			}
		}
	}
	public void processRules(Node xmlelmNode, org.jsoup.nodes.Document dochtml, org.jsoup.nodes.Element headNode, StringBuffer globaljs) throws JSONException{
		//JSON rule begin
		@SuppressWarnings("unchecked")
		List<Node> nl = xmlelmNode.selectNodes("//rule");//(List) xp.evaluate("//rule", xmlelmNode, XPathConstants.NODESET);
		JSONObject rulejson = new JSONObject("{rules:{},messages:{}}");
		for (int i = 0; i < nl.size(); i++) {
			Element ruleElm = (Element) nl.get(i);
			String ruletext = ruleElm.getText();
			logger.debug("Ruleobj="+ruletext);
			if(ruletext != null && ruletext.length() > 0){
				JSONArray jar = new JSONArray(ruleElm.getText());
				//JSONObject messageelmpart =  jobj.getJSONObject("messages");
				for (int j = 0; j < jar.length(); j++) {
					JSONObject jobj = jar.getJSONObject(j);
					String fieldname=jobj.getString("fieldname");
					rulejson.getJSONObject("rules").put(fieldname, jobj.get("rules"));
					rulejson.getJSONObject("messages").put(fieldname, jobj.get("messages"));
				} 
				 
			}
		}
		//default rule properties ,errorElement:\"div\",errorLabelContainer:\"#alertmessage\"
//		rulejson.put("errorElement", "label");
//		rulejson.put("errorLabelContainer", "#alertmessage");
//		rulejson.put("submitHandler", "JSONincludedFunc:function(form){ alert('hi');}");
		globaljs.append("var ruleobj="+rulejson.toString(3)+";\n");
		//JSON rule ends
		
		
	}
	private void processSaveFields(Element xmlelmNode, org.jsoup.nodes.Document dochtml, org.jsoup.nodes.Element headNode, StringBuffer globaljs) {
		//savefieldids begin
		@SuppressWarnings("unchecked")
		List<Node> panels = xmlelmNode.selectNodes("/root/panels/panel/crud");
		for (Node panel : panels) {
			Node savefield = panel.selectSingleNode("savefieldids");
			logger.debug("savefieldids:"+savefield);
			String id = ((Element) panel).attributeValue("id");
			if(savefield != null ){
				globaljs.append("var panel_"+id+" = [");
				String[] idlist = savefield.getText().split(",");
				
				if(idlist[0]!=null && !"".equals(idlist[0]))
				globaljs.append("\""+idlist[0]+"\"");
				
				for (int i = 1; i < idlist.length; i++) {
					globaljs.append(",\""+idlist[i]+"\"");
				}

				globaljs.append("];\n");
				 
			}
		}
		//savefieldids end
	}
	private void processScreenName(Element xmlelmNode, org.jsoup.nodes.Document dochtml, org.jsoup.nodes.Element headNode, StringBuffer globaljs) {
		String screenName =  ((Element) xmlelmNode.selectSingleNode("/root/screen")).attributeValue("name");
		globaljs.append(" var screenName='"+screenName+"'");
	}

	private void processGlobalJs(Element xmlelmNode, org.jsoup.nodes.Document dochtml, org.jsoup.nodes.Element headNode, StringBuffer globaljs) {
		String strrule = "<script>"+globaljs+"</script>";
		headNode.append(strrule);
	}
	
	@Override
	public String process(String inputXML, ActionInvocation invocation){
		//ClassLoader loader = this.getClass().getClassLoader();	
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		String xmlString ="";
    
		 
		try {
			dbf.setIgnoringElementContentWhitespace(true); 
			DOMDocumentFactory factory = new DOMDocumentFactory(); //d4j
			//InputStream reader = new ByteArrayInputStream(inputXML.getBytes());
			SAXReader reader2 = new SAXReader();//d4j
			reader2.setDocumentFactory(factory);//d4j
			StringReader strreader = new StringReader(inputXML);
			logger.debug("HTMLProcessor: before input parsing");
			org.dom4j.Document docXML = reader2.read(strreader);
			logger.debug("HTMLProcessor: after d4j parsing");
			//Document docXML = (org.dom4j.dom.DOMDocument)d4jdoc;
			//Document docXML = dbuild.parse(reader);
			logger.debug("HTMLProcessor: after input parsing");
			Element xmlelmNode = docXML.getRootElement();
			
			String pathhtml = null;
			
			if(xmlelmNode.selectNodes("//htmltemplate").size() > 0){
				pathhtml = xmlelmNode.selectSingleNode("//htmltemplate").getText().trim();
				logger.debug("htmltemplate:"+pathhtml);
			}else{
				logger.debug("HTMLProcessor: no template found in "+inputXML.substring(0,20)+"..." ); 
				templateprocessed= false;
				return "HTMLProcessor: no template found in "+inputXML.substring(0,20)+"...";
			}
			logger.debug("HTMLProcessor: HTML TemplatePath="+ServletActionContext.getServletContext().getRealPath("/"+pathhtml));
			pathhtml = ServletActionContext.getServletContext().getRealPath("/"+pathhtml);
//			logger.debug("HTMLProcessor: HTML TemplatePath="+ServletActionContext.getServletContext().getRealPath("/"+pathhtml));
//			pathhtml = "C:/Eclipse/workspace1/FEtranslator1/WebContent/pages/logintpl.xml";//ServletActionContext.getServletContext().getRealPath("/"+pathhtml);
			if(new File(pathhtml).exists())logger.debug("The html exists");
			//FileInputStream fin = new FileInputStream(new File(pathhtml));
			//InputStream is = loader.getResourceAsStream("log4j.properties");
			logger.debug("HTMLProcessor: before html parsing");
			org.jsoup.nodes.Document dochtml = Jsoup.parse(new File(pathhtml), "UTF-8", "");
			logger.debug("HTMLProcessor: after html parsing");
			logger.debug("HTMLProcessor: dochtml num childs:"+dochtml.getAllElements().size());
			
			Elements headnl = dochtml.select("head");
			if(headnl.size() < 1){
				org.jsoup.nodes.Element htmltop =   dochtml.getElementsByTag("html").get(0);//.selectSingleNode("/html");
				 htmltop.appendElement("head");
				//TODO: append head
			}
			org.jsoup.nodes.Element headNode =   dochtml.getElementsByTag("head").get(0);//.selectSingleNode("/html/head");
			/*String scriptinclude = (String)xp.evaluate("//scriptinclude/text()", xmlelmNode, XPathConstants.STRING);
			if (scriptinclude != null) {
				String[] includeScripts = scriptinclude.split(",");
				for (String val : includeScripts) {
					Element e = document.getRootElement().addElement("script");
					e.attr("src", val);
					e.attr("language", "JavaScript");
					e.attr("type", "text/javascript");
					headNode.appendContent(e);
					headNode.appendContent(dochtml.createTextNode("\n"));
				}
			}
			
			String scripts = (String) xp.evaluate("//scripts/text()", xmlelmNode, XPathConstants.STRING);
			 
			if(scripts.length() > 1){
				appendXmlFragment(dbuild,headNode,scripts);
			}*/
			
			StringBuffer globaljs = new StringBuffer();
			
			//Processing panel specific nodes
			processInputElm(xmlelmNode, dochtml, headNode);
			processCustomElm(xmlelmNode, dochtml, headNode);
			processLabelField(xmlelmNode, dochtml, headNode);
			processDisplayField(xmlelmNode, dochtml, headNode);
			processSelectElm(xmlelmNode, dochtml, headNode);
			processScripts(xmlelmNode, dochtml, headNode);
			processStyles(xmlelmNode, dochtml, headNode);
			processDivElm(xmlelmNode, dochtml, headNode);
			processCompositeElm(xmlelmNode, dochtml, headNode);
			processButtonElm(xmlelmNode, dochtml, headNode);
			
			//processing global nodes
			processRules(xmlelmNode, dochtml, headNode, globaljs);
			processSaveFields(xmlelmNode, dochtml, headNode, globaljs);
			processScreenName(xmlelmNode, dochtml, headNode, globaljs);
			
			processGlobalJs(xmlelmNode, dochtml, headNode, globaljs);
			
			
			
			//appendXmlFragment(dbuild, headNode, strrule);
			
			
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			//initialize StreamResult with File object to save to file
//			StreamResult result = new StreamResult(new StringWriter());
//			DOMSource source = new DOMSource((org.dom4j.dom.DOMDocument)dochtml);
//			transformer.transform(source, result);
		
//			OutputFormat format = OutputFormat.createPrettyPrint();
//			 format.setExpandEmptyElements( true);
//			 StringWriter strw  = new StringWriter();
//			 HTMLWriter writer = new HTMLWriter(strw,format);
//			 
//			 writer.setEscapeText(false); 
//			 writer.write(dochtml);
			 xmlString = dochtml.html();
//			 logger.debug("To  Remove result HTML:"+xmlString);
//			xmlString = result.getWriter().toString(); 
//			xmlString = xmlString.replaceFirst("(\\<\\?xml[\\d\\D]*\\?\\>)","");
			templateprocessed = true;
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {			
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return xmlString;
		
	}


	

	@Override
	public void appendXmlFragment(DocumentBuilder docBuilder, org.w3c.dom.Node parent, NodeList fragment) throws IOException, SAXException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void appendXmlFragment(DocumentBuilder docBuilder, org.w3c.dom.Node parent, String fragment) throws IOException, SAXException {
		// TODO Auto-generated method stub
		
	}
public static void main(String args[]) {
	   HTMLProcessorJsoupImpl htmp = new HTMLProcessorJsoupImpl();
	   String processedhtml =  htmp.process(htmp.fileReadAll("C:/Eclipse/workspace/FEtranslator1/src/actionclass/sampleoutput.xml"), null);
	   System.out.println(processedhtml);
}
}
