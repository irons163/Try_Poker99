package com.example.try_poker99;

public class Poker {
	private int id; // 0~51 �P���N��
	Poker(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	@Override // �ƼgObject��toString()���O�A�I�s�ɦ^�ǵP���M���
	public String toString() {
		String s = "";
		int i = (id % 13) + 1;// �P��
		int j = (int) ((id) / 13);// ���
		switch (j) {
		case 0:
			s += "�®�";
			break;
		case 1:
			s += "����";
			break;
		case 2:
			s += "�j��";
			break;
		case 3:
			s += "����";
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