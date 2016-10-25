package dao;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Player;
import model.SessionDao;
import utils.HibernateUtils;

public class SessionDaoImpl implements SessionDao{
	
	public SessionDaoImpl (){
	}

	public void createSession(Player root, String address, Date date) {
		Session s = HibernateUtils.getSession();
        Transaction t = s.beginTransaction();
 
        model.Session session = new model.Session();
        session.setRoot(root);
        session.setAddress(address);
        session.setDate(date);
   
        
        s.save(session);
        t.commit();
        s.close();
	}

}
