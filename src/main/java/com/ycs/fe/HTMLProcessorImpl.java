package com.ycs.fe;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.dom.DOMDocumentFactory;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
 

 

/**
 * HTMLProcessor merges the XML (populated with data) and HMTL template together and writes to response output stream. 
 * This class is called twice once in process and anoother populateValueStack, hence it is not thread safe.
 * @author Samarjit
 * possible XSD starting point any valid element {@link http://www.stylusstudio.com/w3c/schema0/any.htm } for maximum flexibility
 */
public class HTMLProcessorImpl extends HTMLProcessor   {
	private Logger logger = Logger.getLogger(this.getClass());
	
	private boolean templateprocessed = false;
	
 
	@Override
	public boolean getLastResult(){
		return templateprocessed;
	}
	
 
	@Override
	public String process(String inputXML, ActionInvocation invocation){
		ClassLoader loader = this.getClass().getClassLoader();	
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbuild;
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
			org.dom4j.Document d4jdoc = reader2.read(strreader);
			logger.debug("HTMLProcessor: after d4j parsing");
			//Document docXML = (org.dom4j.dom.DOMDocument)d4jdoc;
			Document docXML = dbuild.parse(reader);
			logger.debug("HTMLProcessor: after input parsing");
			Element xmlelmNode = docXML.getDocumentElement();
			
			String pathhtml = null;
			if(xmlelmNode.getElementsByTagName("htmltempalte").getLength() > 0){
				pathhtml = xmlelmNode.getElementsByTagName("htmltempalte").item(0).getNodeValue().trim();
			}else{
				logger.debug("HTMLProcessor: no template found in "+inputXML.substring(0,20)+"..." ); 
				templateprocessed= false;
				return "HTMLProcessor: no template found in "+inputXML.substring(0,20)+"...";
			}
			XPath  xp = XPathFactory.newInstance().newXPath(); 
		 
		

			
			logger.debug("HTMLProcessor: HTML TemplatePath="+ServletActionContext.getServletContext().getRealPath("/"+pathhtml));
			pathhtml = ServletActionContext.getServletContext().getRealPath("/"+pathhtml);
			if(new File(pathhtml).exists())logger.debug("The html exists");
			FileInputStream fin = new FileInputStream(new File(pathhtml));
			//InputStream is = loader.getResourceAsStream("log4j.properties");
			logger.debug("HTMLProcessor: before html parsing");
			Document dochtml = dbuild.parse(fin);
			logger.debug("HTMLProcessor: after html parsing");
			logger.debug("HTMLProcessor: dochtml num childs:"+dochtml.getChildNodes().getLength());
			
			NodeList nl = (NodeList) xp.evaluate("//fields/field/input", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				Element inputElm = (Element) nl.item(i);
//			    logger.debug(" .. found input type = ..");
			    String type = inputElm.getAttribute("type");
			    String htmlid = inputElm.getAttribute("forid");
				Node n = (Node) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null){
					if(type.equalsIgnoreCase("text") || type.equalsIgnoreCase("password")){
						Element element = dochtml.createElement("input");
						element.setAttribute("name", inputElm.getAttribute("name"));
						element.setAttribute("value", inputElm.getAttribute("value"));
						element.setAttribute("type", inputElm.getAttribute("type"));
						element.setAttribute("class", inputElm.getAttribute("class"));
						element.setAttribute("id", inputElm.getAttribute("id"));
						n.appendChild(element);
				    }else if(type.equalsIgnoreCase("radio") || type.equalsIgnoreCase("checkbox")){
				    	String listValue = inputElm.getAttribute("value");
						if(listValue != null && listValue != ""){
							listValue = listValue.replace("{", " ");
							listValue = listValue.replace("}", " ");
							String[] list = listValue.split(",");
							for(int j=list.length-1;j>=0;j--){
								String val = list[j].trim();
								String[] key = val.split("=");
								Element element = dochtml.createElement("input");
								element.setAttribute("name", inputElm.getAttribute("name"));
								element.setAttribute("value", key[0]);
								element.setAttribute("type", inputElm.getAttribute("type"));
								element.setAttribute("class", inputElm.getAttribute("class"));
								element.setAttribute("id", inputElm.getAttribute("id")+(j+1));
								element.setNodeValue(key[1]);
								n.appendChild(element);
							}
							
						}
				    }
					
//					n.setAttribute("value", inputElm.getAttribute("value"));
//					n.setNodeValue(inputElm.getAttribute("value"));

				}else{
					//TODO: We need to insert in custom fields
				}
			}
			
