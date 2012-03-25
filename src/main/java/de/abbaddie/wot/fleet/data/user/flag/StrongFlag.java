package de.abbaddie.wot.fleet.data.user.flag;

import org.springframework.stereotype.Component;

import de.abbaddie.wot.data.user.UserFlag;
import de.abbaddie.wot.data.user.User;
import de.abbaddie.wot.fleet.lib.NoobProtectionCheck;

@Component
public class StrongFlag implements UserFlag {
	@Override
	public char getFlag() {
		return 's';
	}
	
	@Override
	public boolean show(User user, User viewer) {
		return NoobProtectionCheck.facesProtection(user, viewer);
	}
}
