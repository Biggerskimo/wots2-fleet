package de.abbaddie.wot.fleet.lib;

import org.joda.time.DateTime;
import org.joda.time.Period;

import de.abbaddie.wot.data.user.User;

public final class NoobProtectionCheck {
	private NoobProtectionCheck() {
		
	}
	
	public static boolean facesProtection(User owner, User ofiara) {
		if(owner.isAdmin()) {
			return false;
		}
		long ownerPoints = owner.getStatEntry().getPoints();
		long ofiaraPoints = ofiara.getStatEntry().getPoints();
		
		long limitPoints = (long) (ofiaraPoints * (Math.pow(ofiaraPoints, 3) + 2));
		
		boolean active = ofiara.getLastActivity().isAfter(DateTime.now().minus(new Period().withWeeks(1)));
		
		return ownerPoints > limitPoints && active;
	}
}
