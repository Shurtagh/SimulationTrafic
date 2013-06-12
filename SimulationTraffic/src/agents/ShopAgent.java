package agents;


import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import constantes.Constants;



public class ShopAgent implements Steppable {
	
	/*** attributs ***/
	public int x, y;
	private Stoppable stoppable;

	/**
	 *	Commentaires
	 *		- le magasin peut fournir à l'infini
	 */

	/**
	 * 	Constructeur
	 */
	public ShopAgent(int x , int y, Stoppable stoppable) {
		this.x = x;
		this.y = y;
		this.stoppable = stoppable;
	}

	/**
	 * Action appelée à chaque itération.
	 */
	@Override
	public void step(SimState state) {
		//un agent magasin est inerte.
	}
}
