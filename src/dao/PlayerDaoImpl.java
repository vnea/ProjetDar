package dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import beans.Player;
import utils.HibernateUtils;


public class PlayerDaoImpl implements PlayerDao{
	
	public PlayerDaoImpl (){
	}
	
	public void CreatePlayer(){
		// Récupération d'une session Hibernate
        Session s = HibernateUtils.getSession();
 
        // Début de la transaction
        Transaction t = s.beginTransaction();
 
        // Création d'un objet Event
        Player p = new Player();
        p.setEmail("email@test.com");
        p.setUsername("morvan");
        p.setPassword("toto");
        p.setLastname("Lassauzay");
        p.setFirstname("morvan");
        p.setAddress("monadresse");
        p.setAge(22);
        p.setSex(Player.Sex.H);
        
        
 
        // Enregistrement de l'event
        s.save(p);
 
        // Fin de la transaction
        t.commit();
 
        // Fermeture de la session Hibernate
        s.close();
	}
}
