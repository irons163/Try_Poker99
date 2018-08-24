package com.example.try_poker99;

import android.media.MediaPlayer;
import android.media.SoundPool;

public class Music {
	MediaPlayer music = new MediaPlayer();//放音樂
	int[] soundPool=new int[3];
	SoundPool sp =new SoundPool(5,3,3);//同時播放最大數量,類型,音質
	PokerGameTest L;

	Music(PokerGameTest L) {
		this.L = L;
		soundPool[0]=sp.load(L, R.raw.sound_turns, 0);//context,音效內容,優先權
		soundPool[1]=sp.load(L, R.raw.sound_gameover, 0);
		soundPool[2]=sp.load(L, R.raw.sound_win, 0);
	}

	public void play(int id) {
		music.stop();//把播放中的音樂停掉
		if(L.soundOn){//若設定音效開啟才撥放音樂
		music = MediaPlayer.create(L, id);
		music.setLooping(true);//音樂類的重複撥放
		music.start();}
	}
	public void sound(int i) {
		if(L.soundOn)//若設定音效開啟才撥放
			sp.play(soundPool[i], 1, 1, 0, 0, 1);
			//音源,左聲道音量,右聲道音量,優先權,是否重複,速率
	}
	public void stop() {
		music.stop();
	}

	public void release() {
		music.release();
		sp.release();
	}
	
}
