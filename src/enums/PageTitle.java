package enums;

public enum PageTitle {
    HOME("Accueil"),
    HOST("Héberger"),
    SEARCH("Rechercher"),
    DISCONNECTION("Déconnexion");

    private final String title;

    private PageTitle(final String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
