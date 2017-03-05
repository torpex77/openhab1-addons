package org.openhab.core.jsr223.internal.shared.rules;
import java.util.ArrayList;
import java.util.List;

import org.openhab.core.jsr223.internal.shared.Event;
import org.openhab.core.jsr223.internal.shared.EventTrigger;
import org.openhab.core.jsr223.internal.shared.Rule;
import org.openhab.core.jsr223.internal.shared.StartupTrigger;

public class StartedRule extends BaseRule implements Rule {

    private long delayMs;
    private boolean started = false;

    public StartedRule() { }

    public StartedRule(long delayMs) {
	this.delayMs = delayMs;
    }

    protected void started(Event event) {
	logDebug("OpenHAB started");
    }

    public void execute(Event event) {
	try {
	    logDebug("Event received: " + event.toString());
	    if ( delayMs > 0 ) {
		logDebug("Sleeping " + delayMs + " ms");
		Thread.sleep(delayMs);
	    }
	    started = true;
	    started(event);
	}
	catch (Exception e) {
	    logError("Exception in event handler: ", e);
	}
    }

    public List<EventTrigger> getEventTrigger() {
	List<EventTrigger> triggers = new ArrayList<>();
	triggers.add(new StartupTrigger());
	return triggers;
    }
    
    public boolean isStarted() {
	return started;
    }
}
