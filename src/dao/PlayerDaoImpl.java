package dao;

import java.util.List;

import models.Player;
import models.PlayerDao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import utils.HibernateUtils;


public class PlayerDaoImpl implements PlayerDao {
	
	public PlayerDaoImpl() {
	}
	
	@Override
	public void insert(Player p) {
        Session s = HibernateUtils.getSession();
        Transaction t = s.beginTransaction();
       
        s.save(p);
        t.commit();
        s.close();
	}
	
	@Override
	public void delete(Player p) {
		Session s = HibernateUtils.getSession();
        Transaction t = s.beginTransaction();
       
        s.delete(p);
        t.commit();
        s.close();
	}
	
	@Override
	public void update(Player p) {
		Session s = HibernateUtils.getSession();
        Transaction t = s.beginTransaction();
       
        s.update(p);
        t.commit();
        s.close();
	}
	
	@Override
	public Player getPlayer(String username, String password) {
		Session s = HibernateUtils.getSession();
		List<Player> players = s.createQuery("select p from Player p where p.username like :name and p.password like :pass", Player.class)
							    .setParameter("name", username)
							    .setParameter("pass", password)
							    .getResultList();
		
		s.close();
		return players.isEmpty() ? null : players.get(0);
	}
	
	@Override
	public Player getPlayer(String username) {
	    Session s = HibernateUtils.getSession();
        List<Player> players = s.createQuery("select p from Player p where p.username like :name", Player.class)
                                .setParameter("name", username)
                                .getResultList();
        
        s.close();
        return players.isEmpty() ? null : players.get(0);
	}
	
   @Override
   public List<Player> getPlayerStartWith(String startUsername, String username) {
        Session s = HibernateUtils.getSession();
        List<Player> players = s.createQuery("select p from Player p where lower(p.username) like lower(:startUsername) and lower(p.username) not like lower(:username)", Player.class)
                                .setParameter("username", username)
                                .setParameter("startUsername", startUsername + "%")
                                .getResultList();
        
        s.close();
        return players.isEmpty() ? null : players;
    }

}
