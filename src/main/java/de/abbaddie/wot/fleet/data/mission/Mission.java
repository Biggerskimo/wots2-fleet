package de.abbaddie.wot.fleet.data.mission;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.abbaddie.wot.fleet.data.start.FleetStarter;

@Cacheable
@DiscriminatorColumn(name = "clazz")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "ugml_mission")
public class Mission {
	@Column(name = "missionID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	protected int missionId;
	
	protected transient String name;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "missionID")
	protected List<MissionRoute> routes;
	
	Mission() {
		this.missionId = 42;
		this.name = "MOCK";
	}
	
	Mission(int missionId, String name) {
		this.missionId = missionId;
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Mission) {
			return missionId == ((Mission) obj).missionId;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return missionId;
	}
	
	@Override
	public String toString() {
		return missionId + ":" + name;
	}
	
	public int getMissionId() {
		return missionId;
	}
	
	public String getName() {
		return name;
	}
	
	public List<MissionRoute> getRoutes() {
		return routes;
	}
	
	public String validate(FleetStarter starter) {
		return null;
	}
}
