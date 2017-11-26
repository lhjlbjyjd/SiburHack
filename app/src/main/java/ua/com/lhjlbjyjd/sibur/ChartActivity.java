package ua.com.lhjlbjyjd.sibur;

import android.graphics.Color;
import android.nfc.FormatException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
    private long minValue;
    private Task[] tasks;
    Date periodBegin, periodEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        // случай 1: получено задание, по целям которого нужно построить график
        currentTask = ((MyApp) getApplicationContext()).getTask(getIntent().getIntExtra("Task", 0));
        graphGoal();
        // случай 2: получен массив заданий и границы периода

    }

    void graphTask() {
        taskTitle = "Выполненные задания за период";
        List <Task> fulTasks = new ArrayList<Task>();

        List<Entry> entriesBegin = new ArrayList<Entry>();
        List<Entry> entriesEnd = new ArrayList<Entry>();
        int count = 0;

        for(Task t : tasks) {
            if(t.isFulfilled() && (t.getTaskBegin().getTime() >= periodBegin.getTime())
                    && (t.getTaskEnd().getTime() <= periodEnd.getTime())) {
                fulTasks.add(t);
                count++;
            }
        }

        LineChart chart = (LineChart) findViewById(R.id.chart);
        minValue = fulTasks.get(0).getTaskBegin().getTime();
        for(int i = 0; i < count; i++) {
            Task t = fulTasks.get(i);
            entriesBegin.add(new Entry(convertTimeToFloat(t.getTaskBegin()), convertGoalToPer(i + 1, count)));
            entriesEnd.add(new Entry(convertTimeToFloat(t.getTaskEnd()), convertGoalToPer(i + 1, count)));
        }

        LineDataSet dataSetBegin = new LineDataSet(entriesBegin, taskTitle);
        LineDataSet dataSetEnd = new LineDataSet(entriesEnd, taskTitle);
        // lol kek some color set
        dataSetBegin.setColor(Color.parseColor("#FF0000"));
        dataSetBegin.setValueTextColor(Color.parseColor("#000000"));
        dataSetBegin.setCircleRadius(6f);
        dataSetBegin.setCircleColor(Color.BLUE);
        dataSetBegin.setLineWidth(5f);

        dataSetEnd.setColor(Color.parseColor("#FF0000"));
        dataSetEnd.setValueTextColor(Color.parseColor("#000000"));
        dataSetEnd.setCircleRadius(6f);
        dataSetEnd.setCircleColor(Color.GREEN);
        dataSetEnd.setLineWidth(5f);

        LineData lineData = new LineData(dataSetBegin, dataSetEnd);
        chart.setData(lineData);
        chart.invalidate();

        // work with axises
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(convertTimeToFloat(periodBegin));
        xAxis.setAxisMaximum(convertTimeToFloat(periodEnd));

        xAxis.setValueFormatter(new XAxisValueFormatter());

        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        leftAxis = chart.getAxis(YAxis.AxisDependency.LEFT);

        dataSetBegin.setAxisDependency(YAxis.AxisDependency.RIGHT);
        dataSetEnd.setAxisDependency(YAxis.AxisDependency.RIGHT);

        leftAxis.setDrawLabels(false);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);
        chart.getAxisRight().setEnabled(false);

        chart.invalidate();
    }

    void graphGoal() {
        taskTitle = currentTask.getName();
        goals = currentTask.getGoals();

        Date[] timesOfGoalBegin = new Date[goals.length], timesOfGoalEnd = new Date[goals.length];
        for(int i = 0; i < goals.length; i++){
            timesOfGoalBegin[i] = goals[i].getBeginDate();
            timesOfGoalEnd[i] = goals[i].getEndDate();
        }
        minValue = timesOfGoalBegin[0].getTime();
        LineChart chart = (LineChart) findViewById(R.id.chart);

        boolean goalsFinished = true;
        List<Entry> entries = new ArrayList<Entry>();
        int count = 0;
        for(Goal gol : goals) {
            count++;
            if(gol.getState() == false) {
                goalsFinished = false;
                break;
            }
        }

        int counts = count;
        Date lastDate;

        lastDate = timesOfGoalEnd[count - 1];

        for(int i = 0; i < count; i++)
            entries.add(new Entry(convertTimeToFloat(timesOfGoalBegin[i]),convertGoalToPer(i, counts)));

        entries.add(new Entry(convertTimeToFloat(lastDate),convertGoalToPer(count, count)));

        LineDataSet dataSet = new LineDataSet(entries, taskTitle);
        // lol kek some color set
        dataSet.setColor(Color.parseColor("#FF0000"));
        dataSet.setValueTextColor(Color.parseColor("#000000"));
        dataSet.setCircleRadius(6f);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setLineWidth(5f);

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

        chart.invalidate();
    }

    float convertTimeToFloat(Date newDate)
    {
        long mili = newDate.getTime() - minValue;
        return mili;
    }

    // размер одной сотой графика
    private int sizeForOne = 1;
    float convertGoalToPer(int i, int numberOfGoals) {
        return (i * 100.0f / numberOfGoals * sizeForOne);
    }

    public class XAxisValueFormatter implements IAxisValueFormatter {

        String newPattern = "HH:mm:ss";
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
          String time;
          Date newDate = new Date(((long) value) + minValue);
          DateFormat df = new SimpleDateFormat(newPattern);
          time = df.format(newDate);
          return time;
        }
    }

}
