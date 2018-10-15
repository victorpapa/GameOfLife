package uk.ac.cam.vap32;

public abstract class World implements Cloneable{

    //Fields

    private int mGeneration;
    private Pattern mPattern;

    //Methods

    @Override
    public Object clone() throws CloneNotSupportedException{

        World copy = (World) super.clone();
        copy.mPattern = (Pattern) mPattern.clone();

        return copy;
    }

    public World(World toCopy) throws CloneNotSupportedException {
        this.mGeneration = toCopy.mGeneration;
        this.mPattern = (Pattern)toCopy.mPattern.clone();
    }

    public World(String s) throws PatternFormatException{
        this.mGeneration = 0;
        this.mPattern = new Pattern(s);
    }

    public World(Pattern p) throws PatternFormatException{
        this.mGeneration = 0;
        this.mPattern = p;
    }

    public abstract boolean getCell(int c, int r);
    public abstract void setCell(int c, int r, boolean val);

    public Pattern getPattern(){
        return this.mPattern;
    }

    public int getWidth(){
        return mPattern.getWidth();
    }

    public int getHeight(){
        return mPattern.getHeight();
    }

    public int getGenerationCount(){
        return mGeneration;
    }

    protected void incrementGenerationCount(){
        mGeneration ++;
    }

    public int countNeighbours(int col, int row){

        int mHeight = mPattern.getHeight();
        int mWidth = mPattern.getWidth();

        if (row < 0 || row > mHeight - 1) return 0;
        if (col < 0 || col > mWidth - 1) return 0;

        int result = 0;

        for (int i=-1; i<=1; i++){
            for (int j=-1; j<=1; j++){
                if (!(i == 0 && j == 0)){
                    int newRow = row + i;
                    int newCol = col + j;

                    if (newRow >= 0 && newRow <= mHeight - 1)
                        if (newCol >= 0 && newCol <= mWidth - 1){
                            if (getCell(newCol, newRow) == true)
                                result ++;
                        }
                }
            }
        }

        return result;
    }

    protected boolean computeCell(int col, int row){

        boolean liveCell = getCell(col, row);
        boolean nextCell = false;
        int neighbours = countNeighbours(col, row);

        if (liveCell == true){
            if (neighbours == 2 || neighbours == 3)
                nextCell = true;
        }

        else

        if (neighbours == 3)
            nextCell = true;

        //if (nextCell == true) System.out.println(row + " " + col);

        return nextCell;
    }

    public void nextGeneration(){
        nextGenerationImpl();
        mGeneration++;
    }

    public abstract void nextGenerationImpl();
}