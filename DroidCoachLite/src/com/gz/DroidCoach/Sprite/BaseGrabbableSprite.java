package com.gz.DroidCoach.Sprite;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public abstract class BaseGrabbableSprite extends Sprite{

	private boolean mGrabbed = false;
	
	public BaseGrabbableSprite(float pX, float pY, float pWidth, float pHeight,
			TextureRegion pTextureRegion) {
		super(pX, pY, pWidth, pHeight, pTextureRegion);

	}
	
	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		switch(pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				this.setScale(1.25f);
				this.mGrabbed = true;
				break;
			case TouchEvent.ACTION_MOVE:
				if(this.mGrabbed) {
					this.setPosition(pSceneTouchEvent.getX() - getWidth() / 2, pSceneTouchEvent.getY() - getHeight() / 2);
				}
				break;
			case TouchEvent.ACTION_UP:
				if(this.mGrabbed) {
					this.mGrabbed = false;
					this.setScale(1.0f);
				}
				break;
		}
		return true;
	}

}
