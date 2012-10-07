package com.ycs.be.dto; 
  
 import java.util.ArrayList;
import java.util.Date;
  
 public class PrepstmtDTOArray    { 
 private ArrayList<PrepstmtDTO> ardto =null; 
  
 public ArrayList<PrepstmtDTO> getArdto() { 
         return ardto; 
 } 
 public PrepstmtDTOArray(){ 
         ardto = new ArrayList<PrepstmtDTO>(); 
 } 
 public void add( PrepstmtDTO.DataType dt,String data){ 
         ardto.add(new PrepstmtDTO(dt,data));     
 } 
 public void add( PrepstmtDTO.DataType dt,Integer data){ 
	 ardto.add(new PrepstmtDTO(dt,String.valueOf(data)));     
 } 
 public void add( PrepstmtDTO.DataType dt,Float data){ 
	 ardto.add(new PrepstmtDTO(dt,String.valueOf(data)));     
 } 
 public void add( PrepstmtDTO.DataType dt,Double data){ 
	 ardto.add(new PrepstmtDTO(dt,String.valueOf(data)));     
 } 
 public void add( PrepstmtDTO.DataType dt,Long data){ 
	 ardto.add(new PrepstmtDTO(dt,String.valueOf(data)));     
 } 
 public void add( PrepstmtDTO.DataType dt,Byte data){ 
	 ardto.add(new PrepstmtDTO(dt,String.valueOf(data)));     
 } 
 public void add( PrepstmtDTO.DataType dt,Short data){ 
	 ardto.add(new PrepstmtDTO(dt,String.valueOf(data)));     
 } 
 
 public void add( PrepstmtDTO.DataType dt,Date data){ 
	 String format = "";
	 switch(dt){
	 case DATEDDMMYYYY: format= PrepstmtDTO.DATEDDMMYYYY_FORMAT; break;
	 case DATE_NS: format= PrepstmtDTO.DATE_NS_FORMAT; break;
	 case DATE_TIME_MIN: format= PrepstmtDTO.DATE_TIME_MIN_FORMAT; break;	  
	 case TIMESTAMP:	format= PrepstmtDTO.DATE_NS_FORMAT; break; 
	 }
	 ardto.add(new PrepstmtDTO(dt, PrepstmtDTO.getDateStringFormat(data, format)));     
 } 
 
 public String   toString(String SQL){ 
         String retval = "PerpstmtDTOArray:"; 
         //String[] sqlar = (SQL+" ").split("\\?"); 
         String[] sqlar = SQL.split("\\?"); 
         int i =0 ; 
         for ( i =0 ;i< ardto.size();i++) { 
                 PrepstmtDTO itr =  ardto.get(i); 
                 retval = retval + sqlar[i] +" '"+itr.getData() +"'|"+itr.getTypeString(); 
         } 
         if(i+1 == sqlar.length) 
         retval = retval + sqlar[i]; 
         return retval; 
 } 
  
  
 } 
 