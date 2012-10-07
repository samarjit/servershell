package com.ycs.fe;

import java.io.File;

 
public class Configuration {

	public void setDirectoryForTemplateLoading(File file) {
		// TODO Auto-generated method stub
		
	}

	public TemplateEngine getTemplate(String filePath) {
		File f = new File(filePath);
		if(f.exists())System.out.println("File path exists");
		else{
			System.out.println(f.getAbsolutePath());
		}
		return new TemplateEngine();
	}

	 

}
