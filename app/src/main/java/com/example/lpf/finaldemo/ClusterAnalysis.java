package com.example.lpf.finaldemo;

import java.util.ArrayList;

public class ClusterAnalysis {

    private Cluster[] clusters;// 鎵�鏈夌被绨�
    private int miter;// 杩唬娆℃暟
    private ArrayList<DataPoint> dataPoints = new ArrayList<DataPoint>();// 鎵�鏈夋牱鏈偣
    private int dimNum;//缁村害

    public ClusterAnalysis(int k, int iter, ArrayList<DataPoint> dataPoints,int dimNum) {
        clusters = new Cluster[k];// 绫荤皣绉嶇被鏁�
        for (int i = 0; i < k; i++) {
            clusters[i] = new Cluster("Cluster:" + i);
        }
        this.miter = iter;
        this.dataPoints = dataPoints;
        this.dimNum=dimNum;
    }

    public int getIterations() {
        return miter;
    }

    public ArrayList<DataPoint>[] getClusterOutput() {
        ArrayList<DataPoint> v[] = new ArrayList[clusters.length];
        for (int i = 0; i < clusters.length; i++) {
            v[i] = clusters[i].getDataPoints();
        }
        return v;
    }

   
    public void startAnalysis(double[][] medoids) {

        setInitialMedoids(medoids);

        double[][] newMedoids=medoids;
        double[][] oldMedoids=new double[medoids.length][this.dimNum];

        while(!isEqual(oldMedoids,newMedoids)){
            for(int m = 0; m < clusters.length; m++){//姣忔杩唬寮�濮嬫儏鍐靛悇绫荤皣鐨勭偣
                clusters[m].getDataPoints().clear();
            }
            for (int j = 0; j < dataPoints.size(); j++) {
                int clusterIndex=0;
                double minDistance=Double.MAX_VALUE;

                for (int k = 0; k < clusters.length; k++) {//鍒ゆ柇鏍锋湰鐐瑰睘浜庡摢涓被绨�
                    double eucDistance=dataPoints.get(j).testEuclideanDistance(clusters[k].getMedoid());
                    if(eucDistance<minDistance){
                        minDistance=eucDistance;
                        clusterIndex=k;
                    }
                }

               //灏嗚鏍锋湰鐐规坊鍔犲埌璇ョ被绨�
                clusters[clusterIndex].addDataPoint(dataPoints.get(j));

            }

            for(int m = 0; m < clusters.length; m++){
                clusters[m].getMedoid().calcMedoid();//閲嶆柊璁＄畻鍚勭被绨囩殑璐ㄧ偣
            }

            for(int i=0;i<medoids.length;i++){
                for(int j=0;j<this.dimNum;j++){
                    oldMedoids[i][j]=newMedoids[i][j];
                }
            }


            for(int n=0;n<clusters.length;n++){
                newMedoids[n]=clusters[n].getMedoid().getDimensioin();
            }

            this.miter++;
        }


    }

    private void setInitialMedoids(double[][] medoids) {
        for (int n = 0; n < clusters.length; n++) {
            Medoid medoid = new Medoid(medoids[n]);
            clusters[n].setMedoid(medoid);
            medoid.setCluster(clusters[n]);
        }
    }

   
    private boolean isEqual(double[][] oldMedoids,double[][] newMedoids){
        boolean flag=false;
        for(int i=0;i<oldMedoids.length;i++){
            for(int j=0;j<oldMedoids[i].length;j++){
                if(oldMedoids[i][j]!=newMedoids[i][j]){
                    return flag;
                }
            }
        }
        flag=true;
        return flag;
    }
}