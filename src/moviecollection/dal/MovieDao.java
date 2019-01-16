/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviecollection.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import moviecollection.be.Category;
import moviecollection.be.Movie;
import moviecollection.be.MovieFilter;

/**
 *
 * @author Marek
 */
public class MovieDao
{
    
    private DbProvider conProvider;
    
    public MovieDao() throws DataLayerException
    {
        conProvider = new DbProvider();
    }
    
    private Movie movieFromRs(ResultSet rs) throws SQLException 
    {
        Movie retval = null;
        retval = new Movie(rs.getInt("ID"),rs.getString("Title"));
        retval.setFilePath(rs.getString("FilePath"));
        retval.setPersonalRating(rs.getShort("PersonalRating"));
        retval.setRating(rs.getShort("GlobalRating"));
        retval.setMovieYear(rs.getShort("Year"));
        return retval;
    }

    public List<Movie> getAllMovies() throws DataLayerException  //Probably categories again
    {
        List<Movie> movies = new ArrayList<>();
        try(Connection con = conProvider.getConnection())
        {
            String sqlStatement = "SELECT * FROM Movies";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            while(rs.next())
            {
                movies.add(movieFromRs(rs));
            }
        } 
        catch(SQLException ex){
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataLayerException("Error in SQL command : " + ex.getMessage()); 
        }
        return movies;
    }

    public void editMovie(Movie movie, List<Category> categories) throws DataLayerException { 
        
        try(Connection con = conProvider.getConnection())
        {
            String sql = "UPDATE Movies SET Title = ?, GlobalRating = ?, PersonalRating = ?, FilePath = ?, Year = ? WHERE ID = " + movie.getId();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, movie.getTitle());
            ps.setShort(2, movie.getRating());
            ps.setShort(3, movie.getPersonalRating());
            ps.setString(4, movie.getFilePath());
            ps.setShort(5, movie.getMovieYear());
            ps.execute();
            String deleteCategories = "DELETE FROM CatMovie WHERE MovieID = " + movie.getId();
            con.createStatement().execute(deleteCategories);
            for (Category c : categories) {
                String addCategories = "INSERT INTO CatMovie (MovieID,CatID) VALUES ( " + movie.getId() + ',' + c.getId() + " );";
                Statement stmt = con.createStatement();
                stmt.execute(addCategories);
            }                 
        } 
        catch(SQLException ex){
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataLayerException("Error in SQL command : " + ex.getMessage()); 
        }
    }

    public List<Movie> getFilteredMovies(MovieFilter filter) throws DataLayerException  //We will see wether we will add lsit of categories to each movie
    {
        List<Movie> movies = new ArrayList<>();
        List<Category> catList = filter.getCategories();
        String catFilter = "";
        String orAnd = filter.isIncludeAll() ? "AND" : "OR";
        if(catList.size() > 0)
        {
            catFilter += "AND (CatMovie.CatID = " + catList.get(0).getId() +" ";
            for(int i = 1; i < catList.size(); i++)
                catFilter += orAnd + " CatMovie.CatID = " + catList.get(i).getId() + " ";
            catFilter += ')';
        }
        try(Connection con = conProvider.getConnection())
        {
            String sqlStatement = "SELECT DISTINCT Movies.*,CatMovie.CatID AS Category "
                    + "FROM Movies LEFT JOIN CatMovie ON CatMovie.MovieID = Movies.ID "
                    + "WHERE Movies.GlobalRating >= ? AND Movies.Title LIKE ? " + catFilter + " ORDER BY " + filter.getSortType().name();
            PreparedStatement ps = con.prepareStatement(sqlStatement);
            ps.setInt(1, filter.getRating());
            ps.setString(2, filter.getTitle() + "%");
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                movies.add(movieFromRs(rs));
            }
        } 
        catch(SQLException ex){
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataLayerException("Error in SQL command : " + ex.getMessage()); 
        }
        return movies;
    }

    public void removeMovie(Movie movie) throws DataLayerException { //Should be DONE
        try(Connection con = conProvider.getConnection()){
                   String sql = "DELETE From CatMovie WHERE MovieID = " + movie.getId() + "; DELETE From Movies WHERE ID = " + movie.getId();
                   Statement stmt = con.createStatement();
                   stmt.execute(sql);

               } 
               catch(SQLException ex){
                   throw new DataLayerException("Error in SQL command : " + ex.getMessage()); 
               }
    }

    public void addMovie(Movie movie, List<Category> categories) throws DataLayerException { //Missing adding categories
        
        try(Connection con = conProvider.getConnection()){
            String sql = "INSERT INTO Movies (Title,GlobalRating,PersonalRating,FilePath,Year) VALUES (?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, movie.getTitle());
            ps.setShort(2, movie.getRating());
            ps.setShort(3, movie.getPersonalRating());
            ps.setString(4, movie.getFilePath());
            ps.setShort(5, movie.getMovieYear());
            ps.execute();
            ResultSet rs = con.createStatement().executeQuery("SELECT MAX(ID) as ID FROM Movies"); //this is done because we need to provide id to newly created movie object
            rs.next();
            movie.setId(rs.getInt("ID"));
            for (Category category : categories) {
                Statement stmt = con.createStatement(); // prepareStatement(sql2);
                stmt.execute("INSERT INTO CatMovie (MovieID,CatID) VALUES ( " + movie.getId() + ',' + category.getId() + " );"); 
            }
                 
        } 
        catch(SQLException ex){
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataLayerException("Error in SQL command : " + ex.getMessage()); 
        }
        
    }
    
    public void setMoviePersonalRating(Movie movie, short rating) throws DataLayerException
    {
            try(Connection con = conProvider.getConnection())
        {
            String sql = "UPDATE Movies SET Rating = ?, WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setShort(1, rating);
            ps.setInt(2, movie.getId());
            ps.execute();
                 
        }
        catch(SQLException ex){
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataLayerException("Error in SQL command : " + ex.getMessage()); 
        }
    }
    public List<Integer> getMovieCategories(Movie movie) throws DataLayerException{
        List<Integer> movieCategories = new ArrayList<>();      
        {
        try(Connection con = conProvider.getConnection()){
            String sql = "SELECT CatID from CatMovie Where MovieID = "+movie.getId();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next())
            {
                movieCategories.add(rs.getInt("CatID"));
            } 
        } 
        catch(SQLException ex){
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataLayerException("Error in SQL command : " + ex.getMessage()); 
        }
        return movieCategories;
    }
    
}}
