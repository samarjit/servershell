package servershell.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;
import servershell.be.dao.DBConnector;

public class ReverseEngineerXml {
	private String globalSQL = "  SELECT PRODUCT_CODE ,  PRODUCT_NAME ,  PLASTIC_CODE,   PLASTIC_DESC    FROM PRODUCT_DETAILS ";
	private  StringWriter strw = null;
	JSONObject jresult = null;
	
	public String getStringResult(String globalSQL) throws Exception{
		this.globalSQL = globalSQL;
		reverseEng();
		return jresult.toString();
	}
	public static String toProperCase(String inputString) {

		StringBuffer result = new StringBuffer();
		String[] list = null;
		if (inputString != null && inputString.length() > 0) {
			StringTokenizer tok = new StringTokenizer(inputString, "_ ");
			if (tok.hasMoreElements())
				list = new String[tok.countTokens()];
			int n = 0;
			while (tok.hasMoreElements()) {
				list[n] = (String) tok.nextElement();
				n++;
			}
			if (list != null && list.length > 0) {
				for (int i = 0; i < list.length; i++) {
					String str = list[i];
					str = str.toLowerCase();
					char[] charArray = str.toCharArray();
					charArray[0] = Character.toUpperCase(charArray[0]); // list[i]
																		// = new
																		// String(charArray);
					result.append(new String(charArray));
					if (i < n)
						result.append(" ");
				}
			}
		}
		return result.toString();
	}

