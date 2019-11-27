package com.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import com.Response;
import com.observer.Observer;

/**
 * @author Hasina Develop
 *
 */
public class Dame extends AbstractModel {

	private Piece[][] pieces = new Piece[2][12];
	private Case[][] cases = new Case[8][8];
	private Piece selected_piece;
	private Case[] possibles_coups = new Case[0];
	private Case[] possibles_coups_fatals = new Case[0];
	private Response res;
	private int tour = 1;
	private Piece[] possibles_pieces_to_attaq = new Piece[0];
	private Piece[] piece_a_retirer = new Piece[0];
	private int piecesnoiresnumber = 12;
	private int piecesblanchesnumber = 12;
	private ArrayList<String> listCoups = new ArrayList<String> ();
 	
	public Dame() {
		this.init();
	}
	
	public void init () {
		this.initCases();
		this.initPieces();
		for ( int i=0; i<2; i++ ) {
			for ( int j=0; j<12; j++ ) {
				this.pieces[i][j].addObserver(this);
			}
		}
		this.listCoups.add("< Start >");
	}
	
	public void initCases () {
		for ( int i=0; i<8; i++ ) {
			for ( int j=0; j<8; j++ ) {
				if (i%2 == 0 && j%2 == 0) {
					this.cases[i][j] = new Case(i, j, Color.RED);
				} else if (i%2 == 0 && j%2 != 0) {
					this.cases[i][j] = new Case(i, j, Color.DARK_GRAY);
				} else if (i%2 != 0 && j%2 != 0) {
					this.cases[i][j] = new Case(i, j, Color.RED);
				} else {
					this.cases[i][j] = new Case(i, j, Color.DARK_GRAY);
				}
				this.cases[i][j].addObserver(this);
			}
		}
	}

	public void initPieces() {
		//Pièces noires
		this.pieces[0][0] = new Piece(this.cases[0][1], 0, 1);
		this.pieces[0][1] = new Piece(this.cases[0][3], 0, 2);
		this.pieces[0][2] = new Piece(this.cases[0][5], 0, 3);
		this.pieces[0][3] = new Piece(this.cases[0][7], 0, 4);
		this.pieces[0][4] = new Piece(this.cases[1][0], 0, 5);
		this.pieces[0][5] = new Piece(this.cases[1][2], 0, 6);
		this.pieces[0][6] = new Piece(this.cases[1][4], 0, 7);
		this.pieces[0][7] = new Piece(this.cases[1][6], 0, 8);
		this.pieces[0][8] = new Piece(this.cases[2][1], 0, 9);
		this.pieces[0][9] = new Piece(this.cases[2][3], 0, 10);
		this.pieces[0][10] = new Piece(this.cases[2][5], 0, 11);
		this.pieces[0][11] = new Piece(this.cases[2][7], 0, 12);
		
		//Pièces blanches
		this.pieces[1][0] = new Piece(this.cases[7][6], 1, 1);
		this.pieces[1][1] = new Piece(this.cases[7][4], 1, 2);
		this.pieces[1][2] = new Piece(this.cases[7][2], 1, 3);
		this.pieces[1][3] = new Piece(this.cases[7][0], 1, 4);
		this.pieces[1][4] = new Piece(this.cases[6][7], 1, 5);
		this.pieces[1][5] = new Piece(this.cases[6][5], 1, 6);
		this.pieces[1][6] = new Piece(this.cases[6][3], 1, 7);
		this.pieces[1][7] = new Piece(this.cases[6][1], 1, 8);
		this.pieces[1][8] = new Piece(this.cases[5][6], 1, 9);
		this.pieces[1][9] = new Piece(this.cases[5][4], 1, 10);
		this.pieces[1][10] = new Piece(this.cases[5][2], 1, 11);
		this.pieces[1][11] = new Piece(this.cases[5][0], 1, 12);
		
	}

	public void update ( Response res ) {
		this.res = res;
	}

	public Piece[][] getPieces() {
		return pieces;
	}

	public void setPieces(Piece[][] pieces) {
		this.pieces = pieces;
	}

	public Case[][] getCases() {
		return cases;
	}

	public void setCases(Case[][] cases) {
		this.cases = cases;
	}

	public Piece getSelected_piece() {
		return selected_piece;
	}

