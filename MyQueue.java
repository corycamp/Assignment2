import java.io.*;
import java.util.*;

//Cory Campbell
//CISC 3130

//For overview of code, see the last lines in the program


//Queue used to store the Files in order
public class MyQueue extends LinkList {
    protected String filename;
    protected Artist first;
    protected Artist last;
    protected int size;

    public MyQueue() {
        this.first = this.last = null;
    }

    public void enqueue(String filename){
        Artist temp = new Artist(filename,null);
        if (last == null) {
            this.first = this.last = temp;
        }
        else{
            this.last.next = temp;
            last = temp;
        }
        size++;
    }

    public void dequeue(){
        if(this.first == null)
            return;
        Artist temp = first;
        this.first = this.first.next;
        if(this.first == null)
            this.last = null;
        size--;
    }

    public String front (){
        Artist temp = first;
        this.dequeue();
        return temp.getData();
    }

    public String rear(){
        Artist temp = first;
        while(temp.next != null){
            temp = temp.next;
        }
        last = null;
        size--;
        return temp.getData();
    }

    public boolean isEmpty() {
        if (this.first == null)
            return true;
        else
            return false;
    }

    public int getSize(){
        return size;
    }
}
//Stack class used for the play and playlist method
class Stack extends LinkList{
    Artist top;

    public Stack(){
        top = null;
    }

    public void push(String data){
        Artist temp = new Artist(data,null);
        if(top == null){
           top = temp;
        }
        else{
            temp.next = top;
            top = temp;
        }
    }

    public String pop(){
        Artist temp = top;
        if(!(this.isEmpty()))
            top = top.next;
            return temp.getData();
    }

    public boolean isEmpty(){
        if(top == null)
            return true;
        else
            return false;
    }
}
class LinkList{
    protected Artist first;
    protected Artist head;
    protected int size;

    public LinkList(){
        first = null;
        head = null;
        size = 0;
    }
    //Adds new data to Link List
    public void insertValue(String data){
        Artist temp = new Artist(data,null);
        if(first == null){
            first = temp;
        }
        else{
            head = temp;
            head.next = first;
            first = head;
        }
        size++;
    }
    //Displays all of the Link List properties
    public void displayValue(){
        if(size == 0){
            System.out.println("List is empty");
            return;
        }
        if(first.getLink() == null){
            System.out.println(first.getData());
            return;
        }
        Artist temp = new Artist();
        temp = first;
        System.out.println(temp.getData());
        temp = temp.getLink();
        while(temp.getLink() != null){
            System.out.println(temp.getData());
            temp = temp.getLink();
        }
        System.out.println(temp.getData());
    }
}
//Node Class to be used by the Link List
class Artist{
    protected String data;
    protected Artist next;

    public Artist(){
        next = null;
        data = null;
    }
    public Artist(String data, Artist n){
        this.data = data;
        next = n;
    }

    public void setValue(String data){
        this.data = data;
    }
    public void setLink(Artist n){
        next = n;
    }
    public String getData(){
        return data;
    }
    public Artist getLink(){
        return next;
    }
}
class Records{
    public static void main(String [] args) throws Exception {
        int size;
        PrintWriter printer = new PrintWriter("D:\\Assignment#2\\src\\Music.txt");
        ArrayList<File> allweeks = new ArrayList<>();
        String filename;
        MyQueue weeks = new MyQueue();
        Stack list;
        File file = new File("D:\\Assignment#2\\src\\Music");
        File[] weekFiles = file.listFiles();

        for (File x : weekFiles) {
            filename = x.toString();
            weeks.enqueue(filename);
        }
        ascendingOrder(weeks);

        File file2 = new File("D:\\Assignment#2\\src\\SortedFiles");
        String[] sorted = file2.list();

        //Puts all the weekly files into one merged file
        for (String x : sorted) {
            File f = new File(file2, x);

            // create object of BufferedReader
            BufferedReader br = new BufferedReader(new FileReader(f));
            // Read from current file
            String line = br.readLine();
            while (line != null) {

                // write to the output file
                printer.println(line);
                line = br.readLine();
            }
        }
        File all = new File("D:\\Assignment#2\\src\\Music.txt");
        list = playlist(all);
        Stack history = play(list);
    }
    public static Stack play(Stack list){
        Stack history = new Stack();
        while(!(list.isEmpty())){
            Stack temp = list;
            temp.pop();
            history.push(list.pop());
        }
        return history;
    }

    //Places all the songs in a stack
    public static Stack playlist(File all)throws Exception{
        BufferedReader f = new BufferedReader(new FileReader(all));
        Stack list = new Stack();
        String line = f.readLine();
        while(line != null){
            list.push(line);
            line = f.readLine();
        }
        return list;
    }

    //Sorts all the data, in the files in the queue, in alphabetical order
    public static void ascendingOrder(MyQueue weeks)throws Exception{
        boolean test = false;
        int count = 1;
        while(test == false){
            PrintStream file = new PrintStream("D:\\Assignment#2\\src\\SortedFiles\\SortedList " + count + ".txt" );
            Scanner sc = new Scanner(new FileReader(weeks.front()));
            String input;
            int row = 0;
            String[][] arr = new String[199][10];
            String[] names = new String[199];
            String[] nameHolder = new String[199];
            String[] final_list = new String[199];
            input = sc.nextLine();

            //Check to see if line has a integer value/position at beginning to avoid reading unnecessary lines
            //Populates the arrays with lines from CSV files
            while (sc.hasNext()) {
                if (!(Character.isDigit(input.charAt(0)))) {
                    input = sc.nextLine();
                } else if ((Character.isDigit(input.charAt(0)))) {

                    String[] sep = input.split(",");
                    for (int i = 0; i < sep.length; i++) {
                        arr[row][i] = sep[i];
                    }
                    input = sc.nextLine();
                    row++;
                }
            }
            // Removes the quotations around names and then places them into an array
            for (int i = 0; i < arr.length; i++) {
                if (arr[i][1] != null) {
                    nameHolder[i] = arr[i][1];
                    if (nameHolder[i].charAt(0) == '\"') {
                        names[i] = nameHolder[i].substring(1, nameHolder[i].length() - 1).trim();
                    } else {
                        names[i] = nameHolder[i];
                    }
                }
            }
            // Sorts the Songs into alphabetical order
            for (int i = 0; i < names.length; i++) {
                for (int j = i + 1; j < names.length; j++) {
                    names[i] = names[i].trim();
                    String[] after = new String[199];
                    String[] before = new String[199];
                    before[i] = names[i].toLowerCase();
                    after[i] = names[j].toLowerCase();
                    if (before[i].compareTo(after[i]) > 0) {
                        String temp = names[i];
                        names[i] = names[j];
                        names[j] = temp;
                    }
                }
                file.println(names[i]);
            }
            file.close();
        test = weeks.isEmpty();
        count++;
        }
    }
}
/*
    LinkList, Node(Artist), Queue and Stack classes were created and some reused from old programs, for this new Assignment.
    The program creates a queue of all the files stored in a specific document. The amount of files that the program takes,
    automatically increased by simply placing the new file in the folder in the Music directory.
    The queue is then sent to a method that organizes the songs in the files in ascending order and stores them in a Folder.
    The sorted files are then merged together into one text file, which is then used to create a playlist.
    To create the playlist, the file is read into a stack.
    The stack can then be used in the play method which pops the songs, as if they were being played as the current song.
    When a song is popped, it is simultaneously being pushed into another stack named history, which tracks the songs that have already been played.
 */