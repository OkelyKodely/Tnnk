package mount;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.Timer;
import javax.swing.*;

public class Tnnk extends JPanel implements KeyListener {

    JFrame j = new JFrame();
    Graphics g = null;    
    ArrayList<O> list = new ArrayList<>();
    ArrayList<O> list2 = new ArrayList<>();
    ArrayList<O> enemies = new ArrayList<>();
    ArrayList<O> points = new ArrayList<>();
    ArrayList<O> trees = new ArrayList<>();
    Tnk tnk = new Tnk(100, 300);
    int power = 110;
    JLabel powerLbl = new JLabel();
    boolean starting = true;
    static final double G = 9.8;
    int animationSpeed = 5;
    static int size = 900, ballDiameter = 10;
    double startX, startY, ballX, ballY;
    double xSpeed, ySpeed, lastPointX, lastPointY;
    double time, deltaTime = 0.01 ;
    Timer timer;
    boolean s = true;
    int angle = 0;
    double aangle = 0;
    boolean shooting = false;
    Polygon ppp = new Polygon();

    class O {
        int x, y;
    }
    
    class Tnk {
        int x, y;
        int sqtop_x, sqtop_y;
        int sqbot_x, sqbot_y;
        int can_x, can_y;
        
        Tnk(int xx, int yy) {
            x = xx;
            y = yy;
            sqbot_x = x;
            sqbot_y = y;
            sqtop_x = sqbot_x + 20;
            sqtop_y = sqbot_y - 30;
            can_x = sqtop_x + 50;
            can_y = sqtop_y;
            angle = can_y + 10;
        }
        
