package animeTracker;

import java.util.Date;

public class Review {
	private String review; //the text review
	private Double rating;
	private Date reviewDate;
	
	//review constructor
	public Review(String review, double rating, Date reviewDate) {
		this.review = review;
		this.rating = rating;
		this.reviewDate = reviewDate;
	}
	
	//getters
	public String getReview() {
		return review;
	}
	
	public Double getRating() {
		return rating;
	}
	
	public Date getReviewDate() {
		return reviewDate;
	}
	
	//setters
	public void setReview(String review) {
		this.review = review;
	}
	
	public void setRating(double rating) {
		this.rating = rating;
	}
	
	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}
}
