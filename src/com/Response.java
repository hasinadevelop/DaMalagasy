/**
 * 
 */
package com;

import java.util.ArrayList;

import com.model.Case;

/**
 * @author Hasina Develop
 *
 */
public class Response {

	private String id;
	
	/**
	 * 
	 */
	public Response(String id) {
		this.setId(id);
	}

	public Case[] possiblesCoups () {
		return null;
	}
	
	public boolean hasWinner () { return false; }
	
	public int winner () { return 2; } 
	
	public int getTour () { return 2; }
	
	

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	public ArrayList<String> listCoups () {
		return new ArrayList();
	}

}
