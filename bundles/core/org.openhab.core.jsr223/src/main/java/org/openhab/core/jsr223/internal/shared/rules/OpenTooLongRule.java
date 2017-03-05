package org.openhab.core.jsr223.internal.shared.rules;

import org.joda.time.DateTime;
import org.openhab.core.jsr223.internal.actions.Timer;
import org.openhab.core.jsr223.internal.shared.Event;
import org.openhab.core.jsr223.internal.shared.Openhab;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public abstract class OpenTooLongRule extends OpenClosedRule {

    protected Timer timer;

    final protected int minutes;

    final private ReentrantLock eventLock  = new ReentrantLock();

    public OpenTooLongRule(Map<String, String> itemNames, int minutes) {
	super(itemNames);
	this.minutes = minutes;
    }

    public OpenTooLongRule(String itemName, String friendlyName, int minutes) {
	super(itemName, friendlyName);
	this.minutes = minutes;
    }

    public OpenTooLongRule(String itemName, int minutes) {
	super(itemName);
	this.minutes = minutes;
    }

    public OpenTooLongRule(List<String> items, int minutes) {
	super(items);
	this.minutes = minutes;
    }

    @Override
    protected void open(Event event) {
	eventLock.lock();
	try {
	    if ( timer != null ) {  // shouldn't happen unless we somehow get two OPEN events without a CLOSED
		debugMsg(getFriendlyName(event) + " running timer cancelled.");
		timer.cancel();
		timer = null;
	    }
	    debugMsg(getFriendlyName(event) + " open. Starting timer for " + minutes  + " minutes.");
	    Runnable r = new Runnable(){
		public void run() { 
		    alert();
		}
	    };
	    timer = Openhab.createTimer(new DateTime().plusMinutes(minutes), r);
	}
	finally {
	    eventLock.unlock();
	}
    }

    @Override
    protected void closed(Event event) {
	eventLock.lock();
	try {
	    if ( timer != null ) {
		debugMsg(getFriendlyName(event) + " closed. Timer cancelled.");
		timer.cancel();
		timer = null;
	    }
	    else {
		debugMsg(getFriendlyName(event) + " closed. No timer running.");
	    }
	}
	finally {
	    eventLock.unlock();
	}
    }

    abstract protected void alert();

}
