import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;



public class Utils {
	// ===== Class Valor ===== //
	public static class Trem implements Serializable{
		final String text;
		int numberOfTweets;
		public Trem(String text) {
			this.text = text;
			this.numberOfTweets = 0;
		}
		public String toString(){
			return this.text;
		}
	}
	// ===== Class Tweet ===== //
	public static class Tweet {
		ArrayList<Trem> listOfTrems;
		int numberOfTrems;
		public Tweet(int numberOfTrems, ArrayList<Utils.Trem> listOfTrems) {
			this.listOfTrems = listOfTrems;
			this.numberOfTrems = numberOfTrems;
		}
		public String toString(){
			return " numberOfTrems: " + numberOfTrems + " trems: " + this.listOfTrems.toString() + "<<<";
		}
	}
	// ===== FILES NAMES ===== //
	public static BufferedWriter getBufferWriter(String method, int d) throws IOException{
		return new BufferedWriter(new FileWriter(Main.FilesPath +"dataReduction" + "_Method-" + method +  "_D-"+d +  ".csv"));
	}
	public static BufferedReader getBufferReader(String method, int d) throws IOException{
		return new BufferedReader(new FileReader(Main.FilesPath +"dataReduction" + "_Method-" + method +  "_D-"+d +  ".csv"));
	}
	// ===== Export ArrayList ===== //
	public static void writer (String filename, long[] x) throws IOException{
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter(Main.FilesPath+filename));
		for (int i = 0; i < x.length; i++) {
			outputWriter.write(String.valueOf(x[i]));
			outputWriter.newLine();
		}
		outputWriter.flush();
		outputWriter.close();
	}
	// ===== Serialize SortByNumberOfTweets ===== //
	public static void writerSortByNumberOfTweets(ArrayList<Utils.Trem> sortByNumberOfTweets) throws IOException{
		//serialize the List
		OutputStream file = new FileOutputStream(Main.FileTremsSortedByNumberOfTweets);
		OutputStream buffer = new BufferedOutputStream(file);
		ObjectOutput output = new ObjectOutputStream(buffer);
		
		output.writeObject(sortByNumberOfTweets);
		output.flush();
		output.close();
	}
	// ===== Deserialize SortByNumberOfTweets ===== //
	public static ArrayList<Utils.Trem> readSortByNumberOfTweets() throws IOException{
		
		//deserialize the quarks.ser file
		InputStream file = new FileInputStream(Main.FileTremsSortedByNumberOfTweets);
		InputStream buffer = new BufferedInputStream(file);
		ObjectInput input = new ObjectInputStream (buffer);
		//deserialize the List
		ArrayList<Utils.Trem> recoveredSortByNumberOfTweets = null;
		try {
			recoveredSortByNumberOfTweets = (ArrayList<Utils.Trem>)input.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recoveredSortByNumberOfTweets;
	}
	// ===== Random Number generator ===== //	
	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}
}