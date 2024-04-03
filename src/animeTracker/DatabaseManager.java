package animeTracker;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Scanner;

public class DatabaseManager {
	
	//Method to connect to the animeLibrary database
	public Connection connectToDatabase () {
		//establish connection to database
		String url = "jdbc:mysql://localhost:3306/animelibrary";
		String user = "root";
		String password = "";
		Connection connection = null; //connection object
		
		try {
			connection = DriverManager.getConnection(url, user, password);
			//System.out.println("successfully connected to database");
		} catch (SQLException e) {
			System.out.println("could not connect to database");
			e.printStackTrace();
		}
		return connection;
	}
	//method to return a prepared statement takes the connection and the sql statement
	public PreparedStatement returnStatement(Connection con, String sql) {
		PreparedStatement preparedStatement = null;
		String[] sqlArray = sql.split(" "); //split the sql statement into an array to get the first word
		String statementType = sqlArray[0];
		
		try {
			preparedStatement = con.prepareStatement(sql);
		} catch (SQLException e) {
			System.out.println("Error creating " + statementType + " statement");
			e.printStackTrace();
		}
		return preparedStatement;
	}
	//method to add a anime series to the animeLibrary database
	public void addToDatabase(AnimeSeries anime) {
		ResultSet resultSet = null; //store result from query
		Connection con = connectToDatabase(); //call method to connect to database
		String SqlStatement = new String("INSERT INTO animeseries(Title, ReleaseDate, TotalEpisodes, Genre, Description) VALUES (?, ?, ?, ?, ?)"); 
		PreparedStatement preparedStatement = returnStatement(con, SqlStatement); //call prepared statement method
		
	
		//set parameters for prepared statement
		try {
			if(anime.getTotalEpisodes() == 0) {
				preparedStatement.setString(1, anime.getTitle());
				preparedStatement.setDate(2, anime.getReleaseDate());
				preparedStatement.setNull(3, Types.NULL);
				preparedStatement.setString(4, anime.getGenres());
				preparedStatement.setString(5, anime.getDescription());
			} else {
			preparedStatement.setString(1, anime.getTitle());
			preparedStatement.setDate(2, anime.getReleaseDate());
			preparedStatement.setInt(3, anime.getTotalEpisodes());
			preparedStatement.setString(4, anime.getGenres());
			preparedStatement.setString(5, anime.getDescription());
			}
		} catch (SQLException e) {
			System.out.println("Error setting values for INSERT prepared statement");
			e.printStackTrace();
		}
		
		//create prepared statement to query the animie title to see if it already exist.
		String checkSqlStatement = "SELECT  Title FROM animeseries WHERE Title = ?";
		PreparedStatement checkPreparedStatement = returnStatement(con, checkSqlStatement);
		
		//set parameter for prepared statement
		try {
			checkPreparedStatement.setString(1, anime.getTitle());
		} catch (SQLException e) {
			System.out.println("Error setting values for SELECT prepared statement");
			e.printStackTrace();
		}
		
		//check if row is already in the database
		try {
			resultSet = checkPreparedStatement.executeQuery();
			if(resultSet.next()) {
				System.out.println("row already exists and cannot be added again");
				MainMenu.animeSeriesMenu(); //go back to anime series menu
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//execute statement
		try {
			int rowCount = preparedStatement.executeUpdate();
			if(rowCount == 1) {
				System.out.println("Anime series added");
				MainMenu.animeSeriesMenu(); //return to anime series menu
			} else {
				System.out.println("Error, anime series was not added");
			}
		} catch (SQLException e) {
			System.out.println("Error executeUpdate for prepared statement");
			e.printStackTrace();
		}
	}
	
	//method to list (all) anime series in database
	public void List(Object ob) {
		Connection con = connectToDatabase();
		ResultSet resultSet = null; //store result from query
		
		String SqlStatement = "";
		String errorMessage = "";
		String menuOption = "";
		int ep = 0;
		String episodeMessage = "";
		
		//if list is passed an anime object
		if(ob instanceof AnimeSeries) {
			//create SQL and prepared statement 
		SqlStatement =  "SELECT Title, ReleaseDate, TotalEpisodes, Genre, Description FROM animeseries";
		errorMessage ="no anime series found";
		menuOption = "animeSeries";
		
		//if list is passed a movie object
		} else if(ob instanceof AnimeMovies) {
			
		SqlStatement = "SELECT Title, ReleaseDate, Duration, Genre, Description FROM animemovies";
		errorMessage = "no movies found";
		menuOption = "animeMovie";
		}
			
		PreparedStatement preparedStatement = returnStatement(con, SqlStatement);
		
		try {
			resultSet = preparedStatement.executeQuery();
		} catch (SQLException e) {
			System.out.println("Error could not execute statement");
			e.printStackTrace();
		}
		
		//list everything in the anime series table
		try {
			if(!resultSet.next()) { //return to anime series menu if no anime series found
				System.out.println(errorMessage);
				switch(menuOption) {
				case "animeSeries": MainMenu.animeSeriesMenu();
				break;
				case "animeMovie": MainMenu.animeMovieMenu();
				break;
				default: MainMenu.displayMainMenu();
				}
				
				
			} else {
			do {
					//check which object get total episodes or total duration
				if(ob instanceof AnimeSeries) {
					ep = resultSet.getInt("TotalEpisodes");
					episodeMessage = "Total Episodes: ";
				} else if(ob instanceof AnimeMovies) {
					ep = resultSet.getInt("Duration");
					episodeMessage = "Duration: ";
				}
						String gen = resultSet.getString("Genre");
						String des = resultSet.getString("Description");
						Date da = resultSet.getDate("ReleaseDate");
						
						System.out.println("Title: " + resultSet.getString("Title")+ "\n" + "Release Date: " + (da != null ? da : "unknown") +
						"\n" + episodeMessage +  (ep != 0 ? ep : "unknown") + "\n" + "Genre(s): " + (gen != null ? gen : "unknown") + "\n" +
						"Description: " + (des != null ? des : "unknown"));
						System.out.println("");
			} while(resultSet.next());
			MainMenu.displayMainMenu(); //go back to main menu
			}
		} catch (SQLException e) {
			 System.out.println("Error printing out anime series");
			e.printStackTrace();
		}
}
	//method deletes movies and anime series from the database
	public void deleteFromDatabase(String title, Object ob) {
		Connection con = connectToDatabase();
		ResultSet resultSet = null; //store result from query
		
		//if is a anime series object
		if(ob instanceof AnimeSeries) {
		String deletesqlStatement = "DELETE FROM animeseries WHERE Title = ?";
		PreparedStatement preparedsqlStatement = returnStatement(con, deletesqlStatement);
		
		try {//set value in prepared statement
			preparedsqlStatement.setString(1, title);
		} catch (SQLException e) {
			System.out.println("Error setting value for DELETE prepared statement");
			e.printStackTrace();
		}
		
		try {
			int rowCount = preparedsqlStatement.executeUpdate();
			if(rowCount == 1) {
				System.out.println("anime series deleted");
				MainMenu.displayMainMenu(); //go back to main menu
			} else {
				System.out.println("Anime series does not exist");
				MainMenu.animeSeriesMenu(); // return to anime series menu
			}
		} catch (SQLException e) {
			System.out.println("Error executeUpdate for DELETE prepared statement");
			e.printStackTrace();
		}
	} else if (ob instanceof AnimeMovies) {
		
		String deletesqlStatement = "DELETE FROM animemovies WHERE Title = ?";
		PreparedStatement preparedsqlStatement = returnStatement(con, deletesqlStatement);
		
		try {//set value in prepared statement
			preparedsqlStatement.setString(1, title);
		} catch (SQLException e) {
			System.out.println("Error setting value for DELETE prepared statement");
			e.printStackTrace();
		}
		
		try {
			int rowCount = preparedsqlStatement.executeUpdate();
			if(rowCount == 1) {
				System.out.println("anime movie deleted");
				MainMenu.displayMainMenu(); //go back to main menu
			} else {
				System.out.println("Anime movie does not exist");
				MainMenu.animeMovieMenu(); // return to anime series menu
			}
		} catch (SQLException e) {
			System.out.println("Error executeUpdate for DELETE prepared statement");
			e.printStackTrace();
		}
	}
	}
	//method to search and display a anime based on its title
	public void search(String title, Object ob) {
		//establish database connection
				Connection con = connectToDatabase();
				ResultSet resultSet = null; //store result from query
				String sql = "";
				String menuOption = "";
				String episodeMessage = "";
				int ep = 0;
				
				if(ob instanceof AnimeSeries) {
					sql = "SELECT Title, ReleaseDate, TotalEpisodes, Genre, Description FROM animeseries WHERE Title = ?";
					menuOption = "animeSeries";
					
				} else if(ob instanceof AnimeMovies) {
					sql = "SELECT Title, ReleaseDate, Duration, Genre, Description FROM animemovies WHERE Title = ?";
					menuOption = "animeMovie";
				}
				PreparedStatement preparedStatement = returnStatement(con, sql);
				
				//set values
				try {
					preparedStatement.setString(1, title);
				} catch (SQLException e) {
					System.out.println("Error setting values into prepared statement");
					e.printStackTrace();
				}
				//execute prepared statement
				try {
					resultSet = preparedStatement.executeQuery();
				} catch (SQLException e) {
					System.out.println("Error executing prepared statement");
					e.printStackTrace();
				}
				
				try {
					if(!resultSet.next()) {
						System.out.println("No records found."); //if name isn't found
						switch(menuOption) {
						case "animeSeries": MainMenu.animeSeriesMenu();
						break;
						case "animeMovie": MainMenu.animeMovieMenu();
						break;
						default: MainMenu.displayMainMenu();
						break;
						}
					}
					else {
						if(ob instanceof AnimeSeries) {
							ep = resultSet.getInt("TotalEpisodes");
							episodeMessage = "Total Episodes: ";
							
						} else if(ob instanceof AnimeMovies) {
							ep = resultSet.getInt("Duration");
							episodeMessage = "Duration: ";
						}
						String gen = resultSet.getString("Genre");
						String des = resultSet.getString("Description");
						Date da = resultSet.getDate("ReleaseDate");
						
					 //if record is found
						System.out.println("Title: " + resultSet.getString("Title")+ "\n" + "Release Date: " + (da != null ? da : "unknown") +
								"\n" + episodeMessage + (ep != 0 ? ep : "unknown") + "\n" + "Genre(s): " + (gen != null ? gen : "unknown") + "\n" +
								"Description: " + (des != null ? des : "unknown"));
								System.out.println("");
								MainMenu.displayMainMenu();
					}
				} catch (SQLException e) {
					System.out.println("Error printing out anime series");
					e.printStackTrace();
				}
	}
	//method to update anime series entry in database
	public void updateAnimeSeries(String title, Object ob) {
		//establish connection to database
		Connection con = connectToDatabase();
		ResultSet resultSet = null; //store result from query
		
		String selectSql = "";
		String menuOption = "";
		//check the instance of the object
		if(ob instanceof AnimeSeries) {
			selectSql = "SELECT Title, ReleaseDate, TotalEpisodes, Genre, Description FROM animeseries WHERE Title = ?";
			menuOption = "animeSeries";
		} else if(ob instanceof AnimeMovies) {
			selectSql = "SELECT Title, ReleaseDate, Duration, Genre, Description FROM animemovies WHERE Title = ?";
			menuOption = "animeMovies";
		}
		//create sql and prepared statement
		
		PreparedStatement preparedStatement = returnStatement(con, selectSql);
		
		//insert parameter values
		try {
			preparedStatement.setString(1, title);
		} catch (SQLException e) {
			System.out.println("Error inserting values in perpared statement");
			e.printStackTrace();
		}
		//execute statement
		try {
			resultSet = preparedStatement.executeQuery();
		} catch (SQLException e) {
			System.out.println("Error executing statement");
			e.printStackTrace();
		}
		
		try {
			if(!resultSet.next()) { //if anime doesn't exist
				System.out.println("No records found");
				if(menuOption == "animeSeries") {
					MainMenu.animeSeriesMenu();
				} else if(menuOption == "animeMovies") {
					MainMenu.animeMovieMenu();
				}
			} else {
				int currentTotalEpisodes = 0;
				if(ob instanceof AnimeSeries) {
					currentTotalEpisodes = resultSet.getInt("TotalEpisodes");
				} else if(ob instanceof AnimeMovies) {
					currentTotalEpisodes = resultSet.getInt("Duration");
				}
				//record found
				String currentTitle = resultSet.getString("Title");
				Date currentDate = resultSet.getDate("ReleaseDate");
				String currentGenre = resultSet.getString("Genre");
				String currentDescription = resultSet.getString("Description");
				Scanner input = new Scanner(System.in); //do not close this input for some reason breaks code
				//prompt for new title
				System.out.print("current title is " + currentTitle + ". " +"Enter new title or Enter key to skip: ");
				//if user doesn't enter a new title use the same title.
				String newTitle = input.nextLine();
				String testTitle = newTitle; //testTitle that im gonna use to check it series user input exist
				if(newTitle.isEmpty()) {
					newTitle = title;
				} 
				
				Date date = null; //the date going to the anime series constructor
				int counter = 0; //counter to control loop
				LocalDate thisYear = LocalDate.now(); //todays date
				while(counter == 0) { 
					//prompt for new release date
					System.out.print("current release date is " + (currentDate != null ? currentDate : "unknown") + ". " + "Enter new release date or enter key to skip: ");
					String newDate = input.nextLine();
					
					if(newDate.isEmpty()) { //if input is empty break out of loop
						date = currentDate; //use the old date
						break;
					}
					
					try {//try to turn input into a date
						date = Date.valueOf(newDate);
						if(date.toLocalDate().getYear() > 1920 && date.toLocalDate().getYear() < (thisYear.getYear() + 10)) { //date must between 190 - the current year + 10
							counter++;
					} else {
						System.out.println("Invalid date please enter a year after 1920 and before " + (thisYear.getYear() + 10));
					}
						
					} catch (Exception e) {
						System.out.println("Invalid input enter (yyyy-mm-dd)");
					} 
				
				}
				//check instance of object so i can decide if we are updating duration or total episodes
				String episodePrompt = "";
				String skipPrompt = "";
				String errorMessage = "";
				
				if(ob instanceof AnimeSeries) {
					episodePrompt = "current total episodes is ";
					skipPrompt = "Enter new total episodes or enter key to skip: ";
					errorMessage = "Invalid number of episodes please enter a number between 1 - 3000";
					
				} else if(ob instanceof AnimeMovies) {
					episodePrompt = "current Duration is ";
					skipPrompt = "Enter new Duration or enter key to skip: ";
					errorMessage = "Invalid duration please enter a number between 1 - 300";
				}
				//prompt total episodes
				
				int newTotalEpisodes = 0;
				int count = 0; //counter to control while loop
				while(count == 0) {
				System.out.print(episodePrompt + (currentTotalEpisodes != 0 ? currentTotalEpisodes : "unknown") + ". " + skipPrompt);
				String total = input.nextLine();
				if(total.isEmpty()) {
					newTotalEpisodes = currentTotalEpisodes; //use the same data as the new total of episodes
					break;
				}
				try {
					newTotalEpisodes = Integer.valueOf(total); //try to convert the string to an integer ensure its within range then get out of loop
					if(newTotalEpisodes > 0 && newTotalEpisodes <= 3000) {
						count++;
					} else {
						System.out.println(errorMessage);
					}
				} catch (NumberFormatException e) {
					System.out.print("Invalid input please enter a number\n");
				}
				
				}
				//prompt for new genre(s)
				System.out.print("current genre(s) " + (currentGenre != null ? currentGenre : "unknown") + ". " + "Enter new genre(s) or enter key to skip: ");
				String newGenre = input.nextLine();
				if(newGenre.length() == 0) {
					newGenre = currentGenre;
				}
				
				//prompt for new description
				System.out.print("current description " + (currentDescription != null ? currentDescription : "unknown") + ". " +"Enter new description or enter key to skip: ");
				String newDescription = input.nextLine();
				if(newDescription.length() == 0) {
					newDescription = currentDescription;
				}
				
				//create sql and prepared statement
				String updateSql = "";
				
				if(ob instanceof AnimeSeries) {
					updateSql = "UPDATE animeseries SET Title = ?, ReleaseDate = ?, TotalEpisodes = ?, Genre = ?, Description = ? WHERE Title = ?";
				} else if(ob instanceof AnimeMovies) {
					updateSql = "UPDATE animemovies SET Title = ?, ReleaseDate = ?, Duration = ?, Genre = ?, Description = ? WHERE Title = ?";
				}
				
				PreparedStatement preparedStatement0 = returnStatement(con, updateSql);
				
				if(newTotalEpisodes == 0) {
					preparedStatement0.setString(1, newTitle);
					preparedStatement0.setDate(2, date);
					preparedStatement0.setNull(3, Types.NULL);
					preparedStatement0.setString(4, newGenre);
					preparedStatement0.setString(5, newDescription);
					preparedStatement0.setString(6, title);
				} else {
				//set new values
				preparedStatement0.setString(1, newTitle);
				preparedStatement0.setDate(2, date);
				preparedStatement0.setInt(3, newTotalEpisodes);
				preparedStatement0.setString(4, newGenre);
				preparedStatement0.setString(5, newDescription);
				preparedStatement0.setString(6, title);
				}
				
				String checkSqlStatement = "";
				//create prepared statement to query the anime title to see if it already exist.
				if(ob instanceof AnimeSeries) {
					checkSqlStatement = "SELECT  Title FROM animeseries WHERE Title = ?";
					
				} else if(ob instanceof AnimeMovies) {
					checkSqlStatement = "SELECT Title FROM animemovies WHERE Title = ?";
				}
				PreparedStatement checkPreparedStatement = returnStatement(con,checkSqlStatement);
				
				//set parameter for prepared statement
				try {
					checkPreparedStatement.setString(1, testTitle);
				} catch (SQLException e) {
					System.out.println("Error setting values for SELECT prepared statement");
					e.printStackTrace();
				}
				
				//check if row is already in the database
				try {
					resultSet = checkPreparedStatement.executeQuery();
					if(resultSet.next()) {
						System.out.println("Title already exist must use a different title");
						MainMenu.animeSeriesMenu(); //go back to anime series menu
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String updateMessage = "";
				String updateError = "";
				if(ob instanceof AnimeSeries) {
					updateMessage = "anime series updated";
					updateError = "error. anime series not updated";
					
				} else if(ob instanceof AnimeMovies) {
					updateMessage = "anime movie updated";
					updateError = "error. anime movie not updated";
				}
				//check to see if anime series updated
				int update = preparedStatement0.executeUpdate();
				if(update == 1) {
					System.out.println(updateMessage);
					
					if(menuOption == "animeSeries") {
						MainMenu.animeSeriesMenu();
						
					} else if(menuOption == "animeMovies") {
						MainMenu.animeMovieMenu();
					}
					
				} else {
					System.out.println(updateError);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error saving current data in variables");
			e.printStackTrace();
		}
	}

	public void AddMovieToDatabase(AnimeMovies movie) {
		//establish connection to database
		Connection con = connectToDatabase();
		ResultSet resultSet = null; //store result from query
				
		//create SQL and prepared statement 
		String SqlStatement = new String("INSERT INTO animemovies(Title, ReleaseDate, Duration, Genre, Description) VALUES (?, ?, ?, ?, ?)");
		PreparedStatement preparedStatement = returnStatement(con, SqlStatement);
			
				//set parameters for prepared statement
				try {
					if(movie.getDuration() == 0) {
						preparedStatement.setString(1, movie.getTitle());
						preparedStatement.setDate(2, movie.getReleaseDate());
						preparedStatement.setNull(3, Types.NULL);
						preparedStatement.setString(4, movie.getGenres());
						preparedStatement.setString(5, movie.getDescription());
					} else {
					preparedStatement.setString(1, movie.getTitle());
					preparedStatement.setDate(2, movie.getReleaseDate());
					preparedStatement.setInt(3, movie.getDuration());
					preparedStatement.setString(4, movie.getGenres());
					preparedStatement.setString(5, movie.getDescription());
					}
				} catch (SQLException e) {
					System.out.println("Error setting values for INSERT prepared statement");
					e.printStackTrace();
				}
				
				//create prepared statement to query the animie title to see if it already exist.
				String checkSqlStatement = "SELECT  Title FROM animemovies WHERE Title = ?";
				PreparedStatement checkPreparedStatement = returnStatement(con, checkSqlStatement);
				
				//set parameter for prepared statement
				try {
					checkPreparedStatement.setString(1, movie.getTitle());
				} catch (SQLException e) {
					System.out.println("Error setting values for SELECT prepared statement");
					e.printStackTrace();
				}
				
				//check if row is already in the database
				try {
					resultSet = checkPreparedStatement.executeQuery();
					if(resultSet.next()) {
						System.out.println("row already exists and cannot be added again");
						MainMenu.animeMovieMenu(); //go back to anime series menu
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//execute statement
				try {
					int rowCount = preparedStatement.executeUpdate();
					if(rowCount == 1) {
						System.out.println("Anime movie added");
						MainMenu.animeMovieMenu(); //return to anime series menu
					} else {
						System.out.println("Error, anime movie was not added");
					}
				} catch (SQLException e) {
					System.out.println("Error executeUpdate for prepared statement");
					e.printStackTrace();
		
	}
				
	}
	
	//method to check (by title and object) if the anime series or move exist in their respective table
	public boolean doesItExist(String title, Object ob) {
		ResultSet resultSet = null; //store result from query
		String sqlStatement = "";
		 
		Connection con =connectToDatabase(); //connect to database
		
		if( ob instanceof AnimeSeries) {
			sqlStatement = "SELECT Title FROM animeseries WHERE Title = ?";
		} else if(ob instanceof AnimeMovies) {
			
			sqlStatement = "SELECT Title FROM animemovies WHERE Title = ?";
		}
		
		PreparedStatement checkPreparedStatement = returnStatement(con, sqlStatement);
		
		//set parameter for prepared statement
		try {
			checkPreparedStatement.setString(1, title);
		} catch (SQLException e) {
			System.out.println("Error setting values for SELECT prepared statement");
			e.printStackTrace();
		}
		
		//check if row is already in the database
		try {
			resultSet = checkPreparedStatement.executeQuery();
			if(resultSet.next()) {
				//row does exist so return true
				return true;
			} else {
				//return false if it doesn't exist
				return false;
			}
		} catch (SQLException e) {
			System.out.println("error executing sql statement");
			e.printStackTrace();
		}
		return false;
	}
	public void addToWatchList(String title, int episode, boolean complete, Object ob) {
		//get connection to database
		Connection con = connectToDatabase();
		ResultSet resultSet = null;
		String sqlStatement = "";
		String insertStatement = new String("INSERT INTO watchlist (SeriesID, MovieID, CurrentEpisode, Complete) VALUES (?, ?, ?, ?)");
		/* 
		 * get the seriesID/movieID from the anime series table and save it in a variable to add to watchlist
		 * */
		
		//if object passed is a anime series
		if(ob instanceof AnimeSeries) {
			sqlStatement = "SELECT SeriesID FROM animeseries WHERE Title = ?";
			
		} else if(ob instanceof AnimeMovies) {
			sqlStatement = "SELECT MovieID FROM animemovies WHERE Title = ?";
		}
		//make preparedstatment
		PreparedStatement preparedStatement = returnStatement(con, sqlStatement);
		
		//set values
		try {
			preparedStatement.setString(1, title);
		} catch (SQLException e) {
			System.out.println("error setting values for SELECT prepared statement");
			e.printStackTrace();
		}
		int seriesID = 0; //holds series id to add to the watchlist
		//execute statement
		try {
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				if(ob instanceof AnimeSeries) {
					seriesID = resultSet.getInt("SeriesID");
					
				} else if(ob instanceof AnimeMovies) {
					seriesID = resultSet.getInt("MovieID");
				}
				
			}
		} catch (SQLException e) {
			System.out.println("error executing statement");
			e.printStackTrace();
		}
		/* check to see if the ID i grabbed out of the movie/series table is already in the watchl list if it is print error message
		 * 
		 * */
		ResultSet resultSet2 = null;
		String checkIDStatement = "";
		if(ob instanceof AnimeSeries) {
			 checkIDStatement = "SELECT SeriesID FROM watchlist WHERE SeriesID = ?";
		} else if(ob instanceof AnimeMovies) {
			checkIDStatement = "SELECT MovieID FROM watchlist WHERE MovieID = ?";
		}
		
		PreparedStatement checkIDPreparedStatement = returnStatement(con,checkIDStatement);
		
		try {
			checkIDPreparedStatement.setInt(1, seriesID);
		} catch (SQLException e) {
			System.out.println("error setting value in checkIDPreparedStatement");
			e.printStackTrace();
		}
		//execute query
		try {
			resultSet2 = checkIDPreparedStatement.executeQuery();
			String errorMessage = "";
			if(resultSet2.next()) {
				errorMessage = (ob instanceof AnimeSeries) ? "anime series already in watchlist cannot be added again" : "anime movie already in watchlist cannot be added again";
				System.out.println(errorMessage);
				MainMenu.watchListMenu();
			}
		} catch (SQLException e) {
			System.out.println("error executing checkIDPreparedStatement");
		}
		
		/*
		 * add data to the watchlist 
		 * */
		PreparedStatement addPreparedStatement = returnStatement(con, insertStatement);
		
		//set values
		try {
			if(ob instanceof AnimeSeries) {
				addPreparedStatement.setInt(1, seriesID);
				addPreparedStatement.setNull(2, Types.NULL);
				addPreparedStatement.setInt(3, episode);
				
			} else if(ob instanceof AnimeMovies) {
				addPreparedStatement.setNull(1, Types.NULL);
				addPreparedStatement.setInt(2, seriesID);
				addPreparedStatement.setNull(3, Types.NULL);
			}
			if(complete == true) {
				addPreparedStatement.setInt(4, 1);
			} else {
				addPreparedStatement.setInt(4, 0);
			}
		} catch (SQLException e) {
			System.out.println("error setting values in add prepared statement");
			e.printStackTrace();
		}
		
		//print message to let user know it was added successfully after executing
		try {
			int rowCount = addPreparedStatement.executeUpdate();
			if(rowCount == 1) {
				System.out.println(title + " was added to watch list");
				MainMenu.watchListMenu();
			}
		} catch (SQLException e) {
			System.out.println("error executing add prepared statement");
			e.printStackTrace();
		}
	}
	public void deleteFromWatchList(String title, Object ob) {
		Connection con = connectToDatabase();
		ResultSet resultSet = null;
		String selectStatement = "";
		String deleteStatement = "";
		
		/* 
		 * get the ID from the series/movie table and delete that ID in the watch list table
		 * */
		if(ob instanceof AnimeSeries) {
			selectStatement = "SELECT SeriesID FROM animeseries WHERE Title = ?";
			deleteStatement = "DELETE FROM watchlist WHERE SeriesID = ?";
			
		} else if(ob instanceof AnimeMovies) {
			selectStatement = "SELECT MovieID FROM animemovies WHERE Title = ?";
			deleteStatement = "DELETE FROM watchlist WHERE MovieID = ?";
		}
		
		PreparedStatement preparedStatement = returnStatement(con, selectStatement);
		
		//set values
		try {
			preparedStatement.setString(1, title);
		} catch (SQLException e) {
			System.out.println("error setting value of select prepared statement");
		}
		
		//get the id of the series/movie
		int ID = 0;
		//execute preparedstatement
		try {
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
			if(ob instanceof AnimeSeries) {
				ID = resultSet.getInt("SeriesID");
				
			} else if(ob instanceof AnimeMovies) {
				ID = resultSet.getInt("MovieID");
			}
			
			}
			
		} catch (SQLException e) {
			System.out.println("error executing prepared statement");
		}
		
		//create delete prepared statement
		PreparedStatement deletePreparedStatement = returnStatement(con, deleteStatement);
		
		//set values
		try {
			deletePreparedStatement.setInt(1, ID);
		} catch (SQLException e) {
			System.out.println("error setting value for delete prepared statement");
		}
		//execute statement
		try {
			int rowCount = deletePreparedStatement.executeUpdate();
			if(rowCount == 1) {
				System.out.println(title + " removed from watch list");
				MainMenu.watchListMenu();
			} else {
				System.out.println( title + " doesn't exist in watch list");
				MainMenu.watchListMenu();
			}
		} catch (SQLException e) {
			System.out.println("error executing statement");
			e.printStackTrace();
		}
	}
	public void listWatchList() {
		Connection con = connectToDatabase();
		ResultSet resultSet = null;
		String seriesSqlStatement = "SELECT animeseries.Title,"
				+ "		watchlist.CurrentEpisode,"
				+ "        watchlist.Complete,"
				+ "        animeseries.Description"
				+ "FROM"
				+ "		watchlist"
				+ "JOIN"
				+ "		animeseries ON watchlist.SeriesID = animeseries.SeriesID";
		
		PreparedStatement preparedStatement = returnStatement(con, seriesSqlStatement);
	}
	
}

