package ua.com.lhjlbjyjd.sibur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;

public class ChartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        LineChart chart = (LineChart) findViewById(R.id.chart);

        Date jud = null;
        try {
            jud = new SimpleDateFormat("yyyy-MM-dd-hh HH:mm:ss").parse("2014-02-28 05:30:14");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String month =
                DateFormat.getDateInstance(SimpleDateFormat.LONG, new Locale("ru")).format(jud);
        System.out.println(month);
    }
}
