package com.ycs.fe;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import org.jsoup.nodes.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.dom4j.dom.DOMDocumentFactory;
import org.dom4j.io.HTMLWriter;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;

public class HTMLProcessorJsoupFullImpl extends HTMLProcessor {

private boolean templateprocessed = false;
	private Logger logger = Logger.getLogger(this.getClass());
 
	@Override
	public boolean getLastResult(){
		return templateprocessed;
	}

	 
	public   void appendXmlFragment(DocumentBuilder docBuilder, Node parent, List fragment) throws IOException, SAXException {
//		logger.debug("still coming here");
		//Document doc = parent.getOwnerDocument();
//		Node fragmentNode = docBuilder.parse(new InputSource(new StringReader("<root>"+fragment+"</root>"))).getDocumentElement();
		
		
//		List nl = fragment;
//		if(((Node) fragment.get(0)).getNodeType() == Node.CDATA_SECTION_NODE){
//			appendXmlFragment( docBuilder,  parent,  ((Element) fragment.get(0)).text());
//			return;
//		}
//		Element elmparent = (Element) parent;
//		for (int i = 0; i < nl.size(); i++) {
//			Element n = (Element) nl.get(i);			
//				//Node node = doc.importNode(n, true);
//				elmparent.appendContent(n);
//		}
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
		
//		try {
//			Document domdoc = DocumentHelper.parseText("<root>"+fragment+"</root>");
//			Element elmparent = (Element) parent;
//			elmparent.appendContent(domdoc.getRootElement());
//		} catch (DocumentException e) {
//			logger.debug("appendXmlFragment Exception",e);
//		}
		
	}

