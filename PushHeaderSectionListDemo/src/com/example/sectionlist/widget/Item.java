package com.example.sectionlist.widget;

/**
 * interface of Item.
 * The item should have type and section id.
 * 
 * @author Meng Dandan
 *
 */
public interface Item
{
	public enum ItemType
	{
		Header,Content
	}

	ItemType getType();
	int getSectionId();
	void setSectionId(int id);
}
