package com.ycs.be.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CreateTableSQL {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Enter table as in excel, when completed enter ; to end reading lines :\r\n");
		BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));	
		String s = "";
		String output = "create table ";
		String drop = "drop table ";
		boolean first = true;
		boolean second = false;
		while(!(s = bfr.readLine()).equals(";")){
			if(first == true){
				output += s +" (\r\n";
				drop += s + ";\r\n";
				first = false;
				second = true;
			}else{
				output += s;
			
				if(second == true){
					output += " PRIMARY KEY ";
				   second = false;
				}
				output += ",\r\n";
			}
		}
		  
		String res  = output.substring(0,output.length()-3)+");";
		System.out.println(drop);
		System.out.println(res);
	}

}
