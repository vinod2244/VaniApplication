package com.shukla.tech.hospitalapplication.ui.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.shukla.tech.hospitalapplication.BluetoothPrinter;
import com.shukla.tech.hospitalapplication.R;
import com.shukla.tech.hospitalapplication.utils.DateUtils;
import com.shukla.tech.hospitalapplication.utils.ScreenshotUtils;

import java.io.File;
import java.lang.reflect.Method;

public class ReceiptActivity extends AppCompatActivity {
    TextView txtDate, txtName, txtGAge, txtAddress, txtsf, txtCustID;
    LinearLayout buttonPanel;
    RelativeLayout rootContent;
    BluetoothAdapter mBTAdapter;
    BluetoothSocket mBTSocket = null;
    Dialog dialogProgress;
    String BILL, TRANS_ID;
    //String PRINTER_MAC_ID = "00:1F:B7:02:8F:44";
    final String ERROR_MESSAGE = "There has been an error in printing the bill.";
    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    Button mScan, mPrint, mDisc;
//    BluetoothAdapter mBluetoothAdapter;
//    private UUID applicationUUID = UUID
//            .fromString("00001101-0000-1000-8000-00805F9B34FB");
//    private ProgressDialog mBluetoothConnectProgressDialog;
//    private BluetoothSocket mBluetoothSocket;
//    BluetoothDevice mBluetoothDevice;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_layout);
        Intent intent = getIntent();
        txtDate = findViewById(R.id.txtDate);
        txtName = findViewById(R.id.txtName);
        txtGAge = findViewById(R.id.txtGAge);
        txtAddress = findViewById(R.id.txtAddress);
        txtsf = findViewById(R.id.txtsf);
        txtCustID = findViewById(R.id.txtCustID);
       // txtDate.setText(DateUtils.getCurrDeviceDate("dd-MM-yyyy"));
        txtName.setText(intent.getStringExtra("name"));
        txtGAge.setText(intent.getStringExtra("Age"));
        txtAddress.setText(intent.getStringExtra("Address"));
        txtCustID.setText(intent.getStringExtra("id")+"/"+DateUtils.getCurrDeviceDate("dd-MM-yyyy"));
        //String finalfindings = intent.getStringExtra("final1")!=null
        txtsf.setText(intent.getStringExtra("final1") + "\n" + intent.getStringExtra("final2") + "\n" + intent.getStringExtra("final5") + "\n" + intent.getStringExtra("final3") + "\n" + intent.getStringExtra("final4"));
        buttonPanel = findViewById(R.id.buttonPanel);
        rootContent = findViewById(R.id.root_content);


        // txtsf.setText(ed.getText().toString());
        Button btnSave = findViewById(R.id.btnSave);
        Button btnPrint = findViewById(R.id.btnScreenshot);
        Button btnAdd = findViewById(R.id.btnAdd);
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ReceiptActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenshot();
                showDialog();
            }
        });
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap b = null;
                File file;
                buttonPanel.setVisibility(View.INVISIBLE);//set the visibility to INVISIBLE of first button
                //set the visibility to VISIBLE of hidden text

                b = ScreenshotUtils.getScreenShot(rootContent);

                //After taking screenshot reset the button and view again
                buttonPanel.setVisibility(View.VISIBLE);
                if (b != null) {
                    //    showScreenShotImage(b);//show bitmap over imageview

                    File saveFile = ScreenshotUtils.getMainDirectoryName(ReceiptActivity.this);//get the path to save screenshot
                    file = ScreenshotUtils.store(b, "screenshot" + DateUtils.getCurrDeviceDate("yyyyMMdd_HHmmss") + ".jpg", saveFile);//save the screenshot to selected path
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    Uri screenshotUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
                    sharingIntent.setType("image/jpg");
                    sharingIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                    startActivity(Intent.createChooser(sharingIntent, "Share using"));
                    // shareScreenshot(file);//finally share screenshot
                    //openAddData();
                } else
                    Toast.makeText(ReceiptActivity.this, R.string.screenshot_take_failed, Toast.LENGTH_SHORT).show();

