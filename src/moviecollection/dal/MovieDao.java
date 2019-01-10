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
    
    private Movie MovieFromRs(ResultSet rs)
    {
        Movie retval = null;
        try
        {
            retval = new Movie(rs.getInt("ID"),rs.getString("Name"));
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
                movies.add(MovieFromRs(rs));
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

    public List<Movie> getFilteredMovies(MovieFilter filter) {
        return null;
    }

    public void removeMovie(Movie movie) {
 try(Connection con = conProvider.getConnection()){
            String sql = "DELETE From Movies WHERE ID = ?, DELETE From CatMovie WHERE ID = ?";
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
