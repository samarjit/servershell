package com.ycs.fe;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
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
import javax.xml.xpath.XPathFactory;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.ParentNode;
import nu.xom.ParsingException;
import nu.xom.Text;
import nu.xom.ValidityException;
import nu.xom.converters.DOMConverter;

import org.apache.log4j.Logger;
import org.dom4j.dom.DOMDocumentFactory;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.xml.sax.SAXException;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
 

 

/**
 * HTMLProcessor merges the XML (populated with data) and HMTL template together and writes to response output stream. 
 * This class is called twice once in process and anoother populateValueStack, hence it is not thread safe.
 * @author Samarjit
 * possible XSD starting point any valid element {@link http://www.stylusstudio.com/w3c/schema0/any.htm } for maximum flexibility
 */
public class HTMLProcessorXOMImpl extends HTMLProcessor   {
	private Logger logger = Logger.getLogger(this.getClass());
	
	private boolean templateprocessed = false;
	
 
	@Override
	public boolean getLastResult(){
		return templateprocessed;
	}
	
	public   void appendXmlFragment(DocumentBuilder docBuilder, Node parent, Elements elements) throws IOException, SAXException {
		for (int i = 0; i < elements.size(); i++) {
			Element elm = elements.get(i);
			((ParentNode) parent).appendChild(elm);
		}
	}
	public   void appendXmlFragment(DocumentBuilder docBuilder, Node parent, String fragmentinp) throws IOException, SAXException {
		try {
			String fragment ="<fakeroot>"+fragmentinp+"</fakeroot>";
//			logger.debug("Parsing fragment"+fragment);
			if(fragment == null || fragment.length() <1)return;
			Document newNodeDocument = new Builder().build(fragment, "");
			Nodes children = newNodeDocument.getRootElement().removeChildren();
			for (int i = 0; i < children.size(); i++) {
				((Element)parent).appendChild(children.get(i));
	        }
			//((Element)parent).appendChild(newNodeDocument.getRootElement().copy());
		} catch (ValidityException e) {
			e.printStackTrace();
		} catch (ParsingException e) {
			e.printStackTrace();
		}
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
			Document docXML = new Builder().build(inputXML,"");
			logger.debug("HTMLProcessor: after input parsing");
			Element xmlelmNode = docXML.getRootElement();
			
			String pathhtml = null;
			if(xmlelmNode.query("//htmltempalte").size() > 0){
				pathhtml = xmlelmNode.query("//htmltempalte").get(0).getValue().trim();
			}else{
				logger.debug("HTMLProcessor: no template found in "+inputXML.substring(0,20)+"..." ); 
				templateprocessed= false;
				return "HTMLProcessor: no template found in "+inputXML.substring(0,20)+"...";
			}
			XPath  xp = XPathFactory.newInstance().newXPath(); 
		 
		

			
			//logger.debug("HTMLProcessor: HTML TemplatePath="+ServletActionContext.getServletContext().getRealPath("/"+pathhtml));
			pathhtml = "C:/Eclipse/workspace1/FEtranslator1/WebContent/pages/logintpl.xml";//ServletActionContext.getServletContext().getRealPath("/"+pathhtml);
			if(new File(pathhtml).exists())logger.debug("The html exists");
			FileInputStream fin = new FileInputStream(new File(pathhtml));
			//InputStream is = loader.getResourceAsStream("log4j.properties");
			logger.debug("HTMLProcessor: before html parsing");
			Document dochtml = new Builder().build(fin);
			logger.debug("HTMLProcessor: after html parsing");
			logger.debug("HTMLProcessor: dochtml num childs:"+dochtml.getChildCount());
			
			Nodes nl = xmlelmNode.query("//fields/field/input");//(NodeList) xp.evaluate("//fields/field/input", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.size(); i++) {
				Element inputElm = (Element) nl.get(i);
//			    logger.debug(" .. found input type = ..");
			    String type = inputElm.getAttributeValue("type");
			    String htmlid = inputElm.getAttributeValue("forid");
				Node n = dochtml.query("//*[@id=\""+htmlid+"\"]").get(0);//  xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null){
					if(type.equalsIgnoreCase("text") || type.equalsIgnoreCase("password")){
						Element element = new Element("input");
						element.addAttribute(new Attribute("name", inputElm.getAttributeValue("name")));
						element.addAttribute(new Attribute("value", inputElm.getAttributeValue("value")));
						element.addAttribute(new Attribute("type", inputElm.getAttributeValue("type")));
						element.addAttribute(new Attribute("class", inputElm.getAttributeValue("class")));
						element.addAttribute(new Attribute("id", inputElm.getAttributeValue("id")));
						((ParentNode) n).appendChild(element);
				    }else if(type.equalsIgnoreCase("radio") || type.equalsIgnoreCase("checkbox")){
				    	String listValue = inputElm.getAttributeValue("value");
						if(listValue != null && listValue != ""){
							listValue = listValue.replace("{", " ");
							listValue = listValue.replace("}", " ");
							String[] list = listValue.split(",");
							for(int j=list.length-1;j>=0;j--){
								String val = list[j].trim();
								String[] key = val.split("=");
								Element element = new Element("input");
								element.addAttribute(new Attribute("name", inputElm.getAttributeValue("name")));
								element.addAttribute(new Attribute("value", key[0]));
								element.addAttribute(new Attribute("type", inputElm.getAttributeValue("type")));
								element.addAttribute(new Attribute("class", inputElm.getAttributeValue("class")));
								element.addAttribute(new Attribute("id", inputElm.getAttributeValue("id")+(j+1)));
								element.appendChild(key[1]);
								((ParentNode) n).appendChild(element);
							}
							
						}
				    }
					
//					n.addAttribute(new Attribute("value", inputElm.getAttributeValue("value")));
//					n.setTextContent(inputElm.getAttributeValue("value"));

				}else{
					//TODO: We need to insert in custom fields
				}
			}
			
			nl = xmlelmNode.query("//fields/field/customfield");//(NodeList) xp.evaluate("//fields/field/customfield", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.size(); i++) {
				Element inputElm = (Element) nl.get(i);
				String htmlid = inputElm.getAttributeValue("forid");
				Element n = (Element) dochtml.query("//*[@id=\""+htmlid+"\"]").get(0);// (Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null){
					Node textnodenl  = inputElm.getFirstChildElement("text");
					Element textelement = null;
					if(textnodenl != null){
						textelement = (Element) textnodenl;
					}
					//CDATASection cdata = dochtml.createCDATASection(textelement.getValue());
					//element.appendChild(cdata);
					appendXmlFragment(dbuild,n,textelement.getChildElements());
					
				}else{
					//TODO: We need to insert in custom fields
				}
				
			}
			
			nl = xmlelmNode.query("//fields/field/display") ;//(NodeList) xp.evaluate("//fields/field/display", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.size(); i++) {
				Element inputElm = (Element) nl.get(i);
				String htmlid = inputElm.getAttributeValue("forid");
				Element n =  (Element) dochtml.query("//*[@id=\""+htmlid+"\"]").get(0);//\(Element\) xp\.evaluate\("//*[@id=\""+htmlid+"\"]",(  dochtmll), XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null){
					n.appendChild(inputElm.getAttributeValue("value"));
				}else{
					//TODO: We need to insert in custom fields
				}
				
			}
			
			nl = xmlelmNode.query("//fields/field/select");//(NodeList) xp.evaluate("//fields/field/select", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.size(); i++) {
				Element inputElm = (Element) nl.get(i);
				String htmlid = inputElm.getAttributeValue("forid");
				Nodes n =    dochtml.query("//*[@id=\""+htmlid+"\"]");//\(Element\) xp\.evaluate\("//*[@id=\""+htmlid+"\"]",(  dochtmll), XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null && n.size() >0){
					Elements textnodenl = inputElm.getChildElements("text");
					Element textelement = null;
					if(textnodenl != null){
						textelement = (Element) textnodenl.get(0);
					}
