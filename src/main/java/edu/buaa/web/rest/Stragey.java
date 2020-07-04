package edu.buaa.web.rest;

import java.util.Objects;

class Stragey {
    public int edin;
    public int edout;
    public Stragey(int a, int b){
        this.edin = a;
        this.edout = b;
    }
    @Override
    public String toString() {
        return "State{" +
                "edin=" + edin +
                ", edout=" + edout +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stragey state = (Stragey) o;
        return edin == state.edin &&
                edout == state.edout;
    }
    @Override
    public int hashCode() {
        return Objects.hash(edin, edout);
    }
}
