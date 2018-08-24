package com.example.try_poker99;

public class Poker {
	private int id; // 0~51 牌的代號
	Poker(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	@Override // 複寫Object的toString()類別，呼叫時回傳牌號和花色
	public String toString() {
		String s = "";
		int i = (id % 13) + 1;// 牌號
		int j = (int) ((id) / 13);// 花色
		switch (j) {
		case 0:
			s += "黑桃";
			break;
		case 1:
			s += "紅心";
			break;
		case 2:
			s += "磚塊";
			break;
		case 3:
			s += "梅花";
			break;
		}
		switch (i) {
		case 1:
			s += "A";
			break;
		case 11:
			s += "J";
			break;
		case 12:
			s += "Q";
			break;
		case 13:
			s += "K";
			break;
		default:
			s += "" + i;
		}
		return s;
	}
}