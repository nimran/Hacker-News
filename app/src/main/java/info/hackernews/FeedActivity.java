package info.hackernews;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

/**
 * Activity which loads the TopStories
 * in the form of List
 */

@RequiresPresenter(FeedActivityPresenter.class)
public class FeedActivity extends NucleusAppCompatActivity<FeedActivityPresenter>  {

    @BindView(R.id.activity_feeds) View layoutView;
    @BindView(R.id.feeds_list) RecyclerView feedsList;
    @BindView(R.id.swipe_refresh_feeds) SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);
    }

    public void bindViews() {
        feedsList.setHasFixedSize(true);
        feedsList.setItemAnimator(new DefaultItemAnimator());
        final LinearLayoutManager llm = new LinearLayoutManager(FeedActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        feedsList.setLayoutManager(llm);
        bindEvents();
    }

    public void onResult(String response) {
        if(response != null) {
            try {
                JSONArray jsonArray = new JSONArray(response);

            } catch (JSONException e1) {
                showError("Something went wrong");
            }
        } else {
            showError("Something went wrong");
        }

    }

    public void showError(String error) {
        Snackbar.make(layoutView,error,Snackbar.LENGTH_LONG);
    }

    private void bindEvents() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFeeds();
            }
        });
    }

    private void loadFeeds() {
    }

    private void populateUI() {

    }


}
