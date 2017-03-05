package org.openhab.core.jsr223.internal.shared.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.jsr223.internal.shared.Event;
import org.openhab.core.types.State;

public class ItemRule extends BaseRule {

    protected final Map<String,String> itemNames; 

    public ItemRule(String itemName) {
	this(itemName, null);
    }

    public ItemRule(String itemName, String friendlyName) {
	Map<String,String> map = new HashMap<String,String>();
	if ( friendlyName == null ) {
	    map.put(itemName, itemName);
	}
	else {
	    map.put(itemName, friendlyName);	    
	}
	itemNames = Collections.unmodifiableMap(createItemMap(map));
    }

    public ItemRule(Map<String,String> itemNames) {
	// create an copy and make that unmodifiable
	this.itemNames = Collections.unmodifiableMap(createItemMap(itemNames));
    }
    
    public ItemRule(List<String> items) {
	Map<String,String> map = new HashMap<String,String>();
	for(String itemName : items) {
	    map.put(itemName, itemName);
	}
	itemNames = Collections.unmodifiableMap(createItemMap(map));	
    }

    private Map<String,String> createItemMap(Map<String,String> map) {

	Map<String,String> resolvedMap = new HashMap<>();
	// iterate the keys in the itemNames
	// if item type, add
	// if group type with BaseType, add
	// if group type without BaseType add all members (use the recurse method to get them)
	//for(String itemName : map.)

	for(Map.Entry<String,String> entry: map.entrySet()) {
	    try {
		Item item = getItem(entry.getKey());
		logDebug("itemTYpe = " + item.getClass().getName());
		if ( item instanceof GroupItem ) {
		    GroupItem gi = (GroupItem) item;
		    if ( gi.getBaseItem() == null ) {
			for(Item child : gi.getAllMembers()) {
			    logDebug("GI No Base adding: " + child.getName());
			    resolvedMap.put(child.getName(), entry.getValue());
			}
		    }
		    else {
			logDebug("GI Base adding: " + item.getName());
			resolvedMap.put(entry.getKey(), entry.getValue());
		    }
		}
		else {
			logDebug("Item adding: " + item.getName());
			resolvedMap.put(entry.getKey(), entry.getValue());
		}
	    }
	    catch (ItemNotFoundException e) {
		logError(entry.getKey() + " does not exist. Ignored.");
	    }
	}

	return resolvedMap;
    }

    
    protected List<String> getItemsForTriggers() {
	return new ArrayList<String>(itemNames.keySet());
/*
	List<String> allNames = new ArrayList<String>();
	// iterate the keys in the itemNames
	// if item type, add
	// if group type with BaseType, add
	// if group type without BaseType add all members (use the recurse method to get them)
	//for(String itemName : map.)

	for(Map.Entry<String,String> entry: itemNames.entrySet()) {
	    try {
		Item item = getItem(entry.getKey());
		logDebug("itemTYpe = " + item.getClass().getName());
		if ( item instanceof GroupItem ) {
		    GroupItem gi = (GroupItem) item;
		    if ( gi.getBaseItem() == null ) {
			for(Item child : gi.getAllMembers()) {
			    logDebug("GI No Base adding: " + child.getName());
			    allNames.add(child.getName());
			}
		    }
		    else {
			logDebug("GI Base adding: " + item.getName());
			allNames.add(item.getName());
		    }
		}
		else {
			logDebug("Item adding: " + item.getName());
		    allNames.add(item.getName());
		}
	    }
	    catch (ItemNotFoundException e) {
		logError(entry.getKey() + " does not exist. Ignored.");
	    }
	}

	return allNames;
	*/
    }

    protected String getFriendlyName(String itemName) {
	String s = itemNames.get(itemName);
	if ( s == null ) {
	    return itemName;
	}
	else {
	    return s;
	}
    }

    protected String getFriendlyName(Event event) {
	return getFriendlyName(event.getItem());
    }

    protected String getFriendlyName(Item item) {
	return getFriendlyName(item.getName());
    }

    public boolean all(State state) {
//	logDebug("all(State) == " + state);
//	logDebug("all(State) toString == " + state.toString());
//	logDebug("all(State) class == " + state.getClass().getName());
	boolean b = true;
	for(String itemName : itemNames.keySet() ) {
	    try {
//		logDebug("all getItem(" + itemName + ").getState()) == " + getItem(itemName).getState());
//		logDebug("all getItem(" + itemName + ").getState() toString == " + getItem(itemName).getState().toString());
//		logDebug("all getItem(" + itemName + ").getState() class == " + getItem(itemName).getState().getClass().getName());
		Item item = getItem(itemName);
		if ( item.getState() != state ) {
		    logDebug(itemName + " is " + item.getState().toString());
		    b = false;
		    break;
		}
	    } catch (ItemNotFoundException e) {
		logError(itemName + " does not exist. Ignored.");

	    }
	}
	return b;
    }
    
    public boolean any(State state) {
//	logDebug("any(State) == " + state);
//	logDebug("any(State) toString == " + state.toString());
//	logDebug("any(State) class == " + state.getClass().getName());
	boolean b = false;
	for(String itemName : itemNames.keySet() ) {
	    try {
//		logDebug("any getItem(" + itemName + ").getState()) == " + getItem(itemName).getState());
//		logDebug("any getItem(" + itemName + ").getState() toString == " + getItem(itemName).getState().toString());
//		logDebug("any getItem(" + itemName + ").getState() class == " + getItem(itemName).getState().getClass().getName());
		Item item = getItem(itemName);
		if ( item.getState() == state ) {
		    logDebug(itemName + " is " + item.getState().toString());
		    b = true;
		    break;
		}
	    } catch (ItemNotFoundException e) {
		logError(itemName + " does not exist. Ignored.");

	    }
	}
	return b;
    }

}