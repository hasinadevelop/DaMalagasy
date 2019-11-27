package com.vue;

import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import com.Response;
import com.observer.Observable;
import com.observer.Observer;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Menu extends JMenuBar implements Observable{

	private ArrayList<Observer> listObserver = new ArrayList<Observer> ();

	public Menu() {
		this.setBounds(0, 0, 586, 27);
		
		JMenu mnGame = new JMenu("Game");
		this.add(mnGame);
		
		JMenuItem NewGame = new JMenuItem("New game");
		NewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notifyObserver(new Response("newGame"));
			}
		});
		mnGame.add(NewGame);
		
		JMenuItem SaveGame = new JMenuItem("Save game");
		mnGame.add(SaveGame);
		
		JMenuItem mntmLoadGame = new JMenuItem("Load game");
		mnGame.add(mntmLoadGame);
		
		JSeparator separator = new JSeparator();
		mnGame.add(separator);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notifyObserver(new Response("quit"));
			}
		});
		mntmQuit.setBackground(Color.LIGHT_GRAY);
		
		mnGame.add(mntmQuit);
		
		
	}

	public void addObserver (Observer obs) {
		this.listObserver.add(obs);
	}
	
	public void notifyObserver ( Response res ) {
		for ( Observer obs : this.listObserver ) {
			obs.update(res);
		}
	}
	
	public void removeObserver () {
		this.listObserver = new ArrayList<Observer> ();
	}

}
