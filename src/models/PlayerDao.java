package models;

import java.util.List;

public interface PlayerDao {
	
	public void insert(Player p);
	
	public void delete(Player p);
	
	public void update(Player p);
	
	public Player getPlayer(String username, String password);
	
	public Player getPlayer(String username);
	
    public List<Player> getPlayerStartWith(String startUsername, String username);
	
}
