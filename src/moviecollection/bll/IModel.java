/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviecollection.bll;

import java.util.List;
import moviecollection.be.Category;
import moviecollection.be.Movie;
import moviecollection.be.MovieFilter;

/**
 *
 * @author Marek
 */
public interface IModel
{
    public List<Movie> getFilteredMovies(MovieFilter filter);
    public List<Movie> getAllMovies();
    public List<Category> getAllCategories();
    public void addMovie(Movie m, List<Category> categories);
    public void removeMovie(Movie m);
    public void addCategory(Category c);
    public void removeCategory(Category c);
    public void editMovie(Movie m, List<Category> categories);
    public void setMoviePersonalRating(Movie m, short rating);
    public List<Integer> getMovieCategories(Movie m);
}
