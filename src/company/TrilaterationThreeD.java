package company;

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

public class TrilaterationThreeD {

     static class Point {
        private int id;
        public double x;
        public double y;
        public double z;
        public double r;
        public boolean isAnchor;
        private int cost;

        Point(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
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

        public double getZ() {
            return z;
        }

        public void setZ(double z) {
            this.z = z;
        }

        public double getR() {
            return r;
        }

        public void setR(double r) {
            this.r = r;
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

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.println("број на јазли во мрежата: ");
        int N = scanner.nextInt();

        System.out.println("должина на областа каде е дистрибуирана мрежата: ");
        int L = scanner.nextInt();

        System.out.println("радио опсег");
        int R = scanner.nextInt();

        System.out.println("шум на сигналот: ");
        int Noise = scanner.nextInt();

        System.out.println("фракција (процент) на anchor јазли: ");
        int AncPercent = scanner.nextInt();

        int anchor = (N * AncPercent) / 100;

        double d1;
        double d2;
        double d3;

        double sum = 0;
        double avg = 0;


        ArrayList<Point> anchors = new ArrayList<>();
        ArrayList<Point> jazel = new ArrayList<>();
        ArrayList<Point> tmpJazel = new ArrayList<>();
        ArrayList<Point> anchorJazli = new ArrayList<>();
        ArrayList<Integer> distances = new ArrayList<>();
        ArrayList<Integer> sosedi = new ArrayList<>();

        int num = N - anchor;

        int count = 0;
        for (int i = 0; i < num; i++) {

            double x = random.nextInt(L);
            double y = random.nextInt(L);
            double z = random.nextInt(L);
            Point p = new Point(x, y, z);
            p.isAnchor = false;
            jazel.add(p);
            tmpJazel.add(p);
        }

        for (int i = 0; i < anchor; i++) {

            int x = random.nextInt(L);
            int y = random.nextInt(L);
            int z = random.nextInt(L);
            Point p = new Point(x, y, z);
            p.isAnchor = true;
            anchors.add(p);
            jazel.add(p);
        }


        double[] err = new double[jazel.size()];
        boolean f = false;

        while (jazel.size() != anchors.size()) {
            for (int i = 0; i < jazel.size(); i++) {
                distances.clear();
                count = 0;

                if (jazel.get(i).isAnchor == false) {
                    for (Point point : anchors) {
                        double d = Distance(point.x, point.y, point.z, jazel.get(i).x, jazel.get(i).y, jazel.get(i).z);
                        if (d < R) count++;
                        double shum = d * Noise / 100;
                        d += shum;
                        distances.add((int) d);
                    }

                    int[] array = new int[10];
                    array = Min(distances);
                    d1 = array[0];
                    int poz1 = array[1];

                    double x1 = anchors.get(poz1).x;
                    double y1 = anchors.get(poz1).y;
                    double z1 = anchors.get(poz1).z;

                    d2 = array[2];
                    int poz2 = array[3];

                    double x2 = anchors.get(poz2).x;
                    double y2 = anchors.get(poz2).y;
                    double z2 = anchors.get(poz2).z;

                    d3 = array[4];
                    int poz3 = array[5];

                    double x3 = anchors.get(poz3).x;
                    double y3 = anchors.get(poz3).y;
                    double z3 = anchors.get(poz3).z;


                    if (d1 <= R && d2 <= R && d3 <= R) {
                        Point p1 = new Point(x1, y1, z1);
                        p1.r = d1;
                        Point p2 = new Point(x2, y2, z2);
                        p2.r = d2;
                        Point p3 = new Point(x3, y3, z3);
                        p3.r = d3;

                        Point p = Trilateracija(p1, p2, p3);

                        err[i] = Distance(p.x, p.y, p.z, jazel.get(i).x, jazel.get(i).y, jazel.get(i).z);
                        anchors.add(p);
                        anchorJazli.add(p);
                        jazel.get(i).isAnchor = true;
                        count++;
                    }

                }

                sosedi.add(count);
            }

            if (JazliNajdeni(jazel)) {
                break;
            }
        }

        System.out.println("\t" + "Found: ");

        for (int i = 0; i < tmpJazel.size(); i++) {
            System.out.println("X: " + String.format("%.1f", tmpJazel.get(i).x) +
                    "\t" + "Y: " + String.format("%.1f", tmpJazel.get(i).y) +
                    "\t" + "Z: " + String.format("%.1f", tmpJazel.get(i).z) +
                    "\t" + "X: " + String.format("%.1f", anchorJazli.get(i).x) +
                    "\t" + "Y: " + String.format("%.1f", anchorJazli.get(i).y) +
                    "\t" + "Z: " + String.format("%.1f", anchorJazli.get(i).z));

        }

        System.out.println("original anchor jazli: " + jazel.size());
        System.out.println("najdeni  anchor jazli:" + anchors.size());


        for (int i = 0; i < err.length; i++) {
            sum += err[i];
        }

        avg = sum / (err.length);
        System.out.println("Greska na anchor jazlite: " + String.format("%.1f", avg));
        double percentage = avg * 100 / R;
        System.out.println("Greskata na rang " + String.format("%.1f", percentage));

        double[] error = new double[tmpJazel.size()];
        sum = 0;

        for (int i = 0; i < tmpJazel.size(); i++) {
            double d = Distance(tmpJazel.get(i).x, tmpJazel.get(i).y, tmpJazel.get(i).z, anchorJazli.get(i).x, anchorJazli.get(i).y, anchorJazli.get(i).z);
            error[i] = d;
            sum += error[i];
        }

        avg = sum / (error.length);
        System.out.println("Greska ba2 anchor jazlite: " + String.format("%.1f", avg));
        percentage = avg * 100 / R;
        System.out.println("Greskata na rang" + String.format("%.1f", percentage));

        int suma = 0;
        for (int i = 0; i < sosedi.size(); i++) {
            suma += sosedi.get(i);
        }

        double averageN = suma / sosedi.size();
        System.out.println("Broj na sosedi: " + averageN);


    }

    public static double Distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
    }

