/**
 *
 */

package org.nuxeo.fieldListener;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.LifeCycleConstants;
import org.nuxeo.ecm.core.api.event.CoreEventConstants;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.runtime.api.Framework;


/**
 * @author mikeobrebski
 */
public class FieldValueChangeCaptureListener implements EventListener {


    private static final Log log = LogFactory.getLog(FieldValueChangeCaptureListener.class);

    //DocTypes to watch
    public static final String DOCTYPE = "Document";

    //Fields to watch
    public static final String FIELD_NAME = "dc:created";

    //Not in Event Constants
    private static final String LIFECYCLE_TRANSITION_EVENT = "lifecycle_transition_event";

    private EventProducer eventProducer;
    private EventContext ctx;
    private DocumentModel currentDoc;
    private DocumentModel prevDoc;

    @Override
    public void handleEvent(Event event) throws ClientException {


        ctx = event.getContext();
        if (!(ctx instanceof DocumentEventContext)) {
            return;
        }


        currentDoc = ((DocumentEventContext) ctx).getSourceDocument();
        if (currentDoc == null || !currentDoc.getType().equals(DOCTYPE) || currentDoc.isVersion()) {
            log.debug("Exiting -"+currentDoc.getName()+"-"+currentDoc.getType());
            return;
        }
        if (currentDoc.getProperty(FIELD_NAME)==null ) {
            log.warn("Field \""+FIELD_NAME+"\" doesn't exist for document "+currentDoc.getName()+" of type \""+DOCTYPE+"\"");
            return;
        }
        if  ( !(currentDoc.getPropertyValue(FIELD_NAME) instanceof String[]) ){
            log.warn("Field \""+FIELD_NAME+"\" is not a String[] in "+currentDoc.getName()+" of type \""+DOCTYPE+"\"");
            return;
        }


        if (event.getName().equals(DocumentEventTypes.BEFORE_DOC_UPDATE)){
            log.debug(event.getName());
            init();
            modified();
        } else if (event.getName().equals(DocumentEventTypes.ABOUT_TO_REMOVE)){
            log.debug(event.getName());
            init();
            removed();
        } else if (event.getName().equals(DocumentEventTypes.DOCUMENT_CREATED)){
            log.debug(event.getName());
            init();
            created();
        } else if (event.getName().equals(LIFECYCLE_TRANSITION_EVENT)){
            init();
            lifecycleDelete();
        } else if ( event.getName().equals(DocumentEventTypes.BEFORE_DOC_RESTORE) ||
                event.getName().equals(DocumentEventTypes.DOCUMENT_RESTORED) ){
            log.debug(event.getName());
            init();
            restored(event);
        }
    }

    /*
     * Process Soft Delete and Restore by Lifecycle transition and fire fieldRemovedEvent or fieldCreatedEvent
     */
    private void lifecycleDelete() throws ClientException{
        // Check for delete or Undelete
        String transition = (String)ctx.getProperty(LifeCycleConstants.TRANSTION_EVENT_OPTION_TRANSITION);
        if (transition.equals(LifeCycleConstants.DELETE_TRANSITION)){
            log.debug(LifeCycleConstants.DELETE_TRANSITION);
            //Send field value change event
            ctx.setProperty(FieldValueChangeActionListener.FIELD_NAME_KEY, FIELD_NAME);
            ctx.setProperty(FieldValueChangeActionListener.PREV_VALUES, currentDoc.getPropertyValue(FIELD_NAME));
            eventProducer.fireEvent(ctx.newEvent(FieldValueChangeActionListener.FIELD_REMOVED_EVENT));
        } else if (transition.equals(LifeCycleConstants.UNDELETE_TRANSITION)) {
            log.debug(LifeCycleConstants.UNDELETE_TRANSITION);
            ctx.setProperty(FieldValueChangeActionListener.FIELD_NAME_KEY, FIELD_NAME);
            ctx.setProperty(FieldValueChangeActionListener.NEW_VALUES, currentDoc.getPropertyValue(FIELD_NAME));
            eventProducer.fireEvent(ctx.newEvent(FieldValueChangeActionListener.FIELD_CREATED_EVENT));
        } else {
            log.debug("Non-delete transition");
        }
    }

