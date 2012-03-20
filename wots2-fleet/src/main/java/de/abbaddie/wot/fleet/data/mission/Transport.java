package de.abbaddie.wot.fleet.data.mission;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@DiscriminatorValue("transport")
@Entity
public class Transport extends Mission {
	public Transport() {
		super(3, "Transport");
	}
}
