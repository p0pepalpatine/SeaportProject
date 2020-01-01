/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seaportproject;

import java.util.Comparator;
import java.util.Scanner;

/**
 *
 * @author elich
 */
public class Thing{
    int index, parent;
    String name;//, type,input;

    public Thing(Scanner sc){
        
       /* if(!sc.hasNext()){
            return;
        }
        while(type == null){
            input = sc.next();
            if(input.equalsIgnoreCase("//")){
                sc.nextLine();
            }else{
            type = input;
        System.out.println(type);
        name = sc.next();
        System.out.println(name);
        index = sc.nextInt();
         parent = sc.nextInt();
            
        }*/
        /*
        if(sc.hasNext()){
            if(sc.next().equalsIgnoreCase("//")){
                sc.nextLine();
            }
                   // sc.nextLine();            
        }*/
        //if(sc.hasNext()) type = sc.next();
        //System.out.println(type);
        if(sc.hasNext()) name = sc.next();
        //System.out.println(name);
        if(sc.hasNext()) index = sc.nextInt();
        if(sc.hasNext()) parent = sc.nextInt();
    }    


    
      
}
