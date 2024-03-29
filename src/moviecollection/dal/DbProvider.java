/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviecollection.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marek
 */
public class DbProvider implements IDbProvider
{
    private SQLServerDataSource ds; 
    private Properties prop;
    
    public DbProvider() throws DataLayerException
    {
        ds = new SQLServerDataSource();
        prop = new Properties();      
        loadProperties();
    }
    
    @Override
    public Connection getConnection() throws DataLayerException
    {
        try
        {
            return ds.getConnection();
        } catch (SQLServerException ex)
        {
            Logger.getLogger(DbProvider.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataLayerException("Can not connect to database : '" + prop.getProperty("DbName") + "'");
        }
    }
    
    private void loadProperties() throws DataLayerException
    {
        try(FileInputStream input = new FileInputStream("src/moviecollection/DbConnection.prop")){
            prop.load(input);
        }catch(IOException ex){
            Logger.getLogger(DbProvider.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataLayerException("Database properties file is not accessible");
        }
        ds.setDatabaseName(prop.getProperty("DbName"));
        ds.setUser(prop.getProperty("UserName"));
        ds.setPassword(prop.getProperty("Pass"));
        ds.setPortNumber(Integer.parseInt(prop.getProperty("PortNum")));
        ds.setServerName(prop.getProperty("ServerName"));
    } 
}
