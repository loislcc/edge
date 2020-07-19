package edu.buaa.web.rest;

public class tool {
    //通用类型处理方法,T为除基本类型以7a686964616fe4b893e5b19e31333236396439外的所有类型,T必须覆写 equals()方法.
    public static <T> boolean deepEquals(T[][] a,T[][] b){
        try{
            for(int i=0; i<a.length; i++)
                if(!java.util.Arrays.deepEquals(a[i],b[i]))
                    return false;
            return true;
        }catch(Exception e){}
        return false;
    }
    //基本类型处理方法,这里是int类的,其它基本类型方法雷同
    public static boolean deepEquals(int[][] a,int[][] b){
        try{
            for(int i=0; i<a.length; i++)
                for(int j=0; j<a[i].length; j++)
                    if(a[i][j]!=b[i][j])
                        return false;
            return true;
        }catch(Exception e){}
        return false;
    }
    static void a(Object [] a){}
    public static void main(String[] args) {
//基本类型测试:
        int[][] a = {{1,2,3},{2,3,4},{3,4,5,6,7,8}};
        int[][] b = {{1,2,3},{2,3,4},{3,4,5,6,7,8}};
        int[][] c = {{1,2,3},{2,3,5},{3,4,5,6,7,8}};
        boolean equals;
        equals = deepEquals(a,b);
        if(equals)System.out.println("a和b完全一样");
        else System.out.println("a和b不完全一样");
        equals = deepEquals(a,c);
        if(equals)System.out.println("a和b完全一样");
        else System.out.println("a和c不完全一样");
////非基本类型测试
//        E[][] t1 = {{new E(1),new E(2)},{new E(3),new E(4)},{new E(56),new E(77)},};
//        E[][] t2 = {{new E(1),new E(3)},{new E(3),new E(4)},{new E(56),new E(77)},};
//        E[][] t3 = {{new E(1),new E(2)},{new E(3),new E(4)},{new E(56),new E(77)},};
//        equals = deepEquals(t1,t2);
//        if(equals)System.out.println("t1和t2完全一样");
//        else System.out.println("t1和t2不完全一样");
//        equals = deepEquals(t1,t3);
//        if(equals)System.out.println("t1和t3完全一样");
//        else System.out.println("t1和t3不完全一样");
    }
}

class E{
    int id;
    E(int i){id=i;}
    public boolean equals(Object o){
        return o!=null&&o instanceof E?((E)o).id==id:false;
    }
}
