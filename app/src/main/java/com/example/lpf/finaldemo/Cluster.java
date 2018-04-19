package com.example.lpf.finaldemo;

import java.util.ArrayList;
public class Cluster {
    private String clusterName; // 绫荤皣鍚�
    private Medoid medoid; // 绫荤皣鐨勮川鐐�
    private ArrayList<DataPoint> dataPoints; // 绫荤皣涓悇鏍锋湰鐐�

    public Cluster(String clusterName) {
        this.clusterName = clusterName;
        this.medoid = null; // will be set by calling setCentroid()
        dataPoints = new ArrayList<DataPoint>();
    }

    public void setMedoid(Medoid c) {
        medoid = c;
    }

    public Medoid getMedoid() {
        return medoid;
    }

   
    public void addDataPoint(DataPoint dp) { // called from CAInstance
        dp.setCluster(this);// 鏍囨敞璇ョ被绨囧睘浜庢煇鐐�,璁＄畻娆у紡璺濈
        this.dataPoints.add(dp);
    }

    public void removeDataPoint(DataPoint dp) {
        this.dataPoints.remove(dp);
    }

    public int getNumDataPoints() {
        return this.dataPoints.size();
    }

    public DataPoint getDataPoint(int pos) {
        return (DataPoint) this.dataPoints.get(pos);
    }


    public String getName() {
        return this.clusterName;
    }

    public ArrayList<DataPoint> getDataPoints() {
        return this.dataPoints;
    }
}