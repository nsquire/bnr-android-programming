package com.nick.geoquiz.app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by nsquire on 4/14/14.
 */
public class CheatActivity extends ActionBarActivity {
    public static final String EXTRA_ANSWER_IS_TRUE = "com.nick.geoquiz.app.answer_is_true";
    public static final String EXTRA_ANSER_SHOWN = "com.nick.geoquiz.app.answer_shown";

    private boolean mAnswerIsTrue;

    private TextView mAnswerTextView;
    private Button mShowAnswer;
    private TextView mApiLevelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        // Answer will not be shown until the user
        // presses the button
        setAnswerShowResult(false);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);

        mShowAnswer = (Button) findViewById(R.id.showAnswerButton);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }

                setAnswerShowResult(true);
            }
        });

        mApiLevelTextView = (TextView) findViewById(R.id.apiLevelTextView);
        mApiLevelTextView.setText(String.format("API Level %s", Build.VERSION.SDK_INT));
    }

    public void setAnswerShowResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}
