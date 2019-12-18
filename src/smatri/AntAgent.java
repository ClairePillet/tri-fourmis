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
import java.security.SecureRandom;
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

    private int i ;
    boolean haveError ;

    private int t ;
    Double kPick ;
    int iView;
    Double kDrop ;
    volatile int count = 0;
    volatile String memory = "";
    private Position pos;
    private Environement env;
    private String objectPick = "";

    public void setup() {
        Object[] args = getArguments();
        env = (Environement) args[0];
        i = (int) args[1];
        haveError = (boolean) args[5];
        iView=(int) args[6];
        t = (int) args[2];
        kPick = (double) args[4];
        kDrop = (double) args[3];
        pos = env.addAgent(getAID());

        addBehaviour(new Routine());
    }

    public class Routine extends CyclicBehaviour {

        public void action() {
            //check grid is ok

            double rand = ThreadLocalRandom.current().nextDouble();
            int moveI = 0;
            String objOn = null;
            while (moveI < i) {
                move();
                objOn = isOnObj();
                addMemory(objOn, count);
                moveI++;
                count++;
            }

            if (!objOn.equals("0")) {

                if (objectPick.isEmpty()) {
                    
                    double f = freq(objOn);
                    if(iView>0){
                        f=freqVision(objOn);
                    }
                    double pPick = Math.pow((kPick / (kPick + f)), 2);
                    if (rand <= pPick) {
                        pick();
                    }
                }

            } else {
                if (!objectPick.isEmpty()) {
                    double f = freq(objectPick);
                     if(iView>0){
                        f=freqVision(objectPick);
                    }
                    double pDrop = Math.pow((f / (kDrop + f)), 2);
                    if (rand <= pDrop) {

                        drop();
                    }
                }
            }

        }

        public void addMemory(String s, int i) {

            if (memory.length() < 10) {

                memory = memory.concat(s);

            } else {
                memory = memory.substring(1);
                memory = memory.concat(s);
            }

        }

        public double freq(String s) {
            char[] cArr = memory.toCharArray();
            double i = 0;
            for (char c : cArr) {
                if (c == s.charAt(0)) {
                    i++;
                }
            }
            int size = t;
            if (cArr.length < t) {
                size = cArr.length;
            }
            if (size == 0) {
                return 0;
            }
            double d = i / (double) size;
            if(haveError){
                   double rand = ThreadLocalRandom.current().nextDouble(-0.4,0.4);
                d+=rand;
            }
            return d;
        }

         public double freqVision(String s) {
            char[] cArr = env.getVis(pos, iView).toCharArray();
            double i = 0;
            for (char c : cArr) {
                if (c == s.charAt(0)) {
                    i++;
                }
            }
            int size = cArr.length;
            
            double d = i / (double) size;
            if(haveError){
                   double rand = ThreadLocalRandom.current().nextDouble(-0.4,0.4);
                d+=rand;
            }
            return d;
        }

        synchronized void move() {
            Position p = pos;
            SecureRandom random = new SecureRandom();

            int r = random.nextInt(3 - 0);

            int r2 = random.nextInt(2 - 0);
            int y = pos.getY();
            int x = pos.getX();
            if (r2 > 0) {
                y = r - 1 + pos.getY();
            } else {
                x = r - 1 + pos.getX();
            }

            p = new Position(Math.max(x, 0), Math.max(y, 0));

            if (!pos.equals(p)) {
                if (env.caseIsFreeofAgent(p) == null) {
                    if (env.moveAgent(getAID(), p, pos, false)) {
                        pos = p;
                    }

                }

            }
        }

        public String isOnObj() {
            String s = env.caseIsFree(pos);
            if (s == null) {
                return "0";
            }
            if (s.isEmpty()) {
                return "0";
            }
            return s;
        }

        public void pick() {
            objectPick = env.takeObject(pos);
        }

        public void drop() {
            env.dropObject(pos, objectPick);
            objectPick = "";
        }
    }

}
