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

import com.xiaomeiw.resume.miniresume.model.Project;
import com.xiaomeiw.resume.miniresume.util.DateUtils;

import java.util.Arrays;

/**
 * Created by wenxiaomei on 17/4/12.
 */
public class ProjectActivity extends AppCompatActivity {

    public static final String KEY_PROJECT = "project";
    public static final String KEY_PROJECT_ID = "project_id";

    private Project data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        //menu
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = getIntent().getParcelableExtra(KEY_PROJECT);
        if (data != null) {
            setupUIForEdit();
        } else {
            setupUIForCreate();
        }
        setTitle(data == null? "New project": "edit project");
    }

    private void setupUIForCreate() {
        findViewById(R.id.project_edit_delete).setVisibility(View.GONE);
    }

    private void setupUIForEdit() {
        ((EditText) findViewById(R.id.project_edit_name)).setText(data.name);
        ((EditText) findViewById(R.id.project_edit_start_date))
                .setText(DateUtils.dateToString(data.startDate));
        ((EditText) findViewById(R.id.project_edit_end_date))
                .setText(DateUtils.dateToString(data.endDate));
        ((EditText) findViewById(R.id.project_edit_details))
                .setText(TextUtils.join("\n", data.details));

        findViewById(R.id.project_edit_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_PROJECT_ID, data.id);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

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
            data = new Project();
        }
        data.name = ((EditText) findViewById(R.id.project_edit_name)).getText().toString();
        //data.major = ((EditText) findViewById(R.id.education_edit_major)).getText().toString();
        data.startDate = DateUtils.stringToDate(
                ((TextView) findViewById(R.id.project_edit_start_date)).getText().toString());
        data.endDate = DateUtils.stringToDate(
                ((TextView) findViewById(R.id.project_edit_end_date)).getText().toString());
        data.details = Arrays.asList(TextUtils.split(
                ((EditText) findViewById(R.id.project_edit_details)).getText().toString(), "\n"));

        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_PROJECT, data);
        setResult(Activity.RESULT_OK, resultIntent);
    }
}

