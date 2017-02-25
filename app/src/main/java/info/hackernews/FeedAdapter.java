package info.hackernews;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elyeproj.loaderviewlibrary.LoaderTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.hackernews.models.Posts;
import info.hackernews.utils.AppUtils;

/**
 * Created by imran on 23/02/17.
 * <p>
 * HackerNews
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private Activity mActivity;
    private ArrayList<Posts> postsArrayList;
    private FeedItemsInterface feedItemsInterface;


    public FeedAdapter(Activity mActivity, ArrayList<Posts> postsArrayList) {
        this.mActivity = mActivity;
        this.postsArrayList = postsArrayList;
    }

    void setOnItemChangeListener(FeedItemsInterface feedItemsInterface) {
        this.feedItemsInterface = feedItemsInterface;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_list_items, parent, false);

        return new FeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        Posts posts = postsArrayList.get(position);
        feedItemsInterface.onFeedItemChanged(position);
        if(posts != null ) {
            posts = App.getDatabase().getPost(posts.get_id());
            if (posts != null && posts.isHasDataLoaded()) {
                holder.postAuthorName.setText(posts.get_author());
                holder.postTime.setText(AppUtils.getRelativeTime(posts.get_time()));
                holder.postDesc.setText(posts.get_title());
                holder.postFavCount.setText(AppUtils.getPoints(posts.get_points()));
                holder.postCommentsCount.setText(AppUtils.getCommentsCount(posts.get_comments()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }

    class FeedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.post_author_name)
        LoaderTextView postAuthorName;
        @BindView(R.id.post_timestamp)
        TextView postTime;
        @BindView(R.id.post_description)
        TextView postDesc;
        @BindView(R.id.post_fav_count)
        TextView postFavCount;
        @BindView(R.id.post_comments_count)
        TextView postCommentsCount;


        FeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
