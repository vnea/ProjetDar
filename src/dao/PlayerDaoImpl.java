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
	
	public void CreatePlayer(String email, String username, String password, String lastname, String firstname, 
							int age, Player.Sex sex, String phoneNumber, String address, int postCode, 
							List<String> games, List<String> gamesType, List<String> platforms){
		
        Session s = HibernateUtils.getSession();
        Transaction t = s.beginTransaction();
        
        Player p = new Player();
        p.setEmail(email);
        p.setUsername(username);
        p.setPassword(password);
        p.setLastname(lastname);
        p.setFirstname(firstname);
        p.setAge(age);
        p.setSex(sex);
        p.setAddress(address);
        p.setPostCode(postCode);
        p.setGames(games);
        p.setGamesType(gamesType);
        p.setPlatforms(platforms);
        
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
