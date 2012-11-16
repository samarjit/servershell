package servershell;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReadClob {

	/** 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println("Usage: ReadClob <cardno>");
		}
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.100.217:1521:vizdev01", "ezlink", "ezlink");

		PreparedStatement ps = conn.prepareStatement("select IMAGE_DATA1 from amex_kyc_file_track  where card_no='" + args[0] + "'");

		File file = new File(args[0]+".data1");
		
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			oracle.sql.CLOB clobValue = (oracle.sql.CLOB) rs.getClob(1);
			BufferedReader reader = new BufferedReader(new InputStreamReader(clobValue.getAsciiStream()));
			String read = null;
			FileWriter buffer = new FileWriter(file);
			while ((read = reader.readLine()) != null) {
				buffer.write(read);
			}
			buffer.close();
			
			
			System.out.println("Data retrieved and written to :"+file.getAbsolutePath());
		}
		
		System.out.println("end..");
		rs.close();
		ps.close();
		conn.close();
	}

}
