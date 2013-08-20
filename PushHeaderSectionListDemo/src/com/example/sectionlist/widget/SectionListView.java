package com.example.sectionlist.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * A ListView that shows several sections together.
 * Each section has a header.
 * The top header is the current header and can be pushed up if the second
 * section is coming up.
 * 
 * @author Meng Dandan
 * 
 */
public class SectionListView extends ListView implements OnScrollListener {

	private static final String TAG = "ListView";

	public interface HeaderAdapter {

		public static final int PINNED_HEADER_GONE = 0;

		public static final int PINNED_HEADER_VISIBLE = 1;

		public static final int PINNED_HEADER_PUSHED_UP = 2;

		View getTopHeaderView(View convertView, int firstVisiblePosition,
				ViewGroup parent);

		int getTopHeaderState(int firstVisiblePosition, int headerHeight,
				ListView listView);

		int getNextSectionRelativePosition(int firstVisiblePosition,
				ListView listView);
	}

	private View mTopHeaderView = null;
	private boolean isTopHeaderVisible = true;

	private int mHeaderPaddingLeft;
	private int mHeaderPaddingTop;
	private int mHeaderWidth;
	private int mHeaderHeight;

	private HeaderAdapter mAdapter = null;
	private OnScrollListener mOnScrollListener;

	public SectionListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SectionListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SectionListView(Context context) {
		super(context);
		init();
	}

	private void init() {
		super.setOnScrollListener(this);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		Log.w(TAG, "onLayout");
		mHeaderPaddingLeft = getPaddingLeft();
		mHeaderPaddingTop = getPaddingTop();
		mHeaderWidth = r - l - mHeaderPaddingLeft - getPaddingRight();
		if (null != mTopHeaderView) {
			Log.w(TAG, "layout in onLayout");
			mTopHeaderView.layout(mHeaderPaddingLeft, mHeaderPaddingTop,
					mHeaderWidth, mHeaderHeight);
			Log.w(TAG, "config in onLayout");
			// make sure to call configure here
			// otherwise the layout will not be changed
			configureTopHeaderView(getFirstVisiblePosition());
		}

		Log.i(TAG, "onLayout width: " + mHeaderWidth + ", height: "
				+ mHeaderHeight);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.i(TAG, "onMeasure");
		if (mTopHeaderView != null) {
			Log.i(TAG, "onMeasure -- mHeadView != null");
			measureChild(mTopHeaderView, widthMeasureSpec, heightMeasureSpec);
			mHeaderWidth = mTopHeaderView.getMeasuredWidth();
			mHeaderHeight = mTopHeaderView.getMeasuredHeight();

			// I don't know why the mHeaderHeight is a wrong number(too big)
			Log.i(TAG, "onMeasure width: " + mHeaderWidth + ", height: "
					+ mHeaderHeight);
		}
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		mAdapter = (HeaderAdapter) adapter;
		super.setAdapter(adapter);
	}

	@Override
	public void setOnScrollListener(OnScrollListener listener) {
		mOnScrollListener = listener;

		super.setOnScrollListener(this);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (null != mOnScrollListener) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (null != mAdapter) {
			// get view
			mTopHeaderView = mAdapter.getTopHeaderView(mTopHeaderView,
					firstVisibleItem, this);

			requestLayout();

			// take action according to state

			configureTopHeaderView(firstVisibleItem);

		}

		if (null != mOnScrollListener) {
			mOnScrollListener.onScroll(view, firstVisibleItem,
					visibleItemCount, totalItemCount);
		}

	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (isTopHeaderVisible && null != mTopHeaderView) {
			// Log.i(TAG, "drawTopHeader: " + +mHeaderHeight+ ", " +
			// mHeaderHeight);
			drawChild(canvas, mTopHeaderView, getDrawingTime());
		}
	}

	private void configureTopHeaderView(int firstVisibleItem) {
		if (mTopHeaderView == null) {
			return;
		}
		// get state
		int state = mAdapter.getTopHeaderState(firstVisibleItem, mHeaderHeight,
				this);
		switch (state) {
			case HeaderAdapter.PINNED_HEADER_GONE: {
				isTopHeaderVisible = false;
				break;
			}

			case HeaderAdapter.PINNED_HEADER_VISIBLE: {
				isTopHeaderVisible = true;
				if (mTopHeaderView.getTop() != mHeaderPaddingTop) {
					Log.w(TAG, "layout in Visible");
					mTopHeaderView.layout(mHeaderPaddingLeft,
							mHeaderPaddingTop, mHeaderWidth, mHeaderHeight);
				}
				break;
			}

			case HeaderAdapter.PINNED_HEADER_PUSHED_UP: {
				isTopHeaderVisible = true;

				int nextSection = mAdapter.getNextSectionRelativePosition(
						firstVisibleItem, this);
				View nextHeader = getChildAt(nextSection);
				if (nextHeader != null) {

					int bottom = nextHeader.getTop();

					int y = bottom - mHeaderHeight;

					if (mTopHeaderView.getTop() != mHeaderPaddingTop + y) {

						Log.w(TAG, "layout in Push up");
						mTopHeaderView.layout(mHeaderPaddingLeft,
								mHeaderPaddingTop + y, mHeaderWidth,
								mHeaderHeight + y);
					}

				}
				break;
			}
		}
	}

}
