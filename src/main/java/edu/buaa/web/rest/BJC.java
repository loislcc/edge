package edu.buaa.web.rest;

import java.util.*;

/**
 * 用最少循环求两个数组的交集、差集、并集
 *
 * @author ZQC
 *
 */
public class BJC
{

    public static void main(String[] args)
    {
        String[] m = { "1", "2", "3", "4", "5" };
        String[] n = { "3", "4", "6" };

        System.out.println("----------并集------------");
        String[] b = getB(m, n);
        for (String i : b)
        {
            System.out.println(i);
        }

        System.out.println("----------交集------------");
        String[] j = getJ(m, n);
        for (String i : j)
        {
            System.out.println(i);
        }

        System.out.println("----------差集------------");
        String[] c = getC(n,m);
        for (String i : c)
        {
            System.out.println(i);
        }
    }

    /**
     * 求并集
     *
     * @param m
     * @param n
     * @return
     */
    public static String[] getB(String[] m, String[] n)
    {
        // 将数组转换为set集合
        Set<String> set1 = new HashSet<String>(Arrays.asList(m));
        Set<String> set2 = new HashSet<String>(Arrays.asList(n));

        // 合并两个集合
        set1.addAll(set2);

        String[] arr = {};
        return set1.toArray(arr);
    }

    /**
     * 求交集
     *
     * @param m
     * @param n
     * @return
     */
    public static String[] getJ(String[] m, String[] n)
    {
        List<String> rs = new ArrayList<String>();

        // 将较长的数组转换为set
        Set<String> set = new HashSet<String>(Arrays.asList(m.length > n.length ? m : n));

        // 遍历较短的数组，实现最少循环
        for (String i : m.length > n.length ? n : m)
        {
            if (set.contains(i))
            {
                rs.add(i);
            }
        }

        String[] arr = {};
        return rs.toArray(arr);
    }

    /**
     * 求差集
     *
     * @param m
     * @param n
     * @return
     */
    public static String[] getC(String[] m, String[] n)
    {
        // 将较长的数组转换为set
        Set<String> set = new HashSet<String>(Arrays.asList(m.length > n.length ? m : n));

        // 遍历较短的数组，实现最少循环
        for (String i : m.length > n.length ? n : m)
        {
            // 如果集合里有相同的就删掉，如果没有就将值添加到集合
            if (set.contains(i))
            {
                set.remove(i);
            } else
            {
                set.add(i);
            }
        }

        String[] arr = {};
        return set.toArray(arr);
    }

}


//import java.util.ArrayList;
//        import java.util.Collections;
//
//
//public class BJC {
//    public static void main(String[] args) {
//        ArrayList<String> tmplist=new ArrayList<String>();
//        int[] arr1={1,3,4,5,8,9};
//        int[] arr2={2,3,7,8,9};
//        tmplist=Intersection(arr1,arr2);
//        OutPut(tmplist);
//        tmplist.clear();
//        tmplist=Union(arr1,arr2);
//        OutPut(tmplist);
//    }
//    private static ArrayList<String> Intersection(int[] a1,int[] a2){
//        ArrayList<String> list=new ArrayList<String>();
//        for(int i=0;i<a1.length;i++)
//            for(int j=0;j<a2.length;j++)
//                if(a1[i]==a2[j])
//                    list.add(a2[j]);
//        return list;
//    }
//
//    private static ArrayList<String> Union(int[] a1,int[] a2){
//        ArrayList<String> list1=new ArrayList<String>();
//        ArrayList<String> list2=new ArrayList<String>();
//        for(int i:a1)
//            list1.add(i);
//        for(int i:a2)
//            list2.add(i);
//        list1.removeAll(list2);
//        list2.addAll(list1);
//        return list2;
//    }
//
//    private static void OutPut(ArrayList<String> list){
//        Collections.sort(list);
//        if(list.size()==0)
//            System.out.println("null");
//        else
//            for(int i:list)
//                System.out.print(i);
//        System.out.println("\n");
//    }
//}
