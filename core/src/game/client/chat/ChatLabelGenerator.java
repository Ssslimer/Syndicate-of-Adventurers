package client.chat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class ChatLabelGenerator 
{
	public static Label generateLabel(float x, float y, float width, float height, String labelText)
	{
		LabelStyle style = new LabelStyle();
		style.fontColor = Color.BLACK;
		style.font = new BitmapFont();
		
		Label label = new Label("", style);
		label.setText(labelText);
		label.setWidth(width);
		label.setHeight(height);
		label.setPosition(x, y);
		
		return label;
	}
}
