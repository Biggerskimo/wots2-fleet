package de.abbaddie.wot.fleet.data.fleet;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;

import com.google.common.collect.ImmutableMap;

import de.abbaddie.wot.data.coordinates.AbstractCoordinates;
import de.abbaddie.wot.data.coordinates.Coordinates;
import de.abbaddie.wot.data.event.Event;
import de.abbaddie.wot.data.event.EventExecutor;
import de.abbaddie.wot.data.event.Events;
import de.abbaddie.wot.data.planet.Planet;
import de.abbaddie.wot.data.planet.Planets;
import de.abbaddie.wot.data.resource.CrystalResource;
import de.abbaddie.wot.data.resource.DeuteriumResource;
import de.abbaddie.wot.data.resource.MetalResource;
import de.abbaddie.wot.data.resource.ResourceMapper;
import de.abbaddie.wot.data.resource.ResourcePredicate;
import de.abbaddie.wot.data.resource.ResourceValueSet;
import de.abbaddie.wot.data.resource.Resources;
import de.abbaddie.wot.data.spec.SpecMapper;
import de.abbaddie.wot.data.spec.SpecPredicate;
import de.abbaddie.wot.data.spec.SpecSet;
import de.abbaddie.wot.data.spec.Specs;
import de.abbaddie.wot.fleet.data.mission.Mission;
import de.abbaddie.wot.fleet.data.mission.Missions;
import de.abbaddie.wot.fleet.data.spec.trait.FleetBound;

