$(document).ready(function() {
	var map;
	var initGmap = function() {
		var mapOptions = {
			zoom: 8,
			center: {
				lat: 48.6844685,
				lng: 2.3862227
			}
		};
		
		map = new google.maps.Map($("#gmaps")[0], mapOptions);
	}
    google.maps.event.addDomListener(window, "load", initGmap);


});