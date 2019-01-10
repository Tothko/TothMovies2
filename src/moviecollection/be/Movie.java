/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviecollection.be;

/**
 *
 * @author Marek
 */
public class Movie
{
    private String title;
    private short rating;
    private short personalRating;
    private String filePath;
    private int id;
    private short movieYear;
    
    public Movie(){
        
    }
    public Movie(int id, String title){
        this.id = id;
        this.title = title;
        
    }
    
    
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public short getRating() {
        return rating;
    }

    public void setRating(short rating) {
        this.rating = rating;
    }

    public short getPersonalRating() {
        return personalRating;
    }

    public void setPersonalRating(short personalRating) {
        this.personalRating = personalRating;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(short movieYear) {
        this.movieYear = movieYear;
    }
    
    
    
}
