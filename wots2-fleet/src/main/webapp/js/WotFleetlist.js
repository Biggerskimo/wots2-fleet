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
		var handler = WotLib.getActionResultHandler("Die Flotte wurde zurückgerufen.", WotHandler.rerender, WotHandler.rerender);
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
					.append($("<dd class=\"impactTime time\" />")
						.append($("<time />")
								.attr("datetime", new Date(fleet.impactTime).toISOString())
								.text("BAM!")))
					.append($("<dt class=\"returnTime\" />").text("Rückkehr"))
					.append($("<dd class=\"returnTime time\" />")
						.append($("<time />")
								.attr("datetime", new Date(fleet.returnTime).toISOString())
								.text("BAUM!"))))
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
			$fleetUl.append($li);
		});
	}
};
WotHandler.addModule("fleetlist", {
	requestHandler: { fleetlist: WotFleetlist.fleetlistHandler,
		fleetlistCancelResult: WotLib.getActionResultHandler("Die Flotte wurde zurückgerufen.") },
	navigationLink: { Flottenliste: "fleetlist" },
	page : { fleetlist: WotFleetlist }
});