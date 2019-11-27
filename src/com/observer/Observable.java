/**
 * 
 */
package com.observer;

import com.Response;

/**
 * @author Hasina Develop
 * 
 */
public interface Observable {
	
	public void addObserver ( Observer obs );
	public void removeObserver ();
	public void notifyObserver ( Response res );
	
}
