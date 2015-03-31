
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
 *         &lt;element name="IpTAddMaterials" type="{urn:sap-com:document:sap:soap:functions:mc-style}ZttymmMatnrList" minOccurs="0"/>
 *         &lt;element name="IpTDeleteMaterials" type="{urn:sap-com:document:sap:soap:functions:mc-style}ZttymmMatnrList" minOccurs="0"/>
 *         &lt;element name="IpTitle" type="{urn:sap-com:document:sap:rfc:functions}char50" minOccurs="0"/>
 *         &lt;element name="IpUrl" type="{urn:sap-com:document:sap:rfc:functions}char4096"/>
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
    "ipTAddMaterials",
    "ipTDeleteMaterials",
    "ipTitle",
    "ipUrl"
})
@XmlRootElement(name = "ZusMmFmMaintainMatUrl")
public class ZusMmFmMaintainMatUrl {

    @XmlElement(name = "IpTAddMaterials")
    protected ZttymmMatnrList ipTAddMaterials;
    @XmlElement(name = "IpTDeleteMaterials")
    protected ZttymmMatnrList ipTDeleteMaterials;
    @XmlElement(name = "IpTitle")
    protected String ipTitle;
    @XmlElement(name = "IpUrl", required = true)
    protected String ipUrl;

    /**
     * Gets the value of the ipTAddMaterials property.
     * 
     * @return
     *     possible object is
     *     {@link ZttymmMatnrList }
     *     
     */
    public ZttymmMatnrList getIpTAddMaterials() {
        return ipTAddMaterials;
    }

    /**
     * Sets the value of the ipTAddMaterials property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZttymmMatnrList }
     *     
     */
    public void setIpTAddMaterials(ZttymmMatnrList value) {
        this.ipTAddMaterials = value;
    }

    /**
     * Gets the value of the ipTDeleteMaterials property.
     * 
     * @return
     *     possible object is
     *     {@link ZttymmMatnrList }
     *     
     */
    public ZttymmMatnrList getIpTDeleteMaterials() {
        return ipTDeleteMaterials;
    }

    /**
     * Sets the value of the ipTDeleteMaterials property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZttymmMatnrList }
     *     
     */
    public void setIpTDeleteMaterials(ZttymmMatnrList value) {
        this.ipTDeleteMaterials = value;
    }

    /**
     * Gets the value of the ipTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpTitle() {
        return ipTitle;
    }

    /**
     * Sets the value of the ipTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpTitle(String value) {
        this.ipTitle = value;
    }

    /**
     * Gets the value of the ipUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpUrl() {
        return ipUrl;
    }

    /**
     * Sets the value of the ipUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpUrl(String value) {
        this.ipUrl = value;
    }

}
