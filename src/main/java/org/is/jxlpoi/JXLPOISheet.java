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

import java.util.Vector;



import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class JXLPOISheet {

	private JXLPOIWorkbook workbook;	
	private Sheet sheet; 
	
	
	JXLPOISheet(Sheet sheet, JXLPOIWorkbook workbook){
		this.sheet = sheet;
		this.workbook = workbook;
	}
	
	
	public JXLPOICell getCell(int colIndex, int rowIndex){
		
		Row row = sheet.getRow(rowIndex);
		if(row==null)
			return new JXLPOICell(null, null, colIndex, rowIndex);;
		Cell cell = row.getCell(colIndex);
		JXLPOICell c = new JXLPOICell(workbook, cell, colIndex, rowIndex);
		return c;
	}
	
	
	public JXLPOICell getCell(String coordinate){
		
		Point p = Utils.getCellCoordinate(coordinate);
		return getCell(p.x, p.y);
	}	
	
	
	public JXLPOICell[] getColumn(int colIndex){
		
		int lastRowNum = sheet.getLastRowNum();
		
		Vector<JXLPOICell> collected = new Vector<JXLPOICell>();
		for(int rowIndex=0; rowIndex<=lastRowNum; rowIndex++){
			Row row = sheet.getRow(rowIndex);
			JXLPOICell cs;
			if(row!=null){
				int numCells = row.getLastCellNum();
				if(numCells<colIndex+1){ //no cell
					cs = new JXLPOICell(null, null, colIndex, rowIndex);
				}
				else{
					Cell cell = row.getCell(colIndex);
					cs = new JXLPOICell(workbook, cell, colIndex, rowIndex);
				}
			}
			else{
				cs = new JXLPOICell(null, null, colIndex, rowIndex);
			}
			
			collected.add(cs);
		}
		
		JXLPOICell[] cs = new JXLPOICell[collected.size()];
		collected.copyInto(cs);
		return cs;
	}
	

	public JXLPOICell[] getRow(int rowIndex){
		Row row = sheet.getRow(rowIndex);
		if(row==null){
			return new JXLPOICell[0];
		}
		int numCells = row.getLastCellNum();
		JXLPOICell[] cs = new JXLPOICell[numCells];
		for(int colIndex=0; colIndex<cs.length; colIndex++){
			Cell cell = row.getCell(colIndex);
			cs[colIndex]=new JXLPOICell(workbook, cell, colIndex, rowIndex);
		}
		return cs;
	}
	
	
	public void addCell(int colIndex, int rowIndex, String label){
		Row row = sheet.getRow(rowIndex);
		if(row==null)
			row=sheet.createRow(rowIndex);
		Cell cell = row.getCell(colIndex);
		if(cell==null)
			cell=row.createCell(colIndex);
		cell.setCellValue(label);
	}
	
	
	public int getRows(){
		return sheet.getLastRowNum()+1;
	}
	
	
	public String getName(){
		return sheet.getSheetName();
	}
	
	
}
