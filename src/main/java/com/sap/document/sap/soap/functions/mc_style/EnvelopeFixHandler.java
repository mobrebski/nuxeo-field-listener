package com.sap.document.sap.soap.functions.mc_style;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class EnvelopeFixHandler implements SOAPHandler<SOAPMessageContext> {

    private String SOAP_PREFIX;

    public EnvelopeFixHandler(String prefix) {
        SOAP_PREFIX = prefix;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext messageContext) {

        if (SOAP_PREFIX == null) {
            return true;
        }
        try{
            SOAPEnvelope soapEnvelope = messageContext.getMessage().getSOAPPart().getEnvelope();
            soapEnvelope.setPrefix(SOAP_PREFIX);
            soapEnvelope.removeNamespaceDeclaration("soap");
            messageContext.getMessage().getSOAPBody().setPrefix(SOAP_PREFIX);;
            messageContext.getMessage().getSOAPHeader().setPrefix(SOAP_PREFIX);

            messageContext.getMessage().saveChanges();
        } catch (SOAPException e) {
            throw new ProtocolException(e);
        }


        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext messageContext) {
        return true;
    }

    @Override
    public void close(MessageContext context) {
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

}
