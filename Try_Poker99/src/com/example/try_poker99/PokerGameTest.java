package com.example.try_poker99;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class PokerGameTest extends Activity{
	RelativeLayout gameView, gameTable, mainView;
	private TypedArray ids;
	Data data;
	Music music;
	boolean soundOn = false;
	private Game game;
	private ScrollView sc;
	Chronometer chro;
	private TextView textView;
	private List<ImageView> handCardView, tableCardView;
	Long sec = 0L;
	int gameLevel = 1;
	String playerName;
	SharedPreferences sh;
	int playerNumber;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainView = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.activity_poker_game_test, null);
		setContentView(mainView);
		setTitle("Poker_99");
		this.setRequestedOrientation(1);// 鎖定畫面為垂直模式
		ids = getResources().obtainTypedArray(R.array.poker_drawable);
		data = new Data(this);
		music = new Music(this);
		Setting.getSetting(this);

	}
	
	public void newGame(View v) {
		music.play(R.raw.music_117);
		gameView = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.game, null);
		gameTable = (RelativeLayout) gameView.findViewById(R.id.gameTable1);
		setContentView(gameView);
		gameTable.setRotationX(45);
		game = new Game(this);
		game.newgame();
		sc = (ScrollView) gameView.findViewById(R.id.scrollView1);
		moveAble(sc);

		chro = (Chronometer) gameView.findViewById(R.id.chronometer1);
		chro.setBase(SystemClock.elapsedRealtime());
		textView = (TextView) gameView.findViewById(R.id.t3);
		chro.start();
		handCardView = new ArrayList<ImageView>();
		tableCardView = new ArrayList<ImageView>();
		handCardView.add((ImageView) gameView.findViewById(R.id.HandCard1));
		handCardView.add((ImageView) gameView.findViewById(R.id.HandCard2));
		handCardView.add((ImageView) gameView.findViewById(R.id.HandCard3));
		handCardView.add((ImageView) gameView.findViewById(R.id.HandCard4));
		handCardView.add((ImageView) gameView.findViewById(R.id.HandCard5));

		牌面顯示();

		for (final ImageView i : handCardView) {
			i.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if (game.whosTurn == 0 && game.wait == false) {
							((View) i).setAlpha(0.5f);
						}
						break;
					// 玩家出牌，當輪到玩家出牌，且畫面顯示和遊戲狀態同步時才能出牌
					case MotionEvent.ACTION_UP:
						if (game.whosTurn == 0 && game.wait == false) {
							((View) i).setAlpha(1f);
							game.wait = false;
							game.出牌(game.players.get(0),
									handCardView.indexOf(i));
							game.發牌(game.players.get(0));
							牌面顯示();
						}
						break;
					}
					return true;
				}
			});
		}
	}
	
	public void 牌桌控制() {
		if (!game.wait) {
			gameView.postDelayed(gameRun, 500);
		}
	}

	Runnable gameRun = new Runnable() {

		@Override
		public void run() {
			textView.setText(game.顯示);
			// 顯示更新後自動把捲軸捲到底
			sc.fullScroll(ScrollView.FOCUS_DOWN);
			// 如果牌庫空了，洗牌後，重置tableCardView
			if (game.牌桌.size() < tableCardView.size()) {
				gameTable.removeAllViews();
				tableCardView.clear();
			}
			// 通常把新的牌丟上牌桌
			int i = tableCardView.size();
			if (i < game.牌桌.size()) {
				tableCardView.add((ImageView) PokerGameTest.this
						.getLayoutInflater().inflate(R.layout.card, null));
				final ImageView im = tableCardView.get(i);
				Bitmap bm = BitmapFactory.decodeResource(getResources(),
						ids.getResourceId(game.牌桌.get(i).getId(), -1));
				bm = Bitmap.createScaledBitmap(bm, gameTable.getWidth() / 4,
						gameTable.getHeight() / 4, true);
				im.setImageBitmap(bm);
				float x = (gameTable.getWidth() - bm.getWidth()) / 2;
				float y = (gameTable.getHeight() - bm.getHeight()) / 2;
				im.setX((float) (x + Math.random() * 100 - 50));
				im.setY((float) (y + Math.random() * 100 - 50));

				im.setRotation((float) (-15 + Math.random() * 30));
				gameTable.addView(tableCardView.get(i));
				moveAble(im);
				setTitle(" " + game.count + "輪到" + game.players.get(game.whosTurn).name);
				gameTable.postDelayed(this, 500);
				gameView.invalidate();
				music.sound(0);

			}
			// 牌都放完後，若輪到電腦，則電腦出牌
			else if (game.whosTurn != 0) {
				game.AI(game.players.get(game.whosTurn));
				牌桌控制();
			}
		}
	};

	
	//可動的View
		private void moveAble(final View im) {
			im.setOnTouchListener(new OnTouchListener() {
				float dx, dy;
				@Override
				//當目標View被碰觸時
				public boolean onTouch(View v, MotionEvent event) {
					float x = event.getX();//記錄每次動作(碰觸、移動)的座標
					float y = event.getY();
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						dx = x;//手指動作為碰觸時，將座標存入dx dy (碰觸之原點位置)
						dy = y;
						break;
					case MotionEvent.ACTION_MOVE://手指動作為移動時
						im.setX(im.getX() + x - dx);//將View的位置搬到"原位置加移動位差"
						im.setY(im.getY() + y - dy);
						break;
					}
					return true;
				}
			});
		}
		
		// 程式結束時將該釋放的資源釋放掉
		@Override
		protected void onDestroy() {
			ids.recycle();
			music.release();
			super.onDestroy();
		}
		
		@Override
		protected void onPause() {
			// 遊戲暫停時，停止音樂撥放並記錄目前時間
			music.music.pause();
			if (chro != null) {
				sec = (SystemClock.elapsedRealtime() - chro.getBase());
				chro.stop();
			}
			super.onPause();
		}

		@Override
		protected void onResume() {
			// 遊戲從暫停中恢復時，若音效設定為開啟則恢復撥放音樂
			if (soundOn) {
				music.music.start();
			}
			// 恢復時間
			if (chro != null) {
				chro.setBase(SystemClock.elapsedRealtime() - sec);
				chro.start();
			}
			super.onResume();
		}
		
		private void 牌面顯示() {
			for (int i = 0; i < handCardView.size(); i++) {
				handCardView.get(i).setImageResource(
						ids.getResourceId(
								game.players.get(0).pokers.get(i).getId(), -1));
			}

			牌桌控制();
		}
		
		public void deleteData(View v) {
			data.delete();
			new RecordView(this);
			Toast.makeText(this, "已清除", Toast.LENGTH_SHORT).show();
		}

		public void toMainView(View v) {

			setContentView(mainView);
			Setting.getSetting(this);
			music.stop();
		}

		public void recordView(View v) {
			music.play(R.raw.music177);
			new RecordView(this);
		}

		public void settingView(View v) {
			music.play(R.raw.music0004);
			new Setting(this);
		}

		public void exit(View v) {
			music.stop();
			this.finish();

		}
}
