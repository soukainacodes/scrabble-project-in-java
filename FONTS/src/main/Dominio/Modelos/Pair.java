package Dominio.Modelos;

import java.util.Objects;

public class Pair<F,S> {
    private F first;
    private S second;
    
    public Pair(F first1, S second1) {
        this.first = first1;
        this.second = second1;
    }

    public static <F, S> Pair<F, S> createPair(F first1,S second1) {
        return new Pair<>(first1,second1);
    }
    
    public void setFirst(F newfirst) { first = newfirst;}
    public void setSecond(S newsecond) { second = newsecond;}

    public F getFirst() { return first;}
    public S getSecond() { return second;}

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) obj;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }
}
