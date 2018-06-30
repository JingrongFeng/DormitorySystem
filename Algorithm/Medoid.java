
import java.util.ArrayList;

public class Medoid{

    private double dimension[]; // 璐ㄧ偣鐨勭淮搴�
    private Cluster cluster; //鎵�灞炵被绨�
    private double etdDisSum;//Medoid鍒版湰绫荤皣涓墍鏈夌殑娆у紡璺濈涔嬪拰


    public Medoid(double dimension[]) {
        this.dimension = dimension;
    }

    public void setCluster(Cluster c) {
        this.cluster = c;
    }

    public double[] getDimensioin() {
        return this.dimension;
    }

    public Cluster getCluster() {
        return this.cluster;
    }

    public void calcMedoid() {// 鍙栦唬浠锋渶灏忕殑鐐�
        calcEtdDisSum();
        double minEucDisSum = this.etdDisSum;
        ArrayList<DataPoint> dps = this.cluster.getDataPoints();
        for (int i = 0; i < dps.size(); i++) {
            double tempeucDisSum = dps.get(i).calEuclideanDistanceSum();
            if (tempeucDisSum < minEucDisSum) {
                dimension = dps.get(i).getDimensioin();
                minEucDisSum=tempeucDisSum;
            }
        }
    }

    // 璁＄畻璇edoid鍒板悓绫荤皣鎵�鏈夋牱鏈偣鐨勬鏂窛绂诲拰
    private void calcEtdDisSum() {
        double sum=0.0;
        Cluster cluster=this.getCluster();
        ArrayList<DataPoint> dataPoints=cluster.getDataPoints();

        for(int i=0;i<dataPoints.size();i++){
            double[] dims=dataPoints.get(i).getDimensioin();
            for(int j=0;j<dims.length;j++){
                 double temp=Math.abs(dims[j]-this.dimension[j]);
                 sum=sum+temp;
            }
        }
        etdDisSum= sum;
    }
}