	public void setSelected_piece(Piece selected_piece) {
		if ( !this.hasPossibleAttaq() ) {
			if ( !selected_piece.isDame() ) {
				if ( this.selected_piece == null ) {
					if ( this.tour == selected_piece.getType() ) {
						this.selected_piece = selected_piece;
						this.setPossibles_coups();
						for ( Case pc : this.possibles_coups ) {
							pc.isPossibleCoup();
						}
					}
				} else {
					this.selected_piece = null;
					this.clearPossibles_coups();
				}
			} else {
				if ( this.selected_piece == null ) {
					if ( this.tour == selected_piece.getType() ) {
						this.selected_piece = selected_piece;
						this.setPossibles_coups_dame();
						for ( Case pc : this.possibles_coups ) {
							pc.isPossibleCoup();
						}
					}
				} else {
					this.selected_piece = null;
					this.clearPossibles_coups();
				}
			}
		} else if ( this.isOnPossibleToAttaq(selected_piece) ) {
				if ( this.selected_piece == null ) {
					if ( this.tour == selected_piece.getType() ) {
						this.selected_piece = selected_piece;
						this.setPossibles_coups_fatal();
						for ( Case pc : this.possibles_coups_fatals ) {
							pc.isPossibleCoup();
						}
					}
				} else {
					this.selected_piece = null;
					this.clearPossibles_coups();
				}
		} else {
				for ( Piece p : this.possibles_pieces_to_attaq ) {
					p.isMangeur();
				}
		}
			
		this.notifyObserver(new Response("pc"){
			public Case[] possiblesCoups () {
				return possibles_coups.length != 0 ? possibles_coups : possibles_coups_fatals;
			}
		});
	}

	
	private void setPossibles_coups_dame() {
		int caseIndexi = ((Case) this.selected_piece.getParent()).getI();
		int caseIndexj = ((Case) this.selected_piece.getParent()).getJ();
		
		this.setPossibles_coups_dame_length(caseIndexi, caseIndexj);
		
		int cii = caseIndexi;
		int cij = caseIndexj;
		
		int r = 0;
		if ( caseIndexi > 0 && caseIndexi < 7 ) {
			for ( int i=caseIndexi-1; i>=0; i-- ) {
				if ( caseIndexj-1 >= 0 && !this.cases[i][caseIndexj-1].hasChild()) {
					this.possibles_coups[r] = this.cases[i][caseIndexj-1];
					r++;
				} else {
					break;
				}
				caseIndexj--;
			}
			caseIndexi = cii;
			caseIndexj = cij;
			for ( int i=caseIndexi-1; i>=0; i-- ) {
				if ( caseIndexj+1 <= 7 && !this.cases[i][caseIndexj+1].hasChild()) {
					this.possibles_coups[r] = this.cases[i][caseIndexj+1];
					r++;
				} else {
					break;
				}
				caseIndexj++;
			}
			caseIndexi = cii;
			caseIndexj = cij;
			for ( int i=caseIndexi+1; i<=7; i++ ) {
				if ( caseIndexj-1 >= 0 && !this.cases[i][caseIndexj-1].hasChild()) {
					this.possibles_coups[r] = this.cases[i][caseIndexj-1];
					r++;
				} else {
					break;
				}
				caseIndexj--;
			}
			caseIndexi = cii;
			caseIndexj = cij;
			for ( int i=caseIndexi+1; i<=7; i++ ) {
				if ( caseIndexj+1 <= 7 && !this.cases[i][caseIndexj+1].hasChild()) {
					this.possibles_coups[r] = this.cases[i][caseIndexj+1];
					r++;
				} else {
					break;
				}
				caseIndexj++;
			}
		} else if ( caseIndexi == 0 ) {
			for ( int i=caseIndexi+1; i<=7; i++ ) {
				if ( caseIndexj-1 >= 0 && !this.cases[i][caseIndexj-1].hasChild()) {
					this.possibles_coups[r] = this.cases[i][caseIndexj-1];
					r++;
				} else {
					break;
				}
				caseIndexj--;
			}
			caseIndexi = cii;
			caseIndexj = cij;
			for ( int i=caseIndexi+1; i<=7; i++ ) {
				if ( caseIndexj+1 <= 7 && !this.cases[i][caseIndexj+1].hasChild()) {
					this.possibles_coups[r] = this.cases[i][caseIndexj+1];
					r++;
				} else {
					break;
				}
				caseIndexj++;
			}
		} else if ( caseIndexi == 7 ) {
			for ( int i=caseIndexi-1; i>=0; i-- ) {
				if ( caseIndexj-1 >= 0 && !this.cases[i][caseIndexj-1].hasChild()) {
					this.possibles_coups[r] = this.cases[i][caseIndexj-1];
					r++;
				} else {
					break;
				}
				caseIndexj--;
			}
			caseIndexi = cii;
			caseIndexj = cij;
			for ( int i=caseIndexi-1; i>=0; i-- ) {
				if ( caseIndexj+1 <= 7 && !this.cases[i][caseIndexj+1].hasChild()) {
					this.possibles_coups[r] = this.cases[i][caseIndexj+1];
					r++;
				} else {
					break;
				}
				caseIndexj++;
			}
		}
		
		
	}
	

	public Case[] getPossibles_coups() {
		return possibles_coups;
	}

