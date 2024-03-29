"use strict";

var WotFleetstart = {
	specs: [],
		
	build: function($container) {
		var $form = $("<form class=\"fleetstart form-horizontal\">");
		
		$form.submit(WotFleetstart.fleetstartSubmit);
		$form.append("<ul class=\"fleetstartSpecs\">");
		$form.append(
				$("<div class=\"coordinates\" />")
				.append("<label for=\"coordinatesGalaxy\">Ziel</label>")
				.append($("<div />")
					.append("<input type=\"number\" id=\"coordinatesGalaxy\" name=\"coordinates.galaxy\" size=\"2\" value=\"0\" min=\"1\" max=\"4\" />")
					.append(":")
					.append("<input type=\"number\" id=\"coordinatesSystem\" name=\"coordinates.system\" size=\"3\" value=\"0\" min=\"1\" max=\"499\" />")
					.append(":")
					.append("<input type=\"number\" id=\"coordinatesOrbit\" name=\"coordinates.orbit\" size=\"3\" value=\"0\" min=\"1\" max=\"15\" />")
					.append("<select id=\"coordinatesKind\" name=\"coordinates.kind\"><option value=\"1\">Planet</option>"
							+ "<option value=\"2\">TF</option><option value=\"3\">Mond</option></select>")));
		
		var opts = "";
		for(var i = 100; i >= 10; i -= 10) opts += "<option value=\"" + (i / 100) + "\">" + i + "%</option>";
		$form.append(
				$("<div class=\"speed\" />")
				.append("<label for=\"speedFactor\">Geschwindigkeit</label>")
				.append($("<div />")
					.append("<select name=\"speedFactor\" id=\"speedFactor\">" + opts + "</select>")));
		
		opts = "";
		$.each(WotFleet.missions, function(missionId, name) {
			opts += "<option value=\"" + missionId + "\">" + name + "</option>";
		});
		$form.append(
				$("<div class=\"mission\" />")
				.append("<label for=\"missionId\">Auftrag</label>")
				.append("<div><select name=\"missionId\" id=\"missionId\">" + opts + "</select></div>"));
		$.each({"metal": "Metal", "crystal": "Crystal", "deuterium" : "Deuterium"}, function(lower, upper) {
			var name = WotLib.getResourceName(lower);
			$form.append(
					$("<div class=\""+lower+"\" />")
					.append("<label for=\"resourceCounts"+upper+"\">"+name+"</label>")
					.append($("<div />")
						.append("<input type=\"number\" id=\"resourceCounts"+upper+"\" name=\"resourceCounts["+lower+"]\" size=\"8\" value=\"0\" min=\"0\" />")
						.append($("<span class=\"inline-help\" />")
							.click(function() {
								$("#resourceCounts"+upper).val($("#resourceCounts"+upper).val() == 0 ? Math.round(WotHeader.currentPlanetData[lower]) : 0);
							}))));
			});
		$form.append(
				$("<div class=\"submit\" />")
				.append("<input type=\"submit\" value=\"Starten\" />"));
		
		$container.append($form);
	},
	
	refreshAndRender: function($container) {
		WotRequest.start("fleetstart", null, function() {
			WotHandler.render($container, "fleetstart", false);
			WotHandler.fixHash();
		});
		WotHandler.render($container, "fleetstart", false);
	},
	
	render: function($container) {
		var $specs = $container.find(".fleetstartSpecs");
		
		$.each(WotFleetstartDetails.specs, function(specId, data) {
			var count = WotFleetstart.specs[specId];
			var id = "levels"+ specId;
			var sid = "#" + id;
			
			if(!count) {
				$(sid).closest("li").remove();
			}
			else if($(sid).size()) {
				$(sid).attr("max", count);
				$(sid).parent().find("span.inline-help")
					.unbind("click")
					.click(function() {
						$(sid).val($(sid).val() == 0 ? count : 0);
					})
					.text("(" + count + ")");
			}
			else {
				var $li = $("<li />")
					.append($("<label />")
						.attr("for", id)
						.text(WotLib.getSpecName(specId)))
					.append($("<div />")
						.append($("<input type=\"number\" size=\"6\" min=\"0\" />")
							.attr("name", "levels["+ specId + "]")
							.attr("id", id)
							.attr("max", parseInt(count))
							.data("specId", specId)
							.val(0))
						.append($("<span class=\"inline-help\" />")
							.click(function() {
								$(sid).val($(sid).val() == 0 ? count : 0);
							})
							.text("(" + count + ")")));
				$specs.append($li);
			}
		});
		
		WotLib.eraseActionResult();
		if(WotFleetstart.specs == null || $.isEmptyObject(WotFleetstart.specs)) {
			WotLib.addActionResult("warning", ["Es sind keine Schiffe vorhanden!"]);
		}
		
		$.each({"metal": "Metal", "crystal": "Crystal", "deuterium" : "Deuterium"}, function(lower, upper) {
			$(".fleetstart"+upper+" .inline-help").text("(" + WotLib.formatNumberShort(WotHeader.currentPlanetData[lower]) + ")");
			$("#resourceCounts"+upper).attr("max", WotHeader.currentPlanetData[lower]);
		});
		
		WotSwiper.showRight({
			page: "fleetstartDetails",
			renderArgs: [$container],
			changeCenter: false,
			swapCenter: false,
			swapLevel: 0.2,
			maxSwipe: 0.9
		});
	},
	
	planetChangeHook: function(part) {
		if(WotHandler.currentPage == "fleetstart") {
			WotHandler.rerender(true);
		}
	},
	
	fleetstartHandler: function(data) {
		WotFleetstart.specs = data.specs;
	},
	
	fleetstartSubmit: function() {
		var handler = WotLib.getActionResultHandler({
			successMessage: "Die Flotte wurde versendet.",
			successCallback: WotHandler.rerender
		});
		WotRequest.start("fleetstart/fire", $(".fleetstart").serialize(), handler, "fleetstartResult");
		
		return false;
	}
};
WotHandler.addModule("fleetstart", {
	requestHandler: { fleetstart: WotFleetstart.fleetstartHandler, planetChange: WotFleetstart.planetChangeHook },
	navigationLink: {
		Flottenstart: "fleetstart" },
	page: { fleetstart: WotFleetstart }
});