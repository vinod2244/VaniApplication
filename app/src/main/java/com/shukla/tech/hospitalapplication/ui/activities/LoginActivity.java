package com.shukla.tech.hospitalapplication.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.shukla.tech.hospitalapplication.R;
import com.shukla.tech.hospitalapplication.common.Constants;
import com.shukla.tech.hospitalapplication.utils.ConnectivityReceiver;
import com.shukla.tech.hospitalapplication.utils.PrefUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    public static final String USER_NAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    Button btnLogin;
    TextInputEditText edtUsername, edtPassword;
    LinearLayout llLoading;
    TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        llLoading = findViewById(R.id.llLoading);
        tvMessage = findViewById(R.id.tvMessage);
        if (PrefUtils.isLoggedIn(getApplicationContext())) {

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(USER_NAME, PrefUtils.getStringPreference(LoginActivity.this,
                    PrefUtils.KEY_USER));
            intent.putExtra(PASSWORD, PrefUtils.getStringPreference(LoginActivity.this,
                    PrefUtils.KEY_USER_PASS));
            finish();
            startActivity(intent);
        }
        Constants.isConnected = ConnectivityReceiver.isConnected();
        if (Constants.isConnected) {
            //AlertDialogManager.showToast(getApplicationContext(),Constants.NETWORK_STATUS);
        } else {
            if (PrefUtils.isLoggedIn(getApplicationContext())) {

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra(USER_NAME, PrefUtils.getStringPreference(LoginActivity.this,
                        PrefUtils.KEY_USER));
                intent.putExtra(PASSWORD, PrefUtils.getStringPreference(LoginActivity.this,
                        PrefUtils.KEY_USER_PASS));
                finish();
                startActivity(intent);
            }
            //AlertDialogManager.showNetworkAlert(getApplicationContext());
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtUsername.getText().toString())) {
                    Toast.makeText(LoginActivity.this, getString(R.string.please_enter_username), Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                    Toast.makeText(LoginActivity.this, getString(R.string.please_enter_password), Toast.LENGTH_LONG).show();
                } else {
                    login(edtUsername.getText().toString(), edtPassword.getText().toString());
                }
            }
        });
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showLoader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                llLoading.setVisibility(View.VISIBLE);
            }
        });
    }

    public void showLoader(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                llLoading.setVisibility(View.VISIBLE);
                tvMessage.setText(message);
            }
        });
    }

    public void hideLoader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void login(final String username, final String password) {

        if (!isNetworkAvailable()) {
            Toast.makeText(LoginActivity.this, getString(R.string.login_failure), Toast.LENGTH_LONG).show();
        } else {
            class LoginAsync extends AsyncTask<String, Void, String> {

                //private ProgressDialog progressDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    showLoader();
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                protected String doInBackground(String... params) {
                    String uname = params[0];
                    String pass = params[1];
                    InputStream is;
                    List<NameValuePair> nameValuePairs = new ArrayList<>();
                    nameValuePairs.add(new BasicNameValuePair("username", uname));
                    nameValuePairs.add(new BasicNameValuePair("password", pass));
                    nameValuePairs.add(new BasicNameValuePair("appName", "vaniHearing"));
                    String result = null;

                    try {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost("https://www.hearingcareclinics.com/android/vaniHearing/backendPhp/login.php");
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                        HttpResponse response = httpClient.execute(httpPost);

                        HttpEntity entity = response.getEntity();

                        is = entity.getContent();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8), 8);
                        StringBuilder sb = new StringBuilder();

                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                        result = sb.toString();
                    } catch (IOException e) {
                        Log.e("error", e.getMessage());
                        e.printStackTrace();
                    }
                    return result;
                }

                @Override
                protected void onPostExecute(String result) {
                    String s = result.trim();
                    hideLoader();
                    if (s.equalsIgnoreCase("success")) {
                        PrefUtils.setBooleanPreference(LoginActivity.this, PrefUtils.IS_LOGIN, true);
                        PrefUtils.setStringPreference(LoginActivity.this,
                                PrefUtils.KEY_USER, edtUsername.getText().toString());
                        PrefUtils.setStringPreference(LoginActivity.this,
                                PrefUtils.KEY_USER_PASS, edtPassword.getText().toString());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(USER_NAME, username);
                        finish();
                        startActivity(intent);
                    } else {
                        showCustomDialog(getString(R.string.alert), "Username/Password is incorrect",
                                getString(R.string.ok), null);
                        //alert.showAlertDialog(MainActivity.this, "Login failed..", "Username/Password is incorrect", false);
                        // Toast.makeText(getApplicationContext(), "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                    }
                }
            }

            LoginAsync la = new LoginAsync();
            la.execute(username, password);
        }

    }


    @SuppressLint("ShowToast")
    private void validateOffline() {

        Toast.makeText(this, getString(R.string.login_failure), Toast.LENGTH_LONG);

    }

    public void showCustomDialog(String title, String message, String positiveText, String
            negativeText) {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
