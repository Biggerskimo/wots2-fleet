package de.abbaddie.wot.fleet.data.fleet;

import java.util.List;

import de.abbaddie.wot.data.user.User;

public interface FleetRepository {
	public Fleet findOne(int fleetId);
	
	public List<? extends FleetImpl> findByOwner(User owner);
	
	public EditableFleet create();
	
	@Deprecated
	public void save(EditableFleet fleet);
	
	public void update(EditableFleet fleet);
	
	public void refresh(EditableFleet fleet);
}
