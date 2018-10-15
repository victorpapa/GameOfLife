package uk.ac.cam.vap32;

public class Pattern implements Cloneable, Comparable<Pattern>{

    private String mName;
    private String mAuthor;
    private int mWidth;
    private int mHeight;
    private int mStartCol;
    private int mStartRow;
    private String mCells;

    @Override
    public String toString(){
        return this.getName() + " (" + this.getAuthor() + ")";
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int compareTo(Pattern o) {

        return this.mName.compareTo(o.getName());
    }

    public String getName() {
        return mName;
    }

    public String getAuthor(){
        return mAuthor;
    }

    public int getWidth(){
        return mWidth;
    }

    public int getHeight(){
        return mHeight;
    }

    public int getStartCol(){
        return mStartCol;
    }

    public int getStartRow(){
        return mStartRow;
    }

    public String getCells(){
        return mCells;
    }

    private boolean checkText(String text){
        for (int i=0; i<text.length(); i++){
            if (!(text.charAt(i) >= '0' && text.charAt(i) <= '9'))
                return false;
        }

        return true;
    }

    private boolean checkStringArraySix(String text){
        for (int i=0; i<text.length(); i++){
            if (!(text.charAt(i) >= '0' && text.charAt(i) <= '9') && text.charAt(i) != ' ')
                return false;
        }

        return true;
    }

    public Pattern(String format) throws PatternFormatException{

        String[] stringArray = format.split(":");

        //System.out.println(format);
        if (format.equals("")){
            throw new PatternFormatException("Please specify a pattern.");
        }

        if (stringArray.length != 7){
            throw new PatternFormatException("Invalid pattern format: Incorrect number of fields in pattern (found " + stringArray.length + ").");
        }


        mName = stringArray[0];
        mAuthor = stringArray[1];


        /**********************************************************************************************************/

        boolean c = checkText(stringArray[2]);

        if (!c)
            throw new PatternFormatException("Invalid pattern format: Could not interpret the width field as a number ('" + stringArray[2] + "').");

        mHeight = Integer.parseInt(stringArray[2]);

        /**********************************************************************************************************/

        c = checkText(stringArray[3]);

        if (!c)
            throw new PatternFormatException("Invalid pattern format: Could not interpret the width field as a number ('" + stringArray[3] + "').");

        mWidth = Integer.parseInt(stringArray[3]);

        /**********************************************************************************************************/

        c = checkText(stringArray[4]);

        if (!c)
            throw new PatternFormatException("Invalid pattern format: Could not interpret the width field as a number ('" + stringArray[4] + "').");

        mStartRow = Integer.parseInt(stringArray[4]);

        /**********************************************************************************************************/

        c = checkText(stringArray[5]);

        if (!c)
            throw new PatternFormatException("Invalid pattern format: Could not interpret the width field as a number ('" + stringArray[5] + "').");

        mStartCol = Integer.parseInt(stringArray[5]);

        /**********************************************************************************************************/

        c = checkStringArraySix(stringArray[6]);

        if (!c)
            throw new PatternFormatException("Invalid pattern format: Malformed pattern '" + stringArray[6] + "'.");


        mCells = stringArray[6];

        /**********************************************************************************************************/
    }

    public void initialise(World world) {

        String[] coordinates = mCells.split(" ");


        for (int i=0; i<coordinates.length; i++){
            for (int j=0; j<coordinates[i].length(); j++){

                char x = coordinates[i].charAt(j);
                boolean isAlive = (x == '1');

                world.setCell(mStartCol + j, mStartRow + i, isAlive);

            }
        }


    }
}