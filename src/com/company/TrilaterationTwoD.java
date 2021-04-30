package com.company;

import java.util.*;
//Програмата како влезни аргументи треба да ги има:
//
//N - број на јазли во мрежата
//
//L – должина на областа каде е дистрибуирана мрежата
//
//R - радио опсег
//
//r – шум на сигналот
//
//f – фракција (процент) на anchor јазли

public class TrilaterationTwoD{

    static class Point{
        private int id;
        public double x;
        public double y;
        public boolean isAnchor;
        private int cost;


        Point(double x, double y,boolean isAnchor){
            this.x=x;
            this.y=y;
            this.isAnchor=isAnchor;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public boolean isAnchor() {
            return isAnchor;
        }

        public void setAnchor(boolean anchor) {
            isAnchor = anchor;
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

    }


    public static void main(String[] args) {

        Scanner scanner=new Scanner(System.in);
        Random random=new Random();

        System.out.println("број на јазли во мрежата: ");
        int N = scanner.nextInt();

        System.out.println("должина на областа каде е дистрибуирана мрежата: ");
        int L = scanner.nextInt();

        System.out.println("радио опсег");
        int R = scanner.nextInt();

        System.out.println("шум на сигналот: ");
        int Noise = scanner.nextInt();

        System.out.println("фракција (процент) на anchor јазли: ");
        int procent=scanner.nextInt();

        int anchor=(N*procent)/100;
        if(anchor<3) anchor=3;

        double d1;
        double d2;
        double d3;


        double sum=0;
        double avg=0;


        ArrayList<Point> anchors=new ArrayList<>();
        ArrayList<Point> jazel=new ArrayList<>();
        ArrayList<Point> tmpJazel=new ArrayList<>();
        ArrayList<Point> anchorJazli=new ArrayList<>();
        ArrayList<Integer> distances=new ArrayList<>();
        ArrayList<Integer> sosedi=new ArrayList<>();

        int num=N-anchor;

        int count=0;


        for(int i=0;i<num;i++){

            double x=random.nextInt(L);
            double y=random.nextInt(L);
            Point p=new Point(x,y,false);
            jazel.add(p);
            tmpJazel.add(p);
        }
        for(int i=0;i<anchor;i++){

            int x=random.nextInt(L);
            int y=random.nextInt(L);
            Point p=new Point(x,y,true);
            anchors.add(p);
            jazel.add(p);
        }


        double []err=new double[jazel.size()];
        boolean f=false;

        System.out.println(err.length);

        while (jazel.size()!=anchors.size()){
            for(int i=0;i<jazel.size();i++){
                distances.clear();
                count=0;
                if(jazel.get(i).isAnchor==false){
                    for(int j=0;j<anchors.size();j++){
                        double d=Distance(anchors.get(j).x,anchors.get(j).y,jazel.get(i).x,jazel.get(i).y);
                        if(d<R) count++;
                        double shum=d*Noise/100;
                        d+=Noise;
                        distances.add((int)d);
                    }

                    int []array=new int[10];
                    array=Min(distances);
                    d1=array[0];
                    int poz1=array[1];
                    double x1=anchors.get(poz1).x;
                    double y1=anchors.get(poz1).y;

                    d2=array[2];
                    int poz2=array[3];
                    double x2=anchors.get(poz2).x;
                    double y2=anchors.get(poz2).y;

                    d3=array[4];
                    int poz3=array[5];
                    double x3=anchors.get(poz3).x;
                    double y3=anchors.get(poz3).y;

                    double d=Distance(jazel.get(i).x,jazel.get(i).y,0,0);

                    if(d1<=R && d2<=R && d3<=R){
                        double x=GetX(x1,y1,x2,y2,d1,d2,d);
                        double y=GetY(x1,y1,x2,y2,d1,d2,d);
                        Point p=new Point(x,y,true);
                        err[i]=Distance(x,y,jazel.get(i).x,jazel.get(i).y);
                        anchors.add(p);
                        anchorJazli.add(p);
                        jazel.get(i).isAnchor=true;
                        count++;
                    }

                }

                sosedi.add(count);
            }

            if(JazliNajdeni(jazel)==true){
                break;
            }
        }

        System.out.println("\t" + "Found: ");
        for(int i=0;i<tmpJazel.size();i++){
            System.out.println("X: "+String.format("%.1f", tmpJazel.get(i).x)+
                    "\t"+"Y: "+String.format("%.1f", tmpJazel.get(i).y)+
                    "\t"+"X: "+String.format("%.1f", anchorJazli.get(i).x)+
                    "\t"+"Y: "+String.format("%.1f", anchorJazli.get(i).y));

        }

        System.out.println("original anchor jazli: " + jazel.size());
        System.out.println("najdeni  anchor jazli:" + anchors.size());

        for(int i=0;i<err.length;i++){
            sum+=err[i];
        }

        avg=sum/(err.length);
        System.out.println("Greska na anchor jazlite: " + String.format("%.1f", avg));
        double percentage=avg*100/R;
        System.out.println("Greskata na rang " + String.format("%.1f", percentage));

        double []error=new double[tmpJazel.size()];
        sum=0;

        for(int i=0;i<tmpJazel.size();i++){
            double d=Distance(tmpJazel.get(i).x,tmpJazel.get(i).y,anchorJazli.get(i).x,anchorJazli.get(i).y);
            error[i]=d;
            sum+=error[i];
        }

        avg=sum/(error.length);
        System.out.println("Greska ba2 anchor jazlite: " + String.format("%.1f", avg));
        percentage=avg*100/R;
        System.out.println("Greskata na rang" + String.format("%.1f", percentage));

        int suma=0;
        for(int i=0;i<sosedi.size();i++){
            suma+=sosedi.get(i);
        }
        double averageSosed=suma/sosedi.size();
        System.out.println("Broj na sosedi: " + averageSosed);

    }


    public static double Distance(double x1, double y1, double x2, double y2){
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }

    public static double GetX(double x1, double y1, double x2, double y2, double d1, double d2, double d3){
        return (y1*(x2*x2+y2*y2-d2*d2+d3*d3)-y2*(x1*x1+y1*y1-d1*d1+d3*d3))/(2*(x2*y1-x1*y2));

    }

    public static double GetY(double x1, double y1, double x2, double y2, double d1, double d2, double d3){

        return (x1*(x2*x2+y2*y2-d2*d2+d3*d3)-x2*(x1*x1+y1*y1-d1*d1+d3*d3))/(2*(x1*y2-x2*y1));

    }

    public static int[] Min(ArrayList<Integer> list){
        int current=Integer.MAX_VALUE;

        int poz=0;
        int []array=new int[6];

        for(int i=0;i<list.size();i++){
            if(list.get(i)<current){
                current=list.get(i);
                poz=i;
            }
        }
        array[0]=current;
        array[1]=poz;
        list.set(poz, 1000000);
        current=Integer.MAX_VALUE;

        for(int i=0;i<list.size();i++){
            if(list.get(i)<current){
                current=list.get(i);
                poz=i;
            }
        }

        array[2]=current;
        array[3]=poz;
        list.set(poz, 100000);
        current=Integer.MAX_VALUE;

        for(int i=0;i<list.size();i++){
            if(list.get(i)<current){
                current=list.get(i);
                poz=i;
            }
        }

        array[4]=current;
        array[5]=poz;

        return array;

    }

    public static boolean JazliNajdeni(ArrayList<Point> list){

        boolean flag=true;

        for(int i=0;i<list.size();i++){
            if(list.get(i).isAnchor==false){
                flag=false;
                break;
            }
        }
        return flag;
    }


}