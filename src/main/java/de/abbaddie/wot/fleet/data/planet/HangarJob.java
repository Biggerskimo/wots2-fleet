package de.abbaddie.wot.fleet.data.planet;

import org.joda.time.Duration;

import de.abbaddie.wot.data.resource.ResourceValueSet;
import de.abbaddie.wot.data.spec.Spec;

public class HangarJob {
	protected Spec spec;
	protected int count;
	
	public HangarJob(Spec spec, int count) {
		this.spec = spec;
		this.count = count;
	}
	
	@Override
	public String toString() {
		return spec.getPredicate().getId() + "," + count + ";";
	}
	
	public Duration work(Duration available, double speedFactor) {
		Duration jobDuration = getJobDuration(speedFactor);
		
		if(jobDuration.isShorterThan(available)) {
			spec.setLevel(spec.getLevel() + count);
			count = 0;
			return available.minus(jobDuration);
		}
		
		int possible = (int) (available.getMillis() / getTimePerUnit(speedFactor).getMillis());
		spec.setLevel(spec.getLevel() + possible);
		count -= possible;
		
		return available.minus(new Duration(0).withDurationAdded(getTimePerUnit(speedFactor), possible));
	}
	
	public boolean isFinished() {
		return count == 0;
	}
	
	public Duration getTimePerUnit(double speedFactor) {
		ResourceValueSet rvs = spec.getCosts();
		double costs = rvs.getValue("metal") + rvs.getValue("crystal");
		
		return new Duration(costs / speedFactor * 3600 * 1000);
	}
	
	public Duration getJobDuration(double speedFactor) {
		return new Duration(0).withDurationAdded(getTimePerUnit(speedFactor), count);
	}
}
