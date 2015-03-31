package org.nuxeo.sirona.sap.connector.sap;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.ClientRuntimeException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.sirona.sap.connector.ServiceConnector;

import com.sap.document.sap.soap.functions.mc_style.Bapiret2;
import com.sap.document.sap.soap.functions.mc_style.EnvelopeFixHandler;
import com.sap.document.sap.soap.functions.mc_style.ZsmmMatnrList;
import com.sap.document.sap.soap.functions.mc_style.ZttymmMatnrList;
import com.sap.document.sap.soap.functions.mc_style.ZusWebsrvMaintainMatUrl;


public class SAPConnector implements ServiceConnector {

    // SAP Web Service Properties


    private static final QName PORT_NAME = new QName(
            "urn:sap-com:document:sap:soap:functions:mc-style", "binding");

    private static final QName SERVICE_NAME = new QName(
            "urn:sap-com:document:sap:soap:functions:mc-style",
            "maintain_material_url");

    private static final String SOAP_PREFIX = "soapenv-sap";

    private static final Log log = LogFactory.getLog(SAPConnector.class);

    private static final String SAP_USERNAME = Framework.getProperty("sap.username");
    private static final String SAP_PASSWORD = Framework.getProperty("sap.password");
    private static final String ENDPOINT_ADDRESS = Framework.getProperty("sap.endpoint");
    private static final String TEST_MODE = Framework.getProperty("sap.testmode");


    @Override
    public void notify(DocumentModel document, Serializable propertyValueBefore,
            Serializable propertyValueAfter) throws ClientRuntimeException {

        // Build URL to Doc
        String documentURL = Framework.getProperty("nuxeo.url") + "/nxdoc/"
                + document.getRepositoryName() + "/" + document.getId()
                + "/view_documents";


        String[] added = (String[]) propertyValueAfter;
        String[] removed = (String[]) propertyValueBefore;

        try {
            if (TEST_MODE != null && TEST_MODE.equals("true")) {
                log.warn("Test Mode to SAP WS - document: "+document.getTitle()+ " - Removing: "+Arrays.deepToString(removed)+" - Adding: "+Arrays.deepToString(added));
                log.warn(" - URL: "+documentURL );
                return;
            }
        } catch (ClientException e1) {
            log.error("Failed accessing document");
            throw new ClientRuntimeException(e1);
        }

        Service service = Service.create(SERVICE_NAME);

        // Add a port to the Service
        service.addPort(PORT_NAME, SOAPBinding.SOAP11HTTP_BINDING,
                ENDPOINT_ADDRESS);

        ZusWebsrvMaintainMatUrl port = service.getPort(PORT_NAME,
                ZusWebsrvMaintainMatUrl.class);

        Client client = ClientProxy.getClient(port);
        HTTPConduit http = (HTTPConduit) client.getConduit();

        http.getAuthorization().setUserName(SAP_USERNAME);
        http.getAuthorization().setPassword(SAP_PASSWORD);

        Binding binding = ((BindingProvider) port).getBinding();
        List<Handler> handlerList = binding.getHandlerChain();
        handlerList.add(new EnvelopeFixHandler(SOAP_PREFIX));
        binding.setHandlerChain(handlerList);

        // Set List objects
        ZttymmMatnrList addedNumbers = new ZttymmMatnrList();
        ZttymmMatnrList removedNumbers = new ZttymmMatnrList();
        if (added != null && added.length > 0) {
            ZsmmMatnrList tempAdd;
            List<ZsmmMatnrList> addedList = addedNumbers.getItem();
            for (int i = 0; i < added.length; i++) {
                tempAdd = new ZsmmMatnrList();
                tempAdd.setMatnr(added[i]);
                addedList.add(tempAdd);
            }
        }
        if (removed != null && removed.length > 0) {
            ZsmmMatnrList tempRem = new ZsmmMatnrList();
            List<ZsmmMatnrList> removedList = removedNumbers.getItem();
            for (int i = 0; i < removed.length; i++) {
                tempRem = new ZsmmMatnrList();
                tempRem.setMatnr(removed[i]);
                removedList.add(tempRem);
            }
        }


        Bapiret2 response;
        try {

            log.debug("Invoking SAP WS - zusMmFmMaintainMatUrl");
            response = port.zusMmFmMaintainMatUrl(addedNumbers,
                    removedNumbers, document.getTitle(), documentURL);

            if( response.getType().equals("E")){
                log.error("SAP WS Error Response - "+response.getMessage() );
                log.error("Document - "+document.getTitle()+ " - "+documentURL);
                log.error("SAP Part Numbers - Add: "+added+ ", Remove: "+removed);
            } else {
                log.info("SAP Part Number sync - "+response.getMessage());
            }
        } catch (ClientException e) {
           log.error("SAP WS zusMmFmMaintainMatUrl failed");
           throw new ClientRuntimeException(e);
        }


    }

}
