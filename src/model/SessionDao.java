package model;

import java.util.Date;

public interface SessionDao {

		public void insertSession(Session session);
		
		public Session getSessionByRoot(Player p);
		
}
