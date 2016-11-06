package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


@Entity
@Table(name="GameSessions")
public class GameSession {
	
	public GameSession(){}
	
	// Properties 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer idSession;
	
	@ManyToOne(optional=false)
	private Player root;
	
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Player> players = new ArrayList<Player>();
	
	@Basic(optional=false)
	private String address;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Basic(optional=false)
	private Date date; 
	
	@Column(unique = true)
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<String> games = new ArrayList<String>();
	
	@Column(unique = true)
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<String> platforms = new ArrayList<String>();
		
	
	// Getters/Setters 
	public Integer getIdSession() {
		return idSession;
	}
	public void setIdSession(Integer idSession) {
		this.idSession = idSession;
	}
	public Player getRoot() {
		return root;
	}
	public void setRoot(Player root) {
		this.root = root;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	public List<String> getGames() {
		return games;
	}
	public void setGames(List<String> games) {
		this.games = games;
	}
	public List<String> getPlatforms() {
		return platforms;
	}
	public void setPlatforms(List<String> platforms) {
		this.platforms = platforms;
	}
	
}
