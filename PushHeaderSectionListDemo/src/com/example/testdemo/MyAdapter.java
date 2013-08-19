package com.example.testdemo;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sectionlist.widget.Item;
import com.example.sectionlist.widget.SectionAdapter;
import com.example.testsample3.R;

public class MyAdapter extends SectionAdapter<ConcreteItem>
{
	@Override
	public ConcreteItem getItem(int position)
	{
		Item item = super.getItem(position);
		ConcreteItem concreteItem = null;
		if (null != item)
		{
			concreteItem = (ConcreteItem) item;
		}
		return concreteItem;
	}

	public MyAdapter(Context context, List<ConcreteItem> data)
	{
		super(context, data);
	}

	@Override
	protected View newHeaderView(int position, ViewGroup parent)
	{
		View view = mInflater.inflate(R.layout.header_item, parent, false);
		return view;
	}

	@Override
	protected void bindHeaderView(int position, View view, ViewGroup parent)
	{

		ConcreteItem item = getItem(position);
		if (null != item)
		{
			TextView mTextView = (TextView) view.findViewById(R.id.header);
			mTextView.setText(item.getmText());
			
			TextView mTextView2 = (TextView) view.findViewById(R.id.sub_header);
			mTextView2.setText("subheader: " + item.getmText());
		}

	}

	@Override
	protected View newContentView(int position, ViewGroup parent)
	{
		View view = mInflater.inflate(R.layout.content_item, parent, false);
		return view;
	}

	@Override
	protected void bindContentView(int position, View view, ViewGroup parent)
	{
		ConcreteItem item = getItem(position);
		if (null != item)
		{
			TextView mTextView = (TextView) view.findViewById(R.id.text);
			mTextView.setText(item.getmText());
			
			TextView mTextView2 = (TextView) view.findViewById(R.id.text2);
			mTextView2.setText(item.getmText() + " text2");
		}

	}

	// 为了区分两种不同的View类型，SectionAdapter加了一个Tag显示类型是Header还是Content
	// 所以子类不能使用ViewHolder来改进效率,因为不能再加Tag了

}
