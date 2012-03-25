package de.abbaddie.wot.fleet.data.planet;

import java.util.LinkedList;
import java.util.Queue;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.abbaddie.wot.config.Config;
import de.abbaddie.wot.data.planet.Planet;
import de.abbaddie.wot.data.planet.Producer;
import de.abbaddie.wot.data.spec.Spec;
import de.abbaddie.wot.data.spec.SpecRepository;
import de.abbaddie.wot.data.spec.SpecSet;
import de.abbaddie.wot.fleet.data.spec.trait.HangarProductionCatalyst;

@Component
public class HangarProducer implements Producer {
	protected Planet planet;
	protected Queue<HangarJob> jobs;
	protected Duration offset;
	protected double speedFactor;
	
	@Autowired
	protected Config config;
	
	@Autowired
	protected SpecRepository specRepo;
	
	@Override
	public void setPlanet(Planet planet) {
		this.planet = planet;
		
		loadJobs();
		if(planet.get("b_hangar") != null) {
			offset = new Duration((long) (int) planet.get("b_hangar") * 1000);
		} else {
			offset = new Duration(0);
		}
		
		speedFactor = 1d;
		speedFactor *= Double.parseDouble(config.get("game_speed")) / 2500;
		
		SpecSet<HangarProductionCatalyst> catalysts = planet.getSpecs().filter(HangarProductionCatalyst.class);
		for(HangarProductionCatalyst catalyst : catalysts) {
			speedFactor *= catalyst.getHangarCatalysation();
		}
	}
	
	public Planet getPlanet() {
		return planet;
	}
	
	public double getSpeedFactor() {
		return speedFactor;
	}
	
	@Override
	public void produce(DateTime start, DateTime end) {
		if(jobs.isEmpty()) {
			return;
		}
		
		// produce
		Duration dur = new Duration(start, end).plus(offset);
		
		HangarJob job = jobs.peek();
		dur = job.work(dur);
		
		while(job.isFinished()) {
			jobs.remove();
			job = jobs.peek();
			dur = job.work(dur);
		}
		offset = dur;
		
		// save
		saveJobs();
		planet.set("b_hangar", offset.getMillis() / 1000);
	}
	
	public void loadJobs() {
		String all = (String) planet.get("b_hangar_id");
		jobs = new LinkedList<>();
		
		if(all == null || all.isEmpty()) {
			return;
		}
		
		String[] parts = all.split(";");
		for(String jobStr : parts) {
			if(jobStr.isEmpty()) {
				continue;
			}
			String[] jobParts = jobStr.split(",");
			int specId = Integer.parseInt(jobParts[0]);
			int count = Integer.parseInt(jobParts[1]);
			
			Spec spec = planet.getSpecs().get(specRepo.findOne(specId));
			
			jobs.add(new HangarJob(this, spec, count));
		}
	}
	
	public void saveJobs() {
		String str = "";
		
		for(HangarJob job : jobs) {
			str += job.toString();
		}
		
		planet.set("b_hangar_id", str);
	}
}
