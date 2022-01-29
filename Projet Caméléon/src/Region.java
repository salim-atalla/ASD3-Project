
public class Region { // Quadtree
	
	/*
	 * 	 tete: c'est la premi�re case en haut_gauche de la r�gion ((i=0, j=0) pour la racine).
	 *   tete____________
	 *      |     |     |
	 *      |_HG__|_HD__|
	 *      |     |     |
	 *      |_BG__|_BD__|
	 * 
	 */
	
	// Attribus
	protected Region haut_droite;
	protected Region haut_gauche;
	protected Region bas_droite;
	protected Region bas_gauche;
	
	protected Case tete; // La premi�re case de la r�gion sur le plateau (haut-gauche). 
	protected int longueur_region; // Nombre de case dans la region sur une seule dimension.
	
	protected boolean estFeuille;
	protected char appatenant; // Cette r�gion appartenant � quel joueur.
	
	
	
	
	// Constructeur
	public Region (Plateau p, Case tete, int longueur) {
		
		this.haut_gauche = null;
		this.haut_droite = null;
		this.bas_gauche = null;
		this.bas_droite = null;
		
		this.longueur_region = longueur;
		this.tete = tete;
		
		if (this.longueur_region == 3) { this.estFeuille = true; }
		else { this.estFeuille = false; }
		
		this.appatenant = p.caseVide;
		
		if (this.longueur_region == p.nb_cases) { 
			// Si une racine.
			this.diviseRegion(p);
		}
	}
	
	// Construire une racine.
	public Region (Plateau p) {
		
		this.haut_gauche = null;
		this.haut_droite = null;
		this.bas_gauche = null;
		this.bas_droite = null;
		
		this.longueur_region = p.nb_cases;
		this.tete = new Case (0, 0);
		
		if (this.longueur_region == 3) { this.estFeuille = true; }
		else { this.estFeuille = false; }
		
		this.appatenant = p.caseVide;
		
		this.diviseRegion(p);
	}
	
	
	
	// M�thodes
	// Cr�er la quadtree.
	public Region diviseRegion (Plateau p) {
		
		if (this.estFeuille) { return this; }
		else {
			int demi_long = this.longueur_region / 2;
			
			this.haut_gauche = new Region (p, new Case (this.tete.i, this.tete.j), demi_long);
			this.haut_droite = new Region (p, new Case (this.tete.i, this.tete.j+demi_long), demi_long);
			this.bas_gauche = new Region (p, new Case (this.tete.i+demi_long, this.tete.j), demi_long);
			this.bas_droite = new Region (p, new Case (this.tete.i+demi_long, this.tete.j+demi_long), demi_long);
			
			this.haut_gauche.diviseRegion(p);
			this.haut_droite.diviseRegion(p);
			this.bas_gauche.diviseRegion(p);
			this.bas_droite.diviseRegion(p);
			
			return this;
		}
		
	}
	
	// rechercher un feuille c'est de rechercher une r�gion pour laquelle son nb_cases = 3.
	public Region rechercheFeuille (Case c) {
		
		return this.recherche(c, 3);
	}
	
	// Rechercher et retourner la r�gion de la case c,
	// et pour laquelle son nb_cases = longueur.
	public Region recherche (Case c, int longueur) {
		
		if (this.longueur_region == longueur) { return this; } 
		else {
			int demi_long = this.longueur_region/2;
			
			if (this.tete.i + demi_long > c.i) { // Haut
				if (this.tete.j + demi_long > c.j ) { // Haut_Gauche
					return this.haut_gauche.recherche(c, longueur);
				} else { // Haut_Droite
					return this.haut_droite.recherche(c, longueur);
				}
			} else { // Bas
				if (this.tete.j + demi_long > c.j ) { // Bas_Gauche
					return this.bas_gauche.recherche(c, longueur);
				} else { // Bas_Droite
					return this.bas_droite.recherche(c, longueur);
				}
			}
		}
	}

	// V�rifier si une feuille est acquise.
	public boolean estFeuilleAcquise (Plateau p) {
		char couleur = p.plateau[this.tete.i][this.tete.j];
		
		if (couleur == p.caseVide) { return false; } 
		else {
			boolean check = true;
			
			// V�rifier les cases de la r�gion (9 cases -> 9 it�rations au pire).
			for (int i=0; i<3; i++) {
				for (int j=0; j<3; j++) {
					char c = p.plateau[this.tete.i + i][this.tete.j + j];
					if (c != couleur) {
						check = false;
						break;
					}
				}
			}
			if (check) { this.appatenant = couleur; }
			return check;
		}
	}
	
	// V�rifier si une r�gion est acquise.
	public boolean estAcquise (Plateau p) {
		if (this.estFeuille) { return this.estFeuilleAcquise(p); } 
		else {
			boolean check = this.haut_gauche.estAcquise(p) &&
						  this.haut_droite.estAcquise(p) &&
						  this.bas_gauche.estAcquise(p) &&
						  this.bas_droite.estAcquise(p) &&
						  this.haut_gauche.appatenant == this.haut_droite.appatenant &&
						  this.bas_gauche.appatenant == this.bas_droite.appatenant &&
						  this.haut_gauche.appatenant == this.bas_gauche.appatenant;
			
			if (check) { this.appatenant = p.plateau[this.tete.i][this.tete.j]; }
			return check;
		}
	}
	
