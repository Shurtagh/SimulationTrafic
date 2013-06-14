package agents;


import main.Beings;
import constantes.Constants;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import constantes.Constants;


public abstract class BeingAbstractAgent implements Steppable {
	
	/*** attributs ***/
	public int x, y;
	private Stoppable stoppable;
	public int deplacement;
	public int perimetrePerception;
	public int pv;
	public int attaque;

	
	/**
	 * 	Constructeur
	 */
	public BeingAbstractAgent(int x , int y) {

		//attributs agent
		this.x = x;
		this.y = y;
		this.deplacement = Constants.DISTANCE_DEPLACEMENT;
		this.perimetrePerception = Constants.PERIMETRE_PERCEPTION;
		this.pv = Constants.DEFAULT_PV;
		this.attaque = Constants.DEFAULT_ATTAQUE;
	}

	/**
	 * Action appelée à chaque itération.
	 */
	@Override
	public void step(SimState state) {
	}

	/*** ACTION REALISABLES PAR LES AGENTS BEINGS ***/


	/**
	 *	Tante de se rapprocher du point indiqué.
	 * TODO
	 */
	public void deplacement(int x, int y){
		int current_x = this.x;
		int current_y = this.y;
		int delta_x = Math.abs(current_x - x);
		int delta_y = Math.abs(current_y - y);
		if ((delta_x + delta_y) < this.deplacement) {
			this.x = x;
			this.y = y;
			Beings.getConstants().getYard().setObjectLocation(this, this.x, this.y);
		} else if (delta_x == 0) {
			this.y -= this.deplacement*Math.abs(current_y - y)/(current_y - y);
			Beings.getConstants().getYard().setObjectLocation(this, this.x, this.y);
		} else if (delta_y == 0) {
			this.x -= this.deplacement*Math.abs(current_x - x)/(current_x - x);
			Beings.getConstants().getYard().setObjectLocation(this, this.x, this.y);
		} else {
			double ratio = delta_x / (delta_y + delta_x);
			int mouv_x = (int) (this.deplacement * ratio);
			int mouv_y = this.deplacement - mouv_x;
			this.x -= mouv_x * Math.abs(current_x - x) /(current_x - x);
			this.y -= mouv_y * Math.abs(current_y - y) /(current_y - y);
			Beings.getConstants().getYard().setObjectLocation(this, this.x, this.y);
		}
	}

	/**
	 *	Se déplace dans le rectangle indiqué de façon aléatoire.
	 * TODO
	 */
	public void zonerAleatoire(int x, int y, int largeur, int hauteur){
		//CoordonnŽe du point bas droite
		int bottom_right_x = x + largeur;
		int bottom_right_y = y + hauteur;
		
		//Correction des bornes hauts gauche et bas droit
		if (x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}
		if (x >= Constants.GRID_SIZE) {
			x = Constants.GRID_SIZE;
		}
		if (y >= Constants.GRID_SIZE) {
			y = Constants.GRID_SIZE;
		}
		if (bottom_right_x < 0) {
			bottom_right_x = 0;
		}
		if (bottom_right_y < 0) {
			bottom_right_y = 0;
		}
		if (bottom_right_x >= Constants.GRID_SIZE) {
			bottom_right_x = Constants.GRID_SIZE;
		}
		if (bottom_right_y >= Constants.GRID_SIZE) {
			bottom_right_y = Constants.GRID_SIZE;
		}
		
		int mouv_x, mouv_y;
		//On vŽrifie si on est dans la zone
		if (this.x > x 
				&& this.x < bottom_right_x
				&& this.y > y
				&& this.y < bottom_right_y) {
			mouv_x = (int) (Math.random() * (bottom_right_x - x) + x);
			mouv_y = (int) (Math.random() * (bottom_right_y - y) + y);
		} else {
			//On se dŽplace vers le centre
			mouv_x = (int) ((bottom_right_x - x) / 2 + x);
			mouv_y = (int) ((bottom_right_y - y) / 2 + y);
		}
		deplacement(mouv_x, mouv_y);
	}

	/**
	 *	Se déplace dans le rectangle indiqué  en le cadrillant.
	 * TODO
	 */
	public void zonerCadrillage(int x, int y, int largeur, int hauteur){
		this.zonerAleatoire(x,y,largeur,hauteur);
	}

	public void deplacementAleatoire() {
		int x = this.x - this.deplacement;
		int y = this.y - this.deplacement;
		zonerAleatoire(x, y, this.deplacement*2, this.deplacement*2);
	}
	
	public void prendreDegats(int degat) {
		pv -= degat;
	}
	
	public boolean isDead() {
		return (pv <= 0);
	}
	
	public void die() {
		Beings.getConstants().getYard().remove(this);
		stoppable.stop();
	}
	
	public void attaquer(BeingAbstractAgent agent) {
		int dist = Math.abs(x - agent.x) + Math.abs(y - agent.y);
		double rand = 1 - dist / Constants.DEFAULT_SHOOT_DISTANCE;
		if (Math.random() > rand) {
			agent.prendreDegats(attaque);
		}
	}
	
	public boolean canShoot(BeingAbstractAgent agent) {
		int dist = Math.abs(x - agent.x) + Math.abs(y - agent.y);
		return (dist < Constants.DEFAULT_SHOOT_DISTANCE);
	}
	
	public void frapperEnnemisProches(Bag bag) {
		
	}
	
	public void setStoppable (Stoppable s) {
		this.stoppable = s;
	}
	
	public Stoppable getStoppable () {
		return stoppable;
	}
}
