package model;

import java.util.List;

public interface PlayerDao {
	
	public void insertPlayer(Player p);
	
	public Player getPlayer(String username, String password);
	
}
