import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

// Abspeichern der Werte
// Werte vergleichen - Crossover 
// Mutation

public class Hauptprogramm {
	static Random r = new Random();

	public static double alpha;
	public static double[] crossoverWerte;

	public static int Srange = 4;

	static int s1;
	static int s2;
	static int s3;
	static int s4;

	static ArrayList<Double> FitnessArr = new ArrayList<Double>();

	int dim;
	double[] x; // [2,2] jedes Individum hat ein LÃ¶sungsvektor
	double signum; // schrittweite
	// double fitness;
	public static double lernRate; // rate mit der die schrittweise angepasst werden

	public static void setRandomVar() {
		s1 = r.nextInt(10 - 1) + 1;
		s2 = r.nextInt(10 - 1) + 1;
		s3 = r.nextInt(10 - 1) + 1;
		s4 = r.nextInt(10 - 1) + 1;
		alpha = r.nextDouble() * 3;
	}

//	public Hauptprogramm(int anzahlIndDim) {
//		this.dim = anzahlIndDim;
//		this.x = new double[anzahlIndDim];
//		for (int i = 0; i < anzahlIndDim; i++) {
//			x[i] = 5 * Math.random(); // problemspezifisch geraten
//			if (Math.random() < 0.5)
//				x[i] = -x[i];
//		}
//
//		this.signum = Math.random();
//		lernRate = 1.0 / Math.sqrt((double) (anzahlIndDim)); // Schwefel95
//		// lernRate = 0.5; // probieren
//
//	}
//
//	public void mutieren() {
//		double zz = lernRate * snv();
//		// schrittweite mutieren
//		this.signum = this.signum * Math.exp(zz);
//
//		for (int i = 0; i < this.x.length; i++) {
//			zz = this.signum * snv();
//			this.x[i] = this.x[i] + zz;
//		}
//	}
//
//	public void rekombinieren(double[] e1, double[] e2) {
//		alpha = (e1[3] + e2[3]) / 2.;
//		for (int i = 1; i < Srange; i++) {
//			double zz = Math.random();
//			if (zz < 0.5) {
//				crossoverWerte[i] = e1[i];
//			}
//			else {
//				crossoverWerte[i] = e2[i];
//			}
//		}
//	}
//
//	public static double snv() {
//		// Methode von BoxMuller
//		double z1 = Math.random();
//		double z2 = Math.random();
//		double x1 = Math.cos(z1 * 2 * Math.PI) * Math.sqrt(-2 * Math.log(z2));
//		double x2 = Math.sin(z1 * 2 * Math.PI) * Math.sqrt(-2 * Math.log(z2));
//		if (Math.random() < 0.5)
//			return x1;
//		else
//			return x2;
//	}

	public static void main(String[] args) {
		Queue<Double> queue = new ConcurrentLinkedQueue<Double>();

		double[][] daten = Einlesen.einlesenDiabetes(new File("diabetes.csv"), true);
		int dimension = daten[0].length - 1;

		setRandomVar();

		int[] strukturNN = { s1, s2, s3, (int) alpha };// anzahl Knoten (incl. Bias) pro Hiddenschicht
		KNN netz = new KNN(dimension, strukturNN);

		for (int i = 0; i < 2; i++) {

			setRandomVar();

			strukturNN = strukturNN;// anzahl Knoten (incl. Bias) pro Hiddenschicht
			netz = new KNN(dimension, strukturNN);

			netz.trainieren(daten);// Verlustfunktion min

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
			
			for (int j = 0; j < 4; j++) {
				
				// liest 4 Elemente aus der Queue
				FitnessArr.add(KNN.FitnessQueue[j]);
				System.out.println(j + ": " + KNN.FitnessQueue[j]);
			}
		}

		daten = Einlesen.einlesenDiabetes(new File("diabetes.csv"), false);

		netz.evaluieren(daten);

		// fitness(s1, s2, s3, alpha);

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

		for (int i = 0; i < FitnessArr.size(); i++) {
			System.out.println(FitnessArr.get(i));
		}

	}

}