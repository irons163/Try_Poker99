package com.example.try_poker99;

import java.util.ArrayList;
import java.util.List;

public class Player {
	List<Poker> pokers = new ArrayList<Poker>();
	String name;
	private boolean isLost = false;

	public Player(int id) {
		setLost(false);
		name = "ª±®a" + id;
	}

	public void getCard(Poker card) {
		pokers.add(card);
	}

	public boolean isLost() {
		return isLost;
	}

	public void setLost(boolean isLost) {
		this.isLost = isLost;
	}
}
