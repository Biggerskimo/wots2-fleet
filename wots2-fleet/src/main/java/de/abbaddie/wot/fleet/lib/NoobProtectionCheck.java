package de.abbaddie.wot.fleet.lib;

import de.abbaddie.wot.data.stats.Stats;
import de.abbaddie.wot.data.stats.UserPointsStatEngine;
import de.abbaddie.wot.data.user.WotUser;

public final class NoobProtectionCheck {
	private NoobProtectionCheck() {
		
	}
	
	public static boolean facesProtection(WotUser owner, WotUser ofiara) {
		if(owner.isAdmin()) {
			return false;
		}
		long ownerPoints = Stats.findEntry(new UserPointsStatEngine(), owner).getPoints();
		long ofiaraPoints = Stats.findEntry(new UserPointsStatEngine(), ofiara).getPoints();
		// long ownerPoints = owner.getStatEntry().getPoints();
		// long ofiaraPoints = ofiara.getStatEntry().getPoints();
		
		long limitPoints = (long) (ofiaraPoints * (Math.pow(ofiaraPoints, 3) + 2));
		return ownerPoints > limitPoints;
	}
}
