package enums;

public enum GiantBombResource {
    
    GAMES("games"),
    GENRES("genres"),
    PLATFORMS("platforms"),
    SEARCH("search");
    
    private final String ressourceName;

    private GiantBombResource(final String ressourceName) {
        this.ressourceName = ressourceName;
    }

    @Override
    public String toString() {
        return ressourceName;
    }

}
