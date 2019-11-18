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

    public static void main(String[] args) {
        // TODO code application logic here
        Runtime runtime = Runtime.instance();
        Profile config = new ProfileImpl("localhost", 8888, null);
        config.setParameter("gui", "true");
        AgentContainer mc = runtime.createMainContainer(config);
        AgentController acA;
        AgentController acB;
       

        Environement env = new Environement(2, 2);

        try {
            Object[] param = {env};
            int i = 0;
            while (i < 2) {
                acA = mc.createNewAgent("A" + i, AntAgent.class.getName(), param);
                acA.start();
                i++;
            }

        } catch (StaleProxyException ignored) {
            System.err.println(ignored);
        }
    }

}
