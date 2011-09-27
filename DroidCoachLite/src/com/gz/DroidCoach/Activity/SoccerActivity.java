package com.gz.DroidCoach.Activity;

import java.util.ArrayList;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.ZoomCamera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.input.touch.controller.MultiTouch;
import org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector;
import org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.anddev.andengine.extension.input.touch.exception.MultiTouchException;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.input.touch.detector.ScrollDetector;
import org.anddev.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.anddev.andengine.input.touch.detector.SurfaceScrollDetector;
import org.anddev.andengine.level.LevelLoader;
import org.anddev.andengine.level.LevelLoader.IEntityLoader;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.view.RenderSurfaceView;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.SAXUtils;
import org.xml.sax.Attributes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gz.DroidCoach.R;
import com.gz.DroidCoach.Constant.UISettings;
import com.gz.DroidCoach.Scene.SoccerFormation;
import com.gz.DroidCoach.Sprite.Player;

public class SoccerActivity extends BaseGameActivity implements IOnMenuItemClickListener, IOnSceneTouchListener, IScrollDetectorListener, IPinchZoomDetectorListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static int CAMERA_WIDTH = 720;
	private static int CAMERA_HEIGHT = 480;

	protected static final int MENU_SOCCER = 0;
	protected static final int MENU_QUIT = MENU_SOCCER + 1;

	// ===========================================================
	// Fields
	// ===========================================================

	protected ZoomCamera mZoomCamera;

	protected Scene mMainScene;

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TextureRegion mFieldTextureRegion;

	private BitmapTextureAtlas mFontTexture;
	private Font mFont;
	
	private BitmapTextureAtlas mMenuTexture;
	protected TextureRegion mBallTextureRegion;
	protected TextureRegion mCircleTextureRegion;

	private SurfaceScrollDetector mScrollDetector;
	private PinchZoomDetector mPinchZoomDetector;
	private float mPinchZoomStartedCameraZoomFactor;
	
	private boolean mShowingRed = true;
	private boolean mShowingBlue = true;
	private String mRedFormation = "433 ";
	private String mBlueFormation = "442 ";
	
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
    protected void onSetContentView() {
		final FrameLayout frameLayout = new FrameLayout(this);
        final FrameLayout.LayoutParams frameLayoutLayoutParams =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
                                             FrameLayout.LayoutParams.FILL_PARENT);
 
        
        
//        final AdView adView = new AdView(this, AdSize.BANNER, "xxxxxxxxxxxx");
// 
//        adView.refreshDrawableState();
//        adView.setVisibility(AdView.VISIBLE);
//        final FrameLayout.LayoutParams adViewLayoutParams =
//                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
//                                             FrameLayout.LayoutParams.WRAP_CONTENT,
//                                             Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
//        // getHeight returns 0
//        // http://groups.google.com/group/<span class="posthilit">admob</span>-pu ... a874df3472
//        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50,
//                getResources ().getDisplayMetrics ());
//        // top of AD is at middle of the screen
//        adViewLayoutParams.topMargin = height/2;
//        Log.v(TAG, "Adview height : " + height);
//        Log.v(TAG, "Adview size {x:" + adView.getWidth() + ", y: " + adView.getHeight()+ "}");
// 
        