	public String getXmlmapDataTypeName(String dbimplType, boolean scale, int precision){
		if(dbimplType.equals("VARCHAR2"))return "STRING";
		if(dbimplType.equals("VARCHAR"))return "STRING";
		if(dbimplType.equals("DATE"))return "DATE_NS";
		if(dbimplType.equals("TIMESTAMP"))return "TIMESTAMP";
		
		if(dbimplType.equals("NUMBER") || dbimplType.equals("DECIMAL")  ){
			if(scale)return "FLOAT";
			else if(precision > 10){
				return "LONG";
			}else{
				return "INT";
			}
		}
		if(dbimplType.equals("INTEGER"))return "INT";
		if(dbimplType.equals("FLOAT"))return "FLOAT";
		if(dbimplType.equals("DOUBLE"))return "DOUBLE";
		if(dbimplType.equals("LONG"))return "LONG";
		if(dbimplType.equals("BIGINT"))return "LONG";
		
		return "STRING";
	}
	public void reverseEng() throws Exception {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		Statement st = con.createStatement();

		String sql = globalSQL;
//		StringBuffer inputSql = new StringBuffer();//new BufferedReader(new InputStreamReader(System.in)).readLine();
//		int ch = -1;
//		out.println("Enter a query followed by ';' or simply enter ';' and press <enter>\r\n:");
//		while((ch = System.in.read())!= ';'){
//			inputSql.append((char)ch);
//		}
//		if(inputSql.length() >0){
//			sql= inputSql.toString();
//		}
		ResultSet rs = st.executeQuery(sql);
		ResultSetMetaData metaData = rs.getMetaData();
		int rowCount = metaData.getColumnCount();
		ArrayList<String> arheader = new ArrayList<String>();
		ArrayList<String> aralias = new ArrayList<String>();
		ArrayList<String> arcol = new ArrayList<String>();
		ArrayList<String> ardatatype = new ArrayList<String>();
		ArrayList<Integer> arcolprecision = new ArrayList<Integer>();
		String columnName;
		String alias;
		String col; //temp
		int size = 0;
		int scale= 0;
		String tableName = "";
		tableName = metaData.getTableName(1);
//		if(tableName == null || tableName.equals("")){
//			tableName = metaData.getTableName(2);
//		}
		
		strw = new StringWriter();
		PrintWriter out = new PrintWriter( strw);
		jresult = new JSONObject();
		
		StringBuffer sb = new StringBuffer();
		if(tableName == null || tableName.equals("")){
			Pattern p = Pattern.compile("(?ims:from)\\s+(\\w+)\\s*", Pattern.MULTILINE);
			Matcher  m = p.matcher(sql);
			m.find();
			out.println(m.group(1).toUpperCase());
			tableName = m.group(1).toUpperCase();
//			out.println("Enter Table Name: followed by ';'<enter>");
//			while((ch = System.in.read())!= ';'){
//				sb.append(Character.toUpperCase((char)ch));
//			}
//			if(sb.length() >0){
//				tableName= sb.toString().trim();
//			}
			 
		}
		out.println("Table Name : " + tableName);
		out.println("Field  \tsize\tDataType");
		String screenName = toProperCase(tableName).replaceAll(" ", "");
		for (int i = 0; i < rowCount; i++) {
			columnName = metaData.getColumnName(i + 1);
			 
			arcol.add(columnName);
			col = toProperCase(columnName);
			col = col.replaceAll("\\_", " ");
			arheader.add(col);
			alias = col.toLowerCase().replaceAll(" ", "");
			aralias.add(alias);
			size = (metaData.getPrecision(i+1) ==0)?10:metaData.getPrecision(i+1);//metaData.getColumnDisplaySize(i + 1);
			scale= metaData.getScale(i+1);
			ardatatype.add(getXmlmapDataTypeName(metaData.getColumnTypeName(i + 1), scale != 0, metaData.getPrecision(i+1)));
			arcolprecision.add(size);
			
			//out.print(metaData.getColumnName(i + 1) + "  \t\t\t");
			String scal =  (scale >0)?","+scale:"";
//			out.print(metaData.getColumnDisplaySize(i + 1) + " ==? " + metaData.getPrecision(i+1)+"\t"+scale+"\t");
			out.printf("%-50s %50s \r\n",metaData.getColumnName(i + 1)   ,metaData.getColumnTypeName(i + 1)+"("+size+scal+"), "/*+metaData.getColumnType(i + 1)*/);
		}
		
		if(con!= null){
			con.close();
		}
		
		
		jresult.put("tabledetails", strw.toString());
		strw.getBuffer().setLength(0);
		
		out.println(screenName);
		
		jresult.put("screenname", strw.toString());
		strw.getBuffer().setLength(0);
		
		//html
		out.println("<!DOCTYPE script PUBLIC \"-//W3C//DTD XHTML 1.1 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n" + 
				"<%@taglib prefix=\"s\"  uri=\"/struts-tags\" %>\r\n" + 
				"<%@taglib prefix=\"sj\"  uri=\"/struts-jquery-tags\" %>\r\n" + 
				"<%--@taglib prefix=\"sjg\"  uri=\"/struts-jquery-grid-tags\" --%>\r\n" + 
				"<html>\r\n" + 
				"<head>\r\n" + 
				" <title>"+screenName+"</title>\r\n" + 
				"<sj:head jqueryui=\"true\" jquerytheme=\"redmond\"  />\r\n" +
				"<s:set var=\"ctx\"  >${pageContext.request.contextPath}</s:set>" + 
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"${ctx}/css/ui.jqgrid.css\">\r\n" + 
				"<!--script src=\"../js/jquery.validate.js\" > </script>\r\n" + 
				"<script src=\"../js/additional-methods.js\" > </script-->\r\n" + 
				"<script src=\"../js/i18n/grid.locale-en.js\" > </script>\r\n" + 
				"<script src=\"../js/jquery.jqGrid.min.js\" > </script>\r\n" + 
				"<!--script src=\"../js/json2.js\" > </script-->\r\n"+
				"\r\n"+
				"<script>");
		
		//headers
		out.println("	var rulesframework = {}; \r\n" + 
				           "	<s:if test = \"jsrule != null\" >\r\n" + 
				           "		 rulesframework =  ${jsrule};\r\n" + 
				           "	</s:if>");
		String fieldlist = aralias.toString().replace("[", "        	var fieldlist = \"").replace("]", "\".split(\",\");");
		out.println(fieldlist);
		out.println("   $(document).ready(function(){\r\n" + 
				           "	//iadt.setFieldlist(fieldlist);\r\n" + 
				           "	globalAjaxErrorSetup();\r\n"+
				           "	//$(\"#form1\").validate($.extend(rulesframework,{debug: true}));\r\n" + 
				           "	calljqgrid();		\r\n" + 
				           "   });");
		out.println("	function showAjaxError(request, type, errorThrown)\r\n" + 
							"   {\r\n" + 
							"       var message = \"There was an error with the AJAX request.\\n\";\r\n" + 
							"       switch (type) {\r\n" + 
							"           case 'timeout':\r\n" + 
							"               message += \"The request timed out.\";\r\n" + 
							"               break;\r\n" + 
							"           case 'notmodified':\r\n" + 
							"               message += \"The request was not modified but was not retrieved from the cache.\";\r\n" + 
							"               break;\r\n" + 
							"           case 'parseerror':\r\n" + 
							"               message += \"XML/Json format is bad.\";\r\n" + 
							"               break;\r\n" + 
							"           default:\r\n" + 
							"               message += \"HTTP Error (\" +type+\" \"+ request.status + \" \" + request.statusText + \").\";\r\n" + 
							"       }\r\n" + 
							"       message += \"\\n\";\r\n" + 
							"       alert(message);\r\n" + 
							"   }\r\n" + 
							"   function globalAjaxErrorSetup(){\r\n" + 
							"	   $( \"#globalajaxerror\" ).ajaxError(function(e, jqxhr, settings, exception) {\r\n" + 
							"		     alert( \"Triggered ajaxError handler.\" +exception);\r\n" + 
							"			 if(window.console){\r\n" + 
							"				 console.log(\"Ajax ecxcetion:\");\r\n" + 
							"				 console.log(exception);\r\n" + 
							"				 }  \r\n" + 
							"		 });\r\n" + 
							"	}");
		out.println("  var lastsel= {};\r\n" + 
		                   "  function calljqgrid(formdata){");
		out.println("   jQuery(\"#listid\").jqGrid( {\r\n");
		out.println("      	url:'<%= request.getContextPath() %>/jqgrid.action?command=true&screenName="+screenName+"&submitdata={bulkcmd=\"gridonload\"}',");
		out.println("      	datatype: \"json\",");
		out.println("      	height:350, ");
		  String colNames= "      	colNames:[";
		    boolean first = true;
		    for (String string : arheader) {
		    	colNames += (first)?"":","; first = false;
		    	colNames += "'"+string.trim()+"'";
		    }
		       colNames += "      	],";
		
		out.println(colNames);
		
		//jqgrid column model
		 String colModel = "      	colModel:[";first =true;
		    for (int i = 0; i < aralias.size(); i++) {
		    	alias = aralias.get(i);
		    	size = arcolprecision.get(i);
		    	colModel += (first)?"\r\n":",\r\n";first = false;
		    	 
		    	size = (size > 99)?50:size;
		    	size = (size < 20)?(size == 10)?138:size * 11: size *8;
		    	colModel +="      	{name: '"+alias+"', index: '"+alias+"' , width:"+size+", editable:true }" ;
		    }
		        colModel +="\r\n      	],";
		out.println(colModel);
		out.println("      	rowNum: 15,\r\n" + 
				           "      	rowList: [ 15, 25, 50],\r\n" + 
				           "      	pager: '#pagerid',\r\n" + 
				           "      	sortname: '"+aralias.get(0)+"',\r\n" + 
				           "        viewrecords: true,\r\n" + 
				           "        sortorder: \"desc\",\r\n" + 
				           "        jsonReader: {\r\n" + 
				           "    		repeatitems : false,\r\n" + 
				           "    		userdata: 'userdata',\r\n" +
				           "            id: \"0\"\r\n" + 
				           "    	},\r\n" + 
				           "       onSelectRow: function(id){\r\n" + 
				           "			    		if(id && id!==lastsel){\r\n" + 
				           "			    			//jQuery('#listid').jqGrid('restoreRow',lastsel);\r\n" + 
				           "			    			//jQuery('#listid').jqGrid('editRow',id,true);\r\n" + 
				           "			    			jQuery(\"#listid\").jqGrid('GridToForm',id,\"#form1\");\r\n" + 
				           "			    			lastsel=id;\r\n" + 
				           "			    		}\r\n" + 
				           "			    	},\r\n" + 
				           "	    loadComplete: function(){\r\n" + 
				           "			    		var ret;\r\n" + 
				           " 						$(\"#messagegrid\").text(JSON.stringify(jQuery(\"#listid\").getGridParam('userData'), null, 2));\r\n"+
				           "			    	},\r\n" + 
				           "			       editurl: \"${pageContext.request.contextPath}/html/simpleform.action?screenName="+screenName+"&bulkcmd=grid\",\r\n"+
				           "       caption: \""+screenName+"\"\r\n" + 
				           "   } ).navGrid('#pagerid',{edit:true,add:true,del:true},{},{},{},{multipleSearch:true, multipleGroup:true});");
		out.println("   jQuery(\"#listid\").jqGrid('navButtonAdd','#pagerid',{caption:\"Edit\",\r\n" + 
				           "		onClickButton:function(){\r\n" + 
				           "			var gsr = jQuery(\"#listid\").jqGrid('getGridParam','selrow');\r\n" + 
				           "			if(gsr){\r\n" + 
				           "				jQuery(\"#listid\").jqGrid('GridToForm',gsr,\"#form1\");\r\n" + 
				           "			} else {\r\n" + 
				           "				alert(\"Please select Row\");\r\n" + 
				           "			}							\r\n" + 
				           "		} \r\n" + 
				           "	}); ");
		out.println("  } //end calljqgrid"); 
        out.println("  function ajaxSubmit(){\r\n" + 
        		 		   "	jQuery.post(\"${pageContext.request.contextPath}/html/simpleform.action?screenName="+screenName+"\", \r\n" + 
        		 		   "			$(\"#form1\").serialize(),\r\n" + 
        		 		   "			function(data){\r\n" + 
        		 		   "		var json = jQuery.parseJSON(data);\r\n" + 
        		 		   "		jQuery(\"#listid\").trigger(\"reloadGrid\");\r\n" + 
        		 		   "      }).error(showAjaxError);\r\n" + 
        		 		   "  }");
        
        out.println("</script>");
		out.println("\r\n</head>\r\n" + 
				"\r\n" + 
				"<body>\r\n"+
				"  <table id=\"listid\" ></table>\r\n" + 
				"  <div id=\"pagerid\"></div>"+
				"  <div id=\"messagegrid\"></div>");
		
		
		   
		 
		 out.println("<!--Submit Form -->\r\n<form name=\"form1\" id=\"form1\" method=\"post\" action=\"${pageContext.request.contextPath}/html/simpleform.action?screenName="+screenName+"\">\r\n" + 
		 		"        	 <table>" );
		 for (int i = 0; i < aralias.size(); i++) {
			 alias = aralias.get(i);
			 out.println("        	   <tr><td>"+arheader.get(i)+"</td><td><input name=\""+alias+"\" id=\""+alias+"\" value=\"${resultDTO.data.formonload[0]."+alias+"}\"/></td></tr>");
		 }
		 out.println("        	   \r\n\r\n");
		 for (int i = 0; i < aralias.size(); i++) {
			 alias = aralias.get(i);
			 out.println("        	   <tr><td>"+arheader.get(i)+"</td><td><s:property value=\"#resultDTO.data.formonload[0]."+alias+"\"  /></td></tr>");
		 }		 
		 out.println("        	 </table>\r\n" + 
		       "        	 bulkcmd: <input name=\"bulkcmd\" value=\"frmgridedit\"/>\r\n" + 
		       "        	 <button >submit</button>\r\n" + 
		       "        	 <button type=\"button\" onclick=\"ajaxSubmit()\">Ajax Submit</button>"+
		 		"</form>");
		 out.println("</body>\r\n" + 
		 		"\r\n" + 
		 		"</html>");
		 
		 jresult.put("htmlscreen", strw.toString());
		 strw.getBuffer().setLength(0);
			
		 out.println("<!--View Grid-->\r\n" + 
			 		"        	 <table>" );
		 for (int i = 0; i < aralias.size(); i++) {
			 alias = aralias.get(i);
			 out.println("        	   <tr><td>"+arheader.get(i)+"</td><td><s:property value=\"#resultDTO.data.formonload[0]."+alias+"\"  /></td></tr>");
		 }		 
		 out.println("        	 </table>\r\n"+ "</form>");
		 
		 for (int i = 0; i < aralias.size(); i++) {
			 alias = aralias.get(i);
			 out.println("        	   "+arheader.get(i)+" \t<s:property value=\"#resultDTO.data.formonload[0]."+alias+"\"  />");
		 }
		
		 jresult.put("htmlscreenview", strw.toString());
		 strw.getBuffer().setLength(0);
		 
		 
		 String datatype = "";
			
		 //select clause;
		
		 String sel = "SELECT ";first = true;
		 String sel2 = "SELECT ";
			for (int i = 0; i < aralias.size(); i++) {
				alias = aralias.get(i);
				size = arcolprecision.get(i);
				datatype = ardatatype.get(i);
				col = arcol.get(i);
				sel += (first)?"":",";  
				sel2 += (first)?"":","; 
				
				if(datatype.equals("TIMESTAMP") || datatype.equals("DATE")){
					sel += "TO_CHAR("+col+",'YYYY-MM-DD hh24:mi:ss')||'.0' \""+alias+"\"";
				}else{
					sel += col+" \""+alias+"\"";
				}
				
				if(datatype.equals("TIMESTAMP")){
					sel2 += "TO_CHAR("+col+" ,'YYYY-MM-DD hh24:mi:ss')||'.0' "+col;
				}else{
					sel2 += col;
				}
				
				first = false;
			}
			sel += " FROM "+tableName;
			sel2 += " FROM "+tableName;
			
			out.println(sel);
			out.println(sel2);
			
			 jresult.put("aliasquery", strw.toString());
			 strw.getBuffer().setLength(0);
			
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
					"<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../config.xsd\">\r\n" + 
					"  <screen name=\""+screenName+"\">\r\n" + 
					"    <htmltemplate></htmltemplate>\r\n" + 
					"    <includedjsp></includedjsp>\r\n" + 
					"    <callbackclass></callbackclass>\r\n" + 
					"    <scripts>\r\n" + 
					"      <scriptinclude></scriptinclude>\r\n" + 
					"    </scripts>\r\n" + 
					"    <stylesheets>\r\n" + 
					"      <styleinclude></styleinclude>\r\n" + 
					"    </stylesheets>\r\n" + 
					"    <sessionvars>sessionvars</sessionvars>\r\n" + 
					"    <crud>");
		

