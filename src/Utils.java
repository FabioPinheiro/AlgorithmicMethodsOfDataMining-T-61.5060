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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class Utils {
	
	// ===== Class Term ===== //
	public static class Term implements Serializable{
		final String text;
		int numberOfTweets;
		public Term(String text) {
			this.text = text;
			this.numberOfTweets = 0;
		}
		public String toString(){
			return this.text + "," + this.numberOfTweets;
		}
	}
	
	// ===== Export ArrayList of Terms ===== //
		public static void writerALTerms (String filename, ArrayList<Term> x) throws IOException{
			BufferedWriter outputWriter = new BufferedWriter(new FileWriter(filename));
			for (int i = 0; i < x.size(); i++) {
				outputWriter.write(x.get(i).toString());
				outputWriter.newLine();
			}
			outputWriter.flush();  
			outputWriter.close();  
		}
		
	// ===== Stuff ===== // 
		public static final String[] Methods = {"BruteForce","frequent","infrequent","random"};
		public static final int[] J = {0,2,4,6,8,10,12,14};	
			
	// ===== Class Tweet ===== //
	public static class Tweet {
		int index;
		int numberOfTerms;
		ArrayList<String> listOfTerms;
		public Tweet(int index, int numberOfTerms, ArrayList<String> listOfTerms) {
			this.index = index;
			this.numberOfTerms = numberOfTerms;
			this.listOfTerms = listOfTerms;
		}
		public String toString(){
			return "Number Of Terms: " + this.numberOfTerms + " Terms: " + this.listOfTerms.toString();
		}
	}
	
	// ===== Class State ===== //
	public static class State implements Serializable{
		private static boolean[][] makeFiles = new boolean[4][8];
		private static long[][][] timeTasks = new long [3][4][8];
		private static int tasksToInt(int t){
			return t-2;
		}
		private static int methodsToInt(String method){
			for (int i=0;i<=Methods.length;i++)
				if(Methods[i].equals(method))
					return i;
			throw new RuntimeException();
		}
		private static int jToInt(int j){
			for (int i=0;i<=J.length;i++)
				if(J[i]==j)
					return i;
			throw new RuntimeException();
		}
		public static void fileCreated(String method, int j) throws IOException{
			makeFiles[methodsToInt(method)][jToInt(j)]=true;
			save();
		}
		public static void setTime(int tasks, String method, int j,long time) throws IOException{
			timeTasks[tasksToInt(tasks)][methodsToInt(method)][jToInt(j)]=time;
			save();
		}
		public static boolean isfileCreated(String method, int j){
			return makeFiles[methodsToInt(method)][jToInt(j)];
		}
		public static Boolean isTimeSet(int tasks, String method, int j){
			System.out.println(" === The task:" + tasks +
					" using method " + method +
					" with j=" + j + "   ->  " +
					(timeTasks[tasksToInt(tasks)][methodsToInt(method)][jToInt(j)]==0 ? " TODO " : " DONE ")
						+ " === ");
			return timeTasks[tasksToInt(tasks)][methodsToInt(method)][jToInt(j)]!=0;
		}
		public static void save() throws IOException{
			System.out.println("State:saving");
			BufferedWriter outputWriter = new BufferedWriter(new FileWriter(Main.FilesPath+Main.FileState));
			for(int i = 0; i < 3; i++) {
				String aux = new String();
				for(int j = 0; j < 8; j++)
					aux += makeFiles[i][j] + " ";
				outputWriter.write(aux);
				outputWriter.newLine();
			}
			outputWriter.write("TASKS");
			outputWriter.newLine();
			for(int k = 0; k < 3; k++) {
				for(int i = 0; i < 3; i++){
					String aux = new String();
					for(int j = 0; j < 8; j++)
						aux += timeTasks[k][i][j]  + " ";
					outputWriter.write(aux);
					outputWriter.newLine();
				}
			}
			outputWriter.flush();
			outputWriter.close();
		}
		public static void load() throws IOException{
			System.out.println("State:loading");
			BufferedReader bufferedReader = new BufferedReader(new FileReader(Main.FilesPath+Main.FileState));
			
			for(int i = 0; i < 3; i++) {
				String stringLine = bufferedReader.readLine();
				if(stringLine == null){
					bufferedReader.close();
					throw new RuntimeException();
				}
				String[]  tokens = stringLine.split("\\s+");
				for(int j = 0; j < 8; j++){
					makeFiles[i][j] = Boolean.parseBoolean(tokens[j]);
				}
			}
			
			bufferedReader.readLine(); //outputWriter.write("TACKS");
			
			for(int k = 0; k < 3; k++) {
				for(int i = 0; i < 3; i++){
					String stringLine = bufferedReader.readLine();
					if(stringLine == null){
						bufferedReader.close();
						throw new RuntimeException();
					}
					String[]  tokens = stringLine.split("\\s+");
					for(int j = 0; j < 8; j++)
						timeTasks[k][i][j] = Long.parseLong(tokens[j]);
				}
			}
			bufferedReader.close();
		}
		public static void print() throws IOException{
			System.out.println("State:printing");
			BufferedWriter outputWriter2 = new BufferedWriter(new FileWriter(Main.FilesPath+"Task2.csv"));
			BufferedWriter outputWriter3 = new BufferedWriter(new FileWriter(Main.FilesPath+"Task3.csv"));
			BufferedWriter outputWriter4 = new BufferedWriter(new FileWriter(Main.FilesPath+"Task4.csv"));
			
			for(int i = 0; i < 4; i++){
				String aux = new String();
				for(int j = 0; j < 8; j++)
					aux += timeTasks[0][i][j]  + (j!=7 ? ", " : "");
				outputWriter2.write(aux);
				outputWriter2.newLine();
			}
			for(int i = 0; i < 4; i++){
				String aux = new String();
				for(int j = 0; j < 8; j++)
					aux += timeTasks[1][i][j]  + (j!=7 ? ", " : "");
				outputWriter3.write(aux);
				outputWriter3.newLine();
			}
			for(int i = 0; i < 4; i++){
				String aux = new String();
				for(int j = 0; j < 8; j++)
					aux += timeTasks[2][i][j]  + (j!=7 ? ", " : "");
				outputWriter4.write(aux);
				outputWriter4.newLine();
			}
				
			outputWriter2.flush();
			outputWriter2.close();
			outputWriter3.flush();
			outputWriter3.close();
			outputWriter4.flush();
			outputWriter4.close();
		}
	}
	
	// ===== Class Data ===== //
	public static class Data {
		BufferedReader buffer;
		public Data(String method, int d) throws IOException {
			buffer = getBufferReader(method, d);
		}
		public Tweet getNextTweet() throws IOException{
			ArrayList<String> listOfTerms = new ArrayList<String>();
			String stringLine = buffer.readLine();
			if(stringLine == null){
				buffer.close();
				return null;
			}
			String[] tokens = stringLine.split("\\s+");
			int index = Integer.parseInt(tokens[0]);
			int numberOfTerms = Integer.parseInt(tokens[1]);
			for(int ii=2;ii<tokens.length;ii++){
				listOfTerms.add(tokens[ii]);
			}
			return new Tweet(index, numberOfTerms, listOfTerms);
		}
		public Tweet getNextOptimisticTweet(double angle) throws IOException{
			ArrayList<String> listOfTerms = new ArrayList<String>();
			for (;;){
				String stringLine = buffer.readLine();
				if(stringLine == null){
					buffer.close();
					return null;
				}
				String[]  tokens = stringLine.split("\\s+");
				int index = Integer.parseInt(tokens[0]);
				int numberOfTrems = Integer.parseInt(tokens[1]);
				if(Main.angleTweetOptimistic(numberOfTrems, tokens.length-2)<=angle){
					for(int ii=2;ii<tokens.length;ii++){
						listOfTerms.add(tokens[ii]);
					}
					return new Tweet(index, numberOfTrems, listOfTerms);
				}//else continue;
			}
		}
	}
	
	// ===== FILES NAMES ===== //
	public static BufferedWriter getBufferWriter(String method, int d) throws IOException{
		return new BufferedWriter(new FileWriter(Main.FilesPath +"dataReduction" + "_Method:" + method +  "_D:"+d ));
	}
	public static BufferedReader getBufferReader(String method, int d) throws IOException{
		return new BufferedReader(new FileReader(Main.FilesPath +"dataReduction" + "_Method:" + method +  "_D:"+d ));
	}
	public static void deleteData(String method, int d) throws IOException{
		Files.deleteIfExists(Paths.get(Main.FilesPath +"dataReduction" + "_Method:" + method +  "_D:"+d ));
	}
	
	// ===== Export ArrayList ===== //
	public static void writer(String filename, long[] x) throws IOException{
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter(Main.FilesPath+filename));
		for (int i = 0; i < x.length; i++) {
			outputWriter.write(String.valueOf(x[i]));
			outputWriter.newLine();
		}
		outputWriter.flush();
		outputWriter.close();
	}

	// ===== Random Number generator ===== //	
	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}
}





