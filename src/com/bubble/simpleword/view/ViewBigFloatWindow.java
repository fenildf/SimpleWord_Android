package com.bubble.simpleword.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bubble.simpleword.R;
import com.bubble.simpleword.db.WordCls;
import com.bubble.simpleword.db.WordsManager;
import com.bubble.simpleword.fragment.SettingsFragment;
import com.bubble.simpleword.service.ServiceFloatWord;
import com.bubble.simpleword.util.Util;

/**
 * <p>Title: FloatWindowBigView</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-30 下午1:02:52
 */
public class ViewBigFloatWindow extends LinearLayout {
    public static int viewWidth;  
  
    public static int viewHeight;  
    
    private View view;  
    private TextView tvWord;
    private TextView tvPhonetic;
    private TextView tvDefinition;
    private ImageButton btnPronounce;
    private TextView tvClose;  
    private TextView tvBack;  
    
    private Intent intent;
  
    public ViewBigFloatWindow(final Context context) {  
        super(context);  
        LayoutInflater.from(context).inflate(R.layout.word_float_window_big, this);  
        view = findViewById(R.id.big_window_layout);  
        viewWidth = view.getLayoutParams().width;  
        viewHeight = view.getLayoutParams().height;  
        
        tvWord = (TextView) findViewById(R.id.float_window_big_tv_word);
        tvPhonetic = (TextView) findViewById(R.id.float_window_big_tv_phonetic);
        tvDefinition = (TextView) findViewById(R.id.float_window_big_tv_definition);
        tvWord.setText(WordsManager.getWordCls().getWord());
        tvPhonetic.setText(WordsManager.getWordCls().getPhonetic());
        tvDefinition.setText(WordsManager.getWordCls().getDefinition());
        
        btnPronounce = (ImageButton) findViewById(R.id.float_window_big_imgbtn_pronounce);
        btnPronounce.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Util.pronounceWord(WordsManager.getWordCls(), context);
			}
		});
        
        tvClose = (TextView) findViewById(R.id.close);  
        tvBack = (TextView) findViewById(R.id.back);  
        
        tvClose.setOnClickListener(new OnClickListener() {  

			@Override  
            public void onClick(View v) {  
                // 点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service  
                MyWindowManager.removeBigFloatWord(context);  
                MyWindowManager.removeSmallFloatWord(context);  
                SettingsFragment.closeSwitch(SettingsFragment.KEY_SWITCH_FLOAT_WORD, context);
            }  
        });  
        
        tvBack.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                // 点击返回的时候，移除大悬浮窗，创建小悬浮窗  
                MyWindowManager.removeBigFloatWord(context);  
                MyWindowManager.createSmallFloatWord(context,Util.getSharedPreferences(context).getInt(SettingsFragment.KEY_SEEKBAR_WIDTH_FLOAT_WORD, SettingsFragment.FLOAT_WORD_DEFAULT_WIDTH));  
            }  
        });  
    }  
    
    /**
     * <p>Description: </p>
     * @author bubble
     * @date 2015-8-31 下午5:56:04
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) { 
    	switch (event.getKeyCode()) {
        case KeyEvent.KEYCODE_BACK:
        	MyWindowManager.removeBigFloatWord(getContext());
    		MyWindowManager.createSmallFloatWord(getContext(),Util.getSharedPreferences(getContext()).getInt(SettingsFragment.KEY_SEEKBAR_WIDTH_FLOAT_WORD, SettingsFragment.FLOAT_WORD_DEFAULT_WIDTH));
            return true;
        default:
        	return super.dispatchKeyEvent(event); 
        }
	} 
    
    /**
     * <p>Description: </p>
     * @author bubble
     * @date 2015-9-7 下午8:25:34
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	int x = (int) event.getX();
        int y = (int) event.getY();
        Rect rect = new Rect();
        this.getGlobalVisibleRect(rect);	//获取视图在屏幕坐标中的可视区域
        if ( ! rect.contains(x, y) ) {
        	MyWindowManager.removeBigFloatWord(getContext());
    		MyWindowManager.createSmallFloatWord(getContext(),Util.getSharedPreferences(getContext()).getInt(SettingsFragment.KEY_SEEKBAR_WIDTH_FLOAT_WORD, SettingsFragment.FLOAT_WORD_DEFAULT_WIDTH));
        }
    	return super.onTouchEvent(event);
    }
}
