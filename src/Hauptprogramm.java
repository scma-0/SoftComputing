import java.io.File;
import java.util.Random;

import javax.swing.JSpinner.ListEditor;

public class Hauptprogramm {
	static Random r = new Random();

	public static double fitness(int s1, int s2, double alpha) {
		double[][] daten = Einlesen.einlesenDiabetes(new File("diabetes.csv"), true);
		int dimension = daten[0].length - 1;

	/**	s1 = r.nextInt(10);
		s2 = r.nextInt(10);
		s3 = r.nextInt(10);
		alpha = r.nextDouble() + 1.00;**/

		int[] strukturNN = { s1, s2, (int) alpha };// anzahl Knoten (incl. Bias) pro Hiddenschicht
		KNN netz = new KNN(dimension, strukturNN);
		
		netz.trainieren(daten);// Verlustfunktion min
		for (int i = 0; i < strukturNN.length; i++) {
			
		}
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

		int s1 = r.nextInt(10);
		int s2 = r.nextInt(10);
		int s3 = r.nextInt(10);
		double alpha = r.nextDouble() + 2.00;
		fitness(s1, s2, alpha);
	 
	}

}