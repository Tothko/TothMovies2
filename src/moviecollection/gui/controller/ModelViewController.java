/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviecollection.gui.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import moviecollection.MovieCollectionException;
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
    private ObservableList<CheckBox> categoryCheckBoxList;
    private ObservableList<CheckBox> movieEditCheckBoxes;
    private List<Category> categoryList;
    private IModel model;
    private MovieFilter filter;
    
    public ModelViewController() throws MovieCollectionException
    {
        model = new BusinessModel();
        filter = new MovieFilter();
        movieList = FXCollections.observableArrayList(model.getAllMovies());
        categoryCheckBoxList = FXCollections.observableArrayList();
        categoryList = FXCollections.observableArrayList();
        categoryList = model.getAllCategories();
        for (Category cat : categoryList)
        {
            CheckBox chb = createCheckBoxFromCategory(cat);
            categoryCheckBoxList.add(chb);
        }
    }
    
    private CheckBox createCheckBoxFromCategory(Category cat)
    {
        CheckBox chb = new CheckBox(cat.getName());
        chb.setOnAction( e -> 
        {
            CheckBox source = (CheckBox)e.getSource();
            Category selectedCategory = null;
            for (Category category : categoryList)
            {
                if(category.getName().toLowerCase().equals(source.getText().toLowerCase()))
                    selectedCategory = category;
            }
            if(selectedCategory != null)
            {
                if(source.isSelected())
                    filter.addCategory(cat);
                else
                    filter.removeCategory(cat);
                try
                {
                    movieList.setAll(model.getFilteredMovies(this.filter));
                } catch (MovieCollectionException ex)
                {
                    Logger.getLogger(ModelViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }   
        });
        return chb;
    }
    
    public void setMovieFilter(String filter) throws MovieCollectionException
    {
        this.filter.setTitle(filter);
        movieList.setAll(model.getFilteredMovies(this.filter));
    }
    
    public void setIncludeAll(boolean includeAll) throws MovieCollectionException
    {
        filter.setIncludeAll(includeAll);
        movieList.setAll(model.getFilteredMovies(filter));
    }
    
    public void setOrderBy(MovieFilter.SortType order) throws MovieCollectionException
    {
        filter.setOrder(order);
        movieList.setAll(model.getFilteredMovies(filter));
    }
    
    public void setMinimumRating(short rating) throws MovieCollectionException
    {
        filter.setRating(rating);
        movieList.setAll(model.getFilteredMovies(filter));
    }
    
    public ObservableList<Movie> getMovieList()
    {
        return movieList;
    }
    
    public ObservableList<CheckBox> getCategoryList()
    {
        return categoryCheckBoxList;
    }
    
    public ObservableList<CheckBox> getMovieEditCheckBoxes()
    {
        movieEditCheckBoxes = FXCollections.observableArrayList();
        for (Category category : categoryList)
        {
            movieEditCheckBoxes.add(new CheckBox(category.getName()));
        }
        return movieEditCheckBoxes;
    }
    
    public ObservableList<CheckBox> getMovieEditCheckBoxes(Movie m) throws MovieCollectionException
    {
        movieEditCheckBoxes = FXCollections.observableArrayList();
        List<Integer> movieCatIds = model.getMovieCategories(m);
        for (Category category : categoryList)
        {
            CheckBox chb = new CheckBox(category.getName());
            for (Integer movieCatId : movieCatIds)
            {
                if (movieCatId == category.getId())
                {
                    chb.setSelected(true);
                    break;
                }
            }
            movieEditCheckBoxes.add(chb);
        }
        return movieEditCheckBoxes;
    }
    
    public List<Category> getMovieCategories(Movie m) throws MovieCollectionException
    {
        List<Category> retval = new ArrayList();
        List<Integer> movieCats = model.getMovieCategories(m);
        for (Integer movieCat : movieCats)
        {
            for (Category cat : categoryList)
            {
                if (cat.getId() == movieCat)
                {
                    retval.add(cat);
                    break;
                }
            }
        }
        return retval;
    }
    
    public void addMovie(Movie m) throws MovieCollectionException
    {
        if(!movieList.contains(m))
        {
            model.addMovie(m,getSelectedCategoryList(movieEditCheckBoxes));
        }
    }
    
    public void removeMovie(Movie m) throws MovieCollectionException
    {
        if (movieList.contains(m))
        {
            model.removeMovie(m);
            movieList.remove(m);
        }
    }
    
    public void addCategory(Category c) throws MovieCollectionException
    {
        for (CheckBox chb : categoryCheckBoxList)
            if (chb.getText().toLowerCase().equals(c.getName().toLowerCase()))
                return;
        model.addCategory(c);
        categoryList.add(c);
        categoryCheckBoxList.add(createCheckBoxFromCategory(c));
    }
    
    public void removeCategory(String categoryName) throws MovieCollectionException
    {
        Category delCat = null;
        for(Category cat: categoryList)
        {
            if(cat.getName().toLowerCase().equals(categoryName.toLowerCase()))
                delCat = cat;                
        }
        if(delCat == null)
            return;
        for(CheckBox chb: categoryCheckBoxList)
        {
            if(chb.getText().toLowerCase().equals(categoryName.toLowerCase()))
            {
                categoryCheckBoxList.remove(chb);
                categoryList.remove(delCat);
                model.removeCategory(delCat);
                break;
            }
        }
    }
    
    public void editMovie(Movie m) throws MovieCollectionException
    {
        if(movieList.contains(m))
        {
            model.editMovie(m,getSelectedCategoryList(movieEditCheckBoxes));
        }
    }
    
    public void setMovieRating(Movie m,short rating) throws MovieCollectionException
    {
        if(movieList.contains(m))
        {
            m.setPersonalRating(rating);
            model.setMoviePersonalRating(m, rating);
        }
    }
    
    public boolean tryPlayMovie(Movie m)
    {
        String fpath = m.getFilePath();
        if(fpath != null && !fpath.isEmpty())
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
        
    private List<Category> getSelectedCategoryList(List<CheckBox> CheckBoxes)
    {
        List<Category> retval = new ArrayList();
        for (int i = 0; i < CheckBoxes.size(); i++)
        {
            CheckBox chb = CheckBoxes.get(i);
            if (chb.isSelected())
            {
                retval.add(categoryList.get(i));
            }
        }
        return retval;
    }
    
}
