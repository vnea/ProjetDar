package beans;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name="Players")
public class Player {
	
	public Player(){}
	
	// Properties 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer idPlayer;
	
	@Basic(optional=false)
	private String email;
	@Basic(optional=false)
	private String username;
	@Basic(optional=false)
	private String password;
	@Basic(optional=false)
	private String lastname;
	@Basic(optional=false)
	private String firstname;
	
	public enum Sex {H, F};
	@Basic(optional=false)
	private Sex sex;
	
	@Basic(optional=false)
	private Integer age;

	private Integer phoneNumber;
	private String address;
	private Integer postCode;
	
	@ElementCollection
	private List<String> platforms = new ArrayList<String>();
	@ElementCollection
	private List<String> gameTypes = new ArrayList<String>();
	@ElementCollection
	private List<String> games = new ArrayList<String>();
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Player> friends = new ArrayList<Player>();
	 
	 
	 
	// Getters/Setters
	public Integer getIdPlayer() {
		return idPlayer;
	}
	public void setIdPlayer(Integer idPlayer) {
		this.idPlayer = idPlayer;
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
	
	public Integer getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(Integer phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public Integer getPostCode() {
		return postCode;
	}
	public void setPostCode(Integer postCode) {
		this.postCode = postCode;
	}
	
	public List<String> getPlatforms() {
		return platforms;
	}
	public void setPlatforms(List<String> platforms) {
		this.platforms = platforms;
	}
	
	public List<String> getGameTypes() {
		return gameTypes;
	}
	public void setGameTypes(List<String> gameTypes) {
		this.gameTypes = gameTypes;
	}
	
	public List<String> getGames() {
		return games;
	}
	public void setGames(List<String> games) {
		this.games = games;
	}
	
	
}
