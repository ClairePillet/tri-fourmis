/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

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
     private Collection< Position>ObjectPositions;
    private Image dbImage;

    private painting_area canvas;

    public Gui() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {

        dbImage = ImageIO.read(new File("test.jpg"));

      
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
    public  void moveObject(Collection< Position> positions) {

        this.ObjectPositions = positions;
    }

    public class painting_area extends JPanel {

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //

            g.drawImage(dbImage, 0, 0, getWidth(), getHeight(), this);
            if (Agentpositions!=null && ObjectPositions!=null) {
                for(Position pos :Agentpositions){               
                    try {                      
                        g.drawImage(ImageIO.read(new File("agent.jpg")), pos.getX() * 100 + 25, pos.getY() * 100 + 25,
                                25,
                                25, this);
                    } catch (IOException ex) {
                        Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                  g.setColor(Color.RED);
                for(Position pos :ObjectPositions){               
                    try {                      
                        g.drawImage(ImageIO.read(new File("agent.jpg")), pos.getX() * 100 + 25, pos.getY() * 100 + 25,
                                25,
                                25, this);
                        g.fillOval(pos.getX() * 100 + 25, pos.getX() * 100 + 25, 25, 25);
                    } catch (IOException ex) {
                        Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }
    }
}
