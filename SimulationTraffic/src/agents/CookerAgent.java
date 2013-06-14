package agents;


import sim.engine.SimState;
import sim.engine.Stoppable;
import constantes.Constants;


public class CookerAgent extends GangMemberAbstractAgent {

	/*** caractéristiques ***/
	public int charge_meth_courante;		//charge qu'il porte actuellement en methamphétamine
	public int charge_fourniture_courante;	//charge qu'il porte actuellement en fourniture
	public int charge_max;					//max de fourniture
	public int cuisine_x;
	public int cuisine_y;

	/**
	 *	Va voir le boss lorsqu'il n'a plus de fourniture
	 */


	
	/**
	 * 	Constructeur
	 */
	public CookerAgent(int x , int y ,BossAgent boss, int cuisine_x, int cuisine_y, int gang_number) {

		//appel classe mère
		super(x,y,boss, gang_number);

		//attributs caractéristiques
		this.charge_meth_courante = Constants.METH_INITIALE_COOCKER;
		this.charge_fourniture_courante = Constants.FOURNITURE_INITIALE_COOCKER;
		this.cuisine_x = cuisine_x;
		this.cuisine_y = cuisine_y;
		
		//ajout � la liste du gang
		this.boss.cookers.add(this);

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
				donnerMethBoss();
				//attendre de la fourniture
				retirerFourniture();
			}else{
				//sinon se diriger vers le boss
				dirigerVersLeBoss();
			}	

		}else{

			//OBJECTIF MAGASIN

			//si je suis sur la cuisine
			if(dansLaCuisine()){
				//acheter les fournitures
				transformerFourniture();
			}else{
				//sinon se diriger vers la cuisine
				dirigerVersLaCuisine();
			}
		}
	}

	/**
	 *	FONCTIONS DE SERVICE
	 */
	
	/**
	 * Surcharge de la fonction die pour se retirer de la liste.
	 */
	public void die(){
		super.die();
		this.boss.cookers.remove(this);
	}

	/**
	 *	Retourne vrai si il doit aller voir le boss, faux si il doit aller voir le magasin.
	 */
	private boolean objectifVoirLeBoss(){
		//va voir le boss si plus de fourniture
		return (charge_fourniture_courante == 0);
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
	private boolean dansLaCuisine(){
		return (this.x == this.cuisine_x) && (this.y == this.cuisine_y);
	}

	/**
	 *	Déplace l'agent en direction du magasin le plus proche.
	 *	TOTEST
	 */
	private void dirigerVersLaCuisine(){
		//déplacement vers cette case
		this.deplacement(this.cuisine_x,this.cuisine_y);
	}

	/**
	 *	Fonction permettant de retirer des fournitures dans un magasin
	 *	TOTEST
	 */
	private void transformerFourniture(){

		if(this.charge_fourniture_courante > 0){
			this.charge_meth_courante += Constants.FOURNITURE_TO_METH;
			this.charge_fourniture_courante --;
		}
	}

	/**
	 *	Donne la fourniture récoltée au boss.
	 *	TOTEST
	 */
	private void donnerMethBoss(){

		//vérification position
		if(procheDuBoss()){

			//donner fourniture
			if(this.charge_meth_courante > 0){
				this.boss.ajouterMeth(this.charge_meth_courante);
				this.charge_meth_courante = 0;
			}
		}
	}

	/**
	 *	Retire de l'argent au boss (montant nécéssaire pour l'achat des prévisions)
	 *	TOTEST
	 */
	private void retirerFourniture(){

		//vérification position
		if(procheDuBoss()){

			//demande de la fourniture au boss
			this.charge_fourniture_courante = this.boss.donnerFournitureCuisinier(this);
		}
	}
}