	/**
	 * @param possibles_coups the possibles_coups to set
	 */
	public void setPossibles_coups() {
		
			if ( this.selected_piece != null && this.selected_piece.getType() == 1 ) {
				int caseIndexi = (int) this.getCaseIndex((Case) this.selected_piece.getParent()) / 10;
				int caseIndexj = this.getCaseIndex((Case) this.selected_piece.getParent()) % 10;
				this.setPossibiles_coups_length(caseIndexi, caseIndexj);
				int i = 0;
				
				if ( caseIndexj != 7 && caseIndexj != 0 ) {
					if (caseIndexj < 7 && caseIndexi > 0 && !this.cases[caseIndexi-1][caseIndexj+1].hasChild()) {
						this.possibles_coups[i] = this.cases[caseIndexi - 1][caseIndexj + 1];
						i++;
					} 
					if (caseIndexj > 0 && caseIndexi > 0 && !this.cases[caseIndexi-1][caseIndexj-1].hasChild()) {
						this.possibles_coups[i] = this.cases[caseIndexi - 1][caseIndexj - 1];
						i++;
					}
				} else {
					if (caseIndexj == 7 && caseIndexi > 0 && !this.cases[caseIndexi-1][caseIndexj-1].hasChild()) {
						this.possibles_coups[i] = this.cases[caseIndexi - 1][caseIndexj - 1];
						i++;
					}
					if (caseIndexj == 0 && caseIndexi > 0 && !this.cases[caseIndexi-1][caseIndexj+1].hasChild()){
						this.possibles_coups[i] = this.cases[caseIndexi - 1][caseIndexj + 1];
						i++;
					}
				}
			} else if ( this.selected_piece != null && this.selected_piece.getType() == 0 ){
				int caseIndexi = (int) this.getCaseIndex((Case) this.selected_piece.getParent()) / 10;
				int caseIndexj = this.getCaseIndex((Case) this.selected_piece.getParent()) % 10;
				this.setPossibiles_coups_length(caseIndexi, caseIndexj);
				int i = 0;
				
				if ( caseIndexj != 7 && caseIndexj != 0 ) {
					if (caseIndexj < 7 && caseIndexi < 7 && !this.cases[caseIndexi+1][caseIndexj+1].hasChild()) {
						this.possibles_coups[i] = this.cases[caseIndexi + 1][caseIndexj + 1];
						i++;
					} 
					if (caseIndexj > 0 && caseIndexi < 7 && !this.cases[caseIndexi+1][caseIndexj-1].hasChild()) {
						this.possibles_coups[i] = this.cases[caseIndexi + 1][caseIndexj - 1];
						i++;
					}
				} else {
					if (caseIndexj == 7 && caseIndexi < 7 && !this.cases[caseIndexi+1][caseIndexj-1].hasChild()) {
						this.possibles_coups[i] = this.cases[caseIndexi + 1][caseIndexj - 1];
						i++;
					}
					if (caseIndexj == 0 && caseIndexi < 7 && !this.cases[caseIndexi+1][caseIndexj+1].hasChild()){
						this.possibles_coups[i] = this.cases[caseIndexi + 1][caseIndexj + 1];
						i++;
					}
				}
			} 
		
		
		
		
	}
	
