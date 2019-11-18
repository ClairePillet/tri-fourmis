/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smatri;

import jade.core.AID;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import smatri.Position;

/**
 *
 * @author claire
 */
public class Gui extends JFrame {

    private Collection< Position> Agentpositions;
     private Map< Position, String>Object;
    private Image dbImage;
private int n,m;
    private painting_area canvas;

    public Gui(int n ,int m ,Map< Position, String> Object ) {
        try {
            this.n= n*10;
             this.m= m*10;
             this.Object=Object;
             setSize(this.n,this.m);
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {

     
        canvas = new painting_area();
        add(canvas);

        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                canvas.repaint();
            }
        };

        new Timer(500, taskPerformer).start();

    }

  public  void moveAgent(Collection< Position> positions) {

        this.Agentpositions = positions;
    }
    public  void moveObject(Map<  Position,String > positions) {

        this.Object = positions;
    }

    public class painting_area extends JPanel {

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //
             g.setColor(Color.BLACK);
            for (int x = 0; x <= n; x += 10)
                for (int y = 0; y <= m; y +=10)
                    g.drawRect(x, y, 10, 10);
    
           
            if (Agentpositions!=null && Object!=null) {
                for(Position pos :Agentpositions){               
                                       
                        g.fillOval(pos.getX()*10 , pos.getY()*10 , 10, 10);
                  
                }
               Iterator i = Object.entrySet().iterator();
                while (i.hasNext()) {            
                                 Map.Entry form = (Map.Entry) i.next();
                        String name = (String) form.getValue();
                        Position pos = (Position) form.getKey();        
                        if(name=="B"){
                              g.setColor(new Color(255, 0, 0, 125));
                            
                        }else{
                            
                              g.setColor(new Color(0, 0, 255, 125));
                        }
                            g.fillOval(pos.getX() * 10, pos.getY() * 10, 10, 10);
                   
                }
            }

        }
    }
}
