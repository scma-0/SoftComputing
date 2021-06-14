import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Hauptprogramm {

	static Random r = new Random();
	public static double alpha;

	static double s1;
	static double s2;
	static double s3;

	static int bestFitnessCounter;

	static ArrayList<Double> FitnessArr = new ArrayList<Double>();
	static ArrayList<Double> SaveStrukturNN = new ArrayList<Double>();

	static double[] testArr = new double[4];
	
	public static void setRandomVar() {
		s1 = r.nextInt(10 - 1) + 1;
		s2 = r.nextInt(10 - 1) + 1;
		s3 = r.nextInt(10 - 1) + 1;
		alpha = r.nextDouble() * 5;
	}

	public static void main(String[] args) {
		Queue<Double> queue = new ConcurrentLinkedQueue<Double>();

		double[][] daten = Einlesen.einlesenDiabetes(new File("diabetes.csv"), true);
		int dimension = daten[0].length - 1;

		setRandomVar();

		int[] strukturNN = { (int)s1, (int)s2, (int)s3, (int)alpha };// anzahl Knoten (incl. Bias) pro Hiddenschicht
		KNN netz = new KNN(dimension, strukturNN);

		HashMap<Integer, List<Double>> gewichte = new HashMap<Integer, List<Double>>();
		
		for (int i = 0; i < 3; i++) {

			setRandomVar();

			strukturNN = strukturNN;// anzahl Knoten (incl. Bias) pro Hiddenschicht
			netz = new KNN(dimension, strukturNN);

			netz.trainieren(daten);// Verlustfunktion min

			
			gewichte.put(i, KNN.saveW);
			
			for (Entry<Integer, List<Double>> entry : gewichte.entrySet()) {
			    System.out.println(entry.getKey() + "/" + entry.getValue());
			}

			try {
				File outputFile = new File("output.txt");
				FileWriter writer = new FileWriter(outputFile, StandardCharsets.UTF_8, true);
				writer.write("\nTESTWERT von trainieren");
				for (int j = 0; j < 4; j++) {

					writer.write("\n" + j + ": " + KNN.FitnessQueue[j]);

				}
				writer.write("\n" + "s1: \t" + s1 + "\ns2: \t" + s2 + "\ns3: \t" + s3 + "\nalpha: \t" + alpha + "\n\n");
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Abspeichern der s-/Alpha Werte in Arraylist für Zuordnung
			SaveStrukturNN.add((double) s1);
			SaveStrukturNN.add((double) s2);
			SaveStrukturNN.add((double) s3);
			SaveStrukturNN.add(alpha);

			for (int j = 0; j < 4; j++) {
				// fügt Genauigkeit,richtigPositiv,richtigNegativ,Anzahl Muster der ArrayList
				// hinzu
				FitnessArr.add(KNN.FitnessQueue[j]);
			}
		}

		daten = Einlesen.einlesenDiabetes(new File("diabetes.csv"), false);
		dimension = daten[0].length - 1;
		
		double bestFitness = 0;
		for (int i = 0; i < FitnessArr.size(); i = i + 4) {
			if (FitnessArr.get(i) > bestFitness) {
				bestFitness = FitnessArr.get(i);
				bestFitnessCounter = i;
			}
		}
		
		
		s1 = SaveStrukturNN.get(bestFitnessCounter);
		s2 = SaveStrukturNN.get(bestFitnessCounter+1);
		s3 = SaveStrukturNN.get(bestFitnessCounter+2);
		alpha = SaveStrukturNN.get(bestFitnessCounter+3);
		
		
		
		netz = new KNN(dimension, strukturNN);
		
		netz.trainieren(daten);
		netz.evaluieren(daten);

		try {
			File outputFile = new File("output.txt");
			FileWriter writer = new FileWriter(outputFile, StandardCharsets.UTF_8, true);
			writer.write("\n" + "s1: \t" + s1 + "\ns2: \t" + s2 + "\ns3: \t" + s3 + "\nalpha: \t" + alpha + "\n\n");
			writer.flush();
			writer.close();

			queue.offer(Double.valueOf(s1));
			queue.offer(Double.valueOf(s2));
			queue.offer(Double.valueOf(s3));
			queue.offer(alpha);
		} catch (IOException e) {
			e.printStackTrace();
		}


		System.out.println("bestFitnessCounter  " + bestFitnessCounter);
		
		HashMap<Double, List<Double>> testMap = new HashMap<Double, List<Double>>();
		testMap.put(FitnessArr.get(0), SaveStrukturNN);
		
//		KNN.ausgabeW();
//		KNN.speicherW();
		for (Entry<Integer, List<Double>> entry : gewichte.entrySet()) {
		    System.out.println(entry.getKey() + "/" + entry.getValue());
		}
//		System.out.println(Arrays.asList(gewichte)); // method 1
	}

}