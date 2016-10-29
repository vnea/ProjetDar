package dao;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Player;
import model.PlayerDao;
import utils.HibernateUtils;


public class PlayerDaoImpl implements PlayerDao{
	
	public PlayerDaoImpl (){
	}
	
	public void insertPlayer(Player p){
		
        Session s = HibernateUtils.getSession();
        Transaction t = s.beginTransaction();
       
        s.save(p);
        t.commit();
        s.close();
	}
	
	public Player getPlayer(String username, String password){
		Session s = HibernateUtils.getSession();
		
		List<Player> players = (List<Player>) s.createQuery(
			    "select p from Player p where p.username like :name and p.password like :pass" )
							.setParameter( "name", username ).setParameter("pass", password).getResultList();
		
		return players.get(0);
	}
}
