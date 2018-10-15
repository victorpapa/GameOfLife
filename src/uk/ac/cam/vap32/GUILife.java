package uk.ac.cam.vap32;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GUILife extends JFrame{

    private World mWorld;
    private PatternStore mStore;
    private ArrayList<World> mCachedWorld = new ArrayList<>();
    private GamePanel mGamePanel;
    private JButton mPlayButton;
    private boolean mPlaying;
    private Timer mTimer;

    public GUILife() {
        super("Game of Life");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,400);

        add(new JButton("Centre"));
        add(new JButton("North"), BorderLayout.NORTH);
        add(new JButton("South"), BorderLayout.SOUTH);
        add(new JButton("West"), BorderLayout.WEST);
        add(new JButton("East"), BorderLayout.EAST);
    }

    private void runOrPause() {
        if (mPlaying) {
            mTimer.cancel();
            mPlaying=false;
            mPlayButton.setText("Play");
        }
        else {
            mPlaying=true;
            mPlayButton.setText("Stop");
            mTimer = new Timer(true);
            mTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        moveForward();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 500);
        }
    }

    private void addBorder(JComponent component, String title) {
        Border etch = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border tb = BorderFactory.createTitledBorder(etch,title);
        component.setBorder(tb);
    }

    private JPanel createGamePanel() {
        mGamePanel = new GamePanel();
        addBorder(mGamePanel,"Game Panel");
        return mGamePanel;
    }

    private JPanel createPatternsPanel() {
        JPanel patt = new JPanel(new GridLayout());
        addBorder(patt,"Patterns");

        List<Pattern> aux = mStore.getPatternsNameSorted();

        Pattern[] myPatternArray = new Pattern[aux.size()];
        myPatternArray = aux.toArray(myPatternArray);

        JList<Pattern> myJList = new JList<>(myPatternArray);

        JScrollPane myScrollPane = new JScrollPane(myJList);


        myJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (mTimer != null)
                    mTimer.cancel();
                mPlaying=false;
                mPlayButton.setText("Play");

                JList<Pattern> list = (JList<Pattern>) e.getSource();
                Pattern p = list.getSelectedValue();
                int height = p.getHeight();
                int width = p.getWidth();

                if (height * width <= 64){
                    try {
                        mWorld = new PackedWorld(p);
                    } catch (PatternFormatException e1) {
                        e1.printStackTrace();
                    }
                }
                else{
                    try {
                        mWorld = new ArrayWorld(p);
                    } catch (PatternFormatException e1) {
                        e1.printStackTrace();
                    }
                }

                mCachedWorld.clear();
                mCachedWorld.add(mWorld);

                mGamePanel.display(mWorld);
            }
        });


        patt.add(myScrollPane);

        return patt;
    }

    private JPanel createControlPanel() {
        JPanel ctrl =  new JPanel();
        addBorder(ctrl,"Controls");

        GridLayout myGridLayout = new GridLayout(1, 3);
        ctrl.setLayout(myGridLayout);

        //ctrl.add(new JButton("< Back"), BorderLayout.WEST);
        //ctrl.add(new JButton("Play"), BorderLayout.CENTER);
        //ctrl.add(new JButton("Forward >"), BorderLayout.EAST);

        mPlayButton = new JButton("Play");
        JButton back = new JButton("< Back");
        JButton forward = new JButton("Forward >");


        mPlayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runOrPause();
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mTimer != null)
                    mTimer.cancel();
                mPlaying=false;
                mPlayButton.setText("Play");

                moveBack();
            }
        });

        forward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (mTimer != null)
                    mTimer.cancel();
                mPlaying=false;
                mPlayButton.setText("Play");

                try {
                    moveForward();
                } catch (CloneNotSupportedException e1) {
                    e1.printStackTrace();
                }
            }
        });

        ctrl.add(back);
        ctrl.add(mPlayButton);
        ctrl.add(forward);

        return ctrl;
    }

    public GUILife(PatternStore ps) {
        super("Game of Life");
        mStore = ps;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024,768);

        add(createGamePanel(),BorderLayout.CENTER);
        add(createPatternsPanel(),BorderLayout.WEST);
        add(createControlPanel(),BorderLayout.SOUTH);
    }

    private World copyWorld(boolean useCloning) throws CloneNotSupportedException {

        World copy;

        if (!useCloning){
            if (mWorld instanceof PackedWorld){
                copy = new PackedWorld((PackedWorld) mWorld);
            }
            else{
                copy = new ArrayWorld((ArrayWorld) mWorld);
            }
        }
        else{
            if (mWorld instanceof PackedWorld){
                copy = (PackedWorld) mWorld.clone();
            }
            else{
                copy = (ArrayWorld) mWorld.clone();
            }
        }
        return copy;
    }

    private void moveBack(){

        if (mWorld == null)
            return;

        if (mWorld.getGenerationCount() < 1)
            mWorld = mCachedWorld.get(0);
        else {
            mWorld = mCachedWorld.get(mWorld.getGenerationCount() - 1);
        }

        mGamePanel.display(mWorld);
        print();
    }

    private void moveForward() throws CloneNotSupportedException {

        if (mWorld == null)
            return;

        if (mWorld.getGenerationCount() + 1 < mCachedWorld.size()) {
            mWorld = mCachedWorld.get(mWorld.getGenerationCount() + 1);
        }
        else {

            World aux = copyWorld(true);
            aux.nextGeneration();
            mWorld = aux;
            mCachedWorld.add(mWorld);
        }

        mGamePanel.display(mWorld);
        print();
    }

    public void print() {

        int mHeight = mWorld.getPattern().getHeight();
        int mWidth = mWorld.getPattern().getWidth();

        System.out.println("- " + mWorld.getGenerationCount());
        for (int row = 0; row < mHeight; row++) {
            for (int col = 0; col < mWidth; col++) {
                System.out.print(mWorld.getCell(row, col) ? "#" : "_");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        //GUILife gui = new GUILife();
        //gui.setVisible(true);

        PatternStore myPs = new PatternStore("https://www.cl.cam.ac.uk/teaching/1617/OOProg/ticks/life.txt");
        GUILife myGUI = new GUILife(myPs);
        myGUI.setVisible(true);
    }
}