package com.ycs.fe.interceptor;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;
import com.ycs.fe.HTMLProcessor;
import com.ycs.fe.HTMLProcessorJsoupImpl;
import com.ycs.fe.strutsresult.XMLResult;
import com.ycs.fe.util.CharResponseWrapper;
 
public class DecoratorInterceptor implements Interceptor {
 
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(this.getClass());
	
	private HttpServletResponse responseParent;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		logger.debug("DecoratorInterceptor:Intercepted ... ");
		final ActionContext context = invocation.getInvocationContext ();
		//HttpServletRequest request = (HttpServletRequest) context.get(ServletActionContext.HTTP_REQUEST);
		 responseParent = (HttpServletResponse) context.get(ServletActionContext.HTTP_RESPONSE);
		 CharResponseWrapper wrapper = new CharResponseWrapper(responseParent);
		 ServletActionContext.setResponse(wrapper);
//		ExampleXSLTAction action = (ExampleXSLTAction)invocation.getAction();
//		 System.out.print(action.getName());
//		 loger.debug(action.getRetrievename());
		 HTMLProcessor processor = new HTMLProcessorJsoupImpl();
		 invocation.addPreResultListener(new PreResultListener() {
             public void beforeResult(ActionInvocation invocation, String resultCode) {
                 // perform operation necessary before Result execution
            	 HTMLProcessor preprocess = new HTMLProcessorJsoupImpl();
				
						preprocess.populateValueStack(invocation, resultCode);
//            	 try {
//            		 
////            		 responseParent = (HttpServletResponse) ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
////            		 responseParent = ServletActionContext.getResponse();
////            		 PrintWriter out = responseParent.getWriter();				
////					 out.write("hello this should come first");
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
            	  
             }
		 });
		 logger.debug("DecoratorInterceptor:Before invocation.invoke");
		 String result = null;
		 try{
			result = invocation.invoke();
		 }catch(Exception e){
			 logger.error("YCS: invocation caused exception, do not know how to handle!");
		 }
		 logger.debug("DecoratorInterceptor:After invocation.invoke result of invocation:"+result);      
//			logger.debug( "DecoratorInterceptor:request.getContentLength() expecting first element <root>:"+wrapper.toString());
			String resulthtml = null;
			if(invocation.getResult()== null){
				logger.debug("YCS: Try to figure out if this is normal! invocation result being null");
			}else if(XMLResult.class.getName().equals(invocation.getResult().getClass().getCanonicalName())){
				resulthtml = processor.process(wrapper.toString(), invocation);
			}
			
			
			//CharResponseWrapper newrsp = new CharResponseWrapper(wrapper);
			PrintWriter out = responseParent.getWriter();
			CharArrayWriter car = new CharArrayWriter();
			logger.debug("HTMLProcessor should it be processed?"+XMLResult.class.getName().equals(invocation.getResult().getClass().getCanonicalName())+" then isProcessed:"+processor.getLastResult());
			if(XMLResult.class.getName().equals(invocation.getResult().getClass().getCanonicalName())){
				if(resulthtml!= null && processor.getLastResult())
					car.write(resulthtml);
				else{
					car.write("<h3>Error</h3><p>Some errors in creating result page</p>");	
				}
			}else{
				car.write(wrapper.toString());  //fallthrough for other than custom result types like freemarker, jsp, vm, html
			}
			
//			car.write("TODO: hello from DecoratorInterceptor <a href='index.jsp'>index</a>");
					    
			responseParent.setContentLength(car.toString().length());
			out.write(car.toString());
			out.flush();
			
//		 loger.debug("DecoratorInterceptor:..ending interceptor "+ action.getName()+","+action.getRetrievename());
		 logger.debug("DecoratorInterceptor:..ending");
		 return result;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
