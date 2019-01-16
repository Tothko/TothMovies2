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
import moviecollection.dal.DataAccess;
import moviecollection.dal.IDataAccess;

/**
 *
 * @author Marek
 */
public class BusinessModel implements IModel
{

    private IDataAccess dao;
    
    //Change for GITHUB
    // Good change for GITHUB
    public BusinessModel() throws MovieCollectionException
    {
        dao = new DataAccess();
    }
    

    @Override
    public List<Movie> getFilteredMovies(MovieFilter filter) throws MovieCollectionException
    {
        return dao.getFilteredMovies(filter);
    }

    @Override
    public List<Movie> getAllMovies() throws MovieCollectionException
    {
        return dao.getAllMovies();
    }

    @Override
    public List<Category> getAllCategories() throws MovieCollectionException
    {
        return dao.getAllCategories();
    }

    @Override
    public void addMovie(Movie m, List<Category> categories) throws MovieCollectionException
    {
        dao.addMovie(m, categories);
    }

    @Override
    public void removeMovie(Movie m) throws MovieCollectionException
    {
        dao.removeMovie(m);
    }

    @Override
    public void addCategory(Category c) throws MovieCollectionException
    {
        dao.addCategory(c);
    }

    @Override
    public void removeCategory(Category c) throws MovieCollectionException
    {
        dao.removeCategory(c);
    }

    @Override
    public void editMovie(Movie m, List<Category> categories) throws MovieCollectionException
    {
        dao.editMovie(m, categories);
    }

    @Override
    public void setMoviePersonalRating(Movie m, short rating) throws MovieCollectionException
    {
        dao.setMoviePersonalRating(m, rating);
    }
    
    @Override
    public List<Integer> getMovieCategories(Movie m) throws MovieCollectionException
    {
        return dao.getMovieCategories(m);
    }

    
}
