/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seaportproject;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author elich
 */
public class SeaPort extends Thing{
    
    ArrayList<Dock> docks = new ArrayList<>();
    ArrayList<Ship> que = new ArrayList<>();
    ArrayList<Ship> ships = new ArrayList<>();
    ArrayList<Person> persons = new ArrayList<>();
    
    public SeaPort(Scanner sc){
        super(sc);                
    }
    
    @Override
    public String toString(){
        String st = "\n**Port "+this.name+"**"+"\n>Docks:\n";
        for(int i=0;i<this.docks.size();i++){
            st += "\n"+this.docks.get(i).toString();
        }
        st +="\n\n>>Ships at port:\n";
        for(int i=0;i<this.ships.size();i++){
            st += this.ships.get(i).toString();
        }
        st +="\n>>People at the seaport:\n";
        for(int i=0;i<this.persons.size();i++){
            st += this.persons.get(i).toString();
        }
        /*st +="\n>>Ships at dock:\n";
        for(int i=0;i<this.docks.size();i++){ //May implement this later if more detail of where ships are at is required
            st += this.docks.get(i) + "\n";
        }        
        st +="\n>>Ships in que:\n";
        for(int i=0;i<this.que.size();i++){
            st += this.que.get(i).toString();
        }*/
        return st;
    }
}
