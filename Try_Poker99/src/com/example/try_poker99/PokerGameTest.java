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
		this.setRequestedOrientation(1);// ��w�e���������Ҧ�
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

		�P�����();

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
					// ���a�X�P�A����쪱�a�X�P�A�B�e����ܩM�C�����A�P�B�ɤ~��X�P
					case MotionEvent.ACTION_UP:
						if (game.whosTurn == 0 && game.wait == false) {
							((View) i).setAlpha(1f);
							game.wait = false;
							game.�X�P(game.players.get(0),
									handCardView.indexOf(i));
							game.�o�P(game.players.get(0));
							�P�����();
						}
						break;
					}
					return true;
				}
			});
		}
	}
	
	public void �P�౱��() {
		if (!game.wait) {
			gameView.postDelayed(gameRun, 500);
		}
	}

	Runnable gameRun = new Runnable() {

		@Override
		public void run() {
			textView.setText(game.���);
			// ��ܧ�s��۰ʧⱲ�b���쩳
			sc.fullScroll(ScrollView.FOCUS_DOWN);
			// �p�G�P�w�ŤF�A�~�P��A���mtableCardView
			if (game.�P��.size() < tableCardView.size()) {
				gameTable.removeAllViews();
				tableCardView.clear();
			}
			// �q�`��s���P��W�P��
			int i = tableCardView.size();
			if (i < game.�P��.size()) {
				tableCardView.add((ImageView) PokerGameTest.this
						.getLayoutInflater().inflate(R.layout.card, null));
				final ImageView im = tableCardView.get(i);
				Bitmap bm = BitmapFactory.decodeResource(getResources(),
						ids.getResourceId(game.�P��.get(i).getId(), -1));
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
				setTitle(" " + game.count + "����" + game.players.get(game.whosTurn).name);
				gameTable.postDelayed(this, 500);
				gameView.invalidate();
				music.sound(0);

			}
			// �P���񧹫�A�Y����q���A�h�q���X�P
			else if (game.whosTurn != 0) {
				game.AI(game.players.get(game.whosTurn));
				�P�౱��();
			}
		}
	};

	
	//�i�ʪ�View
		private void moveAble(final View im) {
			im.setOnTouchListener(new OnTouchListener() {
				float dx, dy;
				@Override
				//��ؼ�View�Q�IĲ��
				public boolean onTouch(View v, MotionEvent event) {
					float x = event.getX();//�O���C���ʧ@(�IĲ�B����)���y��
					float y = event.getY();
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						dx = x;//����ʧ@���IĲ�ɡA�N�y�Цs�Jdx dy (�IĲ�����I��m)
						dy = y;
						break;
					case MotionEvent.ACTION_MOVE://����ʧ@�����ʮ�
						im.setX(im.getX() + x - dx);//�NView����m�h��"���m�[���ʦ�t"
						im.setY(im.getY() + y - dy);
						break;
					}
					return true;
				}
			});
		}
		
		// �{�������ɱN�����񪺸귽����
		@Override
		protected void onDestroy() {
			ids.recycle();
			music.release();
			super.onDestroy();
		}
		
		@Override
		protected void onPause() {
			// �C���Ȱ��ɡA����ּ���ðO���ثe�ɶ�
			music.music.pause();
			if (chro != null) {
				sec = (SystemClock.elapsedRealtime() - chro.getBase());
				chro.stop();
			}
			super.onPause();
		}

		@Override
		protected void onResume() {
			// �C���q�Ȱ�����_�ɡA�Y���ĳ]�w���}�ҫh��_���񭵼�
			if (soundOn) {
				music.music.start();
			}
			// ��_�ɶ�
			if (chro != null) {
				chro.setBase(SystemClock.elapsedRealtime() - sec);
				chro.start();
			}
			super.onResume();
		}
		
		private void �P�����() {
			for (int i = 0; i < handCardView.size(); i++) {
				handCardView.get(i).setImageResource(
						ids.getResourceId(
								game.players.get(0).pokers.get(i).getId(), -1));
			}

			�P�౱��();
		}
		
		public void deleteData(View v) {
			data.delete();
			new RecordView(this);
			Toast.makeText(this, "�w�M��", Toast.LENGTH_SHORT).show();
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
