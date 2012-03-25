package de.abbaddie.wot.fleet.data.start;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import com.google.common.collect.ImmutableMap;

import de.abbaddie.wot.config.Config;
import de.abbaddie.wot.data.coordinates.Coordinates;
import de.abbaddie.wot.data.coordinates.DynamicCoordinates;
import de.abbaddie.wot.data.event.EditableEvent;
import de.abbaddie.wot.data.event.EventRepository;
import de.abbaddie.wot.data.planet.EditablePlanet;
import de.abbaddie.wot.data.planet.Planet;
import de.abbaddie.wot.data.planet.PlanetRepository;
import de.abbaddie.wot.data.resource.CrystalResource;
import de.abbaddie.wot.data.resource.DeuteriumResource;
import de.abbaddie.wot.data.resource.MetalResource;
import de.abbaddie.wot.data.resource.ResourcePredicate;
import de.abbaddie.wot.data.resource.ResourceValueSet;
import de.abbaddie.wot.data.resource.Resources;
import de.abbaddie.wot.data.spec.SpecRepository;
import de.abbaddie.wot.data.spec.SpecSet;
import de.abbaddie.wot.data.spec.Specs;
import de.abbaddie.wot.data.spec.filter.PositiveFilter;
import de.abbaddie.wot.fleet.data.event.FleetEventExecutor;
import de.abbaddie.wot.fleet.data.fleet.DefaultEventTypes;
import de.abbaddie.wot.fleet.data.fleet.EditableFleet;
import de.abbaddie.wot.fleet.data.fleet.FleetRepository;
import de.abbaddie.wot.fleet.data.mission.Mission;
import de.abbaddie.wot.fleet.data.mission.MissionRepository;
import de.abbaddie.wot.fleet.data.mission.MissionRoute;
import de.abbaddie.wot.fleet.data.ovent.FleetOventService;
import de.abbaddie.wot.fleet.data.spec.trait.FleetBound;
import de.abbaddie.wot.fleet.data.spec.trait.FleetBound.FleetDrive;

@Service("default")
@Scope("prototype")
@FleetStarterConstraint(message = "")
public class FleetStarterImpl implements FleetStarter {
	@Autowired
	protected Config config;
	
	@Autowired
	protected Validator validator;
	
	@Autowired
	protected FleetOventService oventService;
	
	@Autowired
	protected EventRepository eventRepo;
	
	@Autowired
	protected PlanetRepository planetRepo;
	
	@Autowired
	protected SpecRepository specRepo;
	
	@Autowired
	protected FleetRepository fleetRepo;
	
	@Autowired
	protected MissionRepository missionRepo;
	
	@PersistenceContext
	protected EntityManager em;
	
	protected SpecSet<FleetBound> specs;
	protected Map<String, String> levels;
	
	protected ResourceValueSet resourceSet;
	protected Map<String, String> resourceCounts;
	
	@NotNull(message = "Es wurde kein Startplanet gesetzt.")
	protected Planet startPlanet;
	
	protected boolean targetChanged;
	private Planet targetPlanet;
	
	protected double speedFactor = 1.0;
	
	protected FleetStartCoordinates coordinates;
	
	protected Mission mission;
	protected int missionId;
	
	public FleetStarterImpl() {
		levels = new HashMap<>();
		specs = specRepo.getDynamicSpecSet(Specs.convertToHardByStr(levels)).filter(FleetBound.class);
		
		resourceCounts = new HashMap<>();
		resourceSet = Resources.generateDynamic(getAllowedResourcePredicates(),
				Resources.convertToHardByStr(resourceCounts));
		
		coordinates = new FleetStartCoordinates(0, 0, 0, 0);
	}
	
	// fire
	@Override
	@Transactional
	public BindingResult validateAndFire() {
		DataBinder binder = new DataBinder(this);
		binder.setValidator(validator);
		binder.validate();
		BindingResult result = binder.getBindingResult();
		
		if(!result.hasErrors()) {
			fire();
		}
		
		return result;
	}
	
