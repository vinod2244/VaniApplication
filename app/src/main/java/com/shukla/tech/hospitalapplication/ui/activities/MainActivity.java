package com.shukla.tech.hospitalapplication.ui.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.collect.Range;
import com.shukla.tech.hospitalapplication.AddData;
import com.shukla.tech.hospitalapplication.R;
import com.shukla.tech.hospitalapplication.locationlib.OnActivityUpdatedListener;
import com.shukla.tech.hospitalapplication.locationlib.OnGeofencingTransitionListener;
import com.shukla.tech.hospitalapplication.locationlib.OnLocationUpdatedListener;
import com.shukla.tech.hospitalapplication.locationlib.OnReverseGeocodingListener;
import com.shukla.tech.hospitalapplication.locationlib.SmartLocation;
import com.shukla.tech.hospitalapplication.locationlib.geofencing.model.GeofenceModel;
import com.shukla.tech.hospitalapplication.locationlib.geofencing.utils.TransitionGeofence;
import com.shukla.tech.hospitalapplication.locationlib.location.providers.LocationGooglePlayServicesProvider;
import com.shukla.tech.hospitalapplication.utils.CameraUtil;
import com.shukla.tech.hospitalapplication.utils.DateUtils;
import com.shukla.tech.hospitalapplication.utils.ImageUtil;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity implements OnLocationUpdatedListener, OnActivityUpdatedListener, OnGeofencingTransitionListener {


    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_GALLERY_REQUEST_CODE = 200;

    //    public static final int MEDIA_TYPE_IMAGE = 1;
//    public static final int MEDIA_TYPE_GALLERY = 2;
    private static final int RESULT_LEFT_LOAD_IMAGE = 1;
    private static final int RESULT_RIGHT_LOAD_IMAGE = 2;
    private static final int RESULT_REPORT_LOAD_IMAGE = 3;
    private static final int REQUEST_LEFT_IMAGE_CAPTURE = 4;
    private static final int REQUEST_RIGHT_IMAGE_CAPTURE = 5;
    private static final int REQUEST_REPORT_IMAGE_CAPTURE = 6;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Constant used in the location settings dialog.
     */


    private static final String TAG = "tag";
    private Uri fileUri;
    long totalSize = 0;
    private String otoscopicImageLeftImage_filePath = null, otoscopicImageRightImage_filepath = null, audiogramReportImage_filepath = null;
    Bitmap bitmap;
    private String destinationDirectoryPath;
    private float maxHeight;
    private float maxWidth;
    private int quality;
    private Bitmap.CompressFormat compressFormat;
    String otoscopicImageLeftImage, otoscopicImageRightImage, audiogramReportImage;
    CheckBox chkOthers1, chkOthers;
    TextInputEditText edtOthers1, edtOthers, edtName, edtAge, edtAddress, edtDistrict, edtContact, edtAltContact, edtIdNumber, checkboxAudioRight, checkboxAudioLeft;
    TextView txtDate;
    Spinner idSpinner;
    Button btnSubmit;
    RadioGroup radioGrp;
    LinearLayout noReasonLayout;
    CheckBox checkboxSP, checkboxEP, checkboxHP, checkboxEDP, checkboxPP, checkboxMisA, checkboxDS, checkboxNS, checkboxPPC, checkboxUE, checkboxTWM, checkboxASEC,
            checkboxPNHL, checkboxNR, checkboxHI, checkboxHADRight, checkboxHADLeft, checkboxAudioReport,
            checkboxPerforationRight, checkboxPerforationLeft, checkboxDischargeRight, checkboxDischargeLeft, checkboxWaxRight, checkboxWaxLeft, checkboxOtoRight,
            checkboxOtoLeft, checkboxSpeechDelay, checkboxHearingLoss, checkboxEarPblm, checkboxStammering;
    Dialog receipt_dialog;
    RadioButton gender;
    String latitude, longitude;
    private String mLatitudeLabel;
    private String mLongitudeLabel;
    private String mLastUpdateTimeLabel;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    private String mLastUpdateTime;

    private LocationGooglePlayServicesProvider provider;

    private static final int LOCATION_PERMISSION_ID = 1001;

    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        int selected = radioGrp.getCheckedRadioButtonId();
        gender = findViewById(selected);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        // awesomeValidation.addValidation(this, R.id.edtName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        //  awesomeValidation.addValidation(this, R.id.edtAltContact, "^[2-9]{2}[0-9]{8}$", R.string.mobileerror);
        awesomeValidation.addValidation(this, R.id.edtContact, "^[6-9][0-9]{9}$", R.string.mobileerror);
        awesomeValidation.addValidation(this, R.id.edtAge, Range.closed(0, 150), R.string.ageerror);

        startLocation();
// Set labels.
        chkOthers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    edtOthers.setVisibility(View.VISIBLE);
                else
                    edtOthers.setVisibility(View.GONE);
            }
        });
        chkOthers1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    edtOthers1.setVisibility(View.VISIBLE);
                else
                    edtOthers1.setVisibility(View.GONE);
            }
        });
        checkboxOtoLeft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    selectImage(REQUEST_LEFT_IMAGE_CAPTURE);
            }
        });
        checkboxOtoRight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    selectImage(REQUEST_RIGHT_IMAGE_CAPTURE);
            }
        });
        checkboxAudioReport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    selectImage(REQUEST_REPORT_IMAGE_CAPTURE);
            }
        });
        checkboxNR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    noReasonLayout.setVisibility(View.VISIBLE);
                else
                    noReasonLayout.setVisibility(View.GONE);
            }
        });
        idSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getSelectedItem().toString();
                if (item.equals("Select Id proof")) {
                    edtIdNumber.setVisibility(View.GONE);
                } else {
                    edtIdNumber.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                edtIdNumber.setVisibility(View.GONE);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddData addData = new AddData(txtDate.getText().toString(), edtName.getText().toString(), edtAge.getText().toString(), String.valueOf(gender.getText()), edtAddress.getText().toString(), edtDistrict.getText().toString(),
                        edtContact.getText().toString(), edtAltContact.getText().toString(), idSpinner.getSelectedItem().toString(), edtIdNumber.getText().toString(), String.valueOf(checkboxEarPblm.isChecked()), String.valueOf(checkboxHearingLoss.isChecked()), String.valueOf(checkboxSpeechDelay.isChecked()),
                        edtOthers.getText().toString(), otoscopicImageLeftImage, otoscopicImageRightImage, String.valueOf(checkboxWaxLeft.isChecked()), String.valueOf(checkboxWaxRight.isChecked()), String.valueOf(checkboxDischargeLeft.isChecked()), String.valueOf(checkboxDischargeRight.isChecked()), String.valueOf(checkboxPerforationLeft.isChecked()),
                        String.valueOf(checkboxPerforationRight.isChecked()), audiogramReportImage, checkboxAudioLeft.getText().toString(), checkboxAudioRight.getText().toString(), String.valueOf(checkboxHADLeft.isChecked()), String.valueOf(checkboxHADRight.isChecked()), String.valueOf(checkboxHI.isChecked()),
                        checkboxPNHL.isChecked() ? checkboxPNHL.getText().toString() : "", checkboxASEC.isChecked() ? checkboxASEC.getText().toString() : "", checkboxTWM.isChecked() ? checkboxTWM.getText().toString() : "", checkboxUE.isChecked() ? checkboxUE.getText().toString() : "", checkboxPPC.isChecked() ? checkboxPPC.getText().toString() : "", String.valueOf(checkboxNS.isChecked()), String.valueOf(checkboxDS.isChecked()), String.valueOf(checkboxStammering.isChecked()), String.valueOf(checkboxMisA.isChecked())
                        , String.valueOf(checkboxPP.isChecked()), String.valueOf(checkboxEDP.isChecked()), String.valueOf(checkboxHP.isChecked()), String.valueOf(checkboxEP.isChecked()), String.valueOf(checkboxSP.isChecked()), edtOthers1.getText().toString());
                if (!edtContact.getText().toString().isEmpty() && awesomeValidation.validate())
                    saveData(addData);
                else
                    Toast.makeText(MainActivity.this, "Contact number is mandatory", Toast.LENGTH_LONG).show();
                //postHttpResponse(addData);
                //new MainActivity.OkHttpAync().execute(this, "adding", addData);

            }
        });

        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // showLast();
    }

    private void showLast() {
        Location lastLocation = SmartLocation.with(this).location().getLastLocation();
        if (lastLocation != null) {
//            locationText.setText(
//                    String.format("[From Cache] Latitude %.6f, Longitude %.6f",
//                            lastLocation.getLatitude(),
//                            lastLocation.getLongitude())
//            );
        }

        DetectedActivity detectedActivity = SmartLocation.with(this).activity().getLastActivity();
        if (detectedActivity != null) {
//            activityText.setText(
//                    String.format("[From Cache] Activity %s with %d%% confidence",
//                            getNameFromType(detectedActivity),
//                            detectedActivity.getConfidence())
//            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_ID && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocation();
        }
    }

    private void startLocation() {

        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);

        SmartLocation smartLocation = new SmartLocation.Builder(this).logging(true).build();

        smartLocation.location(provider).start(this);
        smartLocation.activity().start(this);

        // Create some geofences
        GeofenceModel mestalla = new GeofenceModel.Builder("1").setTransition(Geofence.GEOFENCE_TRANSITION_ENTER).setLatitude(39.47453120000001).setLongitude(-0.358065799999963).setRadius(500).build();
        smartLocation.geofencing().add(mestalla).start(this);
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */


    /**
     * Creates a callback for receiving location events.
     */


    private void saveData(final AddData addData) {
        @SuppressLint("StaticFieldLeak")
        class SendPostReqAsyncTask extends AsyncTask<String, Integer, String> {
            private ProgressDialog progressDialog;
            InputStream inputStream;
            String result;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Submitting Data...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected String doInBackground(String... params) {
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("curdate", addData.getDate()));
                nameValuePairs.add(new BasicNameValuePair("user", PrefUtils.getStringPreference(MainActivity.this, PrefUtils.KEY_USER)));
                nameValuePairs.add(new BasicNameValuePair("name", addData.getName()));
                nameValuePairs.add(new BasicNameValuePair("gender", addData.getGender()));
                nameValuePairs.add(new BasicNameValuePair("address", addData.getAddress()));
                nameValuePairs.add(new BasicNameValuePair("age", addData.getAge()));
                nameValuePairs.add(new BasicNameValuePair("district", addData.getDistrict()));
                nameValuePairs.add(new BasicNameValuePair("contact", addData.getContact()));
                nameValuePairs.add(new BasicNameValuePair("alternateContact", addData.getAlternateContact()));
                nameValuePairs.add(new BasicNameValuePair("idCard", addData.getIdCard()));
                nameValuePairs.add(new BasicNameValuePair("idCardValue", addData.getIdCardValue()));
                nameValuePairs.add(new BasicNameValuePair("earProblem", addData.getEarProblem()));
                nameValuePairs.add(new BasicNameValuePair("hearingLoss", addData.getHearingLoss()));
                nameValuePairs.add(new BasicNameValuePair("speechDelay", addData.getSpeechDelay()));
                nameValuePairs.add(new BasicNameValuePair("others", addData.getOthers()));
                nameValuePairs.add(new BasicNameValuePair("otoscopicImageLeft", otoscopicImageLeftImage));
                nameValuePairs.add(new BasicNameValuePair("otoscopicImageRight", otoscopicImageRightImage));
                nameValuePairs.add(new BasicNameValuePair("waxLeft", addData.getWaxLeft()));
                nameValuePairs.add(new BasicNameValuePair("waxRight", addData.getWaxRight()));
                nameValuePairs.add(new BasicNameValuePair("earDischargeLeft", addData.getEarDischargeLeft()));
                nameValuePairs.add(new BasicNameValuePair("earDischargeRight", addData.getEarDischargeRight()));
                nameValuePairs.add(new BasicNameValuePair("perforationLeft", addData.getPerforationLeft()));
                nameValuePairs.add(new BasicNameValuePair("perforationRight", addData.getPerforationRight()));
                nameValuePairs.add(new BasicNameValuePair("audiogramReport", audiogramReportImage));
                nameValuePairs.add(new BasicNameValuePair("audiogramLeft", addData.getAudiogramLeft()));
                nameValuePairs.add(new BasicNameValuePair("audiogramRight", addData.getAudiogramRight()));
                nameValuePairs.add(new BasicNameValuePair("hearingAidTrialLeftEar", addData.getHearingAidTrialLeftEar()));
                nameValuePairs.add(new BasicNameValuePair("hearingAidTrialRightEar", addData.getHearingAidTrialRightEar()));
                nameValuePairs.add(new BasicNameValuePair("hearingImprovement", addData.getHearingImprovement()));
                nameValuePairs.add(new BasicNameValuePair("noResponseR1", addData.getNoResponseR1()));
                nameValuePairs.add(new BasicNameValuePair("noResponseR2", addData.getNoResponseR2()));
                nameValuePairs.add(new BasicNameValuePair("noResponseR3", addData.getNoResponseR3()));
                nameValuePairs.add(new BasicNameValuePair("noResponseR4", addData.getNoResponseR4()));
                nameValuePairs.add(new BasicNameValuePair("noResponseR5", addData.getNoResponseR5()));
                nameValuePairs.add(new BasicNameValuePair("normalSpeech", addData.getNormalSpeech()));
                nameValuePairs.add(new BasicNameValuePair("delayedSpeech", addData.getDelayedSpeech()));
                nameValuePairs.add(new BasicNameValuePair("stammering", addData.getStammering()));
                nameValuePairs.add(new BasicNameValuePair("misArticulation", addData.getMisArticulation()));
                nameValuePairs.add(new BasicNameValuePair("puberphonia", addData.getPuberphonia()));
                nameValuePairs.add(new BasicNameValuePair("earDischargeProblem", addData.getEarDischargeProblem()));
                nameValuePairs.add(new BasicNameValuePair("hearingProblem", addData.getHearingProblem()));
                nameValuePairs.add(new BasicNameValuePair("earPerforation", addData.getEarPerforation()));
                nameValuePairs.add(new BasicNameValuePair("speechProblem", addData.getSpeechProblem()));
                nameValuePairs.add(new BasicNameValuePair("othersFinalScreeningFinding", addData.getOthersFinalScreeningFinding()));
                nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
                nameValuePairs.add(new BasicNameValuePair("longitude", longitude));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    String url = "https://hearingcareclinics.com/android/vaniHearing/backendPhp/add.php";
                    HttpPost httpPost = new HttpPost(url);

//                    AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
//                            new AndroidMultiPartEntity.ProgressListener() {
//                                @Override
//                                public void transferred(long num) {
//                                    publishProgress((int) ((num / (float) totalSize) * 100));
//                                }
//                            });
//                    entity.addPart("curdate", new StringBody(addData.getDate()));
//                    entity.addPart("user", new StringBody(PrefUtils.getStringPreference(MainActivity.this, PrefUtils.KEY_USER)));
//                    entity.addPart("name", new StringBody(addData.getName()));
//                    entity.addPart("gender", new StringBody(addData.getGender()));
//                    entity.addPart("address", new StringBody(addData.getAddress()));
//                    entity.addPart("age", new StringBody(addData.getAge()));
//                    entity.addPart("district", new StringBody(addData.getDistrict()));
//                    entity.addPart("contact", new StringBody(addData.getContact()));
//                    entity.addPart("alternateContact", new StringBody(addData.getAlternateContact()));
//                    entity.addPart("idCard", new StringBody(addData.getIdCard()));
//                    entity.addPart("idCardValue", new StringBody(addData.getIdCardValue()));
//                    entity.addPart("earProblem", new StringBody(addData.getEarProblem()));
//                    entity.addPart("hearingLoss", new StringBody(addData.getHearingLoss()));
//                    entity.addPart("speechDelay", new StringBody(addData.getSpeechDelay()));
//                    entity.addPart("others", new StringBody(addData.getOthers()));
//                    entity.addPart("otoscopicImageLeft", new StringBody(otoscopicImageLeftImage));
//                    entity.addPart("otoscopicImageRight", new StringBody(otoscopicImageRightImage));
//                    entity.addPart("waxLeft", new StringBody(addData.getWaxLeft()));
//                    entity.addPart("waxRight", new StringBody(addData.getWaxRight()));
//                    entity.addPart("earDischargeLeft", new StringBody(addData.getEarDischargeLeft()));
//                    entity.addPart("earDischargeRight", new StringBody(addData.getEarDischargeRight()));
//                    entity.addPart("perforationLeft", new StringBody(addData.getPerforationLeft()));
//                    entity.addPart("perforationRight", new StringBody(addData.getPerforationRight()));
//                    entity.addPart("audiogramReport", new StringBody(audiogramReportImage));
//                    entity.addPart("audiogramLeft", new StringBody(addData.getAudiogramLeft()));
//                    entity.addPart("audiogramRight", new StringBody(addData.getAudiogramRight()));
//                    entity.addPart("hearingAidTrialLeftEar", new StringBody(addData.getHearingAidTrialLeftEar()));
//                    entity.addPart("hearingAidTrialRightEar", new StringBody(addData.getHearingAidTrialRightEar()));
//                    entity.addPart("hearingImprovement", new StringBody(addData.getHearingImprovement()));
//                    entity.addPart("noResponseR1", new StringBody(addData.getNoResponseR1()));
//                    entity.addPart("noResponseR2", new StringBody(addData.getNoResponseR2()));
//                    entity.addPart("noResponseR3", new StringBody(addData.getNoResponseR3()));
//                    entity.addPart("noResponseR4", new StringBody(addData.getNoResponseR4()));
//                    entity.addPart("noResponseR5", new StringBody(addData.getNoResponseR5()));
//                    entity.addPart("normalSpeech", new StringBody(addData.getNormalSpeech()));
//                    entity.addPart("delayedSpeech", new StringBody(addData.getDelayedSpeech()));
//                    entity.addPart("stammering", new StringBody(addData.getStammering()));
//                    entity.addPart("misArticulation", new StringBody(addData.getMisArticulation()));
//                    entity.addPart("puberphonia", new StringBody(addData.getPuberphonia()));
//                    entity.addPart("earDischargeProblem", new StringBody(addData.getEarDischargeProblem()));
//                    entity.addPart("hearingProblem", new StringBody(addData.getHearingProblem()));
//                    entity.addPart("earPerforation", new StringBody(addData.getEarPerforation()));
//                    entity.addPart("speechProblem", new StringBody(addData.getSpeechProblem()));
//                    entity.addPart("othersFinalScreeningFinding", new StringBody(addData.getOthersFinalScreeningFinding()));
//                    totalSize = entity.getContentLength();
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpClient.execute(httpPost);
                    @SuppressWarnings("unused")
                    HttpEntity r_entity = response.getEntity();
                    inputStream = r_entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8), 8);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    result = sb.toString();
                } catch (IOException e) {
                    //throws an exception
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                return result;

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                progressDialog.dismiss();
//                reedem_dialog.dismiss();
//                getCrowns();
                if (result != null && result.contains("Data Inserted Successfully")) {
                    if (result.contains("last_id")) {
                        String line = result;
                        String pattern = ".*last_id(.*?)(?:\\\\s*\\\\S+=.*|$)";
                        Pattern r = Pattern.compile(pattern);
                        Matcher m = r.matcher(line);
                        if (m.find()) {
                            showReceipt(m.group(1));
                        }
                    }

                    reset();
                } else {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                }

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }

    public void reset() {
        checkboxStammering.setChecked(false);
        chkOthers.setChecked(false);
        chkOthers1.setChecked(false);
        checkboxSP.setChecked(false);
        checkboxEP.setChecked(false);
        checkboxEDP.setChecked(false);
        checkboxHP.setChecked(false);
        checkboxPP.setChecked(false);
        checkboxMisA.setChecked(false);
        checkboxDS.setChecked(false);
        checkboxNS.setChecked(false);
        checkboxPPC.setChecked(false);
        checkboxUE.setChecked(false);
        checkboxTWM.setChecked(false);
        checkboxASEC.setChecked(false);
        checkboxPNHL.setChecked(false);
        checkboxNR.setChecked(false);
        checkboxEarPblm.setChecked(false);
        checkboxHI.setChecked(false);
        checkboxHADRight.setChecked(false);
        checkboxHADLeft.setChecked(false);
        checkboxAudioRight.setText("");
        checkboxAudioLeft.setText("");
        checkboxAudioReport.setChecked(false);
        checkboxPerforationRight.setChecked(false);
        checkboxPerforationLeft.setChecked(false);
        checkboxDischargeRight.setChecked(false);
        checkboxDischargeLeft.setChecked(false);
        checkboxWaxRight.setChecked(false);
        checkboxWaxLeft.setChecked(false);
        checkboxOtoRight.setChecked(false);
        checkboxOtoLeft.setChecked(false);
        checkboxSpeechDelay.setChecked(false);
        checkboxHearingLoss.setChecked(false);
        edtOthers.setText("");
        edtOthers1.setText("");
        edtName.setText("");
        edtAge.setText("");
        edtAddress.setText("");
        edtDistrict.setText("");
        edtContact.setText("");
        edtAltContact.setText("");
        edtIdNumber.setText("");
        txtDate.setText("");
        txtDate.setText(DateUtils.getCurrDeviceDate("dd-MM-yyyy"));
        idSpinner.setSelection(0);
        otoscopicImageLeftImage_filePath = null;
        otoscopicImageRightImage_filepath = null;
        audiogramReportImage_filepath = null;
    }

    @SuppressLint("SetTextI18n")
    public void showReceipt(String id) {
        String finalScreeningFindings1 = checkboxEDP.isChecked() ? checkboxEDP.getText().toString() : "";
        String finalScreeningFindings2 = checkboxHP.isChecked() ? checkboxHP.getText().toString() : "";
        String finalScreeningFindings3 = checkboxSP.isChecked() ? checkboxSP.getText().toString() : "";
        String finalScreeningFindings4 = chkOthers1.isChecked() ? edtOthers1.getText().toString() : "";
        String finalscreen = checkboxEP.isChecked() ? checkboxEP.getText().toString() : "";
        Intent intent = new Intent(MainActivity.this, ReceiptActivity.class);
        intent.putExtra("name", edtName.getText().toString());
        intent.putExtra("Age", gender.getText().toString() + "/" + edtAge.getText().toString());
        intent.putExtra("Address", edtAddress.getText().toString());
        intent.putExtra("final1", finalScreeningFindings1);
        intent.putExtra("final2", finalScreeningFindings2);
        intent.putExtra("final3", finalScreeningFindings3);
        intent.putExtra("final4", finalScreeningFindings4);
        intent.putExtra("final5", finalscreen);
        intent.putExtra("id", id);
        startActivity(intent);
        finish();

    }

    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static void store(Bitmap bm, String fileName) {
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareImage(File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            receipt_dialog.dismiss();
            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    public void initialize() {
        noReasonLayout = findViewById(R.id.noreasonLayout);
        radioGrp = findViewById(R.id.radioGrp);
        checkboxStammering = findViewById(R.id.checkboxStammering);
        chkOthers = findViewById(R.id.chkOthers);
        chkOthers1 = findViewById(R.id.chkOthers1);
        checkboxSP = findViewById(R.id.checkboxSP);
        checkboxEP = findViewById(R.id.checkboxEP);
        checkboxEDP = findViewById(R.id.checkboxEDP);
        checkboxHP = findViewById(R.id.checkboxHP);
        checkboxPP = findViewById(R.id.checkboxPP);
        checkboxMisA = findViewById(R.id.checkboxMisA);
        checkboxDS = findViewById(R.id.checkboxDS);
        checkboxNS = findViewById(R.id.checkboxNS);
        checkboxPPC = findViewById(R.id.checkboxPPC);
        checkboxUE = findViewById(R.id.checkboxUE);
        checkboxTWM = findViewById(R.id.checkboxTWM);
        checkboxASEC = findViewById(R.id.checkboxASEC);
        checkboxPNHL = findViewById(R.id.checkboxPNHL);
        checkboxNR = findViewById(R.id.checkboxNR);
        checkboxEarPblm = findViewById(R.id.checkboxEarPblm);
        checkboxHI = findViewById(R.id.checkboxHI);
        checkboxHADRight = findViewById(R.id.checkboxHADRight);
        checkboxHADLeft = findViewById(R.id.checkboxHADLeft);
        checkboxAudioRight = findViewById(R.id.checkboxAudioRight);
        checkboxAudioLeft = findViewById(R.id.checkboxAudioLeft);
        checkboxAudioReport = findViewById(R.id.checkboxAudioReport);
        checkboxPerforationRight = findViewById(R.id.checkboxPerforationRight);
        checkboxPerforationLeft = findViewById(R.id.checkboxPerforationLeft);
        checkboxDischargeRight = findViewById(R.id.checkboxDischargeRight);
        checkboxDischargeLeft = findViewById(R.id.checkboxDischargeLeft);
        checkboxWaxRight = findViewById(R.id.checkboxWaxRight);
        checkboxWaxLeft = findViewById(R.id.checkboxWaxLeft);
        checkboxOtoRight = findViewById(R.id.checkboxOtoRight);
        checkboxOtoLeft = findViewById(R.id.checkboxOtoLeft);
        checkboxSpeechDelay = findViewById(R.id.checkboxSpeechDelay);
        checkboxHearingLoss = findViewById(R.id.checkboxHearingLoss);
        edtOthers = findViewById(R.id.edtOthers);
        edtOthers1 = findViewById(R.id.edtOthers1);
        edtName = findViewById(R.id.edtName);
        edtAge = findViewById(R.id.edtAge);
        edtAddress = findViewById(R.id.edtAddress);
        edtDistrict = findViewById(R.id.edtDistrict);
        edtContact = findViewById(R.id.edtContact);
        edtAltContact = findViewById(R.id.edtAltContact);
        edtIdNumber = findViewById(R.id.edtIdNumber);
        txtDate = findViewById(R.id.txtDate);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtDate.setText(DateUtils.getCurrDeviceDate("dd-MM-yyyy"));
        idSpinner = findViewById(R.id.idProof);
        maxWidth = 612.0f;
        maxHeight = 816.0f;
        compressFormat = Bitmap.CompressFormat.JPEG;
        quality = 80;
        destinationDirectoryPath = getApplicationContext().getCacheDir().getPath() + File.pathSeparator + "VaniHearing";
    }

    private void selectImage(int requestCode) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    captureImage(requestCode);
                } else if (options[item].equals("Choose from Gallery")) {
                    switch (requestCode) {
                        case REQUEST_LEFT_IMAGE_CAPTURE:
                            onPickPhoto(RESULT_LEFT_LOAD_IMAGE);
                            break;
                        case REQUEST_RIGHT_IMAGE_CAPTURE:
                            onPickPhoto(RESULT_RIGHT_LOAD_IMAGE);
                            break;
                        case REQUEST_REPORT_IMAGE_CAPTURE:
                            onPickPhoto(RESULT_REPORT_LOAD_IMAGE);
                            break;
                    }

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void captureImage(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            String fileName = "temp.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(takePictureIntent, requestCode);
        }
    }

    public void onPickPhoto(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    public File compressToFile(File file) throws Throwable {
        return ImageUtil.compressImage(MainActivity.this, Uri.fromFile(file), maxWidth, maxHeight, compressFormat, quality, destinationDirectoryPath);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (fileUri != null) {
            outState.putString("mCapturedImageURI", fileUri.toString());
        }
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
        if (savedInstanceState.containsKey("mCapturedImageURI")) {
            fileUri = Uri.parse(savedInstanceState.getString("mCapturedImageURI"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOCATION_PERMISSION_ID:
                if (provider != null) {
                    provider.onActivityResult(requestCode, resultCode, data);
                }
                break;
            case RESULT_LEFT_LOAD_IMAGE:
                if (requestCode == RESULT_LEFT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    @SuppressLint("Recycle")
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    // String picturePath = cursor.getString(columnIndex);
                    otoscopicImageLeftImage_filePath = cursor.getString(columnIndex);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // down sizing image as it throws OutOfMemory Exception for large images
                    options.inSampleSize = 8;
                    bitmap = CameraUtil.getBitmap(otoscopicImageLeftImage_filePath, MainActivity.this);
                    String path = CameraUtil.compressImage(otoscopicImageLeftImage_filePath);

                    Bitmap bm = BitmapFactory.decodeFile(path);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 75, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    otoscopicImageLeftImage = Base64.encodeToString(b, Base64.DEFAULT);
                }
                break;
            case RESULT_RIGHT_LOAD_IMAGE:
                if (requestCode == RESULT_RIGHT_LOAD_IMAGE && resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    otoscopicImageRightImage_filepath = cursor.getString(columnIndex);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // down sizing image as it throws OutOfMemory Exception for large images
                    options.inSampleSize = 8;
                    bitmap = BitmapFactory.decodeFile(otoscopicImageRightImage_filepath, options);
                    String filename = otoscopicImageRightImage_filepath.substring(otoscopicImageRightImage_filepath.lastIndexOf("/") + 1);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 75, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    otoscopicImageRightImage = Base64.encodeToString(b, Base64.DEFAULT);
                }
                break;
            case RESULT_REPORT_LOAD_IMAGE:
                if (requestCode == RESULT_REPORT_LOAD_IMAGE && resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    @SuppressLint("Recycle")
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    audiogramReportImage_filepath = cursor.getString(columnIndex);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // down sizing image as it throws OutOfMemory Exception for large images
                    options.inSampleSize = 8;
                    bitmap = BitmapFactory.decodeFile(audiogramReportImage_filepath, options);
                    String filename = audiogramReportImage_filepath.substring(audiogramReportImage_filepath.lastIndexOf("/") + 1);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 75, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    audiogramReportImage = Base64.encodeToString(b, Base64.DEFAULT);
                }
                break;
            case REQUEST_LEFT_IMAGE_CAPTURE:
                if (requestCode == REQUEST_LEFT_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = managedQuery(fileUri, projection, null, null, null);
                    int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String picturePath = cursor.getString(column_index_data);
                    otoscopicImageLeftImage_filePath = cursor.getString(column_index_data);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // down sizing image as it throws OutOfMemory Exception for larger
                    // images
                    options.inSampleSize = 8;
                    bitmap = BitmapFactory.decodeFile(otoscopicImageLeftImage_filePath, options);
                    String filename = otoscopicImageLeftImage_filePath.substring(otoscopicImageLeftImage_filePath.lastIndexOf("/") + 1);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 75, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    otoscopicImageLeftImage = Base64.encodeToString(b, Base64.DEFAULT);
                }
                break;
            case REQUEST_RIGHT_IMAGE_CAPTURE:
                if (requestCode == REQUEST_RIGHT_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = managedQuery(fileUri, projection, null, null, null);
                    int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String picturePath = cursor.getString(column_index_data);
                    otoscopicImageRightImage_filepath = cursor.getString(column_index_data);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // down sizing image as it throws OutOfMemory Exception for larger
                    // images
                    options.inSampleSize = 8;
                    bitmap = BitmapFactory.decodeFile(otoscopicImageRightImage_filepath, options);
                    String filename = otoscopicImageRightImage_filepath.substring(otoscopicImageRightImage_filepath.lastIndexOf("/") + 1);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 75, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    otoscopicImageRightImage = Base64.encodeToString(b, Base64.DEFAULT);
//

                }
                break;
            case REQUEST_REPORT_IMAGE_CAPTURE:
                if (requestCode == REQUEST_REPORT_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = managedQuery(fileUri, projection, null, null, null);
                    int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String picturePath = cursor.getString(column_index_data);
                    audiogramReportImage_filepath = cursor.getString(column_index_data);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // down sizing image as it throws OutOfMemory Exception for larger
                    // images
                    options.inSampleSize = 8;
                    bitmap = BitmapFactory.decodeFile(audiogramReportImage_filepath, options);
                    String filename = audiogramReportImage_filepath.substring(audiogramReportImage_filepath.lastIndexOf("/") + 1);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 75, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    audiogramReportImage = Base64.encodeToString(b, Base64.DEFAULT);
//                    edtupload.setText(filename);
//                    txtupload.setText(filename);

                }
                break;
        }
    }

    private void showLocation(Location location) {
        if (location != null) {
            @SuppressLint("DefaultLocale") final String text = String.format("Latitude %.6f, Longitude %.6f",
                    location.getLatitude(),
                    location.getLongitude());
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            Log.e("lat", String.valueOf(location.getLatitude()));
            Log.e("long", String.valueOf(location.getLongitude()));

            // We are going to get the address for the current position
            SmartLocation.with(this).geocoding().reverse(location, new OnReverseGeocodingListener() {
                @Override
                public void onAddressResolved(Location original, List<Address> results) {
                    if (results.size() > 0) {
                        Address result = results.get(0);
                        StringBuilder builder = new StringBuilder(text);
                        builder.append("\n[Reverse Geocoding] ");
                        List<String> addressElements = new ArrayList<>();
                        for (int i = 0; i <= result.getMaxAddressLineIndex(); i++) {
                            addressElements.add(result.getAddressLine(i));
                        }
                        builder.append(TextUtils.join(", ", addressElements));
                        // locationText.setText(builder.toString());
                        Log.e("lat", String.valueOf(location.getLatitude()));
                        Log.e("long", String.valueOf(location.getLongitude()));
                    }
                }
            });
        } else {
            // locationText.setText("Null location");
        }
    }

    @Override
    public void onActivityUpdated(DetectedActivity detectedActivity) {

    }

    @Override
    public void onGeofenceTransition(TransitionGeofence transitionGeofence) {

    }

    @Override
    public void onLocationUpdated(Location location) {
        showLocation(location);
    }
}
