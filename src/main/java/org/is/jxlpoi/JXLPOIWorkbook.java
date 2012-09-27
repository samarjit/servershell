/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.is.jxlpoi;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class JXLPOIWorkbook {
	
	private Workbook workbook;
	
	//For newly created one:
	private boolean newlyCreated;
	private File file;
	

	private JXLPOIWorkbook(Workbook workbook){
		this.workbook=workbook;
	}

	
	public static JXLPOIWorkbook getWorkbook(File file) throws Exception{
		
		FileInputStream fis = new FileInputStream(file.getAbsolutePath());
		Workbook temp = new HSSFWorkbook(fis);//WorkbookFactory.create(fis);
    	fis.close();
    	
		JXLPOIWorkbook wb = new JXLPOIWorkbook(temp);
		return wb;
	}
	
	
	public static JXLPOIWorkbook getWorkbook(File file, JXLPOIWorkbookSettings settings) throws Exception{
		//HSSFWorkbook temp = Workbook.getWorkbook(file/*, settings.getWorkbookSettings()*/);
		return getWorkbook(file); //ignore WorkbookSettings for now (they used for suppress warnings only)
	}
	
	
	public static JXLPOIWorkbook createWorkbook(File file) throws Exception{

		//create in old format - not XSSF - since the old code could not create it anyway 
		Workbook temp = new HSSFWorkbook();
		
		JXLPOIWorkbook wb = new JXLPOIWorkbook(temp);
		wb.file=file;
		wb.newlyCreated=true;
		return wb;
	}	
	
	
	public String[] getSheetNames(){
    	int numSheets = workbook.getNumberOfSheets();
    	String[] names = new String[numSheets];
    	for(int i=0; i<numSheets; i++){
    		Sheet sheet = workbook.getSheetAt(i);
    		names[i]=sheet.getSheetName();
    	}
		return names;
	}
	
	
	public JXLPOISheet getSheet(String name){
		if(name==null)
			return null;
		Sheet sheet = workbook.getSheet(name);
		if(sheet==null)
			return null;
		JXLPOISheet sh = new JXLPOISheet(sheet, this);
		return sh;
	}
	

	public JXLPOISheet getSheet(int index){
		Sheet sheet = workbook.getSheetAt(index);
		if(sheet==null)
			return null;
		JXLPOISheet sh = new JXLPOISheet(sheet, this);
		return sh;
	}
	
	
	public void write() throws Exception{
		BufferedOutputStream out = null;
		try{
			out = new BufferedOutputStream(new FileOutputStream(file));
			workbook.write(out);
		}
		finally{
			try{out.close();}catch(Exception e){/*ignore*/}
		}
	}
	
	
	public void close(){
		//workbook.close();
	}
	
	
	public JXLPOISheet createSheet(String name, int index){
		Sheet sheet = workbook.createSheet(name);
		workbook.setSheetOrder(name, index);
		
		JXLPOISheet sh = new JXLPOISheet(sheet, this);
		return sh;
	}
	
	
	private static SimpleDateFormat fmter= new SimpleDateFormat("yyyy/MM/dd");
	private static DecimalFormat twoPlaces = new DecimalFormat("#.################");	
	private FormulaEvaluator formulaEvaluator=null; //will be created only if necessary
	
	
	public String getCellContentAsString(Cell c){
		
		if(c==null)
			return null;
		
		switch (c.getCellType()){
			case Cell.CELL_TYPE_BOOLEAN:
				if (c.getBooleanCellValue())
					return "Y";
				else
					return "N";
			case Cell.CELL_TYPE_NUMERIC:
				String result="";
				int datatype = c.getCellStyle().getDataFormat();

				String formatString = c.getCellStyle().getDataFormatString();
				if (datatype == 174 && "yyyy/mm/dd".equals(formatString)){
					java.util.Date date = c.getDateCellValue();
				   	return fmter.format(date);
				}
				else if(datatype==49 || datatype==0){
					int d = (int)c.getNumericCellValue();
					result = Integer.toString(d);
				}
				else{
					result = Double.toString(c.getNumericCellValue());
				}
				
				//return Double.toString(c.getNumericCellValue());
	//System.out.println(" number = "+c.getNumericCellValue()+" *** value ="+twoPlaces.format(c.getNumericCellValue())+"");
				
				//return twoPlaces.format(c.getNumericCellValue())+"";
				return result;
			case Cell.CELL_TYPE_STRING:
				return c.getStringCellValue();
			case Cell.CELL_TYPE_BLANK:
				return "";
			case Cell.CELL_TYPE_ERROR:
				return "#ERROR" + c.getErrorCellValue();
			case Cell.CELL_TYPE_FORMULA:
	
				String formulaCellValue;
				
				if(formulaEvaluator==null){
					formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
					//formulaEvaluator.setIgnoreFormulaException();
					//System.out.println(formulaEvaluator);
				}
				
				//formulaEvaluator.evaluateFormulaCell(c);
				//formulaEvaluator.evaluateInCell(c);
				
				CellValue cv = formulaEvaluator.evaluate(c);
				
				switch (cv.getCellType()) {
			    //switch (formulaEvaluator.evaluateInCell(c).getCellType()) {
			        case Cell.CELL_TYPE_BOOLEAN:
						if (cv.getBooleanValue())
							formulaCellValue = "Y";
						else
							formulaCellValue = "F";
			            break;
			        case Cell.CELL_TYPE_NUMERIC:
			        	formulaCellValue=Double.toString(cv.getNumberValue());
			            break;
			        case Cell.CELL_TYPE_STRING:
			        	formulaCellValue=cv.getStringValue();
			            break;
			        case Cell.CELL_TYPE_BLANK:
			        	formulaCellValue="";
			            break;
			        case Cell.CELL_TYPE_ERROR:
			        	formulaCellValue=Byte.toString(cv.getErrorValue());
			            break;
			        default:
			        	formulaCellValue=""; 
			        	break;
			    }//switch
			    
		   		return formulaCellValue;
	    	default:
	    		return "";
			}//switch

	}	
	


}
