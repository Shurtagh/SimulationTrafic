package agents;


import java.util.ArrayList;

import main.Beings;
import sim.engine.SimState;
import sim.util.Bag;
import sim.util.Int2D;
import constantes.Constants;


public class BossAgent extends GangMemberAbstractAgent {

	/*** caractÃ©ristiques ***/
	public int richesse;	//or que possÃ¨de actuellement le boss du gang
	public int fournitures; //Fourniture que possede le gang.
	public int canabis;		//canabis que possÃ¨de le gang.
	public int meth;		//methamphÃ©tamine que possÃ¨de le gang.

	public int prevision_achat_fourniture;	//doit Ãªtre infÃ©rieur Ã  la charge max d'un agent
	public int prevision_donner_fourniture;	//fourniture que l'on pense donner Ã  un cusinier

	public int prevision_porte_meth_manager;	//quantité de meth que doit porter un manager
	public int prevision_porte_canabis_manager;	//quantité de canabis que doit porter un manager
	
	public int prix_canabis;	//prix de vente du canabis
	public int prix_meth;		//prix du vente de la meth
	
	//model
	Beings beings;
	
	//liste de tous les dealers du gang
	public ArrayList<DealerAgent> dealers = new ArrayList<DealerAgent>();
	//liste de tous les cuisiniers du gang
	public ArrayList<CookerAgent> cookers = new ArrayList<CookerAgent>();
	//liste de tous les fournisseurs du gang
	public ArrayList<SupplierAgent> suppliers = new ArrayList<SupplierAgent>();
	//liste de tous les famers du gang
	public ArrayList<FarmerAgent> farmers = new ArrayList<FarmerAgent>();
	//liste de tous les protecteurs du gang
	public ArrayList<ProtectorAgent> protectors = new ArrayList<ProtectorAgent>();
	//liste de tous les protecteurs du gang
	public ArrayList<ManagerAgent> managers = new ArrayList<ManagerAgent>();
	
	//demandes non satisfaites
	public ArrayList<BeingAbstractAgent> missions_protectors = new ArrayList<BeingAbstractAgent>();
	public ArrayList<DealerAgent> missions_manager = new ArrayList<DealerAgent>();
	
	/**
	 * 	Constructeur
	 */
	public BossAgent(Beings beings, int x , int y, int prix_initial_canabis, int prix_initial_meth, int gang_number) {

		//appel classe mère
		super(x, y, null, gang_number);
		this.boss = this;
		
		//prévision
		this.prevision_achat_fourniture = Constants.PREVISION_ACHAT_FOURNITURE_INITIALE;
		this.prevision_donner_fourniture = Constants.PREVISION_DONNER_FOURNITURE;
		this.prevision_porte_meth_manager = Constants.PREVISION_PORTE_METH_MANAGER;
		this.prevision_porte_canabis_manager = Constants.PREVISION_PORTE_CANABIS_MANAGER;
		
		//attributs caractÃ©ristiques
		this.richesse = Constants.RICHESSE_INITIALE;
		this.fournitures = Constants.FOURNITURE_INITIALE;
		this.canabis = Constants.CANABIS_INITIALE;
		this.meth = Constants.METH_INITIALE;
		this.prix_canabis = prix_initial_canabis;
		this.prix_meth = prix_initial_meth;
	
		//model
		this.beings = beings;
	}

