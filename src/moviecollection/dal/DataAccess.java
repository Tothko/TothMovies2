/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviecollection.dal;

import java.util.List;
import moviecollection.be.Category;
import moviecollection.be.Movie;
import moviecollection.be.MovieFilter;

/**
 *
 * @author Marek
 */
public class DataAccess implements IDataAccess
{
    private MovieDao MD;
    private CategoryDao CD;

    public DataAccess() throws DataLayerException{
        MD = new MovieDao();
        CD = new CategoryDao();


    }

   

    @Override
    public List<Movie> getFilteredMovies(MovieFilter filter) throws DataLayerException {
        return MD.getFilteredMovies(filter);
    }

    @Override
    public List<Movie> getAllMovies() throws DataLayerException {
       return MD.getAllMovies();
    }

    @Override
    public List<Category> getAllCategories() throws DataLayerException {
        return CD.getlAllCategories();
    }

    @Override
    public void addMovie(Movie movie, List<Category> categories) throws DataLayerException {
        MD.addMovie(movie, categories);
        
        }

    @Override
    public void removeMovie(Movie movie) throws DataLayerException {
        MD.removeMovie(movie);
    }

    @Override
    public void addCategory(Category category) throws DataLayerException {
        CD.addCategory(category);
        }

    @Override
    public void removeCategory(Category category) throws DataLayerException {
        CD.removeCategory(category);
    }

    @Override
    
    public void editMovie(Movie movie, List<Category> categories) throws DataLayerException {
        MD.editMovie(movie, categories);
        
    }

    @Override
    public void setMoviePersonalRating(Movie movie, short rating) throws DataLayerException
    {
        MD.setMoviePersonalRating(movie ,rating);
    }

    @Override
    public List<Integer> getMovieCategories(Movie m) throws DataLayerException
    {
        return MD.getMovieCategories(m);
    }
   
}