    /*
     * Process Restore Version event for field value modification and fire fieldModifiedEvent
     */
    private void restored(Event event) throws ClientException{

        if (event.getName().equals(DocumentEventTypes.BEFORE_DOC_RESTORE)) {
            ctx.setProperty(FieldValueChangeActionListener.FIELD_NAME_KEY, FIELD_NAME);
            ctx.setProperty(FieldValueChangeActionListener.PREV_VALUES, currentDoc.getPropertyValue(FIELD_NAME));
        } else {
            String[] prevValues = (String[])ctx.getProperty(FieldValueChangeActionListener.PREV_VALUES);
            String[] newValues = (String[])currentDoc.getPropertyValue(FIELD_NAME);
            if ( isSame( newValues, prevValues )) {
                log.debug("No Change in field");
                return;
            }
            ctx.setProperty(FieldValueChangeActionListener.PREV_VALUES, null);
            ctx.setProperty(FieldValueChangeActionListener.NEW_VALUES, newValues);
            eventProducer.fireEvent(ctx.newEvent(FieldValueChangeActionListener.FIELD_CREATED_EVENT));
        }
    }

    /*
     * Process Create Document event and fire fieldCreatedEvent
     */
    private void created() throws ClientException{

        //Send field value created event
        String[] values = (String[])currentDoc.getPropertyValue(FIELD_NAME);
        if ( values.length < 1 ) {
            return;
        }

        ctx.setProperty(FieldValueChangeActionListener.FIELD_NAME_KEY, FIELD_NAME);
        ctx.setProperty(FieldValueChangeActionListener.NEW_VALUES, values);
        eventProducer.fireEvent(ctx.newEvent(FieldValueChangeActionListener.FIELD_CREATED_EVENT));
    }

    /*
     * Process Removed event for permanent delete and fire fieldRemovedEvent
     */
    private void removed() throws ClientException{
        //Permanent Delete
        //Only if deleted directly without going through deleted state
        if (!currentDoc.getCurrentLifeCycleState().equals(LifeCycleConstants.DELETED_STATE)){
            //Send field value change event
            ctx.setProperty(FieldValueChangeActionListener.FIELD_NAME_KEY, FIELD_NAME);
            ctx.setProperty(FieldValueChangeActionListener.PREV_VALUES, currentDoc.getPropertyValue(FIELD_NAME));
            eventProducer.fireEvent(ctx.newEvent(FieldValueChangeActionListener.FIELD_REMOVED_EVENT));
        }
    }

    /*
     * Process BeforeModified Event to compare field value change and fire fieldModifiedEvent
     */
    private void modified() throws ClientException{
        prevDoc = (DocumentModel)ctx.getProperty(CoreEventConstants.PREVIOUS_DOCUMENT_MODEL);

        String[] prevValues = (String[])prevDoc.getPropertyValue(FIELD_NAME);
        String[] newValues = (String[])currentDoc.getPropertyValue(FIELD_NAME);
        if ( isSame( prevValues, newValues )) {
            log.debug("No Change in "+FIELD_NAME);
            return;
        }

        //Send field value change event
        ctx.setProperty(FieldValueChangeActionListener.FIELD_NAME_KEY, FIELD_NAME);
        ctx.setProperty(FieldValueChangeActionListener.PREV_VALUES, prevValues);
        ctx.setProperty(FieldValueChangeActionListener.NEW_VALUES, newValues);
        eventProducer.fireEvent(ctx.newEvent(FieldValueChangeActionListener.FIELD_MODIFIED_EVENT));
    }

    private void init() throws ClientException{
        try {
            eventProducer = Framework.getService(EventProducer.class);
        } catch (Exception e) {
            log.error("Cannot get EventProducer", e);
            throw new ClientException(e);
        }

     }

    private boolean isSame(String[] a, String[] b){
        return Arrays.deepEquals(a, b);
    }
}
