package com.xiaomeiw.resume.miniresume;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaomeiw.resume.miniresume.model.BasicInfo;
import com.xiaomeiw.resume.miniresume.util.ImageUtils;
import com.xiaomeiw.resume.miniresume.util.PermissionUtils;

/**
 * Created by wenxiaomei on 17/4/12.
 */


 /* @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = initializeData();
        if (data != null) {
            setupUIForEdit(data);
        } else {
            setupUIForCreate();
        }
    }

    protected abstract int getLayoutId();
    protected abstract T initializeData();
    protected abstract void setupUIForEdit(@NonNull T data);
    protected abstract void setupUIForCreate();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.ic_save:
                saveAndExit(data);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected abstract void saveAndExit(@NonNull T data); */
public class BasicInfoEditActivity extends AppCompatActivity {
    public static final String KEY_BASIC_INFO = "basic_info";
    private static final int REQ_CODE_PICK_IMAGE = 150;
    private BasicInfo data;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basicinfo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        data = getIntent().getParcelableExtra(KEY_BASIC_INFO);
        if (data != null) {
            setUIForEdit(data);
            Log.i("error", "loading data");
        }
    }

    private void setUIForEdit(BasicInfo data) {
        ((EditText) findViewById(R.id.basic_info_edit_name)).setText(data.name);
        ((EditText) findViewById(R.id.basic_info_edit_email)).setText(data.email);
        if (data.imageUri != null) {
            showImage(data.imageUri);
        }

        findViewById(R.id.basic_info_edit_image_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionUtils.checkPermission(BasicInfoEditActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Log.i("error", "no permit");
                    PermissionUtils.requestReadExternalStoragePermission(BasicInfoEditActivity.this);
                } else {
                    pickImage();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.ic_save:
                saveItem();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void saveItem() {
        if (data == null) {
            data = new BasicInfo();
        }
        data.name = ((EditText) findViewById(R.id.basic_info_edit_name))
                .getText().toString();
        data.email = ((EditText) findViewById(R.id.basic_info_edit_email))
                .getText().toString();
        data.imageUri = (Uri) findViewById(R.id.basic_info_edit_image).getTag();

        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_BASIC_INFO, data);
        setResult(Activity.RESULT_OK, resultIntent);
    }

    private void showImage(Uri imageUri) {
        ImageView imageView = (ImageView) findViewById(R.id.basic_info_edit_image);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        imageView.setTag(imageUri);
        ImageUtils.loadImage(this, imageUri, imageView);
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), REQ_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                showImage(imageUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.REQ_CODE_WRITE_EXTERNAL_STORAGE
                && grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        }
    }
}
