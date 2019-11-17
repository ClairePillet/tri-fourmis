/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agent;

import Env.Environement;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import smatri.Position;

/**
 *
 * @author claire
 */
public class AntAgent extends Agent {

    private int i;
    private
    private Position pos;
   private Environement env;

    

    public void setup() {
        Object[] args = getArguments();
        env = (Environement) args[0];
        
        pos = env.addAgent(getAID());
       
        addBehaviour(new Routine());
    }

    public class Routine extends CyclicBehaviour {

        public void action() {
            //check grid is ok

            if (env.gridIsOK() == false) {
                messageGestion();
                aStarWhithCom();
            } else {
                System.out.println("Finish");
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.err.format("IOException : %s%n", e);
            }
        }
        
    }

}
