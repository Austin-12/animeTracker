package animeTracker;
import java.sql.Date;

public class AnimeEntity { //class for common properties & methods
	//common properties
	protected String title;
	protected Date release_date;
	protected String genres;
	protected String description;
	
	//constructor for animeEntity
	public AnimeEntity(String title, Date release_date, String genres, String description) {
		this.title = title;
		this.release_date = release_date;
		this.genres = genres;
		this.description = description;
	}
	
	public AnimeEntity() {} //so i can make dummy objects 
	
	//getters & setters 
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Date getReleaseDate() {
		return release_date;
	}
	public void setReleaseDate(Date release_date) {
		this.release_date = release_date;
	}
	
	public String getGenres() {
		return genres;
	}
	public void setGenres(String genres) {
		this.genres = genres;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
