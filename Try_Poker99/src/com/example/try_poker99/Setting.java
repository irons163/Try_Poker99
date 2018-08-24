package com.example.try_poker99;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Setting {
	RelativeLayout settingView;
	TextView ev;
	TextView tv1, tv2;
	TextView bt;
	SeekBar sk1;
	RadioGroup rg;
	ToggleButton soundButton;
	PokerGameTest L;

	Setting(final PokerGameTest L) {
		this.L = L;
		settingView = (RelativeLayout) L.getLayoutInflater().inflate(
				R.layout.setting_view, null);
		L.setContentView(settingView);
		ev = (TextView) settingView.findViewById(R.id.editText1);
		tv1 = (TextView) settingView.findViewById(R.id.textView2);
		bt = (TextView) settingView.findViewById(R.id.button3);
		sk1 = (SeekBar) settingView.findViewById(R.id.seekBar1);
		sk1.setMax(6);
		rg = (RadioGroup) settingView.findViewById(R.id.radioGroup1);
		soundButton = (ToggleButton) settingView
				.findViewById(R.id.toggleButton1);
		getSetting();
		tv1.setText((sk1.getProgress() + 2) + " 人");
		sk1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				arg1 += 2;
				tv1.setText(arg1 + " 人");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				L.gameLevel = Integer.parseInt(settingView
						.findViewById(checkedId).getTag().toString());
			}
		});
		soundButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				L.soundOn = soundButton.isChecked();
				//音效設置更動時及時反映
				if(L.soundOn)L.music.play(R.raw.music0004);
				else L.music.stop();
			}
		});
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				L.sh.edit().putString("name", ev.getText().toString())
						.putInt("number", sk1.getProgress() + 2)
						.putInt("level", L.gameLevel)
						.putBoolean("sound", L.soundOn).commit();
				getSetting(L);
				Toast.makeText(L, "設定完成", Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void getSetting() {
		L.sh = L.getSharedPreferences("saveData", Context.MODE_MULTI_PROCESS);
		L.playerName = L.sh.getString("name", "Nameless");
		L.playerNumber = L.sh.getInt("number", 2);
		L.gameLevel = L.sh.getInt("level", 1);
		L.soundOn = L.sh.getBoolean("sound", false);
		ev.setText(L.playerName);
		sk1.setProgress(L.playerNumber - 2);
		rg.check(settingView.findViewWithTag(String.valueOf(L.gameLevel))
				.getId());
		soundButton.setChecked(L.soundOn);

	}

	public static void getSetting(PokerGameTest L) {
		L.sh = L.getSharedPreferences("saveData", Context.MODE_MULTI_PROCESS);
		L.playerName = L.sh.getString("name", "Nameless");
		L.playerNumber = L.sh.getInt("number", 2);
		L.gameLevel = L.sh.getInt("level", 1);
		L.soundOn = L.sh.getBoolean("sound", false);
	}

}
