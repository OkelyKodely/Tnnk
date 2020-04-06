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
    Tnk tnk = new Tnk(100, 400);
    int power = 60;
    JLabel powerLbl = new JLabel();
    boolean starting = true;
    static final double G = 9.8;
    int animationSpeed = 1;
    static int size = 900, ballDiameter = 10;
    double startX, startY, ballX, ballY;
    double xSpeed, ySpeed, lastPointX, lastPointY;
    double time, deltaTime = 0.01 ;
    ArrayList<Point2D> curvePoints= new ArrayList<>();
    Timer timer;
    boolean s = true;
    int angle = 0;
    boolean getout = false;
    double aangle = 0;

    class Bullet {
        int x;
    }
    
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
            g.setColor(Color.green);
            g.fillRect(sqbot_x, sqbot_y, 90, 20);
            g.setColor(Color.black);
            g.fillRect(sqbot_x, sqbot_y+20, 90, 15);
            g.setColor(Color.green);
            g.fillRect(sqtop_x, sqtop_y, 50, 30);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(10));
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
                }
            }
        };
        
        t.start();
    }

    public void paint(Graphics g) {

        if(starting)
            list.clear();

        if(starting)
            list2.clear();

        g.setColor(new Color(100, 205, 155));
        g.fillRect(0, 0, 1200, 800);
        Random rand = new Random();
        if(starting)
        for(int i=0; i<30; i++) {
            int v = 300 + rand.nextInt(140);
            O o = new O();
            o.x = i*45;
            o.y = v;
            list.add(o);
        }
        g.setColor(new Color(240, 150, 70));
        for(int j=0; j<800; j++)
        for(int i=0; i<list.size(); i++) {
            try {
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(3));
    
                g2.drawLine(list.get(i).x, list.get(i).y+j, list.get(i+1).x, list.get(i+1).y+j);
            } catch(Exception e) {}
        }
        if(starting)
        for(int i=0; i<250; i++) {
            int v = 500 + rand.nextInt(200);
            O o = new O();
            o.x = i*50;
            o.y = v;
            list2.add(o);
        }
        if(starting)
            starting = false;
        for(int i=0; i<list2.size(); i++) {
            try {
                g.setColor(new Color(130, 255, 130));
                g.fillRect(list2.get(i).x, list2.get(i).y, 20, 20);
                g.setColor(new Color(200, 150, 90));
                g.fillRect(list2.get(i).x, list2.get(i).y+20, 20, 40);
            } catch(Exception e) {}
        }

        g.setColor(Color.BLACK);
        g.drawString(power + "", 100, 40);
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
            time = 0;
            for(int i=0; i<(int)((double)power/(double)12); i++) {
                ballX= lastPointX = startX = 190;
                ballY = lastPointY = startY = 400;
                getUserInput();

                timer = new Timer(animationSpeed, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {

                        moveBall();

                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                        g2d.setColor(Color.RED);
                        g2d.fillOval((int)ballX,(int)ballY,ballDiameter,ballDiameter);

                        if((Math.abs(lastPointX - ballX)>=1) && (Math.abs(lastPointY - ballY)>=1) ) {
                            curvePoints.add(new Point2D.Double(ballX, ballY));
                            lastPointX = ballX; lastPointY = ballY;
                        }

                        repaint();

                        if(! inBounds()) {
                            timer.stop();
                            getout = false;
                        }
                    }
                });
                timer.start();
                
                getout = true;
            }
        }
        repaint();
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
                double c = y1 - m*x1;
                
                if(Math.abs((ballY - c)/(ballX) - m) < 3 && (Math.abs(ballX - x1) < 25 && Math.abs(ballY - y1) < 25)) {
                    O l = new O();
                    l.x = (int) ballX + 2;
                    l.y = (int) ballY + 300;
                    list.add(i+1, l);
                    l = new O();
                    l.x = (int) ballX + 120;
                    l.y = (int) ballY + 300;
                    list.add(i+2, l);
                }
            } catch(Exception e) {}
        }
        
        repaint();
    }

    private void getUserInput() {

        double speed = 50 + power;
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
        
        j.setLayout(null);
        j.setBounds(0, 0, 1200, 800);
        this.setBounds(j.getBounds());
        j.add(this);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j.setVisible(true);
        j.setExtendedState(j.getExtendedState() | JFrame.MAXIMIZED_BOTH);
  
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