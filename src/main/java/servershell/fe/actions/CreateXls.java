package servershell.fe.actions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@ParentPackage("default")
@Results(value={
		@Result(name="success",type="stream")
		,@Result(name="json",type="json",params={"contentType","text/html","ignoreHierarchy","false","includeProperties","jobj.*,actionErrors.*,actionMessages.*,fieldErrors.*"})
		,@Result(type="json", name="input",params={"contentType","text/html","ignoreHierarchy","false","includeProperties","jobj.*,actionErrors.*,actionMessages.*,fieldErrors.*"})
})
public class CreateXls extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private JSONObject jobj;
	public InputStream inputStream;
	public String message;
	public String contentType;
	public String contentDisposition;
	
	@Action(value="createxls")
	@Validations(requiredFields={ @RequiredFieldValidator(fieldName="message", message="message is required field")})
	public String createxls() throws IOException{
		if(message == null)addActionError("message cannot be null");
		boolean workbookSuccess = false;
		try {
			Workbook wb = new HSSFWorkbook();
			Sheet sheet1 = wb.createSheet("new sheet");
			
			Row row = sheet1.createRow(1);
			CellUtil.createCell(row, 1, "samarjit");
			CellUtil.createCell(row, 2, "POI is bad");
			
			ByteArrayOutputStream s = new ByteArrayOutputStream();
			wb.write(s);
			contentType = "application/vnd.ms-excel";
			contentDisposition = "filename=\"document.xls\"";
			inputStream = new  ByteArrayInputStream(s.toByteArray());
			workbookSuccess = true;
		} catch (Exception e) {
			addActionError("Unknown Exception occured in CreateXls action "+e.toString());
		}
		
		System.out.println("message="+message);
		
		return (workbookSuccess)?"success":"json";
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public JSONObject getJobj() {
		return jobj;
	}

	public void setJobj(JSONObject jobj) {
		this.jobj = jobj;
	}
	
	public static void main(String[] args) throws DocumentException {
		File htmlfile = new File("C:\\Users\\Samarjit\\Desktop\\Upsell_files\\tble2xls_1.html");
		String html;
		try {
			html = FileUtils.readFileToString(htmlfile);
			Document jsoup =   Jsoup.parse(html);
			Elements tbl = jsoup.getElementsByTag("table");
			int tblcols =1;
			int maxcols =1;
			int maxrows = 0;
			
			for (Element elm : tbl) {
//				System.out.println(elm.id()+ " tr "+ elm.getElementsByTag("tr").size());
				tblcols = elm.getElementsByTag("tr").first().children().size();
				maxcols = (tblcols > maxcols)?tblcols: maxcols;
				
				maxrows +=  elm.getElementsByTag("tr").size();
			
			} 
			
			maxcols = 7;
			maxrows = 9;
			System.out.println("Max cols:" +maxcols); //7 column
			System.out.println("Max Rows:" +maxrows); //7 column
			int totalwidthpx = 600;
			int totalheightpx = 320;
			SAXReader saxReader = new SAXReader();
			saxReader.setValidation(false);
			org.dom4j.Document document = saxReader.read("C:\\Users\\Samarjit\\Desktop\\Upsell_files\\intermediate-xml.html");
//			Document ohtml =  Jsoup.parse(FileUtils.readFileToString(new File("C:\\Users\\Samarjit\\Desktop\\Upsell_files\\intermediate-xml.html")),"");
//			Document ohtml =   org.jsoup.parser.Parser.parseBodyFragmentRelaxed(FileUtils.readFileToString(new File("C:\\Users\\Samarjit\\Desktop\\Upsell_files\\intermediate-xml.html")),"" );
//			 Parser parser = new Parser(FileUtils.readFileToString(new File("C:\\Users\\Samarjit\\Desktop\\Upsell_files\\intermediate-xml.html")), "", false);
//		        parser.relaxed = true;
			 
			 
			
//			int eachcolwidth = totalwidthpx/maxcols;
//			for (int i =0 ; i < colwidthspx.length; i++) {
//				colwidthspx[i] = eachcolwidth * (i ); 
//			}
			 
			
			
			 
			int eachrowheight = totalheightpx/maxcols;
//			for (int i =0 ; i < colheightsspx.length; i++) {
//				colheightsspx[i] = eachrowheight * (i ); 
//			}
			
			org.dom4j.Element rootHtml = document.getRootElement();//children(); //outer tables
			List<org.dom4j.Node> tables = rootHtml.selectNodes("/Xdiv/html/body//table/table/tbody/tr");
			System.out.println(tables.size());
//			System.out.println(tables.nextElementSibling().nextElementSibling());
			List<org.dom4j.Node> longtr = null;
			List<org.dom4j.Node> trs = null;
			int maxtr = 0;
			
			int [] colheightsspx = new int[tables.size()];
			
			for (int i = 0;i< tables.size();i++) {
				org.dom4j.Node tblelm = tables.get(i); 
				org.dom4j.Element tr = (org.dom4j.Element) tblelm;
				trs = tr.selectNodes("td");
				if(maxtr < trs.size()){
					maxtr = trs.size();
					longtr = trs;
				}
				 System.out.println(tr.attributeValue("style"));
				 String[] styleparts = tr.attributeValue("style").split(",");
					 colheightsspx [i] = Integer.parseInt( styleparts[3] ); 
			}
			
			int [] colwidthspx = new int[longtr.size()]; 
			System.out.println("maxtr="+maxtr);
			for(int i = 0; i < longtr.size(); i++){
				org.dom4j.Element td = (org.dom4j.Element) longtr.get(i);
				String[] styleparts = td.attributeValue("style").split(",");
				colwidthspx[i] = Integer.parseInt(styleparts[2]);
				System.out.println("x="+colwidthspx[i]);
			}
//			colheightsspx[0]=99;
//			colheightsspx[1]=151;
//			colheightsspx[2]=180;
//			colheightsspx[3]=207;
//			colheightsspx[4]=234;
//			colheightsspx[5]=263;
//			colheightsspx[6]=290;
//			colheightsspx[7]= 344;
//			colheightsspx[8]= 373;
			
			 
			//find cell
			
			
			List<String> lines = FileUtils.readLines(new File("C:\\Users\\Samarjit\\Desktop\\Upsell_files\\intermediate.txt"));
			Workbook wb = new HSSFWorkbook();
			Sheet sheet1 = wb.createSheet("new sheet");
			Map<Integer, Row> rowMap = new HashMap<Integer, Row>(); 
			for (int i =0; i< colheightsspx.length ;i++) {
				System.out.println("y="+i+" "+colheightsspx[i]);
				Row row =  sheet1.createRow(i);
				if(i <1){
					row.setHeight((short)(colheightsspx[i]*15));
				}else{
					row.setHeight((short)((colheightsspx[i]-colheightsspx[i-1])*15));
				}
				rowMap.put(i,row);
			}
			Row row = sheet1.createRow(1);
			CellUtil.createCell(row, 1, "samarjit");
			
			
			Map<String,Font> fontMap = new HashMap<String, Font>();
			for (String string : lines) {
				String[] parts = string.split(",");
				
				int x =Integer.parseInt(parts[0]);
				int y = Integer.parseInt(parts[1]);
				String fontfamily  = parts[3]; 
				String fontsize  = parts[4]; 
				String isBold = parts[5];
				String text = parts[6];
				
				int whichrow = 0;
				for (int i= 0; i < colheightsspx.length && (y > colheightsspx[i]); i++) {
						whichrow = i+1;
				}
				int whichcol = 0;
				for (int i= 0; i < colwidthspx.length && (x > colwidthspx[i]); i++) {
					whichcol = i+1;
				}
				
				if(!rowMap.containsKey(whichrow)){
					rowMap.put(whichrow,sheet1.createRow(whichrow));
				}
				String val = CellUtil.getCell(rowMap.get(whichrow), whichcol).getStringCellValue();
				if(val == null)val = "";
				
				
				Cell c = CellUtil.createCell(rowMap.get(whichrow),whichcol,val+""+text);
				if(whichcol ==0){
					sheet1.setColumnWidth(c.getColumnIndex(), (colwidthspx[whichcol])*30);
				}else{
					sheet1.setColumnWidth(c.getColumnIndex(), (colwidthspx[whichcol]-colwidthspx[whichcol-1])*30);
				}
				
				//font
				CellStyle cs = wb.createCellStyle();
				
				
				Font ft = null;
				if(fontMap.containsKey(fontsize+fontfamily+isBold)){
					ft = fontMap.get(fontsize+fontfamily+isBold);
				}else{
					Font fnt =  wb.createFont();
					fnt.setFontHeightInPoints((short)(Integer.parseInt(fontsize)-5));				
					fnt.setFontName(fontfamily);
					if(isBold.equals("true"))fnt.setBoldweight(Font.BOLDWEIGHT_BOLD);
					fontMap.put(fontsize+fontfamily+isBold, fnt);
					ft = fnt;
				}
				
				cs.setFont(ft);
				c.setCellStyle(cs);
				//font
				
//				System.out.println("x="+x+" y="+y+"whichrow = "+whichrow+" whichcol="+whichcol+"  "+ text+" "+c.getRow().getHeight());
				
			
			}////
			
			FileOutputStream fos = new FileOutputStream("C:\\Users\\Samarjit\\Desktop\\Upsell_files\\test.xls");
			wb.write(fos);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
