package me.solidev.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by _SOLID
 * Date:2016/11/9
 * Time:11:02
 * <p>
 * Desc:A TextView that can get every word in content.
 * </p>
 */

public class GetWordTextView extends AppCompatTextView {

    private CharSequence mText;
    private BufferType mBufferType;

    private OnWordClickListener mOnWordClickListener;
    private SpannableString mSpannableString;

    private BackgroundColorSpan mSelectedBackSpan;
    private ForegroundColorSpan mSelectedForeSpan;

    private int highlightColor;
    private String highlightText;
    private List<String> highlightTexts;
    private int selectedColor_bg;
    private int selectedColor_txt;
    private int language;//0:english,1:chinese

    private boolean getWordable = true;     //设置能否取词

    public void setGetWordable(boolean getWordable) {
        this.getWordable = getWordable;
    }

    public GetWordTextView(Context context) {
        this(context, null);
    }

    public GetWordTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GetWordTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GetWordTextView);
        highlightColor = ta.getColor(R.styleable.GetWordTextView_highlightColor, Color.RED);
        highlightText = ta.getString(R.styleable.GetWordTextView_highlightText);
        selectedColor_bg = ta.getColor(R.styleable.GetWordTextView_selectedColor_bg, Color.BLUE);
        selectedColor_txt = ta.getColor(R.styleable.GetWordTextView_selectedColor_txt, Color.BLUE);
        language = ta.getInt(R.styleable.GetWordTextView_language, 0);
        ta.recycle();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        this.mText = text;
        mBufferType = type;
        //点击 选词背景颜色
        setHighlightColor(Color.TRANSPARENT);
        setMovementMethod(LinkMovementMethod.getInstance());
        setText();
    }

    private void setText() {
        mSpannableString = new SpannableString(mText);
        //set highlight text
        mSpannableString = highlightKeyword(mSpannableString, highlightText);
        mSpannableString = setHighLightSpans(mSpannableString, highlightTexts);
        //separate word
        if (language == 0) {//deal english
            dealEnglish();
        } else {//deal chinese
            dealChinese();
        }
        super.setText(mSpannableString, mBufferType);

    }

    private void dealChinese() {
        for (int i = 0; i < mText.length(); i++) {
            char ch = mText.charAt(i);
            if (Utils.isChinese(ch)) {
                mSpannableString.setSpan(getClickableSpan(), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private void dealEnglish() {
        List<WordInfo> wordInfoList = Utils.getEnglishWordIndices(mText.toString());
        for (WordInfo wordInfo : wordInfoList) {
            if (wordInfo.getStart() != wordInfo.getEnd()) {
                mSpannableString.setSpan(getClickableSpan(), wordInfo.getStart(), wordInfo.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private SpannableString highlightKeyword(SpannableString sp, String str) {
        if (TextUtils.isEmpty(str)) return sp;
        //边界匹配，并且无视大小写
        Pattern p = Pattern.compile("\\b" + str + "\\b", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(mText);
        while (m.find()) {    //通过正则查找，逐个高亮
            int start = m.start();
            int end = m.end();
            Log.e("getWordText", "start-" + start + "-end-" + end);
            sp.setSpan(new ForegroundColorSpan(highlightColor), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return sp;
    }

    private SpannableString setHighLightSpans(SpannableString spannableString, List<String> strings) {
        if (strings != null) {
            for (String s : strings) {
                spannableString = highlightKeyword(spannableString, s);
            }
        }
        return spannableString;
    }

    private void setSelectedSpan(TextView tv) {
        if (mSelectedBackSpan == null || mSelectedForeSpan == null) {
            mSelectedBackSpan = new BackgroundColorSpan(selectedColor_bg);
            mSelectedForeSpan = new ForegroundColorSpan(selectedColor_txt);
        } else {
            mSpannableString.removeSpan(mSelectedBackSpan);
            mSpannableString.removeSpan(mSelectedForeSpan);
        }
        mSpannableString.setSpan(mSelectedBackSpan, tv.getSelectionStart(), tv.getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mSpannableString.setSpan(mSelectedForeSpan, tv.getSelectionStart(), tv.getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        GetWordTextView.super.setText(mSpannableString, mBufferType);
    }

    public void dismissSelected() {
        mSpannableString.removeSpan(mSelectedBackSpan);
        mSpannableString.removeSpan(mSelectedForeSpan);
        GetWordTextView.super.setText(mSpannableString, mBufferType);
    }

    private ClickableSpan getClickableSpan() {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (!getWordable) {
                    return;
                }
                TextView tv = (TextView) widget;
                int selectionStart = tv.getSelectionStart();
                int selectionEnd = tv.getSelectionEnd();
                if (selectionStart >= 0 && selectionEnd >= selectionStart) {
                    String word = tv
                            .getText()
                            .subSequence(tv.getSelectionStart(),
                                    tv.getSelectionEnd()).toString();
                    setSelectedSpan(tv);

                    if (mOnWordClickListener != null) {
                        mOnWordClickListener.onClick(word);
                    }
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
            }
        };
    }


    public void setOnWordClickListener(OnWordClickListener listener) {
        this.mOnWordClickListener = listener;
    }

    public void setHighLightText(String text) {
        highlightText = text;
    }

    public void setHighlightTexts(List<String> texts) {
        highlightTexts = texts;
    }

    public void setSelectedColor_bg(int selectedColor_bg) {
        this.selectedColor_bg = selectedColor_bg;
    }

    public void setHighLightColor(int color) {
        highlightColor = color;
    }

    public interface OnWordClickListener {
        void onClick(String word);
    }
}
