package org.openhab.core.jsr223.internal.shared.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openhab.core.jsr223.internal.shared.ChangedEventTrigger;
import org.openhab.core.jsr223.internal.shared.Event;
import org.openhab.core.jsr223.internal.shared.EventTrigger;
import org.openhab.core.jsr223.internal.shared.Rule;
import org.openhab.core.library.types.DecimalType;

public class RangeRule extends ItemRule implements Rule {

    final protected Double lowValue;
    final protected Double highValue;

    protected boolean alertSent = false;

    public RangeRule(Map<String, String> itemNames, Double lowValue, Double highValue) {
	super(itemNames);
	this.lowValue = lowValue;
	this.highValue = highValue;
    }

    public RangeRule(String itemName, String friendlyName, Double lowValue, Double highValue) {
	super(itemName, friendlyName);
	this.lowValue = lowValue;
	this.highValue = highValue;
    }

    public RangeRule(String itemName, Double lowValue, Double highValue) {
	super(itemName);
	this.lowValue = lowValue;
	this.highValue = highValue;
    }
    
    public RangeRule(List<String> items, Double lowValue, Double highValue) {
	super(items);
	this.lowValue = lowValue;
	this.highValue = highValue;
    }
    
    protected void low(Event event) {
	if ( lowValue != null ) {
	    logWarn(getFriendlyName(event) + " low: " + ((DecimalType)event.getNewState()).doubleValue());
	}
    }

    protected void high(Event event) {
	if ( highValue != null ) {
	    logWarn(getFriendlyName(event) + " high: " + ((DecimalType)event.getNewState()).doubleValue());
	}	
    }

    protected void backInRange(Event event) {
	logWarn(getFriendlyName(event) + " normal: " + ((DecimalType)event.getNewState()).doubleValue());
    }

    // TODO:  delay alert to make sure it's not transient?
    // TODO:  repeat alert after a period of time?
    // TODO:  include unit text?
    public void execute(Event event) {
	try {
	    logDebug("Event received: " + event.toString());
	    if ( event.getNewState() instanceof org.openhab.core.library.types.DecimalType ) {
		DecimalType state = (DecimalType) event.getNewState();
		double val = state.doubleValue();
		if ( lowValue != null && val < lowValue ) {
		    alertSent = true;
		    low(event);
		}
		else if ( highValue != null && val > highValue ) {
		    alertSent = true;
		    high(event);
		}
		else {
		    if ( alertSent ) {
			alertSent = false;
			backInRange(event);
		    }
		}
	    }
	    else {
		logError(event.getItem().getName() + " new state is not DecimalType. Was " + event.getNewState().getClass().getName());
	    }	    

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
