package com.example.android.thinktank;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import org.lucasr.dspec.DesignSpec;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class TTDetailActivity extends AppCompatActivity {

    @BindView(R.id.activity_tt_detail)
    View mLayout;

    @BindView(R.id.bmb)
    BoomMenuButton mMenuButton;

    @BindViews({R.id.keyword1, R.id.keyword2, R.id.keyword3})
    List<EditText> mKeywords;

    @BindView(R.id.think_content)
    EditText mContent;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tt_detail);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DesignSpec background = DesignSpec.fromResource(mLayout, R.raw.background);
        mLayout.getOverlay().add(background);

        String keywords = getIntent().getStringExtra("keyword");
        String content = getIntent().getStringExtra("content");

        String[] keywordsList = keywords.split(" ");
        for(int i=0; i<keywordsList.length; i++) {
            mKeywords.get(i).setText(keywordsList[i]);
        }

        mContent.setText(content);

        setBoomButton();
    }

    private void setBoomButton() {
        mMenuButton.addBuilder(new SimpleCircleButton.Builder()
                .normalImageRes(R.drawable.ic_add_image)
                .normalColorRes(R.color.red)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        // 이미지 추가 버튼 클릭 시
                        Toast.makeText(getApplicationContext(), "이미지 버튼 클릭", Toast.LENGTH_LONG).show();
                    }
                })
        );

        mMenuButton.addBuilder(new SimpleCircleButton.Builder()
                .normalImageRes(R.drawable.ic_think_share)
                .normalColorRes(R.color.blue)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        // 공유 버큰 클릭 시
                        Toast.makeText(getApplicationContext(), "공유 버튼 클릭", Toast.LENGTH_LONG).show();
                    }
                })
        );

        mMenuButton.addBuilder(new SimpleCircleButton.Builder()
                .normalImageRes(R.drawable.ic_delete_think)
                .normalColorRes(R.color.green)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        // 메모 삭제 버튼 클릭 시
                        Toast.makeText(getApplicationContext(), "삭제 버튼 클릭", Toast.LENGTH_LONG).show();
                    }
                })
        );
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
