package de.abbaddie.wot.fleet.data.fleet;

import org.joda.time.DateTime;

public interface FleetCancelService {
	public boolean isCancellable(EditableFleet fleet);
	
	public void cancel(EditableFleet fleet, DateTime time);
}
