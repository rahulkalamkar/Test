package com.android.sampleapp2;

import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sampleapp2.Adapter.CharacterAdapter;
import com.android.sampleapp2.Conectivity.API;
import com.android.sampleapp2.Conectivity.APIClient;
import com.android.sampleapp2.Model.Character;
import com.android.sampleapp2.Utils.EndlessRecyclerViewScrollListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<Character> characterArrayList;
    String nextUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        progressBar = (ProgressBar) findViewById(R.id.mainProgressBar);
        characterArrayList = new ArrayList<Character>();

        if (isInternetOn()) {
            progressBar.setVisibility(View.VISIBLE);
            fetchData(getResources().getString(R.string.url));
        } else {
            showNetworkError();
        }
    }

    // Fetch data from server
    public void fetchData(String path) {
        if (!isInternetOn()) {
            showNetworkError();
            return;
        }
        URL url = null;
        try {
            progressBar.setVisibility(View.VISIBLE);
            url = new URL(path);

            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();
            String parameters = url.getQuery();

            API api = APIClient.getClient(baseUrl).create(API.class);
            Call<com.android.sampleapp2.Model.Response> call = api.getData(apiName, getKeyValuesForApiParametersNew(parameters));
            call.enqueue(new Callback<com.android.sampleapp2.Model.Response>() {
                @Override
                public void onResponse(Call<com.android.sampleapp2.Model.Response> call, Response<com.android.sampleapp2.Model.Response> response) {
                    if (response != null) {
                        com.android.sampleapp2.Model.Response response1 = response.body();
                        characterArrayList.addAll(response1.getResults());
                        nextUrl = response1.getNext();
                        displayData();
                        if (TextUtils.isEmpty(nextUrl))
                            isEnd = true;
                    } else {
                        if (characterArrayList == null || characterArrayList.size() == 0) {
                            showMessage("There is an issue in web service");
                        } else {
                            showRetryAlert();
                        }
                    }
                }

                @Override
                public void onFailure(Call<com.android.sampleapp2.Model.Response> call, Throwable t) {
                    Log.e("Data", "" + t.getMessage());
                    if (characterArrayList == null || characterArrayList.size() == 0) {
                        showMessage("There is an issue in web service");
                    } else {
                        showRetryAlert();
                    }
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // Display Message
    public void showMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static HashMap<String, String> getKeyValuesForApiParametersNew(String parameters) {
        HashMap map = new HashMap();
        if (parameters != null) {
            String[] str = parameters.split("&");
            for (int i = 0; i < str.length; i++) {
                String ss[] = str[i].split("=");
                map.put(ss[0], ss[1]);
            }
        }
        return map;
    }

    // Display alert to user
    public void showRetryAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Error while loading data");
        alertDialogBuilder
                .setMessage("")
                .setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!TextUtils.isEmpty(nextUrl)) {
                            fetchData(nextUrl);
                        } else {
                            progressBar.setVisibility(View.VISIBLE);
                            fetchData(getResources().getString(R.string.url));
                        }
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    CharacterAdapter characterAdapter;
    LinearLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener scrollListener;
    boolean loading = false;
    boolean isEnd = false;
    int y = 0;
    private int endItem = 10;
    private int totalItem = 0;
    private int startItem = 0;

    public void displayData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loading = false;
                if (characterAdapter == null) {
                    characterAdapter = new CharacterAdapter(getApplicationContext(), characterArrayList);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(characterAdapter);
                } else {
                    characterAdapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.GONE);
                totalItem = characterArrayList.size();
                scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                        if (totalItem > endItem || totalItem == endItem) {
                            if (!isEnd & !loading) {
                                startItem = endItem + 1;
                                endItem = endItem + 5;
                                loading = true;
                                fetchData(nextUrl);
                            }
                        }

                    }
                };
                recyclerView.addOnScrollListener(scrollListener);


            }
        });
    }

    public boolean isInternetOn() {

        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    public void showNetworkError() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Internet connection error");
        alertDialogBuilder
                .setMessage("Please check your internet connection")
                .setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!TextUtils.isEmpty(nextUrl)) {
                            fetchData(nextUrl);
                        } else {
                            progressBar.setVisibility(View.VISIBLE);
                            fetchData(getResources().getString(R.string.url));
                        }
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
