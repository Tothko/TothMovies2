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
    private ObservableList<CheckBox> categoryCheckBoxList;
    private ObservableList<CheckBox> movieEditCheckBoxes;
    private List<Category> categoryList;
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
        categoryCheckBoxList = FXCollections.observableArrayList();
        categoryList = FXCollections.observableArrayList();
        categoryList = model.getAllCategories();
        for(Category cat: categoryList)
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
                    movieList.setAll(model.getFilteredMovies(this.filter));
                }   
            });
            return chb;
    }
    
    public void setMovieFilter(String filter)
    {
        this.filter.setTitle(filter);
        movieList.setAll(model.getFilteredMovies(this.filter));
    }
    
    public void setIncludeAll(boolean includeAll)
    {
        filter.setIncludeAll(includeAll);
        movieList.setAll(model.getFilteredMovies(filter));  
    }
    
    public void setOrderBy(MovieFilter.SortType order)
    {
        filter.setOrder(order);
        movieList.setAll(model.getFilteredMovies(filter)); 
    }
    
    public void setMinimumRating(short rating)
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
    
    public void addMovie(Movie m)
    {
        if(!movieList.contains(m))
        {
            model.addMovie(m,getSelectedCategoryList(movieEditCheckBoxes));
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
        for(CheckBox chb: categoryCheckBoxList)
        {
            if(chb.getText().toLowerCase().equals(c.getName().toLowerCase()))
                return;
        }
        model.addCategory(c);
        categoryList.add(c);
        categoryCheckBoxList.add(createCheckBoxFromCategory(c));
    }
    
    public void removeCategory(String categoryName)
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
                model.removeCategory(delCat);
                break;
            }
        }
    }
    
    public void editMovie(Movie m)
    {
        if(movieList.contains(m))
        {
            model.editMovie(m,getSelectedCategoryList(movieEditCheckBoxes));
        }
    }
    
    public void setMovieRating(Movie m,short rating)
    {
        if(movieList.contains(m))
        {
            m.setPersonalRating(rating);
            model.setMoviePersonalRating(m,rating);
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
                if(chb.isSelected())
                    retval.add(categoryList.get(i));                
            }  
            return retval;
    }
    
}
