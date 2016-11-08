package enums;

public enum PageTitle {
    HOME("Accueil"),
    HOST("Héberger"),
    SEARCH("Rechercher"),
    DISCONNECTION("Déconnexion"),
    MY_PLATFORMS("Mes consoles"),
    MY_GAME_TYPES("Mes type de jeux"),
    MY_GAMES("Mes jeux"),
    PLATFORM("Console"),
    MY_FRIENDS("Mes amis"),
    CREATE_SESSION("Créer une partie");

    private final String title;

    private PageTitle(final String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