			nl = (NodeList) xp.evaluate("//fields/field/customfield", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				Element inputElm = (Element) nl.item(i);
				String htmlid = inputElm.getAttribute("forid");
				Element n = (Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null){
					NodeList textnodenl = inputElm.getElementsByTagName("text");
					Element textelement = null;
					if(textnodenl != null){
						textelement = (Element) textnodenl.item(0);
					}
					//CDATASection cdata = dochtml.createCDATASection(textelement.getNodeValue());
					//element.appendChild(cdata);
					appendXmlFragment(dbuild,n,textelement.getChildNodes());
					
				}else{
					//TODO: We need to insert in custom fields
				}
				
			}
			
			nl = (NodeList) xp.evaluate("//fields/field/display", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				Element inputElm = (Element) nl.item(i);
				String htmlid = inputElm.getAttribute("forid");
				Element n = (Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null){
					n.setNodeValue(inputElm.getAttribute("value"));
				}else{
					//TODO: We need to insert in custom fields
				}
				
			}
			
			nl = (NodeList) xp.evaluate("//fields/field/select", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				Element inputElm = (Element) nl.item(i);
				String htmlid = inputElm.getAttribute("forid");
				Element n = (Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null){
					NodeList textnodenl = inputElm.getElementsByTagName("text");
					Element textelement = null;
					if(textnodenl != null){
						textelement = (Element) textnodenl.item(0);
					}
//					CDATASection cdata = dochtml.createCDATASection(textelement.getNodeValue());
//					element.appendChild(cdata);
					appendXmlFragment(dbuild,n,textelement.getNodeValue());
					String listValue = inputElm.getAttribute("value");
					
					if(listValue != null && listValue != ""){
						listValue = listValue.replace("{", " ");
						listValue = listValue.replace("}", " ");
						String[] list = listValue.split(",");
						NodeList nodeList = dochtml.getElementsByTagName("select");
						Node node = nodeList.item(0);//? why only first select
						
						for(String val:list){
							val = val.trim();
							String[] key = val.split("=");
							Element element = dochtml.createElement("option");
							element.setAttribute("value", key[0]);
							element.setNodeValue(key[1]);
							node.appendChild(element);
						}
					}else{ //if hard coded values are not there then look for filling up from action context
						ValueStack stack = ActionContext.getContext().getValueStack();
						Map<String,String>opts = (Map<String, String>) stack.findValue(htmlid);
						if(opts != null){
							NodeList nodeList = dochtml.getElementsByTagName("select");
							Node node = nodeList.item(0);
							for (Entry<String, String> option : opts.entrySet()) {
								Element element = dochtml.createElement("option");
								element.setAttribute("value", option.getKey());
								element.setNodeValue(option.getValue());
								node.appendChild(element);
							}
						}
					}
				}else{
					//TODO: We need to insert in custom fields
				}
				
			}
			
			NodeList headnl = dochtml.getElementsByTagName("head");
			if(headnl.getLength() < 1){
				Element elm = dochtml.createElement("head");
				Node htmltop = dochtml.getElementsByTagName("html").item(0);
				htmltop.appendChild(elm);
				//TODO: append head
			}
			
			
				
