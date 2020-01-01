/*
 * SeaportProject. 25 Jun 2018
 * Implements GUI with option to search for specific properties from the user.
 * Eli Cheek
 * UMUC CMSC 330
 */
package seaportproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;


public class SeaportProject extends JFrame{

    static final long serialVersionUID = 123L;
    
    JPanel displayPanel = new JPanel();
    public JPanel parentJob = new JPanel();
    
    JTextArea jta = new JTextArea();
    
    JFileChooser filechooser = new JFileChooser(".");
    File selectedFile;
    
    Scanner sc;
    World world;
    Ship ship;
    Job jobP;
    Thread startJobThread; // for HW3 not neccessary for actual program
    
    public JTree createJTree(World world){
        JTree tree;// = new JTree();
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("The World");
                //Parts for the tree
        DefaultMutableTreeNode personHolder;// = new DefaultMutableTreeNode("People");
        DefaultMutableTreeNode que;// = new DefaultMutableTreeNode("Ship Que");
        
        tree = new JTree(top);
        DefaultMutableTreeNode portNode = null;
        DefaultMutableTreeNode dockNode = null;
        DefaultMutableTreeNode shipNode = null;
        DefaultMutableTreeNode personNode = null;
        DefaultMutableTreeNode jobHolder = null;
        DefaultMutableTreeNode jobNode = null;
        
        for(SeaPort port : world.ports){
            portNode = new DefaultMutableTreeNode(port.name);
            personHolder = new DefaultMutableTreeNode("People");
            que = new DefaultMutableTreeNode("Ship Que");
            portNode.add(personHolder);
            portNode.add(que);
            top.add(portNode);
            for(Dock dock : port.docks){
                dockNode = new DefaultMutableTreeNode(dock.name);
                portNode.add(dockNode);
                shipNode = new DefaultMutableTreeNode(dock.ship.name);
                jobHolder = new DefaultMutableTreeNode("Jobs");
                shipNode.add(jobHolder);
                for(Job dJob : dock.ship.jobs){
                    jobNode = new DefaultMutableTreeNode(dJob.name);
                    jobHolder.add(jobNode);
                }
                dockNode.add(shipNode);
            }
            for(Ship pShip : port.que){
                shipNode = new DefaultMutableTreeNode(pShip.name);
                jobHolder = new DefaultMutableTreeNode("Jobs");
                shipNode.add(jobHolder);
                for(Job dJob : pShip.jobs){
                    jobNode = new DefaultMutableTreeNode(dJob.name);
                    jobHolder.add(jobNode);
                }
                que.add(shipNode);
            }
            for(Person person : port.persons){
                personNode = new DefaultMutableTreeNode(person.name);
                personHolder.add(personNode);                
            }
            
        }
                
        return tree;
    }
    
