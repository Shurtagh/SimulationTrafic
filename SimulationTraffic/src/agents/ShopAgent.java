package agents;


import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import constantes.Constants;



public class ShopAgent implements Steppable {
	
	/*** attributs ***/
	public int x, y;

	/**
	 *	Commentaires
	 *		- le magasin peut fournir à l'infini
	 */

	/**
	 * 	Constructeur
	 */
	public ShopAgent(int x , int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Action appelée à chaque itération.
	 */
	@Override
	public void step(SimState state) {
		//un agent magasin est inerte.
	}
}
