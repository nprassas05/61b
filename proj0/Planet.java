public class Planet {
	double xxPos;
	double yyPos;
	double xxVel;
	double yyVel;
	double mass;
	String imgFileName;

	public Planet(double xP, double yP, double xV,
				  double yV, double m, String img) {
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}

	public Planet(Planet p) {
		/*xxPos = p.xxPos;
		yyPos = p.yyPos;
		xxVel = p.xxVel;
		yyVel = p.yyVel;
		mass = p.mass;
		imgFileName = p.imgFileName; */

		this(p.xxPos, p.yyPos, p.xxVel, p.yyVel,
			 p.mass, p.imgFileName);	
	}

	public double calcDistance(Planet p) {
		double dx = this.xxPos - p.xxPos;
		double dy = this.yyPos - p.yyPos;

		double dSquared = (dx * dx) + (dy * dy);
		return Math.sqrt(dSquared);
	} 	

	public double calcForceExertedBy(Planet p) {
		double dist = calcDistance(p);
		double G = 6.67e-11;

		return G * mass * p.mass / (dist * dist);
	}
	
	public double calcForceExertedByX(Planet p) {
		double force = calcForceExertedBy(p);
		double dx = p.xxPos - xxPos;
		double r = calcDistance(p);

		return force * dx / r;
	}

	public double calcForceExertedByY(Planet p) {
		double force = calcForceExertedBy(p);
		double dy = p.yyPos - yyPos;
		double r = calcDistance(p);

		return force * dy / r;
	}

	public double calcNetForceExertedByX(Planet[] planets) {
		double netForceX = 0.0;

		for (Planet p: planets) {
			if (this.equals(p) == false) {
				double fx = calcForceExertedByX(p);
				netForceX += fx;
			}
		}

		return netForceX;
	}		

	public double calcNetForceExertedByY(Planet[] planets) {
		double netForceY = 0.0;

		for (Planet p: planets) {
			if (this.equals(p) == false) {
				double fy = calcForceExertedByY(p);
				netForceY += fy;
			}
		}
		return netForceY;
	}		

	public void update(double dt, double fx, double fy) {
		double ax = fx / mass;
		double ay = fy / mass;

		xxVel += (ax * dt);
		yyVel += (ay * dt);

		xxPos += (dt * xxVel);
		yyPos += (dt * yyVel);
	}

	public void draw() {
		StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
	}
}
