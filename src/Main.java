//import java.awt.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
//import java.util.Map;
//import java.util.Map.Entry;
import java.util.Random;
//import java.util.TreeMap;


import javax.swing.text.StyledEditorKit.BoldAction;

//import javax.management.RuntimeErrorException;

///// ===== Algorithmic Methods of Data Mining ===== /////
///// =============== FINAL PROJECT ================ /////

public class Main {
	public static final String FilesPath = "tmp/";
	public static final String FileAllData = "tweets_15m.txt";
	public static final String FileTremsSortedByNumberOfTweets = FilesPath+"TremsSortedByNumberOfTweets";
	
	public static Boolean makingTmp;
	public static void main(String[] args) throws IOException {
		
		File theDir = new File(FilesPath);
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			theDir.mkdir();
			makingTmp = true;
		}else {
			makingTmp = false;
		}

		
		
		HashMap<String, Valor> mapa = mapcreator();
		
		//===Task 1===//
		ArrayList<Valor> sortByNumberOfTweets = task1(mapa);
		
		writerSortByNumberOfTweets(sortByNumberOfTweets);
		sortByNumberOfTweets = null;
		sortByNumberOfTweets = readSortByNumberOfTweets();
		if(sortByNumberOfTweets ==null) System.out.println(FileTremsSortedByNumberOfTweets + " FAIL");
		
		//===Task 2===//
		task2Pre( "frequent", 2, sortByNumberOfTweets, mapa);
		long[] time = {0,0,0,0,0,0,0,0};
		int s = 0;
		int[] j = {0,2,4,6,8,10,12,14};
		for(int a : j){
			time[s] = task2(100, "BF", a, sortByNumberOfTweets); // querry, method, j
			s++;
		}
		
		System.out.println("-----------TASK 3-----------------");
		
		//===Task 3==//
		long[] time2 = {0,0,0,0,0,0,0,0};
		s = 0;
		for(int a : j){
			time2[s] = task3(100, "BF", a, sortByNumberOfTweets); // querry, method, j
			s++;
		}
		
		System.out.println("-----------TASK 4-----------------");
		
		//===Task 4==//
		long[] time3 = {0,0,0,0,0,0,0,0};
		s = 0;
		for(int a : j){
			time3[s] = task4(100, "BF", a, sortByNumberOfTweets); // querry, method, j
			s++;
		}
		
		//===Export===//
		writer("BF.csv",time);
		writer("BF2.csv",time2);
		writer("BF3.csv",time3);	
	}

	public static HashMap<String, Valor> mapcreator() throws IOException{
		final FileReader dataFile = new FileReader(FileAllData);
		final LineNumberReader reader = new LineNumberReader(dataFile);
		
		HashMap<String, Valor> mapa = new HashMap<String, Valor>();
		for(int i = 0;i<1000;i++){
			String[] token = reader.readLine().split("\\s+");
			for(int j=0;j<token.length;j++){
				mapa.put(token[j], new Valor(new String(token[j])));
			}
		}
		reader.close();
		return mapa;
	}
	
