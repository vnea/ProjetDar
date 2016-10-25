package model;

import java.util.List;

public interface PlayerDao {
	
	public void CreatePlayer(String email, String username, String password, String lastname, String firstname, 
			int age, Player.Sex sex, String phoneNumber, String address, int postCode, 
			List<String> games, List<String> gamesType, List<String> platforms);
	
	public Player getPlayer(String username, String password);
	
}
