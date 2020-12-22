package com.example.demo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.template.Engine;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateUtil;
import com.example.demo.model.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
class DemoApplicationTests {

    /**
     * stream流
     */
    @Test
    public void streamTest() {
        List<User> userList = Arrays.asList(
                new User("李星云", 18, 0, "渝州", new BigDecimal(1000)),
                new User("李星云", 18, 0, "渝州", new BigDecimal(1000)),
                new User("陆林轩", 16, 1, "渝州", new BigDecimal(500)),
                new User("姬如雪", 17, 1, "幻音坊", new BigDecimal(800)),
                new User("袁天罡", 99, 0, "藏兵谷", new BigDecimal(100000)),
                new User("张子凡", 19, 0, "天师府", new BigDecimal(900)),
                new User("陆佑劫", 45, 0, "不良人", new BigDecimal(600)),
                new User("张天师", 48, 0, "天师府", new BigDecimal(1100)),
                new User("蚩梦", 18, 1, "万毒窟", new BigDecimal(800))
        );

        //filter：年龄大于20,姓名包含张
        List<User> userList1 = userList.stream().filter(user -> user.getAge() > 20).filter(user -> user.getName().contains("张")).collect(Collectors.toList());
        //distinct：去重
        List<User> userList2 = userList.stream().distinct().collect(Collectors.toList());
        //sorted：根据年龄排序
        List<User> userList3 = userList.stream().sorted(Comparator.comparingInt(User::getAge)).collect(Collectors.toList());
        //limit：返回前4个元素
        List<User> userList4 = userList.stream().limit(4).collect(Collectors.toList());
        //skip：跳过第1个元素
        List<User> userList5 = userList.stream().skip(1).collect(Collectors.toList());
        //map：姓名集合
        List<String> nameList1 = userList.stream().map(User::getName).distinct().collect(Collectors.toList());
        //joining：姓名拼接
        String nameList2 = userList.stream().map(User::getName).distinct().collect(Collectors.joining(", "));
        //summarizingInt：一次性得到元素的个数、总和、最大值、最小值
        IntSummaryStatistics statistics = userList.stream().collect(Collectors.summarizingInt(User::getAge));
        //groupingBy：分组
        Map<String, List<User>> groupMap = userList.stream().collect(Collectors.groupingBy(User::getAddress));

        System.out.println("");
    }

    //---------------------------------------------↓十种排序算法↓----------------------------------------------//

