package uk.ac.cam.vap32;

import java.io.*;
import java.net.*;
import java.util.*;

public class PatternStore{

    private List<Pattern> mPatterns = new LinkedList<>();
    private Map<String,List<Pattern>> mMapAuths = new HashMap<>();
    private Map<String,Pattern> mMapName = new HashMap<>();

    public PatternStore(String source) throws IOException{
        if (source.startsWith("http://") || source.startsWith("https://")) {
            loadFromURL(source);
        }
        else {
            loadFromDisk(source);
        }

		/*print(getPatternAuthors());
		print(getPatternNames());
		printPatternNames(getPatternsAuthorSorted());
		printPatternNames(getPatternsByAuthor("life lexicon"));
		System.out.println(getPatternByName("phi").getName());*/
    }

    public PatternStore(Reader source) throws IOException {
        load(source);
    }

    public Pattern getPatternAt(int val){
        return mPatterns.get(val);
    }

    private void load(Reader r) throws IOException {

        BufferedReader b = new BufferedReader(r);
        String line = b.readLine();

        while (line != null){
            //System.out.println(line);

            try{

                Pattern myPattern = new Pattern(line);

                mPatterns.add(myPattern);

                List<Pattern> myList = mMapAuths.get(myPattern.getAuthor());

                if (myList == null){
                    myList = new LinkedList<Pattern>();
                }

                myList.add(myPattern);

                mMapAuths.put(myPattern.getAuthor(), myList);

				/*System.out.print(myPattern.getAuthor() + " ");
				for (int i=0; i<myList.size(); i++)
					System.out.print(myList.get(i).getName() + " "); System.out.println();*/

                mMapName.put(myPattern.getName(), myPattern);
            }
            catch(PatternFormatException e){
                if (e.getMessage().startsWith("Invalid pattern format: Malformed"))
                    System.out.println(e.getMessage());
                else{
                    System.out.println(e.getMessage());
                    System.exit(0);
                }
            }

            line = b.readLine();
        }
    }


    private void loadFromURL(String url) throws IOException {
        URL destination = new URL(url);
        URLConnection conn = destination.openConnection();
        Reader r = new InputStreamReader(conn.getInputStream());
        load(r);
    }

    private void loadFromDisk(String filename) throws IOException {

        Reader r = new FileReader(filename);
        load(r);
    }

    public List<Pattern> getPatternsNameSorted() {

        List<Pattern> sortedList = new LinkedList<>();

        sortedList.addAll(mPatterns);

        Collections.sort(sortedList);

        return sortedList;
    }

    public List<Pattern> getPatternsAuthorSorted() {

        List<Pattern> sortedList = new LinkedList<>();

        sortedList.addAll(mPatterns);

        Collections.sort(sortedList, new Comparator<Pattern>(){
            public int compare(Pattern p1, Pattern p2) {
                //return (p1.getAuthor()).compareTo(p2.getAuthor());

                if (p1.getAuthor().compareTo(p2.getAuthor()) == 0)
                    return p1.getName().compareTo(p2.getName());

                return (p1.getAuthor()).compareTo(p2.getAuthor());
            }
        });

        return sortedList;
    }

    public List<Pattern> getPatternsByAuthor(String author) {

        List<String> myStringList = new LinkedList<String>();
        List<Pattern> myAuxPatternList = mMapAuths.get(author); // just a reference
        List<Pattern> myPatternList = new LinkedList<Pattern>();

        myPatternList.addAll(myAuxPatternList);

        Collections.sort(myPatternList);

        return myPatternList;
    }

    public Pattern getPatternByName(String name) throws PatternNotFound {

        Pattern result = mMapName.get(name);

        if (result == null)
            throw new PatternNotFound("No such name was found.");

        return mMapName.get(name);
    }

    public List<String> getPatternAuthors() {

        List<String> result = new LinkedList<String>();
        List<Pattern> sortedByAuthor = getPatternsAuthorSorted();

        for (int i=0; i<sortedByAuthor.size(); i++){

            while (i <sortedByAuthor.size() - 1 && sortedByAuthor.get(i).getAuthor().equals(sortedByAuthor.get(i+1).getAuthor()))
                i++;   // REMOVING DUPLICATES

            result.add(sortedByAuthor.get(i).getAuthor());
        }

        return result;
    }

    public List<String> getPatternNames() {
        // sorted by name

        List<String> result = new LinkedList<String>();
        List<Pattern> sortedByName = getPatternsNameSorted();

        for (int i=0; i<sortedByName.size(); i++){
            result.add(sortedByName.get(i).getName());
        }

        return result;
    }

    private void print(List<String> myList){
        for (int i=0; i<myList.size(); i++)
            System.out.print(myList.get(i) + " ");

        System.out.println();
    }

    private void printPatternNames(List<Pattern> myList){
        for (int i=0; i<myList.size(); i++)
            System.out.print(myList.get(i).getName() + " ");

        System.out.println();
    }

   /*public static void main(String args[]) throws java.io.IOException, PatternFormatException, PatternNotFound {
      PatternStore p =
       new PatternStore("http://www.cl.cam.ac.uk/teaching/current/OOProg/ticks/lifetest.txt");


   }*/
}