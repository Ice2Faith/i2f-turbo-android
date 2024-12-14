#include <jni.h>
#include <string>
#include<string.h>
/**
 * 命名规则：Java+包名+类名+方法名
 * Java.com.ugex.savelar.fmahellojni.MainActivity.stringFromJNI
 */
extern "C" JNIEXPORT jstring JNICALL
Java_com_ugex_savelar_fmahellojni_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    /*
     * 参数解析：
     * JNIEnv* env:
     *      访问Java对象的方法和类的
     * jobject obj:
     *      谁调用了此API，就传入谁
     *
     * 因此，这两个参数是必须的
     * */

    //要返回的字符串，在C++中的形式
    std::string hello = "Hello from C++";

    //直接返回了一个字符串，java字符串中构造就有一个以字节数组构造的
    //这里就是字节数组构造形式
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jdouble JNICALL
Java_com_ugex_savelar_fmahellojni_MainActivity_cAddNumber(
        JNIEnv* env,
        jobject  obj,
        jdouble a,jdouble b
        ) {
   jdouble result=a+b;
    return result;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_ugex_savelar_fmahellojni_MainActivity_stringReverse(
        JNIEnv* env,
        jobject obj,
        jstring str) {
    //获取到C++可用的字符串
    const char * cstr=env->GetStringUTFChars(str,0);

    int len=strlen(cstr);
    std::string retBuf="";

    len--;
    while(len>=0)
    {
        retBuf=retBuf+cstr[len--];
    }

    //使用之后，需要释放资源
    env->ReleaseStringUTFChars(str,cstr);

    //重新构造返回结果
    return env->NewStringUTF(retBuf.c_str());
}

///////////////////////////////////////////////////
/**
 * 使用方式2进行注册，是有一些麻烦的，特别是注册的接口少的时候，尤其
 * 写函数
 * 写JNI_OnLoad
 * 填写注册数组
 */
extern "C"
JNIEXPORT
jstring JNICALL CM2_getString(JNIEnv* env,
                             jobject obj)
{
    return env->NewStringUTF("通过方法2 RegisterNative返回的字符串");
}
//类的路径不要写错，否则映射错了，调用失败，程序崩溃
#define JNI_CLASS_PATH "com/ugex/savelar/fmahellojni/MainActivity"
/**
 * NINativeMethod这个结构体还记得吧，对就是他的一个静态数组
 * //signature的书写：(参数列表)返回值类型
 *
 */
static JNINativeMethod g_methods[]={
        {
          "M2_getString",
          "()Ljava/lang/String;",//返回值：L标识这是一个Java对象，后面跟类路径，有点smali的感觉了
          (void *)CM2_getString//映射到C++的函数名称，这个名称和Java层的名称无直接关系
        },
  };
//同一个进程只有一个javaVM（仅仅在安卓中）,注意函数名不要写错，否则你的程序怎么死的不知道
//这也是写native的痛点
jint JNI_OnLoad(JavaVM * vm,void * reserved)
{
    //首先，我们先获取对应版本的env,一般我们使用1.6版本
    JNIEnv * env=NULL;
    vm->GetEnv((void **)&env,JNI_VERSION_1_6);

    //然后，我们就拿到类名，进行注册操作
    jclass cls=env->FindClass(JNI_CLASS_PATH);
    //参数：java层的类名路径，方法映射表，表的大小（也就是表项数目）
    env->RegisterNatives(cls,g_methods,sizeof(g_methods)/sizeof(g_methods[0]));

    return JNI_VERSION_1_6;
}

//////////////////////////////////////////////////////
//C调用java方法
extern "C" JNIEXPORT
jstring JNICALL Java_com_ugex_savelar_fmahellojni_MainActivity_cUseJavaFunction(
        JNIEnv* env,
        jobject  obj
        )
{
    //得到类
    jclass cls=env->FindClass("com/ugex/savelar/fmahellojni/Animal");

    //获取属性和方法
    //参数：类，名称，signature
    //构造函数：<init>,这个如果AS报红，不用管
    jmethodID mtd_construct_id=env->GetMethodID(cls,"<init>","()V");
    jmethodID mtd_setWei_id=env->GetMethodID(cls,"setWeight", "(D)V");
    jmethodID mtd_getWei_id=env->GetMethodID(cls,"getWeight","()D");

    //生成一个对象,直接使用其构造方法
    jobject tobj=env->NewObject(cls,mtd_construct_id);

    //调用其方法
    //由于set方法没有返回值，callvoid
    //参数：对象，方法id，参数(变长)
    //特别注意，参数是浮点数，老老实实转为浮点数传进去，否则，参数无效
    //比如你传入45，将会被当做0进行设置，。。。
    env->CallVoidMethod(tobj,mtd_setWei_id,45.5);

    //其实jdouble就是：typedef double jdouble;所以这是没有问题的
    //另外，调用哪个对象的哪个方法一定要写对了对象，对象写错了，调用不到方法，直接无法运行。。。
    //比如，你把tobj写成了obj...
    double val=env->CallDoubleMethod(tobj,mtd_getWei_id);

    char buf[1024]={0};
    sprintf(buf,"Animal wieght:%.2lf",val);

    return env->NewStringUTF(buf);
}