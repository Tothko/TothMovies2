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

public DataAccess(){
    MD = new MovieDao();
    CD = new CategoryDao();


}

   

    @Override
    public List<Movie> getFilteredMovies(MovieFilter filter) {
        return MD.getFilteredMovies(filter);
    }

    @Override
    public List<Movie> getAllMovies() {
       return MD.getAllMovies();
    }

    @Override
    public List<Category> getAllCategories() {
        return CD.getlAllCategories();
    }

    @Override
    public void addMovie(Movie movie) {
        MD.addMovie(movie);
        
        }

    @Override
    public void removeMovie(Movie movie) {
        MD.removeMovie(movie);
    }

    @Override
    public void addCategory(Category category) {
        CD.addCategory(category);
        }

    @Override
    public void removeCategory(Category category) {
        CD.removeCategory(category);
    }

    @Override
    
    public void editMovie(Movie movie) {
        MD.editMovie(movie);
        
    }
   
}
