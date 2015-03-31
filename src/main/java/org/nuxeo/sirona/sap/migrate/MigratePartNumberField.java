/**
 *
 */

package org.nuxeo.sirona.sap.migrate;

import java.util.Arrays;
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

/**
 * @author mikeobrebski
 */
@Operation(id = MigratePartNumberField.ID, category = Constants.CAT_DOCUMENT, label = "Migrate SAP part numbers", description = "Migrate sap_part_number String fields to sap_part_numbers multi value Strings on SironaDoc types")
public class MigratePartNumberField {

    public static final String ID = "MigratePartNumberField";

    private static final Log log = LogFactory.getLog(MigratePartNumberField.class);

    @Context
    protected CoreSession session;

    @OperationMethod
    public String run() {
        log.warn("MigratePartNumberField");


        // Get all SironaDoc types
        DocumentModelList documentList;
        String query = "SELECT * FROM SironaDocument";
        try {
            documentList = session.query(query);
            log.warn(documentList.size()
                    + " SironaDoc documents found. Processing...");
        } catch (ClientException e) {
            log.error("Error Converting");
            e.printStackTrace();
            return "Error converting";
        }

        // For each Doc
        Iterator<DocumentModel> iter = documentList.iterator();
        while (iter.hasNext()) {
            try {
                // Parse sap_part_number into string[]
                DocumentModel document = iter.next();
                log.warn(document.getId());
                String partNumber = (String) document.getPropertyValue("sironadoc:sap_part_number");
                if (partNumber == null || partNumber.isEmpty()) {
                    log.warn(document.getTitle()
                            + " - No sap_part_number value - skipping");
                } else {
                    String[] partNumbers = partNumber.replaceAll("\\s+","").split(",");

                    log.warn(document.getTitle() + " - " + partNumber + " -> "
                            + Arrays.deepToString(partNumbers));

                    document.setPropertyValue("sironadoc:sap_part_numbers",
                            partNumbers);
                    session.saveDocument(document);
                }

            } catch (Exception e) {
                log.error("Error converting");
                e.printStackTrace();
            }
        }

        return "Successfully converted " + documentList.size()
                + " SironaDoc documents";
    }

}
