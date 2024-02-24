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
	
	//Method to add anime series to database
	public void addToDatabase(AnimeSeries anime) {
		//establish connection to database
		String url = "jdbc:mysql://localhost:3306/animelibrary";
		String user = "root";
		String password = "";
		Connection connection = null; //connection object
		ResultSet resultSet = null; //store result from query
		
		try {
			connection = DriverManager.getConnection(url, user, password);
			//System.out.println("successfully connected to database");
		} catch (SQLException e) {
			System.out.println("could not connect to database");
			e.printStackTrace();
		}
		//create SQL and prepared statement 
		String SqlStatement = new String("INSERT INTO animeseries(Title, ReleaseDate, TotalEpisodes, Genre, Description) VALUES (?, ?, ?, ?, ?)");
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement(SqlStatement);
		} catch (SQLException e) {
			System.out.println("Error creating INSERT prepared statement");
			e.printStackTrace();
		}
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
		PreparedStatement checkPreparedStatement = null;
		
		try {
			checkPreparedStatement = connection.prepareStatement(checkSqlStatement);
		} catch (SQLException e) {
			System.out.println("Error creating SELECT prepared statement");
			e.printStackTrace();
		}
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
	public void List() {
		//establish database connection
		String url = "jdbc:mysql://localhost:3306/animelibrary";
		String user = "root";
		String password = "";
		Connection connection = null; //connection object
		ResultSet resultSet = null; //store result from query
		
		try {
			connection = DriverManager.getConnection(url, user, password);
			//System.out.println("successfully connected to database");
		} catch (SQLException e) {
			System.out.println("could not connect to database");
			e.printStackTrace();
		}
		//create SQL and prepared statement 
		String SqlStatement =  "SELECT Title, ReleaseDate, TotalEpisodes, Genre, Description FROM animeseries";
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement(SqlStatement);
		} catch (SQLException e) {
			System.out.println("Error creating SELECT prepared statement");
			e.printStackTrace();
		}
		
		try {
			resultSet = preparedStatement.executeQuery();
		} catch (SQLException e) {
			System.out.println("Error could not execute statement");
			e.printStackTrace();
		}
		
		//list everything in the anime series table
		try {
			if(!resultSet.next()) { //return to anime series menu if no anime series found
				System.out.println("no anime series found");
				MainMenu.animeSeriesMenu();
				
				
			} else {
			do {
						int ep = resultSet.getInt("TotalEpisodes");
						String gen = resultSet.getString("Genre");
						String des = resultSet.getString("Description");
						Date da = resultSet.getDate("ReleaseDate");
						
						System.out.println("Title: " + resultSet.getString("Title")+ "\n" + "Release Date: " + (da != null ? da : "unknown") +
						"\n" + "Total Episodes: " +  (ep != 0 ? ep : "unknown") + "\n" + "Genre(s): " + (gen != null ? gen : "unknown") + "\n" +
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
	//creates connection to the database and deletes the anime
	public void deleteFromDatabase(String title) {
		//establish database connection
		String url = "jdbc:mysql://localhost:3306/animelibrary";
		String user = "root";
		String password = "";
		Connection connection = null; //connection object
		ResultSet resultSet = null; //store result from query
		
		try {
			connection = DriverManager.getConnection(url, user, password);
			//System.out.println("successfully connected to database");
		} catch (SQLException e) {
			System.out.println("could not connect to database");
			e.printStackTrace();
		}
		
		String deletesqlStatement = "DELETE FROM animeseries WHERE Title = ?";
		PreparedStatement preparedsqlStatement = null;
		
		try {
			preparedsqlStatement = connection.prepareStatement(deletesqlStatement);
		} catch (SQLException e) {
			System.out.println("Error creating DELETE prepared statement");
			e.printStackTrace();
		}
		
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
	}
	//method to search and display a anime based on its title
	public void searchAnimeSeries(String title) {
		//establish database connection
				String url = "jdbc:mysql://localhost:3306/animelibrary";
				String user = "root";
				String password = "";
				Connection connection = null; //connection object
				ResultSet resultSet = null; //store result from query
				
				try {
					connection = DriverManager.getConnection(url, user, password);
					//System.out.println("successfully connected to database");
				} catch (SQLException e) {
					System.out.println("could not connect to database");
					e.printStackTrace();
				}
				
				String sql = "SELECT Title, ReleaseDate, TotalEpisodes, Genre, Description FROM animeseries WHERE Title = ?";
				PreparedStatement preparedStatement = null;
				
				try {
					preparedStatement = connection.prepareStatement(sql);
				} catch (SQLException e) {
					System.out.println("Error creating SELECT prepared statement");
					e.printStackTrace();
				}
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
						MainMenu.animeSeriesMenu(); //go back to series menu
					}
					else {
						int ep = resultSet.getInt("TotalEpisodes");
						String gen = resultSet.getString("Genre");
						String des = resultSet.getString("Description");
						Date da = resultSet.getDate("ReleaseDate");
						
					 //if record is found
						System.out.println("Title: " + resultSet.getString("Title")+ "\n" + "Release Date: " + (da != null ? da : "unknown") +
								"\n" + "Total Episodes: " + (ep != 0 ? ep : "unknown") + "\n" + "Genre(s): " + (gen != null ? gen : "unknown") + "\n" +
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
	public void updateAnimeSeries(String title) {
		//establish connection to database
		String url = "jdbc:mysql://localhost:3306/animelibrary";
		String user = "root";
		String password = "";
		Connection connection = null; //connection object
		ResultSet resultSet = null; //store result from query
		
		try {
			connection = DriverManager.getConnection(url, user, password);
			//System.out.println("successfully connected to database");
		} catch (SQLException e) {
			System.out.println("could not connect to database");
			e.printStackTrace();
		}
		//create sql and prepared statement
		String selectSql = "SELECT Title, ReleaseDate, TotalEpisodes, Genre, Description FROM animeseries WHERE Title = ?";
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement(selectSql);
		} catch (SQLException e) {
			System.out.println("Error creating SELECT prepared statement");
			e.printStackTrace();
		}
		//insert parameter value
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
				MainMenu.animeSeriesMenu();
			} else {
				//record found
				String currentTitle = resultSet.getString("Title");
				Date currentDate = resultSet.getDate("ReleaseDate");
				int currentTotalEpisodes = resultSet.getInt("TotalEpisodes");
				String currentGenre = resultSet.getString("Genre");
				String currentDescription = resultSet.getString("Description");
				Scanner input = new Scanner(System.in); //do not close this input for some reason breaks code
				//prompt for new title
				System.out.print("current title is " + currentTitle + ". " +"Enter new title or Enter key to skip: ");
				//if user doesn't enter a new title use the same title.
				String newTitle = input.nextLine();
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
				//prompt total episodes
				
				int newTotalEpisodes = 0;
				int count = 0; //counter to control while loop
				while(count == 0) {
				System.out.print("current total episodes is " + (currentTotalEpisodes != 0 ? currentTotalEpisodes : "unknown") + ". " + "Enter new total episodes or enter key to skip: ");
				String total = input.nextLine();
				if(total.isEmpty()) {
					break;
				}
				try {
					newTotalEpisodes = Integer.valueOf(total); //try to convert the string to an integer ensure its within range then get out of loop
					if(newTotalEpisodes > 0 && newTotalEpisodes <= 3000) {
						count++;
					} else {
						System.out.println("Invalid number of episodes please enter a number between 1 - 3000");
					}
				} catch (NumberFormatException e) {
					System.out.print("Invalid input please enter a number\n");
				}
				
				}
				//prompt for new genre(s)
				System.out.print("current genre(s) " + (currentGenre != null ? currentGenre : "unknown") + ". " + "Enter new genre(s) or enter key to skip: ");
				String newGenre = input.nextLine();
				if(newGenre.length() == 0) {
					newGenre = null;
				}
				
				//prompt for new description
				System.out.print("current description " + (currentDescription != null ? currentDescription : "unknown") + ". " +"Enter new description or enter key to skip: ");
				String newDescription = input.nextLine();
				if(newDescription.length() == 0) {
					newDescription = null;
				}
				
				//create sql and prepared statement
				String updateSql = "UPDATE animeseries SET Title = ?, ReleaseDate = ?, TotalEpisodes = ?, Genre = ?, Description = ? WHERE Title = ?";
				PreparedStatement preparedStatement0 = null;
				
				try {
					preparedStatement0 = connection.prepareStatement(updateSql);
				} catch (SQLException e) {
					System.out.println("Error creating UPDATE prepared statement");
					e.printStackTrace();
				}
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
				
				//check to see if anime series updated
				int update = preparedStatement0.executeUpdate();
				if(update == 1) {
					System.out.println("anime series updated");
					MainMenu.animeSeriesMenu();
				} else {
					System.out.println("Error. anime series NOT updated");
				}
			}
		} catch (SQLException e) {
			System.out.println("Error saving current data in variables");
			e.printStackTrace();
		}
	}

	public void AddMovieToDatabase(AnimeMovies movie) {
		// TODO Auto-generated method stub
		
	}

}
