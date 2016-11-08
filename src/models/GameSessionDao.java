package models;

import java.util.List;

public interface GameSessionDao {

		public void insert(GameSession gameSession);
		
		public void delete(GameSession gameSession);
		
		public void update(GameSession gameSession);
		
		public List<GameSession> getGameSessionByRoot(Player p);
		
		public List<GameSession> getGameSessionByGame(String gameName);
	
		public List<GameSession> getGameSessionByPlatform(String platformName);
}
