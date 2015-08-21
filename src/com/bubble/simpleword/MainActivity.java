package com.bubble.simpleword;

import java.io.File;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.WindowManager;

import com.bubble.simpleword.db.DBManager;
import com.bubble.simpleword.db.WordsDB;
import com.bubble.simpleword.menu.MainFragment;
import com.bubble.simpleword.menu.SlidingMenuFragment;
import com.bubble.simpleword.wordbook.WordsClass;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-2
 */
public class MainActivity extends SlidingFragmentActivity {
	public DBManager dbManager;
	private Fragment contentFragment;
	SlidingMenu sm;
	ActionBar mActionBar;
	SQLiteDatabase db;
	public static final String DB_NAME = "simpleword.db"; //the database file's name
    public static final String PACKAGE_NAME = "com.bubble.simpleword";
    public static final String FOLDER_NAME = "databases";
    public static final String DB_PATH = File.separator + "data"
            + Environment.getDataDirectory().getAbsolutePath() + File.separator
            + PACKAGE_NAME + File.separator
            + FOLDER_NAME ;  //the path to save database
    public static WordsClass word ;
    
    SharedPreferences pref;
    Editor editor;
    public static final String PREFS_FILE_NAME = "SimpleWord_Prefs_File";  
    public static final String IS_FIRST_START = "is_first_start";  
    public static Boolean isFirstStart;
    
    Cursor cursor;
    public static int cursorIndex = 0;
    public static final String CURSOR_INDEX = "cursor_index";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("onCreate", "开始");
		sm = getSlidingMenu();
		initSlidingMenu(savedInstanceState);
		
		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);//show back button
		mActionBar.setDisplayShowHomeEnabled(false);//don't show app's img
		
		loadDatabase();	//load SDcard's database
		
		pref = this.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE);  
		editor = pref.edit();

		isFirstStart = pref.getBoolean(IS_FIRST_START, true);
		
		Log.i("isFirstStart", Boolean.toString(isFirstStart));
		Log.i("cursorIndex", Integer.toString(cursorIndex));
		Log.i("onCreate", "结束");
	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-20 上午1:02:15
	 */
	@Override
	protected void onStart() {
		super.onStart();
		Log.i("onStart", "开始");
//		cursorIndex = pref.getInt(CURSOR_INDEX, 0);
//		initWords(cursorIndex);
		
		
		Log.i("onStart", "结束");
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-20 上午10:34:12
	 */
	@Override
	protected void onResume() {
		super.onResume();
		Log.i("onResume", "开始");
		//judge if it's the first time to start the app
		//if "true",change the flag "IS_FIRST_START" to "false"
		if ( isFirstStart ) {
			editor.putBoolean(IS_FIRST_START, false);
			editor.commit();
			initWords();
		} else {
			WordsDB.isInOrder = pref.getBoolean(WordsDB.IS_INORDER, true);
			WordsDB.isReverseOrder = pref.getBoolean(WordsDB.IS_REVERSEORDER, false);
			WordsDB.isRandom = pref.getBoolean(WordsDB.IS_RANDOM, false);
			cursorIndex = pref.getInt(CURSOR_INDEX, 0);
			initWords(cursorIndex);
		}
		
		Log.i("onResume", "结束");
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-20 上午10:35:07
	 */
	@Override
	protected void onPause() {
		super.onPause();
		Log.i("onPause", "开始");
		Log.i("onPause", "结束");
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-20 上午10:35:16
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i("onRestart", "开始");
		Log.i("onRestart", "结束");
	}
	
	/**
	 * <p>Title: initWords</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-6
	 */
	private void initWords(){
		WordsDB.initWordsDB(this);
		WordsDB.setWordInOrder();	//if it is the first time to start the app, the default mode to get word is "in order"
	}
	/**
	 * <p>Title: initWords</p>
	 * <p>Description: </p>
	 * @param position
	 * @author bubble
	 * @date 2015-8-20
	 */
	private void initWords(int position){
		WordsDB.initWordsDB(this, position);
//		}
	}
	/**
	 * <p>Title: initSlidingMenu</p>
	 * <p>Description: </p>
	 * @param savedInstanceState
	 * @author bubble
	 * @date 2015-8-2
	 */
	private void initSlidingMenu(Bundle savedInstanceState){	
		if (savedInstanceState != null)
			contentFragment = getFragmentManager().getFragment(savedInstanceState, "contentFragment");
		if (contentFragment == null)
			contentFragment = new MainFragment(this);	
		
		setContentView(R.layout.main_frame_content);
		getFragmentManager().beginTransaction().replace(R.id.content_frame, contentFragment).commit();

		setBehindContentView(R.layout.main_frame_menu);
		getFragmentManager().beginTransaction().replace(R.id.menu_frame, new SlidingMenuFragment()).commit();

		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setTouchModeBehind(SlidingMenu.TOUCHMODE_MARGIN);
		sm.setShadowWidthRes(R.dimen.shadow_width);	
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setFadeDegree(0);
		sm.setBehindScrollScale(0);
		
		Point size = new Point();
    	WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
    	Display display = wm.getDefaultDisplay();
    	display.getSize(size);
    	int slidingWidth = size.x * 2 / 5;
		sm.setBehindWidth(slidingWidth);
		
	}
	/**
	 * <p>Title: loadDatabase</p>
	 * <p>Description: load SDcard's database</p>
	 * @author bubble
	 * @date 2015-8-6 下午11:52:05
	 */
	public void loadDatabase(){
		dbManager = new DBManager(this);
		db = dbManager.getDatabase(DB_PATH + "/" + DB_NAME);
        dbManager.closeDatabase(db);
	}
	
	/**
	 * <p>Title: switchContent</p>
	 * <p>Description: switch content's fragment</p>
	 * @param fragment
	 * @author bubble
	 * @date 2015-8-5
	 */
	public void switchContent(Fragment fragment) {
		contentFragment = fragment;
		getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
//		sm.showContent();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
            	sm.showContent();
            }
        }, 50);
	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-6
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;	
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-2
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getFragmentManager().putFragment(outState, "contentFragment", contentFragment);
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-6
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//press smartphone's "Menu" button to open and close the slidingmenu
		if(keyCode==KeyEvent.KEYCODE_MENU){
			sm.toggle();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-19 下午9:19:42
	 */
	@Override
	protected void onStop() {
		super.onStop();
		Log.i("onStop", "开始");
		cursorIndex = WordsDB.getCursorPosition();
		editor.putInt(CURSOR_INDEX, cursorIndex);
		editor.putBoolean(WordsDB.IS_INORDER, WordsDB.isInOrder);
		editor.putBoolean(WordsDB.IS_REVERSEORDER, WordsDB.isReverseOrder);
		editor.putBoolean(WordsDB.IS_RANDOM, WordsDB.isRandom);
		editor.commit();
		Log.i("cursorIndex", Integer.toString(cursorIndex));
		Log.i("onStop", "结束");
	}

	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-20 上午00:52:05
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("onDestroy", "开始");
		Log.i("onDestroy", "结束");
	}
	
	/**
	 * <p>Description: save temporary data</p>
	 * @author bubble
	 * @date 2015-8-20 下午12:58:39
	 */
	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
		super.onSaveInstanceState(outState, outPersistentState);
	}
}
