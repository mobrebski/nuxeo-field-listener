package org.nuxeo.sirona.sap.test;

import java.io.Serializable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.sirona.sap.connector.ServiceConnector;
import org.nuxeo.sirona.sap.connector.ServiceConnectorAdmin;
import org.nuxeo.sirona.sap.connector.test.LogingTestConnector;

import com.google.inject.Inject;

@Features(CoreFeature.class)
@RunWith(FeaturesRunner.class)
@Deploy({"nuxeo-sirona-sap","studio.extensions.mobrebski-SANDBOX"})
public class TestFieldChangeListeners {

    @Inject
    CoreSession session;

    @Inject
    EventService eventService;

    @Before
    public void cleanup() {
        LogingTestConnector.clear();
    }

    @Test
    public void testServicesDeclared() throws Exception {

        ServiceConnectorAdmin sca = Framework.getService(ServiceConnectorAdmin.class);
        Assert.assertNotNull(sca);

        ServiceConnector sc = Framework.getService(ServiceConnector.class);
        Assert.assertNotNull(sc);

        System.out.println(sc.getClass().getCanonicalName());


        Assert.assertNotNull(session);
    }


    @Test
    public void testCreate() throws Exception {

        DocumentModel doc = session.createDocumentModel("/", "myDoc", "SironaDoc");
        doc.setPropertyValue("dc:title", "Sirona");
        doc.setPropertyValue("sironadoc:sap_part_number", new String[]{"A", "B"});
        doc = session.createDocument(doc);

        session.save();


        //TransactionHelper.commitOrRollbackTransaction();

        Thread.sleep(100);
        eventService.waitForAsyncCompletion();

        Serializable prop = LogingTestConnector.getAfter().get(doc.getId());
        Assert.assertNotNull(prop);


    }
}
