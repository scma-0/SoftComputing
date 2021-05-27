import java.io.File;
import java.util.Random;

import javax.swing.JSpinner.ListEditor;

	// Abspeichern der Werte
	// Werte vergleichen - Crossover 
	// Mutation

public class Hauptprogramm {
	static Random r = new Random();
	public static double[] zwischensp = new double[5];
	public static double alpha;
	
	
	public static double fitness(int s1, int s2, int s3, int s4, double alpha) {
		double[][] daten = Einlesen.einlesenDiabetes(new File("diabetes.csv"), true);
		int dimension = daten[0].length - 1;

		int[] strukturNN = { s1, s2, s3, s4, (int) alpha };// anzahl Knoten (incl. Bias) pro Hiddenschicht
		KNN netz = new KNN(dimension, strukturNN);
		
		netz.trainieren(daten);// Verlustfunktion min
		
		daten = Einlesen.einlesenDiabetes(new File("diabetes.csv"), false);

		netz.evaluieren(daten);

		// netz.evaluierenGUIII(daten);

		return 0.08;
	}

	int dim;
	double[] x; // [2,2] jedes Individum hat ein LÃ¶sungsvektor
	double signum; // schrittweite
	double fitness;
	public static double lernRate; // rate mit der die schrittweise angepasst werden

	public Hauptprogramm(int anzahlIndDim) {
		this.dim = anzahlIndDim;
		this.x = new double[anzahlIndDim];
		for (int i = 0; i < anzahlIndDim; i++) {
			x[i] = 5 * Math.random(); // problemspezifisch geraten
			if (Math.random() < 0.5)
				x[i] = -x[i];
		}

		this.signum = Math.random();
		lernRate = 1.0 / Math.sqrt((double) (anzahlIndDim)); // Schwefel95
		// lernRate = 0.5; // probieren

	}

	public void mutieren() {
		double zz = lernRate * snv();
		// schrittweite mutieren
		this.signum = this.signum * Math.exp(zz);

		for (int i = 0; i < this.x.length; i++) {
			zz = this.signum * snv();
			this.x[i] = this.x[i] + zz;
		}
	}

	public void rekombinieren(Hauptprogramm e1, Hauptprogramm e2) {
		this.signum = (e1.signum + e2.signum) / 2.;
		for (int i = 0; i < this.x.length; i++) {
			double zz = Math.random();
			if (zz < 0.5)
				this.x[i] = e1.x[i];
			else
				this.x[i] = e2.x[i];
		}
	}

	public static double snv() {
		// Methode von BoxMuller
		double z1 = Math.random();
		double z2 = Math.random();
		double x1 = Math.cos(z1 * 2 * Math.PI) * Math.sqrt(-2 * Math.log(z2));
		double x2 = Math.sin(z1 * 2 * Math.PI) * Math.sqrt(-2 * Math.log(z2));
		if (Math.random() < 0.5)
			return x1;
		else
			return x2;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 2; i++) {
			int s1 = r.nextInt(10 - 1) + 1;
			int s2 = r.nextInt(10 - 1) + 1;
			int s3 = r.nextInt(10 - 1) + 1;
			int s4 = r.nextInt(10 - 1) + 1;
			alpha = r.nextDouble() * 5;
			
			
			
			fitness(s1, s2, s3, s4, alpha);
			
			System.out.println(s1);
			System.out.println(s2);
			System.out.println(s3);
			System.out.println(s4);
			System.out.println(alpha);
			
			for (int j = 0; j < zwischensp.length; j++) {
				
				System.out.println("zw " + zwischensp[j]);
			}
			
			zwischensp[0] = s1;
			zwischensp[1] = s2;
			zwischensp[2] = s3;
			zwischensp[3] = s4;
			zwischensp[4] = alpha;
			
		}
	}

}