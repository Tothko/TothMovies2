/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviecollection.be;

import java.util.List;

/**
 *
 * @author Marek
 */
public class MovieFilter
{
    public enum SortType { Title, Category, GlobalRating , ID};
    private String title;
    private short rating;
    private List<Category> categories;
    private SortType order;
    private boolean includeAll;

    public SortType getOrder()
    {
        return order;
    }

    public boolean isIncludeAll()
    {
        return includeAll;
    }

    public void setOrder(SortType order)
    {
        this.order = order;
    }

    public void setIncludeAll(boolean includeAll)
    {
        this.includeAll = includeAll;
    }
    
    
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public void setRating(short rating)
    {
        this.rating = rating;
    }
    
    public void setCategories(List<Category> cats)
    {
        this.categories = cats;
    }
    
    public void setSortType(SortType sort)
    {
        this.order = sort;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public short getRating()
    {
        return rating;
    }
    
    public List<Category> getCategories()
    {
        return categories;
    }
    
    public SortType getSortType()
    {
        return order;
    }
    
}