    public SeaportProject(){
        
        setTitle("Sea Port");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(1000,400);
        setVisible(true);
        //setLayout(new BoarderLayout());
        
        //Set up the text area and scroll pane
        JScrollPane scrollPane = new JScrollPane(displayPanel);
        displayPanel.setBackground(Color.white);
        jta.setColumns(ALLBITS);
        displayPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5,0,0,0), "Display Window"));
        Dimension scrollDimension = new Dimension(300,200);
        scrollPane.setMinimumSize(scrollDimension);
        //add(scrollPane,BorderLayout.CENTER);
        
        //Buttons for the main part of the GUI
        //<editor-fold>
        GridBagConstraints spC = new GridBagConstraints();
        spC.fill = GridBagConstraints.HORIZONTAL;
        spC.anchor = GridBagConstraints.PAGE_START;
        spC.gridx = 1;
        spC.insets = new Insets(5,0,0,0);
        spC.gridwidth = 1;
        JButton readButton = new JButton("Read");
        readButton.setToolTipText("Read in a file");
        JButton displayButton = new JButton("Display");
        displayButton.setToolTipText("Display contents of File in the Display Window");
        JButton searchButton = new JButton("Search");
        searchButton.setToolTipText("Bring up Menu with searching options");
        JButton sortButton = new JButton("Sort");
        sortButton.setToolTipText("Bring up menu with sorting options");
        JButton jobsButton = new JButton("Jobs");
        JButton treeButton = new JButton("Tree");
        JButton helpButton = new JButton("Help");
        helpButton.setToolTipText("Displays help in the Display Window");
        //</editor-fold>
        
        //Main GUI panel
        JPanel jp = new JPanel();
        jp.setLayout(new GridBagLayout());
        spC.gridy = 0;
        jp.add(readButton, spC);
        spC.gridy = 1;
        jp.add(displayButton, spC);
        spC.gridy = 2;
        jp.add(searchButton, spC);
        spC.gridy = 3;
        jp.add(sortButton, spC);
        spC.gridy = 4;
        jp.add(helpButton, spC);
        spC.gridy = 5;
        jp.add(jobsButton, spC);
        spC.gridy = 6;
        jp.add(treeButton, spC);
        Dimension startDimension = new Dimension(75,0);
        jp.setSize(startDimension);
        
        //SplitPane set up for intial startup
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, jp);
        splitPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        add(splitPane);
        
        //Panel and buttons that is tacked on when the search is intiated.
        //<editor-fold>
        JPanel searchPanel = new JPanel();
        GridBagConstraints sePC = new GridBagConstraints();
        sePC.fill = GridBagConstraints.HORIZONTAL;
        sePC.anchor = GridBagConstraints.PAGE_START;
        sePC.gridx = 0;
        sePC.insets = new Insets(5,0,0,0);
        sePC.gridwidth = 1;
        searchPanel.setLayout(new GridBagLayout());
        JTextField sTextField = new JTextField();
        //sTextField.setMaximumSize(new Dimension(150,30));
        JButton spanelButton = new JButton("Search");
        JButton scancelButton = new JButton("Cancel");
        
        JRadioButton portRadio = new JRadioButton("Port");
        portRadio.setActionCommand("Port");
        JRadioButton dockRadio = new JRadioButton("Dock");
        JRadioButton shipRadio = new JRadioButton("Ship");
        JRadioButton personRadio = new JRadioButton("Person");
        JRadioButton jobRadio = new JRadioButton("Job");
        
        ButtonGroup spanelGroup = new ButtonGroup();
        spanelGroup.add(portRadio);
        spanelGroup.add(dockRadio);
        spanelGroup.add(shipRadio);
        spanelGroup.add(personRadio);
        spanelGroup.add(jobRadio);
        
        sePC.gridy = 0;
        sePC.gridwidth = 2;
        sePC.ipady = 3;
        searchPanel.add(sTextField, sePC);
        sePC.ipady = 0;
        sePC.gridwidth = 1;
        sePC.gridy = 1;
        searchPanel.add(spanelButton, sePC);
        sePC.gridx = 1;
        searchPanel.add(scancelButton, sePC);
        sePC.gridx = 0;
        sePC.gridy = 2;
        searchPanel.add(portRadio, sePC);
        sePC.gridy = 3;
        searchPanel.add(dockRadio, sePC);
        sePC.gridy = 4;
        searchPanel.add(shipRadio, sePC);
        sePC.gridy = 5;
        searchPanel.add(personRadio, sePC);
        sePC.gridy = 6;
        searchPanel.add(jobRadio, sePC);
        //</editor-fold>
        
        //Panel for the sort features
        //<editor-fold>
        JPanel sortPanel = new JPanel();
        sortPanel.setMaximumSize(new Dimension(50,100));
        sortPanel.validate();
        sortPanel.setBorder(BorderFactory.createTitledBorder("Ship"));
        
        GridBagConstraints soPC = new GridBagConstraints();
        soPC.fill = GridBagConstraints.HORIZONTAL;
        soPC.anchor = GridBagConstraints.PAGE_START;
        soPC.gridx = 0;
        soPC.insets = new Insets(5,0,0,0);
        soPC.gridwidth = 1;
        sortPanel.setLayout(new GridBagLayout());
        JButton spSortButton = new JButton("Sort");
        JButton spCancelButton = new JButton("Cancel");
        ButtonGroup sortGroup = new ButtonGroup();
        JRadioButton nameRadio = new JRadioButton("Name");
        nameRadio.setActionCommand("Name");
        JRadioButton weightRadio = new JRadioButton("Weight");
        weightRadio.setActionCommand("Weight");
        JRadioButton lengthRadio = new JRadioButton("Length");
        lengthRadio.setActionCommand("Length");
        JRadioButton widthRadio = new JRadioButton("Width");
        widthRadio.setActionCommand("Width");
        JRadioButton numJobRadio = new JRadioButton("Num. Jobs");
        numJobRadio.setActionCommand("Jobs");
        sortGroup.add(nameRadio);
        sortGroup.add(weightRadio);
        sortGroup.add(lengthRadio);
        sortGroup.add(widthRadio);
        sortGroup.add(numJobRadio);
        
        soPC.gridy = 0;
        sortPanel.add(nameRadio, soPC);
        soPC.gridy = 1;
        sortPanel.add(weightRadio, soPC);
        soPC.gridy = 2;
        sortPanel.add(lengthRadio, soPC);
        soPC.gridy = 3;
        sortPanel.add(widthRadio, soPC);
        soPC.gridy = 4;
        sortPanel.add(numJobRadio, soPC);
        soPC.gridy = 5;
        sortPanel.add(spSortButton, soPC);
        soPC.gridx = 1;
        sortPanel.add(spCancelButton, soPC);
        //</editor-fold>
                
        readButton.addActionListener((ActionEvent e) -> {
            //<editor-fold>
            displayPanel.removeAll();
            
            int returnValue = filechooser.showOpenDialog(null);
            if(returnValue == JFileChooser.APPROVE_OPTION){
                selectedFile = filechooser.getSelectedFile();
            }
            try {
                sc = new Scanner(selectedFile);
                if(sc.next().equalsIgnoreCase("//")){sc.nextLine();}
                
                world = new World(sc);
                displayPanel.removeAll();
                jta.setText("");        
                jta.append("File Read");
                displayPanel.add(jta);
                if(!sc.hasNext()){
                    return;
                }
        while(sc.hasNext()){
            switch(sc.next()){
                case "//":
                    sc.nextLine();
                    break;
                case "port":
                    SeaPort seaport = new SeaPort(sc);
                    world.ports.add(seaport);
                    world.hmp.put(seaport.index, seaport);
                    //type = null;
                    break;
                case "dock":
                    Dock dock = new Dock(sc);
                    world.assignDock(dock);
                    world.getDockByIndex(dock.index);
                    world.hmd.put(dock.index, dock);
                    break;
                case "cship":
                    CargoShip cargoShip = new CargoShip(sc);
                    world.assignShip(cargoShip);
                    world.hms.put(cargoShip.index, cargoShip);
                    break;
                case "pship":
                    PassengerShip passengerShip = new PassengerShip(sc);
                    world.assignShip(passengerShip);
                    world.hms.put(passengerShip.index, passengerShip);
                    break;
                case "person":
                    Person person = new Person(sc);
                    world.assignPerson(person);
                    world.hmper.put(Integer.toString(person.index), person);
                    break;
                case "job":
                    Scanner jobLine = new Scanner(sc.nextLine());
                    Job job = new Job(world.hms, world.hmp, world.hmd, jobLine); 
                    
                    world.assignJob(job);
                    world.hmj.put(Integer.toString(job.index), job);                    
                    break;
                default:
                    break;
            }
        }
                validate();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SeaportProject.class.getName()).log(Level.SEVERE, null, ex);
                displayPanel.removeAll();
                jta.setText("");        
                jta.append("File not read");
                displayPanel.add(jta);
                validate();
            }            
        }//</editor-fold>
        );
        displayButton.addActionListener((ActionEvent e) -> {
            displayPanel.removeAll();
            jta.setText("");        
            jta.append(world.toString());
            displayPanel.add(jta);
        validate();
        });
        
        jobsButton.addActionListener((ActionEvent e) -> {
            
            splitPane.remove(splitPane.getLeftComponent());
            JPanel jpJobs = new JPanel();
            jpJobs.setMinimumSize(new Dimension(50,0));
            JPanel shipJob = null;// = new JPanel();
            
            jpJobs.setLayout(new java.awt.GridLayout(0,1));
            //jpJobs.add(new JLabel("The Jobs"));
            if(world== null) return;
            for(SeaPort p: world.ports)
                for(Ship s: p.ships){
                    shipJob = new JPanel();
                    shipJob.setLayout(new java.awt.GridLayout(0,1));
                    shipJob.setBorder(BorderFactory.createTitledBorder(s.name));
                    for (Job j: s.jobs){
                        shipJob.add(j.jobPanel);
                        jpJobs.add(shipJob); 
                        //shipJob.add(jpJobs);
                        validate();
                    }
                    
                        //(new Thread(j)).start();}
                }
            splitPane.setLeftComponent(new JScrollPane(jpJobs));
            for(SeaPort po: world.ports)
                for(Dock d: po.docks)
                    for(Job jo: d.ship.jobs){
                        startJobThread = new Thread(jo);
                        startJobThread.setName(jo.name);
                        startJobThread.start();
                    }
            
            validate();
        });
        treeButton.addActionListener((ActionEvent e) -> {
            splitPane.remove(splitPane.getLeftComponent());
            JPanel treeDisplay = new JPanel();
            treeDisplay.add(createJTree(world));
            treeDisplay.setLayout(new java.awt.GridLayout(0,1));
            splitPane.setLeftComponent(new JScrollPane(treeDisplay));
           validate();
        });
        searchButton.addActionListener((ActionEvent e) -> {
           remove(jp); 
           splitPane.remove(jp);           
           splitPane.add(searchPanel);
           validate();
        });
        
        scancelButton.addActionListener((ActionEvent e) -> {
           remove(searchPanel); 
           splitPane.remove(searchPanel);
           splitPane.add(jp);
           validate();
        });
        spanelButton.addActionListener((ActionEvent e) -> {
            //<editor-fold>
           String search = sTextField.getText();
           String result = "";
           Boolean match = false;
           String found = "Result Found:";
           String notFound = "No Results found";
           jta.setText("");
           if(portRadio.isSelected()){
               for(SeaPort sp : world.ports){
                   if(search.equalsIgnoreCase(sp.name)){
                       match = true;
                       result +="\n" +sp.name;
                   }
                   if(search.equalsIgnoreCase(Integer.toString(sp.index))){
                       match = true;
                       result +="\n" +sp.name;
                   }
               }
               if(match){
                   jta.append(found +result);
                   displayPanel.add(jta);
                   validate();
               }else{
                       jta.append(notFound+"\n"+search);
                       displayPanel.add(jta);
                       validate();
                   }
           }else if(dockRadio.isSelected()){
               for(SeaPort msp : world.ports){
                    for(Dock md : msp.docks){
                        if(search.equalsIgnoreCase(md.name)){
                            match = true;
                            result +="\n"+md.name;
                        }
                        if(search.equalsIgnoreCase(Integer.toString(md.index))){
                            match = true;
                            result +="\n" +md.name;
                        }
                    }
                }
               if(match){
                   jta.append(found +result);
                   displayPanel.add(jta);
                   validate();
               }else{
                       jta.append(notFound+"\n"+search);
                       displayPanel.add(jta);
                       validate();
                   }
           }else if(shipRadio.isSelected()){
               for(SeaPort msp : world.ports){
                    for(Ship ms : msp.ships){
                        if(search.equalsIgnoreCase(ms.name)){
                            match = true;
                            result +="\n"+ms.name;
                        }
                        if(search.equalsIgnoreCase(Integer.toString(ms.index))){
                            match = true;
                            result +="\n" +ms.name;
                        }
                    }
                }
               if(match){
                   jta.append(found +result);
                   displayPanel.add(jta);
                   validate();
               }else{
                       jta.append(notFound+"\n"+search);
                       displayPanel.add(jta);
                       validate();
                   }
           }else if(personRadio.isSelected()){
               for(SeaPort msp : world.ports){
                    for(Person mp : msp.persons){
                        if(search.equalsIgnoreCase(mp.name)){
                            match = true;
                            result +="\n"+mp.name;
                        }
                        if(search.equalsIgnoreCase(Integer.toString(mp.index))){
                            match = true;
                            result +="\n" +mp.name;
                        }
                    }
                }
               if(match){
                   jta.append(found +result);
                   displayPanel.add(jta);
                   validate();
               }else{
                       jta.append(notFound+"\n"+search);
                       displayPanel.add(jta);
                       validate();
                   }
           }else if(jobRadio.isSelected()){
               for(SeaPort msp : world.ports){
                    for(Ship ms : msp.ships){
                        for(Job mj : ms.jobs){
                            if(search.equalsIgnoreCase(mj.name)){
                                match = true;
                                result +="\n"+mj.name;
                            }
                            if(search.equalsIgnoreCase(Integer.toString(mj.index))){
                                match = true;
                                result +="\n" +ms.name;
                            }
                        }
                    }
                }
               if(match){
                   jta.append(found +result);
                   displayPanel.add(jta);
                   validate();
               }else{
                       jta.append(notFound+"\n"+search);
                       displayPanel.add(jta);
                       validate();
                   }
           }else{
               System.out.println("make selection");
           }
           pack();
           validate();
           //</editor-fold>
        });
        sortButton.addActionListener((ActionEvent e) -> {
            remove(jp); 
            splitPane.remove(jp);
            splitPane.add(sortPanel);
            validate();
        });
        spCancelButton.addActionListener((ActionEvent e) -> {
           remove(sortPanel); 
           splitPane.remove(sortPanel);
           splitPane.add(jp);
           validate();
        });
        spSortButton.addActionListener((ActionEvent e) -> {
            //<editor-fold>
            //String newSorted;
            jta.setText("");
            String sortedSet ="";
            String appendedString = "***********NEW SORTED WORLD***********\n";
            switch(sortGroup.getSelection().getActionCommand()){
                case "Name":
                    for(SeaPort msp : world.ports){
                        Collections.sort(msp.ships, new shipNameCompator());
                        Iterator itr = msp.ships.iterator();                        
                        String shipsSorted = "\n"+"------------Details of sorted ships------------";
                        while(itr.hasNext()){
                            Ship s=(Ship)itr.next();            
                        }
                        
                    }
                    jta.setText(appendedString+world.toString());
                    break;
                case "Weight":
                    for(SeaPort msp : world.ports){
                        Collections.sort(msp.ships, new shipWeightCompator());
                        Iterator itr = msp.ships.iterator();
                        String shipsSorted = "\n" + "------------Details of sorted ships------------";
                        while(itr.hasNext()){
                            Ship s=(Ship)itr.next();                
                            shipsSorted += "\n"+s.name+"\t"+s.weight;
                        }
                        sortedSet += shipsSorted;
                    }
                    jta.setText(appendedString+world.toString() +"\n"+sortedSet);
                    break;
                case "Length":
                    for(SeaPort msp : world.ports){
                        Collections.sort(msp.ships, new shipLengthCompator());
                        Iterator itr = msp.ships.iterator();
                        String shipsSorted = "\n" + "------------Details of sorted ships------------";
                        while(itr.hasNext()){
                            Ship s=(Ship)itr.next();                
                            shipsSorted += "\n"+s.name+"\t"+s.length;
                        }
                        sortedSet += shipsSorted;
                    }
                    jta.setText(appendedString+world.toString() +"\n"+sortedSet);
                    break;
                case "Width":
                    for(SeaPort msp : world.ports){
                        Collections.sort(msp.ships, new shipWidthCompator());
                        Iterator itr = msp.ships.iterator();
                        String shipsSorted = "\n" + "------------Details of sorted ships------------";
                        while(itr.hasNext()){
                            Ship s=(Ship)itr.next();                
                            shipsSorted += "\n"+s.name+"\t"+s.width;
                        }
                        sortedSet += shipsSorted;
                    }
                    jta.setText(appendedString+world.toString() +"\n"+sortedSet);
                    break;
                case "Jobs":
                    for(SeaPort msp : world.ports){
                        Collections.sort(msp.ships, new shipNumJobsCompator());
                        Iterator itr = msp.ships.iterator();
                        String shipsSorted = "\n" + "------------Details of sorted ships------------";
                        while(itr.hasNext()){
                            Ship s=(Ship)itr.next();                
                            shipsSorted += "\n"+s.name+"\t"+s.jobs.size();
                        }
                        sortedSet += shipsSorted;
                    }
                    jta.setText(appendedString+world.toString() +"\n"+sortedSet);
                    break;
                default:
                    break;
            }
            //</editor-fold>
        });
        helpButton.addActionListener((ActionEvent e) -> {
            displayPanel.removeAll();
            String help;
            help ="This program is designed to allow for the display of a SeaPort that shows \n"
                    + " the various ports, dock, ships, and people there. To start you will want \n"
                    + " to press the Read button located to the left. If file has been correctly \n"
                    + " read, then the Display window will show 'File Read'. After a file has been \n"
                    + " read, then you can press the 'Display' button which will allow for the \n "
                    + "world with how all of these elements are related. The last two options are \n"
                    + " sort and search. Search allows for you to search for the names of different \n"
                    + " elements in the of the world. Sort allow for you sort ships by various \n "
                    + "elements. The world will be displayed again, and the new sorted elements \n "
                    + "with their details will be displayed.";
            jta.setText("");        
            jta.append(help);
            displayPanel.add(jta);
        validate();
        });
        
        validate();
    }
    
    public static void main(String[] args) {
        SeaportProject sp = new SeaportProject();
    }
    
}
