package cool;

import javax.swing.*;
import java.awt.*;

public class AreaUI extends JPanel implements Runnable {
    private Area grid;
    private SimObjectChooser simobjchooser;
    private boolean stopThread;
    final static int SLEEP_DELAY = 200;

    AreaUI (Area grid, SimObjectChooser simobjchooser) {
        this.grid = grid;
        this.simobjchooser = simobjchooser;
        this.stopThread = true;
        setOpaque (false);
    }

    private int getSquareWidth ()
    {
        int swidth = getWidth() / grid.numColumns ();

        if (swidth > 0) {
            return swidth;
        } else {
            return 1;
        }
    }

    private int getSquareHeight ()
    {
        int sheight = getHeight() / grid.numRows ();

        if (sheight > 0) {
            return sheight;
        } else {
            return 1;
        }
    }

    private int getHorizontalOffset () {
        return (getWidth() - (getSquareWidth () * grid.numColumns ())) / 2;
    }

    private int getVerticalOffset () {
        return (getHeight() - (getSquareHeight () * grid.numRows ())) / 2;
    }

    public void setObjectAt (int x, int y) {
        if (stopThread) {
            int row = (y - getVerticalOffset ()) / getSquareHeight ();
            int col = (x - getHorizontalOffset ()) / getSquareWidth ();

            grid.setObjectAt(row, col, simobjchooser.getSelectedClass());
            repaint ();
        }
    }

    public void startObjects () {
        grid.startObjects ();
        Thread displayThread = new Thread(this);
        displayThread.start();
    }

    public void run() {
        stopThread = false;

        while (!stopThread) {
            repaint ();
            try {
                Thread.sleep (SLEEP_DELAY);
            } catch (java.lang.InterruptedException e) {
                System.out.println("Display thread interrupted.");
                break;
            }
        }
    }

    public void pauseObjects () {
        grid.pauseObjects ();
        this.stopThread = true;
    }

    public void paintComponent (Graphics g) throws RuntimeException
    {
        int squarewidth = getSquareWidth ();
        int squareheight = getSquareHeight ();

        int hoffset = getHorizontalOffset ();
        int voffset = getVerticalOffset ();

        for (int row = 0; row < grid.numRows (); row++) {
            for (int col = 0; col < grid.numColumns (); col++) {
                SimObject tmp = grid.grabObjectAt (row,col);

                if (tmp == null) {
                    g.setColor (Color.black);
                } else {
                    g.setColor (tmp.getColor());
                }

                g.fillRect (hoffset + col * squarewidth, voffset + row * squareheight,
                        squarewidth - 1, squareheight - 1);
            }
        }
    }
}
