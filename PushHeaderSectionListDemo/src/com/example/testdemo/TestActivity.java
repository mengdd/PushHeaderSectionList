package com.example.testdemo;

import java.util.ArrayList;
import java.util.List;

import com.example.sectionlist.widget.Item;
import com.example.sectionlist.widget.Item.ItemType;
import com.example.sectionlist.widget.SectionAdapter;
import com.example.sectionlist.widget.SectionListView;
import com.example.testsample3.R;

import android.R.integer;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

public class TestActivity extends Activity {

	private SectionListView mListView = null;
	private MyAdapter mAdapter = null;
	private List<ConcreteItem> mItems = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_sample3);

		mItems = generateItems();

		mListView = (SectionListView) findViewById(R.id.listView);
		mAdapter = new MyAdapter(this, mItems);

		mListView.setAdapter(mAdapter);
	}

	private List<ConcreteItem> generateItems() {
		List<ConcreteItem> items = new ArrayList<ConcreteItem>();

		items.add(new ConcreteItem(ItemType.Header, "header1"));
		for (int i = 0; i < 10; ++i) {
			items.add(new ConcreteItem(ItemType.Content, "content1" + i));
		}
		items.add(new ConcreteItem(ItemType.Header, "header2"));
		for (int i = 0; i < 10; ++i) {
			items.add(new ConcreteItem(ItemType.Content, "content2" + i));
		}
		items.add(new ConcreteItem(ItemType.Header, "header3"));
		for (int i = 0; i < 10; ++i) {
			items.add(new ConcreteItem(ItemType.Content, "content3" + i));
		}
		items.add(new ConcreteItem(ItemType.Header, "header4"));
		for (int i = 0; i < 10; ++i) {
			items.add(new ConcreteItem(ItemType.Content, "content4" + i));
		}
		return items;
	}

}
