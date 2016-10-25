<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style>
/****************/
/* Style Global */
/****************/

@font-face {
	font-family: customFont;
	src: url("AldotheApache.ttf");
}
html, body {
    font-size: 18px;
    height: 100%;
    padding: 0;
    margin: 0;
    font-family: "Segoe UI", Arial, sans-serif;
    background-color: grey;
}
h1 {
	font-family: customFont;
	font-size: 50px;
	display: inline;
}

/****************/
/*   Menu top   */
/****************/

#logo {
	text-decoration: none;
	color: black;
}
#banniere_web {
	height: 64px;
	background-color: white;
}
#banniere_web img {
	vertical-align: middle;
}
#ptp_titre {
	color: rgb(127, 127, 127);
}
#ptp_titre_extension {
	color: #4CAF50;
}
#slogan {
	font-size: 20px;
	letter-spacing: 3px;
	margin-left: 25px;
}
#menu_banniere {
	float: right;
}
#menu_banniere ul {
	list-style-type: none;
	display: table;
    margin: 0px;
    padding: 0px;
}
#menu_banniere li {
	height: 64px;
	padding-left: 15px;
	padding-right: 15px;
	background-color: rgb(127, 127, 127);
	vertical-align: middle;
	display:table-cell;
}
#menu_banniere a {
	padding: 0px;
	height: 64px;
	margin: 0px;
	display:table-cell;
    vertical-align: middle;
    color: white;
    text-decoration: none;
}
#menu_banniere a:hover {
	color: #4CAF50;
}

/****************/
/*Page connexion*/
/****************/

#accueil {
	background: url("fond.jpg") no-repeat;
	background-size: 100%;
	height: 100%;
}
#contenu_accueil {
	padding-top: 80px;
	display: flex;
	width: 60%;
	margin: auto;
}
#formulaires {
	width: 50%;
	float: right;
}
#formulaires form {
	margin-bottom: 20px;
}
input[type=email] {
	font-family: "Segoe UI", Arial, sans-serif;
    width: 100%;
    padding: 8px 20px;
    margin: 3px 0;
    display: inline-block;
    border: 1px solid #ccc;
    box-sizing: border-box;
    border-radius: 6px;
    font-size: 20px;
}
input[type=number] {
    width: 100%;
    padding: 8px 20px;
    margin: 3px 0;
    display: inline-block;
    border: 1px solid #ccc;
    box-sizing: border-box;
    font-family: "Segoe UI", Arial, sans-serif;
    border-radius: 6px;
    font-size: 20px;
}
input[type=text] {
    width: 100%;
    padding: 8px 20px;
    margin: 3px 0;
    display: inline-block;
    border: 1px solid #ccc;
    box-sizing: border-box;
    font-family: "Segoe UI", Arial, sans-serif;
    border-radius: 6px;
    font-size: 20px;
}
input[type=submit] {
    width: 100%;
    background-color: #4CAF50;
    color: white;
    padding: 8px 20px;
    margin: 3px 0;
    border: none;
    cursor: pointer;
    font-family: "Segoe UI", Arial, sans-serif;
    border-radius: 6px;
    height: 60px;
    font-size: 20px;
    background: linear-gradient(90deg, #4CAF50, rgb(78, 194, 56));
}
input[type=password] {
	font-family: "Segoe UI", Arial, sans-serif;
    width: 100%;
    padding: 8px 20px;
    margin: 3px 0;
    display: inline-block;
    border: 1px solid #ccc;
    box-sizing: border-box;
    border-radius: 6px;
    font-size: 20px;
}
.radio_sexe {
	margin-left: 20px;
}
#presentation {
	width: 50%;
	padding-right: 40px;
	color: white;
	font-size: 20px;
}
.ligne_icone {
	text-align: center;
}
.erreur_formulaire_creation_compte {
	color: red;
}
</style>
<title>Créer un compte</title>
</head>
<body>
<div id="banniere_web">
	<h1><a id="logo" href="CreerCompte"><span id="ptp_titre" onmouseover="surligneTitre(this, true)" onmouseout="surligneTitre(this, false)">PlayersToPlayers</span><span id="ptp_titre_extension">.fr</span></a></h1>
	<span id="slogan">LA COMMUNAUTE DES JOUEURS D'ILE-DE-FRANCE</span>
	<div id="menu_banniere">
		<ul>
			<li><a href="CreerCompte">HEBERGER UNE PARTIE</a></li>
			<li><a href="CreerCompte">CHERCHER UNE PARTIE</a></li>
			<li><a href="CreerCompte">DECONNEXION</a></li>
		</ul>
	</div>
</div>
<div id="accueil">
	<div id="contenu_accueil">
		<div id="presentation">
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
		<div id="formulaires">
			<form action="Connexion" method="POST">
			<input type="text" name="pseudo" placeholder="Pseudo ou email"><br>
			<input type="password" name="mot_passe" placeholder="Mot de passe"><br>
			<input type="submit" value="SE CONNECTER">
			</form>
			<form action="CreerCompte" method="POST">
			<input id="creation_compte_pseudo" type="text" name="pseudo" placeholder="Pseudo"><br>
			<span class="erreur_formulaire_creation_compte" id="creation_compte_pseudo_erreur"></span>
			<input id="creation_compte_mail" type="email" name="email" placeholder="Email"><br>
			<span class="erreur_formulaire_creation_compte" id="creation_compte_email_erreur"></span>
			<input id="creation_compte_adresse" id="adresseAutocomplete" type="text" name="adresse"><br>
			<input type="number" min="12" max="99" placeholder="Age"><br>
			<span class="radio_sexe"><input type="radio" name="sexe" value="garcon" checked>Garçon</span>
			<span class="radio_sexe"><input type="radio" name="sexe" value="fille">Fille</span><br>
			<input id="creation_compte_motdepasse" type="password" name="mot_passe" placeholder="Mot de passe"><br>
			<span class="erreur_formulaire_creation_compte" id="creation_compte_motdepasse_erreur"></span>
			<input id="creation_compte_confirmemotdepasse" type="password" name="mot_passe" placeholder="Confirmation du mot de passe"><br>
			<span class="erreur_formulaire_creation_compte" id="creation_compte_confirmemotdepasse_erreur"></span>
			<input type="submit" value="CREER UN COMPTE">
			</form>
		</div>
	</div>
</div>

	<script>
	function verifieFormulaireCreationCompte() {
		var pseudo = document.getElementById("creation_compte_pseudo").value;
		var email = document.getElementById("creation_compte_email").value;
		var motdepasse = document.getElementById("creation_compte_motdepasse").value;
		var confirmemotdepasse = document.getElementById("creation_compte_confirmemotdepasse").value;
		
		if(/[^a-zA-Z0-9]/.test(pseudo)) {
		       alert('Input is not alphanumeric');
		       return false;
		    }
	}
	
			
	
</body>
</html>