	public void setPossibles_coups_fatal () {
		if ( this.selected_piece != null && this.isPossibleAttaq(this.selected_piece)) {
			if ( !this.selected_piece.isDame() ) {
				int caseIndexi = (int) this.getCaseIndex((Case) this.selected_piece.getParent()) / 10;
				int caseIndexj = this.getCaseIndex((Case) this.selected_piece.getParent()) % 10;
				
				Case chg = null, chd = null, cbg = null, cbd = null;
				try {
					chg = this.cases[caseIndexi - 1][caseIndexj - 1];
				} catch (Exception e1) {}
				try {
					chd = this.cases[caseIndexi - 1][caseIndexj + 1];
				} catch (Exception e1) {}
				try {
					cbg = this.cases[caseIndexi + 1][caseIndexj - 1];
				} catch (Exception e1) {}
				try{
					cbd = this.cases[caseIndexi + 1][caseIndexj + 1];
				} catch (Exception e1) {}
				
				int i = 0;
				if ( chg != null && chg.getChild() != null && chg.getChild().getType() != this.tour ) {
					if ( caseIndexi - 2 >= 0 && caseIndexj - 2 >= 0 && !this.cases[caseIndexi-2][caseIndexj-2].hasChild() ) {
						this.possibles_coups_fatals[i] = this.cases[caseIndexi-2][caseIndexj-2];
						this.piece_a_retirer[i] = chg.getChild();
						i++;
					}
				}
				if ( chd != null && chd.getChild() != null && chd.getChild().getType() != this.tour ) {
					if ( caseIndexi - 2 >= 0 && caseIndexj + 2 <= 7 && !this.cases[caseIndexi-2][caseIndexj+2].hasChild() ) {
						this.possibles_coups_fatals[i] = this.cases[caseIndexi-2][caseIndexj+2];
						this.piece_a_retirer[i] = chd.getChild();
						i++;
					}
				}
				if ( cbg != null && cbg.getChild() != null && cbg.getChild().getType() != this.tour ) {
					if ( caseIndexi + 2 <= 7 && caseIndexj - 2 >= 0 && !this.cases[caseIndexi+2][caseIndexj-2].hasChild() ) {
						this.possibles_coups_fatals[i] = this.cases[caseIndexi+2][caseIndexj-2];
						this.piece_a_retirer[i] = cbg.getChild();
						i++;
					}
				}
				if ( cbd != null && cbd.getChild() != null && cbd.getChild().getType() != this.tour ) {
					if ( caseIndexi + 2 <= 7 && caseIndexj + 2 <= 7 && !this.cases[caseIndexi+2][caseIndexj+2].hasChild() ) {
						this.possibles_coups_fatals[i] = this.cases[caseIndexi+2][caseIndexj+2];
						this.piece_a_retirer[i] = cbd.getChild();
						i++;
					}
				}
			} else {
				
				Case c = (Case) this.selected_piece.getParent();
				int caseIndexi = (int) this.getCaseIndex(c) / 10;
				int caseIndexj = this.getCaseIndex(c) % 10;
				
				int cii = caseIndexi;
				int cij = caseIndexj;
				
				
				Case cahg = null, cahd = null, cabg = null, cabd = null;
				if ( caseIndexi > 0 && caseIndexi < 7 ) {
					for ( int i=caseIndexi-1; i>=0; i-- ) {
						if ( caseIndexj-1 >= 0 && this.cases[i][caseIndexj-1].hasChild() && this.cases[i][caseIndexj-1].getChild().getType() != this.tour ) {
							cahg = this.cases[i][caseIndexj-1];
							break;
						}
						caseIndexj--;
					}
					caseIndexi = cii;
					caseIndexj = cij;
					for ( int i=caseIndexi-1; i>=0; i-- ) {
						if ( caseIndexj+1 <= 7 && this.cases[i][caseIndexj+1].hasChild() && this.cases[i][caseIndexj+1].getChild().getType() != this.tour ) {
							cahd = this.cases[i][caseIndexj+1];
							break;
						}
						caseIndexj++;
					}
					caseIndexi = cii;
					caseIndexj = cij;
					for ( int i=caseIndexi+1; i<=7; i++ ) {
						if ( caseIndexj-1 >= 0 && this.cases[i][caseIndexj-1].hasChild() && this.cases[i][caseIndexj-1].getChild().getType() != this.tour ) {
							cabg = this.cases[i][caseIndexj-1];
							break;
						} 
						caseIndexj--;
					}
					caseIndexi = cii;
					caseIndexj = cij;
					for ( int i=caseIndexi+1; i<=7; i++ ) {
						if ( caseIndexj+1 <= 7 && this.cases[i][caseIndexj+1].hasChild() && this.cases[i][caseIndexj+1].getChild().getType() != this.tour ) {
							cabd = this.cases[i][caseIndexj+1];
						}
						caseIndexj++;
					}
				} else if ( caseIndexi == 0 ) {
					for ( int i=caseIndexi+1; i<=7; i++ ) {
						if ( caseIndexj-1 >= 0 && this.cases[i][caseIndexj-1].hasChild() && this.cases[i][caseIndexj-1].getChild().getType() != this.tour ) {
							cabg = this.cases[i][caseIndexj-1];
							break;
						} 
						caseIndexj--;
					}
					caseIndexi = cii;
					caseIndexj = cij;
					for ( int i=caseIndexi+1; i<=7; i++ ) {
						if ( caseIndexj+1 <= 7 && this.cases[i][caseIndexj+1].hasChild() && this.cases[i][caseIndexj+1].getChild().getType() != this.tour ) {
							cabd = this.cases[i][caseIndexj+1];
						}
						caseIndexj++;
					}
				} else if ( caseIndexi == 7 ) {
					for ( int i=caseIndexi-1; i>=0; i-- ) {
						if ( caseIndexj-1 >= 0 && this.cases[i][caseIndexj-1].hasChild() && this.cases[i][caseIndexj-1].getChild().getType() != this.tour ) {
							cahg = this.cases[i][caseIndexj-1];
							break;
						}
						caseIndexj--;
					}
					caseIndexi = cii;
					caseIndexj = cij;
					for ( int i=caseIndexi-1; i>=0; i-- ) {
						if ( caseIndexj+1 <= 7 && this.cases[i][caseIndexj+1].hasChild() && this.cases[i][caseIndexj+1].getChild().getType() != this.tour ) {
							cahd = this.cases[i][caseIndexj+1];
							break;
						}
						caseIndexj++;
					}
				}
				
				int r = 0;
				if ( cahg != null ) {
					if ( cahg.getI() > 0 && cahg.getJ() > 0  ) {
						int ci = cahg.getI(), cj = cahg.getJ();
						for ( int i=ci-1; i>=0; i-- ) {
							if ( cj-1 >= 0 && !this.cases[i][cj-1].hasChild()) {
								this.possibles_coups_fatals[r] = this.cases[i][cj-1];
								this.piece_a_retirer[r] = cahg.getChild();
								r++;
							} else {
								break;
							}
							cj--;
						}
					}
				}
				if ( cahd != null ) {
					if ( cahd.getI() > 0 && cahd.getJ() < 7  ) {
						int ci = cahd.getI(), cj = cahd.getJ();
						for ( int i=ci-1; i>=0; i-- ) {
							if ( cj+1 <= 7 && !this.cases[i][cj+1].hasChild()) {
								this.possibles_coups_fatals[r] = this.cases[i][cj+1];
								this.piece_a_retirer[r] = cahd.getChild();
								r++;
							} else {
								break;
							}
							cj++;
						}
					}
				}
				if ( cabg != null ) {
					if ( cabg.getI() < 7 && cabg.getJ() > 0  ) {
						int ci = cabg.getI(), cj = cabg.getJ();
						for ( int i=ci+1; i<=7; i++ ) {
							if ( cj-1 >= 0 && !this.cases[i][cj-1].hasChild()) {
								this.possibles_coups_fatals[r] = this.cases[i][cj-1];
								this.piece_a_retirer[r] = cabg.getChild();
								r++;
							} else {
								break;
							}
							cj--;
						}
					}
				}
				if ( cabd != null ) {
					if ( cabd.getI() < 7 && cabd.getJ() < 7  ) {
						int ci = cabd.getI(), cj = cabd.getJ();
						for ( int i=ci+1; i<=7; i++ ) {
							if ( cj+1 <= 7 && !this.cases[i][cj+1].hasChild()) {
								this.possibles_coups_fatals[r] = this.cases[i][cj+1];
								this.piece_a_retirer[r] = cabd.getChild();
								r++;
							} else {
								break;
							}
							cj++;
						}
					}
				}
				
			}
		}
	}
	
