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
    
    private Movie movieFromRs(ResultSet rs)
    {
        Movie retval = null;
        try
        {
            retval = new Movie(rs.getInt("ID"),rs.getString("Title"));
            retval.setFilePath(rs.getString("FilePath"));
            retval.setPersonalRating(rs.getShort("PersonalRating"));
            retval.setRating(rs.getShort("GlobalRating"));
        } catch (SQLException ex)
        {
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retval;
    }

    public List<Movie> getAllMovies() {
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

    public void editMovie(Movie movie) {
        
        try(Connection con = conProvider.getConnection())
        {
            String sql = "UPDATE Movies SET Title = ?, Rating = ?, PersonalRating = ?, FilePath = ?, WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, movie.getTitle());
            ps.setShort(2, movie.getRating());
            ps.setShort(3, movie.getPersonalRating());
            ps.setInt(4, movie.getId());
            ps.execute();
                 
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

    public List<Movie> getFilteredMovies(MovieFilter filter) 
    {
        List<Movie> movies = new ArrayList<>();
        List<Category> catList = filter.getCategories();
        String catFilter = "";
        String orAnd = filter.isIncludeAll() ? "AND" : "OR";
        if(catList.size() > 0)
        {
            catFilter += "LEFT JOIN CatMovie ON CatMovie.CatID = ? ";
            for(int i = 1; i < catList.size(); i++)
                catFilter += orAnd + " CatMovie.CatID = ? ";
        }
        try(Connection con = conProvider.getConnection())
        {
            String sqlStatement = "SELECT Movies.* "
                    + "FROM Movies " + catFilter
                    + "WHERE Movies.GlobalRating >= ? AND Movies.Title LIKE ? ORDER BY " + filter.getSortType().name();
            PreparedStatement ps = con.prepareStatement(sqlStatement);
            int i;
            for (i = 1; i <= catList.size(); i++)
                ps.setInt(i, catList.get(i - 1).getId());  
            ps.setInt(i++, filter.getRating());
            ps.setString(i++, filter.getTitle() + "%");
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

    public void removeMovie(Movie movie) {
 try(Connection con = conProvider.getConnection()){
            String sql = "DELETE From CatMovie WHERE ID = ?; DELETE From Movies WHERE ID = ? ";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, movie.getId());
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

    public void addMovie(Movie movie) {
        
        try(Connection con = conProvider.getConnection()){
            String sql = "INSERT INTO Movies (Title) VALUES (?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, movie.getTitle());
            ps.execute();
                 
        } catch (SQLServerException ex)
        {
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(SQLException ex){
            Logger.getLogger(MovieDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
