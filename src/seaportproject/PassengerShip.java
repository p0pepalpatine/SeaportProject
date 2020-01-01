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
public class PassengerShip extends Ship{
    int numberOfOccupiedRooms, numberOfPassengers, numberOfRooms;
    PassengerShip(Scanner sc){
        super(sc);
        if(sc.hasNext()) numberOfPassengers = sc.nextInt();
        if(sc.hasNext()) numberOfRooms = sc.nextInt();
        if(sc.hasNext()) numberOfOccupiedRooms = sc.nextInt();
    }
    public String toString(){
        String st = this.name;// + "\n"
               /* +"\n\tWeight: " + this.weight 
                +"\n\tLength: " + this.length
                +"\n\tWidth: " + this.width     // will add to this if there is more detail that will be required.
                +"\n\tDraft: " + this.draft 
                +"\n\tNo. of Occupied Rooms: " + this.numberOfOccupiedRooms 
                +"\n\tNo. of Passengers: " + this.numberOfPassengers 
                +"\n\tNo. of Rooms: " + this.numberOfRooms
                +"\n\tJobs on the ship: "*/;
        /*for(int i=0;i<this.jobs.size();i++){
            for(int j=0;j<this.jobs.get(i).requirements.size();j++){
                st+=this.jobs.get(i).requirements.get(j) + " ";      
            }
        }
        st+="\n";*/
        return st;
    }
}