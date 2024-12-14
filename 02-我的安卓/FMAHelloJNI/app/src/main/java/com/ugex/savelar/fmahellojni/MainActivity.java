package com.ugex.savelar.fmahellojni;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

/**
 * JNI的调用：
 * 两种方法的java层书写都是一致的，都是native标记即可
 * *******************************************************
 * 方式1：
 * 比较直接，直接按照约定写即可
 * 通过Java调用C的方法
 * 通过在cpp文件中，导出一个本地方法：
 * extern "C" JNIEXPORT jstring JNICALL
 * Java_com_ugex_savelar_fmahellojni_MainActivity_stringFromJNI(
 *         JNIEnv* env,
 *         jobject );
 * 命名规则：
 * extern "C"
 * JNIEXPORT
 * jstring JNICALL Java_包名_类名_方法名(参数列表)
 *
 * 而在Java 文件中，需要指明这样名称的一个native方法：
 *  public native String stringFromJNI();
 *
 *  这样，Java便能够调用C的方法
 *
 *  而Cpp文件的编译，依赖于CMake文件产生编译脚本，类似于Makefile的东西
 *  这样就能实现Cpp的编译和使用了
 *
 * 实际上，让JVM帮助我们去调用CPP的方法，
 * 既然这样，我们就要遵守JVM规定的命名规则
 **********************************************************
 * 方式2：
 * 通过RegisterNative方式，建立映射，提供给JVM使用
 * 这样的话，灵活度更高了
 * 实际上就是注册几个结构体
 * typedef struct
 * {
 *     const char * name;       //就是java层的名字
 *     const char * signature;  //规定的具有格式的字符串标记
 *     void * fnPtr;            //c++层的函数指针
 * }NINativeMethod;
 * 其中两个约定回调函数很重要：
 * 注册库时回调函数
 * jint JNI_OnLoad(JavaVM * vm,void * reserved);
 * 注销库时回调
 * jint JNI_OnUnload(JavaVM * vm,void * reserved);
 *
 * signature：
 * 用于描述java和c相互调用时的描述符，表示了参数和返回值
 * 其实就是在JVM中，建立了一个映射，形成相互调用的基础
 * signature就相当于key,函数地址就相当于value
 * 其规则：
 * 输入参数放在括号内(),输出参数在括号右边
 * 多个参数使用分号分割的,
 * 【就算只有一个参数后面也要分号】,错了，可以不加，有点蒙
 * 原始类型的signature对照：
 * java类型       描述符号
 * boolean          Z
 * byte             B
 * char             C
 * short            S
 * int              I
 * long             L
 * float            F
 * double           D
 * void             V
 * 举个例子来说
 * (I;D;)D;
 * 这样，就表示这样一个函数：double (*func)(int,double);
 * 明白了吧，就是和函数指针的位置有区别
 *
 * 然而，在java中，类其实是主要的，原始类型相对来说被弱化了
 * 类的signature:
 * L包路径/类名
 * 例如：
 * Ljava/lang/String;
 * 这样就是表示String类
 * 数组的signature:
 * 通过[符号表示
 *
 * 下面来一写案例：
 * ([Cat;)[LAnimal; ==> Animal[] (*) (Cat[])
 * ([Ljava/lang/String;)[Ljava/lang/Object; ==> Object[] (*) (String[])
 * ([I,[Ljava/lang/String;[LAnimal;)Ljava/lang/Object; ==> Object[] (*) (int[],String[],Animal[])
 * 有了以上的例子，相信你写这个signature已经没有什么问题了
 *
 * *************************************************************
 * 说完java调用C了，那么C怎么调用java呢？
 * 说一下步骤：
 * 1.FindClass 找到java类
 * 2.GetMethodID/GetFieldID 获取Java类方法和属性
 * 3.NewObject 创建一个具体的类
 * 4.Call<TYPE>Method/[G/S]et<TYPE>Filed
 *  分解一下：
 *  Call<TYPE>Method
 *  Call<TYPE>Get/Set<TYPE>Filed
 *  也就是访问方法或者访问属性
 *
 * */
/*
* JNI基本概念
* JNIEnv:
*   Java的本地化环境，C要访问Java的方法，类等，都通过它
* JavaVM：
*   Java虚拟机，Android下一个APP对应一个JVM，和Java本身一个进程能够对应多个JVM不同
* 线程：
*   每一个Java线程，对应一个JNIEnv，也就是说，只能访问到所属线程的东西，如果错误的跨线程访问将会Crash
*
* 下面，我们来写一个自己的CPP函数，进行调用
* */
public class MainActivity extends AppCompatActivity {

    /**
     * 需要添加我们的本地库，导入进来
     * 这样才能调用C++ 方法
     */
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(
                "Java invoke C:\n\n"+
                 "method normal:\n"+
                stringReverse(stringFromJNI()+cAddNumber(5,12))+"\n"+
                "method register native:\n"+
                M2_getString()+"\n"+
                "\nC invoke java:\n"+
                cUseJavaFunction());
    }

    /**
     * 这就是我们的C++方法，具有native标志
     * @return 字符串
     */
    public native String stringFromJNI();

    /**
     * 创建我们的方法
     * 可以看到，我们在java中的参数，到C++中之后，一般都是在开头加上j
     * 比如:double-->jdouble
     */
    public native double cAddNumber(double a,double b);
    /*
    * 只是，不同的是，（目前看来）
    * java中的string传过去之后，如果cpp要使用
    * 那么还需要一些转换和释放的过程
    * 返回时，也需要转换的过程
    * */
    public native String stringReverse(String str);

    /**
     * 使用方法二来创建一个本地接口
     */
    public native String M2_getString();

    /**
     * C调用java方法，实现本地接口
     */
    public native String cUseJavaFunction();
}
