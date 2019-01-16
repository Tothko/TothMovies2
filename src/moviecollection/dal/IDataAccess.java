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
public interface IDataAccess
{
    public List<Movie> getFilteredMovies(MovieFilter filter) throws DataLayerException;
    public List<Movie> getAllMovies() throws DataLayerException;
    public List<Category> getAllCategories() throws DataLayerException;
    public void addMovie(Movie movie, List<Category> categories) throws DataLayerException;
    public void removeMovie(Movie movie) throws DataLayerException;
    public void addCategory(Category category) throws DataLayerException;
    public void removeCategory(Category category) throws DataLayerException;
    public void editMovie(Movie movie, List<Category> categories) throws DataLayerException;
    public void setMoviePersonalRating(Movie movie,short rating) throws DataLayerException;
    public List<Integer> getMovieCategories(Movie m) throws DataLayerException;
}