			out.println("	   <jsonrpc outstack=\"formonload\" id=\"onloadqry1\">"+sel+" \r\n" + 
					           "			<countquery pagesize=\"10\">select count('x') from "+tableName+" </countquery>\r\n" +
							   "	   </jsonrpc>\r\n" + 
					           "       <jsonrpc outstack=\"formpagination\" id=\"gridonload\">"+sel+" \r\n" + 
					           "			<countquery pagesize=\"10\">select count('x') from "+tableName+" </countquery>\r\n" + 
					           "	   </jsonrpc>");
			//normal inserts
			out.println("<!--NORMAL SQLs");
			String simplefrmcols ="";
			String simplefrmvals = "";
			String prefix = "frmnrml";
			first = true;
			for (int i = 0; i < aralias.size(); i++) {
				col = arcol.get(i);
				simplefrmcols += (first)?" ":", ";
				simplefrmvals += (first)?" ":", ";first = false;
				
				simplefrmcols +=""+col;
				simplefrmvals += "#inp.form1[0]."+aralias.get(i);
			}
			String simpefrmins="insert into "+tableName +"("+simplefrmcols+") values ("+simplefrmvals+")";
			out.println("      <sqlinsert id=\""+prefix+"add\" outstack=\"inst\">"+simpefrmins+"</sqlinsert>");
			
