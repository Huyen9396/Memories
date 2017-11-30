package com.apps.huyenpham.memories.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apps.huyenpham.memories.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.apps.huyenpham.memories.model.Utils.COL_CONTENT;
import static com.apps.huyenpham.memories.model.Utils.COL_DATE;
import static com.apps.huyenpham.memories.model.Utils.COL_ID;
import static com.apps.huyenpham.memories.model.Utils.COL_TIME;
import static com.apps.huyenpham.memories.model.Utils.COL_TITLE;
import static com.apps.huyenpham.memories.model.Utils.TABLE_NAME;
import static com.apps.huyenpham.memories.model.Utils.checkPressEditFab;
import static com.apps.huyenpham.memories.model.Utils.database;
import static com.apps.huyenpham.memories.model.Utils.idData;
import static com.apps.huyenpham.memories.model.Utils.latitude;
import static com.apps.huyenpham.memories.model.Utils.longitude;

public class GetDataActivity extends Activity {
    private EditText edtContent, edtTitle;
    private ImageView image;
    private ImageButton btnService, btnCamera, btnFolder, btnSave, btnMenu;
    private ImageButton btnHome, btnPhoto, btnAlbum;
    private LinearLayout menuBar, menuBarNav;
    private boolean checkPressFab = false;
    private final int REQUEST_CAPTURE = 123;
    private final int REQUEST_FOLDER = 456;
    private final int REQUEST_LOCATION = 789;
    private byte[] photoConverted;
    private boolean checkPhotoCapture = false, checkPhotoFolder = false;
    private LocationManager locationManager;

    private final String GET_TITLE_CONTENT = "SELECT " + COL_TITLE + ", " + COL_CONTENT + " FROM Memories WHERE " + COL_ID + " = '"+ idData +"'";

