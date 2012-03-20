package de.abbaddie.wot.fleet.data.user.flag;

import org.springframework.stereotype.Component;

import de.abbaddie.wot.data.user.UserFlag;
import de.abbaddie.wot.data.user.WotUser;
import de.abbaddie.wot.fleet.lib.NoobProtectionCheck;

@Component
public class StrongFlag implements UserFlag {
	@Override
	public char getFlag() {
		return 's';
	}
	
	@Override
	public boolean show(WotUser user, WotUser viewer) {
		return NoobProtectionCheck.facesProtection(user, viewer);
	}
}
