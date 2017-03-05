package org.openhab.core.jsr223.internal.shared.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openhab.core.jsr223.internal.shared.Event;
import org.openhab.core.jsr223.internal.shared.EventTrigger;
import org.openhab.core.jsr223.internal.shared.Rule;
import org.openhab.core.jsr223.internal.shared.UpdatedEventTrigger;

public class UpdatedRule extends ItemRule implements Rule {

    public UpdatedRule(Map<String, String> itemNames) {
	super(itemNames);
    }

    public UpdatedRule(String itemName, String friendlyName) {
	super(itemName, friendlyName);
    }

    public UpdatedRule(String itemName) {
	super(itemName);
    }
    
    public UpdatedRule(List<String> items) {
	super(items);
    }
    
    protected void updated(Event event) {
	logDebug(getFriendlyName(event) + " updated");
    }

    public void execute(Event event) {
	try {
	    logDebug("Event received: " + event.toString());
	    updated(event);
	}
	catch (Exception e) {
	    logError("Exception in event handler: ", e);
	}
    }

    public List<EventTrigger> getEventTrigger() {
	List<EventTrigger> triggers = new ArrayList<>();
	for(String itemName : getItemsForTriggers()) {
	    triggers.add(new UpdatedEventTrigger(itemName));
	}
	return triggers;
    }

}
