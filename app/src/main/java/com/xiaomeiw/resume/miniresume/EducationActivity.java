package com.xiaomeiw.resume.miniresume;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaomeiw.resume.miniresume.model.Education;
import com.xiaomeiw.resume.miniresume.util.DateUtils;

import java.util.Arrays;

/**
 * Created by wenxiaomei on 17/4/12.
 */

public class EducationActivity extends AppCompatActivity {
    public static final String KEY_EDUCATION = "education";
    public static final String KEY_EDUCATION_ID = "education_id";
    //public static final String KEY_EDUCATION_ID = "education_id";
    private Education data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);
        //menu
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = getIntent().getParcelableExtra(KEY_EDUCATION);
        if (data != null) {
            setupUIForEdit();
        } else {
            setupUIForCreate();
        }
        setTitle(data == null? "New education": "edit education");
    }

    private void setupUIForCreate() {
        findViewById(R.id.education_edit_delete).setVisibility(View.GONE);
    }
/*

    private Education initializeData() {
        return getIntent().getParcelableExtra(KEY_EDUCATION);
    }
*/

    private void setupUIForEdit() {
        ((EditText) findViewById(R.id.education_edit_school)).setText(data.school);
        ((EditText) findViewById(R.id.education_edit_major)).setText(data.major);
        ((EditText) findViewById(R.id.education_edit_start_date))
                .setText(DateUtils.dateToString(data.startDate));
        ((EditText) findViewById(R.id.education_edit_end_date))
                .setText(DateUtils.dateToString(data.endDate));
        ((EditText) findViewById(R.id.education_edit_courses))
                .setText(TextUtils.join("\n", data.courses));

        findViewById(R.id.education_edit_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_EDUCATION_ID, data.id);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

    }

 /*   private void setupUIForCreate() {
        findViewById(R.id.education_edit_delete).setVisibility(View.GONE);
    }*/

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
                saveItem();
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveItem() {

        if (data == null) {
            data = new Education();
        }
        data.school = ((EditText) findViewById(R.id.education_edit_school)).getText().toString();
        data.major = ((EditText) findViewById(R.id.education_edit_major)).getText().toString();
        data.startDate = DateUtils.stringToDate(
                ((TextView) findViewById(R.id.education_edit_start_date)).getText().toString());
        data.endDate = DateUtils.stringToDate(
                ((TextView) findViewById(R.id.education_edit_end_date)).getText().toString());
        data.courses = Arrays.asList(TextUtils.split(
                ((EditText) findViewById(R.id.education_edit_courses)).getText().toString(), "\n"));

        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_EDUCATION, data);
        setResult(Activity.RESULT_OK, resultIntent);
    }
}
