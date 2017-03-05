package org.openhab.core.jsr223.internal.shared.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openhab.core.jsr223.internal.shared.ChangedEventTrigger;
import org.openhab.core.jsr223.internal.shared.Event;
import org.openhab.core.jsr223.internal.shared.EventTrigger;
import org.openhab.core.jsr223.internal.shared.Rule;

public class ChangedRule extends ItemRule implements Rule {

    public ChangedRule(Map<String, String> itemNames) {
	super(itemNames);
    }

    public ChangedRule(String itemName, String friendlyName) {
	super(itemName, friendlyName);
    }

    public ChangedRule(String itemName) {
	super(itemName);
    }
    
    public ChangedRule(List<String> items) {
	super(items);
    }
    
    protected void changed(Event event) {
	logDebug(getFriendlyName(event) + " changed");
    }

    public void execute(Event event) {
	try {
	    logDebug("Event received: " + event.toString());
	    changed(event);
	}
	catch (Exception e) {
	    logError("Exception in event handler: ", e);
	}
    }

    public List<EventTrigger> getEventTrigger() {
	List<EventTrigger> triggers = new ArrayList<>();
	for(String itemName : getItemsForTriggers()) {
	    triggers.add(new ChangedEventTrigger(itemName));
	}
	return triggers;
    }

}