			//simpleform delete
			String simplefrmdel = "delete from "+tableName +" WHERE "+arcol.get(0)+" = #inp.form1[0]."+aralias.get(0);
			out.println("      <sqldelete id=\""+prefix+"del\" outstack=\"delt\">"+simplefrmdel+"</sqldelete>");
			
			//simpleform update
			String simepleformupd = "update "+tableName +" set ";
			first = true;
			for (int i = 1; i < aralias.size(); i++) {
				simepleformupd += (first)?" ":", ";first = false;
				simepleformupd += arcol.get(i)+" = "+"#inp.form1[0]."+aralias.get(i);
			}
			simepleformupd += " WHERE "+arcol.get(0)+"=#inp.form1[0]."+aralias.get(0);
			out.println("      <sqlupdate id=\""+prefix+"edit\" outstack=\"updt\">"+simepleformupd+"</sqlupdate>");
			out.println("-->");
			
			 
			//start grid inserts
			out.println("<!--GRID INSERTS (check out which alias is required-->");
			//simpleform insert 
			  simplefrmcols ="";
			  simplefrmvals = "";
			  String bulkcmd = "grid"; //bulkcmd+oper
			  String bulkcmdsimplfrm = "frmgrid"; //bulkcmd+oper
			first = true;
			for (int i = 0; i < aralias.size(); i++) {
				col = arcol.get(i);
				simplefrmcols += (first)?" ":", ";
				simplefrmvals += (first)?" ":", ";first = false;
				
				simplefrmcols +=""+col;
				simplefrmvals += "#inp.form1[0]."+arcol.get(i);
			}
			  simpefrmins="insert into "+tableName +"("+simplefrmcols+") values ("+simplefrmvals+" )";
			  out.println("      <sqlinsert id=\""+bulkcmdsimplfrm+"add\" outstack=\"inst\">"+simpefrmins+"</sqlinsert>");
			out.println("      <sqlinsert id=\""+bulkcmd+"add\" outstack=\"inst\">"+simpefrmins+"</sqlinsert>");
			
