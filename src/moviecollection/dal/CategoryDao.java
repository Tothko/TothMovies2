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

/**
 *
 * @author Marek
 */
public class CategoryDao
       
{
    private DbProvider conProvider;
    
    public CategoryDao() throws DataLayerException
    {
        conProvider = new DbProvider();
    }
    
    private Category CategoryFromRs(ResultSet rs) throws SQLException
    {
        Category retval = null;
         retval = new Category(rs.getInt("ID"),rs.getString("Name"));
        return retval;
    }
    
    public List<Category> getlAllCategories() throws DataLayerException {
        
        List<Category> Categories = new ArrayList<>();
        try(Connection con = conProvider.getConnection())
        {
            String sqlStatement = "SELECT * FROM Categories";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            while(rs.next())
            {
                Categories.add(CategoryFromRs(rs));
            }
        } 
        catch(SQLException ex){ //This shouldnt happen
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataLayerException("Error in SQL command : " + ex.getMessage()); 
        }
        return Categories;
    
    }

    public void removeCategory(Category c) throws DataLayerException{
        try (Connection con = conProvider.getConnection()){
            String sql = "DELETE FROM CatMovie WHERE CatID = " + c.getId() + " DELETE FROM Categories WHERE ID = " + c.getId();
            Statement stmt = con.createStatement();
            stmt.execute(sql);
        }
        catch(SQLException ex){ //This shouldnt happen
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataLayerException("Error in SQL command : " + ex.getMessage()); 
        }
    }

    public void addCategory(Category c) throws DataLayerException 
    {
        try (Connection con = conProvider.getConnection()){
            String sql = "INSERT INTO Categories (Name) VALUES (?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, c.getName());
            stmt.execute();
            ResultSet rs = con.createStatement().executeQuery("SELECT MAX(ID) as ID FROM Categories"); //this is done because we need to provide id to newly created category object
            rs.next();
            c.setId(rs.getInt("ID"));
        }
        catch(SQLException ex){ //This shouldnt happen
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataLayerException("Error in SQL command : " + ex.getMessage()); 
        }
    }
    
}
