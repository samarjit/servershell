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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
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
	
	public static void main(String[] args) {
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
			
			int [] colwidthspx = new int[maxcols];
			int eachcolwidth = totalwidthpx/maxcols;
			for (int i =0 ; i < colwidthspx.length; i++) {
				colwidthspx[i] = eachcolwidth * (i ); 
			}
			 
			
			int [] colheightsspx = new int[maxrows];
			 
			int eachrowheight = totalheightpx/maxcols;
			for (int i =0 ; i < colheightsspx.length; i++) {
				colheightsspx[i] = eachrowheight * (i ); 
			}
			
			
			for (int i : colwidthspx) {
				System.out.println("x="+i);
			}
			//find cell
			List<String> lines = FileUtils.readLines(new File("C:\\Users\\Samarjit\\Desktop\\Upsell_files\\intermediate.txt"));
			
			Workbook wb = new HSSFWorkbook();
			Sheet sheet1 = wb.createSheet("new sheet");
			Map<Integer, Row> rowMap = new HashMap<Integer, Row>(); 
			for (int i : colheightsspx) {
				System.out.println("y="+i);
				rowMap.put(i,sheet1.createRow(i));
			}
			Row row = sheet1.createRow(1);
			CellUtil.createCell(row, 1, "samarjit");
			
			
			for (String string : lines) {
				String[] parts = string.split(",");
				
				int x =Integer.parseInt(parts[0]);
				int y = Integer.parseInt(parts[1]);
				String text = parts[2];
				
				int whichrow = 0;
				for (int i= 0; i < colheightsspx.length && (y > colheightsspx[i]); i++) {
						whichrow = i;
				}
				int whichcol = 0;
				for (int i= 0; i < colwidthspx.length && (x > colwidthspx[i]); i++) {
					whichcol = i;
				}
				
				if(!rowMap.containsKey(whichrow)){
					rowMap.put(whichrow,sheet1.createRow(whichrow));
				}
				String val = CellUtil.getCell(rowMap.get(whichrow), whichcol).getStringCellValue();
				if(val == null)val = "";
				
				
				Cell c = CellUtil.createCell(rowMap.get(whichrow),whichcol,val+""+text);
				CellStyle cs = c.getRow().setHeight(height);
				
				System.out.println("whichrow = "+whichrow+" whichcol="+whichcol);
				
			
			}////
			
			FileOutputStream fos = new FileOutputStream("C:\\Users\\Samarjit\\Desktop\\Upsell_files\\test.xls");
			wb.write(fos);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
