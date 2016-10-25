package model;

import java.util.Date;

public interface SessionDao {

		public void createSession(Player root, String address, Date date);
}
