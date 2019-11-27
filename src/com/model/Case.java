/**
 * 
 */
package com.model;

import java.awt.Color;

import javax.swing.border.LineBorder;

import com.Response;


/**
 * @author Hasina Develop
 *
 */
public class Case extends AbstractJPanelModel {

	private int i;
	private int j;
	private Color background;
	
	public Case( int x, int y, Color background ) {
		this.setI(x);
		this.setJ(y);
		this.background = background;
		this.init();
	}

	/**
	 * @return the child
	 */
	@SuppressWarnings("deprecation")
	public boolean hasChild() {
		return this.countComponents() != 0;
	}

	public Piece getChild() {
		return this.getComponents().length == 0 ? null : (Piece) this.getComponents()[0];
	}
	
	/**
	 * @param child the child to set
	 */
	public void removeChild() {
		this.removeAll();
	}
	
	public void isPossibleCoup () {
		this.setBackground(Color.BLUE);
		this.repaint();
	}
	
	public void isNotPossibleCoup () {
		init();
	}
	
	private void init () {
		this.setBorder(new LineBorder(Color.WHITE));
		this.setToolTipText("Table");
		this.setBackground(this.background);
		this.setLayout(null);
	}

	/**
	 * @return the i
	 */
	public int getI() {
		return i;
	}

	/**
	 * @param i the i to set
	 */
	public void setI(int i) {
		this.i = i;
	}

	/**
	 * @return the j
	 */
	public int getJ() {
		return j;
	}

	/**
	 * @param j the j to set
	 */
	public void setJ(int j) {
		this.j = j;
	}


}
