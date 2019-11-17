/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smatri;

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

        Environnement env = new Environnement(5);

        try {
            Object[] param = {env};
            acA = mc.createNewAgent("A", FormeAgent.class.getName(), param);
            acA.start();

            acB = mc.createNewAgent("B", FormeAgent.class.getName(), param);
            acB.start();

            acB = mc.createNewAgent("C", FormeAgent.class.getName(), param);
            acB.start();

            acB = mc.createNewAgent("D", FormeAgent.class.getName(), param);
            acB.start();

            acB = mc.createNewAgent("E", FormeAgent.class.getName(), param);
            acB.start();
          
        } catch (StaleProxyException ignored) {
        }
    }
    
}
