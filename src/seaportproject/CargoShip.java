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
public class CargoShip extends Ship{
    double cargoValue, cargoVolume, cargoWeight;
    //boolean busyFlag;
    CargoShip(Scanner sc){        
        super(sc);
        if(sc.hasNext()) cargoWeight = sc.nextDouble();
        if(sc.hasNext()) cargoVolume = sc.nextDouble();
        if(sc.hasNext()) cargoValue = sc.nextDouble();
    }  
    public String toString(){
        String st =  this.name;// +"\n";
                /*+"\n\tWeight: " + this.weight 
                +"\n\tLength: " + this.length
                +"\n\tWidth: " + this.width //will add this if more detail is required
                +"\n\tDraft: " + this.draft 
                +"\n\tCargo Value: "  + this.cargoValue
                +"\n\tCargo Volume:" + this.cargoVolume 
                +"\n\tCargo Weight: " + this.cargoWeight                 
                +"\n\tJobs on the ship: ";*/
        /*for(int i=0;i<this.jobs.size();i++){
            for(int j=0;j<this.jobs.get(i).requirements.size();j++){
                st+=this.jobs.get(i).requirements.get(j) + " ";      
            }
        }*/
        st+="\n";
        return st;
    }
}