//					CDATASection cdata = dochtml.createCDATASection(textelement.getValue());
//					element.appendChild(cdata);
					appendXmlFragment(dbuild,n.get(0),textelement.getValue());
					String listValue = inputElm.getAttributeValue("value");
					
					if(listValue != null && listValue != ""){
						listValue = listValue.replace("{", " ");
						listValue = listValue.replace("}", " ");
						String[] list = listValue.split(",");
						  Nodes nodeList = dochtml.query("//select[@id=\""+htmlid+"\"]");
						Node node = nodeList.get(0);//? why only first select
						
						for(String val:list){
							val = val.trim();
							String[] key = val.split("=");
							Element element = new Element("option");
							element.addAttribute(new Attribute("value", key[0]));
							element.appendChild(key[1]);
							((ParentNode) node).appendChild(element);
						}
					}else{ //if hard coded values are not there then look for filling up from action context
						ValueStack stack = ActionContext.getContext().getValueStack();
						Map<String,String>opts = (Map<String, String>) stack.findValue(htmlid);
						if(opts != null){
							Nodes nodeList = dochtml.query("//select[@id=\""+htmlid+"\"]");
							Node node = nodeList.get(0);
							for (Entry<String, String> option : opts.entrySet()) {
								Element element = new Element("option");
								element.addAttribute(new Attribute("value", option.getKey()));
								element.appendChild(option.getValue());
								((ParentNode) node).appendChild(element);
							}
						}
					}
				}else{
					//TODO: We need to insert in custom fields
				}
				
			}
			
			Nodes headnl = dochtml.query("/html/head");
			if(headnl.size() < 1){
				Element elm = new Element("head");
				Element htmltop = (Element) dochtml.query("html").get(0);
				htmltop.insertChild(elm,0);
				//TODO: append head
			}
			
			
				
			Node headNode = headnl.get(0);
			/*String scriptinclude = (String)xp.evaluate("//scriptinclude/text()", xmlelmNode, XPathConstants.STRING);
			if (scriptinclude != null) {
				String[] includeScripts = scriptinclude.split(",");
				for (String val : includeScripts) {
					Element e = new Element("script");
					e.addAttribute(new Attribute("src", val));
					e.addAttribute(new Attribute("language", "JavaScript"));
					e.addAttribute(new Attribute("type", "text/javascript"));
					headNode.appendChild(e);
					headNode.appendChild(dochtml.createTextNode("\n"));
				}
			}
			
			String scripts = (String) xp.evaluate("//scripts/text()", xmlelmNode, XPathConstants.STRING);
			 
			if(scripts.length() > 1){
				appendXmlFragment(dbuild,headNode,scripts);
			}*/
			///multiple scriptincludes and scripts tags
			int countnl =  ((Node) xmlelmNode.query("//scripts").get(0)).getChildCount();		
			Node scripts = (Node) xmlelmNode.query("//scripts").get(0);
