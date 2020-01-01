/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seaportproject;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/**
 *
 * @author elich
 */
public class Job extends Thing implements Runnable{
   double duration;
  
   JPanel jobPanel;
   SeaPort port = null;
   Ship parentShip;
   Ship lastShip;
   Dock parentDock;
   String catchWrongPort = "";
   long jobTime = 1000;
   String jobName = "";
   String requirementLabel = "The Requirements: ";
   String workerLabel = "The workers are: ";
   String reqInfo = "";
   String workersInfo = "";
   JProgressBar progBar = new JProgressBar();
   boolean goFlag = true, noKillFlag = true, noPeople = false;
   JButton jbGo = new JButton("Stop");
   JButton jbKill = new JButton ("Cancel");
   Status status = Status.SUSPENDED;
   Location location = Location.QUE;
   Thread newJob;
   int peopleWithSkill = 0;
   
   JLabel workerStats;
   
   enum Status {RUNNING, SUSPENDED, WAITING, DONE};
   enum Location{QUE,FINISHED,DOCKED};
   
   GridBagConstraints c = new GridBagConstraints();
   
   ArrayList<String> requirements = new ArrayList<>();
   
   ArrayList<Person> skilledPeopleToRemove = new ArrayList<>();
   
   Job(HashMap <Integer, Ship> hmShip, HashMap <Integer, SeaPort> hmPort, HashMap<Integer, Dock> hmDock, Scanner sc){
       super(sc);
       
       jobPanel = new JPanel();
       jobPanel.setLayout(new GridBagLayout());
       parentShip= hmShip.get(parent);
       
       
       
       //This try block is to attempt to see if the port is correct. If the index 
       //fed in is actually a dock it will realize that and try again from the dock
       try{
           port = hmPort.get(parentShip.parent);
           catchWrongPort = port.name;
       }catch(NullPointerException e){
           parentDock = hmDock.get(parentShip.parent);
    //       showLocation(Location.DOCKED);
           parentDock.dockedShip = true;
           port = hmPort.get(hmDock.get(parentShip.parent).parent);
       }
       if(sc.hasNext()) duration = sc.nextDouble();
       while(sc.hasNext()) requirements.add(sc.next());
    
    
       progBar = new JProgressBar();
       progBar.setStringPainted(true);
       c.fill = GridBagConstraints.HORIZONTAL;
       c.gridx = 0;
       c.gridy = 0;
       c.ipadx = 5;
       c.ipady = 1;
       //c.insets = new Insets(5,5,5,5);
       jobPanel.add(progBar, c);
       c.gridx = 1;
       jobPanel.add(new JLabel(this.name), c);
       c.gridx = 2;
       jobPanel.add(new JLabel(jobName    , SwingConstants.CENTER),c);
       
       JLabel reqLabel = new JLabel(requirementLabel/*, SwingConstants.TRAILING*/);
       JLabel peopLabel = new JLabel(workerLabel/*, SwingConstants.TRAILING*/);
       c.gridy = 1;
       c.gridx = 0;
       jobPanel.add(reqLabel,c);
       c.gridy = 2;
       c.gridx = 0;
       jobPanel.add(peopLabel,c);
       c.gridx = 3;
       c.gridy = 0;
       jobPanel.add(jbGo,c);
       c.gridx = 4;
       jobPanel.add(jbKill,c);  
       
       jbGo.addActionListener((ActionEvent e) -> {
            toggleGoFlag();
        });
       jbKill.addActionListener((ActionEvent e) -> {
            setKillFlag();
        });
   }
   public void toggleGoFlag(){
       goFlag = !goFlag;
   }
   public void setKillFlag(){
       noKillFlag = false;
       jbKill.setBackground(Color.red);
   }
   void showStatus(Status st){
       status = st;
       switch (status){
           case RUNNING:
               jbGo.setBackground(Color.green);
               jbGo.setText("Running");
               break;
           case SUSPENDED:
               jbGo.setBackground(Color.yellow);
               jbGo.setText("Suspended");
               break;
           case WAITING:
               jbGo.setBackground(Color.orange);
               jbGo.setText("Waiting turn");
               break;
           case DONE:
               jbGo.setBackground(Color.red);
               jbGo.setText("Done");
               break;
       }
   }
  /* void showLocation(Location local){
       location = local;
       switch (location){
           case QUE: 
               jobPanel.setBackground(Color.YELLOW);
               break;
           case DOCKED:
               jobPanel.setBackground(Color.cyan);
               break;
           case FINISHED:
               jobPanel.setBackground(Color.red);
               break;
       }
   }*/
   public String toString(){
       String st =this.name + " Job will take "
               + Double.toString(this.duration)
               + " hours to complete. It's requirements are:";
       for(int i=0;i<this.requirements.size();i++){
           String next = this.requirements.get(i);
           st += " " + next;
       }
       return st;       
   }
   Ship getShipQued(){
       Ship holderShip = null;
       synchronized(port){
           if(port.que.isEmpty()){
               holderShip = null;
           }else{
               holderShip = port.que.get(0);
               port.que.remove(0);
           }
       }
       return holderShip;
   }
   Dock getDock(int value){
       Dock foundDock = null;
       for(Dock dock : port.docks){
           if(value == dock.ship.index){
               foundDock = dock;
           }
       }
       return foundDock;
   }
   