			//simpleform delete
			simplefrmdel = "delete from "+tableName +" WHERE "+arcol.get(0)+" = #inp.form1[0]."+arcol.get(0)+"|STRING";//+arcol.get(0);
			out.println("      <sqldelete id=\""+bulkcmdsimplfrm+"del\" outstack=\"delt\">"+simplefrmdel+"</sqldelete>");
			
			  simplefrmdel = "delete from "+tableName +" WHERE "+arcol.get(0)+" = #inp.form1[0].id|STRING";//+arcol.get(0);
			  out.println("      <sqldelete id=\""+bulkcmd+"del\" outstack=\"delt\">"+simplefrmdel+"</sqldelete>");
			 
			//simpleform update
			String gridupd= "";
			  simepleformupd = "update "+tableName +" set ";
			first = true;
			for (int i = 1; i < aralias.size(); i++) {
				simepleformupd += (first)?" ":", ";first = false;
				simepleformupd += arcol.get(i)+" = "+"#inp.form1[0]."+arcol.get(i);
			}
			gridupd = simepleformupd;
			simepleformupd += " WHERE "+arcol.get(0)+"=#inp.form1[0]."+arcol.get(0);
			out.println("      <sqlupdate id=\""+bulkcmdsimplfrm+"edit\" outstack=\"updt\">"+simepleformupd+"</sqlupdate>");
			gridupd += " WHERE "+arcol.get(0)+"=#inp.form1[0].id|STRING";
			out.println("      <sqlupdate id=\""+bulkcmd+"edit\" outstack=\"updt\">"+gridupd+"</sqlupdate>");
			///end grid inserts

