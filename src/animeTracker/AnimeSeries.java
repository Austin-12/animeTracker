package animeTracker;

import java.sql.Date;

public class AnimeSeries extends AnimeEntity{
	private int total_episodes;
	
	//constructor to make anime series object 
	public AnimeSeries(String title, Date release_date, int total_episodes, String genres, String description) {
		super(title, release_date, genres, description);
		this.total_episodes = total_episodes;
	}
	
	//getters & setters
	public int getTotalEpisodes() {
		return total_episodes;
	}
	public void setTotalEpisodes(int total_episodes) {
		this.total_episodes = total_episodes;
	}	
}
