package uk.ac.cam.vap32;

import java.awt.Color;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

    private World mWorld;

    @Override
    protected void paintComponent(java.awt.Graphics g) {

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        if (mWorld != null){

            //System.out.println("yes");

            Pattern pattern = mWorld.getPattern();

            int rows = pattern.getHeight();
            int cols = pattern.getWidth();

            int ref = (this.getHeight() < this.getWidth()) ? this.getHeight() : this.getWidth();
            int length = ref / Math.max(rows, cols); // smallest between height and width divided by the max between rows and cols

            g.setColor(Color.LIGHT_GRAY);
            for (int i=0; i <= rows * length; i += length){
                g.drawLine(i, 0, i, cols * length);
            }

            for (int i=0; i <= cols * length; i += length){
                g.drawLine(0, i, rows * length, i);
            }

            for (int i=0; i<rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (mWorld.getCell(j, i)) {
                        g.setColor(Color.BLACK);
                        g.fillRect(i * length + 1, j * length + 1, length - 1, length - 1);
                    } else {
                        /**Stays blank**/
                    }
                }
            }

            g.setColor(Color.BLACK);
            g.drawString("Generation: " +  mWorld.getGenerationCount(), 10, this.getHeight() - 30);
        }

        // Sample drawing statements
        /*g.setColor(Color.BLACK);
        g.drawRect(200, 200, 30, 30);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(140, 140, 30, 30);
        g.fillRect(260, 140, 30, 30);
        g.setColor(Color.BLACK);
        g.drawLine(150, 300, 280, 300);
        g.drawString("@@@", 135,120);
        g.drawString("@@@", 255,120);*/
    }

    public void display(World w) {
        mWorld = w;
        repaint();
    }
}
