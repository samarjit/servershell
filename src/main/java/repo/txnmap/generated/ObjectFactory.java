
package repo.txnmap.generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the repo.txnmap.generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Single_QNAME = new QName("", "single");
    private final static QName _Multiple_QNAME = new QName("", "multiple");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: repo.txnmap.generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Rsp }
     * 
     */
    public Rsp createRsp() {
        return new Rsp();
    }

    /**
     * Create an instance of {@link Req }
     * 
     */
    public Req createReq() {
        return new Req();
    }

    /**
     * Create an instance of {@link Root }
     * 
     */
    public Root createRoot() {
        return new Root();
    }

    /**
     * Create an instance of {@link Txn }
     * 
     */
    public Txn createTxn() {
        return new Txn();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "single")
    public JAXBElement<String> createSingle(String value) {
        return new JAXBElement<String>(_Single_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "multiple")
    public JAXBElement<String> createMultiple(String value) {
        return new JAXBElement<String>(_Multiple_QNAME, String.class, null, value);
    }

}
