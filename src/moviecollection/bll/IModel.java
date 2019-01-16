/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviecollection.bll;

import java.util.List;
import moviecollection.MovieCollectionException;
import moviecollection.be.Category;
import moviecollection.be.Movie;
import moviecollection.be.MovieFilter;

/**
 *
 * @author Marek
 */
public interface IModel
{
    public List<Movie> getFilteredMovies(MovieFilter filter) throws MovieCollectionException;
    public List<Movie> getAllMovies()  throws MovieCollectionException;
    public List<Category> getAllCategories() throws MovieCollectionException;
    public void addMovie(Movie m, List<Category> categories) throws MovieCollectionException;
    public void removeMovie(Movie m) throws MovieCollectionException;
    public void addCategory(Category c) throws MovieCollectionException;
    public void removeCategory(Category c) throws MovieCollectionException;
    public void editMovie(Movie m, List<Category> categories) throws MovieCollectionException;
    public void setMoviePersonalRating(Movie m, short rating) throws MovieCollectionException;
    public List<Integer> getMovieCategories(Movie m) throws MovieCollectionException;
}
