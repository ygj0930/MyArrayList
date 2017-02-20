//我的github:https://github.com/ygj0930
//我的博客：http://www.cnblogs.com/ygj0930/

package myarraylist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Administrator
 */
public class MyArrayList<E> {
    public transient E[] elements;//底层的泛型数组用来保存元素
    /*
    这里用transient修饰数组，用transient关键字标记的成员变量不参与序列化过程。
    serialization（序列化）提供了一种持久化对象实例的机制，通过序列化可以把对象输出到文档中保存。
    而不想某个对象参与序列化，就可以用transient把该对象排除在外。
    */
    public  int size;//size记录数组当前的有效元素个数
    
    //实现三个构造方法
    public MyArrayList(int n) {
        if (n > 0) {
            this.elements = (E[])new Object[n];//创建容量为n的数组
            this.size=0;//一开始数组里个数为0
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+n);
        }
    }
     public MyArrayList() {
        this.elements = (E[])new Object[10];
         this.size=0;
    }
     public MyArrayList(Collection<Object> c) {
        elements = (E[])c.toArray();//将集合转换为数组，数组容量为集合元素个数
        if ((size = elements.length) != 0) {    //此时数组元素个数等于数组容量
                elements = (E[])Arrays.copyOf(elements, size, Object[].class);}       
    }
     
     //实现容量扩充
     public void ensureCapacity(int newCapacity){
         int curr=elements.length;
         if(newCapacity>curr){
            elements=Arrays.copyOf(elements, newCapacity);
         }
     }
     
     //调整底层数组容量以契合当前元素数量,避免空元素部分太多而浪费内存。size是数组中实际存在的元素个数
     public void trimToSize(){
         int curr=elements.length;
         if(size<curr){
             elements=Arrays.copyOf(elements, size);
         }
     }
     
     //实现下标检查：避免访问越界内存
     public void rangeCheck(int requestIndex){
         if(requestIndex<0||requestIndex>size){
             throw new IndexOutOfBoundsException();
            
         }
     }
     
     //实现基本操作：增、删、查、改
     
     //增
     //在某个位置插入新元素
     public void add(int index,E element){
         this.rangeCheck(index);//先检查插入位置的合法性
         
         //再检查当前数组元素个数是否已经达到最大值，即达到数组容量，是，则扩充数组
         if(size==elements.length){
             ensureCapacity(elements.length*2+1);
         }
         
         //将插入位置的原数据以及其后的数据往后移动一位，腾出空间
        System.arraycopy(elements, index, elements, index+1, 1);
         
         //插入相应位置，并将数组元素个数加一
         elements[index]=element;
         ++size;        
     }
     
     //在数组末尾追加一个元素
     public void add(E element){
         this.add(size, element);//实则等同于在elements[size]处插入元素
     }
     
     //将一个集合插入到数组某位置
     public void addAll(int index,Collection<? extends E> c){
         rangeCheck(index);
         
         //获取插入元素以及个数
         E[] newEs=(E[])c.toArray();
         int newLength=newEs.length;
         
         //扩充数组容量
         ensureCapacity(size+newLength+1);
         
         //计算插入位置以及其后元素的个数，即：需要右移的元素个数
         int move=size-index;
         
         //调用System.arraycopy()方法进行数组的大量数据移动操作。
         /*
         public static native void arraycopy(Object src,  int  srcPos,  
                                        Object dest, int destPos,  
                                        int length);  
         方法解读：第一个参数指明数据的来源数组，第二个参数说明数据源的起始位置
                   第三个参数说明数据去向的目标数组，第四个参数说明数据在目标数组中存放时的起始位置，最后一个参数说明移动的数据长度（个数）。
         该函数调用了C语言的memmove()函数，比一般的复制方法的实现效率要高很多，也安全很多，很适合用来批量处理数组。
        强烈推荐在复制大量数组元素时用该方法，以取得更高的效率。
         */
         //将原数组index~size范围的元素移动，腾出位置
         if(move>0){
             System.arraycopy(elements, index, elements, index+newLength, move);
         }
        //将插入数组元素复制到elements数组中腾出的位置
         System.arraycopy(newEs,0 , elements, index, newLength);
         
         size+=newLength;//元素个数增加
     }
     
     //将一个集合插入到数组末尾
     public void addAll(Collection<? extends E> c){
         this.addAll(size, c);//相当于在原数组elements[size]处开始，插入一个集合
     }
     
     
     //删
     //移除指定位置的元素
     public E remove(int index){
         rangeCheck(index);
         
         E oldelement=elements[index];
         
         //原位置后面的元素左移
         System.arraycopy(elements, index+1, elements, index, size-index-1);
         
         elements[size]=null;//修改原数组最后一个元素位置为空
         --size;//修改元素个数
         
         return oldelement;
     }
     //移除某个值的元素
     public boolean remove(E element){
         boolean index=false;
         for(int i=0;i<size;++i){
             if(element.equals(elements[i])){//若找到该值
                 index=true;//说明有该值，可以执行移除，结果为true。否则，结果false
                 this.remove(i);//则通过下标来移除
             }
         }
         return index;
     }
    //移除某个 范围内 的元素(不包括end）
     public void removeRange(int start,int end){
         int move=size-end;
         System.arraycopy(elements, end,elements, start, move);
         
         //修改左移留下的位置为空
         for(int i=size-1;i>(size-(end-start)-1);--i){
             elements[i]=null;
         }
         
         size-=(end-start);
     }
    
     
     //查
     //获取某位置的元素
     public E get(int index){
         rangeCheck(index);
         return elements[index];
     }
     
     //改
     //修改某位置的元素
     public void set(int index,E newElement){
         rangeCheck(index);
         
         elements[index]=newElement;
     }
     
     //转变为普通数组
     public E[] toArray()
     {
         E[] array=(E[]) new Object[size];
         System.arraycopy(elements, 0,array,0, size);
         return array;
     }
     
     public static void main(String[] args) {
        MyArrayList<Integer> testMyArrayList=new MyArrayList<Integer>();
        
         System.out.println(testMyArrayList.size);
         
         testMyArrayList.add(1);
         testMyArrayList.add(2);
         testMyArrayList.add(3);
         testMyArrayList.add(4);
         testMyArrayList.add(5);
         
         System.out.println(testMyArrayList.size);
         System.out.println(testMyArrayList.get(3));  
         
         testMyArrayList.removeRange(1, 3);
         
         System.out.println(testMyArrayList.size);
         System.out.println(testMyArrayList.get(2));  
    }
     
}