	private void clearPossibles_coups () {
		if ( this.possibles_coups_fatals.length == 0 ) {
			for ( Case pc : this.possibles_coups ) {
				for ( MouseListener ml : pc.getMouseListeners() ) {
					pc.removeMouseListener(ml);
				}
			}
			this.possibles_coups = new Case[0];
		} else {
			for ( Case pc : this.possibles_coups_fatals ) {
				for ( MouseListener ml : pc.getMouseListeners() ) {
					pc.removeMouseListener(ml);
				}
			}
			for ( Case c : this.possibles_coups_fatals ) {
				c.isNotPossibleCoup();
			}
			this.possibles_coups_fatals = new Case[0];
			this.piece_a_retirer = new Piece[0];
		}
		if ( this.possibles_pieces_to_attaq.length != 0 ) {
			for ( Piece p : this.possibles_pieces_to_attaq ) {
				p.isNotMangeur();
			}
			this.possibles_pieces_to_attaq = new Piece[0];
		}
	}
	
	public int getCaseIndex ( Case c ) {
		int x = 0, y = 0;
		for ( int i=0; i<8; i++ ) {
			for ( int j=0; j<8; j++ ) {
				if ( this.cases[i][j] == c ) {
					x = i; y = j;
				}
			}
		}
		
		return x*10 + y;
	}
	
	public void setPossibiles_coups_length (int caseIndexi, int caseIndexj) {
		int res = 0;
		if ( this.selected_piece.getType() == 1 ){
			if ( caseIndexj != 7 && caseIndexj != 0 ) {
				if (caseIndexj < 7 && caseIndexi > 0 && !this.cases[caseIndexi-1][caseIndexj+1].hasChild()) {
					res += 1;
				} 
				if (caseIndexj > 0 && caseIndexi > 0 && !this.cases[caseIndexi-1][caseIndexj-1].hasChild()) {
					res += 1;
				}
			} else {
				if (caseIndexj == 7 && caseIndexi > 0 && !this.cases[caseIndexi-1][caseIndexj-1].hasChild()) {
					res += 1;
				}
				if (caseIndexj == 0 && caseIndexi > 0 && !this.cases[caseIndexi-1][caseIndexj+1].hasChild()){
					res += 1;
				}
			}
			
		} else {
			if ( caseIndexj != 7 && caseIndexj != 0 ) {
				if (caseIndexj < 7 && caseIndexi < 7 && !this.cases[caseIndexi+1][caseIndexj+1].hasChild()) {
					res += 1;
				} 
				if (caseIndexj > 0 && caseIndexi < 7 && !this.cases[caseIndexi+1][caseIndexj-1].hasChild()) {
					res += 1;
				}
			} else {
				if (caseIndexj == 7 && caseIndexi < 7 && !this.cases[caseIndexi+1][caseIndexj-1].hasChild()) {
					res += 1;
				}
				if (caseIndexj == 0 && caseIndexi < 7 && !this.cases[caseIndexi+1][caseIndexj+1].hasChild()){
					res += 1;
				}
			}
		}
		this.possibles_coups = new Case[res];
	}