/// ===== TASK 1 ===== ///	
	public static ArrayList<Valor> task1(HashMap mapa) throws IOException{
		final FileReader dataFile = new FileReader(FileAllData);
		final LineNumberReader reader = new LineNumberReader(dataFile);
		
		String stringLine = reader.readLine();
		String[] token = stringLine.split("\\s+");
		for(int line = 0;  (stringLine = reader.readLine()) != null && line<1000; line++){
			for(int j=0;j<token.length;j++){
				Valor obj = (Valor) mapa.get(token[j]);
				if(obj != null){
					obj.numberOfTweets++;
				}
			}
			token = stringLine.split("\\s+");
		}

		ArrayList<Valor> sortByNumberOfTweets = new ArrayList<Valor>();
		sortByNumberOfTweets.addAll(mapa.values());

		Collections.sort(sortByNumberOfTweets, new Comparator<Valor>() {
			@Override
			public int compare(Valor o1, Valor o2){
				if (o1.numberOfTweets < o2.numberOfTweets)
					return 1;
				else if(o1.numberOfTweets == o2.numberOfTweets)
					return o1.text.compareTo(o2.text);
				else
					return -1;
			}
		});
		reader.close();
		return sortByNumberOfTweets;
	}
	
	/// ===== PRE ===== ///
	public static ArrayList<Main.Tweet> task2Pre( String method, int d, ArrayList<Valor> sortByNumberOfTweets,HashMap<String, Valor> mapa) throws IOException{
		final FileReader dataFile = new FileReader(FileAllData);
		@SuppressWarnings("resource")
		final LineNumberReader reader = new LineNumberReader(dataFile);

		ArrayList<Main.Tweet> tweets = new ArrayList<Main.Tweet>();
		String stringLine;
		HashMap<String, Valor>  subspaceHashMap = subspace(method, d, sortByNumberOfTweets);
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter(FilesPath +"dataReduction" + "_Method-" + method +  "_D-"+d +  ".csv"));
		
		//LINHAS
		for(int line = 0;  line<5000 && (stringLine = reader.readLine()) != null; line++){
			if(line%1000==0)System.out.println("task2Pre(): line:" + line);
			String[]  tokens = stringLine.split("\\s+");
			
			//ArrayList<Valor> listOfTrems = new ArrayList<Valor>();
			String listOfTremsSTRING = new String();
			for(int ii=0;ii<tokens.length;ii++){
				
				Valor obj = (Valor) subspaceHashMap.get(tokens[ii]);
				if(obj != null){
					//listOfTrems.add(obj);
					listOfTremsSTRING += ", " + obj.text; 
				}
			}
			outputWriter.write(tokens.length + listOfTremsSTRING);
			outputWriter.newLine();
			//tweets.add(new Tweet(tokens.length, listOfTrems));
		}
		outputWriter.flush();
		outputWriter.close();
		return tweets;
	}
/// ===== TASK 2 ===== ///	
	public static long task2(int Q, String method, int j, ArrayList<Valor> sortByNumberOfTweets) throws IOException{
		final FileReader dataFile = new FileReader(FileAllData);
		final LineNumberReader reader = new LineNumberReader(dataFile);
		
		long startTime = System.currentTimeMillis();
		
		if(Q>1000 || Q<1) throw new RuntimeException();
		
		if(method.equals("BF")) System.out.println("     === BRUTE FORCE ===     ");
		else if (method.equals("frequent")) System.out.println("     === D-FREQUENT ===     ");
		else if (method.equals("infrequent")) System.out.println("     === D-INFREQUENT ===     ");
		else if (method.equals("random")) System.out.println("     === D-RANDOM ===     ");
		else throw new RuntimeException();
		
		double angle = Math.PI/2;
		int index = -1;
		String stringLine = null;
		String twt = null;
		
		for(int i=1;i<=Q;i++) stringLine = reader.readLine();
		String[] tokensX = stringLine.split("\\s+");
		System.out.println("Querry: " + stringLine);
		
		for(int k=Q; k<1000;k++) reader.readLine();
		
		HashMap<String, Valor> Sub = subspace(method, j, sortByNumberOfTweets);
		
		for(int line = 0; line<80000; line++){
			stringLine = reader.readLine();
			String[]  tokensY = stringLine.split("\\s+");
			if(line == 1243)System.out.println(">>>>>" +  stringLine); 
			double aux = angle(tokensX, tokensY, method, Sub);
			if(aux<angle){
				angle = aux;
				index = reader.getLineNumber();
				twt = stringLine;	
			}
		}
		System.out.println("Nearest: " + twt );
		System.out.println("Index of Nearest=" + index + "    Angle=" + angle);
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Time elapsed: " + totalTime + "ms");
		reader.close();
		return totalTime;
	}

