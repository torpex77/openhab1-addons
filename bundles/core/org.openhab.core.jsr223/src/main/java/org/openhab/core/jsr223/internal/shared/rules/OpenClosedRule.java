package org.openhab.core.jsr223.internal.shared.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openhab.core.jsr223.internal.shared.ChangedEventTrigger;
import org.openhab.core.jsr223.internal.shared.Event;
import org.openhab.core.jsr223.internal.shared.EventTrigger;
import org.openhab.core.jsr223.internal.shared.Rule;
import org.openhab.core.library.types.OpenClosedType;


public class OpenClosedRule extends ItemRule implements Rule {

    public OpenClosedRule(Map<String, String> itemNames) {
	super(itemNames);
    }

    public OpenClosedRule(String itemName, String friendlyName) {
	super(itemName, friendlyName);
    }

    public OpenClosedRule(String itemName) {
	super(itemName);
    }
    
    public OpenClosedRule(List<String> items) {
	super(items);
    }
    
    protected void open(Event event) {
	logDebug(getFriendlyName(event) + " OPEN");
    }

    protected void closed(Event event) {
	logDebug(getFriendlyName(event) + " CLOSED");
    }


    public void execute(Event event) {
	try {
	    logDebug("Event received: " + event.toString());
	    if ( event.getNewState() instanceof org.openhab.core.library.types.OpenClosedType ) {
		OpenClosedType state = (OpenClosedType) event.getNewState();
		if ( state == OpenClosedType.OPEN ) {
		    open(event);
		}
		else {
		    closed(event);
		}
	    }
	    else {
		logError(event.getItem().getName() + " new state is not OpenClosedType. Was " + event.getNewState().getClass().getName());
	    }
	}
	catch (Exception e) {
	    logError("Exception in event handler: ", e);
	}
    }

    public List<EventTrigger> getEventTrigger() {
	List<EventTrigger> triggers = new ArrayList<EventTrigger>();
	for(String itemName : getItemsForTriggers()) {
	    triggers.add(new ChangedEventTrigger(itemName, OpenClosedType.CLOSED, OpenClosedType.OPEN));
	    triggers.add(new ChangedEventTrigger(itemName, OpenClosedType.OPEN, OpenClosedType.CLOSED));
	}
	return triggers;
    }

}
