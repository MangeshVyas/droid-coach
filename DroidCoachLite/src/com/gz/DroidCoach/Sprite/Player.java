package com.gz.DroidCoach.Sprite;

import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class Player extends BaseGrabbableSprite{

	private ChangeableText txtPosition;
	
	
	public Player(float pX, float pY, float pWidth, float pHeight,
			TextureRegion pTextureRegion, Font pFont, boolean pRed) {
		super(pX, pY, pWidth, pHeight, pTextureRegion);
		
		txtPosition = new ChangeableText(0, 0, pFont, " G ", "LCM".length());
		float x = (getWidth() - txtPosition.getWidth())/2;
		float y = (getHeight() - txtPosition.getHeight())/2;
		txtPosition.setPosition(pWidth/4, y);
		
		txtPosition.setColor(0, 1, 0);
		
		if(pRed) this.setColor(1,0,0);
		else this.setColor(.25f, .5f, 1f);
		
		attachChild(txtPosition);
		
	}

	@Override
	public void setAlpha(float pAlpha){
		txtPosition.setAlpha(pAlpha);
		super.setAlpha(pAlpha);
	}
	
	public void setText(String pText){
		txtPosition.setText(pText);
	}
	
}
