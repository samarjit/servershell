package servershell.fe.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.is.jxlpoi.JXLPOISheet;
import org.is.jxlpoi.JXLPOIWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CreateXL2 {

private static Logger logger = Logger.getLogger(CreateXL2.class);

    public volatile static int tableid;
    
	public static void normalize(Element elm){
//		for (Element sib = null;((sib = elm.nextElementSibling()) != null); ){
//			if(sib.tagName().equals("table")){
//				normalize(sib);
//			}
//		}
		
		Elements tbl = elm.select(">table");
		for (Element tblelm : tbl) {
//			System.out.println(elm.id()+ " tr "+ elm.getElementsByTag("tr").size());
			System.out.println(tblelm.tagName()+" "+tblelm.id());
			Elements trs = tblelm.select(">tbody>tr");
			
			int siz = trs.size();
			System.out.println("trsize:"+siz);
			for (Element element : trs.parents()) {
				System.out.print(">"+element.tagName());
				if(element.tagName().equals("tr")){
					String strPrevrowspan = element.attr("rowspan");
					int prevrowspan = (strPrevrowspan !=null && !"".equals(strPrevrowspan))?Integer.parseInt(strPrevrowspan):1;
					element.attr("rowspan",String.valueOf(prevrowspan+siz-1));
				}
			}
			System.out.println();
			
			for(Element tr:trs){
				Elements tds = tr.select(">td");
				
				
				for (Element td : tds) {
//					Elements innerTbl = td.select("table:first");
					
					normalize(td);
				}
			}
		} 
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File htmlfile = new File("C:\\Users\\Samarjit\\Desktop\\Upsell_files\\tble2xls_1.html");
		String html;
		try {
			html = FileUtils.readFileToString(htmlfile);
			Document jsoup =   Jsoup.parse(html);
			
			Elements body = jsoup.getElementsByTag("body");
			int tblcols =1;
			int maxcols =1;
			int maxrows = 0;
			int yOffset = 0;
			//CreateXL2.normalize(body.get(0));
			
			
			JXLPOIWorkbook workbook = JXLPOIWorkbook.createWorkbook(new File("C:\\Users\\Samarjit\\Desktop\\Upsell_files\\output.xls"));
			JXLPOISheet sheet = workbook.createSheet("First Sheet", 0);
			
//			Elements table1 = body.select(">table:eq(0)");
			Elements tables1 = body.select(">table");
//			Element tab1 = table1.get(0);
			for(Element tab1:tables1){
				
				maxcols = 0;
				maxrows = 0;
				//calculate maxrow maxcols 
				Elements calctrs = tab1.select(">tbody>tr");
				maxrows = calctrs.size() * 2;
				Elements calctds = tab1.select(">tbody>tr:eq(0)>td");
				for (Element calctd : calctds) {
					String strcalctd = calctd.attr("colspan");
					maxcols += (strcalctd != null && !"".equals(strcalctd))?Integer.parseInt(strcalctd):1;
				}
				maxcols = maxcols * 2;
				System.out.println(tab1.id()+" size:"+maxrows/2+"x"+maxcols/2);
				//memory allocation and initialization
				ArrayList <ArrayList<HashMap<String,String>>> tab = new ArrayList<ArrayList<HashMap<String,String>>>();
				for(int j = 0 ; j < maxrows; j++){
					ArrayList<HashMap<String,String>> row1 = new ArrayList<HashMap<String,String>>();  
					for(int i = 0 ;i < maxcols; i++){
						HashMap<String, String> cell = new HashMap<String, String>();
						row1.add(cell );
					}
					tab.add(row1);
				}
				
				
				
				Elements trs = tab1.select(">tbody>tr");
				int _irow = 0;
				int _icell = 0;
				boolean handled = false;
				for (Element trow : trs) {
					Elements tcells = trow.select(">td");
					_icell = 0;
					for (Element tcell : tcells) {
						String strrowspan = tcell.attr("rowspan");
						
						String strcolspan = tcell.attr("colspan");
						int rowspan = (strrowspan != null && !"".equals(strrowspan))?Integer.parseInt(strrowspan):0;
						int colspan = (strcolspan != null && !"".equals(strcolspan))?Integer.parseInt(strcolspan):0;
						
						String strmrowspan = tab.get(_irow).get(_icell).get("rowspan");
						String strmcolspan = tab.get(_irow).get(_icell).get("colspan");
						
						int mrowspan =  (strmrowspan != null && !"".equals(strmrowspan))?Integer.parseInt(strmrowspan):0;
						int mcolspan =  (strmcolspan != null && !"".equals(strmcolspan))?Integer.parseInt(strmcolspan):0;
						System.out.println("_icell="+_icell+" _irow="+_irow+" mrowspan="+mrowspan+" mcolspan="+mcolspan+" text="+tcell.text());
						handled = false;
						
						if(mcolspan > 0)_icell = _icell + mcolspan;
						
						if(rowspan >1 && colspan >1){
							tab.get(_irow).get(_icell).put("text",tcell.text());handled = true;
							int _icell_temp = 0;
							for(int i = 0;i<rowspan ; i++){
								 _icell_temp = _icell;
								 for(int y=colspan;y >0;y --){
									tab.get(_irow+i).get(_icell_temp).put("rowspan",String.valueOf(rowspan -i));
									tab.get(_irow+i).get(_icell_temp).put("colspan",String.valueOf(y));
									//System.out.println("_irow="+(_irow+i)+" _icell="+_icell_temp+" colspan="+y);
									_icell_temp ++;
								}
								
								
							}
							_icell = _icell_temp - 1;
						}else
						if(rowspan >1){
								tab.get(_irow).get(_icell).put("text",tcell.text());handled = true;
								System.out.println("rowspan:"+rowspan);
								for(int i = 0;i<rowspan ; i++){
									tab.get(_irow+i).get(_icell).put("rowspan",String.valueOf(rowspan));
								}
								
						}else 
						if(colspan >1){
								tab.get(_irow).get(_icell).put("text",tcell.text());handled = true;						
								for(int y = colspan+mcolspan;y >0;y --){
									tab.get(_irow).get(_icell).put("colspan",String.valueOf(y));
									_icell ++;
								}
								_icell --;
								
								
						}else{
	//						if(tab.get(_irow).get(_icell).get("text") == null)
	//						tab.get(_irow).get(_icell).put("text","skip "+tcell.text());
							//skip if non zero
						}
						
						if(mrowspan == 0 && mcolspan == 0 && handled == false){
							tab.get(_irow).get(_icell).put("text",tcell.text());
						}else if(handled == false && mrowspan > 0){
							int tmpmcolspan = -1;
							int tmpmrowspan = -1;
							while(tmpmrowspan != 0 || tmpmcolspan != 0){
							_icell = (mcolspan>0)?mcolspan+_icell+1:_icell+1;
							
							strmrowspan = tab.get(_irow).get(_icell).get("rowspan");
							strmcolspan = tab.get(_irow).get(_icell).get("colspan");
							
							tmpmrowspan = (strmrowspan != null && !"".equals(strmrowspan))?Integer.parseInt(strmrowspan):0;
							tmpmcolspan = (strmcolspan != null && !"".equals(strmcolspan))?Integer.parseInt(strmcolspan):0;
							}
							
							tab.get(_irow).get(_icell).put("text",tcell.text()); handled = true;
						}
						_icell++;
					}
					_irow++;
				}
				
				
				for (int j = 0; j < maxrows; j++) {
					ArrayList<HashMap<String, String>> row1 = new ArrayList<HashMap<String, String>>();
					for (int i = 0; i < maxcols; i++) {
						HashMap<String, String> cell = new HashMap<String, String>();
						System.out.println("x=" + i + " y=" + j + " rspan=" + tab.get(j).get(i).get("rowspan") + " cspan=" + tab.get(j).get(i).get("colspan") + " val=" + tab.get(j).get(i).get("text"));

						sheet.addCell(i, j+yOffset, tab.get(j).get(i).get("text"));

					}
					tab.add(row1);
				}
				
				yOffset += maxrows /2 +3;
			}
			//System.out.println(body);
			
			FileOutputStream fos = new FileOutputStream("C:\\Users\\Samarjit\\Desktop\\Upsell_files\\test.html");
			workbook.write();
			workbook.close();
			
			fos.close();
			//fos.write(jsoup.html().getBytes());
			
		}catch(Exception e){
			logger.error("",e);
		}
	}

}
