package enums;

public enum PageTitle {
    HOME("Accueil"),
    HOST("Héberger"),
    SEARCH("Rechercher"),
    DISCONNECTION("Déconnexion"),
    MY_PLATFORMS("Mes plateformes"),
    MY_GAME_TYPES("Mes type de jeux"),
    MY_GAMES("Mes jeux"),
    PLATFORM("Platforme"),
    GAME("Jeu"),
    MY_FRIENDS("Mes amis"),
    PROFILE("Profil"),
    CREATE_SESSION("Créer une partie"),
    MY_GAME_SESSIONS("Mes parties");

    private final String title;

    private PageTitle(final String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
