package info.hackernews.network;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import info.hackernews.utils.AppUtils;

/**
 * ServiceManager class is responsible for Data
 * Transmissions.
 */

public class ServiceManager {


    public static final String HTTP_GET = "GET";
    public static final String HTTP_POST = "POST";


    private Activity theActivity;
    private ServiceResponse asyncResponse;


    /**
     * Constructor which intializes the Service Manager
     * @param activity context
     */
    public ServiceManager(@NonNull Activity activity) {
        theActivity = activity;
    }

    /**
     * Triggers Asynchronous service call
     * @param reqObj body if any
     * @param url url to create request
     * @param type get/post
     */
    public void doServiceCall(String reqObj, String url, String type) {
        Boolean checkNetwork = AppUtils.isNetworkAvailable(theActivity);
        if (checkNetwork) {
            new createAsynRequest().execute(reqObj, url, type);
        } else {
            if(asyncResponse != null) {
                asyncResponse.onError("No Internet Connection");
            }
        }
    }

    /**
     * AsyncTask method which takes
     * care of network operations in bg
     */
    private class createAsynRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            String reqObj = params[0];
            String url = params[1];
            String type = params[2];
            try {
                if (type.equalsIgnoreCase("POST")) {
                    result = executePostRequest(reqObj, url);
                } else {
                    result = executeGetRequest(url);
                }
            } catch (IOException e) {
                e.printStackTrace();
                if(asyncResponse != null) {
                    asyncResponse.onError(e.getLocalizedMessage());
                }
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(asyncResponse != null) {
                if (result != null) {
                    asyncResponse.onSuccess(result);
                } else {
                    asyncResponse.onError("There was error while making your request. Please check your internet connection and try again later !!");
                }
            }

        }
    }

    /**
     * Method responsible to trigger GET call
     * @param url url to create request
     * @return response if any else error
     */
    private String executeGetRequest(String url) {

        URL obj;
        StringBuffer response = null;
        try {
            obj = new URL(url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            //app headers

            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);

            BufferedReader in;
            if (200 <= con.getResponseCode() && con.getResponseCode() <= 299) {
                in = new BufferedReader(new InputStreamReader((con.getInputStream())));
            } else {
                in = new BufferedReader(new InputStreamReader((con.getErrorStream())));
            }
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();


        } catch (IOException e) {
            Log.e("Error in url",e.getLocalizedMessage());
        }


        return response != null ? response.toString() : null;
    }

    /**
     * Method responsible to trigger POST call
     * @param url url to create request
     * @return response if any else error
     */
    private String executePostRequest(String stringObj, String url) throws IOException {
        StringBuffer response = null;
        try {
            JSONObject req = null;
            if (stringObj != null) {
                req = new JSONObject(stringObj);
            }
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "*");
            con.setRequestProperty("Content-Length", "" + Integer.toString(stringObj.getBytes().length));
            con.setRequestProperty("Cache-Control", "no-cache");
            //app headers

            con.setRequestMethod("POST");


            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
            if (req != null) {
                osw.write(req.toString());
            }

            OutputStream os = con.getOutputStream();
            if (req != null) {
                os.write(req.toString().getBytes("UTF-8"));
            }
            os.close();


            // For POST only - END

            int responseCode = con.getResponseCode();
            System.out.println("POST Response  :: " + con.getResponseMessage());

            response = null;
            //success
            BufferedReader in;
            String inputLine;



            if (200 <= con.getResponseCode() && con.getResponseCode() <= 299) {
                in = new BufferedReader(new InputStreamReader((con.getInputStream())));

            } else {
                in = new BufferedReader(new InputStreamReader((con.getErrorStream())));
                in.close();
                return null;
            }
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            if(TextUtils.isEmpty(response) && TextUtils.isEmpty(response.toString()) && responseCode == 503) {
                response.append("Unable to connect to server, please try again later");
            }

            // print result

        } catch (Exception e) {
            Log.e("Error in url",e.getLocalizedMessage());
        }
        return response != null ? response.toString() : "";
    }

    /**
     * interface listener
     * @param asyncResponse
     */
    public void setOnResult(ServiceResponse asyncResponse) {
        this.asyncResponse = asyncResponse;
    }


    /**
     * Interface responsible of passing
     * service responses/data
     */
    public interface ServiceResponse {
        void onSuccess(String result);

        void onError(String error);
    }
}

