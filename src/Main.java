//import java.awt.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

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
		
		HashMap<String, Utils.Trem> mapa = mapcreator();
		
		//===Task 1===//
		ArrayList<Utils.Trem> sortByNumberOfTweets = task1(mapa);
		
		Utils.writerSortByNumberOfTweets(sortByNumberOfTweets);
		sortByNumberOfTweets = null;
		sortByNumberOfTweets = Utils.readSortByNumberOfTweets();
		if(sortByNumberOfTweets ==null) System.out.println(FileTremsSortedByNumberOfTweets + " FAIL");
		
		dataReduction( "frequent", 2, sortByNumberOfTweets, mapa);
		int[] j = {0,2,4,6,8,10,12,14};
		String[] methods = {"frequent","infrequent","random"};
		for(String method : methods){
			 System.out.println(">>> " + method);
			for(int d : j){
				//task2Pre( method, d, sortByNumberOfTweets, mapa);
			}
		}
		/*
		//===Task 2===//
		long[] time = {0,0,0,0,0,0,0,0};
		int s = 0;
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
		Utils.writer("BF.csv",time);
		Utils.writer("BF2.csv",time2);
		Utils.writer("BF3.csv",time3);	*/
	}

	public static HashMap<String, Utils.Trem> mapcreator() throws IOException{
		final FileReader dataFile = new FileReader(FileAllData);
		final LineNumberReader reader = new LineNumberReader(dataFile);
		
		HashMap<String, Utils.Trem> mapa = new HashMap<String, Utils.Trem>();
		for(int i = 0;i<1000;i++){
			String[] token = reader.readLine().split("\\s+");
			for(int j=0;j<token.length;j++){
				mapa.put(token[j], new Utils.Trem(new String(token[j])));
			}
		}
		reader.close();
		return mapa;
	}
	
/// ===== TASK 1 ===== ///	
	public static ArrayList<Utils.Trem> task1(HashMap mapa) throws IOException{
		final FileReader dataFile = new FileReader(FileAllData);
		final LineNumberReader reader = new LineNumberReader(dataFile);
		
		String stringLine = reader.readLine();
		String[] token = stringLine.split("\\s+");
		for(int line = 0;  (stringLine = reader.readLine()) != null && line<1000; line++){
			for(int j=0;j<token.length;j++){
				Utils.Trem obj = (Utils.Trem) mapa.get(token[j]);
				if(obj != null){
					obj.numberOfTweets++;
				}
			}
			token = stringLine.split("\\s+");
		}

		ArrayList<Utils.Trem> sortByNumberOfTweets = new ArrayList<Utils.Trem>();
		sortByNumberOfTweets.addAll(mapa.values());

		Collections.sort(sortByNumberOfTweets, new Comparator<Utils.Trem>() {
			@Override
			public int compare(Utils.Trem o1, Utils.Trem o2){
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
	public static ArrayList<Utils.Tweet> dataReduction( String method, int d, ArrayList<Utils.Trem> sortByNumberOfTweets,HashMap<String, Utils.Trem> mapa) throws IOException{
		final FileReader dataFile = new FileReader(FileAllData);
		@SuppressWarnings("resource")
		final LineNumberReader reader = new LineNumberReader(dataFile);

		ArrayList<Utils.Tweet> tweets = new ArrayList<Utils.Tweet>();
		String stringLine;
		HashMap<String, Utils.Trem>  subspaceHashMap = subspace(method, d, sortByNumberOfTweets);
		BufferedWriter outputWriter = Utils.getBufferWriter(method, d);
		
		//LINHAS
		for(int line = 0;  line<5000 && (stringLine = reader.readLine()) != null; line++){
			if(line%1000==0)System.out.println("dataReduction(): line:" + line);
			String[]  tokens = stringLine.split("\\s+");
			
			//ArrayList<Valor> listOfTrems = new ArrayList<Valor>();
			String listOfTremsSTRING = new String();
			for(int ii=0;ii<tokens.length;ii++){
				
				Utils.Trem obj = (Utils.Trem) subspaceHashMap.get(tokens[ii]);
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
	public static long task2(int Q, String method, int j, ArrayList<Utils.Trem> sortByNumberOfTweets) throws IOException{
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
		
		HashMap<String, Utils.Trem> Sub = subspace(method, j, sortByNumberOfTweets);
		
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
	public static long task3(int Q, String method, int j, ArrayList<Utils.Trem> sortByNumberOfTweets) throws IOException{
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
		
		HashMap<String, Utils.Trem>Sub = subspace(method, j, sortByNumberOfTweets);
		
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
	public static long task4(int Q, String method, int j, ArrayList<Utils.Trem> sortByNumberOfTweets) throws IOException{
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
		
		HashMap<String, Utils.Trem> Sub = subspace(method, j, sortByNumberOfTweets);
		
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
	public static double angle(String[] x, String[] y, String method, HashMap<String, Utils.Trem> subspace){
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
	public static double angle_alphabet(String[] x, String[] y, String method, HashMap<String, Utils.Trem> subspace){
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
	public static HashMap<String, Utils.Trem> subspace(String method, int d, ArrayList<Utils.Trem> sortByNumberOfTweets){
		HashMap<String, Utils.Trem> ret = new HashMap<String, Utils.Trem>();
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
			for(int rand=Utils.randInt(0, sortByNumberOfTweets.size()); randIntList.size()<D; rand=Utils.randInt(0, sortByNumberOfTweets.size())){
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
	
// ===== Export ArrayList ===== //
	public static void writerAL (String filename, ArrayList<Utils.Trem> x) throws IOException{
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter(FilesPath+filename));
		for (int i = 0; i < x.size(); i++) {
			outputWriter.write(x.get(i).toString());
			outputWriter.newLine();
		}
		outputWriter.flush();  
		outputWriter.close();  
	}
	

}
