"use strict";

var WotFleetlist = {
	fleets: [],
		
	build: function($container) {
		$container.append("<ul class=\"fleetlist pageList\" />");
	},
	
	fleetlistHandler: function(data) {
		WotFleetlist.fleets = data.fleets;
	},
	
	refreshAndRender: function($container) {
		WotRequest.start("fleetlist", null, function() {
			WotHandler.render($container, "fleetlist", false);
		});
		WotHandler.render($container, "fleetlist", false);
	},
	
	fleetlistCancelResultHandler: function(data) {
		WotLib.showActionResult(data)
	},
	
	cancel: function(fleetId) {
		var handler = WotLib.getActionResultHandler({
			successMessage: "Die Flotte wurde zurückgerufen.",
			successCallback: WotHandler.rerender,
			errorCallback: WotHandler.rerender
		});
		WotRequest.start("fleetlist/cancel", "fleetId=" + fleetId, handler, "fleetlistCancelResult");
	},
	
	render: function($container) {
		var $fleetUl = $container.find(".fleetlist");
		
		$fleetUl.empty();
		
		$.each(WotFleetlist.fleets, function(key, fleet) {
			var $li = $("<li class=\"menuContainer\" />")
				.addClass("fleet" + fleet.fleetId)
				.append($("<dl class=\"fleetInfo\" />")
					.append($("<dt class=\"impactTime\" />").text("Einschlag"))
					.append($("<dd class=\"impactTime time\" />").time(fleet.impactTime, {format: "relfut"}))
					.append($("<dt class=\"returnTime\" />").text("Rückkehr"))
					.append($("<dd class=\"returnTime time\" />").time(fleet.impactTime, {format: "relfut"})))
				.append($("<p class=\"fleetText=\" />")
					.append($("<b class=\"mission\"/>").text(WotFleet.getMissionName(fleet.missionId)))
					.append(" vom ")
					.append(WotLib.formatPlanet(fleet.startPlanet, { dative: true }))
					.append(" zum ")
					.append(WotLib.formatPlanet(fleet.targetPlanet, { dative: true }))
					.append($("<ul class=\"menuActions\" />")
						.append($("<li />")
							.append($("<a href=\"javascript:void(0);\" />").append($("<span />")
								.click(function() {
									WotFleetlist.cancel(fleet.fleetId);
								})
								.text("Zurückziehen"))))));
			$li.click(function(e) {
				WotFleetlist.toggleDetails.call($li, fleet);
			});
			$fleetUl.append($li);
		});
	},
	toggleDetails: function(fleet) {
		var $found = $(this).find(".pageListDetails");
		
		if($found.size() == 0) {
			var $details = $("<div />");
			WotFleet.showDetails($details, fleet.specs, fleet.resources);
			
			var $elem = $("<div class=\"pageListDetails\" />").html($details).hide();
			$(this).append($elem);
			$elem.slideDown(150);
		}
		else {
			$found.slideToggle(150);
		}
	}
};
WotHandler.addModule("fleetlist", {
	requestHandler: { fleetlist: WotFleetlist.fleetlistHandler },
	navigationLink: { Flottenliste: "fleetlist" },
	page : { fleetlist: WotFleetlist }
});