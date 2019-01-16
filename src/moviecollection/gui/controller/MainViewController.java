/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviecollection.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import moviecollection.be.Category;
import moviecollection.be.Movie;
import moviecollection.be.MovieFilter;

/**
 *
 * @author Marek
 */
public class MainViewController implements Initializable
{
    
    private Label label;
    @FXML
    private ToggleGroup filter;
    @FXML
    private ListView<CheckBox> catList;
    @FXML
    private ListView<Movie> movieList;
    @FXML
    private Label movieTitle;
    @FXML
    private ProgressBar ratingBar;
    @FXML
    private Label rating;
    @FXML
    private TextField movieTextFilter;
    @FXML
    private TextField catSearchBar;
    @FXML
    private ToggleGroup orderBy;
    @FXML
    private Text movieCats;
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        model = new ModelViewController();
        // TODO
        catList.setItems(model.getCategoryList());
        movieList.setItems(model.getMovieList());        
    }    
    
    private ModelViewController model;
    private MovieFilter movieFilter;
    
    @FXML
    private void radioAll(ActionEvent event)
    {
        model.setIncludeAll(true);
    }

    @FXML
    private void radioOne(ActionEvent event)
    {
        model.setIncludeAll(false);
    }

    @FXML
    private void orderByTitle(ActionEvent event)
            
    {
        model.setOrderBy(MovieFilter.SortType.Title);
    }

    @FXML
    private void orderByCat(ActionEvent event)
    {
        model.setOrderBy(MovieFilter.SortType.Category);
    }

    @FXML
    private void orderByRating(ActionEvent event)
    {
        model.setOrderBy(MovieFilter.SortType.GlobalRating);
    }

    @FXML
    private void searchCat(KeyEvent event)
    {
    }

    @FXML
    private void addCat(ActionEvent event)
    {
        Category c = new Category(0, catSearchBar.getText());
        model.addCategory(c);
        
    }

    @FXML
    private void removeCat(ActionEvent event)
    {
        model.removeCategory(catSearchBar.getText());
    }

    @FXML
    private void searchMovies(KeyEvent event)
    {
        model.setMovieFilter(movieTextFilter.getText());
    }

    @FXML
    private void addMovie(ActionEvent event) throws IOException
    {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/moviecollection/gui/view/AddEditView.fxml"));
        stage.setScene(new Scene(loader.load()));
        
        AddEditViewController window = loader.<AddEditViewController>getController();
        window.setModel(model);
        window.setEdit(false);
        stage.showAndWait();
    }

    @FXML
    private void removeMovie(ActionEvent event)
    {
        Movie m = movieList.getSelectionModel().getSelectedItem();
        model.removeMovie(m);
    }

    @FXML
    private void editMovie(ActionEvent event) throws IOException
    {
        Movie selectedMovie = movieList.getSelectionModel().getSelectedItem();
        if(selectedMovie != null)
        {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/moviecollection/gui/view/AddEditView.fxml"));
            stage.setScene(new Scene(loader.load()));

            AddEditViewController window = loader.<AddEditViewController>getController();
            window.setMovie(selectedMovie);
            window.setModel(model);
            window.setEdit(true);
            stage.showAndWait();
            movieList.refresh();
            selectMovie(null);
        }
    }

    @FXML
    private void pressPlay(ActionEvent event)
    {
        Movie selectedMovie = movieList.getSelectionModel().getSelectedItem();
        if(selectedMovie != null)
        {
            if(!model.tryPlayMovie(selectedMovie))
            {
                new Alert(Alert.AlertType.ERROR, "Movie : '" + selectedMovie.getTitle() + "' can not be played").showAndWait();
            }
        }
        
    }

    @FXML
    private void selectMovie(MouseEvent event) {
        Movie m = movieList.getSelectionModel().getSelectedItem();
        rating.setText(m.getRating()+"/10");
        ratingBar.setProgress(((double)m.getRating())/10);
        movieTitle.setText(m.getTitle());
        if(m.getMovieYear()!=0)
            movieTitle.setText(movieTitle.getText()+" ("+m.getMovieYear()+")");
        movieCats.setText("Categories:\n");
        List<Category> categories = model.getMovieCategories(m);
        for (Category c : categories) {
            movieCats.setText(movieCats.getText()+c.getName()+"\n");
        }
    }
    
}
