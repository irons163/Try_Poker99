package com.example.try_poker99;

import android.widget.LinearLayout;
import android.widget.TextView;

public class RecordView {
	LinearLayout linearLayout, view;

	RecordView(final PokerGameTest L) {
		linearLayout = (LinearLayout) L.getLayoutInflater().inflate(
				R.layout.record, null);
		view = (LinearLayout) linearLayout.findViewById(R.id.view);
		L.setContentView(linearLayout);
		Data d = new Data(L);
		final CharSequence[][] list = d.read();
		for (CharSequence[] l : list) {
			LinearLayout v = (LinearLayout) L.getLayoutInflater().inflate(
					R.layout.linear_layout1, null);
			TextView t1, t2, t3;
			t1 = (TextView) v.findViewById(R.id.t1);
			t1.setText(l[0]);
			t2 = (TextView) v.findViewById(R.id.t2);
			t2.setText(l[1]);
			t3 = (TextView) v.findViewById(R.id.t3);
			t3.setText(l[2] + " ¬í");
			view.addView(v);
		}
	}
}
