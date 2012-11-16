package servershell;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class WriteBlob {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception {
		
		if(args.length <2 ){
			System.out.println("Usage: Test <cardno> <filename> [<filename2>] ");
		}
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.100.217:1521:vizdev01", "ezlink", "ezlink");
		
		PreparedStatement stmt = conn.prepareStatement("update amex_kyc_file_track  set IMAGE_DATA1=? ,IMAGE_DATA2=? ,IMAGE_FLAG='S' where card_no='"+args[0]+"'");
		
		File file = new File(args[1]);
		FileReader stringReader = new FileReader(file);
//		StringReader stringReader = new StringReader(s);
		stmt.setCharacterStream(  1,  stringReader , file.length());

		if(args.length == 3){
			File file2 = new File(args[2]);
			FileReader stringReader2 = new FileReader(file2);
	//		StringReader stringReader = new StringReader(s);
			stmt.setCharacterStream( 2,  stringReader2 , file2.length());
		}
		else{
			String s = "NO_DATA";
			StringReader stringReader3 = new StringReader(s);
			stmt.setCharacterStream( 2,  stringReader3 , s.length());
		}
		stmt.execute();
		
		System.out.println("Updated .. ");
		
		stmt.close();
		conn.close();
	}

}
