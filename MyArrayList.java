//�ҵ�github:https://github.com/ygj0930
//�ҵĲ��ͣ�http://www.cnblogs.com/ygj0930/

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
    public transient E[] elements;//�ײ�ķ���������������Ԫ��
    /*
    ������transient�������飬��transient�ؼ��ֱ�ǵĳ�Ա�������������л����̡�
    serialization�����л����ṩ��һ�ֳ־û�����ʵ���Ļ��ƣ�ͨ�����л����԰Ѷ���������ĵ��б��档
    ������ĳ������������л����Ϳ�����transient�Ѹö����ų����⡣
    */
    public  int size;//size��¼���鵱ǰ����ЧԪ�ظ���
    
    //ʵ���������췽��
    public MyArrayList(int n) {
        if (n > 0) {
            this.elements = (E[])new Object[n];//��������Ϊn������
            this.size=0;//һ��ʼ���������Ϊ0
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+n);
        }
    }
     public MyArrayList() {
        this.elements = (E[])new Object[10];
         this.size=0;
    }
     public MyArrayList(Collection<Object> c) {
        elements = (E[])c.toArray();//������ת��Ϊ���飬��������Ϊ����Ԫ�ظ���
        if ((size = elements.length) != 0) {    //��ʱ����Ԫ�ظ���������������
                elements = (E[])Arrays.copyOf(elements, size, Object[].class);}       
    }
     
     //ʵ����������
     public void ensureCapacity(int newCapacity){
         int curr=elements.length;
         if(newCapacity>curr){
            elements=Arrays.copyOf(elements, newCapacity);
         }
     }
     
     //�����ײ��������������ϵ�ǰԪ������,�����Ԫ�ز���̫����˷��ڴ档size��������ʵ�ʴ��ڵ�Ԫ�ظ���
     public void trimToSize(){
         int curr=elements.length;
         if(size<curr){
             elements=Arrays.copyOf(elements, size);
         }
     }
     
     //ʵ���±��飺�������Խ���ڴ�
     public void rangeCheck(int requestIndex){
         if(requestIndex<0||requestIndex>size){
             throw new IndexOutOfBoundsException();
            
         }
     }
     
     //ʵ�ֻ�������������ɾ���顢��
     
     //��
     //��ĳ��λ�ò�����Ԫ��
     public void add(int index,E element){
         this.rangeCheck(index);//�ȼ�����λ�õĺϷ���
         
         //�ټ�鵱ǰ����Ԫ�ظ����Ƿ��Ѿ��ﵽ���ֵ�����ﵽ�����������ǣ�����������
         if(size==elements.length){
             ensureCapacity(elements.length*2+1);
         }
         
         //������λ�õ�ԭ�����Լ��������������ƶ�һλ���ڳ��ռ�
        System.arraycopy(elements, index, elements, index+1, 1);
         
         //������Ӧλ�ã���������Ԫ�ظ�����һ
         elements[index]=element;
         ++size;        
     }
     
     //������ĩβ׷��һ��Ԫ��
     public void add(E element){
         this.add(size, element);//ʵ���ͬ����elements[size]������Ԫ��
     }
     
     //��һ�����ϲ��뵽����ĳλ��
     public void addAll(int index,Collection<? extends E> c){
         rangeCheck(index);
         
         //��ȡ����Ԫ���Լ�����
         E[] newEs=(E[])c.toArray();
         int newLength=newEs.length;
         
         //������������
         ensureCapacity(size+newLength+1);
         
         //�������λ���Լ����Ԫ�صĸ�����������Ҫ���Ƶ�Ԫ�ظ���
         int move=size-index;
         
         //����System.arraycopy()������������Ĵ��������ƶ�������
         /*
         public static native void arraycopy(Object src,  int  srcPos,  
                                        Object dest, int destPos,  
                                        int length);  
         �����������һ������ָ�����ݵ���Դ���飬�ڶ�������˵������Դ����ʼλ��
                   ����������˵������ȥ���Ŀ�����飬���ĸ�����˵��������Ŀ�������д��ʱ����ʼλ�ã����һ������˵���ƶ������ݳ��ȣ���������
         �ú���������C���Ե�memmove()��������һ��ĸ��Ʒ�����ʵ��Ч��Ҫ�ߺܶ࣬Ҳ��ȫ�ܶ࣬���ʺ����������������顣
        ǿ���Ƽ��ڸ��ƴ�������Ԫ��ʱ�ø÷�������ȡ�ø��ߵ�Ч�ʡ�
         */
         //��ԭ����index~size��Χ��Ԫ���ƶ����ڳ�λ��
         if(move>0){
             System.arraycopy(elements, index, elements, index+newLength, move);
         }
        //����������Ԫ�ظ��Ƶ�elements�������ڳ���λ��
         System.arraycopy(newEs,0 , elements, index, newLength);
         
         size+=newLength;//Ԫ�ظ�������
     }
     
     //��һ�����ϲ��뵽����ĩβ
     public void addAll(Collection<? extends E> c){
         this.addAll(size, c);//�൱����ԭ����elements[size]����ʼ������һ������
     }
     
     
     //ɾ
     //�Ƴ�ָ��λ�õ�Ԫ��
     public E remove(int index){
         rangeCheck(index);
         
         E oldelement=elements[index];
         
         //ԭλ�ú����Ԫ������
         System.arraycopy(elements, index+1, elements, index, size-index-1);
         
         elements[size]=null;//�޸�ԭ�������һ��Ԫ��λ��Ϊ��
         --size;//�޸�Ԫ�ظ���
         
         return oldelement;
     }
     //�Ƴ�ĳ��ֵ��Ԫ��
     public boolean remove(E element){
         boolean index=false;
         for(int i=0;i<size;++i){
             if(element.equals(elements[i])){//���ҵ���ֵ
                 index=true;//˵���и�ֵ������ִ���Ƴ������Ϊtrue�����򣬽��false
                 this.remove(i);//��ͨ���±����Ƴ�
             }
         }
         return index;
     }
    //�Ƴ�ĳ�� ��Χ�� ��Ԫ��(������end��
     public void removeRange(int start,int end){
         int move=size-end;
         System.arraycopy(elements, end,elements, start, move);
         
         //�޸��������µ�λ��Ϊ��
         for(int i=size-1;i>(size-(end-start)-1);--i){
             elements[i]=null;
         }
         
         size-=(end-start);
     }
    
     
     //��
     //��ȡĳλ�õ�Ԫ��
     public E get(int index){
         rangeCheck(index);
         return elements[index];
     }
     
     //��
     //�޸�ĳλ�õ�Ԫ��
     public void set(int index,E newElement){
         rangeCheck(index);
         
         elements[index]=newElement;
     }
     
     //ת��Ϊ��ͨ����
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
