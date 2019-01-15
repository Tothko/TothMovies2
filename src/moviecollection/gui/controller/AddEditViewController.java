/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviecollection.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import moviecollection.be.Category;
import moviecollection.be.Movie;

/**
 * FXML Controller class
 *
 * @author mads_
 */
public class AddEditViewController implements Initializable {

    @FXML
    private TextField movieTitle;
    @FXML
    private TextField movieYear;
    @FXML
    private ListView<CheckBox> movieCat;
    @FXML
    private Button saveButton;
    @FXML
    private TextField fileField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    private ModelViewController mvc;
    private boolean edit = false;
    private Movie editMovie; 
    
    @FXML
    private void pressCancel(ActionEvent event) {
        Stage st = (Stage) saveButton.getScene().getWindow();
        st.close();
    }

    @FXML
    private void pressSave(ActionEvent event) {
        String year = movieYear.getText();
        short yearShort;
        //add shit
        if (!(year.matches("[0-9]+") && year.length() == 4)  || !year.isEmpty()) //If format isnt numbers with length 4 or is not empty
        {
            System.out.println("INVALID YEAR");
            return;
        } else {
            yearShort = Short.parseShort(year);
        }
        
        if(edit)
        {
            editMovie.setTitle(movieTitle.getText());
            editMovie.setMovieYear(yearShort);
            editMovie.setFilePath(fileField.getText());
            mvc.editMovie(editMovie);
            
        } else {
            Movie m = new Movie();
            m.setTitle(movieTitle.getText());
            m.setMovieYear(yearShort);
            m.setFilePath(fileField.getText());
            //ADD CATEGORIES
            mvc.addMovie(m);
            
        }
        
        Stage st = (Stage) saveButton.getScene().getWindow();
        st.close();
    }
    
    public void setModel(ModelViewController mvc)
    {
        this.mvc = mvc;
        if(editMovie != null)
            movieCat.setItems(mvc.getMovieEditCheckBoxes(editMovie));
        else
            movieCat.setItems(mvc.getMovieEditCheckBoxes());
    }

    @FXML
    private void chooseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileField.setText(fileChooser.showOpenDialog(((Node)event.getTarget()).getScene().getWindow()).getPath());
    }
    
    public void setEdit(boolean edit)
    {
        this.edit = edit;
    }
    
    public void setMovie(Movie m)
    {
        editMovie = m;
        movieTitle.setText(editMovie.getTitle());
        movieYear.setText(""+editMovie.getMovieYear());
        fileField.setText(editMovie.getFilePath());
    }
    
}
