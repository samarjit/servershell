package servershell.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdRunner {

	private static Logger logger = LoggerFactory.getLogger(CmdRunner.class);
	
	static class StreamGobbler extends Thread
	{
	    InputStream is;
	    String type;
	    
	    StreamGobbler(InputStream is, String type)
	    {
	        this.is = is;
	        this.type = type;
	    }
	    
	    public void run()
	    {
	        try
	        { 
	        	logger.debug("in run!");
	            InputStreamReader isr = new InputStreamReader(is);
	            BufferedReader br = new BufferedReader(isr);
	            String line=null;
	            	while ( (line = br.readLine()) != null){
		            	if(type.equals("ERROR")){
		            		logger.error(type + ">" + line);
		        		}else{
		        			logger.info(type + ">" + line);
		            	}
	        		}
	            	
				final byte[] buffer = new byte[1];
				for (int length = 0; (length = is.read(buffer)) != -1;) {
					System.out.write(buffer, 0, length);
				} 
				br.close(); 
	            } catch (IOException ioe)
	              {
	                ioe.printStackTrace();  
	              }
	    }
	}
	
	public static int process(String cmd){
		int exitVal = 0; 
		try {
			Runtime rt = Runtime.getRuntime();
//			System.out.println("Running cmd:"+cmd); 
			logger.info("Running cmd:"+cmd); 
			Process proc = rt.exec(cmd);
			 // any error message?
			 StreamGobbler errorGobbler = new 
			     StreamGobbler(proc.getErrorStream(), "ERROR");            
			 
			 // any output?
			 StreamGobbler outputGobbler = new 
			     StreamGobbler(proc.getInputStream(), "OUTPUT");
			 PrintWriter pr = new PrintWriter(proc.getOutputStream());
			 InputStreamReader in = new InputStreamReader(System.in);
//			 Scanner scan = new Scanner(System.in);
			
			 
			 // kick them off
			 errorGobbler.start();
			 outputGobbler.start();
			                     
			 int i =0;
			 while((i = in.read())!= 'q'){
				 pr.write(i);
				 pr.flush();
				 System.out.print((char)i);
			 }
			 pr.append("y\r\n");
			 
			 // any error???
			 logger.debug("Waiting for cmd process to complete " );
//			 System.out.println("Waiting for cmd process to complete " );
			exitVal = proc.waitFor();
			 logger.info("ExitValue: " + exitVal);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}    
		return exitVal;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
			String gzipCmd = "sh";// "gpg.exe --batch --passphrase-file C:/Eclipse/workspace2/EzLinkBE/src/sAtw.passphrase --decrypt --output C:/Eclipse/workspace2/sync_inbox/MTR241_20111124.htm C:/Eclipse/workspace2/sync_inbox/MTR241_20111124.htm.gpg";
			CmdRunner.process(gzipCmd);
	}

}