@Entity
@Table(name = "ugml_fleet")
class FleetImpl implements EditableFleet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fleetID")
	protected int id;
	
	@NotNull
	@Column(name = "ownerID")
	protected int ownerId;
	
	@Column(name = "ofiaraID")
	protected Integer ofiaraId;
	
	@NotNull
	@Column(name = "startPlanetID")
	protected int startPlanetId;
	
	@Column(name = "targetPlanetID")
	protected Integer targetPlanetId;
	
	@NotNull
	@OneToMany(cascade = CascadeType.ALL)
	@MapKey(name = "specId")
	@JoinColumn(name = "fleetID")
	protected Map<Integer, FleetSpec> specs = new HashMap<>();
	
	@Column(name = "missionID")
	protected int missionId;
	
	protected long metal;
	protected long crystal;
	protected long deuterium;
	
	@Embedded
	protected FleetCoordinates coords;
	
	protected Integer impactEventId;
	protected int impactTime;
	protected int returnEventId;
	protected int returnTime;
	
	protected transient Map<FleetEventType, Event> events;
	
	public FleetImpl() {
		events = new HashMap<>();
	}
	
	// getter
	@Override
	public int getId() {
		return id;
	}
	
	@Override
	public SpecSet<FleetBound> getSpecs() {
		return Specs.getSpecSet(new FleetSpecMapper(), FleetBound.class);
	}
	
	@Override
	public Planet getStartPlanet() {
		return Planets.findOne(startPlanetId);
	}
	
	@Override
	public Planet getTargetPlanet() {
		return targetPlanetId == null ? null : Planets.findOne(targetPlanetId);
	}
	
	@Override
	public Mission getMission() {
		return Missions.findOne(missionId);
	}
	
	@Override
	public Coordinates getCoordinates() {
		return coords;
	}
	
	@Override
	public ResourcePredicate[] getAllowedResourcePredicates() {
		return new ResourcePredicate[]{new MetalResource(), new CrystalResource(), new DeuteriumResource()};
	}
	
	@Override
	public ResourceValueSet getResources() {
		return Resources.generate(getAllowedResourcePredicates(), new FleetResourceMapper());
	}
	
	@Override
	public Map<FleetEventType, Event> getEvents() {
		ImmutableMap.Builder<FleetEventType, Event> builder = new ImmutableMap.Builder<>();
		
		if(events.containsKey(DefaultEventTypes.IMPACT)) {
			builder.put(DefaultEventTypes.IMPACT, events.get(DefaultEventTypes.IMPACT));
		} else if(impactEventId != null && impactEventId != 0) {
			builder.put(DefaultEventTypes.IMPACT, new Event() {
				@Override
				public int getId() {
					return impactEventId;
				}
				
				@Override
				public DateTime getTime() {
					return new DateTime((long) impactTime * 1000);
				}
				
				@Override
				public EventExecutor getExecutor() {
					throw new UnsupportedOperationException();
				}
				
				@Override
				public int getRelationalId() {
					return id;
				}
			});
		}
		if(events.containsKey(DefaultEventTypes.RETURN)) {
			builder.put(DefaultEventTypes.RETURN, events.get(DefaultEventTypes.RETURN));
		} else {
			builder.put(DefaultEventTypes.RETURN, new Event() {
				@Override
				public int getId() {
					return returnEventId;
				}
				
				@Override
				public DateTime getTime() {
					return new DateTime((long) returnTime * 1000);
				}
				
				@Override
				public EventExecutor getExecutor() {
					throw new UnsupportedOperationException();
				}
				
				@Override
				public int getRelationalId() {
					return id;
				}
			});
		}
		
		return builder.build();
	}
	
	// setter
	@Override
	public void setSpecs(SpecSet<? extends FleetBound> specs) {
		getSpecs().clear();
		getSpecs().add(specs);
	}
	
	@Override
	public void setCoordinates(Coordinates coords) {
		this.coords = new FleetCoordinates(coords);
	}
	
	@Override
	public void setMission(Mission mission) {
		missionId = mission.getMissionId();
	}
	
	@Override
	public void setResources(ResourceValueSet resources) {
		metal = (long) resources.getValue("metal");
		crystal = (long) resources.getValue("crystal");
		deuterium = (long) resources.getValue("deuterium");
	}
	
	@Override
	public void setStartPlanet(Planet planet) {
		startPlanetId = planet.getId();
		ownerId = planet.getOwner().getId();
	}
	
	@Override
	public void setTargetPlanet(Planet planet) {
		if(planet == null) {
			targetPlanetId = null;
			ofiaraId = null;
		} else {
			targetPlanetId = planet.getId();
			ofiaraId = planet.getOwner().getId();
		}
	}
	
	@Override
	public void setEvent(FleetEventType type, Event event) {
		events.put(type, event);
		if(type == DefaultEventTypes.IMPACT) {
			if(event == null) {
				Events.delete(Events.findOne(impactEventId));
				impactEventId = null;
			} else {
				impactEventId = event.getId();
				impactTime = (int) (event.getTime().getMillis() / 1000);
			}
		} else if(type == DefaultEventTypes.RETURN) {
			if(event == null) {
				throw new UnsupportedOperationException();
			}
			returnEventId = event.getId();
			returnTime = (int) (event.getTime().getMillis() / 1000);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	// classes
	@Embeddable
	protected static class FleetCoordinates extends AbstractCoordinates {
		protected int galaxy;
		protected int system;
		protected int planet;
		
		public FleetCoordinates(int galaxy, int system, int planet) {
			this.galaxy = galaxy;
			this.system = system;
			this.planet = planet;
		}
		
		public FleetCoordinates(Coordinates c) {
			galaxy = c.getGalaxy();
			system = c.getSystem();
			planet = c.getOrbit();
		}
		
		public FleetCoordinates() {
			
		}
		
		@Override
		public int getGalaxy() {
			return galaxy;
		}
		
		@Override
		public int getSystem() {
			return system;
		}
		
		@Override
		public int getOrbit() {
			return planet;
		}
		
		@Override
		public int getKind() {
			return 1;
		}
	}
	
	protected class FleetResourceMapper implements ResourceMapper {
		@Override
		public double getCount(ResourcePredicate predicate) {
			if(predicate instanceof MetalResource) {
				return metal;
			} else if(predicate instanceof CrystalResource) {
				return crystal;
			} else if(predicate instanceof DeuteriumResource) {
				return deuterium;
			} else {
				throw new IllegalArgumentException("given predicate is not supported");
			}
		}
		
		@Override
		public void setCount(ResourcePredicate predicate, double count) {
			if(predicate instanceof MetalResource) {
				metal = (long) count;
			} else if(predicate instanceof CrystalResource) {
				crystal = (long) count;
			} else if(predicate instanceof DeuteriumResource) {
				deuterium = (long) count;
			} else {
				throw new IllegalArgumentException("given predicate is not supported");
			}
		}
	}
	
	protected class FleetSpecMapper implements SpecMapper {
		@Override
		public long getLevel(SpecPredicate predicate) {
			FleetSpec spec = specs.get(predicate.getId());
			
			if(spec == null) {
				return 0;
			} else {
				return spec.getCount();
			}
		}
		
		@Override
		public void setLevel(SpecPredicate predicate, long level) {
			FleetSpec spec = specs.get(predicate);
			
			if(spec == null) {
				specs.put(predicate.getId(), new FleetSpec(FleetImpl.this, predicate, level));
			} else {
				specs.get(predicate.getId()).setCount(level);
			}
		}
	}
}