/// ===== TASK 3 ===== ///
	public static long task3(int Q, String method, int j, ArrayList<Valor> sortByNumberOfTweets) throws IOException{
		final FileReader dataFile = new FileReader(FileAllData);
		final LineNumberReader reader = new LineNumberReader(dataFile);
		
		long startTime = System.currentTimeMillis();
		
		if(Q>1000 || Q<1) throw new RuntimeException();
		
		if(method.equals("BF")) System.out.println("     === BRUTE FORCE ===     ");
		else if (method.equals("frequent")) System.out.println("     === D-FREQUENT ===     ");
		else if (method.equals("infrequent")) System.out.println("     === D-INFREQUENT ===     ");
		else if (method.equals("random")) System.out.println("     === D-RANDOM ===     ");
		else throw new RuntimeException();
		
		double angle = Math.PI/2;
		int index = -1;
		String stringLine = null;
		String twt = null;
		
		for(int i=1;i<=Q;i++) stringLine = reader.readLine();
		String[] tokensX = stringLine.split("\\s+");
		System.out.println("Querry: " + stringLine);
		
		for(int k=Q; k<1000;k++) reader.readLine();
		
		HashMap<String, Valor>Sub = subspace(method, j, sortByNumberOfTweets);
		
		for(int line = 0; line<80000; line++){
			stringLine = reader.readLine();
			String[]  tokensY = stringLine.split("\\s+");
			
			double aux = angle_alphabet(tokensX, tokensY, method, Sub);
			if(aux<angle){
				angle = aux;
				index = reader.getLineNumber();
				twt = stringLine;	
			}
		}
		System.out.println("Nearest: " + twt );
		System.out.println("Index of Nearest=" + index + "    Angle=" + angle);
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Time elapsed: " + totalTime + "ms");
		reader.close();
		return totalTime;
	}
	
/// ===== TASK 4 ===== ///
	public static long task4(int Q, String method, int j, ArrayList<Valor> sortByNumberOfTweets) throws IOException{
		final FileReader dataFile = new FileReader(FileAllData);
		final LineNumberReader reader = new LineNumberReader(dataFile);
		
		long startTime = System.currentTimeMillis();
		
		if(Q>1000 || Q<1) throw new RuntimeException();
		
		if(method.equals("BF")) System.out.println("     === BRUTE FORCE ===     ");
		else if (method.equals("frequent")) System.out.println("     === D-FREQUENT ===     ");
		else if (method.equals("infrequent")) System.out.println("     === D-INFREQUENT ===     ");
		else if (method.equals("random")) System.out.println("     === D-RANDOM ===     ");
		else throw new RuntimeException();
		
		double angle = Math.PI/2;
		int index = -1;
		String stringLine = null;
		String twt = null;
		
		for(int i=1;i<=Q;i++) stringLine = reader.readLine();
		String[] tokensX = stringLine.split("\\s+");
		System.out.println("Querry: " + stringLine);
		
		for(int k=Q; k<1000;k++) reader.readLine();
		
		HashMap<String, Valor> Sub = subspace(method, j, sortByNumberOfTweets);
		
		for(int line = 0; line<80000; line++){
			stringLine = reader.readLine();
			String[]  tokensY = stringLine.split("\\s+");
			
			double aux = angle_alphabet(tokensX, tokensY, method, Sub);
			if(aux<=angle*1.5){
				angle = aux;
				index = reader.getLineNumber();
				twt = stringLine;	
			}
		}
		System.out.println("Nearest: " + twt );
		System.out.println("Index of Nearest=" + index + "    Angle=" + angle);
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Time elapsed: " + totalTime + "ms");
		reader.close();
		return totalTime;
	}
	
	
	
// ===== Angle ===== //
	public static double angle(String[] x, String[] y, String method, HashMap<String, Valor> subspace){
		int cont = 0;
		for(int i = 0; i< x.length; i++)
			for(int j = 0; j< y.length; j++)
				if(method.equals("BF")){
					if(x[i].equals(y[j])){
						cont++;
					}
				}
				else if(method.equals("frequent") || method.equals("infrequent") || method.equals("random")){
					if(x[i].equals(y[j]) && subspace.containsKey(y[j])){
						cont++;
					}
				}
				else{
					throw new RuntimeException();
				}
		return Math.acos(cont/(Math.sqrt(x.length)*Math.sqrt(y.length)));
	}

// ===== Angle Alphabet ===== //
	public static double angle_alphabet(String[] x, String[] y, String method, HashMap<String, Valor> subspace){
		int cont = 0;
		int i=0;
		int j=0;
		while(i<x.length && j<y.length){
			if(method.equals("BF")){
				if(x[i].equals(y[j])){
					cont++;
					i++;
					j++;
				}
				else{
					if(x[i].compareTo(y[j])<0) i++;  
					else j++;
				}
			}
			else if(method.equals("frequent") || method.equals("infrequent") || method.equals("random")){
				if(x[i].equals(y[j]) && subspace.containsKey(y[j])){
					cont++;
					i++;
					j++;
				}
				else{
					if(x[i].compareTo(y[j])<0) i++;  
					else j++;
				}
			}
			else{
				throw new RuntimeException();
			}
		}
		return Math.acos(cont/(Math.sqrt(x.length)*Math.sqrt(y.length)));
	}
	