	@Override
	public String process(String inputXML, ActionInvocation invocation){
		ClassLoader loader = this.getClass().getClassLoader();	
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbuild ;
		String xmlString ="";
    
		 
		try {
			dbf.setIgnoringElementContentWhitespace(true); 
			dbuild = dbf.newDocumentBuilder();
			
			DOMDocumentFactory factory = new DOMDocumentFactory(); //d4j
			InputStream reader = new ByteArrayInputStream(inputXML.getBytes());
			SAXReader reader2 = new SAXReader();//d4j
			reader2.setDocumentFactory(factory);//d4j
			StringReader strreader = new StringReader(inputXML);
			logger.debug("HTMLProcessor: before input parsing");
			Document docXML = Jsoup.parse(inputXML);//reader2.read(strreader);
			logger.debug("HTMLProcessor: after d4j parsing");
			//Document docXML = (org.dom4j.dom.DOMDocument)d4jdoc;
			//Document docXML = dbuild.parse(reader);
			logger.debug("HTMLProcessor: after input parsing");
			Element xmlelmNode = docXML.getElementsByTag("html").get(0);
			
			String pathhtml = null;
			
			if(xmlelmNode.select("htmltempalte").size() > 0){
				pathhtml = xmlelmNode.select("htmltempalte").first().text().trim();
				logger.debug("htmltemplate:"+pathhtml);
			}else{
				logger.debug("HTMLProcessor: no template found in "+inputXML.substring(0,20)+"..." ); 
				templateprocessed= false;
				return "HTMLProcessor: no template found in "+inputXML.substring(0,20)+"...";
			}
			XPath  xp = XPathFactory.newInstance().newXPath(); 
		 
		

			
//			logger.debug("HTMLProcessor: HTML TemplatePath="+ServletActionContext.getServletContext().getRealPath("/"+pathhtml));
//			pathhtml = ServletActionContext.getServletContext().getRealPath("/"+pathhtml);
			//logger.debug("HTMLProcessor: HTML TemplatePath="+ServletActionContext.getServletContext().getRealPath("/"+pathhtml));
			pathhtml = "F:/eclipse/workspace/charts/FEtranslator1/WebContent/pages/logintpl.xml";//ServletActionContext.getServletContext().getRealPath("/"+pathhtml);
			if(new File(pathhtml).exists())logger.debug("The html exists");
			FileInputStream fin = new FileInputStream(new File(pathhtml));
			//InputStream is = loader.getResourceAsStream("log4j.properties");
			logger.debug("HTMLProcessor: before html parsing");
			org.jsoup.nodes.Document dochtml = Jsoup.parse(new File(pathhtml), "UTF-8", "");
			logger.debug("HTMLProcessor: after html parsing");
			logger.debug("HTMLProcessor: dochtml num childs:"+dochtml.getAllElements().size());
			
			List nl =  xmlelmNode.select("input");// xp.evaluate("fields/field/input", xmlelmNode, XPathConstants.NODESET);
			logger.debug("To Remove: input"+nl.size());
			for (int i = 0; i < nl.size(); i++) {
				Element inputElm = (Element) nl.get(i);
//			    logger.debug(" .. found input type = ..");
			    String type = inputElm.attr("type");
			    String htmlid = inputElm.attr("forid");
				org.jsoup.nodes.Element n = dochtml.getElementById(htmlid);//("//*[@id=\""+htmlid+"\"]");//(Node) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null){
					if(type.equalsIgnoreCase("text") || type.equalsIgnoreCase("password")){
						org.jsoup.nodes.Element element = n.appendElement("input");
						element.attr("name", inputElm.attr("name"));
						element.attr("value", inputElm.attr("value"));
						element.attr("type", inputElm.attr("type"));
						element.attr("class", inputElm.attr("class"));
						element.attr("id", inputElm.attr("id"));
						//n.appendContent(element);
				    }else if(type.equalsIgnoreCase("radio") || type.equalsIgnoreCase("checkbox")){
				    	String listValue = inputElm.attr("value");
						if(listValue != null && listValue != ""){
							listValue = listValue.replace("{", " ");
							listValue = listValue.replace("}", " ");
							String[] list = listValue.split(",");
							for(int j=list.length-1;j>=0;j--){
								String val = list[j].trim();
								String[] key = val.split("=");
								org.jsoup.nodes.Element element = n.appendElement("input");
								element.attr("name", inputElm.attr("name"));
								element.attr("value", key[0]);
								element.attr("type", inputElm.attr("type"));
								element.attr("class", inputElm.attr("class"));
								element.attr("id", inputElm.attr("id")+(j+1));
								element.appendText(key[1]);
								//n.appendContent(element);
							}
							
						}
				    }
					
//					n.attr("value", inputElm.attr("value"));
//					n.setTextContent(inputElm.attr("value"));

				}else{
					//TODO: We need to insert in custom fields
				}
			}
			
			nl = xmlelmNode.select("fields > field > customfield");//(List) xp.evaluate("fields/field/customfield", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.size(); i++) {
				Element inputElm = (Element) nl.get(i);
				String htmlid = inputElm.attr("forid");
				org.jsoup.nodes.Element n =   dochtml.getElementById(htmlid);//.selectSingleNode("//*[@id=\""+htmlid+"\"]");//(Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null){
//					List textnodenl = inputElm.select("text");
					Element textelement = inputElm.getElementsByTag("text").first();
//					if(textnodenl != null){
//						textelement = (Element) textnodenl.get(0);
//					}
					//CDATASection cdata = dochtml.createCDATASection(textelement.text());
					//element.appendContent(cdata);
					//n.appendText(textelement.get);
					List l = textelement.childNodes();
//					logger.debug("TO Remove"+(String)textelement.getData());
					n.append((String)textelement.text());
					//appendXmlFragment(dbuild,n, (String)textelement.getData());
					
				}else{
					//TODO: We need to insert in custom fields
				}
				
			}
			
			nl = xmlelmNode.select("fields > field > display");//(List) xp.evaluate("fields/field/display", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.size(); i++) {
				Element inputElm = (Element) nl.get(i);
				String htmlid = inputElm.attr("forid");
				org.jsoup.nodes.Element n =  dochtml.getElementById(htmlid);//.selectSingleNode("//*[@id=\""+htmlid+"\"]");//(Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null){
					n.appendText(inputElm.attr("value"));
				}else{
					//TODO: We need to insert in custom fields
				}
				
			}
			
			nl =xmlelmNode.select("fields > field > select");// (List) xp.evaluate("fields/field/select", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.size(); i++) {
				Element inputElm = (Element) nl.get(i);
				String htmlid = inputElm.attr("forid");
				org.jsoup.nodes.Element n =   dochtml.getElementById(htmlid);// dochtml.selectSingleNode("//*[@id=\""+htmlid"\"]");//(Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null){
					//List textnodenl = inputElm.select("text");
					Element textelement = inputElm.getElementsByTag("text").first();
