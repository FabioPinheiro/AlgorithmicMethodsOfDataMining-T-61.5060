//import java.awt.List;
import java.io.BufferedReader;
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
	public static final Boolean UseAllData = false;
	public static final Integer QuerrySize  = 1000;
	public static final String FilesPath = "tmp/";
	public static final String FileAllData = "tweets_15m.txt";
	public static final String FileState = "State";
	public static final String FileTremsSortedByNumberOfTweets = FilesPath+"TremsSortedByNumberOfTweets";
	
	public static void main(String[] args) throws IOException {
		
		File theDir = new File(FilesPath);
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			theDir.mkdir();
			Utils.State.save();
		}else {
			Utils.State.load();
		}
		
		HashMap<String, Utils.Trem> mapa = mapcreator();
		
		//===Task 1===//
		ArrayList<String> sortByNumberOfTweets = task1(mapa);
		
		Utils.writerSortByNumberOfTweets(sortByNumberOfTweets);
		sortByNumberOfTweets = null;
		sortByNumberOfTweets = Utils.readSortByNumberOfTweets();
		if(sortByNumberOfTweets ==null) System.out.println(FileTremsSortedByNumberOfTweets + " FAIL");

		for(String method : Utils.Methods){
			 System.out.println(">>> " + method);
			for(int j : Utils.J){
				if(method.equals("BF")) System.out.println("     === BRUTE FORCE ===     ");
				else if (method.equals(Utils.Methods[0])) System.out.println("     === D-FREQUENT ===     ");
				else if (method.equals(Utils.Methods[1])) System.out.println("     === D-INFREQUENT ===     ");
				else if (method.equals(Utils.Methods[2])) System.out.println("     === D-RANDOM ===     ");
				else throw new RuntimeException();
				if(! Utils.State.isfileCreated(method, j)){
					dataReduction( method, j, sortByNumberOfTweets, mapa);
					Utils.State.fileCreated(method, j);
				}
			}
		}
		
		System.out.println("-----------TASK 2-----------------");
		for(String method : Utils.Methods){
			for(int d : Utils.J){
				if(! Utils.State.isTimeSet(2, method, d)){
					long time = task2(method, d);
					Utils.State.setTime(2, method, d, time);;
				}
				
			}
		}
		
		System.out.println("-----------TASK 3-----------------");
		
		for(String method : Utils.Methods){
			for(int d : Utils.J){
				if(! Utils.State.isTimeSet(3, method, d)){
					long time = task3(method, d);
					Utils.State.setTime(3, method, d, time);;
				}
				
			}
		}
		
		System.out.println("-----------TASK 4-----------------");
		
		for(String method : Utils.Methods){
			for(int d : Utils.J){
				if(! Utils.State.isTimeSet(4, method, d)){
					long time = task4(method, d);
					Utils.State.setTime(4, method, d, time);;
				}
				
			}
		}
	}

	public static HashMap<String, Utils.Trem> mapcreator() throws IOException{
		final FileReader dataFile = new FileReader(FileAllData);
		final LineNumberReader reader = new LineNumberReader(dataFile);
		
		HashMap<String, Utils.Trem> mapa = new HashMap<String, Utils.Trem>();
		for(int i = 0;i<QuerrySize;i++){
			String[] token = reader.readLine().split("\\s+");
			for(int j=0;j<token.length;j++){
				mapa.put(token[j], new Utils.Trem(new String(token[j])));
			}
		}
		reader.close();
		return mapa;
	}
	
