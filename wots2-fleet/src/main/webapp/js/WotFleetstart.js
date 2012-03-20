"use strict";

var WotFleetstart = {
	specs: [],
		
	build: function($container) {
		var $form = $("<form class=\"fleetstart form-horizontal\">");
		
		$form.submit(WotFleetstart.fleetstartSubmit);
		$form.append("<ul class=\"fleetstartSpecs unstyled\">");
		$form.append("<p class=\"alert alert-warning fleetstartNoSpecs\">Keine Schiffe vorhanden.</p>");
		$form.append(
				$("<div class=\"coordinates control-group\" />")
				.append("<label class=\"control-label\" for=\"coordinatesGalaxy\">Ziel</label>")
				.append($("<div class=\"controls\" />")
					.append("<input type=\"number\" id=\"coordinatesGalaxy\" name=\"coordinates.galaxy\" size=\"2\" value=\"0\" />")
					.append(":")
					.append("<input type=\"number\" name=\"coordinates.system\" size=\"3\" value=\"0\" />")
					.append(":")
					.append("<input type=\"number\"name=\"coordinates.orbit\" size=\"3\" value=\"0\" />")
					.append("<select name=\"coordinates.kind\"><option value=\"1\">Planet</option>"
							+ "<option value=\"2\">TF</option><option value=\"3\">Mond</option></select>")));
		var opts = "";
		for(var i = 10; i <= 100; i += 10) opts += "<option value=\"" + (i / 100) + "\">" + i + "%</option>";
		$form.append(
				$("<div class=\"speed control-group\" />")
				.append("<label class=\"control-label\" for=\"speedFactor\">Geschwindigkeit</label>")
				.append($("<div class=\"controls\" />")
					.append("<select name=\"speedFactor\">" + opts + "</select>")));
		opts = "";
		$.each(WotFleet.missions, function(missionId, name) {
			opts += "<label class=\"radio\"><input type=\"radio\" name=\"missionId\" value=\"" + missionId + "\" />" + name + "</label>";
		});
		$form.append(
				$("<div class=\"mission control-group\" />")
				.append("<label class=\"control-label\">Auftrag</label>")
				.append("<div class=\"controls\">" + opts + "</div>"));
		$.each({"metal": "Metal", "crystal": "Crystal", "deuterium" : "Deuterium"}, function(lower, upper) {
			var name = WotLib.getResourceName(lower);
			$form.append(
					$("<div class=\""+lower+" control-group\" />")
					.append("<label class=\"control-label\" for=\"resourceCounts"+upper+"\">"+name+"</label>")
					.append($("<div class=\"controls\" />")
						.append("<input type=\"number\" id=\"resourceCounts"+upper+"\" name=\"resourceCounts["+lower+"]\" size=\"8\" value=\"0\" />")
						.append($("<span class=\"inline-help\" />")
							.click(function() {
								$("#resourceCounts"+upper).val($("#resourceCounts"+upper).val() == 0 ? Math.round(WotHeader.currentPlanetData[lower]) : 0);
							}))));
			});
		
		$container.append($form);
	},
	
	render: function($container) {
		var $specs = $container.find(".fleetstartSpecs");
		$specs.empty();
		
		$.each(WotFleetstart.specs, function(specId, count) {
			var id = "levels"+ specId;
			var $li = $("<li class=\"unstyled\" />")
				.append($("<label class=\"control-label\" />")
					.attr("for", id)
					.text(WotLib.getSpecName(specId)))
				.append($("<div class=\"controls\" />")
					.append($("<input type=\"number\" size=\"6\" />")
						.attr("name", "levels["+ specId + "]")
						.attr("id", id)
						.val(0))
					.append($("<span class=\"inline-help\" />")
						.click(function() {
							$("#" + id).val($("#" + id).val() == 0 ? count : 0);
						})
						.text("(" + count + ")")));
			$specs.append($li);
		});
		
		if(WotFleetstart.specs == null) {
			$(".fleetstartNoSpecs").show();
		}
		else {
			$(".fleetstartNoSpecs").hide();
		}
		
		$(".fleetstartMetal .inline-help")
			.text("(" + WotLib.formatNumberShort(WotHeader.currentPlanetData.metal) + ")");
		$(".fleetstartCrystal .inline-help")
			.text("(" + WotLib.formatNumberShort(WotHeader.currentPlanetData.crystal) + ")");
		$(".fleetstartDeuterium .inline-help")
			.text("(" + WotLib.formatNumberShort(WotHeader.currentPlanetData.deuterium) + ")");
	},
	
	fleetstartHandler: function(data) {
		WotFleetstart.specs = data.specs;
		
		WotHandler.showPage("fleetstart", [true]);
	},
	
	fleetstartResultHandler: function(data) {
		WotLib.showBindingResult(data.result);
	},
	
	fleetstartSubmit: function() {
		WotRequest.start("fleetstart/fire", $(".fleetstart").serialize());
		
		return false;
	}
};
WotHandler.addModule("fleetstart", {
	requestHandler: {
		fleetstart: WotFleet.fleetstartHandler,
		fleetstartResult: WotFleet.fleetStartResultHandler },
	navigationLink: {
		Flottenstart: "fleetstart" },
	page: { fleetstart: WotFleetstart }
});