package agents;


import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.util.Bag;
import constantes.Constants;


public class DealerAgent extends GangMemberAbstractAgent {

	/*** caractéristiques ***/
	private int charge_meth_max; 			//max charge qu'il peut porter
	private int charge_meth_courante;		//charge qu'il porte actuellement en meth
	private int charge_canabis_max; 		//max charge qu'il peut porter
	private int charge_canabis_courante;	//charge qu'il porte actuellement en canabis

	private int montant_courant;			//montant courant de l'agent


	//zone que couvre le dealer
	int zone_x;
	int zone_y;
	int largeur;
	int hauteur;

	
	/**
	 * 	Constructeur
	 */
	public DealerAgent(int x , int y, Stoppable stoppable,BossAgent boss, int zone_x, int zone_y) {

		//appel classe mère
		super(x,y,stoppable,boss);

		//attributs caractéristiques
		this.zone_x = zone_x;
		this.zone_y = zone_y;
		this.hauteur = Constants.HAUTEUR_ZONE_DEALER;
		this.largeur = Constants.LARGEUR_ZONE_DEALER;

	}

	/**
	 * Action appelée à chaque itération.
	 */
	@Override
	public void step(SimState state) {

		//r�cup�re les enemies proches
		Bag enemies_proches = this.regarderAgresseursProche();
		
		//appel la fonction signal du boss avec tous les enemnies
		this.boss.signalEnemies(enemies_proches);
		
		//se deplace aléatoirement dans la zone (ou s'y diriger)
		this.seDeplacerDansLaZone();

	}

	/**
	 *	FONCTIONS DE SERVICE
	 */

	/**
	 *	Donne du canabis à un acheteur.
	 *	TOTEST
	 */
	public int sellCanabis(int quantite_demandee){

		//donne ce que l'on peut
		int quantite = quantite_demandee;
		if(quantite_demandee > charge_canabis_courante){
			quantite = charge_canabis_courante;
		}

		//décrémente
		this.charge_canabis_courante -= quantite;

		//encaisse le montant
		this.montant_courant += quantite *  this.boss.getPrixCanabis();

		//indique combien on lui vend
		return quantite;
	}

	/**
	 *	Donne de la meth un acheteur.
	 *	TOTEST
	 */
	public int sellMeth(int quantite_demandee){

		//donne ce que l'on peut
		int quantite = quantite_demandee;
		if(quantite_demandee > charge_meth_courante){
			quantite = charge_meth_courante;
		}

		//décrémente
		this.charge_meth_courante -= quantite;

		//encaisse le montant
		this.montant_courant += quantite *  this.boss.getPrixMeth();

		//indique combien on lui vend
		return quantite;
	}


	/**
	 *	Fonction qui demande à l'agent de se diriger vers la zone et de la cadriller.
	 */
	public void seDeplacerDansLaZone(){
		this.zonerAleatoire(this.zone_x, this.zone_y, this.largeur, this.hauteur);
	}

}
