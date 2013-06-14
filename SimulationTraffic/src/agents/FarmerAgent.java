package agents;


import sim.engine.SimState;
import sim.engine.Stoppable;
import constantes.Constants;


public class FarmerAgent extends GangMemberAbstractAgent {

	/*** caractéristiques ***/
	public int charge_max; 		//max charge qu'il peut porter
	public int charge_courante;	//charge qu'il porte actuellement en marchandise

	//coordonnées du champ
	public int champ_x;
	public int champ_y;

	/**
	 *	Objectif
	 *	=  0 => aller le boss
	 *  <> 0 => aller dans le champs
	 */
	public int objectif;


	
	/**
	 * 	Constructeur
	 */
	public FarmerAgent(int x , int y, BossAgent boss, int champ_x, int champ_y, int gang_number) {

		//appel classe mère
		super(x,y,boss,gang_number);

		//attributs caractéristiques
		this.charge_max = Constants.CHARGE_MAX_FARMER;
		this.charge_courante = 0;

		//position du champs
		this.champ_x = champ_x;
		this.champ_y = champ_y;

		//au début aller voir le boss
		this.objectif = 0;

		//ajout � la liste du gang
		this.boss.farmers.add(this);
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
				donnerCanabisBoss();
			}else{
				//sinon se diriger vers le boss
				dirigerVersLeBoss();
			}	

		}else{

			//OBJECTIF CULTIVER

			//si je suis sur le magasin
			if(surLeChamp()){
				//acheter les fournitures
				cultiver();
			}else{
				//sinon se diriger vers le magasin
				dirigerVersLeChamps();
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
		this.boss.farmers.remove(this);
	}
	
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
	private boolean surLeChamp(){
		return (this.x == this.champ_x) && (this.y == this.champ_y);
	}

	/**
	 *	Déplace l'agent en direction du magasin le plus proche.
	 *	TOTEST
	 */
	private void dirigerVersLeChamps(){
		//déplacement vers cette case
		this.deplacement(this.champ_x,this.champ_y);
	}

	/**
	 *	Fonction permettant de retirer des fournitures dans un magasin
	 *	TOTEST
	 */
	private void cultiver(){

		//aléa entre min et max
		int recolte = (int) (Math.random()*(Constants.MAX_PRODUCTION_FARMER - Constants.MIN_PRODUCTION_FARMER) + Constants.MIN_PRODUCTION_FARMER);

		//ajout charge courante
		this.charge_courante += recolte;

		//test charge max
		if(this.charge_courante > this.charge_max){
			this.charge_courante = this.charge_max;
		}
	}

	/**
	 *	Donne la fourniture récoltée au boss.
	 *	TOTEST
	 */
	private void donnerCanabisBoss(){

		//vérification position
		if(procheDuBoss()){

			//donner fourniture
			if(this.charge_courante > 0){
				this.boss.ajouterCanabis(this.charge_courante);
				this.charge_courante = 0;
			}
		}
	}

	
}
