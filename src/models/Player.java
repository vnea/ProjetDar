package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;



@Entity
@Table(name="Players")
public class Player {
	
	public Player(){}
	
	// Properties 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer idPlayer;
	
	@Basic(optional=false)
	@Column(unique = true)
	private String email;
	@Basic(optional=false)
	@Column(unique = true)
	private String username;
	@Basic(optional=false)
	private String password;
	@Basic(optional=false)
	private String lastname;
	@Basic(optional=false)
	private String firstname;
	@Basic(optional=false)
	private Integer age;
	
	public enum Sex {H, F};
	@Basic(optional=false)
	private Sex sex;
	
	private String phoneNumber;
	private String address;
	private String postCode;
	
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<String> platforms = new ArrayList<>();
	
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<String> gamesType = new ArrayList<>();
	
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<String> games = new ArrayList<>();

	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
    private List<Player> friends = new ArrayList<>();
	 
	 
	// Getters/Setters
	public Integer getIdPlayer() {
		return idPlayer;
	}
	public void setIdPlayer(Integer idPlayer) {
		this.idPlayer = idPlayer;
	}
	
	public List<Player> getFriends() {
        return friends;
    }
    public void setFriends(List<Player> friends) {
        this.friends = friends;
    }
    
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public Sex getSex() {
		return sex;
	}
	public void setSex(Sex sex) {
		this.sex = sex;
	}
	
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
	public List<String> getPlatforms() {
		return platforms;
	}
	public void setPlatforms(List<String> platforms) {
		this.platforms = platforms;
	}
	
	public List<String> getGamesType() {
		return gamesType;
	}
	public void setGamesType(List<String> gamesType) {
		this.gamesType = gamesType;
	}
	
	public List<String> getGames() {
		return games;
	}
	public void setGames(List<String> games) {
		this.games = games;
	}
	
	
}