	public void setPossibles_coups_dame_length (int caseIndexi, int caseIndexj) {
		
		int cii = caseIndexi;
		int cij = caseIndexj;
		
		int r = 0;
		if ( caseIndexi > 0 && caseIndexi < 7 ) {
			for ( int i=caseIndexi-1; i>=0; i-- ) {
				if ( caseIndexj-1 >= 0 && !this.cases[i][caseIndexj-1].hasChild()) {
					r++;
				} else {
					break;
				}
				caseIndexj--;
			}
			caseIndexi = cii;
			caseIndexj = cij;
			for ( int i=caseIndexi-1; i>=0; i-- ) {
				if ( caseIndexj+1 <= 7 && !this.cases[i][caseIndexj+1].hasChild()) {
					r++;
				} else {
					break;
				}
				caseIndexj++;
			}
			caseIndexi = cii;
			caseIndexj = cij;
			for ( int i=caseIndexi+1; i<=7; i++ ) {
				if ( caseIndexj-1 >= 0 && !this.cases[i][caseIndexj-1].hasChild()) {
					r++;
				} else {
					break;
				}
				caseIndexj--;
			}
			caseIndexi = cii;
			caseIndexj = cij;
			for ( int i=caseIndexi+1; i<=7; i++ ) {
				if ( caseIndexj+1 <= 7 && !this.cases[i][caseIndexj+1].hasChild()) {
					r++;
				} else {
					break;
				}
				caseIndexj++;
			}
		} else if ( caseIndexi == 0 ) {
			for ( int i=caseIndexi+1; i<=7; i++ ) {
				if ( caseIndexj-1 >= 0 && !this.cases[i][caseIndexj-1].hasChild()) {
					r++;
				} else {
					break;
				}
				caseIndexj--;
			}
			caseIndexi = cii;
			caseIndexj = cij;
			for ( int i=caseIndexi+1; i<=7; i++ ) {
				if ( caseIndexj+1 <= 7 && !this.cases[i][caseIndexj+1].hasChild()) {
					r++;
				} else {
					break;
				}
				caseIndexj++;
			}
		} else if ( caseIndexi == 7 ) {
			for ( int i=caseIndexi-1; i>=0; i-- ) {
				if ( caseIndexj-1 >= 0 && !this.cases[i][caseIndexj-1].hasChild()) {
					r++;
				} else {
					break;
				}
				caseIndexj--;
			}
			caseIndexi = cii;
			caseIndexj = cij;
			for ( int i=caseIndexi-1; i>=0; i-- ) {
				if ( caseIndexj+1 <= 7 && !this.cases[i][caseIndexj+1].hasChild()) {
					r++;
				} else {
					break;
				}
				caseIndexj++;
			}
		}
		
			
		this.possibles_coups = new Case[r];
	}
	