			out.println("    </crud>\r\n" + 
					"    <dm>\r\n" + 
					"      <txnproc id=\"\" outstack=\"\"/>\r\n" + 
					"    </dm>\r\n" + 
					"    <bl id=\"\">\r\n" + 
					"      <buslogic id=\"\" method=\"\"/>\r\n" + 
					"    </bl>\r\n" + 
					"    <anyprocs>\r\n" + 
					"      <proc id=\"\" outstack=\"\"/>\r\n" + 
					"    </anyprocs>");

			///xml cmd//
			out.println("    <commands>\r\n" + 
					           "      <cmd instack=\"\" name=\"\" opt=\"\"  result=\"\" />\r\n" + 
					           "      <bulkcmd name=\"fromViewTransition\" opt=\"\" result=\"ajax\" resultScrName=\""+screenName+"\" />\r\n" + 
					           "      <bulkcmd name=\"frmgridadd\" opt=\"sqlinsert:frmgridadd\" result=\"ajax\" resultScrName=\""+screenName+"\"/>\r\n" + 
					           "      <bulkcmd name=\"frmgridedit\" opt=\"sqlupdate:frmgridedit\" result=\"ajax\"/>\r\n" + 
					           "      <bulkcmd name=\"frmgriddel\" opt=\"sqldelete:frmgriddel\" result=\"ajax\"/>\r\n" + 
					           "      <bulkcmd name=\"griddel\" opt=\"sqldelete:griddel\" result=\"ajax\"/>\r\n" + 
					           "      <bulkcmd name=\"gridedit\" opt=\"sqlupdate:gridedit\" result=\"ajax\"/>\r\n" + 
					           "      <bulkcmd name=\"gridadd\" opt=\"sqlinsert:gridadd\" result=\"ajax\"/>\r\n" + 
					           "      <bulkcmd name=\"gridonload\" opt=\"jsonrpc:gridonload\" result=\"ajax\"/>\r\n" + 
					           "      <onload opt=\"jsonrpc:onloadqry1\" result=\"/jsptest/"+screenName+".jsp\"/>\r\n" + 
					           "    </commands>");
			
			
			//xml cmd end//
			
