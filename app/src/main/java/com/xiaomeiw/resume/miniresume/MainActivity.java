package com.xiaomeiw.resume.miniresume;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.xiaomeiw.resume.miniresume.model.BasicInfo;
import com.xiaomeiw.resume.miniresume.model.Education;
import com.xiaomeiw.resume.miniresume.model.Experience;
import com.xiaomeiw.resume.miniresume.model.Project;
import com.xiaomeiw.resume.miniresume.util.DateUtils;
import com.xiaomeiw.resume.miniresume.util.ImageUtils;
import com.xiaomeiw.resume.miniresume.util.ModelUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQ_CODE_EDIT_EDUCATION = 100;
    private static final int REQ_CODE_EDIT_EXPERIENCE = 101;
    private static final int REQ_CODE_EDIT_PROJECT = 102;
    private static final int REQ_CODE_EDIT_BASIC_INFO = 103;

    private static final String MODEL_EDUCATIONS = "educations";
    private static final String MODEL_EXPERIENCES = "experiences";
    private static final String MODEL_PROJECTS = "projects";
    private static final String MODEL_BASIC_INFO = "basic_info";


    private List<Education> educations;
    private List<Experience> experiences;
    private List<Project> projects;
    private BasicInfo basicInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        setupUI();
    }

    private void loadData() {
        BasicInfo savedBasicInfo = ModelUtils.read(this,
                MODEL_BASIC_INFO,
                new TypeToken<BasicInfo>(){});
        basicInfo = savedBasicInfo == null ? new BasicInfo() : savedBasicInfo;

        List<Education> savedEducation = ModelUtils.read(this,
                "educations",
                new TypeToken<List<Education>>(){});
        educations = savedEducation == null ? new ArrayList<Education>() : savedEducation;

        List<Experience> savedExperience = ModelUtils.read(this,
                "experiences",
                new TypeToken<List<Experience>>(){});
        experiences = savedExperience == null ? new ArrayList<Experience>() : savedExperience;

        List<Project> savedProject = ModelUtils.read(this,
                "projects",
                new TypeToken<List<Project>>(){});
        projects = savedProject == null ? new ArrayList<Project>() : savedProject;
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);

  /*      ImageButton edit_basicinfo_btn = (ImageButton) findViewById(R.id.edit_basic_info);
        edit_basicinfo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BasicInfoEditActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_BASIC_INFO);
            }
        });*/

        ImageButton add_education_btn = (ImageButton) findViewById(R.id.add_education_btn);
        add_education_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EducationActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_EDUCATION);
            }
        });

        ImageButton addExperienceBtn = (ImageButton) findViewById(R.id.add_experience_btn);
        addExperienceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExperienceActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_EXPERIENCE);
            }
        });

        ImageButton addProjectBtn = (ImageButton) findViewById(R.id.add_project_btn);
        addProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProjectActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_PROJECT);
            }
        });

        setupBasicInfo();
        setupEducations();
        setupExperiences();
        setupProjects();
    }

    private void setupProjects() {
        LinearLayout linearlayout = (LinearLayout) findViewById(R.id.project_list);
        linearlayout.removeAllViews();
        for(Project project: projects) {
            linearlayout.addView(getProjectLayout(project));
        }
    }

    private void setupBasicInfo() {
        ((TextView) findViewById(R.id.name)).setText(TextUtils.isEmpty(basicInfo.name)
                ? "Your name"
                : basicInfo.name);
        ((TextView) findViewById(R.id.email)).setText(TextUtils.isEmpty(basicInfo.email)
                ? "Your email"
                : basicInfo.email);
        ImageView userPicture = (ImageView) findViewById(R.id.user_picture);
        if (basicInfo.imageUri != null) {
            ImageUtils.loadImage(this, basicInfo.imageUri, userPicture);
        } else {
            userPicture.setImageResource(R.drawable.user_ghost);
        }

        findViewById(R.id.edit_basic_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BasicInfoEditActivity.class);
                intent.putExtra(BasicInfoEditActivity.KEY_BASIC_INFO, basicInfo);
                startActivityForResult(intent, REQ_CODE_EDIT_BASIC_INFO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_EDIT_EDUCATION && resultCode == Activity.RESULT_OK) {
            String educationId = data.getStringExtra(EducationActivity.KEY_EDUCATION_ID);
            if (educationId != null) {
                deleteEducation(educationId);
            } else {
                Education education = data.getParcelableExtra(EducationActivity.KEY_EDUCATION);
                updateEducation(education);
            }
        }
        if (requestCode == REQ_CODE_EDIT_EXPERIENCE && resultCode == Activity.RESULT_OK) {
            String experienceId = data.getStringExtra(ExperienceActivity.KEY_EXPERIENCE_ID);
            if (experienceId != null) {
                deleteExperience(experienceId);
            } else {
                Experience experience = data.getParcelableExtra(ExperienceActivity.KEY_EXPERIENCE);
                updateExperience(experience);
            }

        }
        if (requestCode == REQ_CODE_EDIT_PROJECT && resultCode == Activity.RESULT_OK) {
            String projectId = data.getStringExtra(ProjectActivity.KEY_PROJECT_ID);
            if (projectId != null) {
                deleteProject(projectId);
            } else {
                Project project = data.getParcelableExtra(ProjectActivity.KEY_PROJECT);
                updateProject(project);
            }

        }

        if (requestCode == REQ_CODE_EDIT_BASIC_INFO && resultCode == Activity.RESULT_OK) {
            BasicInfo basicInfo = data.getParcelableExtra(BasicInfoEditActivity.KEY_BASIC_INFO);
            updateBasicInfo(basicInfo);
        }
    }

    private void updateBasicInfo(BasicInfo basicInfo) {
        ModelUtils.save(this, MODEL_BASIC_INFO, basicInfo);

        this.basicInfo = basicInfo;
        setupBasicInfo();
    }

    private void deleteProject(String projectId) {
        for (int i = 0; i < projects.size(); ++i) {
            Project e = projects.get(i);
            if (TextUtils.equals(e.id, projectId)) {
                projects.remove(i);
                break;
            }
        }
        ModelUtils.save(this, MODEL_PROJECTS, projects);
        setupProjects();
    }

    private void deleteExperience(String experienceId) {
        for (int i = 0; i < experiences.size(); ++i) {
            Experience e = experiences.get(i);
            if (TextUtils.equals(e.id, experienceId)) {
                experiences.remove(i);
                break;
            }
        }
        ModelUtils.save(this, MODEL_EXPERIENCES, experiences);
        setupExperiences();
    }

    private void updateProject(Project project) {
        Log.i("error", "update" + projects.size());
        boolean found = false;
        for (int i = 0; i < projects.size(); ++i) {
            Project data = projects.get(i);
            if (TextUtils.equals(data.id, project.id)) {
                found = true;
                projects.set(i, project);
                break;
            }
        }
        if (!found) {
            projects.add(project);
            //      Log.i("error", "" + projects.size());
        }
        ModelUtils.save(this, MODEL_PROJECTS, projects);
        setupProjects();
    }

    private void updateExperience(Experience experience) {
        boolean found = false;
        for (int i = 0; i < experiences.size(); ++i) {
            Experience data = experiences.get(i);
            if (TextUtils.equals(data.id, experience.id)) {
                found = true;
                experiences.set(i, experience);
                break;
            }
        }
        if (!found) {
            experiences.add(experience);
        }
        ModelUtils.save(this, MODEL_EXPERIENCES, experiences);
        setupExperiences();
    }

    private void updateEducation(Education education) {
        boolean found = false;
        for(int i = 0; i < educations.size(); ++i) {
            Education data = educations.get(i);
            if (TextUtils.equals(data.id, education.id)) {
                found = true;
                educations.set(i, education);
                break;
            }
        }
        if (!found) {    //found == false
            educations.add(education);
            Log.i("error", Integer.toString(educations.size()));
        }
        ModelUtils.save(this, MODEL_EDUCATIONS, educations);
        setupEducations();
    }

    private void deleteEducation(@NonNull String educationId) {
        for (int i = 0; i < educations.size(); ++i) {
            Education e = educations.get(i);
            if (TextUtils.equals(e.id, educationId)) {
                educations.remove(i);
                break;
            }
        }
        ModelUtils.save(this, MODEL_EDUCATIONS, educations);
        setupEducations();
    }



    private View getProjectLayout(final Project project) {
        View projectView = getLayoutInflater().inflate(R.layout.item_project, null);
        String dateString = DateUtils.dateToString(project.startDate)
                + " ~ " + DateUtils.dateToString(project.endDate);
        ((TextView) projectView.findViewById(R.id.project_name))
                .setText(project.name + " " + "(" + dateString + ")");
        ((TextView) projectView.findViewById(R.id.project_details))
                .setText(formatItems(project.details));

        ImageButton editProjectBtn = (ImageButton) projectView.findViewById(R.id.edit_project_btn);
        editProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProjectActivity.class);
                intent.putExtra(ProjectActivity.KEY_PROJECT, project);
                startActivityForResult(intent, REQ_CODE_EDIT_PROJECT);
            }
        });
        return projectView;
    }

    private void setupExperiences() {
        LinearLayout linearlayout = (LinearLayout) findViewById(R.id.experience_list);
        linearlayout.removeAllViews();
        for(Experience experience: experiences) {
            linearlayout.addView(getExperienceLayout(experience));
        }
    }

    private View getExperienceLayout(final Experience experience) {
        View experienceView = getLayoutInflater().inflate(R.layout.item_experience, null);
        String dateString = DateUtils.dateToString(experience.startDate)
                + " ~ " + DateUtils.dateToString(experience.endDate);
        ((TextView) experienceView.findViewById(R.id.experience_company))
                .setText(experience.company + " " + experience.title + "(" + dateString + ")");
        ((TextView) experienceView.findViewById(R.id.experience_what_did_you_do))
                .setText(formatItems(experience.experiences));

        ImageButton editExperienceBtn = (ImageButton) experienceView.findViewById(R.id.edit_experience_btn);
        editExperienceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ExperienceActivity.class);
                intent.putExtra(ExperienceActivity.KEY_EXPERIENCE, experience);
                startActivityForResult(intent, REQ_CODE_EDIT_EXPERIENCE);
            }
        });
        return experienceView;
    }

    private void setupEducations() {
        LinearLayout linearlayout = (LinearLayout) findViewById(R.id.education_list);
        linearlayout.removeAllViews();
        for(Education education: educations) {
            linearlayout.addView(getEducationLayout(education));
        }
    }

    private View getEducationLayout(final Education education) {
        View educationView = getLayoutInflater().inflate(R.layout.item_education, null);

        String dateString = DateUtils.dateToString(education.startDate)
                + " ~ " + DateUtils.dateToString(education.endDate);
        ((TextView) educationView.findViewById(R.id.education_school))
                .setText(education.school + " " + education.major + " (" + dateString + ")");
        ((TextView) educationView.findViewById(R.id.education_courses))
                .setText(formatItems(education.courses));

        ImageButton editEducationBtn = (ImageButton) educationView.findViewById(R.id.edit_education_btn);
        editEducationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EducationActivity.class);
                intent.putExtra(EducationActivity.KEY_EDUCATION, education);
                startActivityForResult(intent, REQ_CODE_EDIT_EDUCATION);
            }
        });
        return educationView;
    }

    public static String formatItems(@NonNull List<String> items) {
        StringBuilder sb = new StringBuilder();
        for (String item: items) {
            sb.append(' ').append('-').append(' ').append(item).append('\n');
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
