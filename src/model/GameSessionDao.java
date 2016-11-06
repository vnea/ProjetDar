package model;

import java.util.List;

public interface GameSessionDao {

		public void insertGameSession(GameSession session);
		
		public void deleteGameSession(GameSession session);
		
		public void updateGameSession(GameSession session);
		
		public List<GameSession> getGameSessionByRoot(Player p);
		
		public List<GameSession> getGameSessionByGame(String gameName);
	
}
