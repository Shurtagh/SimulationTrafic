package agents;


import java.util.ArrayList;

import sim.engine.SimState;
import sim.engine.Stoppable;
import constantes.Constants;


public class BossAgent extends GangMemberAbstractAgent {

	/*** caractéristiques ***/
	public int richesse;	//or que possède actuellement le boss du gang
	public int fournitures; //Fourniture que possede le gang.
	public int canabis;		//canabis que possède le gang.
	public int meth;		//methamphétamine que possède le gang.

	public int prevision_achat_fourniture;	//doit être inférieur à la charge max d'un agent
	public int prevision_donner_fourniture;	//fourniture que l'on pense donner à un cusinier

	public int prix_canabis;	//prix de vente du canabis
	public int prix_meth;		//prix du vente de la meth
	
	public ArrayList<DealerAgent> dealers = new ArrayList<DealerAgent>();

	/**
	 * 	Constructeur
	 */
	public BossAgent(int x , int y, Stoppable stoppable, int prix_initial_canabis, int prix_initial_meth) {

		//appel classe mère
		super(x, y, stoppable, null);
		this.boss = this;
		//attributs caractéristiques
		this.prevision_achat_fourniture = Constants.PREVISION_ACHAT_FOURNITURE_INITIALE;
		this.richesse = Constants.RICHESSE_INITIALE;
		this.fournitures = Constants.FOURNITURE_INITIALE;
		this.canabis = Constants.CANABIS_INITIALE;
		this.meth = Constants.METH_INITIALE;
		this.prix_canabis = prix_initial_canabis;
		this.prix_meth = prix_initial_meth;
	}

	/**
	 * Action appelée à chaque itération.
	 */
	@Override
	public void step(SimState state) {

	}

	/**
	 *	FONCTIONS DE SERVICE
	 */

	/**
	 *	Fonction chargée de donnée de l'argent à un Fournisseur.
	 *	TOTEST
	 */
	public int donnerArgentFournisseur(){
		//calcul du montant que l'on souhaite donner
		int montant = prevision_achat_fourniture * Constants.PRIX_FOURNITURES;

		//assez argent
		if (montant < this.richesse){
			this.richesse -= montant;
			return montant;
		}

		//pas assez argent
		montant = this.richesse;
		this.richesse = 0;
		return montant;

	}

	/**
	 *	Fonction chargée de donner des fourniture à un cuisinier
	 *	TOTEST
	 */
	public int donnerFournitureCuisinier(){

		//assez de fourniture
		if (prevision_donner_fourniture < this.fournitures){
			this.fournitures -= prevision_donner_fourniture;
			return prevision_donner_fourniture;
		}

		//pas assez de fourniture
		prevision_donner_fourniture = this.fournitures;
		this.fournitures = 0;
		return prevision_donner_fourniture;

	}
	

	/**
	 *	Fonction chargée de récupérer de les fournitures d'un Fournisseur.
	 *	TOTEST
	 */
	public int ajouterFourniture(int i){
		if (i > 0)
			this.fournitures += i;

		return 0;
	}

	/**
	 *	Fonction chargée de récupérer du canabis d'un cultivateur.
	 *	TOTEST
	 */
	public int ajouterCanabis(int i){
		if (i > 0)
			this.canabis += i;

		return 0;
	}

	/**
	 *	Fonction chargée de récupérer du meth d'un cuisinier.
	 *	TOTEST
	 */
	public int ajouterMeth(int i){
		if (i > 0)
			this.meth += i;

		return 0;
	}
	
	/**
	 * Fonction permettant de recevoir des positions enemies (flics ou gangs)
	 * @param enemies_proches
	 * TODO
	 */
	public void signalEnemies(Bag enemies_proches) {
		/**
		 * Bag peut �tre vide => rien a signaler.
		 */
		// TODO Auto-generated method stub
		
	}

	/**
	 *	GETTER AND SETTER
	 */

	public int getPrixMeth(){
		return this.prix_meth;
	}

	public int getPrixCanabis(){
		return this.prix_canabis;
	}

	public DealerAgent getRandomDealer() {
		if (dealers.size() == 0) {
			return null;
		}
		int pick = (int) (Math.random() * (dealers.size() - 1));
		return dealers.get(pick);
	}

}
