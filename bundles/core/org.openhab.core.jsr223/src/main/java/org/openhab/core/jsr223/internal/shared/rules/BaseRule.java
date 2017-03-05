package org.openhab.core.jsr223.internal.shared.rules;

import java.util.Collection;
import java.util.HashMap;
//import java.util.List;

//import org.openhab.core.jsr223.internal.shared.Event;
//import org.openhab.core.jsr223.internal.shared.EventTrigger;
//import org.openhab.core.jsr223.internal.shared.Rule;

//import org.openhab.core.jsr223.internal.shared.*;

//import org.openhab.core.items.ItemRegistry;









//import org.openhab.core.jsr223.internal.engine.scriptmanager.ScriptManager;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
//import org.openhab.action.mios.internal.MiosAction;
import org.openhab.core.items.Item;
//import org.openhab.core.items.ItemNotFoundException;


// these only have static methods that we use.  They are defined with these
// names in the Script class, but the inner classes don't have access to them
// so the imports here make them available to the inner classes without having
// to pass them in somehow or make any other type of Global class to hold them.

import org.openhab.core.items.ItemNotFoundException;
//import org.openhab.model.script.actions.BusEvent;
import org.slf4j.Logger;
import org.openhab.core.jsr223.internal.engine.scriptmanager.ScriptManager;
import org.openhab.core.jsr223.internal.shared.Openhab;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
//import org.openhab.core.persistence.extensions.PersistenceExtensions;


public class BaseRule {

    public static boolean openHabStarted = false;

    //  protected PersistenceExtensions pe  = new PersistenceExtensions();// ; //PersistenceExtensions.class pe

    protected Logger logger = Openhab.getLogger(getClass().getSimpleName());

    protected void sendCommand(Item item, String commandString) { 
	Openhab.sendCommand(item, commandString);
    }

    protected void sendCommand(String itemName, String commandString) {
	Openhab.sendCommand(itemName, commandString);
    }

    protected void sendCommand(Item item, Number number) {
	Openhab.sendCommand(item, number);
    } 

    protected void sendCommand(Item item, Command command) {
	Openhab.sendCommand(item, command);
    } 

    protected void postUpdate(Item item, Number state) {
	Openhab.postUpdate(item, state);
    }

    protected void postUpdate(Item item, String stateAsString) {
	Openhab.postUpdate(item, stateAsString);
    }

    protected void postUpdate(Item item, State state) {
	Openhab.postUpdate(item, state);
    }

    protected void postUpdate(String itemName, String stateString) {
	Openhab.postUpdate(itemName, stateString);
    }

    protected Object getAction(String actionName) {
	return Openhab.getAction(actionName);
    }

    protected HashMap<String, Object> getActions() {
	return Openhab.getActions();
    }

    protected Item getItem(String itemName) throws ItemNotFoundException {
	//   // return ir.getItem(itemName);
	return ScriptManager.getInstance().getItemRegistry().getItem(itemName);
    }

    protected Item getItemByPattern(String pattern) throws ItemNotFoundException {
	return ScriptManager.getInstance().getItemRegistry().getItem(pattern);
    }

    protected Collection<Item> getItems() {
	return ScriptManager.getInstance().getItemRegistry().getItems();
    }

    protected Collection<Item> getItems(String pattern) {
	return ScriptManager.getInstance().getItemRegistry().getItems(pattern);
    }

    public BaseRule() {
    } 

    protected void logError(String format, Object ... args) {
	logger.error(format, args);
    }

    protected void logWarn(String format, Object ... args) {
        logger.warn(format, args);
    }

    protected void logInfo(String format, Object ... args) {
        logger.info(format, args);
    }

    protected void logDebug(String format, Object ... args) {
        logger.debug(format, args);
    }

    protected void alertMsg(String msg) {
	logWarn(msg);
	postUpdate("AlertMsg", msg);
    }
    
    protected void warnMsg(String msg) {
	logWarn(msg);
	postUpdate("WarnMsg", msg);
    }
    
    protected void infoMsg(String msg) {
	logInfo(msg);
	postUpdate("InfoMsg", msg);
    }

    protected void debugMsg(String msg) {
	logDebug(msg);
	postUpdate("DebugMsg", msg);
    }

    protected void toggle(String itemName) throws ItemNotFoundException {
	Item item = getItem(itemName);
	if ( item.getState() instanceof org.openhab.core.library.types.PercentType ) {
	    // dimmer
	    PercentType pt = (PercentType) item.getState();
	    if ( pt.intValue() > 0 ) {
		sendCommand(itemName, "0");
	    }
	    else {
		sendCommand(itemName, "100");
	    }
	}
	else if ( item.getState() instanceof org.openhab.core.library.types.OnOffType ) {
	    // switch
	    OnOffType type = (OnOffType) item.getState();
	    if ( type == OnOffType.ON) {
		sendCommand(itemName, "OFF");
	    }
	    else {
		sendCommand(itemName, "ON");
	    }

	}
	else {
	    logError(itemName + " toggle ignored. State is type " + item.getState().getClass().getName());
	    logError("item = " + item);
	    logError("state = " + item.getState());
	}
    }
    
    // this could be used by toggle, other than this just returns false if the item is not
    // an switch or dimmer
    protected boolean isOn(String itemName) throws ItemNotFoundException {
	Item item = getItem(itemName);
	if ( item.getState() instanceof org.openhab.core.library.types.PercentType ) {
	    // dimmer
	    PercentType pt = (PercentType) item.getState();
	    if ( pt.intValue() > 0 ) {
		return true;
	    }
	    else {
		return false;
	    }
	}
	else if ( item.getState() instanceof org.openhab.core.library.types.OnOffType ) {
	    // switch
	    OnOffType type = (OnOffType) item.getState();
	   return type == OnOffType.ON;
	}
	else {
	    return false;
	}
    }
}
