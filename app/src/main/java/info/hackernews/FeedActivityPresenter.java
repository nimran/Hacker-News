package info.hackernews;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import info.hackernews.models.Posts;
import info.hackernews.service.ServiceManager;
import info.hackernews.service.ServiceUtils;
import info.hackernews.utils.AppStack;
import info.hackernews.utils.AppUtils;
import info.hackernews.utils.Constants;
import nucleus.presenter.RxPresenter;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Presenter class responsible for
 * costly operations like network etc.
 */


public class FeedActivityPresenter extends RxPresenter<FeedActivity> {

    private ServiceManager serviceManager;
    private FeedActivity feedActivity;


    ArrayList<Posts> postsArrayList = new ArrayList<>();
    ArrayList<Integer> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
    }

    @Override
    protected void onTakeView(FeedActivity feedActivity) {
        super.onTakeView(feedActivity);
        this.feedActivity = feedActivity;
        feedActivity.bindViews();
        serviceManager = new ServiceManager(feedActivity);
        loadServiceCall();
    }

    @Override
    protected void onDropView() {
        super.onDropView();
        serviceManager = null;
    }

    protected void loadServiceCall() {
        feedActivity.showLoading();
        serviceManager.doServiceCall(null, Constants.TOP_STORIES_URL, ServiceManager.HTTP_GET);
        serviceManager.setOnResult(new ServiceManager.ServiceResponse() {
            @Override
            public void onSuccess(String result) {
                feedActivity.hideLoading();
                if(!TextUtils.isEmpty(result)) {
                    new AppStack().setStackValue(Constants.TOP_STORIES_RESPONSE,result);
                    populateItems(result);
                    FeedActivity.isSwipe = false;
                    feedActivity.mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onError(String error) {
                feedActivity.hideLoading();
                feedActivity.showError(error);
            }
        });
    }



    private void populateItems(String response) {
        if (response != null) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                parseJSON(jsonArray);
            } catch (JSONException e1) {
                feedActivity.showError("Something went wrong");
            }
        } else {
            feedActivity.showError("Something went wrong");
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
                    .map(new Func1<Integer, Integer>() {
                        @Override
                        public Integer call(Integer integer) {
                            return integer;
                        }
                    })
                    .subscribe(new Subscriber<Integer>() {
                        @Override
                        public void onCompleted() {
                            feedActivity.populateUI(postsArrayList);
                            loadInitialSet();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("error",e.getLocalizedMessage());
                        }

                        @Override
                        public void onNext(Integer integer) {
                            Posts p = new Posts();
                            p.set_id(integer);
                            p.setHasDataLoaded(false);
                            postsArrayList.add(p);
                        }
                    });
        }
    }

    private void loadInitialSet() {
        loadAlternateSets(0);
    }

    void loadAlternateSets(int fromId) {
        int temp = 0;
        for(int i = fromId;i<postsArrayList.size();i++) {
            temp++;
            feedActivity.loadedItems++;
            loadSingleFeed(postsArrayList.get(i).get_id());
            if(temp == 9) break;
        }
    }
    void loadSingleFeed(int id) {
        Log.e("Loading single post", String.valueOf(id));
        serviceManager.doServiceCall(null, AppUtils.getFeedUrl(id), ServiceManager.HTTP_GET);
        serviceManager.setOnResult(new ServiceManager.ServiceResponse() {
            @Override
            public void onSuccess(String result) {
                if(!TextUtils.isEmpty(result)) {
                    Posts posts = ServiceUtils.bindNewsItem(result);
                    if(posts != null) {
                        new AppStack().setStackValue(""+posts.get_id(),posts);
                        App.getDatabase().savePosts(posts);
                    }
                    feedActivity.notifyData();
                }
            }

            @Override
            public void onError(String error) {
                feedActivity.showError(error);
            }
        });
    }


}