/// ===== TASK 1 ===== ///	
	public static ArrayList<String> task1(HashMap mapa) throws IOException{
		final FileReader dataFile = new FileReader(FileAllData);
		final LineNumberReader reader = new LineNumberReader(dataFile);
		
		String stringLine = reader.readLine();
		String[] token = stringLine.split("\\s+");
		for(int line = 0;  (stringLine = reader.readLine()) != null && line<QuerrySize; line++){
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
		
		ArrayList<String> aux = new ArrayList<String>();
		for(Utils.Trem e : sortByNumberOfTweets)
			aux.add(e.text);
		return aux;
	}
	
	/// ===== PRE ===== ///
	public static void dataReduction( String method, int d, ArrayList<String> sortByNumberOfTweets,HashMap<String, Utils.Trem> mapa) throws IOException{
		final BufferedReader reader = new BufferedReader(new FileReader(Main.FileAllData));
		String stringLine;
		HashMap<String, String>  subspaceHashMap = subspace(method, d, sortByNumberOfTweets);
		BufferedWriter outputWriter = Utils.getBufferWriter(method, d);
		
		//LINHAS
		for(int line = 0;  (line<5000 || UseAllData) && (stringLine = reader.readLine()) != null; line++){
			if(line%QuerrySize==0)System.out.println("dataReduction(): line:" + line);
			String[]  tokens = stringLine.split("\\s+");
			
			//ArrayList<Valor> listOfTrems = new ArrayList<Valor>();
			String listOfTremsSTRING = new String();
			for(int ii=0;ii<tokens.length;ii++){
				
				String obj = (String) subspaceHashMap.get(tokens[ii]);
				if(obj != null){
					//listOfTrems.add(obj);
					listOfTremsSTRING += " " + obj;
				}
			}
			outputWriter.write( line + " " + tokens.length + listOfTremsSTRING);
			outputWriter.newLine();
			//tweets.add(new Tweet(tokens.length, listOfTrems));
		}
		outputWriter.flush();
		outputWriter.close();
	}
/// ===== TASK 2 ===== ///	
	public static long task2(String method, int d) throws IOException{
		double angle = Math.PI/2;
		Utils.Data data = new Utils.Data(method, d);
		ArrayList<Utils.Tweet> querrys = new ArrayList<Utils.Tweet>();
		Utils.Tweet angleMinX = null;
		Utils.Tweet angleMinY = null;
		
		for(int k=0; k<QuerrySize;k++) 
			querrys.add(data.getNextTweet());
		
		long startTime = System.currentTimeMillis();
		for(Utils.Tweet querryY = data.getNextTweet(); querryY != null; querryY = data.getNextTweet()){
			for(Utils.Tweet querryX : querrys){
				double aux = angleTweet(querryX, querryY);
				if(aux<angle){
					angle = aux;
					angleMinX = querryX;
					angleMinY = querryY;
				}
			}
		}
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("NearestX: index: " + angleMinX.index +",  #Tren: " + angleMinX.numberOfTrems + ",  text: " + angleMinX.listOfTrems );
		System.out.println("NearestY: index: " + angleMinY.index +",  #Tren: " + angleMinY.numberOfTrems + ",  text: " + angleMinY.listOfTrems );
		System.out.println("Anglet:" + angle +"   Time elapsed: " + totalTime + "ms");
		return totalTime;
	}
	
	
	/// ===== TASK 3 ===== ///
	public static long task3(String method, int d) throws IOException{
		double angle = Math.PI/2;
		Utils.Data data = new Utils.Data(method, d);
		ArrayList<Utils.Tweet> querrys = new ArrayList<Utils.Tweet>();
		Utils.Tweet angleMinX = null;
		Utils.Tweet angleMinY = null;
		
		for(int k=0; k<QuerrySize;k++) 
			querrys.add(data.getNextTweet());
		
		long startTime = System.currentTimeMillis();
		for(Utils.Tweet querryY = data.getNextTweet(); querryY != null; querryY = data.getNextTweet()){
			for(Utils.Tweet querryX : querrys){
				double aux = angleTweetAlphabet(querryX, querryY);
				if(aux<angle){
					angle = aux;
					angleMinX = querryX;
					angleMinY = querryY;
				}
			}
		}
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("NearestX: index: " + angleMinX.index +",  #Tren: " + angleMinX.numberOfTrems + ",  text: " + angleMinX.listOfTrems );
		System.out.println("NearestY: index: " + angleMinY.index +",  #Tren: " + angleMinY.numberOfTrems + ",  text: " + angleMinY.listOfTrems );
		System.out.println("Anglet:" + angle +"   Time elapsed: " + totalTime + "ms");
		return totalTime;
	}
	
	
	public static long task4(String method, int d) throws IOException{
		double angle = Math.PI/2;
		Utils.Data data = new Utils.Data(method, d);
		ArrayList<Utils.Tweet> querrys = new ArrayList<Utils.Tweet>();
		Utils.Tweet angleMinX = null;
		Utils.Tweet angleMinY = null;
		
		for(int k=0; k<QuerrySize;k++) 
			querrys.add(data.getNextTweet());
		
		long startTime = System.currentTimeMillis();
		for(Utils.Tweet querryY = data.getNextTweet(); querryY != null; querryY = data.getNextTweet()){
			for(Utils.Tweet querryX : querrys){
				double aux = angleTweetAlphabet(querryX, querryY);
				if(aux<angle){
					angle = aux;
					angleMinX = querryX;
					angleMinY = querryY;
				}
			}
		}
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("NearestX: index: " + angleMinX.index +",  #Tren: " + angleMinX.numberOfTrems + ",  text: " + angleMinX.listOfTrems );
		System.out.println("NearestY: index: " + angleMinY.index +",  #Tren: " + angleMinY.numberOfTrems + ",  text: " + angleMinY.listOfTrems );
		System.out.println("Anglet:" + angle +"   Time elapsed: " + totalTime + "ms");
		return totalTime;
	}
	
	/*
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
	}*/
	/*
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
	*/
	
	
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
	public static double angleTweet(Utils.Tweet x, Utils.Tweet y){
		int cont = 0;
		for(int i = 0; i< x.listOfTrems.size(); i++)
			for(int j = 0; j< y.listOfTrems.size(); j++)
				if(x.listOfTrems.get(i).equals(y.listOfTrems.get(j)))
					cont++;
		return Math.acos(cont/(Math.sqrt(x.numberOfTrems)*Math.sqrt(y.numberOfTrems)));
	}

// ===== Angle Alphabet ===== //
	public static double angleTweetAlphabet(Utils.Tweet x, Utils.Tweet y){
		int cont = 0;
		int i=0;
		int j=0;
		while(i<x.listOfTrems.size() && j<y.listOfTrems.size()){
			if(x.listOfTrems.get(i).equals(y.listOfTrems.get(j))){
				cont++;
				i++;
				j++;
			}
			else{
				if(x.listOfTrems.get(i).compareTo(y.listOfTrems.get(j))<0) i++;  
				else j++;
			}
		}
		return Math.acos(cont/(Math.sqrt(x.numberOfTrems)*Math.sqrt(y.numberOfTrems)));
	}
	
	// ===== Generate Subspaces ===== //
	public static HashMap<String, String> subspace(String method, int d, ArrayList<String> sortByNumberOfTweets){
		HashMap<String, String> ret = new HashMap<String, String>();
		int D = 100*2^d;
		if(method.equals("frequent")){
			for(int i=0; i<D; i++)
				ret.put(sortByNumberOfTweets.get(i),sortByNumberOfTweets.get(i));
		}
		else if(method.equals("infrequent")){
			for(int i =0; i<D; i++)
				ret.put(sortByNumberOfTweets.get(sortByNumberOfTweets.size()-i-1),sortByNumberOfTweets.get(sortByNumberOfTweets.size()-i-1));
		}
		else if(method.equals("random")){
			ArrayList<Integer> randIntList = new ArrayList<Integer>();
			for(int rand=Utils.randInt(0, sortByNumberOfTweets.size()); randIntList.size()<D; rand=Utils.randInt(0, sortByNumberOfTweets.size())){
				if(!randIntList.contains(rand));
					randIntList.add(rand);
			}
			for(int i=0;i<D;i++){
				ret.put(sortByNumberOfTweets.get(randIntList.get(i)),sortByNumberOfTweets.get(randIntList.get(i)));
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
