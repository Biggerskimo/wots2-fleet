"use strict";

var WotFleetstartDetails = {
	globalFactor: 1, // to be overwritten
	specs : {}, // to be overwritten
	
	page: {
		build: function($container) {
			var $div = $("<div />");
			$container.append($div);
			var $dl = $("<dl />");
			$div.append($dl);
			
			$dl.append("<dt class=\"distance\">Entfernung</dt>");
			$dl.append("<dd class=\"distance\">0</dd>");
			
			$dl.append("<dt class=\"oneTime\">Wegzeit</dt>");
			$dl.append($("<dd class=\"oneTime\" />").time(new Date(), {up: 1, format: "relfut"}));
			
			$dl.append("<dt class=\"impactTime\">Einschlag</dt>");
			$dl.append($("<dd class=\"impactTime\" />").time(new Date(), {up: 1, format: "abs"}));
			
			$dl.append("<dt class=\"twoTime\">Gesamtzeit</dt>");
			$dl.append($("<dd class=\"twoTime\" />").time(new Date(), {up: 1, format: "relfut"}));
			
			$dl.append("<dt class=\"returnTime\">Rückkehr</dt>");
			$dl.append($("<dd class=\"returnTime\" />").time(new Date(), {up: 1, format: "abs"}));
			
			$dl.append("<dt class=\"consumption\">Deuteriumverbrauch</dt>");
			$dl.append("<dd class=\"consumption\">0</dd>");
			
			$dl.append("<dt class=\"speed\">Max. Geschwindigkeit</dt>");
			$dl.append("<dd class=\"speed\">0</dd>");
			
			$dl.append("<dt class=\"capacity\">Ladekapazität</dt>");
			$dl.append("<dd class=\"capacity\">0</dd>");
		},
		
		refreshAndRender: function($container, $startContainer) {
			WotFleetstartDetails.page.render($container, $startContainer);
		},
		
		render: function($container, $startContainer) {
			var calc = $startContainer.data("calc");
			if(!calc) {
				calc = new WotFleetstartDetails.Calculator($startContainer);
				$startContainer.data("calc", calc);
			}
			
			calc.recalc();
			$container.find("dd.distance").text(calc.getDistance());
			$container.find("dd.oneTime").time(calc.getImpactTime(), {up: 1, manipulator: "html", format: "relfut"});
			$container.find("dd.impactTime").time(calc.getImpactTime(), {up: 1, manipulator: "html", format: "abs"});
			$container.find("dd.twoTime").time(calc.getReturnTime(), {up: 1, manipulator: "html", format: "relfut"});
			$container.find("dd.returnTime").time(calc.getReturnTime(), {up: 1, manipulator: "html", format: "abs"});
			$container.find("dd.consumption").text(calc.getConsumption());
			$container.find("dd.speed").text(calc.getSpeed());
			$container.find("dd.capacity").text(calc.getLoadCapacity());
			
			$startContainer.find("input, select").unbind("change").change(function() {
				WotFleetstartDetails.page.render($container, $startContainer);
			});
		},
	},
	
	Calculator: function($container) {
		if(!(this instanceof WotFleetstartDetails.Calculator)) {
			return new WotFleetstartDetails.calculator($container);
		}
		
		var that = this;
		
		this.lastCalc = new Date(1980);
		this.data = {};
		
		this.recalc = function() {
			if(+new Date() - that.lastCalc < 100) {
				return;
			}
			that.lastCalc = new Date();
			that.calcDistance();
			that.calcSpecs();
			that.calcSpeedFactor();
			that.calcSpeed();
			that.calcDuration();
			that.calcImpactTime();
			that.calcReturnTime();
			that.calcConsumption();
			that.calcCapacity();
			that.calcLoadCapacity();
		};
		
		this.getDistance = function() {
			return that.data.distance;
		};
		
		this.getSpecs = function() {
			return that.data.specs;
		};
		
		this.getSpeedFactor = function() {
			return that.data.speedFactor;
		};
		
		this.getSpeed = function() {
			return that.data.speed;
		};
		
		this.getDuration= function() {
			return that.data.duration;
		};
		
		this.getImpactTime = function() {
			return that.data.impactTime;
		};
		
		this.getReturnTime = function() {
			return that.data.returnTime;
		};
		
		this.getConsumption = function() {
			return that.data.consumption;
		};
		
		this.getCapacity = function() {
			return that.data.capacity;
		};
		
		this.getLoadCapacity = function() {
			return that.data.loadCapacity;
		};
		
		this.calcDistance = function() {
			var start = WotHeader.currentPlanetData.planet.coords;
			var target = [$container.find("#coordinatesGalaxy").val(), $container.find("#coordinatesSystem").val(),
			              $container.find("#coordinatesOrbit").val(), $container.find("#coordinatesKind").val()];
			
			if(start[0] != target[0]) {
				that.data.distance =  20000 * Math.abs(start[0] - target[0]);
			}
			else if(start[1] != target[1]) {
				that.data.distance =  2700 + 95 * Math.abs(start[1] - target[1]);
			}
			else if(start[2] != target[2]) {
				that.data.distance =  1000 + 5 * Math.abs(start[2] - target[2]);
			}
			else {
				that.data.distance = 5;
			}
		};
		
		this.calcSpecs = function() {
			var specs = {};
			$container.find("input[id^=\"levels\"]").each(function() {
				var specId = $(this).data("specId");
				var count = $(this).val();
				
				if(count > 0 && WotFleetstartDetails.specs[specId])
					specs[specId] = count;
			});
			
			that.data.specs = specs;
		};
		
		this.calcSpeedFactor = function() {
			that.data.speedFactor = parseFloat($container.find("#speedFactor").val());
		};
		
		this.calcSpeed = function() {
			var speed = Number.MAX_VALUE;
			
			$.each(that.getSpecs(), function(specId, count) {
				speed = Math.min(speed, WotFleetstartDetails.specs[specId][0]);
			});
			that.data.speed = speed;
		};
		
		this.calcDuration = function() {
			var globalFactor = WotFleetstartDetails.globalFactor;
			var speed = that.getSpeed();
			var speedFactor = that.getSpeedFactor();
			var distance = that.getDistance();
			
			var seconds = Math.round((3500 / speedFactor * Math.sqrt(distance * 10 / speed) + 10) / globalFactor);
			
			that.data.duration = new Date(seconds * 1000);
		};
		
		this.calcImpactTime = function() {
			that.data.impactTime = (+new Date()) + (+that.getDuration());
		};
		
		this.calcReturnTime = function() {
			that.data.returnTime = (+new Date()) + (that.getDuration() * 2);
		};
		
		this.calcConsumption = function() {
			var total = 0;
			var globalFactor = WotFleetstartDetails.globalFactor;
			var distance = that.getDistance();
			var duration = that.getDuration() / 1000;
			
			$.each(that.getSpecs(), function(specId, count) {
				var speed = WotFleetstartDetails.specs[specId][0];
				var consumption = WotFleetstartDetails.specs[specId][1];
				
				var spd = 35000 / (duration * globalFactor - 10);
				spd *= Math.sqrt(distance * 10 / speed);
				
				total += consumption * distance / 35000 * Math.pow(spd / 10 + 1, 2);
			});
			
			total = Math.round(total) + 1;
			
			that.data.consumption = total;
		};
		
		this.calcCapacity = function() {
			var total = 0;
			
			$.each(that.getSpecs(), function(specId, count) {
				var capacity = WotFleetstartDetails.specs[specId][2];
				
				total += capacity * count;
			});
			
			that.data.capacity = total;
		};
		
		this.calcLoadCapacity = function() {
			that.data.loadCapacity = that.getCapacity() - that.getConsumption();
		};
		
		this.recalc();
	}
};
WotHandler.addModule("fleetstartDetails", {
	page: { fleetstartDetails: WotFleetstartDetails.page }
});