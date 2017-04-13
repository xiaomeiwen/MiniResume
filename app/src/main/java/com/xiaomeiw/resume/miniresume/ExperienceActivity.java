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

import com.xiaomeiw.resume.miniresume.model.Experience;
import com.xiaomeiw.resume.miniresume.util.DateUtils;

import java.util.Arrays;

/**
 * Created by wenxiaomei on 17/4/12.
 */
public class ExperienceActivity extends AppCompatActivity {

    public static final String KEY_EXPERIENCE = "experience";
    public static final String KEY_EXPERIENCE_ID = "experience_id";
    private Experience data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = getIntent().getParcelableExtra(KEY_EXPERIENCE);
        if (data != null) {
            setupUI();
        } else {
            setupUIForCreate();
        }

        setTitle(data == null ? "New experience": "Edit experience");
    }

    private void setupUIForCreate() {
        findViewById(R.id.experience_edit_delete).setVisibility(View.GONE);
    }

    private void setupUI() {
        ((EditText) findViewById(R.id.experience_edit_company)).setText(data.company);
        ((EditText) findViewById(R.id.experience_edit_title)).setText(data.title);
        ((EditText) findViewById(R.id.experience_edit_start_date))
                .setText(DateUtils.dateToString(data.startDate));
        ((EditText) findViewById(R.id.experience_edit_end_date))
                .setText(DateUtils.dateToString(data.endDate));
        ((EditText) findViewById(R.id.experience_edit_what_did_you_do))
                .setText(TextUtils.join("\n",data.experiences));

        findViewById(R.id.experience_edit_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_EXPERIENCE_ID, data.id);
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
            data = new Experience();
        }
        data.company = ((EditText) findViewById(R.id.experience_edit_company)).getText().toString();
        data.title = ((EditText) findViewById(R.id.experience_edit_title)).getText().toString();
        data.startDate = DateUtils.stringToDate(
                ((EditText) findViewById(R.id.experience_edit_start_date)).getText().toString());
        data.endDate = DateUtils.stringToDate(
                ((TextView) findViewById(R.id.experience_edit_end_date)).getText().toString());
        data.experiences = Arrays.asList(TextUtils.split(
                ((TextView) findViewById(R.id.experience_edit_what_did_you_do)).getText().toString(), "\n"));

        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_EXPERIENCE,data);
        setResult(Activity.RESULT_OK, resultIntent);
    }
}

