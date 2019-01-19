package com.gluonhq.numjava;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class NumJava {

    private static boolean javafxInitialized = false;
    private static boolean javafxInitializing = false;

    public static void main(String[] args) {
        System.out.println("Hello, NumJava");
        Function<Double, Double> f = x -> 2*x;
        plotFunction(f, 10, 30);
    }

    public static void plotFunction(Function<Double, Double> f, Number xStart, Number xEnd) {
        List<Function<Double, Double>> single = new ArrayList<>();
        single.add(f);
        plotFunction(single, xStart, xEnd);
    }

    public static void plotFunction(List<Function<Double, Double>> functions, Number xStart, Number xEnd) {
        enableJavaFX();
        int div = 500;
        double x0 = xStart.doubleValue();
        double x1 = xEnd.doubleValue();
        double step = 1./div* (x1-x0);
        Axis<Number> xAxis = new NumberAxis(x0, x1, .1* (x1-x0));
        Axis<Number> yAxis = new NumberAxis();
        ObservableList<XYChart.Series<Number, Number>> series = FXCollections.observableArrayList();
        LineChart<Number,Number> chart = new LineChart(xAxis, yAxis, series);
        chart.setCreateSymbols(false);
        for (Function<Double, Double> f: functions) {
            XYChart.Series<Number, Number> mainSeries = new XYChart.Series();
            series.add(mainSeries);
            ObservableList<XYChart.Data<Number, Number>> data = FXCollections.observableArrayList();
            mainSeries.setData(data);
            for (double x = x0; x < x1; x= x +step) {
                final Number y = f.apply(x);
                data.add(new XYChart.Data<>(x,y));
            }
        }

        Platform.runLater(() -> {
            Scene scene = new Scene(chart, 640, 480);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        });
    }

    private static void enableJavaFX() {
        if (javafxInitialized) return;
        if (javafxInitializing) {
            System.err.println("JavaFX is being initialized, should work in a few seconds.");
            return;
        }
        javafxInitializing = true;
        Platform.startup(()->{
            javafxInitialized = true;
            javafxInitializing = false;
        });
        Platform.setImplicitExit(false);
    }
}
