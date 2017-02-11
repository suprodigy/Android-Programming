package com.example.android.thinktank;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.android.thinktank.model.ThinkItem;
import com.example.android.thinktank.model.ThinkObserver;

import org.lucasr.dspec.DesignSpec;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TTDetailActivity extends AppCompatActivity {

    private static final String TAG = "TTDetailActivity";

    private ThinkItem mThinkItem;
    private boolean mDeleted;

    @BindView(R.id.activity_tt_detail)
    View mLayout;

    @BindViews({R.id.keyword1, R.id.keyword2, R.id.keyword3})
    List<EditText> mKeywords;

    @BindView(R.id.think_content)
    EditText mContent;

    @OnClick(R.id.image_button)
    void onImageButtonClicked() {

    }

    @OnClick(R.id.share_button)
    void onShareButtonClicked() {

    }

    @OnClick(R.id.delete_button)
    void onDeleteButtonClicked() {
        Log.d("onDelete()", "" + mThinkItem.getId());
        ThinkObserver.get().delete(mThinkItem);
        mDeleted = true;
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tt_detail);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DesignSpec background = DesignSpec.fromResource(mLayout, R.raw.background);
        mLayout.getOverlay().add(background);

        getThinkItem();
        setView();

        setEventListener();
    }

    private void getThinkItem() {
        mDeleted = false;
        int position = getIntent().getIntExtra("position", -1);
        ThinkItem passedItem = ThinkObserver.get().selectAll().get(position);
        mThinkItem = ThinkObserver.get().getCopiedObject(passedItem);
    }

    private void setView() {
        /*
        RealmList<KeywordItem> keywordsList = mThinkItem.getKeywords();
        for(int i=0; i<keywordsList.size(); i++) {
            mKeywords.get(i).setText("#" + keywordsList.get(i).getName());
        }
        */

        String[] keywords = mThinkItem.getKeywords().split(" ");
        for(int i=0; i<keywords.length; i++) {
            mKeywords.get(i).setText(keywords[i]);
        }

        mContent.setText(mThinkItem.getContent());
    }

    private void setEventListener() {

        /*
        for(int i=0; i<mKeywords.size(); i++) {
            final int idx = i;
            mKeywords.get(i).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mThinkItem.getKeywords().get(idx).setName(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
        */

        for(int i=0; i<mKeywords.size(); i++) {
            mKeywords.get(i).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String keywords = "";
                    for(int j=0; j<mKeywords.size(); j++) {
                        String keyword = mKeywords.get(j).getText().toString();
                        if (keyword.length() != 0) {
                            if(keyword.charAt(0) != '#')
                                keywords += "#" + keyword + " ";
                            else
                                keywords += keyword + " ";
                        }
                    }
                    mThinkItem.setKeywords(keywords);
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        mContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mThinkItem.setContent(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!mDeleted) {
            ThinkObserver.get().update(mThinkItem);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