	@Override
	@Transactional
	public void fire() {
		EditablePlanet planet = (EditablePlanet) getStartPlanet();
		
		DateTime now = DateTime.now();
		
		EditableEvent impactEvent = eventRepo.create();
		impactEvent.setTime(now.withDurationAdded(getDuration(), 1));
		impactEvent.setExecutor(FleetEventExecutor.class);
		eventRepo.update(impactEvent);
		
		EditableEvent returnEvent = eventRepo.create();
		returnEvent.setTime(now.withDurationAdded(getDuration(), 2));
		returnEvent.setExecutor(FleetEventExecutor.class);
		eventRepo.update(returnEvent);
		
		em.flush();
		
		EditableFleet fleet = fleetRepo.create();
		fleet.setSpecs(getSpecs());
		fleet.setCoordinates(coordinates);
		fleet.setMission(mission);
		fleet.setResources(resourceSet);
		fleet.setStartPlanet(startPlanet);
		fleet.setTargetPlanet(targetPlanet);
		fleet.setEvent(DefaultEventTypes.IMPACT, impactEvent);
		fleet.setEvent(DefaultEventTypes.RETURN, returnEvent);
		fleetRepo.update(fleet);
		
		em.flush();
		
		impactEvent.setRelationalId(fleet.getId());
		eventRepo.update(impactEvent);
		
		returnEvent.setRelationalId(fleet.getId());
		eventRepo.update(returnEvent);
		
		oventService.createAll(fleet);
		
		planet.getResources().subtract(getResources());
		planet.getSpecs().subtract(getSpecs());
		
		planetRepo.update(planet);
	}
	
	// complex getters
	@Override
	public List<Mission> getAllowedMissions() {
		Iterable<Mission> all = missionRepo.findAll();
		List<Mission> allowed = new ArrayList<>();
		
		missions:
		for(Mission mission : all) {
			List<MissionRoute> routes = mission.getRoutes();
			
			for(MissionRoute route : routes) {
				boolean startAccepted = false;
				boolean targetAccepted = false;
				Class<? extends Planet> routePlanet;
				
				if(route.getStartPlanetTypeId() == null) {
					startAccepted = (startPlanet == null);
				} else {
					routePlanet = planetRepo.getPlanetClass(route.getStartPlanetTypeId());
					startAccepted = routePlanet.isAssignableFrom(startPlanet.getClass());
				}
				
				if(route.getTargetPlanetTypeId() == null) {
					targetAccepted = (getTargetPlanet() == null);
				} else if(getTargetPlanet() != null) {
					routePlanet = planetRepo.getPlanetClass(route.getTargetPlanetTypeId());
					targetAccepted = routePlanet.isAssignableFrom(targetPlanet.getClass());
				}
				
				String mvalidStr = mission.validate(this);
				boolean missionValid = mvalidStr == null || mvalidStr.isEmpty();
				
				if(startAccepted && targetAccepted && missionValid) {
					allowed.add(mission);
					continue missions;
				}
			}
		}
		
		return allowed;
	}
	
	@Override
	public ResourcePredicate[] getAllowedResourcePredicates() {
		return new ResourcePredicate[]{new MetalResource(), new CrystalResource(), new DeuteriumResource()};
	}
	
	@Override
	public double getCapacity() {
		double capacity = 0d;
		
		for(FleetBound spec : specs) {
			capacity += spec.getCapacity();
		}
		
		return capacity;
	}
	
	@Override
	public ResourceValueSet getConsumption() {
		double totalConsumption = 0d;
		
		int distance = getDistance();
		Duration duration = getDuration();
		double globalFactor = Integer.valueOf(config.get("fleet_speed")) / 2500;
		
		for(FleetBound spec : specs.filter(new PositiveFilter())) {
			FleetDrive drive = getPreferredDrive(spec.getDrives(startPlanet.getOwner().getSpecs()));
			int specSpeed = drive.getSpeed();
			double consumption = drive.getConsumption();
			
			double spd = 35000 / (duration.getStandardSeconds() * globalFactor - 10);
			spd *= Math.sqrt((double) distance * 10 / specSpeed);
			
			totalConsumption += consumption * distance / 35000 * Math.pow(spd / 10 + 1, 2);
		}
		
		totalConsumption = Math.round(totalConsumption) + 1d;
		
		return Resources.generateStatic(ImmutableMap.of((ResourcePredicate) new DeuteriumResource(), totalConsumption));
	}
	
