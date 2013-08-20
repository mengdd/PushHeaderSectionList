package com.example.testdemo;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sectionlist.widget.Item;
import com.example.sectionlist.widget.SectionAdapter;
import com.example.testsample3.R;

public class MyAdapter extends SectionAdapter<ConcreteItem> {
	@Override
	public ConcreteItem getItem(int position) {
		Item item = super.getItem(position);
		ConcreteItem concreteItem = null;
		if (null != item) {
			concreteItem = (ConcreteItem) item;
		}
		return concreteItem;
	}

	public MyAdapter(Context context, List<ConcreteItem> data) {
		super(context, data);
	}

	@Override
	protected View newHeaderView(int position, ViewGroup parent) {

		View view = mInflater.inflate(R.layout.header_item, parent, false);

		ViewHolder1 viewHolder = new ViewHolder1();
		viewHolder.header = (TextView) view.findViewById(R.id.header);
		viewHolder.subHeader = (TextView) view.findViewById(R.id.sub_header);

		view.setTag(viewHolder);

		return view;
	}

	@Override
	protected void bindHeaderView(int position, View view, ViewGroup parent) {

		ConcreteItem item = getItem(position);

		if (null != item) {
			ViewHolder1 viewHolder = (ViewHolder1) view.getTag();
			viewHolder.header.setText(item.getmText());
			viewHolder.subHeader.setText("subheader: " + item.getmText());
		}

	}

	@Override
	protected View newContentView(int position, ViewGroup parent) {
		View view = mInflater.inflate(R.layout.content_item, parent, false);

		ViewHolder2 viewHolder = new ViewHolder2();
		viewHolder.text1 = (TextView) view.findViewById(R.id.text);
		viewHolder.text2 = (TextView) view.findViewById(R.id.text2);
		view.setTag(viewHolder);

		return view;
	}

	@Override
	protected void bindContentView(int position, View view, ViewGroup parent) {
		ConcreteItem item = getItem(position);
		if (null != item) {
			ViewHolder2 viewHolder = (ViewHolder2) view.getTag();
			viewHolder.text1.setText(item.getmText());
			viewHolder.text2.setText(item.getmText() + " text2");
		}

	}

	private static class ViewHolder1 {
		TextView header;
		TextView subHeader;

	}

	private static class ViewHolder2 {
		TextView text1;
		TextView text2;

	}

}