			Node headNode = headnl.item(0);
			/*String scriptinclude = (String)xp.evaluate("//scriptinclude/text()", xmlelmNode, XPathConstants.STRING);
			if (scriptinclude != null) {
				String[] includeScripts = scriptinclude.split(",");
				for (String val : includeScripts) {
					Element e = dochtml.createElement("script");
					e.setAttribute("src", val);
					e.setAttribute("language", "JavaScript");
					e.setAttribute("type", "text/javascript");
					headNode.appendChild(e);
					headNode.appendChild(dochtml.createTextNode("\n"));
				}
			}
			
			String scripts = (String) xp.evaluate("//scripts/text()", xmlelmNode, XPathConstants.STRING);
			 
			if(scripts.length() > 1){
				appendXmlFragment(dbuild,headNode,scripts);
			}*/
			///multiple scriptincludes and scripts tags
			NodeList scriptsnl =   xmlelmNode.getElementsByTagName("scripts").item(0).getChildNodes();		
			for (int i = 0; i < scriptsnl.getLength(); i++) {
				Node scriptnode = scriptsnl.item(i);
//				logger.debug("Adding scipts"+scriptnode.getNodeValue());
				if(scriptnode.getNodeType() == Node.CDATA_SECTION_NODE){
					appendXmlFragment(dbuild,headNode,scriptnode.getNodeValue());
				}
				if(scriptnode.getNodeType() == Node.ELEMENT_NODE && "scriptinclude".equals(scriptnode.getNodeName())){
					String[] includeScripts = scriptnode.getNodeValue().split(",");
					for (String val : includeScripts) {
						Element e = dochtml.createElement("script");
						e.setAttribute("src", val);
						e.setAttribute("language", "JavaScript");
						e.setAttribute("type", "text/javascript");
						headNode.appendChild(e);
						headNode.appendChild(dochtml.createTextNode("\n"));
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
					Element e = dochtml.createElement("link");
					e.setAttribute("href", val);
					e.setAttribute("rel", "stylesheet");
					e.setAttribute("type", "text/css");
					headNode.appendChild(e);
				}
			}*/
			//////
			NodeList stylenl =   xmlelmNode.getElementsByTagName("stylesheets").item(0).getChildNodes();		
			for (int i = 0; i < stylenl.getLength(); i++) {
				Node scriptnode = stylenl.item(i);
//				logger.debug("Adding styles"+scriptnode.getNodeValue());
				if(scriptnode.getNodeType() == Node.CDATA_SECTION_NODE){
					appendXmlFragment(dbuild,headNode,scriptnode.getNodeValue());
				}
				if(scriptnode.getNodeType() == Node.ELEMENT_NODE && "styleinclude".equals(scriptnode.getNodeName())){
					String[] includeScripts = scriptnode.getNodeValue().split(",");
					for (String val : includeScripts) {
						Element e = dochtml.createElement("link");
						e.setAttribute("href", val);
						e.setAttribute("rel", "stylesheet");
						e.setAttribute("type", "text/css");
						headNode.appendChild(e);
						headNode.appendChild(dochtml.createTextNode("\n"));
					}
				}
			}
			///multiple styleincludes and stylesheet tags
			
			nl = (NodeList) xp.evaluate("//fields/field/div", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				Element inputElm = (Element) nl.item(i);
				String htmlid = inputElm.getAttribute("forid");
				Element n = (Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null){
					n.setNodeValue(inputElm.getAttribute("value"));
				}else{
					//TODO: We need to insert in custom fields
					Element e = dochtml.createElement("div");
					e.setAttribute("id", inputElm.getAttribute("id"));
					Element body = (Element) dochtml.getElementsByTagName("body").item(0);
					Node xpathnode = inputElm.getElementsByTagName("xpath").item(0);
					if(xpathnode.getNodeValue().length() >0 )
						body = (Element) xp.evaluate("/html/body", dochtml, XPathConstants.NODE);
					body.insertBefore(e, body.getFirstChild());
					 
				}
				
			}
			
			String globaljs = "";
			//compositefield for DataElements
			nl = (NodeList) xp.evaluate("//fields/field/compositefield", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				Element compositefield = (Element) nl.item(i);
				Element datafield = (Element) compositefield.getElementsByTagName("datafield").item(0);
				String dfid=datafield.getAttribute("id");
				String dfforid=datafield.getAttribute("forid");
				String dfname=datafield.getAttribute("name");
				String dfvalue=datafield.getAttribute("value");
				String dftype=datafield.getAttribute("type");
				if(dfvalue.length() == 0){
					dfvalue="{}";
				}
				HashMap<String,String> dfjson = new Gson().fromJson(dfvalue, HashMap.class);
				NodeList displayfield = compositefield.getElementsByTagName("displayfield");
				Element datafldhtm = dochtml.createElement("input");
				datafldhtm.setAttribute("type", dftype);
				datafldhtm.setAttribute("id", dfid);
				datafldhtm.setAttribute("name", dfname);
				datafldhtm.setAttribute("value", dfvalue);
				Element dfnode = (Element) xp.evaluate("//*[@id=\""+dfforid+"\"]", dochtml, XPathConstants.NODE);
				boolean dfforidfound = false;
				if(dfforid == null){
					dfforidfound = true;
					dfnode.appendChild(datafldhtm);
				}
				for (int j = 0; j < displayfield.getLength(); j++) {
					Element dispelmxml = (Element)displayfield.item(j);
					String dename = dispelmxml.getAttribute("name");
					String deforid = dispelmxml.getAttribute("forid");
					String detype = dispelmxml.getAttribute("type");
					Element n = (Element) xp.evaluate("//*[@id=\""+deforid+"\"]", dochtml, XPathConstants.NODE);
					Element difldhtm = dochtml.createElement("input");
					difldhtm.setAttribute("type", detype);
					difldhtm.setAttribute("id", dename);
					difldhtm.setAttribute("name", dename);
					if(dfjson.containsKey(dename))
					difldhtm.setAttribute("value", dfjson.get(dename));
					difldhtm.setAttribute("onblur", "updateCompositeField(this,'#"+dfid+"')");
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
			nl = (NodeList) xp.evaluate("//validation", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				String validation = nl.item(i).getNodeValue();
				
				if(validation != null && validation.length() > 1){
					globaljs += validation +"\n";
				}
			}
			//JSON rule begin
			nl = (NodeList) xp.evaluate("//rule", xmlelmNode, XPathConstants.NODESET);
			HashMap<String,Object> rulejson = new HashMap<String,Object>(); //"{rules:{},messages:{}}"
			rulejson.put("rules", new HashMap<String,Object>());
			rulejson.put("messages", new HashMap<String,Object>());
			for (int i = 0; i < nl.getLength(); i++) {
				Element ruleElm = (Element) nl.item(i);
				String ruletext = ruleElm.getNodeValue();
				logger.debug("Rule="+ruletext);
				if(ruletext != null && ruletext.length() > 0){
					ArrayList<HashMap<String,Object>> jar = new Gson().fromJson(ruleElm.getNodeValue(), ArrayList.class);
					//JSONArray jar = new JSONArray(ruleElm.getNodeValue());
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
			String strrule = "<script>"+globaljs+"</script>";
			appendXmlFragment(dbuild, headNode, strrule);
			//JSON rule ends
			
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			//initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(dochtml);

			transformer.transform(source, result);

			
			xmlString = result.getWriter().toString();
			templateprocessed = true;
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
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
	/* (non-Javadoc)
	 * @see com.ycs.fe.HTMLProcessor#appendXmlFragment(javax.xml.parsers.DocumentBuilder, org.w3c.dom.Node, org.w3c.dom.NodeList)
	 */
	@Override
	public   void appendXmlFragment(DocumentBuilder docBuilder, Node parent, NodeList fragment) throws IOException, SAXException {
//		logger.debug("still coming here");
		Document doc = parent.getOwnerDocument();
//		Node fragmentNode = docBuilder.parse(new InputSource(new StringReader("<root>"+fragment+"</root>"))).getDocumentElement();
		NodeList nl = fragment;
		if(fragment.item(0).getNodeType() == Node.CDATA_SECTION_NODE){
			appendXmlFragment( docBuilder,  parent,  fragment.item(0).getNodeValue());
			return;
		}
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);			
				Node node = doc.importNode(n, true);
				parent.appendChild(node);
		}
	}
	/* (non-Javadoc)
	 * @see com.ycs.fe.HTMLProcessor#appendXmlFragment(javax.xml.parsers.DocumentBuilder, org.w3c.dom.Node, java.lang.String)
	 */
	@Override
	public   void appendXmlFragment(DocumentBuilder docBuilder, Node parent, String fragment) throws IOException, SAXException {
//		logger.debug("still coming here");
		Document doc = parent.getOwnerDocument();
		Node fragmentNode = docBuilder.parse(new InputSource(new StringReader("<root>"+fragment+"</root>"))).getDocumentElement();
		NodeList nl = fragmentNode.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);			
				Node node = doc.importNode(n, true);
				parent.appendChild(node);
		}
	}
	
	public static void main(String[] args) throws Exception {
		/* Create and adjust the configuration */
		System.out.println("Running template engine");
		Configuration cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File("map/loginmap.xml"));
		// cfg.setObjectWrapper(new DefaultObjectWrapper());
		Map root = new HashMap();
		/* ------------------------------------------------------------------- */
		/* You usually do these for many times in the application life-cycle: */

		/* Get or create a template */
		TemplateEngine temp = cfg.getTemplate("WebContent/pages/logintpl.html");

		Writer out = new OutputStreamWriter(System.out);
		temp.process(root, out);
		out.flush();
		 HTMLProcessorImpl htmp = new HTMLProcessorImpl();
		 htmp.process(htmp.fileReadAll("F:/eclipse/workspace/charts/FEtranslator1/src/actionclass/sampleoutput.xml"), null);
	}
}
