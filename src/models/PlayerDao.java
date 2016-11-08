package models;

public interface PlayerDao {
	
	public void insert(Player p);
	
	public void delete(Player p);
	
	public void update(Player p);
	
	public Player getPlayer(String username, String password);
	
	public Player getPlayer(String username);
	
}
