package org.openhab.core.jsr223.internal.shared.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openhab.core.jsr223.internal.shared.ChangedEventTrigger;
import org.openhab.core.jsr223.internal.shared.Event;
import org.openhab.core.jsr223.internal.shared.EventTrigger;
import org.openhab.core.jsr223.internal.shared.Rule;
import org.openhab.core.library.types.OnOffType;

public class MultiSwitchRule extends BaseRule implements Rule {

    final protected Map<String,Integer> itemButtons;

    public MultiSwitchRule(String msBaseItemName) {
	// one name, assume they are name_N
	itemButtons = new HashMap<>();
	for(int i = 0; i < 8; i++) {
	    itemButtons.put(msBaseItemName + "_" + (i+1), i+1);
	}
    }

    public MultiSwitchRule(List<String> buttonItemNames) {
	this.itemButtons = new HashMap<>();
	int idx = 0;
	for(String name : buttonItemNames ) {
	    this.itemButtons.put(name, ++idx);
	}
    }

    protected void b1(Event event) {
	logDebug("Button 1 pressed");
    }

    protected void b2(Event event) {
	logDebug("Button 2 pressed");
    }

    protected void b3(Event event) {
	logDebug("Button 3 pressed");
    }

    protected void b4(Event event) {
	logDebug("Button 4 pressed");
    }

    protected void b5(Event event) {
	logDebug("Button 5 pressed");
    }

    protected void b6(Event event) {
	logDebug("Button 6 pressed");
    }

    protected void b7(Event event) {
	logDebug("Button 7 pressed");
    }

    protected void b8(Event event) {
	logDebug("Button 8 pressed");
    }


    public void execute(Event event) {
	try {
	    logDebug("Event received: " + event.toString());
	    Integer button = itemButtons.get(event.getItem().getName());
	    if ( button != null ) {
		if ( button == 1 ) { b1(event); }
		else if ( button == 2 ) { b2(event); }
		else if ( button == 3 ) { b3(event); }
		else if ( button == 4 ) { b4(event); }
		else if ( button == 5 ) { b5(event); }
		else if ( button == 6 ) { b6(event); }
		else if ( button == 7 ) { b7(event); }
		else if ( button == 8 ) { b8(event); }
	    }
	    else {
		logError("Could not determine button from event item name " + event.toString());
	    }
	}
	catch (Exception e) {
	    logError("Exception in event handler: ", e);
	}
    }

    public List<EventTrigger> getEventTrigger() {
	List<EventTrigger> triggers = new ArrayList<>();
	for(Entry<String, Integer> button : itemButtons.entrySet() ) {
	    triggers.add(new ChangedEventTrigger(button.getKey(), OnOffType.OFF, OnOffType.ON));
	}

	return triggers;
    }

}