// ======= GARBAGE ======= //

//// ===== Serialize SortByNumberOfTweets ===== //
//public static void writerSortByNumberOfTweets(ArrayList<String> sortByNumberOfTweets) throws IOException{
//	//serialize the List
//	OutputStream file = new FileOutputStream(Main.FileTermsSortedByNumberOfTweets);
//	OutputStream buffer = new BufferedOutputStream(file);
//	ObjectOutput output = new ObjectOutputStream(buffer);
//	
//	output.writeObject(sortByNumberOfTweets);
//	output.flush();
//	output.close();
//}
//// ===== Deserialize SortByNumberOfTweets ===== //
//public static ArrayList<String> readSortByNumberOfTweets() throws IOException{
//	
//	//deserialize the quarks.ser file
//	InputStream file = new FileInputStream(Main.FileTermsSortedByNumberOfTweets);
//	InputStream buffer = new BufferedInputStream(file);
//	ObjectInput input = new ObjectInputStream (buffer);
//	//deserialize the List
//	ArrayList<String> recoveredSortByNumberOfTweets = null;
//	try {
//		recoveredSortByNumberOfTweets = (ArrayList<String>)input.readObject();
//	} catch (ClassNotFoundException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	input.close();
//	return recoveredSortByNumberOfTweets;
//}
