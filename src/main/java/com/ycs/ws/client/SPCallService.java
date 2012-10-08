
package com.ycs.ws.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "SPCallService", targetNamespace = "http://ws.plsqlcall/", wsdlLocation = "http://localhost:9090/WS/SPCall?wsdl")
public class SPCallService
    extends Service
{

    private final static URL SPCALLSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(com.ycs.ws.client.SPCallService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = com.ycs.ws.client.SPCallService.class.getResource(".");
            url = new URL(baseUrl, "http://localhost:9090/WS/SPCall?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://localhost:9090/WS/SPCall?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        SPCALLSERVICE_WSDL_LOCATION = url;
    }

    public SPCallService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SPCallService() {
        super(SPCALLSERVICE_WSDL_LOCATION, new QName("http://ws.plsqlcall/", "SPCallService"));
    }

    /**
     * 
     * @return
     *     returns SPCall
     */
    @WebEndpoint(name = "SPCallPort")
    public SPCall getSPCallPort() {
        return super.getPort(new QName("http://ws.plsqlcall/", "SPCallPort"), SPCall.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SPCall
     */
    @WebEndpoint(name = "SPCallPort")
    public SPCall getSPCallPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://ws.plsqlcall/", "SPCallPort"), SPCall.class, features);
    }

}
