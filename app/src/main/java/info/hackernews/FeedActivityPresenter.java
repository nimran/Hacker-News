package info.hackernews;

import android.os.Bundle;
import android.text.TextUtils;

import info.hackernews.network.ServiceManager;
import info.hackernews.utils.AppStack;
import info.hackernews.utils.Constants;
import nucleus.presenter.RxPresenter;

/**
 * Presenter class responsible for
 * costly operations like network etc.
 */


public class FeedActivityPresenter extends RxPresenter<FeedActivity> {

    private ServiceManager serviceManager;
    private FeedActivity feedActivity;


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

    private void loadServiceCall() {
        serviceManager.doServiceCall(null, Constants.TOP_STORIES_URL, ServiceManager.HTTP_GET);
        serviceManager.setOnResult(new ServiceManager.ServiceResponse() {
            @Override
            public void onSuccess(String result) {
                if(!TextUtils.isEmpty(result)) {
                    new AppStack().setStackValue(Constants.TOP_STORIES_RESPONSE,result);
                    feedActivity.onResult(result);
                }
            }

            @Override
            public void onError(String error) {
                feedActivity.showError(error);
            }
        });
    }
}
