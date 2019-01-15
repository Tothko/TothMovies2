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
    public MovieDao(){
    conProvider = new DbProvider();
}
    
    private Movie movieFromRs(ResultSet rs) //Missing categories
    {
        Movie retval = null;
        try
        {
            retval = new Movie(rs.getInt("ID"),rs.getString("Title"));
            retval.setFilePath(rs.getString("FilePath"));
            retval.setPersonalRating(rs.getShort("PersonalRating"));
            retval.setRating(rs.getShort("GlobalRating"));
            retval.setMovieYear(rs.getShort("Year"));
        } catch (SQLException ex)
        {
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retval;
    }

    public List<Movie> getAllMovies()  //Probably categories again
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
        } catch (SQLServerException ex)
        {
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(SQLException ex){
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return movies;
    }

    public void editMovie(Movie movie, List<Category> categories) { 
        
        try(Connection con = conProvider.getConnection())
        {
            String sql = "UPDATE Movies SET Title = ?, GlobalRating = ?, PersonalRating = ?, FilePath = ?, Year = ? WHERE id = ?";
            String sql2 = "UPDATE CatMovie SET MovieID = ? , CatID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, movie.getTitle());
            ps.setShort(2, movie.getRating());
            ps.setShort(3, movie.getPersonalRating());
            ps.setString(4, movie.getFilePath());
            ps.setShort(5, movie.getMovieYear());
            ps.setInt(6, movie.getId());
            
            
            ps.execute();
            for (Category category : categories) {
                PreparedStatement ps2 = con.prepareStatement(sql2);
                ps2.setInt(1, movie.getId());
                ps2.setInt(2, category.getId());
                ps2.execute();
                
            }
                 
        } catch (SQLServerException ex)
        {
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(SQLException ex){
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*
    public static void main(String [] kkt)
    {
        MovieFilter filt = new MovieFilter();
        filt.setTitle("Lo");
        filt.setIncludeAll(true);
        filt.setOrder(MovieFilter.SortType.GlobalRating);
        filt.setRating((short)0);
        ArrayList<Category> list = new ArrayList();
        list.add(new Category(1,"Action"));
        list.add(new Category(2,"Thriller"));
        filt.setCategories(list);
        MovieDao dao = new MovieDao();
        dao.getFilteredMovies(filt);
    }*/

    public List<Movie> getFilteredMovies(MovieFilter filter)  //We will see wether we will add lsit of categories to each movie
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
            String sqlStatement = "SELECT DISTINCT Movies.* "
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
        } catch (SQLServerException ex)
        {
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(SQLException ex){
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return movies;
    }

    public void removeMovie(Movie movie) { //Should be DONE
        try(Connection con = conProvider.getConnection()){
                   String sql = "DELETE From CatMovie WHERE MovieID = " + movie.getId() + "; DELETE From Movies WHERE ID = " + movie.getId();
                   Statement stmt = con.createStatement();
                   stmt.execute(sql);

               } catch (SQLServerException ex)
               {
                   Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
               }
               catch(SQLException ex){
                   Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
               }
    }

    public void addMovie(Movie movie, List<Category> categories) { //Missing adding categories
        
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
                 
        } catch (SQLServerException ex)
        {
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(SQLException ex){
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void setMoviePersonalRating(Movie movie, short rating)
    {
            try(Connection con = conProvider.getConnection())
        {
            String sql = "UPDATE Movies SET Rating = ?, WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setShort(1, rating);
            ps.setInt(2, movie.getId());
            ps.execute();
                 
        } catch (SQLServerException ex)
        {
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(SQLException ex){
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public List<Integer> getMovieCategories(Movie movie){
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
            
            
         } catch (SQLServerException ex)
        {
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(SQLException ex){
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return movieCategories;
    }
    
}}
