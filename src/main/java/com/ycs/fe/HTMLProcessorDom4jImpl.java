package com.ycs.fe;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.dom.DOMDocumentFactory;
import org.dom4j.io.HTMLWriter;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;

public class HTMLProcessorDom4jImpl extends HTMLProcessor {

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
			org.dom4j.Document docXML = reader2.read(strreader);
			logger.debug("HTMLProcessor: after d4j parsing");
			//Document docXML = (org.dom4j.dom.DOMDocument)d4jdoc;
			//Document docXML = dbuild.parse(reader);
			logger.debug("HTMLProcessor: after input parsing");
			Element xmlelmNode = docXML.getRootElement();
			
			String pathhtml = null;
			
			if(xmlelmNode.selectNodes("//htmltempalte").size() > 0){
				pathhtml = xmlelmNode.selectSingleNode("//htmltempalte").getText().trim();
				logger.debug("htmltemplate:"+pathhtml);
			}else{
				logger.debug("HTMLProcessor: no template found in "+inputXML.substring(0,20)+"..." ); 
				templateprocessed= false;
				return "HTMLProcessor: no template found in "+inputXML.substring(0,20)+"...";
			}
			XPath  xp = XPathFactory.newInstance().newXPath(); 
		 
		

			
			logger.debug("HTMLProcessor: HTML TemplatePath="+ServletActionContext.getServletContext().getRealPath("/"+pathhtml));
			pathhtml = ServletActionContext.getServletContext().getRealPath("/"+pathhtml);
			//logger.debug("HTMLProcessor: HTML TemplatePath="+ServletActionContext.getServletContext().getRealPath("/"+pathhtml));
//			pathhtml = "C:/Eclipse/workspace1/FEtranslator1/WebContent/pages/logintpl.xml";//ServletActionContext.getServletContext().getRealPath("/"+pathhtml);
			if(new File(pathhtml).exists())logger.debug("The html exists");
			FileInputStream fin = new FileInputStream(new File(pathhtml));
			//InputStream is = loader.getResourceAsStream("log4j.properties");
			logger.debug("HTMLProcessor: before html parsing");
			Document dochtml = reader2.read(fin);
			logger.debug("HTMLProcessor: after html parsing");
			logger.debug("HTMLProcessor: dochtml num childs:"+dochtml.nodeCount());
			
			List nl =  xmlelmNode.selectNodes("//fields/field/input");// xp.evaluate("//fields/field/input", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.size(); i++) {
				Element inputElm = (Element) nl.get(i);
//			    logger.debug(" .. found input type = ..");
			    String type = inputElm.attributeValue("type");
			    String htmlid = inputElm.attributeValue("forid");
				Element n = (Element) dochtml.selectSingleNode("//*[@id=\""+htmlid+"\"]");//(Node) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null){
					if(type.equalsIgnoreCase("text") || type.equalsIgnoreCase("password")){
						Element element = n.addElement("input");
						element.addAttribute("name", inputElm.attributeValue("name"));
						element.addAttribute("value", inputElm.attributeValue("value"));
						element.addAttribute("type", inputElm.attributeValue("type"));
						element.addAttribute("class", inputElm.attributeValue("class"));
						element.addAttribute("id", inputElm.attributeValue("id"));
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
								Element element = n.addElement("input");
								element.addAttribute("name", inputElm.attributeValue("name"));
								element.addAttribute("value", key[0]);
								element.addAttribute("type", inputElm.attributeValue("type"));
								element.addAttribute("class", inputElm.attributeValue("class"));
								element.addAttribute("id", inputElm.attributeValue("id")+(j+1));
								element.setText(key[1]);
								//n.appendContent(element);
							}
							
						}
				    }
					
//					n.addAttribute("value", inputElm.attributeValue("value"));
//					n.setTextContent(inputElm.attributeValue("value"));

				}else{
					//TODO: We need to insert in custom fields
				}
			}
			
			nl = xmlelmNode.selectNodes("//fields/field/customfield");//(List) xp.evaluate("//fields/field/customfield", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.size(); i++) {
				Element inputElm = (Element) nl.get(i);
				String htmlid = inputElm.attributeValue("forid");
				Element n = (Element) dochtml.selectSingleNode("//*[@id=\""+htmlid+"\"]");//(Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null){
//					List textnodenl = inputElm.selectNodes("text");
					Element textelement = inputElm.element("text");
//					if(textnodenl != null){
//						textelement = (Element) textnodenl.get(0);
//					}
					//CDATASection cdata = dochtml.createCDATASection(textelement.getText());
					//element.appendContent(cdata);
					//n.addText(textelement.get);
					List l = textelement.elements();
