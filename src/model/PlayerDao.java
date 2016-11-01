package model;

public interface PlayerDao {
	
	public void insertPlayer(Player p);
	
	public void deletePlayer(Player p);
	
	public void updatePlayer(Player p);
	
	public Player getPlayer(String username, String password);
	
	public Player getPlayer(String username);
	
}