// ===== Generate Subspaces ===== //
	public static HashMap<String, Valor> subspace(String method, int d, ArrayList<Valor> sortByNumberOfTweets){
		HashMap<String, Valor> ret = new HashMap<String, Valor>();
		int D = 100*2^d;
		if(method.equals("frequent")){
			for(int i=0; i<D; i++)
				ret.put(sortByNumberOfTweets.get(i).text,sortByNumberOfTweets.get(i));
		}
		else if(method.equals("infrequent")){
			for(int i =0; i<D; i++)
				ret.put(sortByNumberOfTweets.get(sortByNumberOfTweets.size()-i-1).text,sortByNumberOfTweets.get(sortByNumberOfTweets.size()-i-1));
		}
		else if(method.equals("random")){
			ArrayList<Integer> randIntList = new ArrayList<Integer>();
			for(int rand=randInt(0, sortByNumberOfTweets.size()); randIntList.size()<D; rand=randInt(0, sortByNumberOfTweets.size())){
				if(!randIntList.contains(rand));
					randIntList.add(rand);
			}
			for(int i=0;i<D;i++){
				ret.put(sortByNumberOfTweets.get(randIntList.get(i)).text,sortByNumberOfTweets.get(randIntList.get(i)));
			}
		}
		else if(method.equals("BF")){}
		else throw new RuntimeException();
		return ret;
	}

// ===== Class Valor ===== //
	public static class Valor implements Serializable{
		final String text;
		int numberOfTweets;
		public Valor(String text) {
			this.text = text;
			this.numberOfTweets = 0;
		}
		public String toString(){
			return this.text;
		}
	}
// ===== Class Tweet ===== //
	public static class Tweet {
		ArrayList<Valor> listOfTrems;
		int numberOfTrems;
		public Tweet(int numberOfTrems, ArrayList<Main.Valor> listOfTrems) {
			this.listOfTrems = listOfTrems;
			this.numberOfTrems = numberOfTrems;
		}
		public String toString(){
			return " numberOfTrems: " + numberOfTrems + " trems: " + this.listOfTrems.toString() + "<<<";
		}
	}
	
// ===== Export ArrayList ===== //
	public static void writerAL (String filename, ArrayList<Valor> x) throws IOException{
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter(FilesPath+filename));
		for (int i = 0; i < x.size(); i++) {
			outputWriter.write(x.get(i).toString());
			outputWriter.newLine();
		}
		outputWriter.flush();  
		outputWriter.close();  
	}
	
// ===== Export ArrayList ===== //
	public static void writer (String filename, long[] x) throws IOException{
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter(FilesPath+filename));
		for (int i = 0; i < x.length; i++) {
			outputWriter.write(String.valueOf(x[i]));
			outputWriter.newLine();
		}
		outputWriter.flush();
		outputWriter.close();
	}
// ===== Serialize SortByNumberOfTweets ===== //
	public static void writerSortByNumberOfTweets(ArrayList<Valor> sortByNumberOfTweets) throws IOException{
		//serialize the List
		OutputStream file = new FileOutputStream(FileTremsSortedByNumberOfTweets);
		OutputStream buffer = new BufferedOutputStream(file);
		ObjectOutput output = new ObjectOutputStream(buffer);
		
		output.writeObject(sortByNumberOfTweets);
		output.flush();
		output.close();
	}
// ===== Deserialize SortByNumberOfTweets ===== //
	public static ArrayList<Valor> readSortByNumberOfTweets() throws IOException{
		
		//deserialize the quarks.ser file
		InputStream file = new FileInputStream(FileTremsSortedByNumberOfTweets);
		InputStream buffer = new BufferedInputStream(file);
		ObjectInput input = new ObjectInputStream (buffer);
		//deserialize the List
		ArrayList<Valor> recoveredSortByNumberOfTweets = null;
		try {
			recoveredSortByNumberOfTweets = (ArrayList<Valor>)input.readObject();
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
