package de.abbaddie.wot.fleet.lib;

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
		return ownerPoints > limitPoints;
	}
}
