package com.example.android.githubissue;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jr on 2017-01-22.
 */

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.IssueHolder> {

    private List<IssueInfo> mIssueInfos;
    private Context mContext;
    private ItemClickListener mItemClickListener;

    public IssueAdapter(Context context, List<IssueInfo> issues, ItemClickListener listener) {
        mContext = context;
        mIssueInfos = issues;
        mItemClickListener = listener;
    }

    @Override
    public IssueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
        view.setOnClickListener(mItemClickListener);
        IssueHolder holder = new IssueHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(IssueHolder holder, int position) {
        IssueInfo issue = mIssueInfos.get(position);
        holder.mIssueNumber.setText(issue.getNumber());
        holder.mIssueTitle.setText(issue.getTitle());
    }

    @Override
    public int getItemCount() {
        return (mIssueInfos == null) ? 0 : mIssueInfos.size();
    }

    public class IssueHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.issueNumber)
        TextView mIssueNumber;
        @BindView(R.id.issueTitle)
        TextView mIssueTitle;

        public IssueHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public interface ItemClickListener extends View.OnClickListener{
        public void onClick(View v);
    }

}
