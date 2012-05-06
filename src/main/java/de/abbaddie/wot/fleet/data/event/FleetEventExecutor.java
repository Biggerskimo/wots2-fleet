package de.abbaddie.wot.fleet.data.event;

import de.abbaddie.wot.data.event.Event;
import de.abbaddie.wot.data.event.EventExecutor;

public class FleetEventExecutor implements EventExecutor {
	protected FleetEventImpl event;
	
	public FleetEventExecutor(FleetEventImpl event) {
		this.event = event;
	}
	
	@Override
	public Event getEvent() {
		return event;
	}
	
	@Override
	public void execute() throws Exception {
		throw new UnsupportedOperationException();
	}
}
