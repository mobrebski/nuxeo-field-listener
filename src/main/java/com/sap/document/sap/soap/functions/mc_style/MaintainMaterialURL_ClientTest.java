package com.sap.document.sap.soap.functions.mc_style;

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;

public final class MaintainMaterialURL_ClientTest {


    private static final String SAP_PASSWORD = "";
    private static final String SAP_USERNAME = "";
    private static final QName PORT_NAME = new QName("urn:sap-com:document:sap:soap:functions:mc-style", "binding");
    private static final QName SERVICE_NAME = new QName("urn:sap-com:document:sap:soap:functions:mc-style", "maintain_material_url");
    private static final String ENDPOINT_ADDRESS = "http://localhost:8081/sap/bc/srt/rfc/sap/zus_websrv_maintain_mat_url/700/maintain_material_url/binding";
    private static final String SOAP_PREFIX = "soapenv-sap";

    public static void main(String args[]) throws java.lang.Exception {



        Service service = Service.create(SERVICE_NAME);

        // Add a port to the Service
        service.addPort(PORT_NAME, SOAPBinding.SOAP11HTTP_BINDING, ENDPOINT_ADDRESS);

        ZusWebsrvMaintainMatUrl port = service.getPort(PORT_NAME, ZusWebsrvMaintainMatUrl.class);

        Client client= ClientProxy.getClient(port);
        HTTPConduit http = (HTTPConduit)client.getConduit();

        http.getAuthorization().setUserName(SAP_USERNAME);
        http.getAuthorization().setPassword(SAP_PASSWORD);

        Binding binding = ((BindingProvider)port).getBinding();
        List<Handler> handlerList = binding.getHandlerChain();
        handlerList.add(new EnvelopeFixHandler(SOAP_PREFIX));
        binding.setHandlerChain(handlerList);


        {
            System.out.println("Invoking zusMmFmMaintainMatUrl...");
            //Set List objects
            String[] added = {"add-1", "add-2", "add-3"};
            String[] removed = {"rem-1", "rem-2", "rem-3"};
            ZttymmMatnrList addedNumbers = new ZttymmMatnrList();
            ZttymmMatnrList removedNumbers = new ZttymmMatnrList();
            if (added != null && added.length>0 ){
                ZsmmMatnrList tempAdd;
                List<ZsmmMatnrList> addedList = addedNumbers.getItem();
                for (int i = 0; i < added.length; i++) {
                    tempAdd = new ZsmmMatnrList();
                    tempAdd.setMatnr(added[i]);
                    addedList.add(tempAdd);
                }

            }
            if (removed != null && removed.length>0) {
                ZsmmMatnrList tempRem = new ZsmmMatnrList();
                List<ZsmmMatnrList> removedList = removedNumbers.getItem();
                for (int i = 0; i < removed.length; i++) {
                    tempRem = new ZsmmMatnrList();
                    tempRem.setMatnr(removed[i]);
                    removedList.add(tempRem);
                }

            }
            String ipTitle = "title_test";
            String ipUrl = "http://linkback.com/linback_test";
            Bapiret2 response = port.zusMmFmMaintainMatUrl(addedNumbers, removedNumbers, ipTitle, ipUrl);
            System.out.println("zusMmFmMaintainMatUrl.result=" + response);

        }

        System.exit(0);
    }

}
