package entities;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ItemButton extends TextButton
{
	private Item item;

	public ItemButton(Skin skin, Item item) 
	{
		super(item.getType().toString() + "(" + item.getAttack() + "," + item.getDefence() + "," + item.getHPBonus() + ")", skin);
		this.item = item;
	}
	
	public Item getItem()
	{
		return this.item;
	}
}
