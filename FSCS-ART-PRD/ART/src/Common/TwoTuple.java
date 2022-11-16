//: net/mindview/util/TwoTuple.java（Java编程思想_代码_目录）
package Common;

public class TwoTuple<A, B extends Comparable<B>> implements Comparable{
    public  A first;
    public  B second;
    public TwoTuple(A a, B b) {
        first = a;
        second = b;
    }

    @Override
    public String toString() {
        return "TwoTuple{" +
                 "second=" + second.toString() +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if(this.second.compareTo(((TwoTuple<A, B >) o).second)==0){
            return 0;
        }else if(this.second.compareTo(((TwoTuple<A, B >) o).second)==-1){
            return 1;
        }else{
            return -1;
        }
    }
}
