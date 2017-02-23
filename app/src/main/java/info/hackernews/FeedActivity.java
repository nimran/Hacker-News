package info.hackernews;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.hackernews.models.Posts;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;
import rx.Observable;
import rx.functions.Action1;

/**
 * Activity which loads the TopStories
 * in the form of List
 */

@RequiresPresenter(FeedActivityPresenter.class)
public class FeedActivity extends NucleusAppCompatActivity<FeedActivityPresenter> {

    @BindView(R.id.activity_feeds)
    View layoutView;
    @BindView(R.id.feeds_list)
    RecyclerView feedsList;
    @BindView(R.id.swipe_refresh_feeds)
    SwipeRefreshLayout mSwipeRefreshLayout;

    ArrayList<Integer> items = new ArrayList<>();
    ArrayList<Posts> posts = new ArrayList<>();

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

    private void bindEvents() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFeeds();
            }
        });
    }

    public void onResult(String response) {
        Observable<int[]> observable;
        if (response != null) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                parseJSON(jsonArray);
            } catch (JSONException e1) {
                showError("Something went wrong");
            }
        } else {
            showError("Something went wrong");
        }

    }

    private void parseJSON(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                items.add((Integer) jsonArray.get(i));
            }
        } catch (Exception e) {
            Log.e("Exception", e.getLocalizedMessage());
        } finally {
            Observable.from(items)
                    .subscribe(new Action1<Integer>() {
                        @Override
                        public void call(Integer integer) {
                            Posts post = new Posts();
                            post.set_id(integer);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.e("Error", throwable.getLocalizedMessage());
                        }
                    });
        }
    }


    public void showError(String error) {
        Snackbar.make(layoutView, error, Snackbar.LENGTH_LONG);
    }


    private void loadFeeds() {
    }

    private void populateUI() {

    }


}
