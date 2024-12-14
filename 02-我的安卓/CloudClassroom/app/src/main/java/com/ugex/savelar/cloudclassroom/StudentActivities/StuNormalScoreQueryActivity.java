package com.ugex.savelar.cloudclassroom.StudentActivities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.ugex.savelar.cloudclassroom.Entities.EntityStudent;
import com.ugex.savelar.cloudclassroom.R;
import com.ugex.savelar.cloudclassroom.Tools.UtilHelper;

import java.util.Map;

public class StuNormalScoreQueryActivity extends Activity {
    private EntityStudent student;
    private Map<String,Double> scoreMap;
    private TextView tvNo;
    private TextView tvName;
    private TextView tvSum;
    private TextView tvAvg;
    private TextView tvMax;
    private TextView tvMin;
    private TextView tvHomeworkSum;
    private TextView tvHomeworkAvg;
    private TextView tvHomeworkMax;
    private TextView tvHomeworkMin;
    private TextView tvAccessSum;
    private TextView tvAccessAvg;
    private TextView tvAccessMax;
    private TextView tvAccessMin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_normal_score_query);

        InitActivity();
    }

    private void InitActivity() {
        student=new EntityStudent(getIntent().getStringExtra(UtilHelper.ExtraKey.ACCOUNT));
        student.getDataFromDb(getContentResolver());
        if(UtilHelper.stringIsNullOrEmpty(student.Cpno)==false) {
            scoreMap = EntityStudent.getStudentNormalScore(getContentResolver(), student.Cpno);
        }
        tvNo=(TextView)findViewById(R.id.textViewSelectSno);
        tvName=(TextView)findViewById(R.id.textViewSelectSname);
        tvSum=(TextView)findViewById(R.id.textViewSumScore);
        tvAvg=(TextView)findViewById(R.id.textViewAvgScore);
        tvMax=(TextView)findViewById(R.id.textViewMaxScore);
        tvMin=(TextView)findViewById(R.id.textViewMinScore);
        tvHomeworkSum=(TextView)findViewById(R.id.textViewSumHomeworkScore);
        tvHomeworkAvg=(TextView)findViewById(R.id.textViewAvgHomeworkScore);
        tvHomeworkMax=(TextView)findViewById(R.id.textViewMaxHomeworkScore);
        tvHomeworkMin=(TextView)findViewById(R.id.textViewMinHomeworkScore);
        tvAccessSum=(TextView)findViewById(R.id.textViewSumAccessScore);
        tvAccessAvg=(TextView)findViewById(R.id.textViewAvgAccessScore);
        tvAccessMax=(TextView)findViewById(R.id.textViewMaxAccessScore);
        tvAccessMin=(TextView)findViewById(R.id.textViewMinAccessScore);

        ShowDataToView();

    }
    private void ShowDataToView(){
        tvName.setText(UtilHelper.stringToEmptyWhenNull(student.Cname));
        tvNo.setText(UtilHelper.stringToEmptyWhenNull(student.Cpno));
        tvSum.setText(""+scoreMap.get(EntityStudent.NormalScoreHelper.SCORE_SUM_HOMEWORK_AND_CLASSACCESS));
        tvAvg.setText(""+scoreMap.get(EntityStudent.NormalScoreHelper.SCORE_AVG_HOMEWORK_AND_CLASSACCESS));
        tvMax.setText(""+scoreMap.get(EntityStudent.NormalScoreHelper.SCORE_MAX_HOMEWORK_AND_CLASSACCESS));
        tvMin.setText(""+scoreMap.get(EntityStudent.NormalScoreHelper.SCORE_MIN_HOMEWORK_AND_CLASSACCESS));

        tvHomeworkSum.setText(""+scoreMap.get(EntityStudent.NormalScoreHelper.SCORE_SUM_HOMEWORK));
        tvHomeworkAvg.setText(""+scoreMap.get(EntityStudent.NormalScoreHelper.SCORE_AVG_HOMEWORK));
        tvHomeworkMax.setText(""+scoreMap.get(EntityStudent.NormalScoreHelper.SCORE_MAX_HOMEWORK));
        tvHomeworkMin.setText(""+scoreMap.get(EntityStudent.NormalScoreHelper.SCORE_MIN_HOMEWORK));

        tvAccessSum.setText(""+scoreMap.get(EntityStudent.NormalScoreHelper.SCORE_SUM_CLASSACCESS));
        tvAccessAvg.setText(""+scoreMap.get(EntityStudent.NormalScoreHelper.SCORE_AVG_CLASSACCESS));
        tvAccessMax.setText(""+scoreMap.get(EntityStudent.NormalScoreHelper.SCORE_MAX_CLASSACCESS));
        tvAccessMin.setText(""+scoreMap.get(EntityStudent.NormalScoreHelper.SCORE_MIN_CLASSACCESS));
    }
}
