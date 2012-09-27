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

import java.awt.Point;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class Utils {

	 private final static String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	 
	
	 /**
	  * Transforms spreadsheet's horizontal coordinates A, AA, EF etc - into zero-based
	  * column numbers: 0, 27, 135 etc
	  */
	 public static int excelColumn(String dim){
		 if(dim.length()==1)
			 return dim.charAt(0)-65;
		 return (dim.charAt(0)-64)*26 + dim.charAt(1)-65;
	 }
	 

	 /**
	  * Transforms column number into spreadsheet's horizontal coordinate
	  * (reverse to the previous method)
	  */
	 public static String excelColumn(int col) throws Exception{

		 if(col<26)
			 return Character.toString(LETTERS.charAt(col));

		 int firstLetter = (int)col/26;
		 if(firstLetter>=26)
			 throw new Exception("Too many columns");
		 
		 int secondLetter = col%26;
		 	
		 return Character.toString(LETTERS.charAt(firstLetter))+Character.toString(LETTERS.charAt(secondLetter));
	 }
	 
	 
	 public static Point getCellCoordinate(String spreadsheetCoord){
		 Point coordinate = new Point();
		 if(Character.isDigit(spreadsheetCoord.charAt(1))){
			 //one-letter
			 coordinate.x=excelColumn(spreadsheetCoord.substring(0, 1));
			 coordinate.y=Integer.parseInt(spreadsheetCoord.substring(1))-1;
		 }
		 else{
			 //2 letters
			 coordinate.x=excelColumn(spreadsheetCoord.substring(0, 2));
			 coordinate.y=Integer.parseInt(spreadsheetCoord.substring(2))-1;
		 }
		 return coordinate;
	 }
	 
	 
	 /**
	  * If integer is presented as a float (with 0s after decimal point) - parse as well
	  */
	 public static int parseInt(String number){
		 try{
			 return Integer.parseInt(number);
		 }
		 catch(NumberFormatException nfe){
			 int dec = number.indexOf('.');
			 if(dec!=-1){
				 number=number.substring(0, dec);
				 return Integer.parseInt(number);
			 }
			 throw nfe;
		 }
	 }
	 
}
