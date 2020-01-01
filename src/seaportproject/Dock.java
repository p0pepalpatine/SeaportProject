/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seaportproject;

import java.util.Scanner;

/**
 *
 * @author elich
 */
public class Dock extends Thing{
    //ArrayList<Ship> ships = new ArrayList<>();
    Ship ship;
    boolean dockedShip = false;
    
    Dock(Scanner sc){
        super(sc);
    } 
    
    public String toString(){
        String st=">Dock " + this.name + "<\n" + ">>>The ship at dock: " + ship.name;
        //TODO show what ship is assigned to the dock.
        return st;
    }
}
