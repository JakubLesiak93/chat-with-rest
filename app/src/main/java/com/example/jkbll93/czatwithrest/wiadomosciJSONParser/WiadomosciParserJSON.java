/*
	autor: Jakub Lesiak
*/
package com.example.jkbll93.czatwithrest.wiadomosciJSONParser;

import com.example.jkbll93.czatwithrest.wiadomosci.Wiadomosci;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WiadomosciParserJSON {
   
   public static List<Wiadomosci> parserJSON(String content){
   
      try {
         JSONArray ar = new JSONArray(content);
         List<Wiadomosci> listaWiadomosci = new ArrayList<>();
         
         for (int i = 0; i < ar.length(); i++) {
            JSONObject obj = ar.getJSONObject(i);
            
            Wiadomosci wiadomosci = new Wiadomosci();
            wiadomosci.setNazwa(obj.getString("nazwa"));
            wiadomosci.setTresc(obj.getString("tresc"));

            listaWiadomosci.add(wiadomosci);
         }
         
         return listaWiadomosci;
      
      }catch (JSONException e){
         e.printStackTrace();
         return null;
      }
   }
}
