package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Player;
import model.GameSession;
import model.GameSessionDao;
import utils.HibernateUtils;

public class GameSessionDaoImpl implements GameSessionDao{
	
	public GameSessionDaoImpl (){
	}

	public void insertGameSession(GameSession session) {
		Session s = HibernateUtils.getSession();
        Transaction t = s.beginTransaction();

        s.save(session);
        t.commit();
        s.close();
	}
	
	public void deleteGameSession(GameSession session){
		Session s = HibernateUtils.getSession();
        Transaction t = s.beginTransaction();

        s.delete(session);
        t.commit();
        s.close();
	}
	
	public void updateGameSession(GameSession session){
		Session s = HibernateUtils.getSession();
        Transaction t = s.beginTransaction();

        s.update(session);
        t.commit();
        s.close();
	}
	
	public List<GameSession> getGameSessionByRoot(Player p){
		Session s = HibernateUtils.getSession();
		
		List<GameSession> sessions = (List<GameSession>) s.createQuery(
			    "select s from GameSession s where s.root like :root" )
							.setParameter( "root", p ).getResultList();
		
		s.close();
		return sessions;
	}

}