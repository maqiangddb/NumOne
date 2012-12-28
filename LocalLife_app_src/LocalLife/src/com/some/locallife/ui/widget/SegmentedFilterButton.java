package com.some.locallife.ui.widget;

import java.util.ArrayList;
import java.util.List;

import com.some.locallife.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class SegmentedFilterButton extends LinearLayout {

	private int mBtnArray;
	private int mBtnLayout;
	private String[] mBtnTitles;
	private int defaultBtnPosition = 0;

	public SegmentedFilterButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SegmentedFilterButton, 0, 0);

		this.mBtnArray = a.getInteger(R.styleable.SegmentedFilterButton_btn_array, R.array.segmented_btn_text_default);
		this.mBtnLayout = a.getInt(R.styleable.SegmentedFilterButton_btn_layout, 0);//R.layout.segmented_filter_btn_item_default);
		this.mBtnTitles = context.getResources().getStringArray(this.mBtnArray);

		a.recycle();
	}

	private void _addButtons(String[] titles) {
		for(int i = 0; i< titles.length; i++) {
		Spinner spinner = new Spinner(this.getContext());
		spinner.setAdapter(new ArrayAdapter<String>(this.getContext(), mBtnLayout, titles) {

			});
		spinner.setSelection(defaultBtnPosition);
		spinner.setBackgroundResource(R.drawable.yellow_spinner_selector);

		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1);
		addView(spinner, llp);

		}
	}

	private class SpinnerAdapter1 extends ArrayAdapter<String> {

		public SpinnerAdapter1(Context context, int resource,
				int textViewResourceId, String[] objects) {
			super(context, resource, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}

	}

	private class SpinnerAdapter2 implements SpinnerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub

		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub

		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
