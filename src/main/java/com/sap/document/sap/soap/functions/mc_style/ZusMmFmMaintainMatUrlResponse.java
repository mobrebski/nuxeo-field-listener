
package com.sap.document.sap.soap.functions.mc_style;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="OpReturn" type="{urn:sap-com:document:sap:soap:functions:mc-style}Bapiret2"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "opReturn"
})
@XmlRootElement(name = "ZusMmFmMaintainMatUrlResponse")
public class ZusMmFmMaintainMatUrlResponse {

    @XmlElement(name = "OpReturn", required = true)
    protected Bapiret2 opReturn;

    /**
     * Gets the value of the opReturn property.
     * 
     * @return
     *     possible object is
     *     {@link Bapiret2 }
     *     
     */
    public Bapiret2 getOpReturn() {
        return opReturn;
    }

    /**
     * Sets the value of the opReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bapiret2 }
     *     
     */
    public void setOpReturn(Bapiret2 value) {
        this.opReturn = value;
    }

}
