package servershell;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		float x = 9.15f;
		int y = Math.round(x*100);
		System.out.println(y);
		HashMap<String,String> cards = new HashMap<String,String>();
		List<String> ln = FileUtils.readLines(new File("C:\\work\\EZLink\\PAssion\\ONEPAYCS_120725.csv -import2ndRound.csv"));
		String str = "";
		FileWriter fr = new FileWriter("c:\\Users\\Samarjit\\Desktop\\sortedcards.txt");
		String cardlist = "'1009710001466184', '1009710000650090', '1009710000767899', '1009710000808450', '1009710003902480'";
		for (String line : ln) {
			String s[] = line.split(",");
			str = null;
			
			if(s.length == 5){
				str = s[4];
				cards.put(str, line);
			}else{
//				System.out.println(line);
			}
			
			
			
			if(cardlist.indexOf(str) > 0)
				System.out.println(line);
//			if(cards.containsKey(str) && str != null){
//				System.out.println(cards.get(str)+"  "+line);
//			}
			
		}
		
		for (String string : cards.keySet()) {
			fr.write(string+"\r\n");
		}
		fr.close();
	}

}
