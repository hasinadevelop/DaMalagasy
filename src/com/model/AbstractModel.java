/**
 * 
 */
package com.model;

import java.util.ArrayList;

import com.Response;
import com.observer.Observable;
import com.observer.Observer;

/**
 * @author Hasina Develop
 *
 */
public abstract class AbstractModel implements Observable, Observer{
	
	protected ArrayList<Observer> listObserver = new ArrayList<Observer> ();
	
	public void addObserver (Observer obs) {
		this.listObserver.add(obs);
	}
	
	public void notifyObserver ( Response res ) {
		for ( Observer obs : this.listObserver ) obs.update(res);
	}
	
	public void removeObserver () {
		this.listObserver = new ArrayList<Observer> ();
	}
	
	public void update ( Response res ) {}
	
}
