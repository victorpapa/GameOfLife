package uk.ac.cam.vap32;

import java.util.ArrayList;

public class ArrayWorld extends World implements Cloneable{

    private boolean[][] mWorld;
    private boolean[] mDeadRow;

    @Override
    public Object clone() throws CloneNotSupportedException{

        ArrayWorld copy = (ArrayWorld)super.clone();
        copy.mWorld = new boolean[this.mWorld.length][this.mWorld[0].length];

        for (int i=0; i<this.mWorld.length; i++){

            boolean ok = false; /// do we find a "true" in the row? if not, then it is a DeadRow

            for (int j=0; j<this.mWorld[i].length; j++)
                if (mWorld[i][j] == true){
                    ok = true;
                    break;
                }

            if (ok == true)
                copy.mWorld[i] = this.mWorld[i].clone();
            else
            {copy.mWorld[i] = this.mDeadRow;/* System.out.println("****   ----- " + i + "  " + copy.mWorld[i].toString());*/}
        }
        //System.out.println(copy.mWorld[1].toString() + "   ******   " + this.mWorld[1].toString());

        return copy;
    }

    public ArrayWorld(ArrayWorld toCopy) throws CloneNotSupportedException {
        super(toCopy);

        this.mWorld = new boolean[toCopy.mWorld.length][toCopy.mWorld[0].length]; /**** initialise mWorld ****/
        this.mDeadRow = toCopy.mDeadRow;

        for (int i=0; i<toCopy.mWorld.length; i++){

            boolean ok = false; /**** do we find a "true" in the row? if not, then it is a DeadRow ****/

            for (int j=0; j<toCopy.mWorld[i].length; j++)
                if (toCopy.mWorld[i][j] == true){
                    ok = true;
                    break;
                }

            if (ok == true)
                this.mWorld[i] = toCopy.mWorld[i].clone();
            else
                this.mWorld[i] = toCopy.mDeadRow;
        }
    }

    public ArrayWorld(String serial) throws PatternFormatException{

        super(serial);

        /**** initialise mWorld ****/

        mWorld = new boolean[getPattern().getHeight()][getPattern().getWidth()];
        getPattern().initialise(this);

        /**** initialise mDeadRow ****/

        mDeadRow = new boolean[mWorld[0].length];
        for (int i=0; i<mWorld[0].length; i++)
            mDeadRow[i] = false;



        for (int i=0; i<mWorld.length; i++){

            boolean ok = false; /**** do we find a "true" in the row? if not, then it is a DeadRow ****/

            for (int j=0; j<mWorld[i].length; j++)
                if (mWorld[i][j] == true){
                    ok = true;
                    break;
                }

            if (ok != true)
                this.mWorld[i] = mDeadRow;
        }
    }

    public ArrayWorld(Pattern p) throws PatternFormatException{

        super(p);

        /**** initialise mWorld ****/

        mWorld = new boolean[p.getHeight()][p.getWidth()];
        p.initialise(this);

        /**** initialise mDeadRow ****/

        mDeadRow = new boolean[mWorld[0].length];
        for (int i=0; i<mWorld[0].length; i++)
            mDeadRow[i] = false;


        for (int i=0; i<mWorld.length; i++){

            boolean ok = false; /**** do we find a "true" in the row? if not, then it is a DeadRow ****/

            for (int j=0; j<mWorld[i].length; j++)
                if (mWorld[i][j] == true){
                    ok = true;
                    break;
                }

            if (ok != true)
                this.mWorld[i] = mDeadRow;
        }
    }

    public boolean getCell(int col, int row) {

        int mHeight = mWorld.length;
        int mWidth = mWorld[0].length;

        if (row < 0 || row >= mHeight) return false;
        if (col < 0 || col >= mWidth) return false;

        return mWorld[row][col];
    }

    public void setCell(int col, int row, boolean value){

        int mHeight = mWorld.length;
        int mWidth = mWorld[0].length;

        if (row >= 0 && row <= mHeight - 1)
            if (col >= 0 && col <= mWidth - 1){
                mWorld[col][row] = value;
            }
    }

    public void nextGenerationImpl(){

        int mHeight = mWorld.length;
        int mWidth = mWorld[0].length;

        boolean[][] nextGeneration = new boolean[mHeight][];
        for (int y = 0; y < mHeight; ++y) {
            nextGeneration[y] = new boolean[mWidth];
            for (int x = 0; x < mWidth; ++x) {
                boolean nextCell = super.computeCell(x, y);
                nextGeneration[y][x]=nextCell;
            }
        }
        mWorld = nextGeneration;
    }
}