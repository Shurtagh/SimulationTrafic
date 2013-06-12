package agents;


import sim.engine.SimState;
import main.Beings;
import sim.engine.Stoppable;
import constantes.Constants;



public class SupplierAgent extends GangMemberAbstractAgent {

	/*** caractéristiques ***/
	private int charge_max; 		//max charge qu'il peut porter
	private int charge_courante;	//charge qu'il porte actuellement en marchandise
	private int montant_courant;	//montant courant de l'agent

	/**
	 *	Objectif
	 *	=  0 => aller le boss
	 *  <> 0 => aller dans le magasin le plus proche
	 */
	private int objectif;


	
	/**
	 * 	Constructeur
	 */
	public SupplierAgent(int x , int y, Stoppable stoppable,BossAgent boss) {

		//appel classe mère
		super(x, y, stoppable, boss);

		//attributs caractéristiques
		this.charge_max = Constants.CHARGE_MAX_SUPPLIER;
		this.charge_courante = 0;
		this.montant_courant = 0;

		//au début aller voir le boss
		this.objectif = 0;
	}

	/**
	 * Action appelée à chaque itération.
	 */
	@Override
	public void step(SimState state) {


		if(objectifVoirLeBoss()){

			//OBJECTIF BOSS

			//si je suis sur le boss
			if(procheDuBoss()){
				//rendre la fourniture
				donnerFournitureAuBoss();
				//attendre de l'argent
				retirerArgent();
			}else{
				//sinon se diriger vers le boss
				dirigerVersLeBoss();
			}	

		}else{

			//OBJECTIF MAGASIN
			ShopAgent magasin = ((Beings) state).getClosestShop(this.x, this.y);

			//si je suis sur le magasin
			if(surLeMagasin(magasin)){
				//acheter les fournitures
				retirerFournitureMagasin();
			}else{
				//sinon se diriger vers le magasin
				dirigerVersLeMagasin(magasin);
			}
		}
	}

	/**
	 *	FONCTIONS DE SERVICE
	 */

	/**
	 *	Retourne vrai si il doit aller voir le boss, faux si il doit aller voir le magasin.
	 */
	private boolean objectifVoirLeBoss(){
		return this.objectif == 0;
	}

	/**
	 *	Retourne vrai si l'agent est adjacent au boss
	 *	TOTEST
	 */
	private boolean procheDuBoss(){
		return Constants.casesAdjacentes(this.x, this.y, this.boss.x, this.boss.y);
	}

	/**
	 *	Déplace l'agent en direction du boss.
	 *	TOTEST
	 */
	private void dirigerVersLeBoss(){
		//déplacement vers cette case
		this.deplacement(this.boss.x,this.boss.y);
	}

	/**
	 *	Retourne vrai si l'agent est sur un magasin
	 *	TOTEST
	 */
	private boolean surLeMagasin(ShopAgent magasin){
		return (this.x == magasin.x) && (this.y == magasin.y);
	}

	/**
	 *	Déplace l'agent en direction du magasin le plus proche.
	 *	TOTEST
	 */
	private void dirigerVersLeMagasin(ShopAgent magasin){
		//déplacement vers cette case
		this.deplacement(magasin.x,magasin.y);
	}

	/**
	 *	Fonction permettant de retirer des fournitures dans un magasin
	 *	TOTEST
	 */
	private void retirerFournitureMagasin(){
		while((this.montant_courant > Constants.PRIX_FOURNITURES) && (this.charge_courante < this.charge_max)){
			this.montant_courant -= Constants.PRIX_FOURNITURES;
			this.charge_courante ++;
		}
	}

	/**
	 *	Donne la fourniture récoltée au boss.
	 *	TOTEST
	 */
	private void donnerFournitureAuBoss(){

		//vérification position
		if(procheDuBoss()){

			//donner fourniture
			if(this.charge_courante > 0){
				this.boss.ajouterFourniture(this.charge_courante);
				this.charge_courante = 0;
			}
		}
	}

	/**
	 *	Retire de l'argent au boss (montant nécéssaire pour l'achat des prévisions)
	 *	TOTEST
	 */
	private void retirerArgent(){

		//vérification position
		if(procheDuBoss()){

			//demande de l'argent au boss
			this.montant_courant += this.boss.donnerArgentFournisseur();


			//si recu argent => objectif magasin
			if(this.montant_courant > 0){
				this.objectif = 1;
			}
		}
	}
}