	public boolean isPossibleAttaq ( Piece p ) {
		if ( !p.isDame() ) {
			Case c = (Case) p.getParent();
			int caseIndexi = (int) this.getCaseIndex(c) / 10;
			int caseIndexj = this.getCaseIndex(c) % 10;
			
			Case chg = null, chd = null, cbg = null, cbd = null;
			try {
				chg = this.cases[caseIndexi - 1][caseIndexj - 1];
			} catch (Exception e1) {}
			try {
				chd = this.cases[caseIndexi - 1][caseIndexj + 1];
			} catch (Exception e1) {}
			try {
				cbg = this.cases[caseIndexi + 1][caseIndexj - 1];
			} catch (Exception e1) {}
			try{
				cbd = this.cases[caseIndexi + 1][caseIndexj + 1];
			} catch (Exception e1) {}
			
			int i = 0;
			if ( chg != null && chg.getChild() != null && chg.getChild().getType() != this.tour ) {
				if ( caseIndexi - 2 >= 0 && caseIndexj - 2 >= 0 && !this.cases[caseIndexi-2][caseIndexj-2].hasChild() ) {
					i++;
				}
			}
			if ( chd != null && chd.getChild() != null && chd.getChild().getType() != this.tour ) {
				if ( caseIndexi - 2 >= 0 && caseIndexj + 2 <= 7 && !this.cases[caseIndexi-2][caseIndexj+2].hasChild() ) {
					i++;
				}
			}
			if ( cbg != null && cbg.getChild() != null && cbg.getChild().getType() != this.tour ) {
				if ( caseIndexi + 2 <= 7 && caseIndexj - 2 >= 0 && !this.cases[caseIndexi+2][caseIndexj-2].hasChild() ) {
					i++;
				}
			}
			if ( cbd != null && cbd.getChild() != null && cbd.getChild().getType() != this.tour ) {
				if ( caseIndexi + 2 <= 7 && caseIndexj + 2 <= 7 && !this.cases[caseIndexi+2][caseIndexj+2].hasChild() ) {
					i++;
				}
			}
			
			if ( i > 0 ) {
				this.possibles_coups_fatals = new Case[i];
				this.piece_a_retirer = new Piece[i];
				return true;
			} 
		} else {
			
			Case c = (Case) p.getParent();
			int caseIndexi = (int) this.getCaseIndex(c) / 10;
			int caseIndexj = this.getCaseIndex(c) % 10;
			
			int cii = caseIndexi;
			int cij = caseIndexj;
			
			
			Case cahg = null, cahd = null, cabg = null, cabd = null;
			if ( caseIndexi > 0 && caseIndexi < 7 ) {
				for ( int i=caseIndexi-1; i>=0; i-- ) {
					if ( caseIndexj-1 >= 0 && this.cases[i][caseIndexj-1].hasChild() && this.cases[i][caseIndexj-1].getChild().getType() != this.tour ) {
						cahg = this.cases[i][caseIndexj-1];
						break;
					}
					caseIndexj--;
				}
				caseIndexi = cii;
				caseIndexj = cij;
				for ( int i=caseIndexi-1; i>=0; i-- ) {
					if ( caseIndexj+1 <= 7 && this.cases[i][caseIndexj+1].hasChild() && this.cases[i][caseIndexj+1].getChild().getType() != this.tour ) {
						cahd = this.cases[i][caseIndexj+1];
						break;
					}
					caseIndexj++;
				}
				caseIndexi = cii;
				caseIndexj = cij;
				for ( int i=caseIndexi+1; i<=7; i++ ) {
					if ( caseIndexj-1 >= 0 && this.cases[i][caseIndexj-1].hasChild() && this.cases[i][caseIndexj-1].getChild().getType() != this.tour ) {
						cabg = this.cases[i][caseIndexj-1];
						break;
					} 
					caseIndexj--;
				}
				caseIndexi = cii;
				caseIndexj = cij;
				for ( int i=caseIndexi+1; i<=7; i++ ) {
					if ( caseIndexj+1 <= 7 && this.cases[i][caseIndexj+1].hasChild() && this.cases[i][caseIndexj+1].getChild().getType() != this.tour ) {
						cabd = this.cases[i][caseIndexj+1];
					}
					caseIndexj++;
				}
			} else if ( caseIndexi == 0 ) {
				for ( int i=caseIndexi+1; i<=7; i++ ) {
					if ( caseIndexj-1 >= 0 && this.cases[i][caseIndexj-1].hasChild() && this.cases[i][caseIndexj-1].getChild().getType() != this.tour ) {
						cabg = this.cases[i][caseIndexj-1];
						break;
					} 
					caseIndexj--;
				}
				caseIndexi = cii;
				caseIndexj = cij;
				for ( int i=caseIndexi+1; i<=7; i++ ) {
					if ( caseIndexj+1 <= 7 && this.cases[i][caseIndexj+1].hasChild() && this.cases[i][caseIndexj+1].getChild().getType() != this.tour ) {
						cabd = this.cases[i][caseIndexj+1];
					}
					caseIndexj++;
				}
			} else if ( caseIndexi == 7 ) {
				for ( int i=caseIndexi-1; i>=0; i-- ) {
					if ( caseIndexj-1 >= 0 && this.cases[i][caseIndexj-1].hasChild() && this.cases[i][caseIndexj-1].getChild().getType() != this.tour ) {
						cahg = this.cases[i][caseIndexj-1];
						break;
					}
					caseIndexj--;
				}
				caseIndexi = cii;
				caseIndexj = cij;
				for ( int i=caseIndexi-1; i>=0; i-- ) {
					if ( caseIndexj+1 <= 7 && this.cases[i][caseIndexj+1].hasChild() && this.cases[i][caseIndexj+1].getChild().getType() != this.tour ) {
						cahd = this.cases[i][caseIndexj+1];
						break;
					}
					caseIndexj++;
				}
			}
			
			int r = 0;
			if ( cahg != null ) {
				if ( cahg.getI() > 0 && cahg.getJ() > 0  ) {
					int ci = cahg.getI(), cj = cahg.getJ();
					for ( int i=ci-1; i>=0; i-- ) {
						if ( cj-1 >= 0 && !this.cases[i][cj-1].hasChild()) {
							r++;
						} else {
							break;
						}
						cj--;
					}
				}
			}
			if ( cahd != null ) {
				if ( cahd.getI() > 0 && cahd.getJ() < 7  ) {
					int ci = cahd.getI(), cj = cahd.getJ();
					for ( int i=ci-1; i>=0; i-- ) {
						if ( cj+1 <= 7 && !this.cases[i][cj+1].hasChild()) {
							r++;
						} else {
							break;
						}
						cj++;
					}
				}
			}
			if ( cabg != null ) {
				if ( cabg.getI() < 7 && cabg.getJ() > 0  ) {
					int ci = cabg.getI(), cj = cabg.getJ();
					for ( int i=ci+1; i<=7; i++ ) {
						if ( cj-1 >= 0 && !this.cases[i][cj-1].hasChild()) {
							r++;
						} else {
							break;
						}
						cj--;
					}
				}
			}
			if ( cabd != null ) {
				if ( cabd.getI() < 7 && cabd.getJ() < 7  ) {
					int ci = cabd.getI(), cj = cabd.getJ();
					for ( int i=ci+1; i<=7; i++ ) {
						if ( cj+1 <= 7 && !this.cases[i][cj+1].hasChild()) {
							r++;
						} else {
							break;
						}
						cj++;
					}
				}
			}
			
			if ( r > 0 ) {
				this.possibles_coups_fatals = new Case[r];
				this.piece_a_retirer = new Piece[r];
				return true;
			} 
			
		}
		return false;
	}
	
