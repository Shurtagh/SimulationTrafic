package main;

import sim.engine.SimState;
import agents.BossAgent;
import agents.ShopAgent;


public class Beings extends SimState {

	private static constantes.Constants constantes;
	
	public Beings(long seed) {
		super(seed);
		constantes = new constantes.Constants();
	}
	
	public void start() {
		super.start();
		constantes.getYard().clear();
	}

	/**
	 *	FONCTIONS D'AJOUT D'AGENTS
	 */

	/**
	 *	FONCTION DE SERVICE
	 */


	/**
	 *	Retourne l'agent magasin le plus proche de la position indiquée.
	 *	/!\ Retourne null si aucun magasin trouvé.
	 * TODO
	 */
	public ShopAgent getClosestShop(int x, int y){
		return null;
	}

	/**
	 * Retourne la position d'un Boss pour un gang donné.
	 * /!\ Retourne null si le boss n'existe pas.
	 * TODO
	 */
	public BossAgent getBossGang(int gang_number){
		return null;
	}

	public static constantes.Constants getConstants() {
		return constantes;
	}
}
