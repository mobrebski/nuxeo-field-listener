/**
 *
 */

package org.nuxeo.sirona.sap.migrate;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.sirona.sap.connector.ServiceConnector;

/**
 * @author mikeobrebski
 */
@Operation(id=BulkSyncSAPPartNumbers.ID, category=Constants.CAT_DOCUMENT, label="BulkSyncSAPPartNumbers", description="")
public class BulkSyncSAPPartNumbers {

    public static final String ID = "BulkSyncSAPPartNumbers";

    private static final Log log = LogFactory.getLog(BulkSyncSAPPartNumbers.class);

    @Context
    protected CoreSession session;

    @OperationMethod
    public String run() {

     // Get all SironaDoc types
        DocumentModelList documentList;
        String query = "SELECT * FROM SironaDocument";
        try {
            documentList = session.query(query);
            log.warn(documentList.size()
                    + " SironaDoc documents found. Processing SAP Sync...");
        } catch (ClientException e) {
            log.error("Error Getting SironaDoc types");
            e.printStackTrace();
            return "Error syncing";
        }

        // For each Doc
        Iterator<DocumentModel> iter = documentList.iterator();
        while (iter.hasNext()) {
            try {
                // Parse get part_numbers []
                DocumentModel document = iter.next();
                String[] partNumbers = (String[]) document.getPropertyValue("sironadoc:sap_part_numbers");
                if (partNumbers == null || partNumbers.length < 1) {
                    log.warn(document.getTitle()
                            + " - No sap_part_numbers - skipping");
                } else {
                    Framework.getService(ServiceConnector.class).notify(document, null, partNumbers);
                }
            } catch (Exception e) {
                log.error("Error Synchronizing");
                e.printStackTrace();
            }
        }

        return "Successfully Bulk SAP synced " + documentList.size()
                + " SironaDoc documents";
    }

}
