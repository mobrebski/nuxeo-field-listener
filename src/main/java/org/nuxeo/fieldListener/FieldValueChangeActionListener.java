/**
 *
 */

package org.nuxeo.fieldListener;

import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.PostCommitEventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

/**
 * @author mikeobrebski
 */
abstract public class FieldValueChangeActionListener implements PostCommitEventListener {

    public static final String FIELD_NAME_KEY = "fieldName";

    public static final String NEW_VALUES = "newValues";

    public static final String PREV_VALUES = "prevValues";

    public static final String FIELD_REMOVED_EVENT = "fieldRemovedEvent";

    public static final String FIELD_CREATED_EVENT = "fieldCreatedEvent";

    public static final String FIELD_MODIFIED_EVENT = "fieldModifiedEvent";



    private static final Log log = LogFactory.getLog(FieldValueChangeActionListener.class);

    private Event event;

    private EventContext ctx;



    @Override
    public void handleEvent(EventBundle eventBundle) throws ClientException {


        Iterator<Event> bundleIterator = eventBundle.iterator();
        while (bundleIterator.hasNext()) {
            event = bundleIterator.next();

            ctx = event.getContext();
            if (!(ctx instanceof DocumentEventContext)) {
                return;
            }

            String eventType = event.getName();

            if (eventType.equals(FIELD_MODIFIED_EVENT)) {
                fieldModified();
            } else if (eventType.equals(FIELD_REMOVED_EVENT)) {
                fieldRemoved();
            } else if (eventType.equals(FIELD_CREATED_EVENT)) {
                fieldCreated();
            }

        }
    }

    abstract protected void fieldCreated();

    abstract protected void fieldRemoved();

    abstract protected void fieldModified();

}
