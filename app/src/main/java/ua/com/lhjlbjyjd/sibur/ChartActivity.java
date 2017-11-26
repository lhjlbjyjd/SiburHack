package ua.com.lhjlbjyjd.sibur;

import android.graphics.Color;
import android.nfc.FormatException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.Locale;

public class ChartActivity extends AppCompatActivity {
    Task currentTask;
    private Goal[] goals;
    private String pattern = "HH:mm:ss dd-MM-yyyy";
    private String taskTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Task currentTask = (Task)getIntent().getSerializableExtra("Task");
        taskTitle = currentTask.getName();
        goals = currentTask.getGoals();

        Date[] timesOfGoalBegin = new Date[goals.length], timesOfGoalEnd = new Date[goals.length];

        for(int i = 0; i < goals.length; i++){
            timesOfGoalBegin[i] = goals[i].getBeginDate();
            timesOfGoalEnd[i] = goals[i].getEndDate();
        }

        LineChart chart = (LineChart) findViewById(R.id.chart);

        boolean goalsFinished = true;
        List<Entry> entries = new ArrayList<Entry>();
        int count = 0;
        for(Goal gol : goals) {
            if(gol.getState() == false) {
                goalsFinished = false;
                break;
            }
            count++;
        }

        int counts = count;
        Date lastDate;

        if(goalsFinished)
            lastDate = timesOfGoalEnd[count - 1];
        else
            lastDate = timesOfGoalBegin[count];

        for(int i = 0; i < count; i++)
            entries.add(new Entry(convertTimeToFloat(timesOfGoalBegin[i]),convertGoalToPer(i, counts)));

            entries.add(new Entry(convertTimeToFloat(lastDate),convertGoalToPer(count, count)));

        LineDataSet dataSet = new LineDataSet(entries, taskTitle);
        // lol kek some color set
        dataSet.setColor(Color.parseColor("#FF0000"));
        dataSet.setValueTextColor(7);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();

        // work with axises
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(convertTimeToFloat(timesOfGoalBegin[0]));
        chart.setContentDescription("LOL KEK");
        xAxis.setAxisMaximum(convertTimeToFloat(lastDate));

        xAxis.setValueFormatter(new XAxisValueFormatter());

        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        leftAxis = chart.getAxis(YAxis.AxisDependency.LEFT);
        dataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

        leftAxis.setDrawLabels(false);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);
        chart.getAxisRight().setEnabled(false);
    }

    float convertTimeToFloat(Date newDate)
    {
        long mili = newDate.getTime();
        return mili;
    }

    // размер одной сотой графика
    private int sizeForOne = 1;
    float convertGoalToPer(int i, int numberOfGoals) {
        return (i * 100.0f / numberOfGoals * sizeForOne);
    }

    public class XAxisValueFormatter implements IAxisValueFormatter {

        String newPattern = "HH:mm";
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
          String time;
          Date newDate = new Date((long) value);
          DateFormat df = new SimpleDateFormat(newPattern);
          time = df.format(newDate);
          return time;
        }
    }

}
