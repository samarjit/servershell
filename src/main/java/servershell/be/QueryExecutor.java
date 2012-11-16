package servershell.be;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.sql.rowset.CachedRowSet;

import oracle.jdbc.rowset.OracleCachedRowSet;

import org.apache.log4j.Logger;

import servershell.be.dao.BackendException;
import servershell.be.dao.DBConnector;
import servershell.be.dto.ResultDTO;

import com.google.gson.Gson;

public class QueryExecutor  {
	private static Logger logger = Logger.getLogger(QueryExecutor.class);
	private String globalSQL = "  SELECT *  FROM customer_master";
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

	public String getXmlmapDataTypeName(String dbimplType, boolean scale){
		if(dbimplType.equals("VARCHAR2"))return "STRING";
		if(dbimplType.equals("VARCHAR"))return "STRING";
		if(dbimplType.equals("DATE"))return "DATE_NS";
		if(dbimplType.equals("TIMESTAMP"))return "TIMESTAMP";
		
		if(dbimplType.equals("NUMBER") || dbimplType.equals("DECIMAL")  ){
			if(scale)return "FLOAT";
			else
			return "INT";
		}
		if(dbimplType.equals("INTEGER"))return "INT";
		if(dbimplType.equals("FLOAT"))return "FLOAT";
		if(dbimplType.equals("DOUBLE"))return "DOUBLE";
		if(dbimplType.equals("LONG"))return "LONG";
		if(dbimplType.equals("BIGINT"))return "LONG";
		
		return "STRING";
	}
	public String querySelect(String sql) throws Exception {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		Statement st = con.createStatement();

//		String sql = globalSQL;
//		StringBuffer inputSql = new StringBuffer();//new BufferedReader(new InputStreamReader(System.in)).readLine();
//		int ch = -1;
//		System.out.println("Enter a query followed by ';' or simply enter ';' and press <enter>\r\n:");
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
		if(tableName == null || tableName.equals("")){
			tableName = metaData.getTableName(0);
		}
		StringBuffer sb = new StringBuffer();
//		if(tableName == null || tableName.equals("")){
//			System.out.println("Enter Table Name(in CAPS): followed by ';'<enter>");
//			while((ch = System.in.read())!= ';'){
//				sb.append((char)ch);
//			}
//			if(sb.length() >0){
//				tableName= sb.toString().trim();
//			}
//			 
//		}
		System.out.println("Table Name : " + tableName);
		System.out.println("Field  \tsize\tDataType");
		String screenName = toProperCase(tableName).replaceAll(" ", "");
		for (int i = 0; i < rowCount; i++) {
			columnName = metaData.getColumnName(i + 1);
			 
			arcol.add(columnName);
			col = toProperCase(columnName);
			col = col.replaceAll("\\_", " ");
			arheader.add(col);
			alias = col.toLowerCase().replaceAll(" ", "");
			aralias.add(alias);
			size = metaData.getColumnDisplaySize(i+1);//metaData.getColumnDisplaySize(i + 1);
			scale= metaData.getScale(i+1);
			ardatatype.add(getXmlmapDataTypeName(metaData.getColumnTypeName(i + 1), scale != 0));
			arcolprecision.add(size);
			
			System.out.print(metaData.getColumnName(i + 1) + "  \t");
			System.out.print(metaData.getColumnDisplaySize(i + 1) + " ==? " + metaData.getPrecision(i+1)+"\t");
			System.out.println(metaData.getColumnTypeName(i + 1)+" , "+metaData.getColumnType(i + 1));
		}
		
		CachedRowSet crs =  new OracleCachedRowSet();
		crs.populate(rs);
		HashMap<String,Object> jr = null;
		List<HashMap<String,Object>> jrs = new ArrayList<HashMap<String,Object>>();
		List row = null;
		int count = 0;
		while(crs.next()){
			jr = new HashMap<String,Object>();
			row = new ArrayList();
			for (int i = 0; i < rowCount; i++) {
				jr.put(arcol.get(i), crs.getString(i+1));
//				row.add(crs.getString(i+1));
			}
			jr.put("row",count);
//			jr.put("data", row);
			jrs.add(jr);
			count ++;
		}
		
		System.out.println(new Gson().toJson(jrs).toString());
		rs.close();
		crs.close();
		
		if(con!= null){
			con.close();
		}
		
		return jrs.toString();
	}
	
	public String queryParser(String sqlquery) throws BackendException{
		int countrec = -1;
		DBConnector  dbconn = new DBConnector();
		ResultDTO resultDTO = new ResultDTO();
		if(sqlquery!= null && sqlquery.trim().length() >0 ){
	    	List<Map<String, String>> values = new ArrayList<Map<String, String>>();
	    	Map<String, String> row ; 
		    	
			if(sqlquery.matches("(?ims:select)[\\S\\s]*(?ims:from)[\\S\\s]*")){
	
			}else if(sqlquery.matches("[\\S\\s]*(?ims:insert)[\\S\\s]*(?ims:into)[\\S\\s]*")){
				countrec = dbconn.executeUpdate(sqlquery);
				resultDTO.addMessage("SUCCESS:"+String.valueOf(countrec));
			}else if(sqlquery.matches("[\\S\\s]*(?ims:update|delete)[\\S\\s]*(?ims:where)[\\S\\s]*")){
				countrec = dbconn.executeUpdate(sqlquery);
				resultDTO.addMessage("SUCCESS:"+String.valueOf(countrec));
			
			}else{
				logger.debug("invalid query skipping..."+sqlquery);
				 
				 resultDTO.addError("ERROR:Invalid Query");
				 //return resultDTO; ? how it worked before , coz' even then Invalid query came!
			}
		}
		
		return resultDTO.toString();
	}
	
	public static void main(String[] args) throws Exception{
		QueryExecutor queryExecutor = new QueryExecutor();
		queryExecutor.querySelect(null);
	}
}