	/**
	 * Action appelÃ©e Ã  chaque itÃ©ration.
	 */
	@Override
	public void step(SimState state) {
		
			/** PRIMORDIAL => au moins un dealer **/
		
			if(this.dealers.size() <= 0){
				embaucherDealer();
			}
		
			/** Traitement des demandes non satisfaites **/
		
			/* protection */
			boolean valide = true;
			for(int i = 0; (i < this.missions_protectors.size()) && (valide); i++){
				valide = this.repondreMissionProtection(this.missions_protectors.get(i));
				
				//si attribuée retirer de la liste
				if(valide){
					this.missions_protectors.remove(i);
				}else{
					
					//embaucher car pas assez
					this.embaucherProtector();
				}
				
			}
			
			/* manager */
			valide = true;
			for(int i = 0; (i < this.missions_manager.size()) && (valide); i++){
				valide = this.repondreMissionManager(this.missions_manager.get(i));
				
				//si attribuée retirer de la liste
				if(valide){
					this.missions_manager.remove(i);
				}else{
					
					//embaucher car pas assez
					this.embaucherManager();
				}
				
			}
		
			/** FARMERS **/
			
			//calcul de la prévision de fournitiure
			int prevision_famers = this.calculPrevisionFourniture();
			
			//pas de supplier => embauche
			if(this.farmers.size() <= 0){
				embaucherFarmer();
			}else{				
				
				//combien de plus ?
				int nb_embauche_farmer = prevision_famers / this.farmers.get(0).charge_max;
				for(int i = 0; i < nb_embauche_farmer; i++){
					embaucherFarmer();
				}
			}
			
			/** SUPPLIERS **/
		
			//calcul de la prévision de fournitiure
			this.prevision_donner_fourniture = this.calculPrevisionFourniture();
			
			//pas de supplier => embauche
			if(this.suppliers.size() <= 0){
				embaucherSupplier();
			}else{				
				
				//combien de plus ?
				int nb_embauche_supplier = this.prevision_donner_fourniture / this.suppliers.get(0).charge_max;
				for(int i = 0; i < nb_embauche_supplier; i++){
					embaucherSupplier();
				}
			}
			
			
			/** COOKERS **/
						
			//calcul de la prévision de fournitiure
			int prevision_cookers = this.calculPrevisionCuisine();

			//pas de supplier => embauche
			if(this.cookers.size() <= 0){
				embaucherCooker();
			}else{				
				
				//combien de plus ?
				if(this.cookers.get(0).charge_max > 0){
					int nb_embauche_coocker = prevision_cookers / this.cookers.get(0).charge_max;
					for(int i = 0; i < nb_embauche_coocker; i++){
						embaucherCooker();
					}
				}
					
			}
			
			/** DEALERS **/
			boolean embauche = true;
			while((this.richesse > (Constants.SEUIL_RICHESSE_DEALER * this.dealers.size())) && embauche){
				embauche = embaucherDealer();
			}			
			
			
			//gestion avec fournisseurs : gérer par méthodes basées sur : prevision_achat_fourniture
				//ESTIMATION DE CE QUE L ON DONNE A UN FOURNISSEUR
				//estimation : richesse / nb fournisseur
				//donner le max possible (car seul qui a besoin de l'argent)
		
			//gestion avec le cuisinier : gérer par méthodes basées sur : prevision_donner_fourniture
				//ESTIMATION DE CE QUE L ON DONNE A UN CUISINIER
				//diviser équitable entre tous les cuisiniers
		
			//lorsque on apporte du canabis le payer
		
		
			//manque de drogue illustrer lors de l'achat par le manager
			//si on demande de la drogue et que l'on peut pas fournir => pb => embauche ou augmenter achat
				//impossible de fournir en canabis => embauche un farmer
				//impossible de fournir de la meth
					
			//EMBAUCHES
				//production
					//cuisinier
					//prevision met suppérieur à charge max fournisseur
					//calcul de la prévision : manque dealers / nb fournisseur
						//fournisseur
					//impossible de fournir un manager en canabis et stock ferme < charge
						//cultivateur
				//défence
					//manque de protecteur impossible de trouver un de libre alors que besoin
						//protecteur
				//couverture
					//manque de manager illustrer par impossible prendre manager libre
						//manager
					//dépasse un certain seuil d'argent => embauches
						//dealer
	}

	/**
	 * Fonction permettant de recevoir des positions enemies (flics ou gangs)
	 * @param enemies_proches
	 * TODO
	 */
	public void signalEnnemies(Bag enemies_proches) {
		/**
		 * Bag peut être vide => rien a signaler.
		 */

		//pour chacun des enemies
		for(int i = 0; i < enemies_proches.size(); i++){
			
			//envoyer un protecteur le plus proche (si possible)
			if(enemies_proches.get(i) instanceof BeingAbstractAgent){
				if(!this.repondreMissionProtection((BeingAbstractAgent)enemies_proches.get(i))){
					
					//si impossible : ajout historique + tentative embauche
					this.missions_protectors.add((BeingAbstractAgent)enemies_proches.get(i));
					this.embaucherProtector();
				}
			}
		}
		
	}
	
