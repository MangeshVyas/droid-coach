package com.gz.DroidCoach;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.content.Intent;
import android.view.KeyEvent;

import com.gz.DroidCoach.Activity.SoccerActivity;

public class DroidCoachLiteActivity extends BaseGameActivity implements IOnMenuItemClickListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	protected static final int MENU_SOCCER = 0;
	protected static final int MENU_QUIT = MENU_SOCCER + 1;

	// ===========================================================
	// Fields
	// ===========================================================

	protected Camera mCamera;

	protected Scene mMainScene;

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TextureRegion mFaceTextureRegion;

	protected MenuScene mMenuScene;

	private BitmapTextureAtlas mMenuTexture;
	protected TextureRegion mMenuSoccerTextureRegion;
	protected TextureRegion mMenuQuitTextureRegion;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_box_menu.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mBitmapTextureAtlas);

		this.mMenuTexture = new BitmapTextureAtlas(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mMenuSoccerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mMenuTexture, this, "menu_soccer.png", 0, 0);
		this.mMenuQuitTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mMenuTexture, this, "menu_quit.png", 0, 50);
		this.mEngine.getTextureManager().loadTexture(this.mMenuTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.createMenuScene();

		/* Just a simple scene with an animated face flying around. */
		this.mMainScene = new Scene();
		this.mMainScene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

//		final Sprite face = new Sprite(0, 0, this.mFaceTextureRegion);
//		face.registerEntityModifier(new MoveModifier(30, 0, CAMERA_WIDTH - face.getWidth(), 0, CAMERA_HEIGHT - face.getHeight()));
//		this.mMainScene.attachChild(face);

		this.mMainScene.setChildScene(this.mMenuScene, false, true, true);
		
		return this.mMainScene;
	}

	@Override
	public void onLoadComplete() {

	}

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if(pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
//			if(this.mMainScene.hasChildScene()) {
//				/* Remove the menu and reset it. */
//				this.mMenuScene.back();
//			} else {
//				/* Attach the menu. */
				this.mMainScene.setChildScene(this.mMenuScene, false, true, true);
			//}
			return true;
		} else {
			return super.onKeyDown(pKeyCode, pEvent);
		}
		
	}

	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
		switch(pMenuItem.getID()) {
			case MENU_SOCCER:
				this.startActivity(new Intent(this, SoccerActivity.class));
				return true;
			case MENU_QUIT:
				/* End Activity. */
				this.finish();
				return true;
			default:
				return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void createMenuScene() {
		this.mMenuScene = new MenuScene(this.mCamera);

		//TODO: Change texture region for soccer
		final SpriteMenuItem resetMenuItem = new SpriteMenuItem(MENU_SOCCER, this.mMenuSoccerTextureRegion);
		resetMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMenuScene.addMenuItem(resetMenuItem);

		final SpriteMenuItem quitMenuItem = new SpriteMenuItem(MENU_QUIT, this.mMenuQuitTextureRegion);
		quitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mMenuScene.addMenuItem(quitMenuItem);

		this.mMenuScene.buildAnimations();

		this.mMenuScene.setBackgroundEnabled(false);

		this.mMenuScene.setOnMenuItemClickListener(this);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}