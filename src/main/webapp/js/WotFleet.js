"use strict";

var WotFleet = {
	// startup
	startup: function() {
		WotGalaxy.setMoonAction(WotFleet.moonScanAction);
	},
	
	// ovents
	oventParser: {
		text: function(ovent) {
			var $ul = $("<ul />");
			$.each(ovent.fleets, function(key, fleet) {
				var $li = $("<li />");
				var str = "";
				var own = (WotHeader.currentPlanetData.planet.ownerId == fleet.ownerId);
				
				$li.addClass(own ? "own" : "foreign");
				$li.addClass(fleet.cssClass);
				$li.addClass(fleet.passage);
				
				$li.append("Eine ");
				
				if(own)
					$li.append("deiner Flotten");
				else if(fleet.missionId == 1 || fleet.missionId == 11) // TODO modularise
					$li.append("feindliche Flotte");
				else
					$li.append("fremde Flotte");
				
				$li.append(" vom ");
				
				$li.append(WotLib.formatPlanet({
						coords: fleet.startCoords,
						name: fleet.startPlanetName,
						planetId: fleet.startPlanetId,
						ownerId: fleet.ownerId
					}, { dative: true }));
				
				if(fleet.passage == "flight")
					$li.append(" erreicht den ");
				else if(fleet.passage == "return")
					$li.append(" kehrt vom ");
				else // standBy
					$li.append(" beginnt ihren Rückflug vom ");
				
				$li.append(WotLib.formatPlanet({
						coords: fleet.targetCoords,
						name: fleet.targetPlanetName,
						planetId: fleet.targetPlanetId,
						ownerId: fleet.ofiaraId
					}, { dative: true }));
				
				if(fleet.passage == "return")
					$li.append(" zurück. Ihr Auftrag lautete: ");
				else // flight, standBy
					$li.append(". Ihr Auftrag lautet: ");
				
				$li.append(WotFleet.getMissionName(fleet.missionId));
				
				
				$ul.append($li);
			});
			
			return $ul;
		},
		details: function(ovent) {
			var $container = $("<ul />");
			
			ovent.fleets.forEach(function(fleet) {
				var $li = $("<li />");
				WotFleet.showDetails($li, fleet.specs, fleet.resources);
				$container.append($li);
			});
			return $container;
		}
	},
	
	showDetails: function($container, specs, resources) {
		var $ships = $("<p />");
		$ships.append("<b>Schiffe</b>: ");
		var $list = $("<ul class=\"oventFleetSpecs\" />");
		$.each(specs, function(specId, count) {
			$list.append($("<li />").append(count + " " + WotLib.getSpecName(specId)));
		});
		$ships.append($list);
		$container.append($ships);
		
		var $resources = $("<p />");
		$resources.append("<b>Rohstoffe</b>: ");
		var text;
		switch(true) {
			case resources.metal > 0 && resources.crystal > 0 && resources.deuterium > 0:
				text = resources.metal + " Metall, " +
						resources.crystal + " Kristall und " +
						resources.deuterium + " Deuterium";
				break;
			case resources.metal > 0 && resources.crystal > 0:
				text = resources.metal + " Metall und " +
						resources.crystal + " Kristall";
				break;
			case resources.metal > 0 && resources.deuterium > 0:
				text = resources.metal + " Metall und " +
						resources.deuterium + " Deuterium";
				break;
			case resources.crystal > 0 && resources.deuterium > 0:
				text = resources.crystal + " Kristall und " +
						resources.deuterium + " Deuterium";
				break;
			case resources.metal > 0:
				text = resources.metal + " Metall";
				break;
			case resources.crystal > 0:
				text = resources.crystal + " Kristall";
				break;
			case resources.deuterium > 0:
				text = resources.deuterium + " Deuterium";
				break;
			default:
				text = "Keine Rohstoffe";
		}
		if(text) {
			$resources.append($("<span />").text(text));
			$container.append($resources);
		}
	},
	
	// galaxy
	scanActionProvider: function(row) {
		var $button = $("<li class=\"scan\" />");
		$button.click(function() {
			WotFleet.scan(row.kinds[1].planetId, row.coords);
		});
		
		return $button;
	},
	
	moonScanAction: function(row) {
		WotFleet.scan(row.kinds[3].planetId, row.coords);
	},
	
	scan: function(planetId, coords) {
		var handler = WotLib.getActionResultHandler({
			successMessage: "Die Spionagesonde wurde versendet.",
			cumulative: true,
			prefix: "["+coords[0]+":"+coords[1]+":"+coords[2]+"]: "
		});
		
		WotRequest.start("fleetstart/fire/espionage", "levels[210]=1&targetPlanetId=" + planetId, handler, "fleetstartResult");
	},
	
	
	// utils
	missions: {
		1: "Angriff",
		3: "Transport",
		4: "Stationierung",
		5: "Zerstörung",
		6: "Spionage",
		8: "Abbau",
		9: "Kolonisierung"
	},
	getMissionName: function(missionId) {
		// TODO modularise
		switch(missionId) {
			case 1:
				return "Angriff";
			case 3:
				return "Transport";
			case 4:
				return "Stationierung";
			case 5:
				return "Zerstörung";
			case 6:
				return "Spionage";
			case 8:
				return "Abbau";
			case 9:
				return "Kolonisierung";
			case 11:
				return "Verbandsangriff";
			case 12:
				return "Halten";
			case 20:
				return "Raketenangriff";
			default:
				return "Weltzerstörung";
		}
	}
};
WotHandler.addModule("fleet", {
	oventParser: {fleet: WotFleet.oventParser},
	galaxyRowAction: [WotFleet.scanActionProvider],
	startup: [WotFleet.startup]
});