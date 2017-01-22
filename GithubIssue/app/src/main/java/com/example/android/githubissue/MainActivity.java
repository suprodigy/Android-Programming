package com.example.android.githubissue;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements IssueAdapter.ItemClickListener {

    private List<IssueInfo> mIssueInfos;
    private MyAsyncTask mMyAsyncTask;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.imageView)
    ImageView mAvatar;
    @BindView(R.id.textView)
    TextView mUserId;
    @BindView(R.id.textView3)
    TextView mComments;
    @BindView(R.id.textView2)
    TextView mIssueNumber;
    @BindView(R.id.textView4)
    TextView mIssueTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mMyAsyncTask = new MyAsyncTask();
        mMyAsyncTask.execute();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onClick(final View v) {
        int position = mRecyclerView.getChildLayoutPosition(v);
        IssueInfo item = mIssueInfos.get(position);

        Glide.with(this)
                .load(item.getUser().getAvatar_url())
                .bitmapTransform(new CropCircleTransformation(this))
                .into(mAvatar);
        mUserId.setText("ID : " + item.getUser().getLogin());
        mComments.setText("Comments : " + item.getComments());
        mIssueNumber.setText("Numer : " + item.getNumber());
        mIssueTitle.setText("Title : " + item.getTitle());
    }

    public class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);
                Call<List<IssueInfo>> call = gitHubService.repoContributors("JakeWharton", "DiskLruCache");
                List<IssueInfo> results = call.execute().body();
                mIssueInfos = results;
            } catch(IOException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setAdapter(new IssueAdapter(MainActivity.this, mIssueInfos, MainActivity.this));
        }
    }

}
