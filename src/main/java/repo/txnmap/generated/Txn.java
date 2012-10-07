
package repo.txnmap.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}rsp"/>
 *         &lt;element ref="{}req"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "rsp",
    "req"
})
@XmlRootElement(name = "txn")
public class Txn {

    @XmlElement(required = true)
    protected Rsp rsp;
    @XmlElement(required = true)
    protected Req req;
    @XmlAttribute
    protected String id;

    /**
     * Gets the value of the rsp property.
     * 
     * @return
     *     possible object is
     *     {@link Rsp }
     *     
     */
    public Rsp getRsp() {
        return rsp;
    }

    /**
     * Sets the value of the rsp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Rsp }
     *     
     */
    public void setRsp(Rsp value) {
        this.rsp = value;
    }

    /**
     * Gets the value of the req property.
     * 
     * @return
     *     possible object is
     *     {@link Req }
     *     
     */
    public Req getReq() {
        return req;
    }

    /**
     * Sets the value of the req property.
     * 
     * @param value
     *     allowed object is
     *     {@link Req }
     *     
     */
    public void setReq(Req value) {
        this.req = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
