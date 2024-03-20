import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends JFrame {
    private ReentrantLock lock1 = new ReentrantLock();
    private ReentrantLock lock2 = new ReentrantLock();
    private ReentrantLock lock3 = new ReentrantLock();
    private ReentrantLock lock4 = new ReentrantLock();
    private ReentrantLock lock5 = new ReentrantLock();
    private ReentrantLock lock6 = new ReentrantLock();
    private ReentrantLock lock7 = new ReentrantLock();
    private JPanel panel = new JPanel();
    private JLabel redSquare = new JLabel();
    private JLabel blackSquare;
    private JLabel greenSquare;
    private JLabel termSquare = new JLabel();

    private int x = 250;
    private int y = 250;
    private ArrayList<int []> squareList = new ArrayList<>();
    private ArrayList<JLabel> greenSquareList = new ArrayList<>();
    private ArrayList<JLabel> blackSquareList = new ArrayList<>();
    private ArrayList<JLabel> squares = new ArrayList<>();
    private int count1 = -1;
    private int count2 = -1;
    private int myValue1 = 0;
    private int myValue2 = 0;
    private int removedBlack = 0;

    public Game() {
        squareList.add(new int[]{x, y});

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel.setBackground(Color.LIGHT_GRAY);
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(500, 500));
        panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        panel.setFocusable(true);
        add(panel);

        setResizable(false);
        pack();
        setVisible(true);
    }

    public void addingEnemies() {
        blackSquare = new JLabel();
        blackSquare.setOpaque(true);
        blackSquare.setBackground(Color.BLACK);
        int [] locationList = new int[2];
        int first;
        int last;
        do {
            first = 1;
            while (first % 10 != 0) {
                first = (int) (Math.random() * 49) * 10;
            }
            locationList[0] = first;
            last = 1;
            while (last % 10 != 0) {
                last = (int) (Math.random() * 49) * 10;
            }
            locationList[1] = last;
        } while (isContain(locationList));
        squareList.add(locationList);
        blackSquare.setBounds(first, last, 10, 10);
        squares.add(blackSquare);
        blackSquareList.add(blackSquare);
        panel.add(blackSquare);
    }

    public void addingFriends() {
        greenSquare = new JLabel();
        greenSquare.setOpaque(true);
        greenSquare.setBackground(Color.GREEN);
        int [] locationList = new int[2];
        int first;
        int last;
        do {
            first = 1;
            while (first % 10 != 0) {
                first = (int) (Math.random() * 49) * 10;
            }
            locationList[0] = first;
            last = 1;
            while (last % 10 != 0) {
                last = (int) (Math.random() * 49) * 10;
            }
            locationList[1] = last;
        } while (isContain(locationList));
        squareList.add(locationList);
        greenSquare.setBounds(first, last, 10, 10);
        squares.add(greenSquare);
        greenSquareList.add(greenSquare);
        panel.add(greenSquare);
    }
    public boolean isContain(int[] array) {
        for (int[] ints : squareList) {
            if (ints[0] == array[0] && ints[1] == array[1]) {
                return true;
            }
        }
        return false;
    }
    public void movingEnemy(int x, int y, int local) {

        while (true) {
            //System.out.println("1");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock1.lock();
            int randomNumber = (int) Math.ceil(Math.random() * 4);
            if(randomNumber == 1) {
                x = x - 10;
                if(x < 0) {
                    x = x + 20;
                }
            }
            else if(randomNumber == 2) {
                x = x + 10;
                if(x > 490) {
                    x = x - 20;
                }
            }
            else if(randomNumber == 3) {
                y = y - 10;
                if(y < 0) {
                    y = y + 20;
                }
            }
            else if(randomNumber == 4) {
                y = y + 10;
                if(y > 490) {
                    y = y - 20;
                }
            }
            myValue1++;

            if(myValue1 == 2 * blackSquareList.size() + 1) {
                myValue1 = 1;
            }

            if(!(blackSquareList.get(local) instanceof TermThread)) {
                blackSquareList.get(local).setLocation(x, y);
            }
            boolean isCollision2 = false;
            int index = -1;

            for(int i = 0; i < greenSquareList.size(); i++) {
                if(!(blackSquareList.get(local) instanceof TermThread) && !(greenSquareList.get(i) instanceof TermThread) && blackSquareList.get(local).getBounds().intersects(greenSquareList.get(i).getBounds())) {
                    isCollision2 = true;
                    index = i;
                    break;
                }
            }
            if (isCollision2) {
                panel.remove(blackSquareList.get(local));
                panel.remove(greenSquareList.get(index));
                blackSquareList.add(local, new TermThread(termSquare));
                blackSquareList.remove(local + 1);
                removedBlack++;
                if(removedBlack == blackSquareList.size()) {
                    dispose();
                    JOptionPane.showMessageDialog(null, "Oyunu kazandınız");
                    System.exit(0);
                }
                greenSquareList.add(index, new TermThread(termSquare));
                greenSquareList.remove(index + 1);
                repaint();
            }
            if(blackSquareList.get(local).getBounds().intersects(redSquare.getBounds())) {
                dispose();
                JOptionPane.showMessageDialog(null, "Oyunu kaybettiniz");
                System.exit(0);
            }
            if(!(blackSquareList.get(local) instanceof TermThread) && myValue1 > blackSquareList.size()) {
                FireBlue fire = new FireBlue(x, y);
                fire.start();
            }
            lock1.unlock();

        }
    }
    public void movingFriend(int x, int y, int local) {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock2.lock();
            int randomNumber = (int) Math.ceil(Math.random() * 4);
            if(randomNumber == 1) {
                x = x - 10;
                if(x < 0) {
                    x = x + 20;
                }
            }
            else if(randomNumber == 2) {
                x = x + 10;
                if(x > 490) {
                    x = x - 20;
                }
            }
            else if(randomNumber == 3) {
                y = y - 10;
                if(y < 0) {
                    y = y + 20;
                }
            }
            else if(randomNumber == 4) {
                y = y + 10;
                if(y > 490) {
                    y = y - 20;
                }
            }
            myValue2++;
            if(myValue2 == 2 * greenSquareList.size() + 1) {
                myValue2 = 1;
            }
            if(!(greenSquareList.get(local) instanceof TermThread)) {
                greenSquareList.get(local).setLocation(x, y);
            }
            if(greenSquareList.get(local).getBounds().intersects(redSquare.getBounds())) {
                if(randomNumber == 1) {
                    x = x + 20;
                    greenSquareList.get(local).setLocation(x, y);
                }
                else if(randomNumber == 2) {
                    x = x - 20;
                    greenSquareList.get(local).setLocation(x, y);
                }
                else if(randomNumber == 3) {
                    y = y + 20;
                    greenSquareList.get(local).setLocation(x, y);
                }
                else if(randomNumber == 4) {
                    y = y - 20;
                    greenSquareList.get(local).setLocation(x, y);
                }
            }
            boolean isCollision2 = false;
            int index = -1;
            for(int i = 0; i < blackSquareList.size(); i++) {
                if(!(greenSquareList.get(local) instanceof TermThread) && !(blackSquareList.get(i) instanceof TermThread) && greenSquareList.get(local).getBounds().intersects(blackSquareList.get(i).getBounds())) {
                    isCollision2 = true;
                    index = i;
                    break;
                }
            }
            if (isCollision2) {
                panel.remove(greenSquareList.get(local));
                panel.remove(blackSquareList.get(index));
                greenSquareList.add(local, new TermThread(termSquare));
                greenSquareList.remove(local + 1);
                blackSquareList.add(index, new TermThread(termSquare));
                blackSquareList.remove(index + 1);
                removedBlack++;
                if(removedBlack == blackSquareList.size()) {
                    dispose();
                    JOptionPane.showMessageDialog(null, "Oyunu kazandınız");
                    System.exit(0);
                }
                repaint();

            }
            if(!(greenSquareList.get(local) instanceof TermThread) && myValue2 > greenSquareList.size()) {
                FireMagenta fire = new FireMagenta(x, y);
                fire.start();
            }
            lock2.unlock();
        }
    }

    class Enemy extends Thread{
        public Enemy() {
            addingEnemies();
        }
        public void run() {
            int x, y;
            int local;

            lock6.lock();
            local = count1;
            local = local + 1;
            count1 = local;
            x = 1000;
            y = 1000;
            if(!(blackSquareList.get(local) instanceof TermThread)) {
                x = blackSquareList.get(local).getX();
                y = blackSquareList.get(local).getY();
            }
            lock6.unlock();
            //System.out.println("1");
            movingEnemy(x, y, local);

        }
    }
    class Friend extends Thread{
        public Friend() {
            addingFriends();
        }
        public void run() {

            int x, y;
            int local;

            lock7.lock();
            local = count2;
            local = local + 1;
            count2 = local;
            x = 1000;
            y = 1000;
            if(!(greenSquareList.get(local) instanceof TermThread)) {
                x = greenSquareList.get(local).getX();
                y = greenSquareList.get(local).getY();
            }
            lock7.unlock();
            //System.out.println("2");
            movingFriend(x, y, local);

        }

    }
    class AirCraft extends Thread implements MouseInputListener, KeyListener{
        public AirCraft() {
            addMouseListener(this);
            addMouseMotionListener(this);
            panel.addKeyListener(this);

            redSquare.setOpaque(true);
            redSquare.setBackground(Color.RED);
            redSquare.setBounds(x, y, 10, 10);
            squares.add(redSquare);
            panel.add(redSquare);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            FireOrange fire = new FireOrange();
            fire.start();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_A) {
                x = x - 10;
            }
            else if(e.getKeyCode() == KeyEvent.VK_S) {
                y = y + 10;
            }
            else if(e.getKeyCode() == KeyEvent.VK_D) {
                x = x + 10;
            }
            else if(e.getKeyCode() == KeyEvent.VK_W) {
                y = y - 10;
            }
            redSquare.setLocation(x, y);
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
    class TermThread extends JLabel {

        public TermThread(JLabel term) {
            term = new JLabel();
        }

    }
    class FireOrange extends Thread {
        private JLabel fireOrange1;
        private JLabel fireOrange2;
        private int fireX1;
        private int fireY1;
        private int fireX2;
        private int fireY2;
        public FireOrange() {
            fireOrange1 = new JLabel();
            fireOrange2 = new JLabel();

            fireOrange1.setOpaque(true);
            fireOrange1.setBackground(Color.ORANGE);
            fireX1 = x - 5;
            fireY1 = y;
            fireOrange1.setBounds(fireX1, fireY1, 5, 5);
            panel.add(fireOrange1);

            fireOrange2.setOpaque(true);
            fireOrange2.setBackground(Color.ORANGE);
            fireX2 = x + 10;
            fireY2 = y;
            fireOrange2.setBounds(fireX2, fireY2, 5, 5);
            panel.add(fireOrange2);
        }
        public void run() {

            while(fireX1 > 0 | fireX2 < 500) {
                //System.out.println("cc");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException f) {
                    f.printStackTrace();
                }
                lock5.lock();
                fireX1 = fireX1 - 10;
                fireOrange1.setLocation(fireX1, fireY1);
                fireX2 = fireX2 + 10;
                fireOrange2.setLocation(fireX2, fireY2);

                EventQueue.invokeLater(() -> {
                    boolean isCollision1 = false;
                    boolean isCollision2 = false;
                    boolean isCollision3 = false;
                    boolean isCollision4 = false;
                    for(int i = 0; i < greenSquareList.size(); i++) {
                        if(!(greenSquareList.get(i) instanceof TermThread) && fireOrange1.getBounds().intersects(greenSquareList.get(i).getBounds())) {
                            isCollision1 = true;
                            break;
                        }
                    }
                    if(isCollision1) {
                        panel.remove(fireOrange1);
                        repaint();
                    }
                    for(int i = 0; i < greenSquareList.size(); i++) {
                        if(!(greenSquareList.get(i) instanceof TermThread) && fireOrange2.getBounds().intersects(greenSquareList.get(i).getBounds())) {
                            isCollision2 = true;
                            break;
                        }
                    }
                    if(isCollision2) {
                        panel.remove(fireOrange2);
                        repaint();
                    }
                    int index1 = -1;
                    int index2 = -1;
                    for(int i = 0; i < blackSquareList.size(); i++) {
                        if(!isCollision1 && !(blackSquareList.get(i) instanceof TermThread) && fireOrange1.getBounds().intersects(blackSquareList.get(i).getBounds())) {
                            isCollision3 = true;
                            index1 = i;
                            break;
                        }
                    }
                    if(isCollision3) {
                        panel.remove(fireOrange1);
                        panel.remove(blackSquareList.get(index1));
                        blackSquareList.add(index1, new TermThread(termSquare));
                        blackSquareList.remove(index1 + 1);
                        removedBlack++;
                        if(removedBlack == blackSquareList.size()) {
                            dispose();
                            JOptionPane.showMessageDialog(null, "Oyunu kazandınız");
                            System.exit(0);
                        }
                        repaint();
                    }
                    for(int i = 0; i < blackSquareList.size(); i++) {
                        if(!isCollision2 && !(blackSquareList.get(i) instanceof TermThread) && fireOrange2.getBounds().intersects(blackSquareList.get(i).getBounds())) {
                            isCollision4 = true;
                            index2 = i;
                            break;
                        }
                    }
                    if(isCollision4) {
                        panel.remove(fireOrange2);
                        panel.remove(blackSquareList.get(index2));
                        blackSquareList.add(index2, new TermThread(termSquare));
                        blackSquareList.remove(index2 + 1);
                        removedBlack++;
                        if(removedBlack == blackSquareList.size()) {
                            dispose();
                            JOptionPane.showMessageDialog(null, "Oyunu kazandınız");
                            System.exit(0);
                        }
                        repaint();
                    }
                });

                lock5.unlock();
            }
        }
    }
    class FireBlue extends Thread {
        private JLabel fireBlue1;
        private JLabel fireBlue2;
        private int fireX1;
        private int fireY1;
        private int fireX2;
        private int fireY2;
        public FireBlue(int x, int y) {
            fireBlue1 = new JLabel();
            fireBlue2 = new JLabel();

            fireBlue1.setOpaque(true);
            fireBlue1.setBackground(Color.BLUE);
            fireX1 = x - 5;
            fireY1 = y;
            fireBlue1.setBounds(fireX1, fireY1, 5, 5);
            panel.add(fireBlue1);

            fireBlue2.setOpaque(true);
            fireBlue2.setBackground(Color.BLUE);
            fireX2 = x + 10;
            fireY2 = y;
            fireBlue2.setBounds(fireX2, fireY2, 5, 5);
            panel.add(fireBlue2);
        }
        public void run() {

            while(fireX1 > 0 | fireX2 < 500) {
                //System.out.println("cc");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException f) {
                    f.printStackTrace();
                }
                lock3.lock();
                fireX1 = fireX1 - 10;
                fireBlue1.setLocation(fireX1, fireY1);
                fireX2 = fireX2 + 10;
                fireBlue2.setLocation(fireX2, fireY2);

                EventQueue.invokeLater(() -> {
                    boolean isCollision1 = false;
                    boolean isCollision2 = false;
                    boolean isCollision3 = false;
                    boolean isCollision4 = false;
                    for(int i = 0; i < blackSquareList.size(); i++) {
                        if(!(blackSquareList.get(i) instanceof TermThread) && fireBlue1.getBounds().intersects(blackSquareList.get(i).getBounds())) {
                            isCollision1 = true;
                            break;
                        }
                    }
                    if(isCollision1) {
                        panel.remove(fireBlue1);
                        repaint();
                    }
                    for(int i = 0; i < blackSquareList.size(); i++) {
                        if(!(blackSquareList.get(i) instanceof TermThread) && fireBlue2.getBounds().intersects(blackSquareList.get(i).getBounds())) {
                            isCollision2 = true;
                            break;
                        }
                    }
                    if(isCollision2) {
                        panel.remove(fireBlue2);
                        repaint();
                    }
                    int index1 = -1;
                    int index2 = -1;
                    for(int i = 0; i < greenSquareList.size(); i++) {
                        if(!isCollision1 && !(greenSquareList.get(i) instanceof TermThread) && fireBlue1.getBounds().intersects(greenSquareList.get(i).getBounds())) {
                            isCollision3 = true;
                            index1 = i;
                            break;
                        }
                    }
                    if(isCollision3) {
                        panel.remove(fireBlue1);
                        panel.remove(greenSquareList.get(index1));
                        greenSquareList.add(index1, new TermThread(termSquare));
                        greenSquareList.remove(index1 + 1);
                        repaint();
                    }
                    for(int i = 0; i < greenSquareList.size(); i++) {
                        if(!isCollision2 && !(greenSquareList.get(i) instanceof TermThread) && fireBlue2.getBounds().intersects(greenSquareList.get(i).getBounds())) {
                            isCollision4 = true;
                            index2 = i;
                            break;
                        }
                    }
                    if(isCollision4) {
                        panel.remove(fireBlue2);
                        panel.remove(greenSquareList.get(index2));
                        greenSquareList.add(index2, new TermThread(termSquare));
                        greenSquareList.remove(index2 + 1);
                        repaint();
                    }
                    if(fireBlue1.getBounds().intersects(redSquare.getBounds())) {
                        //System.out.println("Oyunu Kaybettiniz");
                        redSquare.setLocation(1000, 1000);
                        dispose();
                        JOptionPane.showMessageDialog(null, "Oyunu kaybettiniz");
                        System.exit(0);
                    }
                    if(fireBlue2.getBounds().intersects(redSquare.getBounds())) {
                        //System.out.println("Oyunu Kaybettiniz");
                        redSquare.setLocation(1000, 1000);
                        dispose();
                        JOptionPane.showMessageDialog(null, "Oyunu kaybettiniz");
                        System.exit(0);
                    }
                });
                lock3.unlock();
            }
        }
    }
    class FireMagenta extends Thread {
        private JLabel fireMagenta1;
        private JLabel fireMagenta2;
        private int fireX1;
        private int fireY1;
        private int fireX2;
        private int fireY2;
        public FireMagenta(int x, int y) {
            fireMagenta1 = new JLabel();
            fireMagenta2 = new JLabel();

            fireMagenta1.setOpaque(true);
            fireMagenta1.setBackground(Color.MAGENTA);
            fireX1 = x - 5;
            fireY1 = y;
            fireMagenta1.setBounds(fireX1, fireY1, 5, 5);
            panel.add(fireMagenta1);

            fireMagenta2.setOpaque(true);
            fireMagenta2.setBackground(Color.MAGENTA);
            fireX2 = x + 10;
            fireY2 = y;
            fireMagenta2.setBounds(fireX2, fireY2, 5, 5);
            panel.add(fireMagenta2); /*
            System.out.println(fireX1 + " " + fireX2);
            System.out.println("-----");*/
        }
        public void run() {

            while(fireX1 > 0 | fireX2 < 500) {
                //System.out.println("cc");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException f) {
                    f.printStackTrace();
                }
                lock4.lock();
                fireX1 = fireX1 - 10;
                fireMagenta1.setLocation(fireX1, fireY1);
                fireX2 = fireX2 + 10;
                fireMagenta2.setLocation(fireX2, fireY2);
                //System.out.println(fireX1 + " " + fireX2);
                EventQueue.invokeLater(() -> {
                    boolean isCollision1 = false;
                    boolean isCollision2 = false;
                    boolean isCollision3 = false;
                    boolean isCollision4 = false;
                    for(int i = 0; i < greenSquareList.size(); i++) {
                        if(!(greenSquareList.get(i) instanceof TermThread) && fireMagenta1.getBounds().intersects(greenSquareList.get(i).getBounds())) {
                            isCollision1 = true;
                            break;
                        }
                    }
                    if(isCollision1) {
                        panel.remove(fireMagenta1);
                        repaint();
                    }
                    for(int i = 0; i < greenSquareList.size(); i++) {
                        if(!(greenSquareList.get(i) instanceof TermThread) && fireMagenta2.getBounds().intersects(greenSquareList.get(i).getBounds())) {
                            isCollision2 = true;
                            break;
                        }
                    }
                    if(isCollision2) {
                        panel.remove(fireMagenta2);
                        repaint();
                    }
                    int index1 = -1;
                    int index2 = -1;
                    for(int i = 0; i < blackSquareList.size(); i++) {
                        if(!isCollision1 && !(blackSquareList.get(i) instanceof TermThread) && fireMagenta1.getBounds().intersects(blackSquareList.get(i).getBounds())) {
                            isCollision3 = true;
                            index1 = i;
                            break;
                        }
                    }
                    if(isCollision3) {
                        panel.remove(fireMagenta1);
                        panel.remove(blackSquareList.get(index1));
                        blackSquareList.add(index1, new TermThread(termSquare));
                        blackSquareList.remove(index1 + 1);
                        removedBlack++;
                        if(removedBlack == blackSquareList.size()) {
                            dispose();
                            JOptionPane.showMessageDialog(null, "Oyunu kazandınız");
                            System.exit(0);
                        }
                        repaint();
                    }
                    for(int i = 0; i < blackSquareList.size(); i++) {
                        if(!isCollision2 && !(blackSquareList.get(i) instanceof TermThread) && fireMagenta2.getBounds().intersects(blackSquareList.get(i).getBounds())) {
                            isCollision4 = true;
                            index2 = i;
                            break;
                        }
                    }
                    if(isCollision4) {
                        panel.remove(fireMagenta2);
                        panel.remove(blackSquareList.get(index2));
                        blackSquareList.add(index2, new TermThread(termSquare));
                        blackSquareList.remove(index2 + 1);
                        removedBlack++;
                        if(removedBlack == blackSquareList.size()) {
                            dispose();
                            JOptionPane.showMessageDialog(null, "Oyunu kazandınız");
                            System.exit(0);
                        }
                        repaint();
                    }
                    if(fireMagenta1.getBounds().intersects(redSquare.getBounds())) {
                        panel.remove(fireMagenta1);
                        repaint();
                    }
                    if(fireMagenta2.getBounds().intersects(redSquare.getBounds())) {
                        panel.remove(fireMagenta2);
                        repaint();
                    }
                });
                //System.out.println("--*--");
                lock4.unlock();
            }
        }
    }
}