    private final String DELETE_DATA = "DELETE FROM " + TABLE_NAME + " WHERE id = '"+ idData +"'";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);
        initView();
        supportView();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            getLocation();
        } else {
            dialogOpenGPS();
        }

        final Calendar calendar = Calendar.getInstance();

        if (checkPressEditFab) {
            Cursor cursor = database.getData(GET_TITLE_CONTENT);
            while (cursor.moveToNext()) {
                edtTitle.setText(cursor.getString(0));
                edtContent.setText(cursor.getString(1));
            }

        }

        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPressFab == false) {
                    menuBar.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_anim_enter));
                    menuBar.setVisibility(View.VISIBLE);
                    menuBarNav.setVisibility(View.INVISIBLE);
                    checkPressFab = true;
                } else {
                    menuBar.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_anim_exit));
                    menuBar.setVisibility(View.INVISIBLE);
                    checkPressFab = false;
                }
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPressFab) {
                    menuBarNav.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_anim_enter));
                    menuBarNav.setVisibility(View.VISIBLE);
                    menuBar.setVisibility(View.INVISIBLE);
                    checkPressFab = false;
                }
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intentCamera, REQUEST_CAPTURE);
                } else {
                    ActivityCompat.requestPermissions(GetDataActivity.this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAPTURE);
                }
            }
        });

        btnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    Intent intentFolder = new Intent(Intent.ACTION_PICK);
                    intentFolder.setType("image/*");
                    startActivityForResult(intentFolder, REQUEST_FOLDER);
                } else {
                    ActivityCompat.requestPermissions(GetDataActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_FOLDER);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertImageToArray();
                SimpleDateFormat formatDate = new SimpleDateFormat("EEE, MMM d, yyyy");
                String date = formatDate.format(calendar.getTime()).toString();

                SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a");
                String time = formatTime.format(calendar.getTime());

                if (checkPressEditFab && checkPhotoCapture == false && checkPhotoFolder == false) {

                    database.queryData("UPDATE " + TABLE_NAME + " SET " +
                                       COL_TITLE + " = '"+ edtTitle.getText().toString() +"', " +
                                       COL_CONTENT + " = '"+ edtContent.getText().toString() +"', " +
                                       COL_DATE + " = '"+ date +"', " +
                                       COL_TIME + " = '"+ time +"' WHERE id = '"+ idData +"'");

                    checkPressEditFab = false;
                    Toast.makeText(GetDataActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    edtTitle.setText("");
                    edtContent.setText("");
                    image.setImageResource(R.drawable.icon_photo);
                    startActivity(new Intent(getApplicationContext(), UserDataListActivity.class));

                } else if (checkPressEditFab && (checkPhotoCapture == true || checkPhotoFolder == true)) {
                    database.queryData(DELETE_DATA);
                    getLocation();

                    database.insertData(photoConverted,
                                        edtTitle.getText().toString(),
                                        edtContent.getText().toString(),
                                        date,
                                        time,
                                        latitude,
                                        longitude);

                    Toast.makeText(GetDataActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    edtTitle.setText("");
                    edtContent.setText("");
                    image.setImageResource(R.drawable.icon_photo);
                    checkPressEditFab = false;
                    startActivity(new Intent(getApplicationContext(), UserDataListActivity.class));

                } else {
                    if (checkPhotoCapture || checkPhotoFolder) {
                        getLocation();
                        database.insertData(photoConverted,
                                            edtTitle.getText().toString(),
                                            edtContent.getText().toString(),
                                            date,
                                            time,
                                            latitude,
                                            longitude);

                        Toast.makeText(GetDataActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        edtTitle.setText("");
                        edtContent.setText("");
                        image.setImageResource(R.drawable.icon_photo);
                        startActivity(new Intent(getApplicationContext(), UserDataListActivity.class));
                    } else {
                        Toast.makeText(GetDataActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            } else {
                latitude = 0;
                longitude = 0;
            }
        }
    }

    private void dialogOpenGPS() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Location");
        dialog.setMessage("Would you like to update your current location?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAPTURE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intentCamera, REQUEST_CAPTURE);
                }
                break;

            case REQUEST_FOLDER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intentFolder = new Intent(Intent.ACTION_PICK);
                    intentFolder.setType("image/*");
                    startActivityForResult(intentFolder, REQUEST_FOLDER);
                }
                break;

            case REQUEST_LOCATION:
                getLocation();
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bitmap bitmapCapture = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bitmapCapture);
            checkPhotoCapture = true;
        } else {
            checkPhotoCapture = false;
        }
        if (requestCode == REQUEST_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri imageLink = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageLink);
                Bitmap bitmapFolder = BitmapFactory.decodeStream(inputStream);
                image.setImageBitmap(bitmapFolder);
                checkPhotoFolder = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            checkPhotoFolder = false;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void convertImageToArray() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        photoConverted = byteArrayOutputStream.toByteArray();
    }

    private void initView() {
        edtContent = (EditText) findViewById(R.id.edt_content);
        edtTitle = (EditText) findViewById(R.id.edt_title);
        image = (ImageView) findViewById(R.id.image_capture);
        btnService = (ImageButton) findViewById(R.id.btn_service);
        btnCamera = (ImageButton) findViewById(R.id.btn_camera);
        btnFolder = (ImageButton) findViewById(R.id.btn_folder);
        btnSave = (ImageButton) findViewById(R.id.btn_save);
        btnMenu = (ImageButton) findViewById(R.id.btn_menu_nav);
        btnHome = (ImageButton) findViewById(R.id.btn_home_2);
        btnPhoto = (ImageButton) findViewById(R.id.btn_all_photos_2);
        btnAlbum = (ImageButton) findViewById(R.id.btn_album_2);
        menuBar = (LinearLayout) findViewById(R.id.menu_bar);
        menuBarNav = (LinearLayout) findViewById(R.id.menu_bar_nav);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PhotosActivity.class));
            }
        });

        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserDataListActivity.class));
            }
        });

    }

    private void supportView() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        float textSize = height/60;
        edtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        edtContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        LinearLayout.LayoutParams layoutMenuBar = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 8);
        int marginVer = height/115;
        int marginHor = width/75;
        layoutMenuBar.setMargins(marginHor, marginVer, marginHor, marginVer);
        menuBar.setLayoutParams(layoutMenuBar);

        LinearLayout.LayoutParams layoutMenuBarNav = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 6);
        layoutMenuBarNav.setMargins(marginHor, marginVer, marginHor, marginVer);
        menuBarNav.setLayoutParams(layoutMenuBarNav);

        int fabHeight = height/13;
        int fabWidth = width/8;
        LinearLayout.LayoutParams layoutFabService = new LinearLayout.LayoutParams(fabWidth, fabHeight);
        btnService.setLayoutParams(layoutFabService);
        btnCamera.setLayoutParams(layoutFabService);
        btnFolder.setLayoutParams(layoutFabService);
        btnSave.setLayoutParams(layoutFabService);
        btnMenu.setLayoutParams(layoutFabService);
        btnHome.setLayoutParams(layoutFabService);
        btnPhoto.setLayoutParams(layoutFabService);
        btnAlbum.setLayoutParams(layoutFabService);

        int paddingHor = width/150;
        int paddingVer = height/230;
        btnService.setPadding(paddingHor, paddingVer, paddingHor, paddingVer);
        btnCamera.setPadding(paddingHor, paddingVer, paddingHor, paddingVer);
        btnFolder.setPadding(paddingHor, paddingVer, paddingHor, paddingVer);
        btnSave.setPadding(paddingHor, paddingVer, paddingHor, paddingVer);
        btnAlbum.setPadding(paddingHor, paddingVer, paddingHor, paddingVer);
        btnHome.setPadding(paddingHor, paddingVer, paddingHor, paddingVer);
        btnPhoto.setPadding(paddingHor, paddingVer, paddingHor, paddingVer);
    }
}
