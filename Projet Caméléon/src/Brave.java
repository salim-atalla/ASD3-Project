
public class Brave extends Plateau {
	
	// Attribus
	
	
	
	// Constructeur
	public Brave(int n, boolean initVide, char caseVide, Joueur J1, Joueur J2) {
		super(n, initVide, caseVide, J1, J2);
		
	}

	
	
	// M�thodes
	// Retourner le nombre des cases qui peut J gagn� s'il choisit la case c.
	public int EvalCase (Case c, Joueur J) {
		
		// Chercher l'id de l'autre joueur.
		char id_autre;
		if (J.id == this.J2.id) { id_autre = this.J1.id; } 
		else { id_autre = this.J2.id; }
		
		
		int nb1 = 0; // le nombre de points le joueur peut gagn�s s'il choisit la case c.
		int nb2 = 0; // Le nombre des cases remplit autour de la case c.

		
		// V�rifier que la case c est bien vide.
		if (this.estCaseLibre(c)) {
			
			// Le point de la case s�l�ctionn�e.
			nb1++; 
			nb2++;
			
			// On fait 9 it�rations pour v�rifier les cases autour de c.
			for (int i=-1; i<=1; i++) {
				for (int j=-1; j<=1; j++) {
					
					Case c_tmp = new Case (c.i+i, c.j+j);
					// Tester si la cases est bien dans le plateau.
					if (this.estCaseExiste(c_tmp)) {
						// Si oui, alors v�rifier sa couleur (l'id).
						if (this.plateau[c_tmp.i][c_tmp.j] == id_autre) {
							nb1++;
						}
					}
					// Si la case temporaire est colori�e.
					if (this.plateau[c_tmp.i][c_tmp.j] != this.caseVide) {
						nb2++;
					}
				}
			}
		}
		if (nb2 > nb1 && nb2 == 9) { return nb2; }
		return nb1;
	}
	
	// Chercher la case qui peut gagn� le plus pour le joueur J en param�tre.
	public Case chercher_meilleur_case(Joueur J) {
		
		Case case_max = this.casesVides.get(this.casesVides.size()-1); 
		int score_max = this.EvalCase(case_max, J);
		int score_courant = score_max;
		
		// V�rifier tous les cases vides pour trouver la meilleur.
		for (int i=0; i<this.casesVides.size(); i++) {
			// �valuer chaque case.
			score_courant = this.EvalCase(this.casesVides.get(i), J);
			// Chercher la meilleur case dans la liste des cases vides.
			if (score_courant > score_max) {
				score_max = score_courant;
				case_max = this.casesVides.get(i);
			}
		}
		System.out.println("Votre score sera: " + (J.Score+score_max));
		return case_max;
	}
	
	// Jouer Glouton avec Brave.
	public Case JouerGlouton (Case c, Joueur J) {
		
		Case meilleur = this.chercher_meilleur_case(J);
		this.supp_case(c);
		
		return meilleur;
	}
	
	// Applique le coloriage de la case s�l�ctionn�e avec les changements.
	public boolean Colorier (Case c, Joueur J) {  
		
		if (this.estCaseLibre(c)) {
			// Changer le couleur de la case.
			this.colorier_case(c, J);
				
			// Changer les couleur autour de la case s�l�ction�e --> 9 it�rations.
			for (int i=-1; i<=1; i++) {
				for (int j=-1; j<=1; j++) {
					
					Case c_tmp = new Case (c.i+i, c.j+j);
					// Faire les testes n�cessaires.
					if (this.estCaseExiste(c_tmp)) {
						if (!this.estCaseLibre(c_tmp) && 
								this.plateau[c_tmp.i][c_tmp.j] != J.id) {
							
							// Si tous les tests sont vrais alors,
							// colorier la case gagn�e et supprimer un point de l'autre.
							this.colorier_case(c_tmp , J);
							this.suppAutrePoint(J);
						}
					}
				}
			}
			// Afficher le jeu dans le console.
			this.afficherPlateau();
			
			return true;
		} else {
			return false;
		}
	}

	
	
	
	// Sp�cifique pour Temeraire.
	@Override
	public Case chercher_meilleur_case_IA(Joueur J) { /* Rien � faire */
		return null;
	}
	@Override
	public Case JouerIATemeraire(Case c, Joueur J) { /* Rien � faire */
		return null;
	}



	








	
} // Fin Brave.
