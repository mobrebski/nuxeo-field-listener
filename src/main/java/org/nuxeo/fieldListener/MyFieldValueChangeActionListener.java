package org.nuxeo.fieldListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MyFieldValueChangeActionListener extends FieldValueChangeActionListener {
	
    private static final Log log = LogFactory.getLog(MyFieldValueChangeActionListener.class);

    protected void fieldCreated(){
    	log.info("field created");
    };

    protected void fieldRemoved(){
    	log.info("field modified");    	
    };

    protected void fieldModified(){
    	log.info("field modified");
    };

}
