package com.example.testdemo;

import com.example.sectionlist.widget.Item;

public class ConcreteItem implements Item
{

	//fields to satisfy Item interface
	private ItemType mType = ItemType.Content;
	private int mSectionId = -1;

	// custom fields
	private String mText;

	public String getmText()
	{
		return mText;
	}

	public void setmText(String mText)
	{
		this.mText = mText;
	}

	public ConcreteItem(ItemType itemType)
	{
		mType = itemType;
	}

	public ConcreteItem(ItemType itemType, String text)
	{
		mType = itemType;
		mText = text;
	}

	@Override
	public ItemType getType()
	{
		return mType;
	}

	public void setItemType(ItemType itemType)
	{
		this.mType = itemType;
	}

	@Override
	public int getSectionId()
	{
		return mSectionId;
	}

	public void setSectionId(int id)
	{
		this.mSectionId = id;
	}

}
