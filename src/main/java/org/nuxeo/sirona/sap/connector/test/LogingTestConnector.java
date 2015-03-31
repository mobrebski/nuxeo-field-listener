package org.nuxeo.sirona.sap.connector.test;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientRuntimeException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.sirona.sap.connector.ServiceConnector;

public class LogingTestConnector implements ServiceConnector {

    protected static Map<String, Serializable> before = new HashMap<String, Serializable>();
    protected static Map<String, Serializable> after = new HashMap<String, Serializable>();

    @Override
    public void notify(DocumentModel doc, Serializable propertyValueBefore,
            Serializable propertyValueAfter) throws ClientRuntimeException {

        before.put(doc.getId(), propertyValueBefore);
        after.put(doc.getId(), propertyValueAfter);
    }

    public static Map<String, Serializable> getBefore() {
        return before;
    }

    public static Map<String, Serializable> getAfter() {
        return after;
    }

    public static void clear() {
        before.clear();
        after.clear();
    }
}
