package models;

import java.util.List;

public interface GameSessionDao {

	public void insert(GameSession gameSession);
	
	public void delete(GameSession gameSession);
	
	public void update(GameSession gameSession);
	
	public GameSession getGameSession(Integer id);
	
    public List<GameSession> getGameSessions();
	
    public List<GameSession> getGameSessionsByRoot(Player p);
    
    public List<GameSession> getAllGameSessionsByRoot(Player p);
	
	public List<GameSession> getGameSessionsCreatedByRoot(Player p);
	
	public List<GameSession> getGameSessionsByGame(String gameName);

	public List<GameSession> getGameSessionsByPlatform(String platformName);

}
