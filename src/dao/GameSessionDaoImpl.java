package dao;

import java.util.ArrayList;
import java.util.List;

import models.GameSession;
import models.GameSessionDao;
import models.Player;

import org.hibernate.Session;
import org.hibernate.Transaction;

import utils.HibernateUtils;


public class GameSessionDaoImpl implements GameSessionDao {
	
	public GameSessionDaoImpl() {
	}
	
    @Override
	public void insert(GameSession gameSession) {
		Session s = HibernateUtils.getSession();
        Transaction t = s.beginTransaction();

        s.save(gameSession);
        t.commit();
        s.close();
	}
	
    @Override
	public void delete(GameSession gameSession) {
		Session s = HibernateUtils.getSession();
        Transaction t = s.beginTransaction();

        s.delete(gameSession);
        t.commit();
        s.close();
	}
	
    @Override
	public void update(GameSession gameSession) {
		Session s = HibernateUtils.getSession();
        Transaction t = s.beginTransaction();

        s.update(gameSession);
        t.commit();
        s.close();
	}
    
    @Override
    public GameSession getGameSession(Integer id) {
        Session s = HibernateUtils.getSession();
        List<GameSession> gameSessions = s.createQuery("select gs from GameSession gs where gs.idSession like :id", GameSession.class)
                                .setParameter("id", id)
                                .getResultList();
        
        s.close();
        return gameSessions.isEmpty() ? null : gameSessions.get(0);
    }
    
    @Override
    public List<GameSession> getGameSessions() {
        Session s = HibernateUtils.getSession();
        // Take all GameSession
        List<GameSession> gameSessions = s.createQuery("select gs from GameSession gs", GameSession.class)
                                          .getResultList();
        
        s.close();
        return gameSessions;
    }
    
    @Override
    public List<GameSession> getGameSessionsByRoot(Player p) {
        List<GameSession> gameSessions = getGameSessions();
     
        List<GameSession> resultGameSessions = new ArrayList<>(gameSessions.size());
        for (GameSession gameSession : gameSessions) {
            List<Player> players = gameSession.getPlayers();
            for (Player player : players) {
                // Only keep game sessions which the player participates
                if (player.getIdPlayer() == p.getIdPlayer()) {
                    resultGameSessions.add(gameSession);
                }
            }
        }
        
        return resultGameSessions;
    }
	
    @Override
	public List<GameSession> getGameSessionsCreatedByRoot(Player p) {
		Session s = HibernateUtils.getSession();
		List<GameSession> gameSessions = s.createQuery("select gs from GameSession gs where gs.root like :root", GameSession.class)
							              .setParameter("root", p)
							              .getResultList();
		
		s.close();
		return gameSessions;
	}
    
	@Override
	public List<GameSession> getGameSessionsByGame(String gameName) {
        List<GameSession> gameSessions = getGameSessions();
        
        List<GameSession> resultGameSessions = new ArrayList<>(gameSessions.size());
        for (GameSession gameSession : gameSessions) {
            List<String> games = gameSession.getGames();
            for (String game : games) {
                if (game.equalsIgnoreCase(gameName)) {
                    resultGameSessions.add(gameSession);
                }
            }
        }
        
        return resultGameSessions;
	}
	
	@Override
	public List<GameSession> getGameSessionsByPlatform(String platformName) {
        List<GameSession> gameSessions = getGameSessions();
        
        List<GameSession> resultGameSessions = new ArrayList<>(gameSessions.size());
        for (GameSession gameSession : gameSessions) {
            List<String> platforms = gameSession.getPlatforms();
            for (String platform : platforms) {
                if (platform.equalsIgnoreCase(platformName)) {
                    resultGameSessions.add(gameSession);
                }
            }
        }
        
		return resultGameSessions;
	}

}
