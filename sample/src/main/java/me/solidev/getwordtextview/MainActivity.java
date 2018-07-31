package me.solidev.getwordtextview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.solidev.library.GetWordTextView;

public class MainActivity extends AppCompatActivity {

    private GetWordTextView mEnglishGetWordTextView;
    private GetWordTextView mChineseGetWordTextView;

    private String textStr1 = "Rainforests—\n" +
            "                                                     \n" +
            "                                                     Tropical— rainforests live up to their name: They are forests where it rains a lot. In fact, a typical tropical rainforest receives between 150 and 400 centimeters (59–157 in) of rain each year. They are also warm. Their temperature averages between 25° and 35° Celsius (77°F–95°F). Rainforests are green year-round. Their hot, humid, and rainy climate is perfect for tall trees, vines, ferns, and other plants. The really thick parts of rainforests are what we call jungle.\n" +
            "                                                     \n" +
            "                                                     Tropical rainforests cover a small part (about 6 percent) of Earth’s surface. But over half (50 percent) of the world’s plant and animal species are found in them! Rainforests usually lie in tropical areas near Earth’s equator. Most of the world’s rainforests are in Africa, Southeast Asia, and South America.";

    private String textStr2 = "There— Is- a Place\n" +
            "                                                      \n" +
            "                                                      There is a place where monkeys swing and howl. There is a place where jaguars leap from tree to tree. In this place, bananas and pineapples grow for free. In this place, tiny frogs live in flowers. This is where pink-colored dolphins swim in the river. This is where storms come often, and where the air is sweet.\n" +
            "                                                      \n" +
            "                                                      Some sunlight filters through the vines and leaves, but it is mostly dark here on the ground. It is hot, steamy, and surprisingly still. Rainwater trickles down from leaf to leaf. You hear a slow sound: drip, drip, drip.\n" +
            "                                                      \n" +
            "                                                      Your skin is sweaty. An insect lands on your neck. An ant quietly walks across your sandal. Suddenly a little brown monkey swings to a nearby branch. Then a bright green bird flutters past. Welcome to the Amazon rainforest.";
    private String textStr3 = "the pencil case";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEnglishGetWordTextView = (GetWordTextView) findViewById(R.id.english_get_word_text_view);
        List<String> strings = new ArrayList<>();
        strings.add("is");
        strings.add("There");
        strings.add("in");
        mEnglishGetWordTextView.setHighlightTexts(strings);
        mEnglishGetWordTextView.setHighLightColor(Color.RED);
        mEnglishGetWordTextView.setText(textStr2);
        mEnglishGetWordTextView.setOnWordClickListener(new GetWordTextView.OnWordClickListener() {
            @Override
            public void onClick(String word) {
                showToast(word);
            }
        });

        mChineseGetWordTextView = (GetWordTextView) findViewById(R.id.chinese_get_word_text_view);
        mChineseGetWordTextView.setText("这是一个中文句子的例子，这是一个中文句子的例子。");
        mChineseGetWordTextView.setOnWordClickListener(new GetWordTextView.OnWordClickListener() {
            @Override
            public void onClick(String word) {
                showToast(word);
            }
        });
        findViewById(R.id.btn_dismiss_selected).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEnglishGetWordTextView.dismissSelected();
                mChineseGetWordTextView.dismissSelected();
            }
        });
    }

    Toast toast;

    public void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }
}
