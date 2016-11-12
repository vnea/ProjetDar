$(document).ready(function() {
	// CreateGameSession
    var CGS = {
        init: function() {
            // Init datetimepicker
            $("#datetimepicker1").datetimepicker({
                locale: "fr",
                format: "DD-MM-YYYY HH:mm"
            });
            
            // Init games and platforms
            CGS.games.init();
            CGS.platforms.init();
            
            // Add remove handler for platforms and games
            $('span[class*="resource"]').each(function() {
        		$(this).click(function() {
            		$(this).closest("div").remove();
        		})
        	});
        },

        // Timer for searching text
        timerSearch: 400,
        
        // Timer for on keyup
        timerKeyUp: 250,
        
        // CSS class message
        classMessage: {
        	success: "successMessage",
        	error: "errorMessage"
        },
        
        resetText: function(resultElem) {
        	$(resultElem).text("");
            $(resultElem).removeClass(CGS.classMessage.success);
            $(resultElem).removeClass(CGS.classMessage.error);
        },
        
        createInput: function(list, inputName, inputValue) {
        	// Only create input its value is non empty && if it does not already exist
        	inputValue = inputValue.trim();
        	if (inputValue.length && !$(list).find("input")
        									 // Ignore case sensitive
						        			 .filter(function() {
						        			     return this.value.toLowerCase() == inputValue.toLowerCase();
						        			 })
						        			 .length) {
	        	var $divTag = $("<div>", {
	    			class: "input-group"
	    		});
	    		
	    		var $inputTag = $("<input>", {
	    			value: inputValue,
	    			name: inputName,
	    			readonly: true
	    		});
	    		
	    		var $spanTag = $("<span>", {
	    			class: "resource cursor-pointer",
	    			click: function() {
	    				$divTag.remove();
	    			}
	    		});
	    		
	    		var $iTag = $("<i>", {
	    			class: "glyphicon glyphicon-minus"
	    		});
	    		
	    		$spanTag.append($iTag);
	    		$divTag.append($inputTag).append($spanTag);
	    		
	    		$(list).append($divTag);
        	}
        },
        
        /********************
         * GAMES            *
         ********************/
        games: {
            init: function() {
            	// Search games on keyup
        		var timerGames;
        		$(CGS.games.elements.inputSearch).keyup(function() {
        			clearTimeout(timerGames);
        			var value = $(this).val().trim();
        			
        			// Input has a value, we do search
        			if (value.length) {
        				timerGames = setTimeout(function() {
	                		// Remove all games found
	                		$(CGS.games.elements.datalist).empty();
	    	
	                		// Show that we are searching for games
	                		CGS.games.showSearchingText();
	    	
	                		// Call server API to get games
	                		GiantBomb.getGames(
	                			value,
                				CGS.games.handlers.success,
                				CGS.games.handlers.error
	                		);
	        			}, CGS.timerKeyUp);
        			}
        			else {
                		$(CGS.games.elements.datalist).empty();
                    	CGS.resetText(CGS.games.elements.result);
        			}
        		});
            	
            	// Add input game when clicking on + btn
            	$(CGS.games.elements.btnAdd).click(function() {
            		CGS.createInput(
            				CGS.games.elements.list,
            				CGS.games.elements.inputList,
            				$(CGS.games.elements.inputSearch).val()
            		);
            	});
            },

            // Html elements
            elements: {
            	btnSearch: $("#btn-search-games"),
            	btnAdd: $("#btn-add-games"),
                datalist: $("#datalist-games"),
                inputSearch: $("#input-search-games"),
                inputList: "input-list-games",
                result: $("#result-search-games"),
                list: $("#list-games")
            },

            // Handlers when calling API server
            handlers: {
                success: function(data, textStatus, jqXHR) {
                    var resultMsg;

                    // We found games
                    if (jqXHR.status == 200) {
                    	// Create one option tag for each game found
                        for (var i = 0; i < data.results.length; ++i) {
                        	$(CGS.games.elements.datalist).append($("<option>", {
                                value: data.results[i].name
                            }));
                        }

                        $(CGS.games.elements.result).addClass(CGS.classMessage.success);
                        $(CGS.games.elements.result).removeClass(CGS.classMessage.error);
                        resultMsg = "Recherche terminée.";
                    }
                    // Request is ok but no games found
                    else if (jqXHR.status == 204) {
                        $(CGS.games.elements.result).addClass(CGS.classMessage.error);
                        $(CGS.games.elements.result).removeClass(CGS.classMessage.success);
                    	resultMsg = "Aucun jeu n'a été trouvé.";
                    }
                    // Unknown case
                    else {
                        $(CGS.games.elements.result).addClass(CGS.classMessage.error);
                        $(CGS.games.elements.result).removeClass(CGS.classMessage.success);
                    	resultMsg = "Erreur inconnue.";
                    }

                    // Update searching text
                    $(CGS.games.elements.result).text(resultMsg);
                },

                error: function(jqXHR, textStatus, errorThrown) {
                    $(CGS.games.elements.result).addClass(CGS.classMessage.error);
                    $(CGS.games.elements.result).removeClass(CGS.classMessage.success);
                	
                    // Update searching text
                    $(CGS.games.elements.result).text("Erreur de la requête.");
                }
            },
            
            // Show message : msg. -> msg.. -> msg... -> etc.
            showSearchingText: function() {
            	CGS.resetText(CGS.games.elements.result);
                $(CGS.games.elements.result).text("Recherche de jeux en cours...");
            }
        },
        
        /********************
         * PLATFORMS        *
         ********************/
        platforms: {
            init: function() {
            	// Search platforms on keyup
            	var timerPlatforms;
        		$(CGS.platforms.elements.inputSearch).keyup(function() {
        			clearTimeout(timerPlatforms);
        			var value = $(this).val().trim();
        			
        			// Input has a value, we do search
        			if (value.length) {
        				timerPlatforms = setTimeout(function() {
	                		// Remove all platforms found
	                		$(CGS.platforms.elements.datalist).empty();
	    	
	                		// Show that we are searching for platforms
	                		CGS.platforms.showSearchingText();
	    	
	                		// Call server API to get platforms
	                		GiantBomb.getPlatforms(
	                			value,
                				CGS.platforms.handlers.success,
                				CGS.platforms.handlers.error
	                		);
	        			}, CGS.timerKeyUp);
        			}
        			else {
                		$(CGS.platforms.elements.datalist).empty();
                    	CGS.resetText(CGS.platforms.elements.result);
        			}
        		});
            	
            	// Add input platform when clicking on + btn
            	$(CGS.platforms.elements.btnAdd).click(function() {
            		CGS.createInput(
            				CGS.platforms.elements.list,
            				CGS.platforms.elements.inputList,
            				$(CGS.platforms.elements.inputSearch).val()
            		);
            	});
            },    
            
            elements: {
            	btnSearch: $("#btn-search-platforms"),
            	btnAdd: $("#btn-add-platforms"),
            	datalist: $("#datalist-platforms"),
                inputSearch: $("#input-search-platforms"),
                inputList: "input-list-platforms",
                result: $("#result-search-platforms"),
                list: $("#list-platforms")
            },

            handlers: {
            	success: function(data, textStatus, jqXHR) {
                    var resultMsg;

                    // We found games
                    if (jqXHR.status == 200) {
                    	// Create one option tag for each platform found
                        for (var i = 0; i < data.results.length; ++i) {
                        	$(CGS.platforms.elements.datalist).append($("<option>", {
                                value: data.results[i].name
                            }));
                        }
                        
                        $(CGS.platforms.elements.result).addClass(CGS.classMessage.success);
                        $(CGS.platforms.elements.result).removeClass(CGS.classMessage.error);
                        resultMsg = "Recherche terminée.";
                    }
                    // Request is ok but no games found
                    else if (jqXHR.status == 204) {
                        $(CGS.platforms.elements.result).addClass(CGS.classMessage.error);
                        $(CGS.platforms.elements.result).removeClass(CGS.classMessage.success);
                    	resultMsg = "Aucune plateforme n'a été trouvé.";
                    }
                    // Unknown case
                    else {
                        $(CGS.platforms.elements.result).addClass(CGS.classMessage.error);
                        $(CGS.platforms.elements.result).removeClass(CGS.classMessage.success);
                    	resultMsg = "Erreur inconnue.";
                    }

                    // Update searching text
        			$(CGS.platforms.elements.result).text(resultMsg);
                },

                error: function(jqXHR, textStatus, errorThrown) {
                    $(CGS.platforms.elements.result).addClass(CGS.classMessage.error);
                    $(CGS.platforms.elements.result).removeClass(CGS.classMessage.success);
                	
                    // Update searching text
        			$(CGS.platforms.elements.result).text("Erreur de la requête.");
                }
            },
            
            // Show message : msg. -> msg.. -> msg... -> etc.
            showSearchingText: function() {
            	CGS.resetText(CGS.platforms.elements.result);
            	$(CGS.platforms.elements.result).text("Recherche de plateformes en cours...");
            }
        }
    }
    
    // Init CreateGameSession
    CGS.init();
});