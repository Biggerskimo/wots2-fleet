package de.abbaddie.wot.fleet.data.fleet;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import de.abbaddie.wot.data.user.WotUser;
import de.abbaddie.wot.lib.BeanConfigurationUtil;

public final class Fleets {
	protected static FleetRepository repo;
	
	protected static Fleets instance = new Fleets();
	
	@Autowired
	protected FleetRepository repo_;
	
	private Fleets() {
		BeanConfigurationUtil.configure(this);
		
		repo = repo_;
	}
	
	public static List<? extends FleetImpl> findByOwner(WotUser owner) {
		return repo.findByOwner(owner);
	}
	
	public static Fleet findOne(int fleetId) {
		return repo.findOne(fleetId);
	}
	
	public static EditableFleet create() {
		return repo.create();
	}
	
	@Deprecated
	public static void save(EditableFleet fleet) {
		repo.save(fleet);
	}
	
	public static void update(EditableFleet fleet) {
		repo.update(fleet);
	}
	
	public static void refresh(EditableFleet fleet) {
		repo.refresh(fleet);
	}
}