	// Colorier la r�gion en fonctionne de l'id du joueur J. 
	public void ColorierRegion (Plateau p, Joueur J) {
		
		Case c_deb, c_fin;
		c_deb = new Case (this.tete.i, this.tete.j);
		c_fin = new Case (c_deb.i + this.longueur_region, c_deb.j + this.longueur_region);
		
		for (int i=c_deb.i; i<c_fin.i; i++) {
			for (int j=c_deb.j; j<c_fin.j; j++) {
				
				if (p.plateau[i][j] != J.id) {
					Case c_tmp = new Case (i, j);
					if (!p.estCaseLibre(c_tmp)) {
						p.suppAutrePoint(J);
					}
					p.addPoint(J);
					p.plateau[i][j] = J.id;
				}
			}
		}
	}
	
	
	
	// V�rifier si la r�gion est compl�tement rempli.
	public boolean estRemplie (Plateau p) {
		
		Case tmp = new Case (this.tete.i, this.tete.j);
		int count = 0;
		
		while (p.estCaseExiste(tmp) &&
				p.plateau[tmp.i][tmp.j] != p.caseVide &&
				tmp.i < this.tete.i+this.longueur_region) {
			
			while (p.estCaseExiste(tmp) && 
				p.plateau[tmp.i][tmp.j] != p.caseVide &&
				tmp.j < this.tete.j+this.longueur_region) {
				
					tmp.j++;
					count++;
			}
			tmp.i++;
			tmp.j = this.tete.j;
		}
		return count == this.longueur_region * this.longueur_region;
	}
	
	
	// Colorier les r�gions en appliquant les r�gles 4 et 5 du jeu.
	public Region RemplirRegion (Plateau p, Joueur J) { 
	// J: qui a jou� le derni�r et appel� cette m�thode.
		
		if (this.estAcquise(p)) {
			if (this.appatenant == p.J1.id) {
				this.ColorierRegion(p, p.J1);
			} else {
				this.ColorierRegion(p, p.J2);
			}
			
		} else {
			if (this.estFeuille) { return this; }
			int j1 = 0, j2 = 0;
			
			// Chercher les r�gions appartenants aux joueurs.
			if (this.haut_gauche.estAcquise(p)) {
				if (this.haut_gauche.appatenant == p.J1.id) { j1++; }
				else if (this.haut_gauche.appatenant == p.J2.id) { j2++; }
			}
	
			if (this.haut_droite.estAcquise(p)) {
				if (this.haut_droite.appatenant == p.J1.id) { j1++; }
				else if (this.haut_droite.appatenant == p.J2.id) { j2++; }
			}
			
			if (this.bas_gauche.estAcquise(p)) {
				if (this.bas_gauche.appatenant == p.J1.id) { j1++; }
				else if (this.bas_gauche.appatenant == p.J2.id) { j2++; }
			}
			
			if (this.bas_droite.estAcquise(p)) {
				if (this.bas_droite.appatenant == p.J1.id) { j1++; }
				else if (this.bas_droite.appatenant == p.J2.id) { j2++; }
			}
			
			// Comparaison de nombre de r�gions acquises de chaque joueur.
			if (this.estRemplie(p)) {
				
				// Chaque joueur a deux r�gions, mais le gagnant qui joue le derni�r (idJoueur).
				if (j1 == 2 && j2 == 2) { 
					this.ColorierRegion(p, J);
					
				} else if (j1 >= 2 && j2 < j1) { // J1 qui a gagn� la r�gion.
					this.ColorierRegion(p, p.J1);
					
				} else if (j2 >= 2 && j1 < j2) { // J1 qui a gagn� la r�gion.
					this.ColorierRegion(p, p.J2);
				}
			}
			// Personne a gagn� la r�gion.
			this.haut_gauche.RemplirRegion (p, J);
			this.haut_droite.RemplirRegion (p, J);
			this.bas_gauche.RemplirRegion  (p, J);
			this.bas_droite.RemplirRegion  (p, J);
		}
		return this;
	}
	
	// Afficher la r�gion sur le console.
	public void afficherRegion (Plateau p) {
		
		int countL = 0;
		int countC = 0;
		
		int deb_i, deb_j, fin_i, fin_j;
		deb_i = this.tete.i;
		deb_j = this.tete.j;
		fin_i = this.longueur_region + deb_i;
		fin_j = this.longueur_region + deb_j;
		
		for (int i=deb_i; i<fin_i; i++) {
			for (int j=deb_j; j<fin_j; j++) {
				
				System.out.print(p.plateau[i][j] + " ");
				countL++;
				if (countL%3 == 0) { System.out.print(" "); }
			}
			System.out.println("");
			countC++;
			if (countC%3 == 0) { System.out.println(""); }
		}
	}
	

} // Fin Region.
