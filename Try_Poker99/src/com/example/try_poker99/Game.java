package com.example.try_poker99;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.SystemClock;

public class Game {
	private PokerGameTest L;
	List<Poker> poker, �P�w, �P��;
	// poker�Ψө���~���P�A�]�N�O��52�i�P���A���b�P�w�B�]���b���a��W���P�C
	List<Player> players;
	// �`���a��                           �����              �ثe����                     �ثe�Ʀr
	int howManyPlayers, whosTurn, ����, temp����, count, tempcount;
	String ���;
	boolean wait;

	public Game(PokerGameTest L) {
		this.L = L;
		init();
		L.setTitle("GameStart");
	}
	
	private void init() {
		poker = new ArrayList<Poker>();
		�P�w = new ArrayList<Poker>();
		�P�� = new ArrayList<Poker>();
		players = new ArrayList<Player>();
		��� = "";
		wait = false;
		howManyPlayers = L.playerNumber;
		count = 0;
		tempcount = 0;
		whosTurn = 0;
		���� = 1;
		temp���� = 1;
	}
	
	public void newgame() {
		// 1.�s�y52�i�P��ipoker
		for (int i = 0; i < 52; i++) {
			poker.add(new Poker(i));
		}
		// 2.�񪱮a
		for (int i = 0; i < howManyPlayers; i++) {
			players.add(new Player(i));
		}
		players.get(0).name = L.playerName;
		// 3.��poker�üƶ�i�P��
		while (poker.size() != 0) {
			int j = (int) (Math.random() * poker.size());
			�P�w.add(poker.get(j));
			poker.remove(j);
		}
		// 4.�o�_��P���a5�i�AAI�h�̷����׳]�w0/1/2 �o3�i��5�i�P
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < players.size(); j++) {
				if (j == 0 || i < L.gameLevel + 3)
					�o�P(players.get(j));
			}
		}
	}
	
	public void �o�P(Player player) {
		// ���P�_�G�p�G�P�w�ŤF�N�~�P(�P��̳��ݨ��i�P�d�b��W)
		if (�P�w.size() == 0) {
			Poker temp = �P��.get(�P��.size() - 1);// ����X�P��̤W�����i�P���temp�Ȧs
			�P��.remove(�P��.size() - 1);
			poker.addAll(�P��);// ��L�P��ipoker�}�C
			�P��.clear();// �M�ŵP��
			�P��.add(temp);// �A���~�Ȧs���P��^�P��
			while (poker.size() != 0) {// ��poker�}�C���P�üƶ�i�P�w
				int j = (int) (Math.random() * poker.size());
				�P�w.add(poker.get(j));
				poker.remove(j);
			}
		}
		// �q�P�w��P��J���a��P
		player.getCard(�P�w.get(0));
		�P�w.remove(0);
	}

	// �X�P���w�q�G���a��P���P��W�A���ɮھڳW�h�P�_�C���ܰʡB�ӭt
	public void �X�P(Player player, int i) {
		�W�h�P�_(player.pokers.get(i).getId());
		�P��.add(player.pokers.get(i));
		player.pokers.remove(i);
		if (wait == false)
			�^�X�ʧ@();
	}

	private void �^�X�ʧ@() {
		if (tempcount < 100) {
			count = tempcount;
		}
		���� = temp����;
		��� += "\n" + players.get(whosTurn).name + "�X�F" + �P��.get(�P��.size() - 1)
				+ " (" + count + ")";
		if (tempcount > 99) // �W�L99..�Ӫ��a�z�F
		{
			if (whosTurn == 0) {
				// ���a0�z�� �C������ ���X�����e��
				�����e��("You Lost");

			} else {
				��� += "\n" + players.get(whosTurn).name + " LOST";
				// ����Ӫ��a�]�ݩ�isLost�A���^�X�����N�������a�C
				players.get(whosTurn).setLost(true);
				// ��P�M�i�P��
				poker.addAll(players.get(whosTurn).pokers);
				players.get(whosTurn).pokers.clear();
			}
		}
		// ���U�@�a
		whosTurn += ����;
		whosTurn += players.size();
		whosTurn %= players.size();
		// �����鱼���q�����a
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).isLost()) {
				players.remove(i);
				if (whosTurn > i)
					whosTurn--;
				// �p�G�^�X���Фj��Q�������a�A�����Ӫ��a��A���Ф]�n��۴�1
				break;// �]���C�^�X���h�鱼�@�H�A�Ҥ@����a�N�i�Hbreak��for�j��
			}
		}
		// �Y�q�����a����F�A�����ӡA���X�����e��
		if (players.size() == 1) {
			�����e��("You Win");

		}
	}

	private void �W�h�P�_(int id) {
		tempcount = count;// count�n���X�P�P�_�Ӫ��a�S���~��[�W�h...�ҥH���Ȧs
		temp���� = ����;
		// �P��ID�������G0~12=�®�A~K 13~25=����A~K.....
		// �®�A=�k�s
		if (id == 0) {
			tempcount = 0;
		} else {
			int c = (id % 13) + 1;
			switch (c) {
			case 4:// �j��
				temp���� = (temp���� == 1) ? -1 : 1;
				break;
			case 5:// ���w
				if (whosTurn == 0 && players.size() > 2) {
					wait = true;
					final String playerName[] = new String[players.size() - 1];
					for (int i = 0; i < players.size() - 1; i++) {
						playerName[i] = players.get(i + 1).name;
					}
					AlertDialog.Builder bu = new AlertDialog.Builder(
							L.gameView.getContext());
					bu.setCancelable(false);
					bu.setTitle("choose");
					bu.setItems(playerName,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int i) {
									��� += "\n���w" + players.get(i + 1).name;
									wait = false;
									whosTurn = i + 1;
									L.�P�౱��();
								}
							});
					bu.create().show();
				}
				break;
			case 10:// �[�δ� 10
				tenAndQ(10);
				break;
			case 11:// pass
				break;
			case 12:// �[�δ�20
				tenAndQ(20);
				break;
			case 13:// �����[��99
				tempcount = 99;
				break;
			default:
				tempcount += c;
			}
		}

	}

	// �q�����a���ʧ@: ��P��@�i�X�P
	public void AI(Player ai) {
		// �ǳƤ@���u���ת��Ʀr�}�C,����AI���C�i��P
		List<Integer> �u���� = new ArrayList<Integer>();
		for (int i = 0; i < ai.pokers.size(); i++) {
			�u����.add(0);
		}
		// �]�w�u���׶���...�u���׺�X�ӳ̰������X��
		for (int i = 0; i < ai.pokers.size(); i++) {
			�W�h�P�_(ai.pokers.get(i).getId());
			if (tempcount > 99) {// �|���Ʀr�z�����P�u���׳̧C
				�u����.set(i, -10);
			} else if (ai.pokers.get(i).getId() == 0) {// �®�A
				�u����.set(i, 1);
			} else {
				int c = ((ai.pokers.get(i)).getId() % 13) + 1;
				switch (c) {
				case 4:// �j��
					�u����.set(i, 33);
					break;
				case 5:// ���w
					�u����.set(i, 32);
					break;
				case 10:// �[�δ� 10
					�u����.set(i, 25);
					break;
				case 11:// pass
					�u����.set(i, 31);
					break;
				case 12:// �[�δ�20
					�u����.set(i, 20);
					break;
				case 13:// �����[��99
					�u����.set(i, 10);
					break;
				default:
					�u����.set(i, c * 2 + 100);
				}// �A���u���׵y�L�üƤ@�U
				�u����.set(i, �u����.get(i) + (int) (Math.random() * 9));
			}
		}
		// ��X�u���׳̤j��
		int temp = -20;
		int j = 0;
		for (int i = 0; i < ai.pokers.size(); i++) {
			if (�u����.get(i) > temp) {
				j = i;
				temp = �u����.get(i);
			}
		}
		// �u���׳̤j�� �X�P
		�X�P(ai, j);
		�o�P(ai);

	}

	private void tenAndQ(final int n) {
		if (whosTurn == 0 && tempcount < 100 - n) {
			wait = true;
			Builder bu = new Builder(L.gameView.getContext());
			bu.setCancelable(false);
			bu.setNegativeButton("+" + n,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							tempcount += n;
							wait = false;
							�^�X�ʧ@();
							L.�P�౱��();
						}
					});
			bu.setPositiveButton("-" + n,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							tempcount -= n;
							wait = false;
							�^�X�ʧ@();
							L.�P�౱��();
						}
					});
			bu.show();
		} else if (tempcount >= 100 - n)
			tempcount -= n;
		else {
			tempcount += n;
		}
	}

	private void �����e��(String subTitle) {
		L.music.stop();
		wait = true;
		L.chro.stop();
		// ���a��F�A����GameOver����
		if (subTitle.equals("You Lost")) {
			L.music.sound(1);

		} else {// ���aĹ�F�A������ӭ��ġA��s����
			L.music.sound(2);
			L.sec = (SystemClock.elapsedRealtime() - L.chro.getBase()) / 1000;
			L.data.add(players.get(0).name, L.sec.toString());
		}// ���X�������
		Builder bu = new Builder(L.gameView.getContext());
		bu.setCancelable(false);
		bu.setTitle(subTitle);
		bu.setNegativeButton("�����C��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				L.setTitle("Poker_99");
				L.setContentView(L.mainView);

			}
		});
		bu.setPositiveButton("�A�@��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				L.newGame(L.mainView);

			}
		});
		bu.show();
	}
}
