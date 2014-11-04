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

public class Main {

	public static void main(String[] args) throws IOException {
		final FileReader dataFile = new FileReader("tweets_15m.txt");
		final FileReader dataFile2 = new FileReader("tweets_15m.txt");
		//System.out.println(new File("./tweets_15m.txt").getCanonicalPath());
		//final Reader r = new Reader(dataFile);
		final LineNumberReader reader = new LineNumberReader(dataFile);
		final LineNumberReader reader2 = new LineNumberReader(dataFile2);
		//final BufferedReader reader = new BufferedReader(dataFile);
		
		HashMap<String, Valor> mapa = mapcreator(reader);
		
		//System.out.println(reader.getLineNumber());
		//System.out.println(reader.markSupported());
		ArrayList<Valor> sortByNumberOfTweets = task1(mapa,reader);
		task2(reader2,  sortByNumberOfTweets, 1);
		
		String[] x ={"a","admit","been","c","case","co","confirm","d","ebola","ha","hospit","howard","http","lik","not","of","paty","symptom","t","to","tvnl","u","viru","with","ww","zl"};
		String[] y ={"airport","bathroom","clean","door","ebola","ebolaansw","ground","handl","left","of","on","paty","ppl","reider","ronanfarrow","sweat","the","think","toilet","who"};
		String[] z ={"asnowros","at","c","co","d","ebola","hospit","howard","http","in","lik","paty","rt","symptom","t","time","treat","univer","vmwjjgucrl","washington","with"};
		System.out.println(angle(x, z, new ArrayList<String>()));
		//angle=1.031476373310863		 
				 
		//for (int i =0; i< subSpace.size();i++)
		//	System.out.println(subSpace.get(i));
		//for (int i =0; i< sortByNumberOfTweets.size();i++)
		//	System.out.println(sortByNumberOfTweets.get(i).text + "   numberOfTweets=" + sortByNumberOfTweets.get(i).numberOfTweets);
		//for (Entry<String, Valor> entry : mapa.entrySet())
		//	System.out.println(entry.getKey() + "/   \t numberOfTweets=" + entry.getValue().numberOfTweets + "\t  aux=" + entry.getValue().aux);
	}

	public static HashMap<String, Valor> mapcreator(BufferedReader reader) throws IOException{
		HashMap<String, Valor> mapa = new HashMap<String, Valor>();
		for(int i = 0;i<5000;i++){
			String[] token = reader.readLine().split("\\s+");
			for(int j=0;j<token.length;j++){
				mapa.put(token[j], new Valor(new String(token[j])));
			}
		}
		return mapa;
	}
	
	public static class Valor {
		final String text;
		int numberOfTweets;
		int aux;
		public Valor(String text) {
			this.text = text;
			this.numberOfTweets = 0;
			this.aux = -1;
		}
	}
	
	public static void task2(LineNumberReader reader, ArrayList<Valor> sortByNumberOfTweets, int Q) throws IOException{
		int D = 100*2;
		double angle = Math.PI/2;
		int index = -1;
		ArrayList<String> subSpace = subspace("frequent", D, sortByNumberOfTweets);
		//if(Q>1000) throw new RuntimeException();
		String stringLine = reader.readLine();
		System.out.println(stringLine);
		
		String[] tokensX = stringLine.split("\\s+");
		for(int i=1; i<1000;i++) reader.readLine();
		//reader.setLineNumber(1000);
		
		for(int ĺine = 0; ĺine<5000; ĺine++){
			stringLine = reader.readLine();
			String[]  tokensY = stringLine.split("\\s+");
			double aux = angle(tokensX, tokensY, subSpace);
			if(aux<angle){
				angle = aux;
				//index = ĺine + 1000;
				index = reader.getLineNumber();//ĺine + 1000;
				System.out.println(stringLine);
				System.out.println("index=" + index + "  angle=" + angle);
				//textAUX = stringLine;
			}
			
		}
	}
	public static ArrayList<Valor> task1(HashMap mapa, BufferedReader reader) throws IOException{
		String stringLine = reader.readLine();
		String[] token = stringLine.split("\\s+");
		for(int ĺine = 0;  (stringLine = reader.readLine()) != null && ĺine<5000; ĺine++){
			for(int j=0;j<token.length;j++){
				Valor obj = (Valor) mapa.get(token[j]);
				if(obj != null){
					if(obj.aux != ĺine){
						obj.aux=ĺine;
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
	
	public static double angle(String[] x, String[] y, ArrayList<String> subspace){
		int cont = 0;
		for(int i = 0; i< x.length; i++)
			for(int j = 0; j< y.length; j++)
				if(x[i].equals(y[j]))
					//if(subspace.contains(x[i])) //have to be in the subspace
						cont++;
		return Math.acos(cont/(Math.sqrt(x.length)*Math.sqrt(y.length)));
	}
	
	public static ArrayList<String> subspace(String f, int d, ArrayList<Valor> sortByNumberOfTweets){
		ArrayList<String> ret = new ArrayList<String>();
		if(f.equals("frequent")){
			for(int i =0; i<d; i++)
				ret.add(sortByNumberOfTweets.get(i).text);
		}else if(f.equals("infrequent")){
			for(int i =0; i<d; i++)
				ret.add(sortByNumberOfTweets.get(sortByNumberOfTweets.size()-i-1).text);
		}else if(f.equals("random")){
			ret.add("TODO");//TODO
		}else throw new RuntimeException();
		return ret;
	}
}