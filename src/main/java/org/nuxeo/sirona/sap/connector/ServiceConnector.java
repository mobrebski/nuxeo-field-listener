package org.nuxeo.sirona.sap.connector;

import java.io.Serializable;

import org.nuxeo.ecm.core.api.ClientRuntimeException;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface ServiceConnector {

    void notify(DocumentModel doc, Serializable propertyValueBefore, Serializable propertyValueAfter) throws ClientRuntimeException;
}