	/**
	 * Fonction appelée par les dealers qui ont besoin d'être livré.
	 */
	public void appelerManager(DealerAgent dealer){

		//affecter le manager libre le plus proche (si disponible)
		if(!this.repondreMissionManager(dealer)){
			
			//si impossible : ajout historique + tentative embauche
			this.missions_manager.add(dealer);
			this.embaucherManager();
		}
		
	}
	
	
	
	/**
	 *	FONCTIONS DE SERVICE
	 */
	
	/**
	 *	Fonction chargÃ©e de donnÃ©e de l'argent Ã  un Fournisseur.
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
	 *	Fonction chargÃ©e de donner des fourniture Ã  un cuisinier
	 *	TOTEST
	 */
	public int donnerFournitureCuisinier(CookerAgent cuisinier){

		//res
		int res = 0;
		
		//assez de fourniture
		if (prevision_donner_fourniture < this.fournitures){
			res =  prevision_donner_fourniture;
		}else{
			res = this.fournitures;			
		}
		
		//borne par la charge max
		if(res > cuisinier.charge_max){
			res = cuisinier.charge_max;
		}
		
		//décrémente
		this.fournitures -= res;
		

		//pas assez de fourniture
		return res;

	}
	
	/**
	 * Essaye d'embauche un manager.
	 */
	public boolean embaucherManager() {
		
		//si j'ai assez d'argent pour embaucher => embaucher
		if(this.richesse >= Constants.PRIX_MANAGER){
			
			//coordonnées proche du boss
			int champ_x = this.getCloseX(Constants.MAX_DISTANCE_BOSS_MANAGER);
			int champ_y = this.getCloseY(Constants.MAX_DISTANCE_BOSS_MANAGER);
			
			//création farmer
			ManagerAgent manager = new ManagerAgent(
					this.x,							//position x
					this.y,							//position y
					this,							//boss
					this.gang_number				//gang number
			);
			
			//ajouter un farmer 
			this.beings.addBeingsAbstractAgent(manager);
			
			//ajouter à la liste
			this.managers.add(manager);
			
			//décrémenter l'argent
			this.richesse -= Constants.PRIX_MANAGER;
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Essaye d'embauche un protector.
	 */
	public boolean embaucherProtector() {
		
		//si j'ai assez d'argent pour embaucher => embaucher
		if(this.richesse >= Constants.PRIX_PROTECTOR){
			
			//coordonnées proche du boss
			int champ_x = this.getCloseX(Constants.MAX_DISTANCE_BOSS_PROTECTOR);
			int champ_y = this.getCloseY(Constants.MAX_DISTANCE_BOSS_PROTECTOR);
			
			//création farmer
			ProtectorAgent protector = new ProtectorAgent(
					this.x,							//position x
					this.y,							//position y
					this,							//boss
					this.gang_number				//gang number
			);
			
			//ajouter un farmer 
			this.beings.addBeingsAbstractAgent(protector);
			
			//ajouter à la liste
			this.protectors.add(protector);
			
			//décrémenter l'argent
			this.richesse -= Constants.PRIX_PROTECTOR;
			
			return true;
		}
		
		return false;
	}

	/**
	 * Essaye d'embauche un farmer.
	 */
	public boolean embaucherFarmer() {
		
		//si j'ai assez d'argent pour embaucher => embaucher
		if(this.richesse >= Constants.PRIX_FARMER){
			
			//coordonnées proche du boss
			int champ_x = this.getCloseX(Constants.MAX_DISTANCE_BOSS_FARMER);
			int champ_y = this.getCloseY(Constants.MAX_DISTANCE_BOSS_FARMER);
			
			//création farmer
			FarmerAgent farmer = new FarmerAgent(
					this.x,						//position x
					this.y,							//position y
					this,							//boss
					champ_x,						//pos champ x
					champ_y,						//pos champ y
					this.gang_number				//gang number
			);
			
			//ajouter un farmer 
			this.beings.addBeingsAbstractAgent(farmer);
			
			//ajouter à la liste
			this.farmers.add(farmer);
			
			//décrémenter l'argent
			this.richesse -= Constants.PRIX_FARMER;
			
			return true;
		}
		
		return false;
	}

	/**
	 * Essaye d'embauche un supplier.
	 */
	public boolean embaucherSupplier(){
		
		//si j'ai assez d'argent pour embaucher => embaucher
		if(this.richesse >= Constants.PRIX_SUPPLIER){
			
			//coordonnées proche du boss
			int champ_x = this.getCloseX(Constants.MAX_DISTANCE_BOSS_SUPPLIER);
			int champ_y = this.getCloseY(Constants.MAX_DISTANCE_BOSS_SUPPLIER);
			
			//création farmer
			SupplierAgent supplier = new SupplierAgent(
					this.x,							//position x
					this.y,							//position y
					this,							//boss
					this.gang_number				//gang number
			);
			
			//ajouter un farmer 
			this.beings.addBeingsAbstractAgent(supplier);
			
			//ajouter à la liste
			this.suppliers.add(supplier);
			
			//décrémenter l'argent
			this.richesse -= Constants.PRIX_SUPPLIER;
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Essaye d'embauche un coocker.
	 */
	public boolean embaucherCooker(){

		//si j'ai assez d'argent pour embaucher => embaucher
		if(this.richesse >= Constants.PRIX_COOKER){
			
			//coordonnées proche du boss
			int cuisine_x = this.getCloseX(Constants.MAX_DISTANCE_BOSS_COOKER);
			int cuisine_y = this.getCloseY(Constants.MAX_DISTANCE_BOSS_COOKER);
			
			//création cooker
			CookerAgent cooker = new CookerAgent(
					this.x,						//position x
					this.y,							//position y
					this,							//boss
					cuisine_x,						//pos cuis x
					cuisine_y,						//pos cuis y
					this.gang_number				//gang number
			);
			
			//ajouter un farmer 
			this.beings.addBeingsAbstractAgent(cooker);
			
			//ajouter à la liste
			this.cookers.add(cooker);
			
			//décrémenter l'argent
			this.richesse -= Constants.PRIX_COOKER;
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Essaye d'embauche un dealer.
	 */
	public boolean embaucherDealer(){

		//si j'ai assez d'argent pour embaucher => embaucher
		if(this.richesse >= Constants.PRIX_DEALER){
			
			//calcul de la zone du dealer
			Int2D new_zone_dealer = getNewZoneDealer();
			
			//pas de zone libre
			if(new_zone_dealer == null){
				return false;
			}
			
			//création cooker
			DealerAgent dealer = new DealerAgent(
					this.x,							//position x
					this.y,							//position y
					this,							//boss
					new_zone_dealer.x,				//zone x
					new_zone_dealer.y,				//zone y
					this.gang_number				//gang number
			);
			
			//ajouter un farmer 
			this.beings.addBeingsAbstractAgent(dealer);
			
			//ajouter à la liste
			this.dealers.add(dealer);
			
			//décrémenter l'argent
			this.richesse -= Constants.PRIX_DEALER;
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Affecte un protecteur à la mission.
	 * Si il peut pas : embauche + retourne false
	 * Retourne vrai si on a pu attribuer un protecteur
	 */
	public boolean repondreMissionProtection(BeingAbstractAgent cible){

		
		//recherche un manager de libre le plus pret
		ProtectorAgent agent = null;
		
		for (int i =0; i < this.protectors.size(); i++) {
			if (!this.protectors.get(i).isOccuped()) {
				
				if(agent == null){
					
					agent = this.protectors.get(i);
					
				}else{
					
					//tester plus proche ou non
					int delta_x = Math.abs(agent.x - cible.x);
					int delta_y = Math.abs(agent.y - cible.y);
					int delta = delta_x + delta_y;
					
					int delta_x_new = Math.abs(this.protectors.get(i).x - cible.x);
					int delta_y_new = Math.abs(this.protectors.get(i).y - cible.y);
					int delta_new = delta_x_new + delta_y_new;
					
					//comparaison des deltas
					if(delta_new < delta){
						agent = this.protectors.get(i);
					}
					
					
				}
			}
		}
		
		//trouvé ?
		if(agent != null){
			agent.setMission(cible);
			return true;
		}
	
		//non
		return false;
	}

	/**
	 * Affecte un protecteur à la mission.
	 * Si il peut pas : embauche + retourne false
	 * Retourne vrai si on a pu attribuer un manager
	 */
	public boolean repondreMissionManager(DealerAgent dealer){
		
		//recherche un manager de libre le plus pret
		ManagerAgent agent = null;
		for (int i =0; i < this.managers.size(); i++) {
			if (!this.managers.get(i).isOccuped()) {
				if(agent == null){
					agent = this.managers.get(i);
				}else{
					//tester plus proche ou non
					int delta_x = Math.abs(agent.x - dealer.x);
					int delta_y = Math.abs(agent.y - dealer.y);
					int delta = delta_x + delta_y;
					
					int delta_x_new = Math.abs(this.managers.get(i).x - dealer.x);
					int delta_y_new = Math.abs(this.managers.get(i).y - dealer.y);
					int delta_new = delta_x_new + delta_y_new;
					
					//comparaison des deltas
					if(delta_new < delta){
						agent = this.managers.get(i);
					}
					
					
				}
			}
		}
		
		//trouvé ?
		if(agent != null){
			agent.setMission(dealer);
			return true;
		}
	
		//non
		return false;
	}

	/**
	 * Fonction permettant de décider ou placer un dealer.
	 * @return peut retourner null
	 */
	public Int2D getNewZoneDealer(){
		
		int pos_depart_x;
		int pos_depart_y;
		
		//pas de dealer
		if(this.boss.dealers.size() <= 0){
			
			pos_depart_x = this.boss.x;
			pos_depart_y = this.boss.y;
			return new Int2D(pos_depart_x,pos_depart_y);
			
		}else{
			
			//pour chaque dealer => regarder 4 côté
			boolean not_found = true;
			
			for(int i = 0; (i < this.boss.dealers.size()) && not_found; i++){
				
				//dealer
				int posX = this.boss.dealers.get(i).x;
				int posY = this.boss.dealers.get(i).y;
				
				//haut
				pos_depart_x = posX;
				pos_depart_y = posY;
				pos_depart_y -= Constants.HAUTEUR_ZONE_DEALER;
				not_found = isMine(pos_depart_x,pos_depart_y);
				if(!not_found){
					return new Int2D(pos_depart_x,pos_depart_y);
				}
				
				//bas
				pos_depart_x = posX;
				pos_depart_y = posY;				
				pos_depart_y += Constants.HAUTEUR_ZONE_DEALER;
				not_found = isMine(pos_depart_x,pos_depart_y);
				if(!not_found){
					return new Int2D(pos_depart_x,pos_depart_y);
				}
				
				//gauche
				pos_depart_x = posX;
				pos_depart_y = posY;				
				pos_depart_x -= Constants.LARGEUR_ZONE_DEALER;
				not_found = isMine(pos_depart_x,pos_depart_y);
				if(!not_found){
					return new Int2D(pos_depart_x,pos_depart_y);
				}
				
				//droite
				pos_depart_x = posX;
				pos_depart_y = posY;				
				pos_depart_x += Constants.LARGEUR_ZONE_DEALER;
				not_found = isMine(pos_depart_x,pos_depart_y);
				if(!not_found){
					return new Int2D(pos_depart_x,pos_depart_y);
				}
				
			}
						
		}
		
		//aucun trouvé
		return null;
	
		
	}
	
	/**
	 * Retourne vrai si un dealer couvre déjà cette zone. False sinon.
	 * TODO
	 */
	public boolean isMine(int x, int y){
		
		//regarde la zone de tous les dealers
		for(int i = 0; i < this.boss.dealers.size(); i++){
			if((this.boss.dealers.get(i).zone_x == x) && (this.boss.dealers.get(i).zone_y == y)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * X proche du boss
	 */
	public int getCloseX(int distance){
		
		//random distance entre 0 et Distance
		int random_distance = (int)(Math.random()*distance) + 1;
		
		//random direction
		double random_direction = Math.random();
		if(random_direction == 0.5){
			random_direction *= -1;
		}
			
		return this.x + random_distance;
		
	}
	
	/**
	 * Y proche du boss
	 */
	public int getCloseY(int distance){
		//random distance entre 0 et Distance
		int random_distance = (int)(Math.random()*distance) + 1;
		
		//random direction
		double random_direction = Math.random();
		if(random_direction == 0.5){
			random_direction *= -1;
		}
			
		return this.y + random_distance;
	}
	
	/**
	 * Estime la meth manquante => fourniture
	 */
	public int calculPrevisionFourniture(){
		if (this.suppliers.size() == 0) {
			return 0;
		}
		//calcul la meth manquante
		int manque_meth = 0;
		for(int i = 0; i < this.dealers.size(); i++){
			manque_meth += this.dealers.get(i).charge_meth_max - this.dealers.get(i).charge_meth_courante;
		}
		//calcul combien de fourniture il faut pour la faire
		int manque_fourniture = manque_meth / Constants.FOURNITURE_TO_METH;
		
		//division pour chaque fournisseur
		return manque_fourniture / this.suppliers.size();
	}
	
	/**
	 * Estime la meth manquante => fourniture
	 */
	public int calculPrevisionCanabis(){
		if (this.farmers.size() == 0) {
			return 0;
		}
		
		//calcul la meth manquante
		int manque_canabis = 0;
		for(int i = 0; i < this.dealers.size(); i++){
			manque_canabis += this.dealers.get(i).charge_canabis_max - this.dealers.get(i).charge_canabis_courante;
		}
		
		//division pour chaque fournisseur
		return manque_canabis / this.farmers.size();
	}
	
	/**
	 * Estime la fourniture que devrait avoir chaque cuisinier
	 */
	public int calculPrevisionCuisine(){
		if (this.cookers.size() == 0) {
			return 0;
		}
		
		//calcul la fourniture totale
		int fourniture = this.fournitures;
		for(int i = 0; i < this.cookers.size(); i++){
			fourniture += this.cookers.get(i).charge_fourniture_courante;
		}
		
		//division pour chaque cookers
		return fourniture / this.cookers.size();
	}

	/**
	 *	Fonction chargÃ©e de rÃ©cupÃ©rer de les fournitures d'un Fournisseur.
	 *	TOTEST
	 */
	public int ajouterFourniture(int i){
		if (i > 0)
			this.fournitures += i;

		return 0;
	}

	/**
	 *	Fonction chargÃ©e de rÃ©cupÃ©rer du canabis d'un cultivateur.
	 *	TOTEST
	 */
	public int ajouterCanabis(int i){
		if (i > 0)
			this.canabis += i;

		return 0;
	}

	/**
	 *	Fonction chargÃ©e de rÃ©cupÃ©rer du meth d'un cuisinier.
	 *	TOTEST
	 */
	public int ajouterMeth(int i){
		if (i > 0)
			this.meth += i;
		return 0;
	}
	
	
	
	/**
	 * Un agent manager donne son argent au boss.
	 */
	public void recevoirArgent(int montant){
		if(montant > 0){
			this.richesse += montant;
		}
	}
	
	/**
	 * Fonction permettant de donner à un manager la quantité de meth qu'il doit avoir sur lui.
	 */
	public void donnerMethManager(ManagerAgent manager){
		if(manager != null){
			
			//récupère le manque
			int manque = manager.charge_meth_max - manager.charge_meth_courante;
			
			if(manque > 0){				
				//ce que l'on peut donner
				if(manque > this.meth){
					manque = this.meth;
				}
				//donne le manque
				manager.charge_meth_courante += manque;
				//décrémente
				this.meth -= manque;
			}
		}
	}
	
	/**
	 * Fonction permettant de donner à un manager la quantité de canabis qu'il doit avoir sur lui.
	 */
	public void donnerCanabisManager(ManagerAgent manager){
		if(manager != null){
			
			//récupère le manque
			int manque = manager.charge_canabis_max - manager.charge_canabis_courante;
			
			if(manque > 0){				
				//ce que l'on peut donner
				if(manque > this.canabis){
					manque = this.canabis;
				}
				//donne le manque
				manager.charge_canabis_courante += manque;
				//décrémente
				this.canabis -= manque;
			}
		}
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
