package agents;


import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.util.Bag;
import constantes.Constants;


public class DealerAgent extends GangMemberAbstractAgent {

	/*** caractéristiques ***/
	public int charge_meth_max; 			//max charge qu'il peut porter
	public int charge_meth_courante;		//charge qu'il porte actuellement en meth
	public int charge_canabis_max; 		//max charge qu'il peut porter
	public int charge_canabis_courante;	//charge qu'il porte actuellement en canabis

	private int montant_courant;			//montant courant de l'agent


	//zone que couvre le dealer
	int zone_x;
	int zone_y;
	int largeur;
	int hauteur;

	
	/**
	 * 	Constructeur
	 */
	public DealerAgent(int x , int y,BossAgent boss, int zone_x, int zone_y, int gang_number) {

		//appel classe m�re
		super(x,y,boss,gang_number);

		//attributs caractéristiques
		this.zone_x = zone_x;
		this.zone_y = zone_y;
		this.hauteur = Constants.HAUTEUR_ZONE_DEALER;
		this.largeur = Constants.LARGEUR_ZONE_DEALER;
		this.charge_canabis_max = Constants.CHARGE_CANABIS_MAX_DEALER;
		this.charge_meth_max = Constants.CHARGE_METH_MAX_DEALER;

		//colorer sa zone
		colorerZone(zone_x, zone_y, hauteur, largeur);	
		
		//ajouter � la liste des dealers du boss
		this.boss.dealers.add(this);

	}
	
	/**
	 * Surcharge de la fonction die pour se retirer de la liste.
	 */
	public void die(){
		super.die();
		this.boss.dealers.remove(this);
	}

	/**
	 * Action appelée à chaque itération.
	 */
	@Override
	public void step(SimState state) {

		//r�cup�re les enemies proches
		Bag enemies_proches = this.regarderAgresseursProche();
		
		//appel la fonction signal du boss avec tous les enemnies
		this.boss.signalEnnemies(enemies_proches);
		
		//se deplace aléatoirement dans la zone (ou s'y diriger)
		this.seDeplacerDansLaZone();
		
		//est ce que j'appel un manager ? (quantite_courante < (quantite_max)*1/3)
		if((this.charge_meth_courante < (this.charge_meth_max / 3)) 
			|| (this.charge_canabis_courante < (this.charge_canabis_max / 3))){
			this.boss.appelerManager(this);
		}
		

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
	 * donne argent au manager
	 */
	public int donnerArgent(){
		
		//s�curit�
		if(this.montant_courant < 0){
			this.montant_courant = 0;
		}
		int montant = this.montant_courant;
		this.montant_courant = 0;
		return montant;
		
	}

	/**
	 *	Fonction qui demande à l'agent de se diriger vers la zone et de la cadriller.
	 */
	public void seDeplacerDansLaZone(){
		this.zonerAleatoire(this.zone_x, this.zone_y, this.largeur, this.hauteur);
	}

}
