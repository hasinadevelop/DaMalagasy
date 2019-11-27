/**
 * 
 */
package com.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import com.Response;


/**
 * @author Hasina Develop
 *
 */
public class Piece extends AbstractJPanelModel {

	private Case parent;
	private int type;
	private int index;
	private boolean isDame;
	
	public Piece( Case parent, int type, int index ) {
		this.parent = parent;
		this.type = type;
		this.index = index;
		this.init();
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Case parent) {
		Case old = this.parent;
		this.parent = parent;
		this.parent.add(this);
		old.repaint();
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
	public void remove () {
		this.parent.remove(this);
		this.parent.repaint();
	}
	
	public void isMangeur () {
		this.setBorder(new LineBorder(new Color(255, 0, 0), 35, true));
	}
	
	public void isNotMangeur () {
		if ( !this.isDame ) {
			init();
		} else {
			setDame(true);
		}
	}
	
	public boolean isDame () {
		return this.isDame;
	}
	
	public void setDame (boolean d) {
		this.isDame = d;
		if ( this.type == 0 ) {
			this.setBorder(new LineBorder(new Color(0, 0, 255), 35, true));
			this.setToolTipText("Pièce Noire Dame");
		} else {
			this.setBorder(new LineBorder(new Color(0, 255, 0), 35, true));
			this.setToolTipText("Pièce Blanche Dame");
		}
	}
	
	private void init () {
		if ( this.type == 0 ) {
			this.setToolTipText("Pièce Noire");
			this.setBackground(Color.DARK_GRAY);
			this.setBorder(new LineBorder(new Color(0, 0, 0), 35, true));
			this.setBounds(10, 11, 35, 35);
			this.parent.add(this);
			this.setLayout(new BorderLayout(0, 0));
		} else {
			this.setToolTipText("Pièce Blanche");
			this.setBackground(Color.DARK_GRAY);
			this.setBorder(new LineBorder(new Color(255, 255, 255), 35, true));
			this.setBounds(10, 11, 35, 35);
			this.parent.add(this);
			this.setLayout(new BorderLayout(0, 0));
		}
	}

}