    /**
     * 交换数组内两个元素
     *
     * @param array 数组
     * @param i     下标1
     * @param j     下标2
     */
    public static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }


    /**
     * 冒泡排序
     * 依次比较相邻元素
     *
     * @param array 数组
     * @return 排序后数组
     */
    public static int[] bubbleSort(int[] array) {
        if (array.length == 0) {
            return array;
        }
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - 1 - i; j++) {
                if (array[j + 1] < array[j]) {
                    swap(array, j + 1, j);
                }
            }
        }
        return array;
    }

    /**
     * 选择排序
     * <p>
     * 1. 在未排序序列中找到最小（大）元素，存放到排序序列的起始位置，
     * 2. 再从剩余未排序元素中继续寻找最小（大）元素，然后放到已排序序列的末尾。以此类推
     *
     * @param array 数组
     * @return 排序后数组
     */
    public static int[] selectionSort(int[] array) {
        if (array.length == 0) {
            return array;
        }
        for (int i = 0; i < array.length; i++) {
            int minIndex = i;
            for (int j = i; j < array.length; j++) {
                //找到最小的数
                if (array[j] < array[minIndex]) {
                    //将最小数的索引保存
                    minIndex = j;
                }
            }
            swap(array, minIndex, i);
        }
        return array;
    }

    /**
     * 插入排序
     * 构建有序序列，对于未排序数据，在已排序序列中从后向前扫描，找到相应位置并插入
     *
     * @param array 数组
     * @return 排序后数组
     */
    public static int[] insertionSort(int[] array) {
        if (array.length == 0) {
            return array;
        }
        int current;
        for (int i = 0; i < array.length - 1; i++) {
            current = array[i + 1];
            int preIndex = i;
            while (preIndex >= 0 && current < array[preIndex]) {
                array[preIndex + 1] = array[preIndex];
                preIndex--;
            }
            array[preIndex + 1] = current;
        }
        return array;
    }

    /**
     * 希尔排序
     * 也是一种插入排序，它是简单插入排序经过改进之后的一个更高效的版本，也称为缩小增量排序，同时该算法是冲破O(n2）的第一批算法之一。
     * 它与插入排序的不同之处在于，它会优先比较距离较远的元素。希尔排序又叫缩小增量排序。
     * <p>
     * 选择增量gap=length/2，缩小增量继续以gap = gap/2的方式，将整个待排序的记录序列分割成为若干子序列分别进行直接插入排序
     *
     * @param array 数组
     * @return 排序后数组
     */
    public static int[] ShellSort(int[] array) {
        int len = array.length;
        int temp, gap = len / 2;
        while (gap > 0) {
            for (int i = gap; i < len; i++) {
                temp = array[i];
                int preIndex = i - gap;
                while (preIndex >= 0 && array[preIndex] > temp) {
                    array[preIndex + gap] = array[preIndex];
                    preIndex -= gap;
                }
                array[preIndex + gap] = temp;
            }
            gap /= 2;
        }
        return array;
    }

    /**
     * 归并排序
     * <p>
     * 1．把长度为n的输入序列分成两个长度为n/2的子序列；
     * 2．对这两个子序列分别采用归并排序；
     * 3．将两个排序好的子序列合并成一个最终的排序序列。
     *
     * @param array 数组
     * @return 排序后数组
     */
    public static int[] MergeSort(int[] array) {
        if (array.length < 2) return array;
        int mid = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);
        return merge(MergeSort(left), MergeSort(right));
    }

    /**
     * 将两段排序好的数组结合成一个排序数组
     *
     * @param left  有序数组1
     * @param right 有序数组2
     * @return 合并后的有序数组
     */
    public static int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        for (int index = 0, i = 0, j = 0; index < result.length; index++) {
            if (i >= left.length) {
                result[index] = right[j++];
            } else if (j >= right.length) {
                result[index] = left[i++];
            } else if (left[i] > right[j]) {
                result[index] = right[j++];
            } else {
                result[index] = left[i++];
            }
        }
        return result;
    }

    /**
     * 快速排序
     * <p>
     * 1．先从数列中取出一个数作为基准数。
     * 2．分区过程，将比这个数大的数全放到它的右边，小于或等于它的数全放到它的左边。
     * 3．再对左右区间重复第二步，直到各区间只有一个数
     *
     * @param array 数组
     * @param start
     * @param end
     * @return 有序数组
     */
    public static int[] QuickSort(int[] array, int start, int end) {
        if (array.length < 1 || start < 0 || end >= array.length || start > end) {
            return null;
        }
        int smallIndex = partition(array, start, end);
        if (smallIndex > start) {
            QuickSort(array, start, smallIndex - 1);
        }
        if (smallIndex < end) {
            QuickSort(array, smallIndex + 1, end);
        }
        return array;
    }

    /**
     * 快速排序算法——partition
     *
     * @param array 数组
     * @param start
     * @param end
     * @return 有序数组
     */
    public static int partition(int[] array, int start, int end) {
        //随机取基准数
        int pivot = (int) (start + Math.random() * (end - start + 1));
        int smallIndex = start - 1;

        swap(array, pivot, end);

        for (int i = start; i <= end; i++)
            if (array[i] <= array[end]) {
                smallIndex++;
                if (i > smallIndex) {
                    swap(array, i, smallIndex);
                }
            }
        return smallIndex;
    }


    //声明全局变量，用于记录数组array的长度；
    static int len;

    /**
     * 堆排序算法
     * 子结点的键值或索引总是小于（或者大于）它的父节点
     * <p>
     * 1. 将初始待排序关键字序列(R1,R2….Rn)构建成大顶堆，此堆为初始的无序区；
     * 2. 将堆顶元素R[1]与最后一个元素R[n]交换，此时得到新的无序区(R1,R2,……Rn-1)和新的有序区(Rn),且满足R[1,2…n-1]<=R[n]；
     * 3. 由于交换后新的堆顶R[1]可能违反堆的性质，因此需要对当前无序区(R1,R2,……Rn-1)调整为新堆，然后再次将R[1]与无序区最后一个元素交换，得到新的无序区(R1,R2….Rn-2)和新的有序区(Rn-1,Rn)。不断重复此过程直到有序区的元素个数为n-1，则整个排序过程完成。
     *
     * @param array 数据
     * @return 有序数组
     */
    public static int[] HeapSort(int[] array) {
        len = array.length;
        if (len < 1) {
            return array;
        }
        //1.构建一个最大堆
        buildMaxHeap(array);
        //2.循环将堆首位（最大值）与末位交换，然后在重新调整最大堆
        while (len > 0) {
            swap(array, 0, len - 1);
            len--;
            adjustHeap(array, 0);
        }
        return array;
    }

    /**
     * 建立最大堆
     *
     * @param array 数组
     */
    public static void buildMaxHeap(int[] array) {
        //从最后一个非叶子节点开始向上构造最大堆
        for (int i = (len / 2 - 1); i >= 0; i--) {
            adjustHeap(array, i);
        }
    }

    /**
     * 调整成为最大堆
     *
     * @param array 数组
     * @param i
     */
    public static void adjustHeap(int[] array, int i) {
        int maxIndex = i;
        //如果有左子树，且左子树大于父节点，则将最大指针指向左子树
        if (i * 2 < len && array[i * 2] > array[maxIndex]) {
            maxIndex = i * 2;
        }
        //如果有右子树，且右子树大于父节点，则将最大指针指向右子树
        if (i * 2 + 1 < len && array[i * 2 + 1] > array[maxIndex]) {
            maxIndex = i * 2 + 1;
        }
        //如果父节点不是最大值，则将父节点与最大值交换，并且递归调整与父节点交换的位置。
        if (maxIndex != i) {
            swap(array, maxIndex, i);
            adjustHeap(array, maxIndex);
        }
    }

    /**
     * 计数排序
     * 核心在于将输入的数据值转化为键存储在额外开辟的数组空间中
     * 要求输入的数据必须是有确定范围的整数，只能对整数进行排序。
     * <p>
     * 1. 找出待排序的数组中最大和最小的元素；
     * 2. 统计数组中每个值为i的元素出现的次数，存入数组C的第i项；
     * 3. 对所有的计数累加（从C中的第一个元素开始，每一项和前一项相加）；
     * 4. 反向填充目标数组：将每个元素i放在新数组的第C(i)项，每放一个元素就将C(i)减去1。
     *
     * @param array 数据
     * @return 有序数组
     */
    public static int[] CountingSort(int[] array) {
        if (array.length == 0) return array;
        int bias, min = array[0], max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
            if (array[i] < min) {
                min = array[i];
            }
        }
        bias = -min;
        int[] bucket = new int[max - min + 1];
        Arrays.fill(bucket, 0);
        for (int j : array) {
            bucket[j + bias]++;
        }
        int index = 0, i = 0;
        while (index < array.length) {
            if (bucket[i] != 0) {
                array[index] = i - bias;
                bucket[i]--;
                index++;
            } else {
                i++;
            }
        }
        return array;
    }

    /**
     * 桶排序
     * 是计数排序的升级版。它利用了函数的映射关系，高效与否的关键就在于这个映射函数的确定
     * <p>
     * 1. 人为设置一个BucketSize，作为每个桶所能放置多少个不同数值（例如当BucketSize==5时，该桶可以存放｛1,2,3,4,5｝这几种数字，但是容量不限，即可以存放100个3）；
     * 2. 遍历输入数据，并且把数据一个一个放到对应的桶里去；
     * 3. 对每个不是空的桶进行排序，可以使用其它排序方法，也可以递归使用桶排序；
     * 4. 从不是空的桶里把排好序的数据拼接起来。
     * <p>
     * 注意，如果递归使用桶排序为各个桶排序，则当桶数量为1时要手动减小BucketSize增加下一循环桶的数量，否则会陷入死循环，导致内存溢出。
     *
     * @param array      数组
     * @param bucketSize 数值
     * @return 有序数组
     */
    public static ArrayList<Integer> BucketSort(ArrayList<Integer> array, int bucketSize) {
        if (array == null || array.size() < 2) {
            return array;
        }
        int max = array.get(0), min = array.get(0);
        // 找到最大值最小值
        for (Integer integer : array) {
            if (integer > max) {
                max = integer;
            }
            if (integer < min) {
                min = integer;
            }
        }
        int bucketCount = (max - min) / bucketSize + 1;
        ArrayList<ArrayList<Integer>> bucketArr = new ArrayList<>(bucketCount);
        ArrayList<Integer> resultArr = new ArrayList<>();
        for (int i = 0; i < bucketCount; i++) {
            bucketArr.add(new ArrayList<>());
        }
        for (Integer integer : array) {
            bucketArr.get((integer - min) / bucketSize).add(integer);
        }
        for (int i = 0; i < bucketCount; i++) {
            if (bucketSize == 1) { // 如果带排序数组中有重复数字时
                resultArr.addAll(bucketArr.get(i));
            } else {
                if (bucketCount == 1) {
                    bucketSize--;
                }
                ArrayList<Integer> temp = BucketSort(bucketArr.get(i), bucketSize);
                resultArr.addAll(temp);
            }
        }
        return resultArr;
    }

    /**
     * 基数排序
     * 按照低位先排序，然后收集；再按照高位排序，然后再收集；依次类推，直到最高位。
     * 有时候有些属性是有优先级顺序的，先按低优先级排序，再按高优先级排序。最后的次序就是高优先级高的在前，高优先级相同的低优先级高的在前
     * <p>
     * 1. 取得数组中的最大数，并取得位数；
     * 2. arr为原始数组，从最低位开始取每个位组成radix数组；
     * 3. 对radix进行计数排序（利用计数排序适用于小范围数的特点）
     * <p>
     * <p>
     * 基数排序 vs 计数排序 vs 桶排序
     * 这三种排序算法都利用了桶的概念，但对桶的使用方法上有明显差异：
     * 基数排序：根据键值的每位数字来分配桶
     * 计数排序：每个桶只存储单一键值
     * 桶排序：每个桶存储一定范围的数值
     *
     * @param array 数据
     * @return 有序数组
     */
    public static int[] RadixSort(int[] array) {
        if (array == null || array.length < 2) {
            return array;
        }
        // 1.先算出最大数的位数；
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            max = Math.max(max, array[i]);
        }
        int maxDigit = 0;
        while (max != 0) {
            max /= 10;
            maxDigit++;
        }
        int mod = 10, div = 1;
        ArrayList<ArrayList<Integer>> bucketList = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            bucketList.add(new ArrayList<>());
        for (int i = 0; i < maxDigit; i++, mod *= 10, div *= 10) {
            for (int value : array) {
                int num = (value % mod) / div;
                bucketList.get(num).add(value);
            }
            int index = 0;
            for (ArrayList<Integer> integers : bucketList) {
                for (Integer integer : integers) {
                    array[index++] = integer;
                }
                integers.clear();
            }
        }
        return array;
    }

    /**
     * 排序算法测试
     */
    @Test
    public void sortTest() {
        int[] array = new int[]{34, 20, 25, 7, 21, 48, 85, 53, 47, 18, 3, 69};

        int[] ints = bubbleSort(array);
        int[] ints1 = selectionSort(array);
        int[] ints2 = insertionSort(array);
        int[] ints3 = ShellSort(array);
        int[] ints4 = MergeSort(array);
        int[] ints5 = QuickSort(array, 0, 11);
        int[] ints6 = HeapSort(array);
        int[] ints7 = CountingSort(array);

        ArrayList<Integer> list = (ArrayList<Integer>) Arrays.stream(array).boxed().collect(Collectors.toList());
        ArrayList<Integer> ints8 = BucketSort(list, 5);

        int[] ints9 = RadixSort(array);

        System.out.println("");
    }

    //---------------------------------------------↓十大经典算法↓----------------------------------------------//

    /**
     * 二分查找算法（非递归方式）
     *
     * @param array  数组
     * @param target 查找目标
     * @return 目标的下标
     */
    public static int binarySearch(int[] array, int target) {
        int left = 0;
        int right = array.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (array[mid] == target) {
                return mid;
            } else if (array[mid] > target) {
                right = mid - 1; // 向左找
            } else {
                left = mid + 1; // 向右找
            }
        }
        return -1;
    }

    /**
     * 分治算法
     * （1）基本概念
     * 分治算法是一种很重要的算法，字面上的解释是“分而治之”，就是把一个复杂的问题分解成两个或更多的相同或相似的子问题...
     * 直到最后子问题可以简单的直接求解，原问题的解即子问题的解的合并，
     * 这个技巧就是很多高效算法的基础，如排序算法（快速排序，归并排序），傅里叶变换（快速傅里叶变换）...
     * （2）基本步骤
     * 1）分解：将原问题分解为若干个规模较小的问题，相互独立，与原问题形式相同的子问题
     * 2）解决：若子问题规模较小则直接解决，否则递归地解各个子问题
     * 3）合并：将各个子问题的解合并为原问题的解
     * （3）分治算法设计模式
     * if |P|<=n0
     * then return (ADHOC(P))
     * // 将P分解为较小的问题P1,P2...PK
     * for i <- 1 to k
     * do yi <- Divide-and-Conquer(Pi) 递归解决Pi
     * T <- MERGE(y1,y2...yk) 合并子问题
     * return (T)
     * <p>
     * |P|：表示问题P的规模
     * n0：表示阈值，表示当问题P的规模不超过n0时，问题已容易直接解出，不必再继续分解。
     * ADHOC(P)：是该分治法中的基本子算法，用于直接解小规模的问题P。因此，当P的规模不超过n0时直接用算法ADHOC(P)求解
     * 算法MERGE(y1,y2...yk)：是该分治算法中的合并子算法，用于将P的子问题P1,P2...PK的相应的解y1,y2,..yk合并为P的解。
     * <p>
     * <p>
     * 经典案例：汉诺塔
     * 有三根杆(编号A、B、C)，在A杆自下而上、由大到小按顺序放置n个盘子。
     * 游戏的目标：把A杆上的盘子全部移到C杆上，并仍保持原有顺序叠好，写出盘子移动步骤。
     * 操作规则：每次只能移动一个盘子，并且在移动过程中三根杆上都始终保持大盘在下，小盘在上，操作过程中盘子可以置于A、B、C任一杆上。
     * <p>
     * 思路分析：
     * (1)以C盘为中介，从A杆将1至n-1号盘移至B杆；
     * (2)将A杆中剩下的第n号盘移至C杆；
     * (3)以A杆为中介；从B杆将1至n-1号盘移至C杆。
     */
    public static void hanoiTower(int num, char a, char b, char c) {
        if (num == 1) { // 只有一个盘，直接解出
            System.out.println("第1个盘从" + a + "->" + c);
        } else {
            // 如果n>=2的情况
            // 1.先把最上面的所有盘A->B，移动过程会使用C
            hanoiTower(num - 1, a, c, b);
            // 2.把最下边的盘A->C
            System.out.println("第" + num + "个盘从" + a + "->" + c);
            // 3.把B塔所有盘从B->C，移动过程使用到A
            hanoiTower(num - 1, b, a, c);
        }
    }

    /**
     * 动态规划算法
     * 案例：背包问题，有n个物品，它们有各自的重量和价值，现有给定容量的背包，如何让背包里装入的物品具有最大的价值总和？
     * 思路分析：
     * （1）假设：
     * 用w[i],v[i]来确定是否需要将该物品放入背包中；
     * 即对于给定的n个物品，设v[i],w[i]分别为第i个物品的价值和重量，C为背包的容量。
     * 再令v[i][j] 表示在前i个物品中能够装入容量j的背包的最大价值。则我们有下面的结果：
     * （2）结论：
     * 1）当v[i][0]=v[0][j]=0; // 表示填入表 第一行和第一列是0
     * 2）当w[i]>j时；v[i][j]=v[i-1][j] // 当准备加入新增的商品的容量大于当前背包的容量时，就直接使用上一个单元格的装入策略
     * 3）当j>=w[i]时；v[i][j]=max{v[i-1][j], v[i]+v[i-1][j-w[i]]}
     * // 当准入的新增的商品的容量小于等于当前背包的容量，装入方式：
     * v[i-1][j]：就是上一个单元格的装入的最大值
     * v[i]：表示当前商品的价值
     * v[i-1][j-w[i]]：装入i-1商品，到剩余空间j-w[i]的最大值
     * 当j>=w[i]时：v[i][j] = max{v[i-1][j], v[i-1][j-w[i]]}
     *
     * @param weight 物品重量
     * @param value  物品价值
     * @param num    背包的容量
     */
    public static void KnapsackProblem(int[] weight, int[] value, int num) {
        int n = value.length; // 物品个数
        // 创建二维数据
        int[][] v = new int[n + 1][num + 1];
        // 1）当v[i][0]=v[0][j]=0; // 表示填入表 第一行和第一列是0
        for (int i = 0; i < v.length; i++) {
            v[0][i] = 0; // 第一列为0
        }
        for (int i = 0; i < v.length; i++) {
            v[i][0] = 0; // 第一行为0
        }
        int[][] path = new int[n + 1][num + 1];
        for (int i = 1; i < v.length; i++) {
            for (int j = 1; j < v[0].length; j++) { // 不处理第1列
                // 当w[i]>j时；v[i][j]=v[i-1][j] // 当准备加入新增的商品的容量大于当前背包的容量时，就直接使用上一个单元格的装入策略
                if (weight[i - 1] > j) {
                    v[i][j] = v[i - 1][j];
                } else {
                    // 当j>=w[i]时；v[i][j]=max{v[i-1][j], v[i]+v[i-1][j-w[i]]}
                    // v[i-1][j]：就是上一个单元格的装入的最大值
                    // v[i]：表示当前商品的价值
                    // v[i-1][j-w[i]]：装入i-1商品，到剩余空间j-w[i]的最大值
                    // 当准入的新增的商品的容量小于等于当前背包的容量，装入方式：
                    if (v[i - 1][j] < value[i - 1] + v[i - 1][j - weight[i - 1]]) { // w[i]->w[i-1]替换?
                        v[i][j] = value[i - 1] + v[i - 1][j - weight[i - 1]];
                        // 把当前的情况记录到path
                        path[i][j] = 1;
                    } else {
                        v[i][j] = v[i - 1][j];
                    }
                }
            }
        }
        // 输出一把
        for (int[] ints : v) {
            for (int anInt : ints) {
                System.out.print(anInt + "\t");
            }
            System.out.println();
        }
        System.out.println("========================");
        // 其实我们只需要最后的放入
        int i = path.length - 1;
        int j = path[0].length - 1;
        while (i > 0 && j > 0) {
            if (path[i][j] == 1) {
                System.out.printf("第%d个商品放入到背包%n", i);
                j -= weight[i - 1];
            }
            i--;
        }
    }

    /**
     * 字符串模式匹配--暴力破解法
     *
     * @param s 主串
     * @param t 模式串
     * @return 如果找到，返回在主串中第一个字符出现的下标，否则为-1
     */
    public static int bruteForceMatch(String s, String t) {
        char[] s_arr = s.toCharArray();
        char[] t_arr = t.toCharArray();
        // 主串的位置
        int i = 0;
        // 模式串的位置
        int j = 0;
        while (i < s_arr.length && j < t_arr.length) {
            if (s_arr[i] == t_arr[j]) {
                // 当两个字符相同，就比较下一个
                i++;
                j++;
            } else {
                // 一旦不匹配，i后退，j归0
                i = i - j + 1;
                j = 0;
            }
        }
        if (j == t_arr.length) {
            return i - j;
        } else {
            return -1;
        }
    }

    /**
     * 字符串模式匹配--KMP算法
     * <p>
     * 利用已经部分匹配这个有效信息，保持i指针不回溯，通过修改j指针，让模式串尽量地移动到有效的位置。
     * 整个KMP的重点就在于当某一个字符与主串不匹配时，要将j指针移动到哪
     * <p>
     * 当T[i] != P[j]时
     * 有T[i-j ~ i-1] == P[0 ~ j-1]
     * 由P[0 ~ k-1] == P[j-k ~ j-1]
     * 必然：T[i-k ~ i-1] == P[0 ~ k-1]
     *
     * @param s 主串
     * @param t 模式串
     * @return 若匹配成功，返回t在s中的位置（第一个相同字符对应的位置），若匹配失败，返回-1
     */
    public static int kmpMatch(String s, String t) {
        char[] s_arr = s.toCharArray();
        char[] t_arr = t.toCharArray();
        int[] next = getNextArray(t_arr);
        int i = 0, j = 0;
        while (i < s_arr.length && j < t_arr.length) {
            if (j == -1 || s_arr[i] == t_arr[j]) {
                i++;
                j++;
            } else {
                j = next[j];
            }
        }
        if (j == t_arr.length) {
            return i - j;
        } else {
            return -1;
        }
    }

    /**
     * 求出一个字符数组的next数组
     *
     * @param t 字符数组
     * @return next数组
     */
    public static int[] getNextArray(char[] t) {
        int[] next = new int[t.length];
        next[0] = -1;
        next[1] = 0;
        int k;
        for (int j = 2; j < t.length; j++) {
            k = next[j - 1];
            while (k != -1) {
                if (t[j - 1] == t[k]) {
                    next[j] = k + 1;
                    break;
                } else {
                    k = next[k];
                }
                next[j] = 0;  //当k==-1而跳出循环时，next[j] = 0，否则next[j]会在break之前被赋值
            }
        }
        return next;
    }

    /**
     * 算法测试
     */
    @Test
    public void algorithmTest() {
        int[] array = {2, 5, 7, 14, 16, 22, 35, 39, 48, 54, 57};
        int index = binarySearch(array, 39);
        System.out.println("39是数组的第" + (index + 1) + "个元素");

        hanoiTower(5, 'A', 'B', 'C');

        KnapsackProblem(new int[]{1, 3, 4, 6, 7}, new int[]{50, 200, 400, 550, 800}, 10);

        System.out.println(bruteForceMatch("abcabaabaabcacb", "abaabcac"));

        System.out.println(kmpMatch("abcabaabaabcacb", "abaabcac"));
    }

    //---------------------------------------------↓根据HTML生成PDF↓----------------------------------------------//

    //PDF保存路径(绝对路径)
    public String pdf_save_path = "/pdf/export/";
    //PDF字体路径(相对路径)
    public String pdf_font_path = "pdf/font/MicrosoftYaHei.ttf";
    //模板路径(相对路径)
    public String pdf_tmpl_path = "pdf/tmpl/";
    //模板名称
    public String pdf_tmpl_name = "demo.ftl";
    //PDF加密密钥
    public String pdf_secret = "qwe123!@#.";

    @Test
    public void createPdf() {
        //读取模板
        Engine engine = TemplateUtil.createEngine(new TemplateConfig(pdf_tmpl_path, TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate(pdf_tmpl_name);

        HashMap<String, Object> data = Maps.newHashMap();
        //添加数据
        HashMap<String, Object> user = Maps.newHashMap();
        user.put("name", "admin");
        user.put("age", "18");
        user.put("gender", "男");
        data.put("user", user);

        //根据模板和数据生成html
        String html = template.render(data);
        //对html内容进行反转义,例如将&ldquo;和&rdquo;转为双引号,防止XML解析异常
        html = StringEscapeUtils.unescapeHtml4(html);
        String fileName = "demo.pdf";
        try (BufferedOutputStream out = FileUtil.getOutputStream(pdf_save_path + fileName);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ITextRenderer iTextRenderer = new ITextRenderer();
            iTextRenderer.getFontResolver().addFont(pdf_font_path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            iTextRenderer.setDocumentFromString(html);
            iTextRenderer.layout();
            iTextRenderer.createPDF(baos);
            iTextRenderer.finishPDF();
            //添加水印(可添加多个)
            ArrayList<String> texts = Lists.newArrayList(DateUtil.now(), "我是水印1", "我是水印2");
            InputStream in = new ByteArrayInputStream(baos.toByteArray());
            baos.flush();
            addWaterMark(in, out, texts);
            log.info("success!");
        } catch (Exception e) {
            log.error("=====创建pdf异常：", e);
        }
    }

    /**
     * 添加水印
     *
     * @param arr
     */
    private void addWaterMark(InputStream in, OutputStream out, List<String> arr) throws DocumentException, IOException {
        PdfReader pdfReader = new PdfReader(in);
        PdfStamper pdfStamper = new PdfStamper(pdfReader, out);
        pdfStamper.setEncryption(null, pdf_secret.getBytes(), PdfWriter.ENCRYPTION_AES_128, false);
        int pages = pdfReader.getNumberOfPages() + 1;
        // 设置水印透明度
        PdfGState gs = new PdfGState();
        gs.setFillOpacity(0.3f);
        for (int i = 1; i < pages; i++) {
            PdfContentByte underContent = pdfStamper.getOverContent(i);
            underContent.beginText();
            underContent.setFontAndSize(BaseFont.createFont(pdf_font_path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED), 12);
            underContent.setGState(gs);
            underContent.setColorFill(Color.blue);
            for (int m = 0; m < 6; m++) {
                for (int j = 0; j < 6; j++) {
                    for (int n = 0; n < arr.size(); n++) {
                        underContent.showTextAlignedKerned(Element.ALIGN_LEFT, arr.get(n), 100 * j, (n + 1) * 20 + 200 * m, 45);
                    }
                }
            }
            underContent.endText();
            underContent.stroke();
        }
        pdfStamper.close();
        pdfReader.close();
    }
}
