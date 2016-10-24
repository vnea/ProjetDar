<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="../../favicon.ico">

<title>Accueil</title>

<!-- Bootstrap core CSS -->
<link href="css/bootstrap.css" rel="stylesheet">

<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<link href="../../assets/css/ie10-viewport-bug-workaround.css"
	rel="stylesheet">

<!-- Custom styles for this template -->
<link href="css/style.css" rel="stylesheet">

<!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
<!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
<script src="../../assets/js/ie-emulation-modes-warning.js"></script>

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>

	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false"
					aria-controls="navbar">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">PlayersToPlayers</a>
			</div>
			<div id="navbar" class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li><a href="#">Héberger</a></li>
					<li><a href="#about">Rechercher</a></li>
					<li><a href="#contact">Déconnexion</a></li>
					<li>
						<form class="navbar-form navbar-right">
							<div class="form-group">
								<input type="text" placeholder="Pseudo ou email"
									class="form-control">
							</div>
							<div class="form-group">
								<input type="password" placeholder="Mot de passe"
									class="form-control">
							</div>
							<button type="submit" class="btn btn-success">Connexion</button>
						</form>
					</li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</nav>

	<div class="container">
		<div id="contenu_accueil">
			<div id="container_presentation">
				<div class="ligne_icone" id="icone_amitie"><img src="amitie_icone.png"></div>
				<p>
					PTP est une communauté de joueurs d'Ile de France, pour organiser des sessions de jeux vidéos ! 
				</p>
				<br>
				<div class="ligne_icone" id="icone_maison"><img src="maison_icone.png"></div>
				<p>
					Héberge une partie chez toi avec tes amis, ou rejoins une partie en IDF et fais toi de nouveaux amis !
				</p>
				<br>
				<div class="ligne_icone" id="icone_manette"><img src="manette_icone.png"></div>
				<p>
					Ne joue plus jamais seul à tes jeux de sport ou de combat favoris et passe des soirées super fun !
				</p>
				<br>
			</div>
			<div id="container_formulaire_inscription">
				<form action="CreerCompte" method="POST">
					<input type="text" name="pseudo" placeholder="Pseudo">
					<input type="email" name="email" placeholder="Email">
					<br>
					<input id="adresseAutocomplete" type="text" name="adresse">
					<br>
					<input type="number" min="12" max="99" placeholder="Age">
					<br>
					<input type="password" name="mot_passe" placeholder="Mot de passe">
					<br>
					<input id="dernier_input" type="password" name="mot_passe" placeholder="Confirmation du mot de passe">
					<br>
					<input type="submit" value="Créer un compte">
				</form>
			</div>
		</div>
	</div>
	<!-- /.container -->


	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="js/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script src="js/ie10-viewport-bug-workaround.js"></script>
	<script>

		var autocomplete, adresseEntree = false;
		var composantsAdresse = {
			numero_rue : 'short_name',
			voie : 'long_name',
			ville : 'long_name',
			code_postal : 'short_name'
		};
		function initialiseAutocomplete() {
			autocomplete = new google.maps.places.Autocomplete(document.getElementById('adresseAutocomplete'), {
				types : [ 'geocode' ],
				componentRestrictions : {
					country : 'fr'
				}
			});
			autocomplete.addListener('place_changed', function() {
				adresseEntree = true;
			});
		}
		
	</script>
	<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBGDRi-9N24b6zcOyRLaYzsgGPkYZVR6i0&libraries=places&callback=initialiseAutocomplete"></script>
</body>
</html>
