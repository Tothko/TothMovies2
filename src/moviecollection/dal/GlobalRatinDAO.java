/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviecollection.dal;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.net.MalformedURLException;

import java.net.URL;

/**
 *
 * @author Tothko
 */
public class GlobalRatinDAO {

    public static void main(String[] args) {

        try {

            URL url = new URL("https://www.imdb.com/title/tt1270797/?ref_=fn_al_tt_1");

            // read text returned by server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;

            while ((line = in.readLine()) != null) {

                System.out.println(line);

            }

            in.close();

        } catch (MalformedURLException e) {

            System.out.println("Malformed URL: " + e.getMessage());

        } catch (IOException e) {

            System.out.println("I/O Error: " + e.getMessage());

        }

    }

}