			//screen->panels ->fields
			out.println("  </screen>\r\n" + 
					"  <panels>\r\n" + 
					"    <panel id=\"\">\r\n" + 
					"       <fields>\r\n" + 
					"          <field>");
			
			//validation xml
			String validationXml = "";
			String label = "";
			for (int i = 0; i < aralias.size(); i++) {
				alias = aralias.get(i);
				size = arcolprecision.get(i);
				datatype = ardatatype.get(i);
				col = arcol.get(i);
				
				validationXml +="          	<validationfld dbcolsize=\""+size+"\" name=\""+alias+"\" column=\""+col+"\" mandatory=\"yes\" forid=\""+alias+"\" dbdatatype=\""+datatype+"\" />\r\n" ;
				label += "          	<label replace=\"modify\" key=\""+alias+"\" value=\""+arheader.get(i).trim()+"\" forname=\""+alias+"\"/>\r\n";
			}
			 out.println(validationXml);
			 out.println(label);

			 
			 //ending xml
			 out.println("          </field>\r\n" + 
			 		"       </fields>\r\n" + 
			 		"      <button forid=\"\" id=\"\" onclick=\"\" replace=\"modify\" type=\"\">button</button>\r\n" + 
			 		"    </panel>\r\n" + 
			 		"  </panels>\r\n" + 
			 		"</root>\r\n");
			 
			 jresult.put("pagexml", strw.toString());
			 strw.getBuffer().setLength(0);
			 
			 out.println("\r\n<screen name=\""+screenName+"\" mappingxml=\"map/jsptest/"+screenName+".xml\" />\r\n");
			 //xml validation
			
			 jresult.put("screenmapxml", strw.toString());
			 strw.getBuffer().setLength(0);
			
