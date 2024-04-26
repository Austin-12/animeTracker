package animeTracker;
import java.time.LocalDate;
import java.util.Scanner;
import java.sql.Date;

public class MainMenu {
	public static void main(String[] args) {
		displayMainMenu();
		
	}
	
	//method will have all menu screens
	public static void displayMainMenu() {
		System.out.println("*****Anime Tracker*****\n\tMain Menu\n");
		System.out.println("   1. Anime series");
		System.out.println("   2. Anime Movies");
		System.out.println("   3. Watch List");
		System.out.println("   4. Exit");
		System.out.println("enter a number to select");
		System.out.println("***********************");
		
		Scanner input = new Scanner(System.in); //scanner object
		
		//call the menus based on the users choice
		if(input.hasNextInt()) { //check if user input is a # and between 1-4
		int choice = input.nextInt();
		if (choice >= 1 && choice <= 4) { //check if number is 1-4
		switch (choice) {
			case 1: 
				animeSeriesMenu();
				break;
			case 2:
				animeMovieMenu();
				break;
			case 3:
				watchListMenu();
				break;
			case 4:
				System.exit(0);
				break;
			default:		
		}
	} else {
		System.out.println("Invalid choice. Please enter a number between 1-4.");
		displayMainMenu();
	}		
		} else {
			System.out.println("Invalid choice. Please enter a number between 1 - 4.");
			displayMainMenu();
		}
		input.close();
	}
	public static void animeSeriesMenu() {
		System.out.println("*****Anime Tracker*****\n   Anime Series Menu\n");
		System.out.println("   1. Add Anime Series");
		System.out.println("   2. Update Anime Series");
		System.out.println("   3. Delete anime Series");
		System.out.println("   4. List Anime Series");
		System.out.println("   5. Search Anime Series");
		System.out.println("   6. Return to Main Menu");
		System.out.println("enter a number to select");
		System.out.println("***********************");
		
		Scanner input = new Scanner(System.in); //scanner object
		
		if(input.hasNextInt()) { //check if input is an int
		int choice =input.nextInt();
		if (choice >= 1 && choice <= 6) { //check if choice is 1-6 included
		switch (choice) {
			case 1:
				addAnimeSeries();
				break;
			case 2:
				updateAnimeSeries();
				break;
			case 3:
				removeAnimeSeries();
				break;
			case 4:
				listAnimeSeries();
				break;
			case 5:
				searchAnimeSeries();
				break;
			case 6:
				displayMainMenu();
				break;
			default:
		}
		} else {
			System.out.println("Invalid choice. Please enter choice 1 - 6");
			animeSeriesMenu();
		}
	} else {
		System.out.println("Invalid choice. Please enter choice 1 - 6");
		animeSeriesMenu();
	}
		input.close();
	}
//call method to update anime series based on the title
public static void updateAnimeSeries() {
	Scanner input = new Scanner(System.in);
	System.out.print("Enter title of the anime you want to update: ");
	String title = input.nextLine();
	
	if(title.length() == 0) { //if user doesn't enter title send back to the menu with error message
		System.out.println("must enter title of anime series");
		animeSeriesMenu();
	}
	DatabaseManager manager = new DatabaseManager();
	AnimeSeries anime = new AnimeSeries();
	
	manager.updateAnimeSeries(title, anime);
	input.close();
	}

//method to get input and create anime series object
public static void addAnimeSeries() {
	Scanner input = new Scanner(System.in);
	//collect input from user
	System.out.print("Enter anime title: ");
	String title = input.nextLine();
	
	if(title.length() == 0) { //make sure string isn't empty
		System.out.println("must provide anime series title");
		addAnimeSeries();
	}
	
	Date date = null; //the date going to the anime series constructor
	int counter = 0; //counter to control loop
	LocalDate thisYear = LocalDate.now(); //todays date
	while(counter == 0) { 
		System.out.print("Enter anime release date: (yyyy-mm-dd) or enter to continue ");
		String releaseDate = input.nextLine();
		
		if(releaseDate.isEmpty()) { //if input is empty break out of loop
			break;
		}
		
		try {//try to turn input into a date
			date = Date.valueOf(releaseDate);
			if(date.toLocalDate().getYear() > 1920 && date.toLocalDate().getYear() < (thisYear.getYear() + 10)) { //date must between 190 - the current year + 10
				counter++;
		} else {
			System.out.println("Invalid date please enter a year after 1920 and before " + (thisYear.getYear() + 10));
		}
			
		} catch (Exception e) {
			System.out.println("Invalid input enter (yyyy-mm-dd)");
		} 
	
	}
	int totalEpisodes = 0;
	int count = 0; //counter to control while loop
	while(count == 0) {
	System.out.print("Enter total number of episodes: ");
	String total = input.nextLine();
	if(total.isEmpty()) {
		break;
	}
	try {
		totalEpisodes = Integer.valueOf(total); //try to convert the string to an integer ensure its within range then get out of loop
		if(totalEpisodes > 0 && totalEpisodes <= 3000) {
			count++;
		} else {
			System.out.println("Invalid number of episodes please enter a number between 1 - 3000");
		}
	} catch (NumberFormatException e) {
		System.out.print("Invalid input please enter a number\n");
	}
	
	}
	
		
		System.out.print("Enter genre(s): "); //genre accepts null values
		String genre = input.nextLine();
		if(genre.isEmpty()) {
			genre = null;
		}
	
	System.out.print("Enter anime description: "); //description accepts null values
	String description = input.nextLine();
	if(description.isEmpty()) {
		description = null;
	}

	
	//create anime series object
	AnimeSeries animeOb = new AnimeSeries(title, date, totalEpisodes, genre, description);
	
	DatabaseManager manager = new DatabaseManager(); //database object
	manager.addToDatabase(animeOb);
	

	//input.nextLine(); //consume a new line (this line was giving me errors)
	input.close();
}

//call delete from database function with the title of the anime series
public static void removeAnimeSeries() {
	Scanner input = new Scanner(System.in);
	System.out.print("Enter anime series title to delete: ");
	String title = input.nextLine();
	
	if(title.length() == 0) {
		System.out.println("must enter anime series title");
		removeAnimeSeries();
	}
	DatabaseManager manager = new DatabaseManager();
	AnimeSeries anime = new AnimeSeries();  //dummy anime series object
	manager.deleteFromDatabase(title, anime);
	
	input.close();
	
}

public static void searchAnimeSeries() {
	Scanner input = new Scanner(System.in);
	System.out.print("Enter title of anime series to search: ");
	String title = input.nextLine();
	
	if(title.length() == 0) {
		System.out.println("must enter anime series title");
		searchAnimeSeries();
	}
	
	DatabaseManager manager = new DatabaseManager();
	AnimeSeries anime = new AnimeSeries();
	
	manager.search(title, anime);
	input.close();
}

public static void listAnimeSeries() {
	DatabaseManager manager = new DatabaseManager();
	AnimeSeries anime = new AnimeSeries();
	manager.List(anime);
}
public static void animeMovieMenu() {
	System.out.println("*****Anime Tracker*****\n   Anime Movie Menu\n");
	System.out.println("   1. Add Movie");
	System.out.println("   2. Update Movie");
	System.out.println("   3. Delete Movie");
	System.out.println("   4. List Movies");
	System.out.println("   5. Search Movie");
	System.out.println("   6. Return to Main Menu");
	System.out.println("enter a number to select");
	System.out.println("***********************");
	
	Scanner input = new Scanner(System.in); //scanner object
	
	if(input.hasNextInt()) {
	int choice =input.nextInt();
	switch (choice) {
		case 1:
			addMovie();
			break;
		case 2:
			updateMovie();
			break;
		case 3:
			removeMovie();
			break;
		case 4:
			listMovie();
			break;
		case 5:
			searchMovie();
			break;
		case 6:
			displayMainMenu();
			break;
		default:
	}
} else {
	System.out.println("Invalid choice. Please enter choice 1 - 6");
	animeMovieMenu();
}
	input.close();
		
	}

public static void searchMovie() {
	Scanner input = new Scanner(System.in);
	System.out.print("Enter the name of the movie to search: ");
	String title = input.nextLine();
	
	if(title.length() == 0) {
		System.out.println("must enter title of movie");
	}
	DatabaseManager manager = new DatabaseManager();
	AnimeMovies movie = new AnimeMovies();
	manager.search(title, movie);

}

public static void listMovie() {
	DatabaseManager manager = new DatabaseManager();
	AnimeMovies movie = new AnimeMovies();
	manager.List(movie);
}

public static void removeMovie() {
	Scanner input = new Scanner(System.in);
	System.out.print("Enter title of the movie you want to remove: ");
	String title = input.nextLine();
	
	if(title.length() == 0) {
		System.out.println("must enter title of movie");
		animeMovieMenu();
	}
	DatabaseManager manager = new DatabaseManager();
	AnimeMovies movie = new AnimeMovies();
	manager.deleteFromDatabase(title, movie);
	
}

public static void updateMovie() {
	Scanner input = new Scanner(System.in);
	System.out.print("Enter title of the movie you want to update: ");
	String title = input.nextLine();
	
	if(title.length() == 0) { //if user doesn't enter title send back to the menu with error message
		System.out.println("must enter title of movie");
		animeMovieMenu();
	}
	DatabaseManager manager = new DatabaseManager();
	AnimeMovies movie = new AnimeMovies();
	manager.updateAnimeSeries(title, movie);
	input.close();
	
}

public static void addMovie() {
	Scanner input = new Scanner(System.in);
	//enter movie title
	System.out.print("Enter movie title: ");
	String title = input.nextLine();
	if(title.length() == 0) {
		System.out.println("must provide movie title");
		addMovie();
	}
	//enter date
	Date date = null; //the date going to the anime series constructor
	int counter = 0; //counter to control loop
	LocalDate thisYear = LocalDate.now(); //todays date
	while(counter == 0) { 
		System.out.print("Enter movie release date: (yyyy-mm-dd) or enter to continue ");
		String releaseDate = input.nextLine();
		
		if(releaseDate.isEmpty()) { //if input is empty break out of loop
			break;
		}
		
		try {//try to turn input into a date
			date = Date.valueOf(releaseDate);
			if(date.toLocalDate().getYear() > 1920 && date.toLocalDate().getYear() < (thisYear.getYear() + 10)) { //date must between 190 - the current year + 10
				counter++;
		} else {
			System.out.println("Invalid date please enter a year after 1920 and before " + (thisYear.getYear() + 10));
		}
			
		} catch (Exception e) {
			System.out.println("Invalid input enter (yyyy-mm-dd)");
		} 
	
	}
	
	int duration = 0;
	int count = 0; //counter to control while loop
	while(count == 0) {
	System.out.print("Enter movie duration in minutes: ");
	String total = input.nextLine();
	if(total.isEmpty()) {
		break;
	}
	try {
		duration = Integer.valueOf(total); //try to convert the string to an integer ensure its within range then get out of loop
		if(duration > 0 && duration <= 300) {
			count++;
		} else {
			System.out.println("Invalid duration please enter a number between 1 - 3000");
		}
	} catch (NumberFormatException e) {
		System.out.print("Invalid input please enter a number\n");
	}
	
	}
	//get genres from user
	System.out.print("Enter genre(s): "); //genre accepts null values
	String genre = input.nextLine();
	if(genre.isEmpty()) {
		genre = null;
	}
	//get description from user
	System.out.print("Enter movie description: ");
	String description = input.nextLine();
	if(description.isEmpty()) {
		description = null;
	}
	
	AnimeMovies movie = new AnimeMovies(title, date, duration, genre, description); //movie object
	
	DatabaseManager manager = new DatabaseManager();
	manager.AddMovieToDatabase(movie);
}

public static void watchListMenu() {
	System.out.println("*****Anime Tracker*****\n   Watch List Menu\n");
	System.out.println("   1. Add to Watch List");
	System.out.println("   2. Update Watch List");
	System.out.println("   3. Delete from Watch List");
	System.out.println("   4. List Watch List");
	System.out.println("   5. Search Watch List");
	System.out.println("   6. Reviews");
	System.out.println("   7. Return to Main Menu");
	System.out.println("enter a number to select");
	System.out.println("***********************");
	
	Scanner input = new Scanner(System.in); //scanner object
	
	if(input.hasNextInt()) {
	int choice =input.nextInt();
	switch (choice) {
		case 1:
			addToWatchList();
			break;
		case 2:
			updateWatchList();
			break;
		case 3:
			removeFromWatchlist();
			break;
		case 4:
			listWatchList();
			break;
		case 5:
			searchWatchList();
			break;
		case 6:
			reviewsMenu();
			break;
		case 7:
			displayMainMenu();
			break;
		default:
	}
} else {
	System.out.println("Invalid choice. Please enter choice 1 - 6");
	watchListMenu();
}
	input.close();
}

//menu to add, update, delete, list, search anime reviews
public static void reviewsMenu() {
	System.out.println("*****Anime Tracker*****\n   Anime Review Menu\n");
	System.out.println("   1. Add Review");
	System.out.println("   2. Update Review");
	System.out.println("   3. Delete Review");
	System.out.println("   4. List Reviews");
	System.out.println("   5. Search Review");
	System.out.println("   6. Return to Main Menu");
	System.out.println("enter a number to select");
	System.out.println("***********************");
	
Scanner input = new Scanner(System.in); //scanner object
	
	if(input.hasNextInt()) {
	int choice =input.nextInt();
	switch (choice) {
		case 1:
			addReview();
			break;
		case 2:
			updateReview();
			break;
		case 3:
			removeReview();
			break;
		case 4:
			listReview();
			break;
		case 5:
			searchReview();
			break;
		case 6:
			displayMainMenu();
			break;
		default:
	}
} else {
	System.out.println("Invalid choice. Please enter choice 1 - 6");
	reviewsMenu();
}
	input.close();
}


private static void searchReview() {
	Scanner scanner = new Scanner(System.in);
	System.out.print("Enter anime title to search review: ");
	
	String title = scanner.nextLine();
	
	//check to see if the title exist first
	DatabaseManager manager = new DatabaseManager();
	AnimeSeries anime = new AnimeSeries();
	AnimeMovies movie = new AnimeMovies();
	
	Boolean seriesExist = manager.doesItExist(title, anime);
	Boolean movieExist = manager.doesItExist(title, movie);
	int watchListID = 0;
	
	if(seriesExist) {
		//exist in anime series table now get the watch list ID
		watchListID = manager.getWatchListID(title, anime);
		
		//if watchListID is greater than 0 then it exist in the watchlist if not then print off error message
		if(watchListID > 0) {
			manager.searchReview(title, watchListID);
			
		} else {
			System.out.println("anime does not exist in watch list");
			reviewsMenu();
		}
		
	} else if(movieExist) {
		//exist in movie table
		watchListID = manager.getWatchListID(title, movie);
		
		if(watchListID > 0) {
			manager.searchReview(title, watchListID);
			
		} else {
			System.out.println("anime does not exist in watch list");
			reviewsMenu();
		}
		
	} else {
		System.out.print("anime does not exist");
		reviewsMenu();
	}
	
	
	
}

private static void listReview() {
	DatabaseManager manager = new DatabaseManager();
	/* call a method in database manger to list all the reviews in the database
	 */
	manager.listReviews();
	
}

private static void removeReview() {
	Scanner scanner = new Scanner(System.in);
	System.out.print("Enter review ID to delete review: ");
	String input = scanner.nextLine();
	int ID = 0;
	
//try to convert the input into a int if not print error message and go to the reviewsMenu
	try {
		 ID = Integer.parseInt(input);
	} catch (Exception e) {
		System.out.println("Invalid input, enter a number");
		reviewsMenu();
	}
	//call method in database with the ID to delete the review
	DatabaseManager manager = new DatabaseManager();
	manager.deleteReview(ID);

	
}

private static void updateReview() {
	// TODO Auto-generated method stub
	
}

public static void addReview() {
	/* call a method that takes the title the user inputs
	 * gets the seriesID/movieID sees if it exist in the watchlist
	 * if it does the user can add a review for that anime if doesn't 
	 * print error cannot leave a review for anime not on watch list
	 * */
	Scanner scanner = new Scanner(System.in);
	
	System.out.print("Enter Anime Title To Add Review: ");
	String title = scanner.nextLine();
	AnimeSeries anime = new AnimeSeries();
	AnimeMovies movie = new AnimeMovies();
	DatabaseManager manager = new DatabaseManager();
	
	//first see if anime exist at all in the database
	Boolean checkSeries = manager.doesItExist(title, anime);
	Boolean checkMovies = manager.doesItExist(title, movie);
	int watchID = 0; //the watch id that will be stored in a review
	
	if(checkSeries) {
	//check if the series is in the watchlist 
		watchID = manager.getWatchListID(title, anime);
	} else if(checkMovies) {
		//check if the movie is in the watch list
		watchID = manager.getWatchListID(title, movie);
	} else {
		System.out.println("anime does not exist"); //anime doesn't exist in the database
		reviewsMenu();
	}
	double rating = 0;
	String input = ""; //holds input from the user
	String review ="";
	/* get user inputs for the review
	 * */
	//get rating of the anime on a scale from 1.0 - 10.
	int counter = 0; //loop counter
	while(counter < 1) {
		System.out.print("Rate " + title + " (Horrible) 1.0 - 10 (Peak Fiction): ");
		input = scanner.nextLine();
		//try to parse the input to a double
		try {
			rating = Double.parseDouble(input);
		}
			catch (Exception e) {
				System.out.println("Enter a rating between 1.0 - 10" + " (example 7.9)");
				continue;
			}
		//check if rating is greater than or equal to 1 and less than 10
		if(rating >= 1.0 && rating <= 10.0) {
			counter++;
			
		} else {
			//stay in loop until user enters a good rating and print error message
			System.out.println("Enter a rating between 1.0 - 10" + " (example 7.9)");
			continue;
		}
	}
	// enter a text review for the anime (optional) there are no restraints on a the text review
			System.out.println("Enter a text review for " + title + " (type 'quit' to end): or press enter to skip");
			StringBuilder inputText = new StringBuilder();
			//user can enter multiple lines 
			String line;
			while(!(line = scanner.nextLine()).equalsIgnoreCase("quit")) {
				if(line == "" ) {break;}
				inputText.append(line).append("\n");
			}
			review = inputText.toString();
			
			//call the method to communicate with the database and add the review (id, rating, review)
			manager.addReview(watchID, rating, review);
}

//method to update watch list object based on title of the series/movie
private static void updateWatchList() {
	/* user enters title that he wants to update
	 *make sure the title exist in the series/movie table
	 *if it does check if the corresponding series/movie ID is in the watch list
	 *select current episode and complete and update if you wish
	 * */
	Scanner scanner = new Scanner(System.in);
	DatabaseManager manager = new DatabaseManager();
	
	System.out.print("Enter title to update Anime in the Watch List: ");
	String title = scanner.nextLine();
	
	//check if anime exist in the series/movie table first
	AnimeSeries anime = new AnimeSeries();
	AnimeMovies movie = new AnimeMovies();
	
	Boolean checkSeries = manager.doesItExist(title, anime);
	Boolean checkMovies = manager.doesItExist(title, movie);
	
	if(checkSeries) {
		manager.updateWatchList(title, anime);
		
	} else if(checkMovies) {
		manager.updateWatchList(title, movie);
		
	} else {
		System.out.println("Record not found");
		watchListMenu();
	}

	
}

private static void searchWatchList() {
	Scanner scanner = new Scanner(System.in);
	System.out.print("Enter Title to search Watch List: ");
	
	String title = scanner.nextLine();

	DatabaseManager manager = new DatabaseManager();

	AnimeSeries anime = new AnimeSeries();
	AnimeMovies movie = new AnimeMovies();
	
	Boolean checkSeries = manager.doesItExist(title, anime);
	Boolean checkMovies = manager.doesItExist(title, movie);
	/* see if the title exist in the series/movie table if it does call the search watch list in manager if it doesn't
	 * "record not found"
	 * */
	if(checkSeries) {
		manager.searchWatchList(title, anime);
		
	} else if(checkMovies) {
		manager.searchWatchList(title, movie);
		
	} else {
		System.out.println("No Records Found");
	}
}

private static void listWatchList() {
	/* list function should list:
	 * title
	 * current episode/complete
	 * description
	 * 
	 * */
	DatabaseManager manager = new DatabaseManager();
	manager.listWatchList();
	
}

//method to return the choice a user makes (if they want to add/delete anime series/movie)
private static int userChoice(Boolean adding) {
	Scanner scanner = new Scanner(System.in);
	int choice = 0; //stores the choice the user makes
	
	int counter = 0;
	while(counter != 1) {
	//prompt user to enter 1 to add anime series or 2 to add movie
		if(adding) {
			System.out.print("Enter 1 to add anime series or 2 to add anime movie to watch list or press enter to return to the main menu: ");
		} else {
			System.out.print("Enter 1 to delete anime series or 2 to delete anime movie to watch list or press enter to return to the main menu: ");
		}
	
	String input = scanner.nextLine();
	
	//return to main menu if nothing is entered
	if(input.isEmpty()) {
		watchListMenu();
	}
	
//if choice is not empty try to convert to a int if can't print error
	if(!input.isEmpty()) {
		try {
			choice = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			System.out.println("error. input the number 1 or 2");
			continue;
		}
		//choice is a int ensure it is either a 1 or 2 if not print error message
		if(choice == 1 || choice == 2) {
			counter++;
		} else {
			System.out.println("error. input the number 1 or 2");
			continue;
		}
	}
} //end of while loop
	return choice;
}

//method to remove from watchlist
private static void removeFromWatchlist() {
	Scanner scanner = new Scanner(System.in);
	
	int choice = userChoice(false);
	DatabaseManager manager = new DatabaseManager();
	
	//if user wants to delete a anime series
	if(choice == 1) {
		System.out.print("Enter title of anime series to delete from watch list: ");
		String title = scanner.nextLine();
		AnimeSeries anime = new AnimeSeries();
		
		//check if it exist in the series/movie table
		if(manager.doesItExist(title, anime)) {
				
		//call database manager method
		manager.deleteFromWatchList(title, anime);
		} else {
			System.out.println("anime series does not exist");
			watchListMenu();
		}
	} else if(choice == 2) {
		
		System.out.print("Enter title of anime movie to delete from watch list: ");
		String title = scanner.nextLine();
		AnimeMovies movie = new AnimeMovies();
		
		if(manager.doesItExist(title, movie)) {
			
		manager.deleteFromWatchList(title, movie);
		} else {
			System.out.println("anime movie does not exist");
			watchListMenu();
		}
		
	}
	scanner.close();
	
}

private static void addToWatchList() {
	Scanner scanner = new Scanner(System.in);
	
	int choice = userChoice(true);
	//database manager to call methods 
	DatabaseManager manager = new DatabaseManager();
	//if choice == 1 we are adding a anime series from our animeseries table to the watchlist and 2 for movie
	if(choice == 1) {
		System.out.print("Enter title of anime series to add to watch list: ");
		String title = scanner.nextLine();
		
		//create anime series object
		AnimeSeries anime = new AnimeSeries();
		//once we have the title we have to check if the title exist in the anime series table.
		Boolean doesItExist = manager.doesItExist(title, anime);
		
		int episode = 0;
		Boolean complete = false; //the data i need to get from the user
		//if the title exist in the anime series table
		if(doesItExist) {
			System.out.print("Insert current episode, or 'f' for finished: ");

			String input = scanner.nextLine();// Read the input

			// Check if the input is empty
			if (input.isEmpty()) {
			    System.out.println("Must enter current episode or 'f' for finished.");
			    watchListMenu();
			} else {
			    // Process the input
			    if (input.equalsIgnoreCase("f")) {
			        complete = true;
			    } else if (input.matches("\\d+")) { // Check if input is a number
			        episode = Integer.parseInt(input);
			    } else {
			        System.out.println("Invalid input. Please enter a number or 'f' for finished.");
			        watchListMenu();
			    }
			}

		}
		//call method to add to the watchlist
		manager.addToWatchList(title, episode, complete, anime);
		
	} else if(choice == 2) { //if user chooses movie to add to watchlist
		System.out.print("Enter title of the anime movie to add to watch list: ");
		String title = scanner.nextLine();
		
		//create anime movies
		AnimeMovies movie = new AnimeMovies();
		Boolean doesItExist = manager.doesItExist(title, movie);
		
		int episode = 0;
		Boolean complete = false; //the data i need to get from the user
		//if the title exist in the anime series table
		if(doesItExist) {
			System.out.print("Enter f for finished or n for not watched yet: ");
			
			//if user puts in nothing print warning
			String input = scanner.nextLine();
			if(input.isEmpty()) {
				System.out.println("must enter either 'f' for finished or 'n' for not watched yet.");
				watchListMenu();
			}
		
				if(input.equalsIgnoreCase("f")) {
					complete = true;
				} else if(input.equalsIgnoreCase("n")){
					complete = false;
				} else {
					System.out.println("Invalid input. pleae enter 'f' for finished or 'n' for not watched yet");
					watchListMenu();
				}
				
		} else { //anime series does not exist in anime series table
			System.out.println("anime movie does not exist, can't be added to watch list");
			watchListMenu();
		}
		//call method to add to the watchlist
				manager.addToWatchList(title, episode, complete, movie);
	}
	
}
	
}
