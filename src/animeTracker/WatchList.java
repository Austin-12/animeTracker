package animeTracker;

public class WatchList {
	private int currentEpisode;
	private boolean complete;
	
	//constructor
	public WatchList(int currentEpisode, boolean complete) {
		this.currentEpisode = currentEpisode;
		this.complete = complete;
	}
	
	//getters & setters
	public int getCurrentEpisode() {return currentEpisode;}
	public void setCurrentEpisode(int currentEpisode) {this.currentEpisode = currentEpisode;}
	
	public boolean getComplete() {return complete;}
	public void setComplete(boolean complete) {this.complete = complete;}

}
/* 
 * only need to get current episode and if we finished the anime from the user
 * */

