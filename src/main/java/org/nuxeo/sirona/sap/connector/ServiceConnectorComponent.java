package org.nuxeo.sirona.sap.connector;

import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.DefaultComponent;
import org.nuxeo.sirona.sap.connector.sap.SAPConnector;
import org.nuxeo.sirona.sap.connector.test.LogingTestConnector;

public class ServiceConnectorComponent extends DefaultComponent implements ServiceConnectorAdmin {

    protected ServiceConnector connector;

    @Override
    public String getPropertyXPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDocumentType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T getAdapter(Class<T> adapter) {
        if (adapter.getCanonicalName().equals(ServiceConnector.class.getCanonicalName())) {
            return  adapter.cast(getConnector());
        }
        return super.getAdapter(adapter);
    }

    protected ServiceConnector getConnector() {

        if (Framework.isTestModeSet()) {
            return new LogingTestConnector();
        } else {
            if (connector==null) {
                connector = new SAPConnector();
            }
            return connector;
        }

    }



}
