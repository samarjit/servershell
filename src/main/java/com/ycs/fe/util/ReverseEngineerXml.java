package com.ycs.fe.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import com.ycs.fe.dao.DBConnector;

public class ReverseEngineerXml {

	public void reverseEng() throws Exception{
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		 Statement st = con.createStatement();



		  String sql = "select * from DEAL_MASTER"; 

		  ResultSet rs = st.executeQuery(sql);

		  ResultSetMetaData metaData = rs.getMetaData();



		  int rowCount = metaData.getColumnCount();



		  System.out.println("Table Name : " + metaData.getTableName(2));

		  System.out.println("Field  \tsize\tDataType");



		  for (int i = 0; i < rowCount; i++) {

		  System.out.print(metaData.getColumnName(i + 1) + "  \t");

		  System.out.print(metaData.getColumnDisplaySize(i + 1) + "\t");

		  System.out.println(metaData.getColumnTypeName(i + 1));

		  } 

		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new ReverseEngineerXml().reverseEng();
	}

}
