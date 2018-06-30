import java.beans.Statement;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

public class TestMain
{
  //public static Vector<String> indata = new Vector();
  public static ArrayList<DataPoint> data = new ArrayList();
  private static int k=5;
  
  public static ArrayList[] Distribute(Vector<String> indata) throws SQLException
  {
	ArrayList<DataPoint> dataPoints = new ArrayList();
    //loadData("D:\\PY\\JAVA\\kmedoids\\src\\rawdata.csv");
    pretreatment(indata);
   
    int[] index=randomCommon(0,indata.size(),k);//初始随机选取质心
    ClusterAnalysis ca = new ClusterAnalysis(k, 0, data, 12);
    double[][] cen=new double[k][12];
    for(int i=0;i<k;i++) {
    	double[] choice=data.get(index[i]).getDimensioin();
    	cen[i]=choice;
    }
    
    ca.startAnalysis(cen);
        
    
    ArrayList[] v = ca.getClusterOutput();
    return v;
    /*for (int ii = 0; ii < v.length; ii++)
    {
      ArrayList tempV = v[ii];
      System.out.println("-----------Cluster" + ii + "---------");
      Iterator iter = tempV.iterator();
      while (iter.hasNext())
      {
        DataPoint dpTemp = (DataPoint)iter.next();
        System.out.println(dpTemp.getPointName());
      }
    }*/
  }
  
  public static int[] randomCommon(int min, int max, int n){  
	    if (n > (max - min + 1) || max < min) {  
	           return null;  
	       }  
	    int[] result = new int[n];  
	    int count = 0;  
	    while(count < n) {  
	        int num = (int) (Math.random() * (max - min)) + min;  
	        boolean flag = true;  
	        for (int j = 0; j < n; j++) {  
	            if(num == result[j]){  
	                flag = false;  
	                break;  
	            }  
	        }  
	        if(flag){  
	            result[count] = num;  
	            count++;  
	        }  
	    }  
	    return result;  
	}  
  
  /*public static boolean loadData(String url)
  {
	try {    
	     BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(url), "gbk"));  //换成你的文件名   
	     String line = null;    
	     while((line=reader.readLine())!=null){    
	         indata.add(line); 
	     }    
	 } catch (Exception e) {    
	     e.printStackTrace();    
	}
    return false;
  }*/
  
  public static void pretreatment(Vector<String> indata)
  {
    int i = 0;
    while (i < indata.size())
    {
      double[] tem = new double[12];
      for(int j=0;j<11;j++)
    	  tem[j]=0;
      int k=0;
      String t = (String)indata.get(i);
      String[] sourceStrArray = t.split(",");
      for (int j = 0; j < 2; j++) {
        tem[k++] = Integer.parseInt(sourceStrArray[j]);
      }
      for(int j= 2;j<5;j++) {
    	  int x=Integer.parseInt(sourceStrArray[j]);
    	  if(x!=7) {
    		  tem[x+2]=1;
    	  }
      }
      String plan=sourceStrArray[5];
      if(plan.equals("国内读研  ")) {
    	  tem[9]=1;
    	  tem[10]=0;
    	  tem[11]=0;
      }
      else if(plan.equals("毕业工作  ")) {
    	  tem[9]=0;
    	  tem[10]=1;
    	  tem[11]=0;
      }
      else if(plan.equals("出国读研  ")) {
    	  tem[9]=0;
    	  tem[10]=0;
    	  tem[11]=1;
      }
      /*for(int j=0;j<12;j++) {
    	  System.out.print(tem[j]+" ");
      }
      System.out.println(sourceStrArray[6]);*/
      data.add(new DataPoint(tem,sourceStrArray[6]));
      i++;
    }
  }  
}