    public static int[] Min(ArrayList<Integer> list) {
        int current = Integer.MAX_VALUE;

        int poz = 0;
        int[] array = new int[6];

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) < current) {
                current = list.get(i);
                poz = i;
            }
        }
        array[0] = current;
        array[1] = poz;
        list.set(poz, 1000000);
        current = Integer.MAX_VALUE;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) < current) {
                current = list.get(i);
                poz = i;
            }
        }

        array[2] = current;
        array[3] = poz;
        list.set(poz, 100000);
        current = Integer.MAX_VALUE;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) < current) {
                current = list.get(i);
                poz = i;
            }
        }

        array[4] = current;
        array[5] = poz;

        return array;

    }

    public static boolean JazliNajdeni(ArrayList<Point> list) {
        boolean flag = true;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isAnchor == false) {
                flag = false;
                break;
            }
        }
        return flag;
    }


    public static double sqr(double a) {
        return a * a;
    }

    public static double norm(Point a) {
        return Math.sqrt(sqr(a.x) + sqr(a.y) + sqr(a.z));
    }

    public static double dot(Point a, Point b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public static Point vector(Point a, Point b) {

        double x = a.x - b.x;
        double y = a.y - b.y;
        double z = a.z - b.z;
        Point p = new Point(x, y, z);
        return p;

    }

    public static Point vector_add(Point a, Point b) {

        double x = a.x + b.x;
        double y = a.y + b.y;
        double z = a.z + b.z;
        Point p = new Point(x, y, z);
        return p;

    }

    public static Point vector_cross(Point a, Point b) {

        double x = a.y * b.z - a.z * b.y;
        double y = a.z * b.x - a.x * b.z;
        double z = a.x * b.y - a.y * b.x;
        Point p = new Point(x, y, z);
        return p;

    }

    public static Point vector_divide(Point a, double b) {

        double x = a.x / b;
        double y = a.y / b;
        double z = a.z / b;
        Point p = new Point(x, y, z);
        return p;

    }

    public static Point vector_multiply(Point a, double b) {

        double x = a.x * b;
        double y = a.y * b;
        double z = a.z * b;
        Point p = new Point(x, y, z);
        return p;

    }

    public static Point Trilateracija(Point p1, Point p2, Point p3) {

        Point ex = vector_divide(vector(p2, p1), norm(vector(p2, p1)));
        double i = dot(ex, vector(p3, p1));
        Point a = vector(vector(p3, p1), vector_multiply(ex, i));
        Point a1 = vector_divide(a, norm(a));
        Point a2 = vector_cross(ex, a1);
        double d = norm(vector(p2, p1));
        double j = dot(a1, vector(p3, p1));

        double x = (sqr(p1.r) - sqr(p2.r) + sqr(d)) / (2 * d);
        double y = (sqr(p1.r) - sqr(p3.r) + sqr(i) + sqr(j)) / (2 * j) - (i / j) * x;
        double z = Math.sqrt(sqr(p1.r) - sqr(x) - sqr(y));

        Point aa = vector_add(p1, vector_add(vector_multiply(ex, x), vector_multiply(a1, y)));
        Point p4a = vector_add(a, vector_multiply(a2, z));
        Point p4b = vector(a, vector_multiply(a2, z));

        return aa;

    }

}