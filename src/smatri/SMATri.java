/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smatri;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

/**
 *
 * @author claire
 */
public class SMATri {
    static int NBANT=20;
    static int NBOBJ=400;
     static int NBTYPEOBJ=2;
       static int I=1;
        static int IVIEW=2;
     static int T=10;
       static double KDrop=0.3;
     static double KPick=0.1;
     static boolean HaveError=false;
    public static void main(String[] args) {
        // TODO code application logic here
        Runtime runtime = Runtime.instance();
        Profile config = new ProfileImpl("localhost", 8888, null);
        config.setParameter("gui", "false");
        AgentContainer mc = runtime.createMainContainer(config);
        AgentController acA;
        AgentController acB;
       

        Environement env = new Environement(50, 50,NBOBJ,NBTYPEOBJ);

        try {
            Object[] param = {env,I,T,KDrop,KPick,HaveError,IVIEW};
            int i = 0;
            while (i < NBANT) {
                acA = mc.createNewAgent("A" + i, AntAgent.class.getName(), param);
                acA.start();
                i++;
            }

        } catch (StaleProxyException ignored) {
            System.err.println(ignored);
        }
    }

}
