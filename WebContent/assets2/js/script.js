var autocomplete;
var postalCode= null;

/* Used for Google Places API for the inputs related to addresses */
function autocompleteInitialise() {
	autocomplete = new google.maps.places.Autocomplete(document.getElementById('autocompleteAddress'), {
		types : [ 'geocode' ],
		componentRestrictions : {
			country : 'fr'
		}
	});
	autocomplete.addListener('place_changed', function() {
		var place = autocomplete.getPlace();
		for (var i = 0; i < place.address_components.length; i++) {
			if (place.address_components[i].types[0] == 'postal_code') {
				postalCode = place.address_components[i].long_name;
			}
		}
	});
}

/* Return true if the string only consists in numbers and letters */
function isAlphaNumString(string) {
	return /^[ a-zA-Z0-9]+$/i.test(string);
}

/* Check if the inputs are all filled, if both pseudo and password are alphanum strings 
 * and if password confirmation matches password */
function checkCreateAccountForm() {
	var pseudo = document.getElementById("subscription_pseudo");
	
}