//			logger.debug("To Remove Adding scripts:"+countnl);
			for (int i = 0; i < countnl; i++) {
				Node scriptnode = scripts.getChild(i);
//				logger.debug("Adding scipts"+scriptnode.getValue());
				if(scriptnode  instanceof Text){
					appendXmlFragment(dbuild,headNode,scriptnode.getValue());
				}
				if(scriptnode instanceof Element && "scriptinclude".equals(((Element) scriptnode).getLocalName())){
					String[] includeScripts = scriptnode.getValue().split(",");
					for (String val : includeScripts) {
						Element e = new Element("script");
						e.addAttribute(new Attribute("src", val));
						e.addAttribute(new Attribute("language", "JavaScript"));
						e.addAttribute(new Attribute("type", "text/javascript"));
						((ParentNode) headNode).appendChild(e);
						((Element)headNode).appendChild("\n");
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
					Element e = new Element("link");
					e.addAttribute(new Attribute("href", val));
					e.addAttribute(new Attribute("rel", "stylesheet"));
					e.addAttribute(new Attribute("type", "text/css"));
					headNode.appendChild(e);
				}
			}*/
			//////
			Node style =   xmlelmNode.query("//stylesheets").get(0);		
			int count = style.getChildCount();
			for (int i = 0; i < count ; i++) {
				
				Node scriptnode = style.getChild(i);
//				logger.debug("Adding styles"+scriptnode.getValue());
				if(scriptnode instanceof Text){
					appendXmlFragment(dbuild,headNode,scriptnode.getValue());
				}
				if(scriptnode instanceof Element && "styleinclude".equals(((Element) scriptnode).getLocalName())){
					String[] includeScripts = scriptnode.getValue().split(",");
					for (String val : includeScripts) {
						Element e = new Element("link");
						e.addAttribute(new Attribute("href", val));
						e.addAttribute(new Attribute("rel", "stylesheet"));
						e.addAttribute(new Attribute("type", "text/css"));
						((ParentNode) headNode).appendChild(e);
						((Element) headNode).appendChild("\n");
					}
				}
			}
			///multiple styleincludes and stylesheet tags
			
			nl = xmlelmNode.query("//fields/field/div");//(NodeList) xp.evaluate("//fields/field/div", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.size(); i++) {
				Element inputElm = (Element) nl.get(i);
				String htmlid = inputElm.getAttributeValue("forid");
				Nodes n =  dochtml.query("//*[@id=\""+htmlid+"\"]");//(Element) xp.evaluate("//*[@id=\""+htmlid+"\"]", dochtml, XPathConstants.NODE);
				logger.debug("setting values forid:"+"//*[@id=\""+htmlid+"\"]");
				if(n != null && n.size()>0){
					((Element) n.get(0)).appendChild(inputElm.getAttributeValue("value"));
				}else{
					//TODO: We need to insert in custom fields
					Element e = new Element("div");
					e.addAttribute(new Attribute("id", inputElm.getAttributeValue("id")));
					Element body = (Element) dochtml.getRootElement().getChildElements("body").get(0);
					Node xpathnode = inputElm.getFirstChildElement("xpath");
					if(xpathnode.getValue().length() >0 )
						body =  dochtml.getRootElement().getFirstChildElement("body");//\(Element\) xp\.evaluate\("/html/body",(  dochtmll), XPathConstants.NODE);
					body.insertChild(e, 0);
					 
				}
				
			}
			
			String globaljs = "";
			//compositefield for DataElements
			nl = xmlelmNode.query("//fields/field/compositefield");//(NodeList) xp.evaluate("//fields/field/compositefield", xmlelmNode, XPathConstants.NODESET);
			for (int i = 0; i < nl.size(); i++) {
				Element compositefield = (Element) nl.get(i);
				Element datafield = (Element) compositefield.getFirstChildElement("datafield");
				String dfid=datafield.getAttributeValue("id");
				String dfforid=datafield.getAttributeValue("forid");
				String dfname=datafield.getAttributeValue("name");
				String dfvalue=datafield.getAttributeValue("value");
				String dftype=datafield.getAttributeValue("type");
				if(dfvalue.length() == 0){
					dfvalue="{}";
				}
				JSONObject dfjson = new JSONObject(dfvalue);
				Elements displayfield = compositefield.getChildElements("displayfield");
				Element datafldhtm = new Element("input");
				datafldhtm.addAttribute(new Attribute("type", dftype));
				datafldhtm.addAttribute(new Attribute("id", dfid));
				datafldhtm.addAttribute(new Attribute("name", dfname));
				datafldhtm.addAttribute(new Attribute("value", dfvalue));
				Nodes dfnode =   dochtml.query("//*[@id=\""+dfforid+"\"]");//\(Element\) xp\.evaluate\("//*[@id=\""+dfforid+"\"]",(  dochtmll), XPathConstants.NODE);
				boolean dfforidfound = false;
				if(dfforid != null && dfnode.size()>0){
					dfforidfound = true;
					((Element) dfnode.get(0)).appendChild(datafldhtm);
				}
				for (int j = 0; j < displayfield.size(); j++) {
					Element dispelmxml = (Element)displayfield.get(j);
					String dename = dispelmxml.getAttributeValue("name");
					String deforid = dispelmxml.getAttributeValue("forid");
					String detype = dispelmxml.getAttributeValue("type");
					Nodes den =   dochtml.query("//*[@id=\""+deforid+"\"]");//\(Element\) xp\.evaluate\("//*[@id=\""+deforid+"\"]",(  dochtmll), XPathConstants.NODE);
					Element denode ;
					if(den.size()>0){
						denode = (Element) den.get(0);
					}else{
						continue;
					}
					Element difldhtm = new Element("input");
					difldhtm.addAttribute(new Attribute("type", detype));
					difldhtm.addAttribute(new Attribute("id", dename));
					difldhtm.addAttribute(new Attribute("name", dename));
					if(dfjson.has(dename))
					difldhtm.addAttribute(new Attribute("value", dfjson.getString(dename)));
					difldhtm.addAttribute(new Attribute("onblur", "updateCompositeField(this,'#"+dfid+"')"));
					if(dfforidfound == false && denode!=null){
						dfforidfound = true; //appending to first display element
						denode.appendChild(datafldhtm);
					}
					if(denode!=null)
					denode.appendChild(difldhtm);
				}
				
			}
			//compositefield for DataElements end
			//Append all validations
			nl =  xmlelmNode.query("//validation");//, XPathConstants.NODESET);
			for (int i = 0; i < nl.size(); i++) {
				String validation = nl.get(i).getValue();
				
				if(validation != null && validation.length() > 1){
					globaljs += validation +"\n";
				}
			}
			//JSON rule begin
			nl =  xmlelmNode.query("//rule");//, XPathConstants.NODESET);
			JSONObject rulejson = new JSONObject("{rules:{},messages:{}}");
			for (int i = 0; i < nl.size(); i++) {
				Element ruleElm = (Element) nl.get(i);
				String ruletext = ruleElm.getValue();
				logger.debug("Rule="+ruletext);
				if(ruletext != null && ruletext.length() > 0){
					JSONArray jar = new JSONArray(ruleElm.getValue());
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
			appendXmlFragment(dbuild, headNode, strrule);
			//JSON rule ends
			
			DOMImplementationRegistry  registry =
			       DOMImplementationRegistry.newInstance();
			DOMImplementation domImpl =
			       registry.getDOMImplementation("XML 3.0");
			org.w3c.dom.Document w3cDoc = DOMConverter.convert(dochtml	,  domImpl); 
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
//			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			//initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(w3cDoc);

			transformer.transform(source, result);

			logger.debug("After conversion");
			//xmlString = dochtml.toXML();
//			ByteArrayOutputStream stro = new ByteArrayOutputStream();
//			 Serializer serializer = new Serializer(stro, "ISO-8859-1"); 
//			  //serializer.setIndent(4);
//		     //  serializer.setMaxLength(200);
//		      serializer.write(dochtml);  
		      
			xmlString =  result.getWriter().toString();
			templateprocessed = true;
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
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
	/* (non-Javadoc)
	 * @see com.ycs.fe.HTMLProcessor#appendXmlFragment(javax.xml.parsers.DocumentBuilder, org.w3c.dom.Node, org.w3c.dom.NodeList)
	 */
	@Override
	public   void appendXmlFragment(DocumentBuilder docBuilder, org.w3c.dom.Node parent,  org.w3c.dom.NodeList fragment) throws IOException, SAXException {
//		logger.debug("still coming here");
//		Document doc = parent.getOwnerDocument();
////		Node fragmentNode = docBuilder.parse(new InputSource(new StringReader("<root>"+fragment+"</root>"))).getDocumentElement();
//		NodeList nl = fragment;
//		if(fragment.get(0).getNodeType() == Node.CDATA_SECTION_NODE){
//			appendXmlFragment( docBuilder,  parent,  fragment.get(0).getValue());
//			return;
//		}
//		for (int i = 0; i < nl.size(); i++) {
//			Node n = nl.get(i);			
//				Node node = doc.importNode(n, true);
//				parent.appendChild(node);
//		}
	}
	/* (non-Javadoc)
	 * @see com.ycs.fe.HTMLProcessor#appendXmlFragment(javax.xml.parsers.DocumentBuilder, org.w3c.dom.Node, java.lang.String)
	 */
	@Override
	public   void appendXmlFragment(DocumentBuilder docBuilder, org.w3c.dom.Node parent, String fragment) throws IOException, SAXException {
//		logger.debug("still coming here");
//		Document doc = parent.getOwnerDocument();
//		Node fragmentNode = docBuilder.parse(new InputSource(new StringReader("<root>"+fragment+"</root>"))).getDocumentElement();
//		NodeList nl = fragmentNode.getChildNodes();
//		for (int i = 0; i < nl.size(); i++) {
//			Node n = nl.get(i);			
//				Node node = doc.importNode(n, true);
//				parent.appendChild(node);
//		}
	}
	
	public static void main(String[] args) throws Exception {
		/* Create and adjust the configuration */
		System.out.println("Running local HTMLProcessorXOM");
	 
		 HTMLProcessorXOMImpl htmp = new HTMLProcessorXOMImpl();
		String processedhtml = htmp.process(htmp.fileReadAll("C:/Eclipse/workspace1/FEtranslator1/src/actionclass/sampleoutput.xml"), null);
		System.out.println(processedhtml);
	}
}
