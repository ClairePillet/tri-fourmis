package smatri;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import jade.core.AID;
import static java.lang.Math.max;

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
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import smatri.Position;
import smatri.Gui;

/**
 *
 * @author claire
 */
public class Environement {

    private Integer n;
    private Integer m;
  private Integer nbObj;
    private Integer nbTypeObj;
    volatile Map<AID, Position> agentPosition;
    volatile Map<Position, String> objectPosition;
    private Gui GUI;
    static int countDrop=0;
    boolean verbose = false;

    public Environement(int n, int m, int nbObj, int nbTypeObj) {
        this.n = n;
        this.m = m;
        this.nbObj=nbObj;
         this.nbTypeObj=nbTypeObj;
        objectPosition = new ConcurrentHashMap<Position, String>();
        agentPosition = new ConcurrentHashMap<AID, Position>();

        
        this.GUI = new Gui(n, m, objectPosition);
        GUI.setSize(n * 10, m * 10);
        GUI.setLocation(700, 20);
        GUI.setVisible(true);
        GUI.validate();
        generateObject();
    }

    public void generateObject() {
        char obj = 'A';
        for (int i = 0; i < nbObj; i++) {
            if (i % (nbObj/nbTypeObj)==0) {
                obj ++;
                System.err.println(i+" "+ obj);
            }
            boolean isFree = false;
            Random rand = new Random();
            int rx = 0, ry = 0;
            while (!isFree) {
                rx = rand.nextInt(n - 0);
                ry = rand.nextInt(m - 0);
                if (caseIsFree(new Position(rx, ry)) == null) {
                    isFree = true;
                }

            }
            objectPosition.put(new Position(rx, ry), String.valueOf(obj));
        }
        update();
    }
    public synchronized void update(){
        GUI.moveAgent(agentPosition.values());
        GUI.moveObject(objectPosition);
    }
    public synchronized boolean moveAgent(AID id, Position new_position, Position oldP, boolean back) {

        if (new_position.getY() > m) {
            new_position.setY(new_position.getY() - 1);
        }
        if (new_position.getX() > m) {
            new_position.setX(new_position.getX() - 1);
        }
        if (caseIsFreeofAgent(new_position) != null) {
            return false;
        }
        agentPosition.remove(id);
        agentPosition.put(id, new_position);

        GUI.moveAgent(agentPosition.values());
        return true;
    }
    public synchronized String getVis(Position p, int champs){
        String s="";
        for(int i=p.getX()-champs ; i<(champs+p.getX()); i++){
             for(int j=p.getY()-champs ; j<(champs+p.getY()); j++){
                 String v = caseIsFree(new Position(i,j));
                 if(v==null){
                     s=s.concat("0");
                 }else{
                     s=s.concat(v); 
                 }
            }
        }
        return s;
    }
    public synchronized String takeObject(Position new_position) {
        String o = objectPosition.get(new_position);
        if (o != null) {
            objectPosition.remove(new_position);
            GUI.moveObject(objectPosition);
            return o;
        }

        return "";
    }

    public synchronized void dropObject(Position new_position, String obj) {
        objectPosition.put(new_position, obj);                
        GUI.moveObject(objectPosition);
    }

    public synchronized AID caseIsFreeofAgent(Position new_position) {
        //0 agent
        Iterator i = agentPosition.entrySet().iterator();

        while (i.hasNext()) {
            Map.Entry ps = (Map.Entry) i.next();
            Position pos = (Position) ps.getValue();
            if (pos.equals(new_position) == true) {

                return (AID) ps.getKey();
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

                return (String) ps.getValue();
            }
        }

        return null;
    }

    public Position addAgent(AID aid) {
        boolean isFree = false;
        int rx = 0, ry = 0;
        Random rand = new Random();
        while (!isFree) {
            rx = rand.nextInt(n - 0);
            ry = rand.nextInt(m - 0);
            if (caseIsFreeofAgent(new Position(rx, ry)) == null && caseIsFree(new Position(rx, ry)) == null) {
                isFree = true;
            }
        }
        Position p1 = new Position(rx, ry);

        agentPosition.put(aid, p1);
        //GUI.moveAgent(agentPosition.values());
        return p1;
    }

}
