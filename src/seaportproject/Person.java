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
public class Person extends Thing{
    String skill;
    boolean busyFlag = false;
    Person(Scanner sc){
        super(sc);
        if(sc.hasNext()) skill = sc.next();
    }
    public String toString(){
        return "\n"+this.name;// +" is a " + this.skill ; //will add if more detail is required
    }
}
