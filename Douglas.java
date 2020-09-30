import java.util.ArrayList;
import java.util.List;

class Douglas{
 //arraylist for storing
 public List<Point> points = new ArrayList<Point>();
 //define threshold value
 private static final double D = 1;
 
 public Douglas(List<Point> source){
  points = source;
 }
 
 public void compress(Point from, Point to){
  boolean activeCompress = false;
  double A = Math.sqrt(Math.pow((from.getXValue()-to.getXValue()),2) + Math.pow((from.getYValue()-to.getYValue()),2)); //length between start point and end point
  
  double d = 0;
  double dmax = 0;
  int m = points.indexOf(from);
  int n = points.indexOf(to);
  
  if(n == m+1) return;
  Point middle = null;
  List<Double> distance = new ArrayList<Double>();
  
  for(int i = m+1; i< n; i++){
   double B = Math.sqrt(Math.pow((from.getXValue()-points.get(i).getXValue()),2) + Math.pow((from.getYValue()-points.get(i).getYValue()),2));
   double C = Math.sqrt(Math.pow((to.getXValue()-points.get(i).getXValue()),2) + Math.pow((to.getYValue()-points.get(i).getYValue()),2));
   if(C+B == A) d =0; //if point is on the distance
   else if(Math.pow(C,2) >= (Math.pow(A,2) +Math.pow(B,2))) d = B; 
   else if(Math.pow(B,2) >= (Math.pow(A,2) +Math.pow(C,2))) d = C;
   else{
    double p =(A+B+C)/2;
    double s = Math.sqrt(p*(p-A)*(p-B)*(p-C));//apply Heron's formula
    d = 2*s/C;
   }
   distance.add(d);
  }
  dmax = distance.get(0);
  for(int j=1; j<distance.size(); j++){
   //updating dmax
   if(distance.get(j)>dmax) dmax = distance.get(j);
   //System.out.println("dmax is "+dmax);
  }
  if(dmax >D) activeCompress = true;
  else activeCompress = false;
  if(!activeCompress){
   for (int i =m+1;i<n;i++) points.get(i).setIndex(-1); //delete point
  }
  else{
   for(int i =m+1;i<n;i++){
    double B = Math.sqrt(Math.pow((from.getXValue()-points.get(i).getXValue()),2) + Math.pow((from.getYValue()-points.get(i).getYValue()),2));
    double C = Math.sqrt(Math.pow((to.getXValue()-points.get(i).getXValue()),2) + Math.pow((to.getYValue()-points.get(i).getYValue()),2));
	if(C+B == A) d =0; //if point is on the distance
    else if(Math.pow(C,2) >= (Math.pow(A,2) +Math.pow(B,2))) d = B; 
    else if(Math.pow(B,2) >= (Math.pow(A,2) +Math.pow(C,2))) d = C;
	else{
		double p =(A+B+C)/2;
		double s = Math.sqrt(p*(p-A)*(p-B)*(p-C));
		d = 2*s/C;
		}
    if(d == dmax){
     middle = points.get(i);
    }
   }
   compress(from, middle);
   compress(middle, to);
  }
  
 }
 
 public static void AnotherCompress(List<Point> source){
  int lastIndex = -1;
  
  for(int i =1; i<source.size() -1; i++){
   if(i==1){
    lastIndex =0;
   }
   else{
    Point currentPoint = source.get(i);
    Point lastEffectivePoint = source.get(lastIndex);
    Point followPoint = source.get(i+1);
   
    double A = Math.sqrt(Math.pow((lastEffectivePoint.getXValue()-followPoint.getXValue()),2) + Math.pow((lastEffectivePoint.getYValue()-followPoint.getYValue()),2));
    double B = Math.sqrt(Math.pow((lastEffectivePoint.getXValue()-currentPoint.getXValue()),2) + Math.pow((lastEffectivePoint.getYValue()-currentPoint.getYValue()),2));
    double C = Math.sqrt(Math.pow((followPoint.getXValue()-currentPoint.getXValue()),2) + Math.pow((followPoint.getYValue()-currentPoint.getYValue()),2));
    double p =(A+B+C)/2;
    double s = Math.sqrt(p*(p-A)*(p-B)*(p-C));
    double d = 2*s/C;
    
    if(d <= D){
     currentPoint.setIndex(-1); //delete point
    }
    else{
     lastIndex =i;
    }
   }
  }
 }
  public static void main(String[] args){
  List<Point> source = new ArrayList<Point>();
  
  source.add(new Point(1,5,1));
  source.add(new Point(2,3,2));
  source.add(new Point(3,6,3));
  source.add(new Point(4,8,4));
  source.add(new Point(5,8,5));
  source.add(new Point(8,13,6));
  source.add(new Point(10,-9,7));
  source.add(new Point(12,11,8));
  source.add(new Point(14,17,9));
  source.add(new Point(17,16,10));
  source.add(new Point(18,16,11));
  source.add(new Point(18,15,12));
  source.add(new Point(19,14.5,13));
  source.add(new Point(20,20,14));
  source.add(new Point(21,10,15));
  source.add(new Point(21,17,16));
  source.add(new Point(22,18,17));
  source.add(new Point(25,19,18));
  source.add(new Point(26,20,19));
  source.add(new Point(27,21,20));
  
  
  Douglas testD = new Douglas(source);
  
  System.out.println("Before DP-compress");
  for(int i=0;i<testD.points.size();i++){
   Point p = testD.points.get(i);
   System.out.print("(" + p.getXValue() +"," + p.getYValue() +") ");
  }
  System.out.println("\nIn total: " + testD.points.size());
  //draw the line between first and last point
  long dpStartTime = System.nanoTime();
  testD.compress(testD.points.get(0), testD.points.get(testD.points.size()-1));
  long dpEndTime = System.nanoTime();
  
  System.out.println("\nAfter DP-compress");
  int count1 =0;
  for(int i=0;i<testD.points.size();i++){
   Point p = testD.points.get(i);
   
   if(p.getIndex() >-1){
    System.out.print("(" + p.getXValue() +"," + p.getYValue() +") ");
	count1 = count1+1;
   }
   
  }
  System.out.println("\nIn total: " + count1);
  

  System.out.println("\nrun time is " +(dpEndTime-dpStartTime));
  System.out.println("\nNow using another type of compression");
  
  long dpStartTime2 = System.nanoTime();
  AnotherCompress(source);
  long dpEndTime2 = System.nanoTime();
  
  System.out.println("After another compress");
  int count2 =0;
  for(int j=0;j<source.size();j++){
   Point q = source.get(j);
   if(q.getIndex() > -1){
    System.out.print("(" + q.getXValue() +"," + q.getYValue() +") ");
	count2 = count2+1;
   }
  }
  System.out.println("\nIn total: " + count2);
  System.out.println("\nrun time is " +(dpEndTime2-dpStartTime2));
  
 }
 
}


//construct point
class Point{
 //x and y axis value
 private double x = 0;
 private double y = 0;
 //the order of point;
 private int index = 0;
 public double getXValue(){return x;}
 public void setXValue(double x){this.x = x;}
 public double getYValue(){return y;}
 public void setYValue(double y){this.y = y;}
 public int getIndex(){return index;}
 public void setIndex(int index){this.index = index;}
 
 public Point(double x, double y, int index){
  this.x = x;
  this.y = y;
  this.index = index;
 }
}