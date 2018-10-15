package uk.ac.cam.vap32;

public class PackedWorld extends World implements Cloneable{

    private long mWorld;

    @Override
    public Object clone() throws CloneNotSupportedException{
        PackedWorld copy = (PackedWorld)super.clone();
        return copy;
    }

    public PackedWorld(PackedWorld toCopy) throws CloneNotSupportedException {
        super(toCopy);
        this.mWorld = toCopy.mWorld;
    }

    public PackedWorld(String format) throws PatternFormatException{
        super(format);

        getPattern().initialise(this);
    }

    public PackedWorld(Pattern p) throws PatternFormatException{
        super(p);

        p.initialise(this);
    }

    private long set(long packed, int position, boolean value) {
        if (value) {
            packed |= (1L<<position);
        }
        else {
            packed &= ~(1L<<position);
        }
        return packed;
    }

    private static boolean get(long packed, int position) {

        long check = ((packed >> position) & 1L);

        return (check == 1);
    }

    public boolean getCell(int col, int row) {

        int mHeight = super.getPattern().getHeight();
        int mWidth = super.getPattern().getWidth();

        if (row < 0 || row >= mHeight) return false;
        if (col < 0 || col >= mWidth) return false;

        int position = col + 8 * row;

        return get(mWorld, position);
    }

    public void setCell(int col, int row, boolean value){

        int mHeight = super.getPattern().getHeight();
        int mWidth = super.getPattern().getWidth();

        if (row >= 0 && row <= mHeight - 1)
            if (col >= 0 && col <= mWidth - 1){
                mWorld = set(mWorld, col + 8 * row, value);
            }
    }

    public void nextGenerationImpl(){

        int mHeight = super.getPattern().getHeight();
        int mWidth = super.getPattern().getWidth();

        long copy = 0L;
        long initial = mWorld;

        for (int i=0; i<mHeight; i++){
            for (int j=0; j<mWidth; j++){

                setCell(j, i, super.computeCell(j, i));
                copy = set(copy, j + 8*i, get(mWorld, j + 8*i));
                mWorld = initial;

            }
        }

        mWorld = copy;
    }

}