	public void deplaceSelected_piece(Case finish) {
		if ( this.possibles_coups_fatals.length != 0 ) {
			for ( int i=0; i<this.possibles_coups_fatals.length; i++ ) {
				if ( this.possibles_coups_fatals[i] == finish ) {
					int index = this.selected_piece.getType() == 1 ? 0 : 1;
					for ( int j=0; j<12; j++ ) {
						if ( this.piece_a_retirer[i] == this.pieces[index][j] ) {
							this.pieces[index][j].remove();
							if ( index == 0 ) {
								this.piecesnoiresnumber--;
							} else if ( index == 1 ) {
								this.piecesblanchesnumber--;
							}
						}
					}
				}
			}
			this.addCoups(((Case) this.selected_piece.getParent()).getI(), ((Case) this.selected_piece.getParent()).getJ(), finish.getI(), finish.getJ());
			this.selected_piece.setParent(finish);
			this.clearPossibles_coups();
			
			this.hasPossibleAttaq();
			if ( this.isOnPossibleToAttaq(finish.getChild()) ) {
				this.selected_piece = null;
				this.setSelected_piece(finish.getChild());
			} else {
				this.isDame();
				this.reColor();
				this.reinit();
			}
			this.notifyObserver(new Response("winner"){
				public boolean hasWinner () {
					if ( piecesblanchesnumber <= 0 || piecesnoiresnumber <= 0 ) {
						return true;
					}
					return false;
				}
				
				public int winner () {
					if ( piecesblanchesnumber <= 0 ) {
						return 0;
					} 
					return 1;
				}
			});
		} else {
			this.addCoups(((Case) this.selected_piece.getParent()).getI(), ((Case) this.selected_piece.getParent()).getJ(), finish.getI(), finish.getJ());
			this.selected_piece.setParent(finish);
			this.isDame();
			this.reColor();
			this.reinit();
		}
	}
	
	public boolean isDame () {
		if ( !this.selected_piece.isDame() ) {
			if ( this.selected_piece.getType() == 0 && ((Case) this.selected_piece.getParent()).getI() == 7 ) {
				this.selected_piece.setDame(true);
				return true;
			} else if ( this.selected_piece.getType() == 1 && ((Case) this.selected_piece.getParent()).getI() == 0 ) {
				this.selected_piece.setDame(true);
				return true;
			} 
			return false;
		} 
		return true;
	}
	
	private void reinit () {
		this.clearPossibles_coups();
		this.tour = this.tour == 1 ? 0 : 1;
		this.selected_piece = null;
		this.notifyObserver(new Response("changeTour"){
			public int getTour () {
				return tour;
			}
		});
	}

	
	private boolean hasPossibleAttaq () {
		
		int i = 0;
		for ( Piece p : this.pieces[this.tour] ) {
			if ( this.isPossibleAttaq(p) ) {
				i++;
			}
		}
		this.possibles_pieces_to_attaq = new Piece[i];
		i = 0;
		for ( Piece p : this.pieces[this.tour] ) {
			if ( this.isPossibleAttaq(p) ) {
				this.possibles_pieces_to_attaq[i] = p;
				i++;
			}
		}
		if ( this.possibles_pieces_to_attaq.length == 0 ) {
			return false;
		}
		return true;
	}

	private boolean isOnPossibleToAttaq ( Piece selected_piece ) {
		for ( Piece p : this.possibles_pieces_to_attaq ) {
			if ( selected_piece == p ) {
				return true;
			}
		}
		return false;
	}

	public void reColor() {
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++){
				if (i%2 == 0 && j%2 == 0) {
					this.cases[i][j].setBackground(Color.RED);
				} else if (i%2 == 0 && j%2 != 0) {
					this.cases[i][j].setBackground(Color.DARK_GRAY);
				} else if (i%2 != 0 && j%2 != 0) {
					this.cases[i][j].setBackground(Color.RED);
				} else {
					this.cases[i][j].setBackground(Color.DARK_GRAY);
				}
				this.cases[i][j].repaint();
			}
		}
		
	}
	
	public void addCoups (int is, int js, int ie, int je) {
		String tour_color = this.tour == 0 ? "Black" : "White";
		is = is + 1;
		ie = ie + 1;
		is = is==1 ? 8 : is==2 ? 7 : is==3 ? 6 : is==4 ? 5 : is==5 ? 4 : is==6 ? 3 : is==7 ? 2 : 1; 
		ie = ie==1 ? 8 : ie==2 ? 7 : ie==3 ? 6 : ie==4 ? 5 : ie==5 ? 4 : ie==6 ? 3 : ie==7 ? 2 : 1; 
		String istart = "" + is;
		String iend = "" + ie;
		String jstart = js==0 ? "a" : js==1 ? "b" : js==2 ? "c" : js==3 ? "d" : js==4 ? "e" : js==5 ? "f" : js==6 ? "g" : "h";
		String jend = je==0 ? "a" : je==1 ? "b" : je==2 ? "c" : je==3 ? "d" : je==4 ? "e" : je==5 ? "f" : je==6 ? "g" : "h";
		this.listCoups.add(tour_color + ": " + jstart + istart + " To " + jend + iend);
		this.notifyObserver(new Response("listCoups"){
			public ArrayList<String> listCoups () {
				return listCoups;
			}
		});
	}

}

