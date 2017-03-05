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
import org.openhab.core.jsr223.internal.shared.StartupTrigger;
import org.openhab.core.jsr223.internal.shared.TriggerType;
import org.openhab.core.jsr223.internal.shared.UpdatedEventTrigger;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;

/**
 * implements a MiniMote button remote with an optional linked MultiSwitch.
 * 
 * 
 * @author doug
 *
 */
public class MiniMoteRule extends BaseRule implements Rule {

    // TODO:  Build in a check to not starting processing commands until after OH has been started for a minue????

    private boolean started = false;

    final protected String minimoteSceneItemName;

    final protected Map<String,Integer> itemButtons;

    public MiniMoteRule(String minimoteSceneItemName) {
	this.minimoteSceneItemName = minimoteSceneItemName;
	itemButtons = null;
    }


    public MiniMoteRule(String minimoteSceneItemName, String msBaseItemName) {
	this.minimoteSceneItemName = minimoteSceneItemName;

	// one name, assume they are name_N
	itemButtons = new HashMap<>();
	for(int i = 0; i < 8; i++) {
	    itemButtons.put(msBaseItemName + "_" + (i+1), i+1);
	}
    }

    public MiniMoteRule(String minimoteSceneItemName, List<String> buttonItemNames) {
	this.minimoteSceneItemName = minimoteSceneItemName;
	itemButtons = new HashMap<>();
	int idx = 0;
	for(String name : buttonItemNames ) {
	    this.itemButtons.put(name, ++idx);
	}
    }

    protected void b1Tap(Event event) {
	logDebug("Button 1 tap");
    }


    protected void b1Hold(Event event) {
	logDebug("Button 1 hold");
    }

    protected void b2Tap(Event event) {
	logDebug("Button 2 tap");
    }


    protected void b2Hold(Event event) {
	logDebug("Button 2 hold");
    }

    protected void b3Tap(Event event) {
	logDebug("Button 3 tap");
    }


    protected void b3Hold(Event event) {
	logDebug("Button 3 hold");
    }

    protected void b4Tap(Event event) {
	logDebug("Button 4 tap");
    }


    protected void b4Hold(Event event) {
	logDebug("Button 4 hold");
    }

    public void execute(Event event) {
	try {
	    logDebug("Event received: " + event.toString());
	    if ( started ) {
		if ( event.getTriggerType() == TriggerType.UPDATE ) {
		    // state == a DecimalType, so
		    if ( event.getNewState() instanceof DecimalType ) {
			if ( event.getOldState() == null ) {
			    logDebug("getOldState is null");
			}
			else {
			    logDebug("getOldState class is " + event.getOldState().getClass().getName() );
			}
			dispatch(((DecimalType) event.getNewState()).intValue(), event);
			//		    if ( event.getOldState() != null && ( ! (event.getOldState() instanceof UnDefType)) ) {
			//			dispatch(((DecimalType) event.getNewState()).intValue(), event);
			//		    }
			//		    else {
			// TODO:  Not sure I want to do this....Might break minimote?
			//			logInfo("Ignoring update with undefined old state: " + event.toString());
			//		    }
		    }
		    else {
			logError("newState was not a DecimalType");
		    }
		}
		else {
		    Integer button = itemButtons.get(event.getItem().getName());
		    dispatch(button, event);		
		}
	    }
	    else { 
		if ( event.getTriggerType() == TriggerType.STARTUP ) {
		    logInfo("Waiting 60 seconds for OH startup to complete");
		    Thread.sleep(60000);
		    logInfo("OH started.  Minimote enabled.");
		    started = true;
		}
		else {
		    logDebug("Ignored during startup: " + event.toString());
		}
	    }

	}
	catch (Exception e) {
	    logError("Exception in event handler: ", e);
	}
    }

    private void dispatch(Integer button, Event event) {
	if ( button != null ) {
	    if ( button == 1 ) { b1Tap(event); }
	    else if ( button == 2 ) { b1Hold(event); }
	    else if ( button == 3 ) { b2Tap(event); }
	    else if ( button == 4 ) { b2Hold(event); }
	    else if ( button == 5 ) { b3Tap(event); }
	    else if ( button == 6 ) { b3Hold(event); }
	    else if ( button == 7 ) { b4Tap(event); }
	    else if ( button == 8 ) { b4Hold(event); }
	    else {
		logError("Could not determine button from event " + event.toString());
	    }
	}
	else {
	    logError("Could not determine button from event item name " + event.toString());
	}
    }

    public List<EventTrigger> getEventTrigger() {
	List<EventTrigger> triggers = new ArrayList<>();
	triggers.add( new UpdatedEventTrigger(minimoteSceneItemName));

	if ( itemButtons != null ) {
	    for(Entry<String, Integer> button : itemButtons.entrySet() ) {
		triggers.add(new ChangedEventTrigger(button.getKey(), OnOffType.OFF, OnOffType.ON));
	    }
	}

	triggers.add(new StartupTrigger());

	return triggers;
    }

}
