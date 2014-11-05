import java.awt.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.management.RuntimeErrorException;

///// ===== Algorithmic Methods of Data Mining ===== /////
///// =============== FINAL PROJECT ================ /////

public class Main {

	public static void main(String[] args) throws IOException {
		final FileReader dataFile = new FileReader("tweets_15m.txt");
		final FileReader dataFile2 = new FileReader("tweets_15m.txt");

		final LineNumberReader reader = new LineNumberReader(dataFile);
		final LineNumberReader reader2 = new LineNumberReader(dataFile2);
		
		HashMap<String, Valor> mapa = mapcreator(reader);
		
		//===Task 1===//
		ArrayList<Valor> sortByNumberOfTweets = task1(mapa,reader);
		
		//===Task 2===//
		task2(reader2,  1, "BF", 0, sortByNumberOfTweets); // querry, method, j
			
		//===Export===//
		//writer("sort.csv",sortByNumberOfTweets);
			
		//for (int i =0; i< subSpace.size();i++)
		//	System.out.println(subSpace.get(i));
		//for (int i =0; i< sortByNumberOfTweets.size();i++)
		//	System.out.println(sortByNumberOfTweets.get(i).text + "   numberOfTweets=" + sortByNumberOfTweets.get(i).numberOfTweets);
		//for (Entry<String, Valor> entry : mapa.entrySet())
		//	System.out.println(entry.getKey() + "/   \t numberOfTweets=" + entry.getValue().numberOfTweets + "\t  aux=" + entry.getValue().aux);
	}

	public static HashMap<String, Valor> mapcreator(BufferedReader reader) throws IOException{
		HashMap<String, Valor> mapa = new HashMap<String, Valor>();
		for(int i = 0;i<1000;i++){
			String[] token = reader.readLine().split("\\s+");
			for(int j=0;j<token.length;j++){
				mapa.put(token[j], new Valor(new String(token[j])));
			}
		}
		return mapa;
	}
	
/// ===== TASK 1 ===== ///	
	public static ArrayList<Valor> task1(HashMap mapa, BufferedReader reader) throws IOException{
		String stringLine = reader.readLine();
		String[] token = stringLine.split("\\s+");
		for(int line = 0;  (stringLine = reader.readLine()) != null && line<1000; line++){
			for(int j=0;j<token.length;j++){
				Valor obj = (Valor) mapa.get(token[j]);
				if(obj != null){
					if(obj.aux != line){
						obj.aux=line;
						obj.numberOfTweets++;
					}
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
		return sortByNumberOfTweets;
	}

/// ===== TASK 2 ===== ///	
	public static void task2(LineNumberReader reader, int Q, String method, int j, ArrayList<Valor> sortByNumberOfTweets) throws IOException{
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
		
		ArrayList<String> Sub = subspace(method, j, sortByNumberOfTweets);
		
		for(int line = 0; line<5000; line++){
			stringLine = reader.readLine();
			String[]  tokensY = stringLine.split("\\s+");
			
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
		
		//int D = 100*2;
		//ArrayList<String> subSpace = subspace("frequent", D, sortByNumberOfTweets);
	}


// ===== Angle ===== //
	public static double angle(String[] x, String[] y, String method, ArrayList<String> subspace){
		int cont = 0;
		for(int i = 0; i< x.length; i++)
			for(int j = 0; j< y.length; j++)
				if(method.equals("BF")){
					if(x[i].equals(y[j])){
						cont++;
					}
				}
				else if(method.equals("frequent") || method.equals("infrequent") || method.equals("random")){
					if(x[i].equals(y[j]) && subspace.contains(y[j])){
						cont++;
					}
				}
				else{
					throw new RuntimeException();
				}
		return Math.acos(cont/(Math.sqrt(x.length)*Math.sqrt(y.length)));
	}

// ===== Generate Subspaces ===== //
	public static ArrayList<String> subspace(String method, int j, ArrayList<Valor> sortByNumberOfTweets){
		ArrayList<String> ret = new ArrayList<String>();
		int D = 100*2^j;
		if(method.equals("frequent")){
			for(int i=0; i<D; i++)
				ret.add(sortByNumberOfTweets.get(i).text);
		}
		else if(method.equals("infrequent")){
			for(int i =0; i<D; i++)
				ret.add(sortByNumberOfTweets.get(sortByNumberOfTweets.size()-i-1).text);
		}
		else if(method.equals("random")){
			ret.add("TODO");//TODO
		}
		else if(method.equals("BF")){}
		else throw new RuntimeException();
		return ret;
	}

// ===== Class Valor ===== //
	public static class Valor {
		final String text;
		int numberOfTweets;
		int aux;
		public Valor(String text) {
			this.text = text;
			this.numberOfTweets = 0;
			this.aux = -1;
		}
		public String toString(){
			return this.text;
		}
	}
	
// ===== Export Data ===== //
	public static void writer (String filename, ArrayList<Valor> x) throws IOException{
		BufferedWriter outputWriter = new BufferedWriter(new FileWriter(filename));
		for (int i = 0; i < x.size(); i++) {
			outputWriter.write(x.get(i).toString());
			outputWriter.newLine();
		}
		outputWriter.flush();  
		outputWriter.close();  
	}
}