package edu.buaa.web.rest;

class pair implements Comparable<pair>{
    public int index;
    public double value;
    public pair(int index, double value){
        this.index = index;
        this.value = value;
    }
    public int compareTo(pair o){
        if(this.value < o.value){
            return 1;
        }else if(this.value > o.value){
            return -1;
        }else{
            return 0;
        }
    }
}
