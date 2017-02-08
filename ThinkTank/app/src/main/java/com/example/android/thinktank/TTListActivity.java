package com.example.android.thinktank;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.lucasr.dspec.DesignSpec;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TTListActivity extends AppCompatActivity {

    @BindView(R.id.think_list_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tt_list);

        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new TTAdapter(this));
        mRecyclerView.requestFocus();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public class TTHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.list_item_keywords)
        TextView mKeywords;

        @BindView(R.id.list_item_content)
        TextView mContent;

        @BindView(R.id.list_item_background)
        View mBackground;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public TTHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

            DesignSpec background = DesignSpec.fromResource(mBackground, R.raw.list_item_background);
            mBackground.getOverlay().add(background);
        }

        public void bindThinkItem(String keywords, String content) {
            mKeywords.setText(keywords);
            mContent.setText(content);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), TTDetailActivity.class);
            intent.putExtra("keyword", mKeywords.getText());
            intent.putExtra("content", mContent.getText());
            startActivity(intent);
        }
    }

    private class TTAdapter extends RecyclerView.Adapter<TTHolder> {

        private Context mContext;
        List<String> mKeywordList;
        List<String> mContentList;

        public TTAdapter(Context context) {
            mContext = context;
            mKeywordList = new ArrayList<>();
            mContentList = new ArrayList<>();
            for(int i=0; i<100; i++) {
                mKeywordList.add("#헌법 #국회의원");
                mContentList.add(getString(R.string.korean_lorem_ipsum));
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public TTHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.tt_list_item, parent, false);
            return new TTHolder(view);
        }

        @Override
        public void onBindViewHolder(TTHolder holder, int position) {
            holder.bindThinkItem(mKeywordList.get(position), mContentList.get(position));
        }

        @Override
        public int getItemCount() {
            return mKeywordList.size();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tt_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            // 검색 기능
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
