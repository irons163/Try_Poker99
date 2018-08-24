package com.example.try_poker99;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.SystemClock;

public class Game {
	private PokerGameTest L;
	List<Poker> poker, 牌庫, 牌桌;
	// poker用來放場外的牌，也就是指52張牌中，不在牌庫、也不在玩家手上的牌。
	List<Player> players;
	// 總玩家數                           輪到誰              目前順序                     目前數字
	int howManyPlayers, whosTurn, 順序, temp順序, count, tempcount;
	String 顯示;
	boolean wait;

	public Game(PokerGameTest L) {
		this.L = L;
		init();
		L.setTitle("GameStart");
	}
	
	private void init() {
		poker = new ArrayList<Poker>();
		牌庫 = new ArrayList<Poker>();
		牌桌 = new ArrayList<Poker>();
		players = new ArrayList<Player>();
		顯示 = "";
		wait = false;
		howManyPlayers = L.playerNumber;
		count = 0;
		tempcount = 0;
		whosTurn = 0;
		順序 = 1;
		temp順序 = 1;
	}
	
	public void newgame() {
		// 1.製造52張牌放進poker
		for (int i = 0; i < 52; i++) {
			poker.add(new Poker(i));
		}
		// 2.放玩家
		for (int i = 0; i < howManyPlayers; i++) {
			players.add(new Player(i));
		}
		players.get(0).name = L.playerName;
		// 3.把poker亂數塞進牌堆
		while (poker.size() != 0) {
			int j = (int) (Math.random() * poker.size());
			牌庫.add(poker.get(j));
			poker.remove(j);
		}
		// 4.發起手牌玩家5張，AI則依照難度設定0/1/2 發3張到5張牌
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < players.size(); j++) {
				if (j == 0 || i < L.gameLevel + 3)
					發牌(players.get(j));
			}
		}
	}
	
	public void 發牌(Player player) {
		// 先判斷：如果牌庫空了就洗牌(牌桌最頂端那張牌留在桌上)
		if (牌庫.size() == 0) {
			Poker temp = 牌桌.get(牌桌.size() - 1);// 先抽出牌桌最上面那張牌放到temp暫存
			牌桌.remove(牌桌.size() - 1);
			poker.addAll(牌桌);// 其他牌放進poker陣列
			牌桌.clear();// 清空牌桌
			牌桌.add(temp);// 再把剛才暫存的牌放回牌桌
			while (poker.size() != 0) {// 把poker陣列的牌亂數塞進牌庫
				int j = (int) (Math.random() * poker.size());
				牌庫.add(poker.get(j));
				poker.remove(j);
			}
		}
		// 從牌庫把牌放入玩家手牌
		player.getCard(牌庫.get(0));
		牌庫.remove(0);
	}

	// 出牌的定義：玩家把牌丟到牌桌上，此時根據規則判斷遊戲變動、勝負
	public void 出牌(Player player, int i) {
		規則判斷(player.pokers.get(i).getId());
		牌桌.add(player.pokers.get(i));
		player.pokers.remove(i);
		if (wait == false)
			回合動作();
	}

	private void 回合動作() {
		if (tempcount < 100) {
			count = tempcount;
		}
		順序 = temp順序;
		顯示 += "\n" + players.get(whosTurn).name + "出了" + 牌桌.get(牌桌.size() - 1)
				+ " (" + count + ")";
		if (tempcount > 99) // 超過99..該玩家爆了
		{
			if (whosTurn == 0) {
				// 玩家0爆掉 遊戲結束 跳出結束畫面
				結束畫面("You Lost");

			} else {
				顯示 += "\n" + players.get(whosTurn).name + " LOST";
				// 先把該玩家設屬性isLost，等回合結束就移除玩家。
				players.get(whosTurn).setLost(true);
				// 手牌清進牌堆
				poker.addAll(players.get(whosTurn).pokers);
				players.get(whosTurn).pokers.clear();
			}
		}
		// 換下一家
		whosTurn += 順序;
		whosTurn += players.size();
		whosTurn %= players.size();
		// 移除輸掉的電腦玩家
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).isLost()) {
				players.remove(i);
				if (whosTurn > i)
					whosTurn--;
				// 如果回合指標大於被移除玩家，移除該玩家後，指標也要跟著減1
				break;// 因為每回合頂多輸掉一人，所一抓到輸家就可以break掉for迴圈
			}
		}
		// 若電腦玩家全輸了，顯示獲勝，跳出結束畫面
		if (players.size() == 1) {
			結束畫面("You Win");

		}
	}

	private void 規則判斷(int id) {
		tempcount = count;// count要等出牌判斷該玩家沒掛才能加上去...所以先暫存
		temp順序 = 順序;
		// 牌的ID對應花色：0~12=黑桃A~K 13~25=紅心A~K.....
		// 黑桃A=歸零
		if (id == 0) {
			tempcount = 0;
		} else {
			int c = (id % 13) + 1;
			switch (c) {
			case 4:// 迴轉
				temp順序 = (temp順序 == 1) ? -1 : 1;
				break;
			case 5:// 指定
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
									顯示 += "\n指定" + players.get(i + 1).name;
									wait = false;
									whosTurn = i + 1;
									L.牌桌控制();
								}
							});
					bu.create().show();
				}
				break;
			case 10:// 加或減 10
				tenAndQ(10);
				break;
			case 11:// pass
				break;
			case 12:// 加或減20
				tenAndQ(20);
				break;
			case 13:// 直接加到99
				tempcount = 99;
				break;
			default:
				tempcount += c;
			}
		}

	}

	// 電腦玩家的動作: 手牌選一張出牌
	public void AI(Player ai) {
		// 準備一個優先度的數字陣列,對應AI的每張手牌
		List<Integer> 優先度 = new ArrayList<Integer>();
		for (int i = 0; i < ai.pokers.size(); i++) {
			優先度.add(0);
		}
		// 設定優先度順序...優先度算出來最高的先出掉
		for (int i = 0; i < ai.pokers.size(); i++) {
			規則判斷(ai.pokers.get(i).getId());
			if (tempcount > 99) {// 會讓數字爆掉的牌優先度最低
				優先度.set(i, -10);
			} else if (ai.pokers.get(i).getId() == 0) {// 黑桃A
				優先度.set(i, 1);
			} else {
				int c = ((ai.pokers.get(i)).getId() % 13) + 1;
				switch (c) {
				case 4:// 迴轉
					優先度.set(i, 33);
					break;
				case 5:// 指定
					優先度.set(i, 32);
					break;
				case 10:// 加或減 10
					優先度.set(i, 25);
					break;
				case 11:// pass
					優先度.set(i, 31);
					break;
				case 12:// 加或減20
					優先度.set(i, 20);
					break;
				case 13:// 直接加到99
					優先度.set(i, 10);
					break;
				default:
					優先度.set(i, c * 2 + 100);
				}// 再讓優先度稍微亂數一下
				優先度.set(i, 優先度.get(i) + (int) (Math.random() * 9));
			}
		}
		// 找出優先度最大的
		int temp = -20;
		int j = 0;
		for (int i = 0; i < ai.pokers.size(); i++) {
			if (優先度.get(i) > temp) {
				j = i;
				temp = 優先度.get(i);
			}
		}
		// 優先度最大的 出牌
		出牌(ai, j);
		發牌(ai);

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
							回合動作();
							L.牌桌控制();
						}
					});
			bu.setPositiveButton("-" + n,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							tempcount -= n;
							wait = false;
							回合動作();
							L.牌桌控制();
						}
					});
			bu.show();
		} else if (tempcount >= 100 - n)
			tempcount -= n;
		else {
			tempcount += n;
		}
	}

	private void 結束畫面(String subTitle) {
		L.music.stop();
		wait = true;
		L.chro.stop();
		// 玩家輸了，播放GameOver音效
		if (subTitle.equals("You Lost")) {
			L.music.sound(1);

		} else {// 玩家贏了，播放獲勝音效，更新紀錄
			L.music.sound(2);
			L.sec = (SystemClock.elapsedRealtime() - L.chro.getBase()) / 1000;
			L.data.add(players.get(0).name, L.sec.toString());
		}// 跳出結束選單
		Builder bu = new Builder(L.gameView.getContext());
		bu.setCancelable(false);
		bu.setTitle(subTitle);
		bu.setNegativeButton("結束遊戲", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				L.setTitle("Poker_99");
				L.setContentView(L.mainView);

			}
		});
		bu.setPositiveButton("再一局", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				L.newGame(L.mainView);

			}
		});
		bu.show();
	}
}
