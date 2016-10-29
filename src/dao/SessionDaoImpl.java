package dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Player;
import model.SessionDao;
import utils.HibernateUtils;

public class SessionDaoImpl implements SessionDao{
	
	public SessionDaoImpl (){
	}

	public void insertSession(model.Session session) {
		Session s = HibernateUtils.getSession();
        Transaction t = s.beginTransaction();

        s.save(session);
        t.commit();
        s.close();
	}
	
	public model.Session getSessionByRoot(Player p){
		Session s = HibernateUtils.getSession();
		
		List<model.Session> sessions = (List<model.Session>) s.createQuery(
			    "select s from Session s where s.root like :root" )
							.setParameter( "root", p ).getResultList();
		
		return sessions.get(0);
	}

}
