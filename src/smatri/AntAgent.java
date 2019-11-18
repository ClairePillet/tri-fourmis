/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smatri;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.Timestamp;
/**
 *
 * @author claire
 */
public class AntAgent extends Agent {

    private int i = 1;
       private int count = 0;
    private String memory="";
    private int t = 10;
    Double kPick = 0.1;
    Double kDrop = 0.3;
    private Position pos;
    private Environement env;
    private String objectPick = "";

    public void setup() {
        Object[] args = getArguments();
        env = (Environement) args[0];
        
        pos = env.addAgent(getAID());

        addBehaviour(new Routine());
    }

    public class Routine extends CyclicBehaviour {

        public void action() {
            //check grid is ok
            
            double rand = ThreadLocalRandom.current().nextDouble();
            System.err.println(getName()+ " "+pos);
            move();
            String obj = isOnObj();
            addMemory(obj,count);
            if (obj != null) {
                
                if (objectPick.isEmpty()) {
                    double f = freq(obj);
                    double pPick = Math.pow((kPick / (kPick + f)), 2.);
                    if (rand >= pPick) {
                        pick();
                    }

                }

            } else {
                if (!objectPick.isEmpty()) {
                    double f = freq(objectPick);
                    double pDrop = Math.pow((f / (kDrop + f)), 2.);
                    if (rand >= pDrop) {
                        drop();
                    }
                }
            }
            count++;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.err.format("IOException : %s%n", e);
            }
        }
        public void addMemory(String s, int i){
            char[] cArr = memory.toCharArray();
            char c;
            if(s==null){
                c='0';
            }else{
                c=s.charAt(0);
            }
            if(cArr.length<10){
               String ss=String.valueOf(c);
               memory= memory.concat( ss);
            }else{
                 cArr[i%t]=c;
            }
           
        }
        public double freq(String s) {
            char[] cArr = memory.toCharArray();
            int i = 0;
            for (char c : cArr) {
                if (c == s.charAt(0)) {
                    i++;
                }
            }
            int size=t;
            if(cArr.length<t){
                size=cArr.length;
            }
            if(size==0){
                return 0;
            }
            return i / size;
        }

        public void move() {
            Position p=pos;
                Timestamp t =new Timestamp(System.currentTimeMillis());
              Random randOne = ThreadLocalRandom.current().setSeed((new Timestamp(System.currentTimeMillis())));
              Random randtwo = ThreadLocalRandom.current().setSeed((new Timestamp(System.currentTimeMillis())));
            int dir =  randOne.nextInt(2 - 0);
            int minusORPlus = randtwo.nextInt(1 - 0);

            switch (dir) {
                case 0:// droite
                    if (minusORPlus == 0) {//-
                        p = new Position(Math.max(pos.getX() - i,0), pos.getY());
                    } else {
                        p = new Position(pos.getX() + i, pos.getY());
                    }
                    break;
                case 1:// gauche
                    if (minusORPlus == 0) {//-
                        p = new Position(pos.getX(), Math.max(pos.getY() - i,0));
                    } else {
                        p = new Position(pos.getX(), pos.getY() + i);
                    }
                    break;
                case 2:// gauche
                    if (minusORPlus == 0) {//-
                        p = new Position(Math.max(pos.getX() - i,0), Math.max(pos.getY() - i,0));
                    } else {
                        p = new Position(pos.getX() + i, pos.getY() + i);
                    }
                    break;

            }
            if(!pos.equals(p)){
                env.moveAgent(getAID(), p);
                pos=p;
            }
        }

        public String isOnObj() {
            String s = env.caseIsFree(pos);
            if (s == null) {
                return null;
            }
            if (s.isEmpty()) {
                return null;
            }
            return s;
        }

        public void pick() {
            objectPick = env.takeObject(pos);
        }

        public void drop() {
            env.dropObject(pos, objectPick);
            objectPick="";
        }
    }

}
