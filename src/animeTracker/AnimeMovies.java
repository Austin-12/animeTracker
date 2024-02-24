package animeTracker;
import java.sql.Date;

public class AnimeMovies extends AnimeEntity {
	private int duration;
	
	//constructor
	public AnimeMovies(String title, Date release_date, int duration, String genre, String description) {
		super(title, release_date, genre, description);
		this.duration = duration;
	}
	
	//getter & setters
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}

}
