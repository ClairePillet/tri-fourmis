package Env;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import jade.core.AID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import smatri.Position;
import Vue.Gui;

/**
 *
 * @author claire
 */
public class Environement {

    private Integer n;
    private Integer m;
  
    volatile Map<AID, Position> agentPosition;
    volatile Map<Position, String> objectPosition;
    private Gui GUI ;
    
    boolean verbose = false;

    public Environement(Integer n, Integer m, Gui GUI) {
       
        objectPosition = new ConcurrentHashMap<Position, String>();
        agentPosition = new ConcurrentHashMap<AID, Position>();
        this.GUI=GUI;
        GUI.setSize(500, 500);
        GUI.setLocation(700, 20);
        GUI.setVisible(true);
        GUI.validate();
        generateObject();
       
    }
    public void generateObject(){
        String obj="A";
        for(int i=0; i<400; i++){
            if(i==200){
                obj="B";
            }
             boolean isFree = false;
            int rx=0;
            int ry=0;
            while(!isFree){
                if(caseIsFreeofObject(new Position(rx, ry))==null && caseIsFreeofAgent(new Position(rx, ry))==null){
                    isFree=true;
                }
            }
            objectPosition.put( new Position(rx, ry),obj);
        }
    }
    
    public synchronized void moveAgent(AID id, Position new_position) {
        agentPosition.remove(id);
        agentPosition.put(id, new_position);
        GUI.moveAgent(agentPosition.values());       
    }
     public synchronized void takeObject(Position new_position) {
        objectPosition.remove(new_position);
        GUI.moveObject(objectPosition.keySet());       
    }
public synchronized void dropObject(Position new_position,String obj) {
        objectPosition.put(new_position,obj);
        GUI.moveObject(objectPosition.keySet());       
    }
   
    public synchronized AID caseIsFreeofAgent(Position new_position) {
        //0 agent
         Iterator i = agentPosition.entrySet().iterator();
      
        while (i.hasNext()) {
            Map.Entry ps = (Map.Entry) i.next();
            Position pos = (Position) ps.getValue();
            if (pos.equals(new_position) == true) {
                
                return (AID)ps.getKey();
            }
        }
        return null;
    }
    public synchronized String caseIsFree(Position new_position) {

         Iterator i = objectPosition.entrySet().iterator();
      
        while (i.hasNext()) {
            Map.Entry ps = (Map.Entry) i.next();
            Position pos = (Position) ps.getKey();
            if (pos.equals(new_position) == true) {
                
                return (String)ps.getValue();
            }
        }
        
        return null;
    }
    
    public Position addAgent(AID aid) {
        boolean isFree = false;
        int rx=0;
        int ry=0;
        while(!isFree){
            if(caseIsFreeofAgent(new Position(rx, ry))==null && caseIsFreeofObject(new Position(rx, ry))==null){
                isFree=true;
            }
        }
        Position p1= new Position(rx,ry);
       
        agentPosition.put(aid, p1);
        GUI.moveAgent(agentPosition.values());
        return p1;
    }

   

 

}
