package de.abbaddie.wot.fleet.data.planet;

import java.util.LinkedList;
import java.util.Queue;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import de.abbaddie.wot.data.planet.Planet;
import de.abbaddie.wot.data.planet.Producer;
import de.abbaddie.wot.data.spec.Spec;
import de.abbaddie.wot.data.spec.SpecRepository;
import de.abbaddie.wot.data.spec.SpecSet;
import de.abbaddie.wot.fleet.data.spec.trait.HangarProductionCatalyst;

@Service
@DependsOn("config")
@Lazy
public class HangarProducer implements Producer {
	@Value("${factor.construction.hangar}")
	protected double globalFactor;
	
	@Autowired
	protected SpecRepository specRepo;
	
	public Queue<HangarJob> loadJobs(Planet planet) {
		String all = (String) planet.get("hangar_jobs");
		Queue<HangarJob> jobs = new LinkedList<>();
		
		if(all == null || all.isEmpty()) {
			return jobs;
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
			
			jobs.add(new HangarJob(spec, count));
		}
		
		return jobs;
	}
	
	public void saveJobs(Planet planet, Queue<HangarJob> jobs) {
		String str = "";
		
		for(HangarJob job : jobs) {
			str += job.toString();
		}
		
		planet.set("hangar_jobs", str);
	}
	
	@Override
	public void produce(Planet planet, DateTime start, DateTime end) {
		// load jobs
		Queue<HangarJob> jobs = loadJobs(planet);
		
		// load duration
		Duration offset;
		if(planet.get("hangar_offset") != null) {
			offset = new Duration(planet.get("hangar_offset"));
		} else {
			offset = new Duration(0);
		}
		
		// load speed
		double speedFactor = globalFactor;
		
		SpecSet<HangarProductionCatalyst> catalysts = planet.getSpecs().filter(HangarProductionCatalyst.class);
		for(HangarProductionCatalyst catalyst : catalysts) {
			speedFactor *= catalyst.getHangarCatalysation();
		}
		
		// produce
		if(jobs.isEmpty()) {
			return;
		}
		
		Duration dur = new Duration(start, end).plus(offset);
		
		HangarJob job = jobs.peek();
		dur = job.work(dur, speedFactor);
		
		while(job.isFinished()) {
			jobs.remove();
			job = jobs.peek();
			dur = job.work(dur, speedFactor);
		}
		offset = dur;
		
		// save
		saveJobs(planet, jobs);
		planet.set("hangar_offset", offset.getMillis() / 1000);
	}
}
