var GiantBomb = {
	url: "GiantBombApi",
	
	resource: {
		platforms: "platforms",
		games: "games"
	},

	getGames: function(gameName, handlerSuccess, handlerError) {
		$.get({
			url : GiantBomb.url,
			data : {
				resource: GiantBomb.resource.games,
				game : gameName.trim()
			},
			success : function(data, textStatus, jqXHR) {
				handlerSuccess(data, textStatus, jqXHR);
			},
			error : function(jqXHR, textStatus, errorThrown) {
				handlerError(jqXHR, textStatus, errorThrown);
			}
		});
	},
	
	getPlatforms: function(platformName, handlerSuccess, handlerError) {
		$.get({
			url : GiantBomb.url,
			data : {
				resource: GiantBomb.resource.platforms,
				platform : platformName.trim()
			},
			success : function(data, textStatus, jqXHR) {
				handlerSuccess(data, textStatus, jqXHR);
			},
			error : function(jqXHR, textStatus, errorThrown) {
				handlerError(jqXHR, textStatus, errorThrown);
			}
		});
	}
};