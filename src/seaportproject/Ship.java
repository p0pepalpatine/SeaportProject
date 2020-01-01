/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seaportproject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

/**
 *
 * @author elich
 */
public class Ship extends Thing{
    double draft,length,weight,width;
    public boolean busyFlag = false;
    public boolean dockedFlag = false;
    public int jobIndex = 0;
    public int jobReqIndex = 0;
    public int peopleThatCanDoTheJob = 0;
    ArrayList<Person> workers = new ArrayList<>();
    ArrayList<Job> jobs = new ArrayList<>();
    
    
    
    Ship(Scanner sc){
        super(sc);
        if(sc.hasNext()) weight = sc.nextDouble();
        if(sc.hasNext()) length = sc.nextDouble();
        if(sc.hasNext()) width = sc.nextDouble();
        if(sc.hasNext()) draft = sc.nextDouble();        
    }
    
    
    public String toString(){
        String st = "Weight: " + this.weight + "\nLength: " + this.length
                +"\nWidth: " + this.width + "\nDraft: " + this.draft 
                +"\n";
        return st;
    }
        
}    
class shipNameCompator implements Comparator<Thing>{

    @Override
    public int compare(Thing  o1, Thing o2) {
        Ship ship1 = (Ship)o1;
        Ship ship2 = (Ship)o2;
        
        return ship1.name.compareTo(ship2.name);
    }
    
}
class shipWeightCompator implements Comparator<Thing>{

    @Override
    public int compare(Thing  o1, Thing o2) {
        Ship ship1 = (Ship)o1;
        Ship ship2 = (Ship)o2;
        if(ship1.weight==ship2.weight)
            return 0;
        else if(ship1.weight>ship2.weight)
            return 1;
        else
            return -1;
    }
    
}
class shipLengthCompator implements Comparator<Thing>{

    @Override
    public int compare(Thing  o1, Thing o2) {
        Ship ship1 = (Ship)o1;
        Ship ship2 = (Ship)o2;
        if(ship1.length==ship2.length)
            return 0;
        else if(ship1.length>ship2.length)
            return 1;
        else
            return -1;
    }
    
}
class shipWidthCompator implements Comparator<Thing>{

    @Override
    public int compare(Thing  o1, Thing o2) {
        Ship ship1 = (Ship)o1;
        Ship ship2 = (Ship)o2;
        if(ship1.width==ship2.width)
            return 0;
        else if(ship1.width>ship2.width)
            return 1;
        else
            return -1;
    }
    
}
class shipNumJobsCompator implements Comparator<Thing>{

    @Override
    public int compare(Thing  o1, Thing o2) {
        Ship ship1 = (Ship)o1;
        Ship ship2 = (Ship)o2;
        if(ship1.jobs.size()==ship2.jobs.size())
            return 0;
        else if(ship1.jobs.size()>ship2.jobs.size())
            return 1;
        else
            return -1;
    }
    
}
