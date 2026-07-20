package RandomGenerator2;

import java.util.HashMap;

public class ParseWmx {
    int[] removedNums;  //删除的学号
    int[] amplifiedNums;//放大倍数的学号
    double amplifiedCount;//倍数
    HashMap<Integer, Integer> map;//固定被抽取的顺序
    HashMap<Integer, Integer> effectMap;//特效map
}
