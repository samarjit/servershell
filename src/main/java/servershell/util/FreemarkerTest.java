package servershell.util;

 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.ycs.fe.util.Constants;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class FreemarkerTest {

    public static void main(String[] args) throws Exception {
        
    
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(
                new File("C:/Eclipse/workspace2/servershell/src/main/webapp/jsptest"));
        cfg.setObjectWrapper(new DefaultObjectWrapper());

        Template temp = cfg.getTemplate("test.ftl");

       
        
        
        //-----------------------------------------------------------------//
        
        /* Create a data-model */
        Map<String,Object> root = new HashMap<String,Object>();
        root.put("user", "Big Joe");
       

	    ArrayList<HashMap<String, String>> arRow = new ArrayList<HashMap<String, String>>();
	    HashMap<String, String> row1 = new HashMap<String, String>();
	    HashMap<String, String> row2 = new HashMap<String, String>();
	    HashMap<String, String> row3 = new HashMap<String, String>();
	    row1.put("name","a1");row1.put("email","b1");
	    row2.put("name","a2");row2.put("email","b2");
	    row3.put("name","a3");row3.put("email","b3");
		arRow.add(row1 );
		arRow.add(row2 );
		arRow.add(row3 );
	       
		ArrayList<String> header = new ArrayList<String>();
		header.add("name");header.add("email");
		root.put("headers", header);
		root.put("myrows", arRow);
		
		
		 String jsonPath="C:/Users/Samarjit/Desktop/demo/demo/web/cardOps/satWFailedRecords.json";
		  BufferedReader br = new BufferedReader(new FileReader(jsonPath));
			String jsonstr = "";
			String line;
			while ((line = br.readLine()) != null) {
				jsonstr += line;
			}
		   HashMap<String, HashMap<String,HashMap<String,Object>>> json = new Gson().fromJson(jsonstr, HashMap.class);
//			JSONArray rowHeader = json.get("data").getJSONObject("jobsTrack").getJSONArray("header");
//			JSONArray rowModel = json.getJSONObject("data").getJSONObject("jobsTrack").getJSONArray("model");
//			String width = json.getJSONObject("data").getJSONObject("jobsTrack").getString("width");
//			JSONArray jar = json.getJSONObject("data").getJSONObject("jobsTrack").getJSONArray("datalist");
//			String title = json.getJSONObject("data").getJSONObject("jobsTrack").getString("title");
			ArrayList rowHeader = (ArrayList) json.get("data").get("jobsTrack").get("header");
			ArrayList rowModel = (ArrayList) json.get("data").get("jobsTrack").get("model");
			String width = (String) json.get("data").get("jobsTrack").get("width");
			ArrayList jar = (ArrayList) json.get("data").get("jobsTrack").get("datalist");
			String title = (String) json.get("data").get("jobsTrack").get("title");
			String[] widtharr = width.split(",");
			
			root.put("rowmodel", rowModel);
			root.put("headers", rowHeader);
			root.put("myrows", jar);
			
        /* Merge data-model with template */
        Writer out = new OutputStreamWriter(System.out);
        temp.process(root, out);
        out.flush();
        
        
        //////String template /////
//        StringWriter str = new StringWriter();
//        
//        temp.process(root, str); 
//        out.flush();
//        System.out.println(str.toString());
        
    }
    
    
	public String getString(String key) {
		ResourceBundle rb = ResourceBundle.getBundle(Constants.PATH_CONFIG);
		String ret = "";
		try {
			Template t = new Template("name", new StringReader(rb.getString(key)), new Configuration());
			StringWriter out = new StringWriter();
			t.process(rb, out );
			ret = out.toString();
		} catch (Exception e) {
			e.printStackTrace();
			ret = rb.getString(key);
		}
		return ret;
	}
}  