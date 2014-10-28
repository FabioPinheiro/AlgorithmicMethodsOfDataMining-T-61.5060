import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) throws IOException {
		final BufferedReader reader = new BufferedReader(new FileReader("tweets_15m.txt"));
		
		HashMap<String, Point> mapa = mapcreator(reader);
		task1(mapa,reader);
		for (HashMap.Entry<String, Point> entry : mapa.entrySet())
		{
		    System.out.println(entry.getKey() + "/" + entry.getValue());
		}
	}

	public static HashMap<String, Point> mapcreator(BufferedReader reader) throws IOException{
		HashMap<String, Point> mapa = new HashMap<String, Point>();
		for(int i = 0;i<1000;i++){
			String[] token = reader.readLine().split("\\s+");
			for(int j=0;j<token.length;j++){
				mapa.put(token[j], new Point(0,-1));
			}
		}
		return mapa;
	}
	
	public static void task1(HashMap mapa,BufferedReader reader) throws IOException{
		Point number = new Point(0,0);
		String[] token = reader.readLine().split("\\s+");
		for(int i = 0;i<5000;i++){
			for(int j=0;j<token.length;j++){
				if(mapa.get(token[j])!=null){
					Point xx = (Point) mapa.get(token[j]);
					if(xx.getY()!=i){
						xx.y=i;
						xx.x++;
					}
				}
			}
			token = reader.readLine().split("\\s+");
		}
	}
	
	
	
}
