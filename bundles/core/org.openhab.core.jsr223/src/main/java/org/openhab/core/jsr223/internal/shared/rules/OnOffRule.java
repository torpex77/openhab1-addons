package org.openhab.core.jsr223.internal.shared.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openhab.core.jsr223.internal.shared.ChangedEventTrigger;
import org.openhab.core.jsr223.internal.shared.Event;
import org.openhab.core.jsr223.internal.shared.EventTrigger;
import org.openhab.core.jsr223.internal.shared.Rule;
import org.openhab.core.library.types.OnOffType;

public class OnOffRule extends ItemRule implements Rule {

    public OnOffRule(Map<String, String> itemNames) {
	super(itemNames);
    }

    public OnOffRule(String itemName, String friendlyName) {
	super(itemName, friendlyName);
    }

    public OnOffRule(String itemName) {
	super(itemName);
    }
    
    public OnOffRule(List<String> items) {
	super(items);
    }
    
    protected void on(Event event) {
	logDebug(getFriendlyName(event) + " ON");
    }

    protected void off(Event event) {
	logDebug(getFriendlyName(event) + " OFF");
    }

    public void execute(Event event) {
	try {
	    logDebug("Event received: " + event.toString());
	    if ( event.getNewState() instanceof org.openhab.core.library.types.OnOffType ) {
		OnOffType state = (OnOffType) event.getNewState();
		if ( state == OnOffType.OFF ) {
		    off(event);
		}
		else {
		    on(event);
		}
	    }
	    else {
		logError(event.getItem().getName() + " new state is not OnOffType.  Was " + event.getNewState().getClass().getName());
	    }
	}
	catch (Exception e) {
	    logError("Exception in event handler: ", e);
	}
    }

    public List<EventTrigger> getEventTrigger() {
	List<EventTrigger> triggers = new ArrayList<>();
	for(String itemName : getItemsForTriggers()) {
	    triggers.add(new ChangedEventTrigger(itemName, OnOffType.OFF, OnOffType.ON));
	    triggers.add(new ChangedEventTrigger(itemName, OnOffType.ON, OnOffType.OFF));
	}
	return triggers;
    }

}
