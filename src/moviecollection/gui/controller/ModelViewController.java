/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviecollection.gui.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import moviecollection.be.Category;
import moviecollection.be.Movie;
import moviecollection.be.MovieFilter;
import moviecollection.bll.BusinessModel;
import moviecollection.bll.IModel;

/**
 *
 * @author Marek
 */
public class ModelViewController
{
    private ObservableList<Movie> movieList;
    private ObservableList<CheckBox> categoryList;
    private IModel model;
    private MovieFilter filter;
    
  /*  public static void main(String[] kokot)
    {
        ModelViewController model = new ModelViewController();
        Movie m = new Movie();
        m.setFilePath("C:\\Users\\Marek\\Pictures\\Instagram\\911B253A-5CDB-4F1D-A6FF-3EF0426252D7.mov");
        model.tryPlayMovie(m);
    }*/
    public ModelViewController()
    {
        model = new BusinessModel();
        filter = new MovieFilter();
        movieList = FXCollections.observableArrayList(model.getAllMovies());
        categoryList = FXCollections.observableArrayList();
        List<Category> temp = model.getAllCategories();
        for(Category cat: temp)
            categoryList.add(new CheckBox(cat.getName()));
    }
    
    public void setMovieFilter(String filter)
    {
        this.filter.setTitle(filter);
        movieList.setAll(model.getFilteredMovies(this.filter));
    }
    
    public void setMovieFilter(MovieFilter filter)
    {
        this.filter = filter;
        movieList.setAll(model.getFilteredMovies(filter));
    }
    
    public List<Movie> getMovieList()
    {
        return movieList;
    }
    
    public List<CheckBox> getCategoryList()
    {
        return categoryList;
    }
    
    public void addMovie(Movie m)
    {
        if(!movieList.contains(m))
        {
            model.addMovie(m);
            movieList.add(m);
        }
    }
    
    public void removeMovie(Movie m)
    {
        if(movieList.contains(m))
        {
            model.removeMovie(m);
            movieList.remove(m);
        }
    }
    
    public void addCategory(Category c)
    {
        for(CheckBox chb: categoryList)
        {
            if(chb.getText().toLowerCase().equals(c.getName().toLowerCase()))
                return;
        }
        model.addCategory(c);
        categoryList.add(new CheckBox(c.getName()));
    }
    
    public void removeCategory(Category c)
    {
        for(CheckBox chb: categoryList)
        {
            if(chb.getText().toLowerCase().equals(c.getName().toLowerCase()))
            {
                categoryList.remove(chb);
                model.removeCategory(c);
                break;
            }
        }
    }
    
    public void editMovie(Movie m)
    {
        if(movieList.contains(m))
            model.editMovie(m);
    }
    
    public void setMovieRating(Movie m,short rating)
    {
        if(movieList.contains(m))
        {
            m.setPersonalRating(rating);
            model.editMovie(m);
        }
    }
    
    public boolean tryPlayMovie(Movie m)
    {
        if(!m.getFilePath().isEmpty())
        {
            File f = new File(m.getFilePath());
            if(f.exists() && !f.isDirectory()) 
            { 
                try
                {
                    Desktop.getDesktop().open(f);
                } catch (IOException ex)
                {
                    return false;
                }
                return true;
            }
        }
        return false;
    }
}
