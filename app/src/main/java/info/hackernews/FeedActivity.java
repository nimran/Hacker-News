package info.hackernews;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.hackernews.models.Posts;
import info.hackernews.utils.AppStack;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

/**
 * Activity which loads the TopStories
 * in the form of List
 */

@RequiresPresenter(FeedActivityPresenter.class)
public class FeedActivity extends NucleusAppCompatActivity<FeedActivityPresenter> implements FeedItemsInterface{

    @BindView(R.id.activity_feeds)
    View layoutView;
    @BindView(R.id.feeds_list)
    RecyclerView feedsList;
    @BindView(R.id.swipe_refresh_feeds)
    SwipeRefreshLayout mSwipeRefreshLayout;

    ArrayList<Posts> posts = null;
    ProgressDialog progressDialog = null;

    FeedAdapter feedAdapter;
    LinearLayoutManager llm = null;

    public int loadedItems = 0;
    public static boolean isSwipe = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);
    }

    public void bindViews() {
        progressDialog = new ProgressDialog(this);
        feedsList.setHasFixedSize(true);
        feedsList.setItemAnimator(new DefaultItemAnimator());
        llm = new LinearLayoutManager(FeedActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        feedsList.setLayoutManager(llm);
        bindEvents();
    }

    private void bindEvents() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isSwipe = true;
                loadedItems = 0;
                getPresenter().loadServiceCall();
            }
        });
    }

    public void showError(String error) {
        Snackbar.make(layoutView, error, Snackbar.LENGTH_LONG);
    }


    void populateUI(ArrayList<Posts> posts) {
        this.posts = posts;
        feedAdapter = new FeedAdapter(this, posts);
        feedAdapter.setOnItemChangeListener(this);
        feedsList.setAdapter(feedAdapter);
    }

    void notifyData() {
        feedAdapter.notifyDataSetChanged();
    }
    @Override
    public void onFeedItemChanged(int position) {
        if(posts != null) {
            if (new AppStack().getStackValue(""+posts.get(position).get_id()) == null) {
                Log.e("loading single feed", String.valueOf(position));
                getPresenter().loadSingleFeed(position);
            } else if(isSwipe) {
                Log.e("loading single feed", String.valueOf(position));
                getPresenter().loadSingleFeed(position);
            }
        }

    }

    public void showLoading() {
        progressDialog.setMessage("Loading !!");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    void hideLoading() {
        progressDialog.dismiss();
    }
}