        void drawMe() {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.red);
            g2.fillRect(sqbot_x, sqbot_y, 90, 20);
            g2.setColor(Color.black);
            g2.fillRect(sqbot_x, sqbot_y+20, 90, 15);
            g2.setColor(Color.red);
            g2.fillRect(sqtop_x, sqtop_y, 50, 30);
            g2.setColor(Color.black);
            g2.drawRect(sqbot_x-1, sqbot_y-1, 92, 22);
            g2.setColor(Color.black);
            g2.drawRect(sqbot_x-1, sqbot_y+20-1, 92, 17);
            g2.setColor(Color.black);
            g2.drawRect(sqtop_x-1, sqtop_y-1, 52, 32);
            g2.setStroke(new BasicStroke(10));
            g2.setColor(Color.red);
            if(s) {
                angle = can_y+10;
                s = false;
            }
            g2.drawLine(can_x, can_y+10, can_x + 30, angle);
        }
    }
    
    public Tnnk() {

        setGUI();

        Thread t = new Thread() {
            public void run() {
                while(true) {
                    tnk.drawMe();
                    try {
                        Thread.sleep(100);
                    } catch(Exception e) {}
                }
            }
        };
        t.start();
    }

    public void paint(Graphics g) {

        if(starting) {
            list.clear();
            list2.clear();

            enemies.clear();
        }

        for(int i=0; i<801; i++) {
            g.setColor(new Color(100, 155, 55+i/4));
            g.drawLine(0, i, 1200, i);
        }
        Random rand = new Random();
        if(starting)
        for(int i=0; i<45; i++) {
            int v = 200 + rand.nextInt(100);
            O o = new O();
            o.x = i*30;
            o.y = v;
            list.add(o);
            
            if(i==3) {
                tnk = new Tnk(o.x, o.y);
            }
        }
        g.setColor(new Color(140, 250, 70));
        points.clear();
        for(int i=0; i+1<list.size(); i++) {

            double v = (double)list.get(i+1).x;
            double u = -(double)list.get(i+1).y;
            double t = (double)list.get(i).x;

            double w = 0d;

            double m = 0d;
            double y1 = 0d;

            m = (u-(-1*(double)list.get(i).y))/(v - t);

            for(int j=(int) t; j<v; j++) {
                O o = new O();

                w = j;

                y1 = u - m*(v - w);

                y1 = Math.round(y1);

                int x = (int) w;
                int y = -(int) y1;

                o.x = x;
                o.y = y;

                points.add(o);
            }
        }
        Graphics2D g2 = (Graphics2D) g;
        if(starting)
        for(int i=0; i<122; i++) {
            int v = 20 + rand.nextInt(130);
            O o = new O();
            o.x = 20 + rand.nextInt(1100);
            o.y = v;
            list2.add(o);
        }
        g2.setColor(Color.WHITE);
        for(int i=0; i<list2.size(); i++) {
            try {
                g2.drawOval(list2.get(i).x, list2.get(i).y, 1, 1);
            } catch(Exception e) {}
        }
        ppp = new Polygon();
        for(int i=0; i<points.size(); i++) {
            ppp.addPoint(points.get(i).x, points.get(i).y);
        }
        ppp.addPoint(1200, 800);
        ppp.addPoint(0, 800);
        g2.setColor(new Color(10, 50, 10));
        g2.fillPolygon(ppp);
        if(starting)
        for(int i=0; i<points.size(); i++) {
            int v = rand.nextInt(40);
            if(v == 0) {
                O o = new O();
                o.x = points.get(i).x;
                o.y = points.get(i).y;
                enemies.add(o);
            } else {
                O o = new O();
                o.x = -1000;
                o.y = -1111;
                enemies.add(o);
            }
        }
        for(int i=0; i<points.size(); i++) {
            try {
                if(enemies.get(i).x != -1000) {
                    enemies.get(i).x = points.get(i).x-25;
                    enemies.get(i).y = points.get(i).y-40;
                }
            } catch(Exception e) {}
        }
        for(int i=0; i<enemies.size(); i++) {
            try {
                ImageIcon ii = new ImageIcon(this.getClass().getResource("prisoner.gif_c200"));
                Image im = ii.getImage();
                g2.drawImage(im, enemies.get(i).x, enemies.get(i).y, 40, 60, null);
            } catch(Exception e) {}
        }
        if(starting)
            for(int i=0; i<50; i++) {
                O o = new O();
                o.x = rand.nextInt(1200);
                o.y = 500 + rand.nextInt(200);
                trees.add(o);
            }
        for(int i=0; i<trees.size(); i++) {
            g.setColor(Color.GREEN);
            g.fillRect(trees.get(i).x, trees.get(i).y, 10, 10);
            g.setColor(new Color(200, 100, 50));
            g.fillRect(trees.get(i).x, trees.get(i).y+10, 10, 20);
        }
        g.setColor(new Color(100, 100, 255));
        g.fillRoundRect(100, 600, 1000, 150, 50, 50);
        if(starting)
            starting = false;
        
        g.setColor(Color.GRAY);
        g.fillOval(1000, 50, 100, 100);
        g.setColor(new Color(150, 150, 150));
        g.fillOval(1030, 60, 20, 20);
        g.setColor(new Color(150, 150, 150));
        g.fillOval(1060, 80, 20, 20);
        g.setColor(new Color(150, 150, 150));
        g.fillOval(1030, 100, 20, 20);

        g.setColor(Color.BLUE);

        g.setFont(new Font("arial", Font.PLAIN, 25));

        g.drawString("F L A T L A N D E R", 100, 30);
        
        g.setFont(new Font("arial", Font.PLAIN, 15));
        
        g.setColor(Color.RED);
        g.drawString("POWER: " + power + "   ANGLE: " + aangle, 100, 70);

        g.setColor(Color.BLACK);
        
        g.drawString("power is left/right keys", 100, 90);
        g.drawString("angle is up/down keys", 100, 120);
        g.drawString("to shoot, press spacebar", 100, 150);

        tnk.drawMe();
    }

    private void drawExplosion(int x, int y) {
        java.awt.Image imgFb = null;

        String imageFb = "explosion.gif";

        javax.swing.ImageIcon iFb = new javax.swing.ImageIcon(this.getClass().getResource(imageFb));
        imgFb = iFb.getImage();

        g.drawImage(imgFb, x, y, 100, 100, null);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP) {
            angle -= 4;
            aangle += 7;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            angle += 4;
            aangle -= 7;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            power--;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            power++;
        }
        else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            if(!shooting) {
                shooting = true;
                time = 0;
                for(int i=0; i<(int)((double)power/(double)12); i++) {
                    ballX= lastPointX = startX = tnk.x + 90;
                    ballY = lastPointY = startY = tnk.y - 30;
                    getUserInput();

                    timer = new Timer(animationSpeed, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent event) {

                            if(time != -1) {
                                moveBall();

                                Graphics2D g2d = (Graphics2D) g;
                                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                        RenderingHints.VALUE_ANTIALIAS_ON);
                                g2d.setColor(Color.RED);
                                g2d.fillOval((int)ballX,(int)ballY,ballDiameter,ballDiameter);

                                repaint();

                                if(! inBounds()) {
                                    timer.stop();

                                    shooting = false;
                                }
                            }
                        }
                    });
                    timer.start();
                }
            }
        }
        repaint();
        
        tnk.drawMe();
    }

    private void moveBall() {

        ballX = startX + (xSpeed * time);
        ballY = startY - ((ySpeed *time)-(1.0 *G * Math.pow(time, 2))) ;
        time += deltaTime;
        
        for(int i=0; i<list.size(); i++) {
            try {
                int y2, y1, x2, x1;
                y2 = list.get(i+1).y;
                y1 = list.get(i).y;
                x2 = list.get(i+1).x;
                x1 = list.get(i).x;
                double m = (double)(y2 - y1)/(double)(x2 - x1);
                
                double v = (double)list.get(i+1).x;
                double u = -(double)list.get(i+1).y;
                double t = (double)list.get(i).x;

                double w = 0d;
                    
                m = (u-(-1*(double)list.get(i).y))/(v - t);
                double c = y1 - m*x1;

                if(Math.abs((ballY - c)/(ballX) - m) <= 0.6 && (Math.abs(ballX - x1) < 13 && Math.abs(ballY - y1) < 13)) {
                    
                    try {
                        drawExplosion((int)ballX,(int)ballY);
                        
                        list.remove(list.get(i+1));

                        O l = new O();
                        l.x = (int) ballX + 2;
                        l.y = (int) ballY+30;
                        list.add(i+1, l);
                        l = new O();
                        l.x = (int) ballX + 20;
                        l.y = (int) ballY + 30;
                        list.add(i+2, l);

                        list.get(i).x = list.get(i).x - 50;
                        list.get(i).y = l.y + 30;

                        list.get(i+1).x = list.get(i+1).x;
                        list.get(i+1).y = l.y + 30;

                        list.get(i+2).x = list.get(i+2).x;
                        list.get(i+2).y = l.y + 30;

                        time = -1;

                        shooting = false;

                    } catch(Exception e) {}
                }
            } catch(Exception e) {}
        }
        
//        for(int i=0; i<points.size(); i++) {
//            if((Math.abs(ballX - points.get(i).x) < 1 && Math.abs(ballY - points.get(i).y) < 1)) {
//
//                O o = new O();
//                o.x = points.get(i).x;
//                o.y = points.get(i).y+30;
//
//                for(int j=0; j<list.size(); j++) {
//                    if((Math.abs(ballX - list.get(j).x) < 1 && Math.abs(ballY - list.get(j).y) < 1)) {
//                        drawExplosion((int)ballX,(int)ballY);
//                        
//                        list.remove(list.get(j+1));
//
//                        O l = new O();
//                        l.x = (int) ballX + 2;
//                        l.y = (int) ballY+30;
//                        list.add(j+1, l);
//                        l = new O();
//                        l.x = (int) ballX + 20;
//                        l.y = (int) ballY + 120;
//                        list.add(j+2, l);
//
//                        list.get(j).x = list.get(j).x - 150;
//                        list.get(j).y = l.y + 50;
//                        break;
//                    }
//                }
//
//                time = -1;
//
//                shooting = false;
//            }
//        }

        repaint();
    }

    private void getUserInput() {

        double speed = 0 + power;
        xSpeed = speed * Math.cos(aangle * (Math.PI / 180));
        ySpeed = speed * Math.sin(aangle * (Math.PI / 180));
    }

    private boolean inBounds() {

        if((ballX < 0) || (ballX > (getWidth()))
                || ( ballY  > (getHeight() - ballDiameter) ) ) {
            return false;
        }

        return true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    void setGraphics() {
        
        g = this.getGraphics();
    }
    
    void setGUI() {
 
        j.setTitle("F L A T L A N D E R");
        
        j.setLayout(null);
        j.setBounds(0, 0, 1200, 800);
        this.setBounds(j.getBounds());
        j.add(this);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j.setVisible(true);
  
        setGraphics();
 
        j.addKeyListener(this);
    }
    
    public static void main(String[] args) {

        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new Tnnk();
                }
            });
        } catch(Exception e) {}
    }
}