	@Override
	public int getDistance() {
		if(coordinates.getGalaxy() != startPlanet.getCoordinates().getGalaxy()) {
			return 20000 * Math.abs(coordinates.getGalaxy() - startPlanet.getCoordinates().getGalaxy());
		}
		if(coordinates.getSystem() != startPlanet.getCoordinates().getSystem()) {
			return 2700 + 95 * Math.abs(coordinates.getSystem() - startPlanet.getCoordinates().getSystem());
		}
		if(coordinates.getOrbit() != startPlanet.getCoordinates().getOrbit()) {
			return 1000 + 5 * Math.abs(coordinates.getOrbit() - startPlanet.getCoordinates().getOrbit());
		}
		return 5;
	}
	
	@Override
	public Duration getDuration() {
		double globalFactor = Integer.valueOf(config.get("fleet_speed")) / 2500;
		int distance = getDistance();
		int speed = getSpeed();
		
		long seconds = Math.round((3500 / speedFactor * Math.sqrt((double) distance * 10 / speed) + 10) / globalFactor);
		
		return new Duration(seconds * 1000);
	}
	
	@Override
	public FleetDrive getPreferredDrive(List<FleetDrive> drives) {
		FleetDrive best = null;
		
		for(FleetDrive current : drives) {
			if(best == null || current.getSpeed() > best.getSpeed()) {
				best = current;
			}
		}
		return best;
	}
	
	@Override
	public int getSpeed() {
		int speed = Integer.MAX_VALUE;
		for(FleetBound spec : specs.filter(new PositiveFilter())) {
			FleetDrive drive = getPreferredDrive(spec.getDrives(startPlanet.getOwner().getSpecs()));
			if(drive == null) {
				throw new IllegalStateException("found a ship without drive");
			}
			speed = Math.min(speed, drive.getSpeed());
		}
		return speed;
	}
	
	@Override
	public ResourceValueSet getUsedResources() {
		return Resources.unify(getResources(), getConsumption());
	}
	
	@Override
	public Planet getTargetPlanet() {
		if(targetChanged) {
			targetPlanet = planetRepo.findByCoordinates(coordinates);
			targetChanged = false;
		}
		return targetPlanet;
	}
	
	// simple getters
	
	@Override
	public Map<String, String> getLevels() {
		return levels;
	}
	
	@Override
	public Mission getMission() {
		return mission;
	}
	
	@Override
	public int getMissionId() {
		return missionId;
	}
	
	@Override
	public Map<String, String> getResourceCounts() {
		return resourceCounts;
	}
	
	@Override
	public double getSpeedFactor() {
		return speedFactor;
	}
	
	@Override
	public Planet getStartPlanet() {
		return startPlanet;
	}
	
	// mutable getters
	@Override
	public Coordinates getCoordinates() {
		return coordinates;
	}
	
	@Override
	public ResourceValueSet getResources() {
		return resourceSet;
	}
	
	@Override
	public SpecSet<FleetBound> getSpecs() {
		return specs;
	}
	
	// setter
	@Override
	public void setMissionId(int missionId) {
		this.missionId = missionId;
		
		mission = missionRepo.findOne(missionId);
	}
	
	@Override
	public void setMission(Mission mission) {
		missionId = mission.getMissionId();
		
		this.mission = mission;
	}
	
	@Override
	public void setTargetPlanetId(int targetPlanetId) {
		targetPlanet = planetRepo.findOne(targetPlanetId);
		coordinates = new FleetStartCoordinates(targetPlanet.getCoordinates());
	}
	
	@Override
	public void setSpeedFactor(double speed) {
		this.speedFactor = speed;
	}
	
	@Override
	public void setStartPlanet(Planet startPlanet) {
		this.startPlanet = startPlanet;
		coordinates = new FleetStartCoordinates(startPlanet.getCoordinates());
		targetPlanet = startPlanet;
	}
	
	// classes
	public class FleetStartCoordinates extends DynamicCoordinates {
		public FleetStartCoordinates(int galaxy, int system, int orbit, int kind) {
			super(galaxy, system, orbit, kind);
		}
		
		public FleetStartCoordinates(Coordinates coords) {
			super(coords);
		}
		
		@Override
		protected void onChange() {
			targetChanged = true;
		}
	}
}
