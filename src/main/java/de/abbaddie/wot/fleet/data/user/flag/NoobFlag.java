package de.abbaddie.wot.fleet.data.user.flag;

import org.springframework.stereotype.Component;

import de.abbaddie.wot.data.user.UserFlag;
import de.abbaddie.wot.data.user.User;
import de.abbaddie.wot.fleet.lib.NoobProtectionCheck;

@Component
public class NoobFlag implements UserFlag {
	@Override
	public char getFlag() {
		return 'n';
	}
	
	@Override
	public boolean show(User user, User viewer) {
		return NoobProtectionCheck.facesProtection(viewer, user);
	}
}
