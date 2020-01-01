/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seaportproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author elich
 */



public class World{
    
    ArrayList<SeaPort> ports = new ArrayList<>();
    HashMap<Integer, SeaPort> hmp = new HashMap<>();
    HashMap<Integer, Dock> hmd = new HashMap<>();
    HashMap<Integer, Ship> hms = new HashMap<>();
    HashMap<String, Person> hmper = new HashMap<>();
    HashMap<String, Job> hmj = new HashMap<>();
    //Scanner Class
    public World(Scanner sc){
        
    }
    //toString Method
    public String toString(){
        String st = "-----The World-----\nPorts:\n";
        for(int i=0;i<this.ports.size();i++){
            st += this.ports.get(i).name +"\n";
        }
        for(int j=0;j<this.ports.size();j++){
            st += this.ports.get(j).toString();
        }
        return st;
    }
    
    SeaPort getSeaPortByIndex(int index){
        return hmp.get(index);
    }
    
    Dock getDockByIndex(int index){
        return hmd.get(Integer.toString(index));
    }
    Ship getShipByIndex(int index){
        return hms.get(index);
    }
    
    void assignDock(Dock md){
        SeaPort msp = getSeaPortByIndex(md.parent);
        msp.docks.add(md);
    }
    
    void assignShip(Ship ms){        
        for(SeaPort msp : ports){
            for(Dock md : msp.docks){
                if(md.index==ms.parent){
                    md.ship = ms;//ship assigned to a dock.
                    md.ship.dockedFlag = true;
                    msp.ships.add(ms);
                }
                }
            if(msp.index==ms.parent){
                    msp.ships.add(ms);
                    msp.que.add(ms);
            }
        }
        
    }
    
    void assignPerson(Person mp){
        for(SeaPort msp: ports){
            if(mp.parent==msp.index){
                msp.persons.add(mp);
            }
        }
    }
    
    void assignJob(Job mj){//TODO get seaport ship and jobs by index and assign based on index
        for(SeaPort msp: ports){
            for(Ship ms: msp.ships){
                if(ms.index==mj.parent){
                    ms.jobs.add(mj);
                }                
            }
        }
    }
}