			String crs = "";
			String crs1 = "";
			String crs2 = "";
			String insertSql = " insert into "+tableName+" (\"+insertCol+\") values (\"+insertVal+\")";
			String insertCol = "";
			String insertVal = "";
			String ins = "";
			String upd = "";
			for (int i = 0; i < arheader.size(); i++) {
				crs += "res.set"+arheader.get(i).replace(" ", "")+"(crs.getString(\""+arcol.get(i)+"\"));\r\n";
				crs1 += "res.set"+arheader.get(i).replace(" ", "")+"(crs.getString(\""+aralias.get(i)+"\"));\r\n";
				crs2 += "res.set"+ toProperCase(aralias.get(i).replace(" ", ""))+"(crs.getString(\""+aralias.get(i)+"\"));\r\n";
				
				upd+="if (pinMaster.get"+arheader.get(i).replace(" ", "")+"() != null) {\r\n" + 
						"				qry+= (first)?\"\":\",\";first = false;\r\n" + 
						"				qry+= \""+arcol.get(i)+" = ?\";\r\n" + 
						"				arPrepstmt.add(DataType."+ardatatype.get(i)+", pinMaster.get"+arheader.get(i).replace(" ", "")+"());\r\n" + 
						"			}\r\n";
				
				ins +="if (pinMaster.get"+arheader.get(i).replace(" ", "")+"() != null) {\r\n" + 
						"              insertCol += (first)?\"\":\",\";\r\n" + 
						"              insertVal += (first)?\"\":\",\";\r\n" + 
						"              insertCol +=\"" +arcol.get(i)+"\";\r\n"+
						"              insertVal += \"?\";\r\n" + 
						"              first = false;\r\n" + 
						"              arPrepstmt.add(DataType."+ardatatype.get(i)+", pinMaster.get"+arheader.get(i).replace(" ", "")+"());\r\n" + 
						"           }\r\n";
			}
			out.println(crs+"\r\n");
			out.println(crs1+"\r\n");
			out.println(crs2+"\r\n");
			out.println(upd+"\r\n");
			out.println(ins+"\r\n");
			out.println(insertSql+"\r\n");
			jresult.put("sqls", strw.toString());
			 strw.getBuffer().setLength(0);
			out.println("struts.xml <action name=\"cms\" class=\"com.ycs.ezlink.action.CmsAction\">" +
					"<result name=\"jobsTrack\" >/jsp/jobsTrack.jsp</result>" +
					"..</action>" +
					"\r\n" +
					"CmsAction.java\r\n" +
					"struts execute(){" +
					"lse if(\"jobsTrack\".equals(method)){\r\n" + 
					"				String jobName = request.getParameter(\"jobName\");\r\n" + 
					"				JSONObject json = facade.jobsTrack(jobName);\r\n" + 
					"				resultName = parseResult(json);\r\n" + 
					"			}" +
					"}" +
					"\r\n" +
					"" +
					"CmsFacade.java:" +
					"" +
					"public JSONObject jobsTrack(String jobName) {\r\n" + 
					"		JSONObject result = new JSONObject();\r\n" + 
					"		ResultDTO resultDTO = null;\r\n" + 
					"		JSONObject data = new JSONObject();\r\n" + 
					"		data.put(CMSConstant.JOB_NAME, jobName);\r\n" + 
					"		resultDTO = processCmd(data, \"jobsTrack\", \"cms\");\r\n" + 
					"		if(resultDTO !=null && resultDTO.getErrors().size() == 0 ){\r\n" + 
					"			JSONObject jar = (JSONObject) resultDTO.getData().get(\"jobsTrack\");\r\n" + 
					"			result.put(CMSConstant.RESULT_NAME, \"jobsTrack\");\r\n" + 
					"			result.put(CMSConstant.DATA, jar);\r\n" + 
					"		}else{\r\n" + 
					"			result.put(CMSConstant.RESULT_NAME, \"jobsTrack\");\r\n" + 
					"			result.put(CMSConstant.ERROR, CMSConstant.SQL_ERROR);\r\n" + 
					"			result.put(CMSConstant.DATA, new JSONArray());\r\n" + 
					"		}\r\n" + 
					"		return result;\r\n" + 
					"	}" +
					"\r\n" +
					"\r\n" +
					"" +
					"cms.xml\r\n" +
					"" +
					"<bl>\r\n<buslogic id=\"jobsTrack\" method=\"jobsTrack\"> </buslogic></bl>\r\n" +
					"<bulkcmd name=\"jobsTrack\" opt=\"buslogic:jobsTrack\" result=\"ajax\" />\r\n" +
					"JobTrack.jsp\r\n" +
					"CmsBL.java\r\n" +
					"else if (\"jobsTrack\".equals(methodName)) {\r\n" + 
					"				jobsTrack(inputDTO,resDTO);\r\n" + 
					"			}\r\n" +
					"JodTrackDAO.java\r\n" +
					"" +
					"");
			
			jresult.put("miscellaneous", strw.toString());
			strw.getBuffer().setLength(0);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		ReverseEngineerXml re = new ReverseEngineerXml();
		 
		re.getStringResult("select * from alert_queue ");
	}

}
