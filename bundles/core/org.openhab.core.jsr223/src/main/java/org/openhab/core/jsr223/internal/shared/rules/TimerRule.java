package org.openhab.core.jsr223.internal.shared.rules;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openhab.core.jsr223.internal.shared.Event;
import org.openhab.core.jsr223.internal.shared.EventTrigger;
import org.openhab.core.jsr223.internal.shared.Rule;
import org.openhab.core.jsr223.internal.shared.TimerTrigger;

public class TimerRule extends BaseRule implements Rule {

    protected final List<String> cronSpecs; 

    public TimerRule(String cronSpec) {
	List<String> l = new ArrayList<String>();
	l.add(cronSpec);
	cronSpecs = Collections.unmodifiableList(l);
    }
    
    public TimerRule(List<String> cronSpecs) {
	List<String> l = new ArrayList<String>();
	l.addAll(cronSpecs);
	this.cronSpecs = Collections.unmodifiableList(l);
    }

    protected void timeFor(Event event) {
	logDebug("Time for event");
    }

    public void execute(Event event) {
	try {
	    logDebug("Event received: " + event.toString());
	    timeFor(event);
	}
	catch (Exception e) {
	    logError("Exception in event handler: ", e);
	}
    }

    public List<EventTrigger> getEventTrigger() {
	List<EventTrigger> triggers = new ArrayList<EventTrigger>();
	for(String cronSpec : cronSpecs) {
	    triggers.add(new TimerTrigger(cronSpec));
	}
	return triggers;
    }
    
}
