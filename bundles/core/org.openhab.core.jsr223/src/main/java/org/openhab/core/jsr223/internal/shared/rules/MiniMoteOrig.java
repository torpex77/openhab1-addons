package org.openhab.core.jsr223.internal.shared.rules;

import java.util.ArrayList;
import java.util.List;

import org.openhab.core.jsr223.internal.shared.Event;
import org.openhab.core.jsr223.internal.shared.EventTrigger;
import org.openhab.core.jsr223.internal.shared.Rule;
import org.openhab.core.jsr223.internal.shared.UpdatedEventTrigger;
import org.openhab.core.library.types.DecimalType;

public class MiniMoteOrig extends BaseRule implements Rule {

    // base_BN_Tap

    final protected String minimoteSceneItemName;

    //    final protected String msItemName;

    public MiniMoteOrig(String minimoteSceneItemName) {
	this.minimoteSceneItemName = minimoteSceneItemName;
    }

    protected void b1Hold(Event event) {
	logDebug("Button 1 hold");
    }

    protected void b1Tap(Event event) {
	logDebug("Button 1 tap");
    }

    protected void b2Hold(Event event) {
	logDebug("Button 2 hold");
    }

    protected void b2Tap(Event event) {
	logDebug("Button 2 tap");
    }

    protected void b3Hold(Event event) {
	logDebug("Button 3 hold");
    }

    protected void b3Tap(Event event) {
	logDebug("Button 3 tap");
    }

    protected void b4Hold(Event event) {
	logDebug("Button 4 hold");
    }

    protected void b4Tap(Event event) {
	logDebug("Button 4 tap");
    }


    public void execute(Event event) {
	try {
	    // state == a DecimalType, so
	    if ( event.getNewState() instanceof DecimalType ) {
		int button = ((DecimalType) event.getNewState()).intValue();
		if ( button == 1 ) { b1Tap(event); }
		else if ( button == 2 ) { b1Hold(event); }
		else if ( button == 3 ) { b2Tap(event); }
		else if ( button == 4 ) { b2Hold(event); }
		else if ( button == 5 ) { b3Tap(event); }
		else if ( button == 6 ) { b3Hold(event); }
		else if ( button == 7 ) { b4Tap(event); }
		else if ( button == 8 ) { b4Hold(event); }
		else {
		    logError("Could not determine button from event new state" + event.toString());
		}
	    }
	    else {
		logError("newState was not a DecimalType");
	    }

	}
	catch (Exception e) {
	    logError("Exception in event handler: ", e);
	}
    }

    public List<EventTrigger> getEventTrigger() {
	List<EventTrigger> triggers = new ArrayList<>();
	triggers.add( new UpdatedEventTrigger(minimoteSceneItemName));

	return triggers;
    }

}
