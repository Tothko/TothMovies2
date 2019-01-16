/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviecollection.dal;

import moviecollection.MovieCollectionException;

/**
 *
 * @author Marek
 */
public class DataLayerException extends MovieCollectionException
{

    public DataLayerException(String message)
    {
        super(message);
    }
    
}