//					logger.debug("TO Remove"+(String)textelement.getData());
					appendXmlFragment(dbuild,n, (String)textelement.getData());
					
				}else{
					//TODO: We need to insert in custom fields
				}
				
			}
			
			nl = xmlelmNode.selectNodes("//fields/field/display");//(List) xp.evaluate("//fields/field/display", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.size(); i++) {
				Element inputElm = (Element) nl.get(i);
				String htmlid = inputElm.attributeValue("forid");
				Element n = (Element) dochtml.selectSingleNode("//*[@id=\""+htmlid+"\"]");//(Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null){
					n.setText(inputElm.attributeValue("value"));
				}else{
					//TODO: We need to insert in custom fields
				}
				
			}
			
			nl =xmlelmNode.selectNodes("//fields/field/select");// (List) xp.evaluate("//fields/field/select", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.size(); i++) {
				Element inputElm = (Element) nl.get(i);
				String htmlid = inputElm.attributeValue("forid");
				String id = inputElm.attributeValue("id");
				Element n = (Element) dochtml.selectSingleNode("//*[@id=\""+htmlid+"\"]");//(Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null){
					//List textnodenl = inputElm.selectNodes("text");
					Element textelement = inputElm.element("text");
//					if(textnodenl != null){
//						textelement = (Element) textnodenl.get(0);
//					}
//					CDATASection cdata = dochtml.createCDATASection(textelement.getText());
//					element.appendContent(cdata);
					logger.debug("To remove:"+textelement.getText());
					appendXmlFragment(dbuild,n,textelement.getText());
					String listValue = inputElm.attributeValue("value");
					
					if(listValue != null && listValue != ""){
						listValue = listValue.replace("{", " ");
						listValue = listValue.replace("}", " ");
						String[] list = listValue.split(",");
//						List List = dochtml.selectNodes("select");
//						Node node = List.get(0);
//						logger.debug("To remove:"+"//select[@id=\""+htmlid+"\"]");
						Node node = dochtml.selectSingleNode("//select[@id=\""+id+"\"]");
						for(String val:list){
							val = val.trim();
							String[] key = val.split("=");
							Element element = ((Branch) node).addElement("option");
							element.addAttribute("value", key[0]);
							element.setText(key[1]);
//							Element nodelm = (Element) node;
							//nodelm.appendContent(element);
						}
					}else{ //if hard coded values are not there then look for filling up from action context
						ValueStack stack = ActionContext.getContext().getValueStack();
						Map<String,String>opts = (Map<String, String>) stack.findValue(htmlid);
						if(opts != null){
//							List list = dochtml.selectNodes("select");
//							Node node = list.get(0);
							Node node = dochtml.selectSingleNode("//select[@id=\""+id+"\"]");
							for (Entry<String, String> option : opts.entrySet()) {
								Element element = ((Branch) node).addElement("option");
								element.addAttribute("value", option.getKey());
								element.setText(option.getValue());
								Element nodelm = (Element) node;
								//nodelm.appendContent(element);
							}
						}
					}
				}else{
					//TODO: We need to insert in custom fields
				}
				
			}
			
			List  headnl = dochtml.selectNodes("/html/head");
			if(headnl.size() < 1){
				Element htmltop = (Element) dochtml.selectSingleNode("/html");
				Element elm = htmltop.addElement("head");
				//TODO: append head
			}
			
			
				
			Element headNode = (Element) dochtml.selectSingleNode("/html/head");
			/*String scriptinclude = (String)xp.evaluate("//scriptinclude/text()", xmlelmNode, XPathConstants.STRING);
			if (scriptinclude != null) {
				String[] includeScripts = scriptinclude.split(",");
				for (String val : includeScripts) {
					Element e = document.getRootElement().addElement("script");
					e.addAttribute("src", val);
					e.addAttribute("language", "JavaScript");
					e.addAttribute("type", "text/javascript");
					headNode.appendContent(e);
					headNode.appendContent(dochtml.createTextNode("\n"));
				}
			}
			
			String scripts = (String) xp.evaluate("//scripts/text()", xmlelmNode, XPathConstants.STRING);
			 
			if(scripts.length() > 1){
				appendXmlFragment(dbuild,headNode,scripts);
			}*/
			///multiple scriptincludes and scripts tags
			Iterator scriptsnl =   ((Element)xmlelmNode.selectSingleNode("//scripts")).nodeIterator();
			
			for (int i = 0;  scriptsnl.hasNext(); i++) {
				Node scriptnode = (Node) scriptsnl.next();
				if(scriptnode.getNodeType() == Node.CDATA_SECTION_NODE){
					appendXmlFragment(dbuild,headNode,scriptnode.getText());
				}
				if(scriptnode.getNodeType() == Node.ELEMENT_NODE && "scriptinclude".equals(scriptnode.getName())){
					String[] includeScripts = scriptnode.getText().split(",");
					for (String val : includeScripts) {
						Element e = headNode.addElement("script");
						e.addAttribute("src", val);
						e.addAttribute("language", "JavaScript");
						e.addAttribute("type", "text/javascript");
						 // headNode.appendContent(e);
						headNode.addText("\n");
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
					e.addAttribute("href", val);
					e.addAttribute("rel", "stylesheet");
					e.addAttribute("type", "text/css");
					headNode.appendContent(e);
				}
			}*/
			//////
			Iterator stylenl =   ((Element) xmlelmNode.selectSingleNode("//stylesheets")).nodeIterator();		
			for (int i = 0; stylenl.hasNext(); i++) {
				Node scriptnode = (Node) stylenl.next();
//				logger.debug("Adding styles"+scriptnode.getText());
				if(scriptnode.getNodeType() == Node.CDATA_SECTION_NODE){
					appendXmlFragment(dbuild,headNode,scriptnode.getText());
				}
				if(scriptnode.getNodeType() == Node.ELEMENT_NODE && "styleinclude".equals(scriptnode.getName())){
					String[] includeScripts = scriptnode.getText().split(",");
					for (String val : includeScripts) {
						Element e = headNode.addElement("link");
						e.addAttribute("href", val);
						e.addAttribute("rel", "stylesheet");
						e.addAttribute("type", "text/css");
						//headNode.appendContent(e);
						headNode.addText("\n");
					}
				}
			}
			///multiple styleincludes and stylesheet tags
			
			nl = xmlelmNode.selectNodes("//fields/field/div");// xp.evaluate("//fields/field/div", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.size(); i++) {
				Element inputElm = (Element) nl.get(i);
				String htmlid = inputElm.attributeValue("forid");
				Element n =   (Element) dochtml.selectSingleNode("//*[@id=\""+htmlid+"\"]");//(Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null){
					n.setText(inputElm.attributeValue("value"));
				}else{
					//TODO: We need to insert in custom fields
					org.w3c.dom.Element e = ((org.w3c.dom.Document)dochtml).createElement("div");
					e.setAttribute("id", inputElm.attributeValue("id"));
					Element body = (Element) dochtml.selectSingleNode("//body");
					Node xpathnode = (Node) inputElm.selectNodes("xpath").get(0);
					if(xpathnode.getText().length() >0 )
						body = (Element) dochtml.selectSingleNode("/html/body");//(Element) xp.evaluate("/html/body", dochtml, XPathConstants.NODE);
					((org.w3c.dom.Node) body).insertBefore((org.w3c.dom.Element)e, (org.w3c.dom.Element)body.elements().get(0));
					 
				}
				
			}
			
			String globaljs = "";
			//compositefield for DataElements
			nl = xmlelmNode.selectNodes("//fields/field/compositefield");//(List) xp.evaluate("//fields/field/compositefield", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.size(); i++) {
				Element compositefield = (Element) nl.get(i);
				Element datafield = (Element) compositefield.selectNodes("datafield").get(0);
				String dfid=datafield.attributeValue("id");
				String dfforid=datafield.attributeValue("forid");
				String dfname=datafield.attributeValue("name");
				String dfvalue=datafield.attributeValue("value");
				String dftype=datafield.attributeValue("type");
				if(dfvalue.length() == 0){
					dfvalue="{}";
				}
				HashMap<String,String> dfjson = new Gson().fromJson(dfvalue, HashMap.class);
				List displayfield = compositefield.selectNodes("displayfield");
				Element datafldhtm = factory.createElement("input");
				datafldhtm.addAttribute("type", dftype);
				datafldhtm.addAttribute("id", dfid);
				datafldhtm.addAttribute("name", dfname);
				datafldhtm.addAttribute("value", dfvalue);
				Element dfnode =   (Element)dochtml.selectSingleNode("//*[@id=\""+dfforid+"\"]");// xp.evaluate("//*[@id=\""+dfforid+"\"]", dochtml, XPathConstants.NODE);
				boolean dfforidfound = false;
				if(dfforid == null){
					dfforidfound = true;
					dfnode.add(datafldhtm);
				}
				for (int j = 0; j < displayfield.size(); j++) {
					Element dispelmxml = (Element)displayfield.get(j);
					String dename = dispelmxml.attributeValue("name");
					String deforid = dispelmxml.attributeValue("forid");
					String detype = dispelmxml.attributeValue("type");
					Element n = (Element)dochtml.selectSingleNode("//*[@id=\""+deforid+"\"]");// xp.evaluate("//*[@id=\""+deforid+"\"]", dochtml, XPathConstants.NODE);
					Element difldhtm = factory.createElement("input");
					difldhtm.addAttribute("type", detype);
					difldhtm.addAttribute("id", dename);
					difldhtm.addAttribute("name", dename);
					if(dfjson.containsKey(dename))
					difldhtm.addAttribute("value", dfjson.get(dename));
					difldhtm.addAttribute("onblur", "updateCompositeField(this,'#"+dfid+"')");
					if(dfforidfound == false && n!=null){
						dfforidfound = true; //appending to first display element
						n.add(datafldhtm);
					}
					if(n!=null)
					n.add(difldhtm);
				}
				
			}
			//compositefield for DataElements end
			//Append all validations
			nl =xmlelmNode.selectNodes("//validation");// (List) xp.evaluate("//validation", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.size(); i++) {
				String validation = ((Element) nl.get(i)).getText();
				
				if(validation != null && validation.length() > 1){
					globaljs += validation +"\n";
				}
			}
			//JSON rule begin
			nl = xmlelmNode.selectNodes("//rule");//(List) xp.evaluate("//rule", xmlelmNode, XPathConstants.NODESET);
			HashMap<String,Object> rulejson = new HashMap<String,Object>(); //"{rules:{},messages:{}}"
			rulejson.put("rules", new HashMap<String,Object>());
			rulejson.put("messages", new HashMap<String,Object>());
			for (int i = 0; i < nl.size(); i++) {
				Element ruleElm = (Element) nl.get(i);
				String ruletext = ruleElm.getText();
				logger.debug("Rule="+ruletext);
				if(ruletext != null && ruletext.length() > 0){
					//JSONArray jar = new JSONArray(ruleElm.getText());
					ArrayList<HashMap<String,Object>> jar = new Gson().fromJson(ruleElm.getText(), ArrayList.class);
					//JSONObject messageelmpart =  jobj.getJSONObject("messages");
					for (int j = 0; j < jar.size(); j++) {
						HashMap<String, Object> jobj = jar.get(j);
						String fieldname = (String) jobj.get("fieldname"); //string
						((HashMap<String,Object>) rulejson.get("rules")).put(fieldname, jobj.get("rules"));
						((HashMap<String,Object>) rulejson.get("messages")).put(fieldname, jobj.get("messages"));
					} 
					 
				}
			}
			//default rule properties ,errorElement:\"div\",errorLabelContainer:\"#alertmessage\"
			rulejson.put("errorElement", "label");
			rulejson.put("errorLabelContainer", "#alertmessage");
			globaljs +="var rule="+new Gson().toJson(rulejson)+";\n";
			//JSON rule ends
			
			//savefieldids begin
			List<Node> panels = xmlelmNode.selectNodes("/root/panels/panel");
			for (Node panel : panels) {
				Node savefield = panel.selectSingleNode("savefieldids");
				String id = ((Element) panel).attributeValue("id");
				if(savefield != null ){
					globaljs +="var panel_"+id+" = [";
					String[] idlist = savefield.getText().split(",");
					for (int i = 0; i < idlist.length; i++) {
						globaljs +="\""+idlist[i]+"\",";
					}
					if(globaljs.charAt(globaljs.length()-1) == ',')globaljs = globaljs.substring(0,globaljs.length()-2);
					globaljs +="];\n";
					 
				}
			}
			//savefieldids end
			
			
			String strrule = "<script>"+globaljs+"</script>";
			appendXmlFragment(dbuild, headNode, strrule);
			//JSON rule ends
			
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			//initialize StreamResult with File object to save to file
//			StreamResult result = new StreamResult(new StringWriter());
//			DOMSource source = new DOMSource((org.dom4j.dom.DOMDocument)dochtml);
//			transformer.transform(source, result);
		
			OutputFormat format = OutputFormat.createPrettyPrint();
			 format.setExpandEmptyElements( true);
			 StringWriter strw  = new StringWriter();
			 HTMLWriter writer = new HTMLWriter(strw,format);
			 
			 writer.setEscapeText(false); 
			 writer.write(dochtml);
			 xmlString = strw.toString();
//			xmlString = result.getWriter().toString(); 
//			xmlString = xmlString.replaceFirst("(\\<\\?xml[\\d\\D]*\\?\\>)","");
			templateprocessed = true;
			
		} catch (SAXException e) {
			e.printStackTrace();
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
	   HTMLProcessorDom4jImpl htmp = new HTMLProcessorDom4jImpl();
	   String processedhtml =  htmp.process(htmp.fileReadAll("C:/Eclipse/workspace1/FEtranslator1/src/actionclass/sampleoutput.xml"), null);
	   System.out.println(processedhtml);
}
}
