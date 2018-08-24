package com.example.try_poker99;

import android.media.MediaPlayer;
import android.media.SoundPool;

public class Music {
	MediaPlayer music = new MediaPlayer();//�񭵼�
	int[] soundPool=new int[3];
	SoundPool sp =new SoundPool(5,3,3);//�P�ɼ���̤j�ƶq,����,����
	PokerGameTest L;

	Music(PokerGameTest L) {
		this.L = L;
		soundPool[0]=sp.load(L, R.raw.sound_turns, 0);//context,���Ĥ��e,�u���v
		soundPool[1]=sp.load(L, R.raw.sound_gameover, 0);
		soundPool[2]=sp.load(L, R.raw.sound_win, 0);
	}

	public void play(int id) {
		music.stop();//�⼽�񤤪����ְ���
		if(L.soundOn){//�Y�]�w���Ķ}�Ҥ~���񭵼�
		music = MediaPlayer.create(L, id);
		music.setLooping(true);//�����������Ƽ���
		music.start();}
	}
	public void sound(int i) {
		if(L.soundOn)//�Y�]�w���Ķ}�Ҥ~����
			sp.play(soundPool[i], 1, 1, 0, 0, 1);
			//����,���n�D���q,�k�n�D���q,�u���v,�O�_����,�t�v
	}
	public void stop() {
		music.stop();
	}

	public void release() {
		music.release();
		sp.release();
	}
	
}