//        AdRequest adRequest = new AdRequest();
//        adRequest.addTestDevice( AdRequest.TEST_EMULATOR);
//        adView.loadAd(adRequest);
 
        
        //TESTING
        final LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View ll = inflater.inflate(R.layout.soccer_options, null);
        View toggle = inflater.inflate(R.layout.soccer_options_toggle, null);
        final LinearLayout.LayoutParams llParams = 
        	new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
        								  LinearLayout.LayoutParams.WRAP_CONTENT,
        								  Gravity.LEFT);
       
        final LinearLayout.LayoutParams toggleParams = 
        	new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
        								  LinearLayout.LayoutParams.WRAP_CONTENT,
        								  Gravity.BOTTOM);
        
        
        this.mRenderSurfaceView = new RenderSurfaceView(this);
        mRenderSurfaceView.setRenderer(mEngine);
 
        final android.widget.FrameLayout.LayoutParams surfaceViewLayoutParams =
                new FrameLayout.LayoutParams(super.createSurfaceViewLayoutParams());
 
        frameLayout.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);
        frameLayout.addView(ll,llParams);
        frameLayout.addView(toggle,toggleParams);
        
        //frameLayout.addView(adView, adViewLayoutParams);
 
        
        this.setContentView(frameLayout, frameLayoutLayoutParams);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.red3223:
	    	UISettings.RedFormation = "3223";
	        return true;
	    case R.id.red3232:
	    	UISettings.RedFormation = "3232";
	        return true;
	    case R.id.red343:
	    	UISettings.RedFormation = "343";
	        return true;
	    case R.id.red352:
	    	UISettings.RedFormation = "352";
	        return true;
	    case R.id.red4231:
	    	UISettings.RedFormation = "4231";
	        return true;
	    case R.id.red433:
	    	UISettings.RedFormation = "433";
	        return true;
	    case R.id.red442:
	    	UISettings.RedFormation = "442";
	        return true;
	    case R.id.red451:
	    	UISettings.RedFormation = "451";
	        return true;
	    case R.id.redleftcnr_flat:
	    	UISettings.RedFormation = "left_cnr_flat";
	    	return true;
	    case R.id.red_show:
	    	UISettings.ShowRed = !UISettings.ShowRed;
	    	return true;
	    case R.id.blue3223:
	    	UISettings.BlueFormation = "3223";
	        return true;
	    case R.id.blue3232:
	    	UISettings.BlueFormation = "3232";
	        return true;
	    case R.id.blue343:
	    	UISettings.BlueFormation = "343";
	        return true;
	    case R.id.blue352:
	    	UISettings.BlueFormation = "352";
	        return true;
	    case R.id.blue4231:
	    	UISettings.BlueFormation = "4231";
	        return true;
	    case R.id.blue433:
	    	UISettings.BlueFormation = "433";
	        return true;
	    case R.id.blue442:
	    	UISettings.BlueFormation = "442";
	        return true;
	    case R.id.blue451:
	    	UISettings.BlueFormation = "451";
	    	return true;
	    case R.id.blueleftcnr_flat:
	    	UISettings.BlueFormation = "left_cnr_flat";
	    	return true;
	    case R.id.blue_show:
	    	UISettings.ShowBlue = !UISettings.ShowBlue;
	    	return true;
	    case R.id.save_form:
	    	SaveForm();
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.soccer_menu, menu);
	    return true;
	}
	
	@Override
	public Engine onLoadEngine() {
		
		
		
		final DisplayMetrics displayMetrics = new DisplayMetrics();
 		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		
 		//TODO: PinchZoomDetector
 		
 		CAMERA_WIDTH = displayMetrics.widthPixels;
 		CAMERA_HEIGHT = displayMetrics.heightPixels;
 		
		this.mZoomCamera = new ZoomCamera(0, 0, CAMERA_WIDTH,CAMERA_HEIGHT); 
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mZoomCamera));
	}

	@Override
	public void onLoadResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFieldTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "soccer/field_outline_black.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mBitmapTextureAtlas);

		this.mMenuTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBallTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mMenuTexture, this, "soccer/soccerball.png", 0, 0);
		this.mCircleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mMenuTexture, this, "soccer/circle.png", 400, 0);
		this.mEngine.getTextureManager().loadTexture(this.mMenuTexture);
		
		this.mFontTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = new Font(this.mFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), CAMERA_WIDTH/50, true, Color.BLACK);

		this.mEngine.getTextureManager().loadTexture(this.mFontTexture);
		this.getFontManager().loadFont(this.mFont);
	}

	@Override
	public Scene onLoadScene() {
		
		/* Just a simple scene with an animated face flying around. */
		this.mMainScene = new Scene();
		this.mMainScene.setBackground(new ColorBackground(1,1,1));
		
		mMainScene.setTouchAreaBindingEnabled(true);
		
		final Sprite face = new Sprite(0,0,CAMERA_WIDTH, CAMERA_HEIGHT, this.mFieldTextureRegion);
		//face.registerEntityModifier(new MoveModifier(30, 0, CAMERA_WIDTH - face.getWidth(), 0, CAMERA_HEIGHT - face.getHeight()));
		this.mMainScene.attachChild(face);

		float w = CAMERA_WIDTH/15;
		
		addTouchableSprite(50, 50, .75f*w, .75f*w, mBallTextureRegion.deepCopy());
			
		mMainScene.setOnAreaTouchTraversalFrontToBack();
		
		
		for (int i = 0; i < 11; i++) {
			addPlayer((CAMERA_WIDTH - 30)/11*i, 100, w, w,true);
			addPlayer((CAMERA_WIDTH - 30)/11*i, 250, w, w,false);
		}
		
		LoadLevel("433.lvl", true);
		LoadLevel("442.lvl", false);
		
		this.mScrollDetector = new SurfaceScrollDetector(this);
		if(MultiTouch.isSupportedByAndroidVersion()) {
			try {
				this.mPinchZoomDetector = new PinchZoomDetector(this);
			} catch (final MultiTouchException e) {
				this.mPinchZoomDetector = null;
			}
		} else {
			this.mPinchZoomDetector = null;
		}

		this.mMainScene.setOnSceneTouchListener(this);
		this.mMainScene.setTouchAreaBindingEnabled(true);
		
		
		mMainScene.registerUpdateHandler(new IUpdateHandler(){

			@Override
			public void onUpdate(float pSecondsElapsed) {
								
				if(mShowingBlue != UISettings.ShowBlue){
					for (int i = 0; i < mBluePlayers.size(); i++) {
//						float alpha = 0;
//						if(UISettings.ShowBlue)alpha = 1;
//						mBluePlayers.get(i).setAlpha(alpha);
						mBluePlayers.get(i).setVisible(UISettings.ShowBlue);
						mShowingBlue = UISettings.ShowBlue;
					}
				}
				
				if(mShowingRed != UISettings.ShowRed){
					for (int i = 0; i < mRedPlayers.size(); i++) {
//						float alpha = 0;
//						if(UISettings.ShowRed)alpha = 1;
//						mRedPlayers.get(i).setAlpha(alpha);
						mRedPlayers.get(i).setVisible(UISettings.ShowRed);
						mShowingRed = UISettings.ShowRed;
					}
				}
				
				if(mBlueFormation != UISettings.BlueFormation){
					LoadLevel(UISettings.BlueFormation + ".lvl", false);
					UISettings.BlueFormation = UISettings.BlueFormation + " ";
					mBlueFormation = UISettings.BlueFormation;
				}
				
				if(mRedFormation != UISettings.RedFormation){
					LoadLevel(UISettings.RedFormation + ".lvl", true);
					UISettings.RedFormation = UISettings.RedFormation + " ";
					mRedFormation = UISettings.RedFormation;
				}
			}

			@Override
			public void reset() {
				
			}
			
		});
		
		return this.mMainScene;
	}

	
	
	@Override
	public void onLoadComplete() {

		ListView lstRed = (ListView)findViewById(R.id.lstRedFormation);
      	ListView lstBlue = (ListView)findViewById(R.id.lstBlueFormation);
        String[] formations = getResources().getStringArray(R.array.soccer_formations);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item,formations); 
        lstRed.setAdapter(adapter);
        lstBlue.setAdapter(adapter);
        
        lstRed.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Debug.d("Item Click: " + arg1.toString());
				TextView view = (TextView)arg1;
				UISettings.RedFormation = (String) view.getText();
			}
		});

        lstBlue.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Debug.d("Item Click: " + arg1.toString());
				TextView view = (TextView)arg1;
				UISettings.BlueFormation = (String) view.getText();
			}
		});
        
        CheckBox chkRed = (CheckBox)findViewById(R.id.chkShowRed);
        CheckBox chkBlue = (CheckBox)findViewById(R.id.chkShowBlue);
        
        chkRed.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				UISettings.ShowRed = isChecked;
			}
		});
        
        chkBlue.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				UISettings.ShowBlue = isChecked;
			}
		});
        
        final LinearLayout llOptions = (LinearLayout)findViewById(R.id.llOptions);
        
        Button btnToggle = (Button)findViewById(R.id.btnToggle);
        btnToggle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(llOptions.getVisibility() == View.VISIBLE){
					llOptions.setVisibility(View.INVISIBLE);
				}
				else
				{
					llOptions.setVisibility(View.VISIBLE);
				}
			}
		});
        
        btnToggle.setVisibility(View.INVISIBLE);
        llOptions.setVisibility(View.INVISIBLE);
	}

	
	@Override
	public void onScroll(final ScrollDetector pScollDetector, final TouchEvent pTouchEvent, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mZoomCamera.getZoomFactor();
		this.mZoomCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
	}

	@Override
	public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent) {
		this.mPinchZoomStartedCameraZoomFactor = this.mZoomCamera.getZoomFactor();
	}

	@Override
	public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		this.mZoomCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
	}

	@Override
	public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		this.mZoomCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
	}
	
	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		if(this.mPinchZoomDetector != null) {
			this.mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);

			if(this.mPinchZoomDetector.isZooming()) {
				this.mScrollDetector.setEnabled(false);
			} else {
				if(pSceneTouchEvent.isActionDown()) {
					this.mScrollDetector.setEnabled(true);
				}
				this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
			}
		} else {
			this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
		}

		return true;
	}

	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
		switch(pMenuItem.getID()) {
			case MENU_SOCCER:
					SoccerFormation scene = new SoccerFormation();
				
					final Sprite sprite = new Sprite(100,100,this.mCircleTextureRegion);
					scene.attachChild(sprite);
					
					mMainScene.setChildScene(scene);
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

	private void addTouchableSprite(final int pX, final int pY, final float pW, final float pH, final TextureRegion pTR) {
		final Sprite sprite = new Sprite(pX, pY, pW, pH, pTR) {
			boolean mGrabbed = false;

			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch(pSceneTouchEvent.getAction()) {
					case TouchEvent.ACTION_DOWN:
						this.setScale(1.25f);
						this.mGrabbed = true;
						break;
					case TouchEvent.ACTION_MOVE:
						if(this.mGrabbed) {
							this.setPosition(pSceneTouchEvent.getX() - pW / 2, pSceneTouchEvent.getY() - pH / 2);
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
		};

		this.mMainScene.attachChild(sprite);
		this.mMainScene.registerTouchArea(sprite);
	}

	private ArrayList<Player> mRedPlayers = new ArrayList<Player>();
	private ArrayList<Player> mBluePlayers = new ArrayList<Player>();
	private int mCurrentLoadPlayer = 0;
	
	private void addPlayer(final int pX, final int pY, final float pW, final float pH, boolean pRed){
		final Player player = new Player(pX, pY, pW, pH, mCircleTextureRegion.deepCopy(), mFont,pRed);
		
		if(pRed)mRedPlayers.add(player);
		else mBluePlayers.add(player);
		
		this.mMainScene.attachChild(player);
		this.mMainScene.registerTouchArea(player);
	}

	private void LoadLevel(String pFormationName, final boolean pRed){
		final LevelLoader levelLoader = new LevelLoader();
		levelLoader.setAssetBasePath("formations/");
		
		levelLoader.registerEntityLoader("formation", new IEntityLoader(){

			@Override
			public void onLoadEntity(String pEntityName, Attributes pAttributes) {
				String name = SAXUtils.getAttributeOrThrow(pAttributes, "name");
				mCurrentLoadPlayer = 0;
			}
			
		});
		
		levelLoader.registerEntityLoader("player", new IEntityLoader(){

			@Override
			public void onLoadEntity(String pEntityName, Attributes pAttributes) {
				String sx = SAXUtils.getAttributeOrThrow(pAttributes, "x");
				String sy = SAXUtils.getAttributeOrThrow(pAttributes, "y");
				String type = SAXUtils.getAttributeOrThrow(pAttributes, "type");
				
				Player player;
				
				float x = new Float(sx)*CAMERA_WIDTH;
				float y = new Float(sy)* CAMERA_HEIGHT;
				
				if(pRed){
					player = mRedPlayers.get(mCurrentLoadPlayer);
				}
				else{
					player = mBluePlayers.get(mCurrentLoadPlayer);
					x = CAMERA_WIDTH - x;
					y = CAMERA_HEIGHT - y;
				}
				
				x-=player.getWidth()/2;
				y-=player.getHeight()/2;
				
				player.setPosition(x,y);
				player.setText(type);
				
				mCurrentLoadPlayer++;
			}
			
		});
		
		try {
			Debug.d("Loading Level: " + pFormationName);
			levelLoader.loadLevelFromAsset(this, pFormationName);
		} catch (Exception e) {
			Debug.e(e);
		}
	}

	private void SaveForm()
	{
		for (int i = 0; i < mRedPlayers.size(); i++) {
			Player player = mRedPlayers.get(i);
		}
		
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}