//					if(textnodenl != null){
//						textelement = (Element) textnodenl.get(0);
//					}
//					CDATASection cdata = dochtml.createCDATASection(textelement.text());
//					element.appendContent(cdata);
//					logger.debug("To remove:"+textelement.text());
					n.append(textelement.text());
					//appendXmlFragment(dbuild,n,textelement.text());
					String listValue = inputElm.attr("value");
					
					if(listValue != null && listValue != ""){
						listValue = listValue.replace("{", " ");
						listValue = listValue.replace("}", " ");
						String[] list = listValue.split(",");
//						List List = dochtml.select("select");
//						Node node = List.get(0);
//						logger.debug("To remove:"+"//select[@id=\""+htmlid+"\"]");
						org.jsoup.nodes.Node node = dochtml.getElementById(htmlid);//.selectSingleNode("//select[@id=\""+htmlid+"\"]");
						for(String val:list){
							val = val.trim();
							String[] key = val.split("=");
							org.jsoup.nodes.Element element =  ((org.jsoup.nodes.Element)node).appendElement("option");
							element.attr("value", key[0]);
							element.text(key[1]);
//							Element nodelm = (Element) node;
							//nodelm.appendContent(element);
						}
					}else{ //if hard coded values are not there then look for filling up from action context
						ValueStack stack = ActionContext.getContext().getValueStack();
						Map<String,String>opts = (Map<String, String>) stack.findValue(htmlid);
						if(opts != null){
//							List list = dochtml.select("select");
//							Node node = list.get(0);
							org.jsoup.nodes.Node node = dochtml.getElementById(htmlid);//.selectSingleNode("//select[@id=\""+htmlid+"\"]");
							for (Entry<String, String> option : opts.entrySet()) {
								org.jsoup.nodes.Element element = ((org.jsoup.nodes.Element) node).appendElement("option");
								element.attr("value", option.getKey());
								element.text(option.getValue());
								Element nodelm = (Element) node;
								//nodelm.appendContent(element);
							}
						}
					}
				}else{
					//TODO: We need to insert in custom fields
				}
				
			}
			
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
			///multiple scriptincludes and scripts tags
			   List<Node> scriptsnl = xmlelmNode.select("scripts").first().childNodes();
			
			for (int i = 0;i < scriptsnl.size(); i++) {
				Node scriptnode =   scriptsnl.get(i);
				logger.debug("To remove CDATA section matching");
				if("text".equals(scriptnode.nodeName())){
					logger.debug("To remove: scripts:"+ scriptnode.toString());
					headNode.append( ((Element) scriptnode).text());
					//appendXmlFragment(dbuild,headNode,scriptnode.text());
				}
				if((scriptnode instanceof Element) && "scriptinclude".equals(scriptnode.nodeName())){
					String[] includeScripts = ((Element) scriptnode).text().split(",");
					for (String val : includeScripts) {
						org.jsoup.nodes.Element e = headNode.appendElement("script");
						e.attr("src", val);
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
			//////
			  List<Node> stylenl = xmlelmNode.select("stylesheets").first().childNodes();		
			for (int i = 0; i<stylenl.size(); i++) {
				Node scriptnode =   (Node) stylenl.get(i);
//				logger.debug("Adding styles"+scriptnode.text());
				if("text".equals(scriptnode.nodeName() )){
					headNode.append( ((Element) scriptnode).text());
					//appendXmlFragment(dbuild,headNode,scriptnode.text());
				}
				if((scriptnode instanceof Element) && "styleinclude".equals(scriptnode.nodeName())){
					String[] includeScripts = ((Element) scriptnode).text().split(",");
					for (String val : includeScripts) {
						org.jsoup.nodes.Element e = headNode.appendElement("link");
						e.attr("href", val);
						e.attr("rel", "stylesheet");
						e.attr("type", "text/css");
						//headNode.appendContent(e);
						headNode.appendText("\n");
					}
				}
			}
			///multiple styleincludes and stylesheet tags
			
			nl = xmlelmNode.select("fields > field > div");// xp.evaluate("fields/field/div", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.size(); i++) {
				Element inputElm = (Element) nl.get(i);
				String htmlid = inputElm.attr("forid");
				org.jsoup.nodes.Element n =     dochtml.getElementById(htmlid);//.selectSingleNode("//*[@id=\""+htmlid+"\"]");//(Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null){
					n.text(inputElm.attr("value"));
				}else{
					//TODO: We need to insert in custom fields
					org.jsoup.nodes.Element body = dochtml.getElementsByTag("body").first();
					org.jsoup.nodes.Element div = dochtml.createElement("div");
					logger.debug("To Remove"+inputElm.attr("id"));
					div.attr("id",inputElm.attr("id"));
     				body.prependChild(div);
					
					//					org.w3c.dom.Element e = ((org.w3c.dom.Document)dochtml).createElement("div");
//					e.setAttribute("id", inputElm.attr("id"));
//					org.jsoup.nodes.Element body = (org.jsoup.nodes.Element) dochtml.selectSingleNode("//body");
//					Node xpathnode = (Node) inputElm.select("xpath").get(0);
//					if(xpathnode.text().length() >0 )
//						body = (Element) dochtml.selectSingleNode("/html/body");//(Element) xp.evaluate("/html/body", dochtml, XPathConstants.NODE);
//					((org.w3c.dom.Node) body).insertBefore((org.w3c.dom.Element)e, (org.w3c.dom.Element)body.elements().get(0));
					 
				}
				
			}
			
			String globaljs = "";
			//compositefield for DataElements
			nl = xmlelmNode.select("fields > field > compositefield");//(List) xp.evaluate("fields/field/compositefield", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.size(); i++) {
				Element compositefield = (Element) nl.get(i);
				Element datafield = (Element) compositefield.select("datafield").get(0);
				String dfid=datafield.attr("id");
				String dfforid=datafield.attr("forid");
				String dfname=datafield.attr("name");
				String dfvalue=datafield.attr("value");
				String dftype=datafield.attr("type");
				if(dfvalue.length() == 0){
					dfvalue="{}";
				}
				JSONObject dfjson = new JSONObject(dfvalue);
				List displayfield = compositefield.select("displayfield");
				org.jsoup.nodes.Element datafldhtm = dochtml.createElement("input");
				datafldhtm.attr("type", dftype);
				datafldhtm.attr("id", dfid);
				datafldhtm.attr("name", dfname);
				datafldhtm.attr("value", dfvalue);
				org.jsoup.nodes.Element dfnode =   (org.jsoup.nodes.Element)dochtml.getElementById(dfforid);//.selectSingleNode("//*[@id=\""+dfforid+"\"]");// xp.evaluate("//*[@id=\""+dfforid+"\"]", dochtml, XPathConstants.NODE);
				boolean dfforidfound = false;
				if(dfforid == null){
					dfforidfound = true;
					dfnode.appendChild(datafldhtm);
				}
				for (int j = 0; j < displayfield.size(); j++) {
					Element dispelmxml = (Element)displayfield.get(j);
					String dename = dispelmxml.attr("name");
					String deforid = dispelmxml.attr("forid");
					String detype = dispelmxml.attr("type");
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
			//Append all validations
			nl =xmlelmNode.select("validation");// (List) xp.evaluate("//validation", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.size(); i++) {
				String validation = ((Element) nl.get(i)).text();
				
				if(validation != null && validation.length() > 1){
					globaljs += validation +"\n";
				}
			}
			//JSON rule begin
			nl = xmlelmNode.select("rule");//(List) xp.evaluate("//rule", xmlelmNode, XPathConstants.NODESET);
			JSONObject rulejson = new JSONObject("{rules:{},messages:{}}");
			for (int i = 0; i < nl.size(); i++) {
				Element ruleElm = (Element) nl.get(i);
				String ruletext = ruleElm.text();
				logger.debug("Rule="+ruletext);
				if(ruletext != null && ruletext.length() > 0){
					JSONArray jar = new JSONArray(ruleElm.text());
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
			rulejson.put("errorElement", "label");
			rulejson.put("errorLabelContainer", "#alertmessage");
			globaljs +="var rule="+rulejson.toString(3)+";\n";
			String strrule = "<script>"+globaljs+"</script>";
			headNode.append(strrule);
			//appendXmlFragment(dbuild, headNode, strrule);
			//JSON rule ends
			
			
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
//			xmlString = result.getWriter().toString(); 
//			xmlString = xmlString.replaceFirst("(\\<\\?xml[\\d\\D]*\\?\\>)","");
			templateprocessed = true;
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {			
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}catch(Exception e){
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
	   HTMLProcessorJsoupFullImpl htmp = new HTMLProcessorJsoupFullImpl();
	   String processedhtml =  htmp.process(htmp.fileReadAll("F:/eclipse/workspace/charts/FEtranslator1/src/actionclass/sampleoutput.xml"), null);
	   System.out.println(processedhtml);
}
}
