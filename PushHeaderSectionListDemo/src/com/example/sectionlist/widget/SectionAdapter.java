package com.example.sectionlist.widget;

import java.util.ArrayList;
import java.util.List;

import com.example.sectionlist.widget.Item.ItemType;
import com.example.sectionlist.widget.SectionListView.HeaderAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

/**
 * Base class for section list adapter.
 * 
 * @author Meng Dandan
 * 
 * @param <T>
 *            the Item Data in this Adapter should implements the interface
 *            {@link Item}
 */
public abstract class SectionAdapter<T extends Item> extends BaseAdapter
		implements SectionIndexer, HeaderAdapter
{
	public static final int SECTION_HEADER_TYPE = 0;
	public static final int SECTION_CONTENT_TYPE = 1;

	private static final String TAG = "Adapter";

	protected Context mContext = null;
	protected LayoutInflater mInflater = null;

	private List<T> mDataList = null;

	private int mSectionCount = 0;
	private List<Integer> mSectionStartPositions = new ArrayList<Integer>();

	private boolean mCacheValid = true;

	public SectionAdapter(Context context, List<T> data)
	{
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mDataList = data;
		mCacheValid = false;

	}

	public void setData(List<T> data)
	{
		mDataList = data;
		mCacheValid = false;
	}

	@Override
	public Item[] getSections()
	{
		ensureCacheValid();

		Item[] headers = null;
		if (null != mDataList)
		{
			headers = new Item[mSectionCount];

			int j = 0;
			for (int i = 0; i < mSectionStartPositions.size(); ++i)
			{
				headers[j++] = mDataList.get(mSectionStartPositions.get(i));

			}

		}
		return headers;
	}

	@Override
	public int getPositionForSection(int section)
	{
		ensureCacheValid();

		int position = -1;

		if (mSectionStartPositions.size() > 0)
		{
			position = mSectionStartPositions.get(section);
		}

		return position;
	}

	@Override
	public int getSectionForPosition(int position)
	{
		ensureCacheValid();

		int sectionId = -1;
		Item item = getItem(position);
		if (null != item)
		{
			sectionId = item.getSectionId();
		}
		return sectionId;
	}

	protected void ensureCacheValid()
	{
		if (mCacheValid)
		{
			return;
		}

		// set section id to each item
		mSectionCount = 0;
		mSectionStartPositions.clear();
		if (null != mDataList)
		{
			for (int i = 0; i < mDataList.size(); ++i)
			{
				Item item = mDataList.get(i);

				if (ItemType.Header == item.getType())
				{
					mSectionStartPositions.add(i);

					if (0 == i)
					{
						mSectionCount = 0;

					}
					else
					{
						++mSectionCount;
					}

				}
				item.setSectionId(mSectionCount);

			}
		}

		mCacheValid = true;
	}

	@Override
	public int getCount()
	{
		int count = 0;
		if (null != mDataList)
		{
			count = mDataList.size();
		}
		return count;
	}

	@Override
	public Item getItem(int position)
	{
		ensureCacheValid();
		Item item = null;
		if (null != mDataList)
		{
			item = mDataList.get(position);
		}
		return item;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ensureCacheValid();
		Item item = getItem(position);
		View view = null;
		if (null != item)
		{
			if (ItemType.Header == item.getType())
			{
				view = getHeaderView(position, convertView, parent);

			}
			else if (ItemType.Content == item.getType())
			{
				view = getContentView(position, convertView, parent);
			}

		}

		return view;
	}

	protected View getHeaderView(int position, View convertView,
			ViewGroup parent)
	{
		View view = null;
		// 为了区分两种不同的View类型，加了一个Tag显示类型是Header还是Content
		// 所以子类不能使用ViewHolder来改进效率
		if (null != convertView
				&& SECTION_HEADER_TYPE == (Integer) convertView.getTag())
		{
			Log.i(TAG, "getHeaderView: null != convertView and Type is right");
			view = convertView;
		}
		else
		{
			Log.i(TAG, "getHeaderView: null == convertView");
			view = newHeaderView(position, parent);
			view.setTag(SECTION_HEADER_TYPE);
		}

		bindHeaderView(position, view, parent);
		return view;

	}

	protected abstract View newHeaderView(int position, ViewGroup parent);

	protected abstract void bindHeaderView(int position, View view,
			ViewGroup parent);

	protected View getContentView(int position, View convertView,
			ViewGroup parent)
	{
		View view = null;
		if (null != convertView
				&& SECTION_CONTENT_TYPE == (Integer) convertView.getTag())
		{
			Log.i(TAG, "getContentView: null != convertView and Type is right");
			view = convertView;
		}
		else
		{
			Log.i(TAG, "getContentView: null == convertView");
			view = newContentView(position, parent);
			view.setTag(SECTION_CONTENT_TYPE);
		}

		bindContentView(position, view, parent);
		return view;

	}

	protected abstract View newContentView(int position, ViewGroup parent);

	protected abstract void bindContentView(int position, View view,
			ViewGroup parent);

	@Override
	public View getTopHeaderView(View convertView, int firstVisiblePosition,
			ViewGroup parent)
	{
		View topHeaderView = null;

		int currentSection = getSectionForPosition(firstVisiblePosition);
		int sectionHeadPosition = getPositionForSection(currentSection);

		Log.i("getTopHeader", "firstPosition: " + firstVisiblePosition);
		Log.i("getTopHeader", "currentSection: " + currentSection);
		Log.i("getTopHeader", "sectionHeadPosition: " + sectionHeadPosition);
		topHeaderView = getHeaderView(sectionHeadPosition, convertView, parent);

		return topHeaderView;

	}

	@Override
	public int getTopHeaderState(int firstVisiblePosition, int headerHeight,
			ListView listView)
	{
		int state = 0;

		int currentSection = getSectionForPosition(firstVisiblePosition);
		int nextSectionPostition = getPositionForSection(currentSection + 1);

		// Log.i(TAG, "firstVisiblePosition: " + firstVisiblePosition);
		// Log.i(TAG, "nextSectionPostition: " + nextSectionPostition);

		View nextHeader = null;
		int nextSectionTop = 0;

		if (nextSectionPostition >= firstVisiblePosition)
		{
			nextHeader = listView.getChildAt(nextSectionPostition
					- firstVisiblePosition);
			nextSectionTop = nextHeader.getTop();
			// Log.i(TAG, "State: topHeader: Height: " + headerHeight);
			// Log.i(TAG, "State: nextHeader: Top: " + nextHeader.getTop());
		}

		if (null != nextHeader && nextSectionTop <= headerHeight)
		{
			state = PINNED_HEADER_PUSHED_UP;

		}
		else if (getCount() < 0)
		{
			state = PINNED_HEADER_GONE;
		}
		else
		{
			state = PINNED_HEADER_VISIBLE;
		}

		return state;
	}

	@Override
	public int getNextSectionRelativePosition(int firstVisiblePosition,
			ListView listView)
	{
		int relativePosition = -1;

		int currentSection = getSectionForPosition(firstVisiblePosition);
		int nextSectionPostition = getPositionForSection(currentSection + 1);

		if (nextSectionPostition >= firstVisiblePosition)
		{
			relativePosition = nextSectionPostition - firstVisiblePosition;
		}

		return relativePosition;
	}
}
