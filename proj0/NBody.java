public class NBody {
	public static double readRadius(String fileName) {
		In in = new In(fileName);

		int n = in.readInt();
		double radius = in.readDouble();

		in.close();
		
		return radius;
	}

	public static Planet[] readPlanets(String fileName) {
		In in = new In(fileName);

		int n = in.readInt();
		double radius = in.readDouble(); // wont use here

		Planet[] planets = new Planet[n];

		for (int i = 0; i < n; i++) {
			double xPos = in.readDouble();
			double yPos = in.readDouble();
			double xVel = in.readDouble();
			double yVel = in.readDouble();
			double mass = in.readDouble();
			String imgFile = in.readString();
		
			planets[i] = new Planet(xPos, yPos, xVel, yVel, mass, imgFile);
		}

		in.close();
		return planets;
	}

	public static void main(String[] args) {
		// get time of simulation and time increment
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);

		// get name of file containing planet information
		String filename = args[2];

		/* use planet file to get radius and construct
		   array of planet object instances */
		double radius = readRadius(filename);
		double negRadius = radius - (2 * radius);
		Planet[] planets = readPlanets(filename);		

		/* draw the background and planets in their starting position */
		StdDraw.setScale(negRadius, radius);
		StdDraw.picture(0, 0, "./images/starfield.jpg");
		StdDraw.show();

		for (Planet p: planets) {
			p.draw();
		}

		// perform simulation in a loop
		double time = 0.0;
		while (time < T) {
			double[] xForces = new double[planets.length];
			double[] yForces = new double[planets.length];

			for (int i = 0; i < planets.length; i++) {
				 xForces[i] = planets[i].calcNetForceExertedByX(planets);
				 yForces[i] = planets[i].calcNetForceExertedByY(planets);
			 }
			 
			for (int i = 0; i < planets.length; i++) {
				planets[i].update(dt, xForces[i], yForces[i]);
			}

			StdDraw.picture(0, 0, "./images/starfield.jpg");
			for (Planet p: planets) {
				p.draw();
			}
			StdDraw.show(10);
			time += dt;
		}
	}
}