//                try {
//
//
//                    mBTAdapter = BluetoothAdapter.getDefaultAdapter();
//
//                    if (mBTAdapter == null) {
//                        Toast.makeText(ReceiptActivity.this, "Device has no bluetooth capability", Toast.LENGTH_LONG).show();
//                        finish();
//                    } else {
//                        if (!mBTAdapter.isEnabled()) {
//                            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                            startActivityForResult(i, 0);
//                        }
//
//                        // Register the BroadcastReceiver
//                        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//                        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
//
//                        dialogProgress = new Dialog(ReceiptActivity.this);
//                        dialogProgress.setTitle("Finding printer...");
//                        dialogProgress.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                            public void onDismiss(DialogInterface dialog) {
//                                dialog.dismiss();
//                                setResult(RESULT_CANCELED);
//                                finish();
//                            }
//                        });
//                        dialogProgress.show();
//                    }
//
//                    if (mBTAdapter.isDiscovering())
//                        mBTAdapter.cancelDiscovery();
//                    else
//                        mBTAdapter.startDiscovery();
//
//                //    System.out.println("BT Searching status :" + mBTAdapter.isDiscovering());
//
//                } catch (Exception e) {
//                    Log.e("Class ", "My Exe ", e);
//                }
//                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                if (mBluetoothAdapter == null) {
//                    Toast.makeText(ReceiptActivity.this, "Message1", Toast.LENGTH_SHORT).show();
//                } else {
//                    if (!mBluetoothAdapter.isEnabled()) {
//                        Intent enableBtIntent = new Intent(
//                                BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                        startActivityForResult(enableBtIntent,
//                                REQUEST_ENABLE_BT);
//                    } else {
//                        ListPairedDevices();
//                        Intent connectIntent = new Intent(ReceiptActivity.this,
//                                DeviceListActivity.class);
//                        startActivityForResult(connectIntent,
//                                REQUEST_CONNECT_DEVICE);
//                    }
//                }
            }


        });
    }


    public void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Image saved in gallery in Reciepts folder");

        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        // Create the AlertDialog object and return it

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    // System.out.println("***" + device.getName() + " : " + device.getAddress());

                    if (device.getAddress() != null) {
                        mBTAdapter.cancelDiscovery();
                        dialogProgress.dismiss();

                        Toast.makeText(ReceiptActivity.this, device.getName() + " Printing data", Toast.LENGTH_LONG).show();
                        printBillToDevice(device.getAddress());
                        Toast.makeText(ReceiptActivity.this, device.getName() + " found", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ReceiptActivity.this, "connect printer to print", Toast.LENGTH_LONG).show();
                    }
                    // }
                }
            } catch (Exception e) {
                Log.e("Class ", "My Exe ", e);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (dialogProgress != null)
                dialogProgress.dismiss();
            if (mBTAdapter != null)
                mBTAdapter.cancelDiscovery();
            this.unregisterReceiver(mReceiver);
        } catch (Exception e) {
            Log.e("Class ", "My Exe ", e);
        }
    }


    @Override
    public void onBackPressed() {
        try {
            if (mBTAdapter != null)
                mBTAdapter.cancelDiscovery();
            this.unregisterReceiver(mReceiver);
        } catch (Exception e) {
            Log.e("Class ", "My Exe ", e);
        }
        setResult(RESULT_CANCELED);
        finish();
    }


    public void printBillToDevice(final String address) {
        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        dialogProgress.setTitle("Connecting...");
                        dialogProgress.show();
                    }

                });

                mBTAdapter.cancelDiscovery();

                try {
                    System.out.println("**************************#****connecting");
                    BluetoothDevice mdevice = mBTAdapter.getRemoteDevice(address);
                    Method m = mdevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                    mBTSocket = (BluetoothSocket) m.invoke(mdevice, 1);

                    if (mdevice != null)
                        mBTSocket.connect();


                    BluetoothPrinter mPrinter = new BluetoothPrinter(mdevice);
                    mPrinter.connectPrinter(new BluetoothPrinter.PrinterConnectListener() {

                        @Override
                        public void onConnected() {
                            Bitmap b = null;

                            buttonPanel.setVisibility(View.INVISIBLE);//set the visibility to INVISIBLE of first button
                            //set the visibility to VISIBLE of hidden text

                            b = ScreenshotUtils.getScreenShot(rootContent);

                            //After taking screenshot reset the button and view again
                            buttonPanel.setVisibility(View.VISIBLE);
                            mPrinter.setAlign(BluetoothPrinter.ALIGN_CENTER);
                            mPrinter.printImage(b);
                            mPrinter.finish();
                        }

                        @Override
                        public void onFailed() {
                            Log.d("BluetoothPrinter", "Conection failed");
                        }

                    });
                    setResult(RESULT_OK);
                    finish();
                } catch (Exception e) {
                    Log.e("Class ", "My Exe ", e);
                    e.printStackTrace();
                    setResult(RESULT_CANCELED);
                    finish();

                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            dialogProgress.dismiss();
                        } catch (Exception e) {
                            Log.e("Class ", "My Exe ", e);
                        }
                    }

                });

            }

        }).start();
    }

    private void takeScreenshot() {
        Bitmap b = null;

        buttonPanel.setVisibility(View.INVISIBLE);//set the visibility to INVISIBLE of first button
        //set the visibility to VISIBLE of hidden text

        b = ScreenshotUtils.getScreenShot(rootContent);

        //After taking screenshot reset the button and view again
        buttonPanel.setVisibility(View.VISIBLE);
        if (b != null) {
            //    showScreenShotImage(b);//show bitmap over imageview

            File saveFile = ScreenshotUtils.getMainDirectoryName(this);//get the path to save screenshot
            File file = ScreenshotUtils.store(b, "screenshot" + DateUtils.getCurrDeviceDate(DateUtils.FORMAT_YYYYMMDDTHHMMSSZ) + ".jpg", saveFile);//save the screenshot to selected path
            // shareScreenshot(file);//finally share screenshot
            //openAddData();
        } else
            Toast.makeText(ReceiptActivity.this, R.string.screenshot_take_failed, Toast.LENGTH_SHORT).show();


    }

//    private void shareScreenshot(File file) {
//        Uri uri = Uri.fromFile(file);//Convert file path into Uri for sharing
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_SEND);
//        intent.setType("image/*");
//        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
//        intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.sharing_text));
//        intent.putExtra(Intent.EXTRA_STREAM, uri);//pass uri here
//        startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
//    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }


//    public void openAddData() {
//        Intent intent = new Intent(ReceiptActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();
//    }
}