   @Override
   public void run(){
       //Establish a time line for the progressbar
       int startTime = 1;
       int stopTime = (int)duration;
       //Tell the parentship that it is busy and that the threads shouldn't run(Should the busy flag be at the dock?)
       //Check to see if tasks can be completed with avaiable sources at the port, if not the thread should complete
       synchronized (port){ 
        peopleWithSkill = 0;           
            for(String requirement : requirements){
                for(Person worker : port.persons){
                    if(worker.skill.equals(requirement)){
                        peopleWithSkill++;
                        break;//should leave once a single worker is found
                    }
                }
                reqInfo += requirement + " ";
            }
            if(peopleWithSkill < requirements.size() && !requirements.isEmpty()){//ignores jobs that don't have requirements
                noPeople = true;
                setKillFlag();
                jbKill.setBackground(Color.black);
            }
            while(parentShip.busyFlag){
                showStatus(Status.WAITING);
                    try{
                    port.wait();
                    //showLocation(Location.DOCKED);
                    }
                    catch (InterruptedException e){}
            }
            c.gridx = 2;
       c.gridy = 1;
       c.gridwidth = 3;
       jobPanel.add(new JLabel(reqInfo),c);
       }  
       
            
       parentShip.busyFlag = true;
       //Thread should grab people at the port and start completing their jobs (Shouldn't hold for an extended period of time)
       //SOmething is off and may require a different sync to add people maybe check the people with skill and add based off of that
       synchronized(port){
           for(String requirement : requirements){
                for(Person worker : port.persons){
                    if(worker.skill.equals(requirement)){
                        workersInfo += worker.name + " " + worker.skill + ". ";
                        parentShip.workers.add(worker);//people added to arraylist in order to arraylist that the job will have
                        port.persons.remove(worker);//Takes the people away from the port
                        break;//should leave once a single worker is found
                    }
                }
            }
           c.gridy = 2;
       c.gridx = 1;
       jobPanel.add(new JLabel(workersInfo),c);
       }    
       
       
       //If resources are being used and it cannot complete then should leave port return resources and go into que
       synchronized(port){
           if(parentShip.workers.size() != requirements.size() && !parentShip.workers.isEmpty()){
               for(Person worker : parentShip.workers){
                   //add people back to the port
                   port.persons.add(worker);
                   //remove skilled people from boat
                   skilledPeopleToRemove.add(worker);
                   //parentShip.workers.remove(worker);
               }
               parentShip.workers.removeAll(skilledPeopleToRemove);
               skilledPeopleToRemove.clear();
               port.notifyAll();
           }
           
         
       }
       while (startTime < stopTime && noKillFlag){
          // Long time = System.currentTimeMillis();
            try{
                Thread.sleep(100);
            }
            catch (InterruptedException e){}
            if (goFlag){
                showStatus (Status.RUNNING);
                startTime += 1;
                progBar.setValue((int)((startTime*100)/stopTime));
                progBar.repaint();
            }else{
                showStatus(Status.SUSPENDED);
            }
        }
       //Job is completed at this point
       progBar.setValue(100);
       showStatus (Status.DONE);
//       showLocation(Location.FINISHED);
       parentShip.jobIndex++;
       if(parentShip.jobIndex == parentShip.jobs.size()){
           getDock(parentShip.index).dockedShip = false;
       }
       
       //If all task are completed it should leave port then add a ship and start its jobs
       synchronized (port){           
            if(!parentShip.workers.isEmpty()){
                for(Person worker : parentShip.workers){
                   //add people back to the port
                   port.persons.add(worker);
                   skilledPeopleToRemove.add(worker);
                }
                parentShip.workers.removeAll(skilledPeopleToRemove);
                skilledPeopleToRemove.clear();
            }
           parentShip.busyFlag = false;
           port.notifyAll();
           if(port.que.isEmpty()){
               return;
           }else{
               for(Dock emptyDock : port.docks){
                    if(!emptyDock.dockedShip){
                        emptyDock.ship = getShipQued();
                        emptyDock.dockedShip = true;
                        for(Job jo : emptyDock.ship.jobs){
                            newJob = new Thread(jo);
                            newJob.setName(jo.name);
                            newJob.start();
                        }
                    }
                }
           }
           
       }
   }

}
