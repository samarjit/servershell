package servershell.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

import com.ycs.be.cache.AppCacheManager;

public class ResourceBundleReloader {

	 public static void reloadBundles()  
     {  
         try  
         {  
        	 AppCacheManager.removeCache("xmlcache");
             clearMap(ResourceBundle.class, null, "cacheList");   
             clearTomcatCache();  
         }  
         catch (Exception e)  
         {  
             System.out.println("Could not reload resource bundles"+e.getMessage());  
         }  
     }  


     private static void clearTomcatCache()  
     {  
         ClassLoader loader = Thread.currentThread().getContextClassLoader();  
         // no need for compilation here.  
         Class cl = loader.getClass();  

         try  
         {  
             if ("org.apache.catalina.loader.WebappClassLoader".equals(cl.getName()))  
             {  
                 clearMap(cl, loader, "resourceEntries");  
             }  
             else  
             {  
                 System.out.println("class loader " + cl.getName() + " is not tomcat loader.");  
             }  
         }  
         catch (Exception e)  
         {  
             System.out.println("couldn't clear tomcat cache"+e.getMessage());  
         }  
     }  


	private static void clearMap(Class cl, Object obj, String name)   
	     throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {   
	     Field field = cl.getDeclaredField(name);   
	     field.setAccessible(true);   
	
	     Object cache = field.get(obj);   
	     Class ccl = cache.getClass();   
	     Method clearMethod = ccl.getMethod("clear", null);   
	     clearMethod.invoke(cache, null);   
	 }

}
