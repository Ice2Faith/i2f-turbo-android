#include <jni.h>
#include <string>
#include"FileJoin.hpp"
#include"HuffmanZip.hpp"
#include"MultiFileZip.hpp"
extern "C" JNIEXPORT jstring JNICALL
Java_com_ugex_savelar_filejoinziper_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
FileJoin g_joiner;
HuffmanZip g_ziper;
MultiFileZip g_mulziper;
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_ugex_savelar_filejoinziper_MainActivity_hzp(JNIEnv *env, jobject thiz, jstring src_file,
                                                     jstring drt_file) {
   const char * srcName=env->GetStringUTFChars(src_file,0);
   const char * drtName=env->GetStringUTFChars(drt_file,0);
   BOOL_T  ret=g_ziper.ZipFile((char *)srcName,(char *)drtName);
   jboolean val=JNI_FALSE;
   if(ret==BOOL_TRUE)
       val=JNI_TRUE;
   return val;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_ugex_savelar_filejoinziper_MainActivity_dhzp(JNIEnv *env, jobject thiz, jstring src_file,
                                                      jstring drt_file) {
    const char * srcName=env->GetStringUTFChars(src_file,0);
    const char * drtName=env->GetStringUTFChars(drt_file,0);
    BOOL_T  ret=g_ziper.UnzipFile((char *)srcName,(char *)drtName);
    jboolean val=JNI_FALSE;
    if(ret==BOOL_TRUE)
        val=JNI_TRUE;
    return val;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_ugex_savelar_filejoinziper_MainActivity_fjs(JNIEnv *env, jobject thiz,
                                                     jobjectArray src_files, jstring drt_file) {
  int size=env->GetArrayLength(src_files);
  char ** srcNames=(char **)malloc(sizeof(char *)*size);
  for(int i=0;i<size;i++)
  {
       jstring jstr = (jstring)env->GetObjectArrayElement(src_files,i);
      srcNames[i] = (char *)env->GetStringUTFChars(jstr,0);
  }
  const char * drtName=env->GetStringUTFChars(drt_file,0);
    BOOL_T  ret=g_joiner.join(size,srcNames,(char *)drtName);

    jboolean val=JNI_FALSE;
    if(ret==BOOL_TRUE)
        val=JNI_TRUE;
    return val;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_ugex_savelar_filejoinziper_MainActivity_dfjs(JNIEnv *env, jobject thiz, jstring src_file,
                                                      jstring drt_path) {
    const char * srcName=env->GetStringUTFChars(src_file,0);
    const char * drtPath=env->GetStringUTFChars(drt_path,0);
    BOOL_T  ret=g_joiner.split((char *)srcName,(char *)drtPath);

    jboolean val=JNI_FALSE;
    if(ret==BOOL_TRUE)
        val=JNI_TRUE;
    return val;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_ugex_savelar_filejoinziper_MainActivity_mzp(JNIEnv *env, jobject thiz,
                                                     jobjectArray src_files, jstring drt_file) {
    int size=env->GetArrayLength(src_files);
    char ** srcNames=(char **)malloc(sizeof(char *)*size);
    for(int i=0;i<size;i++)
    {
        jstring jstr = (jstring)env->GetObjectArrayElement(src_files,i);
        srcNames[i] = (char *)env->GetStringUTFChars(jstr,0);
    }
    const char * drtName=env->GetStringUTFChars(drt_file,0);
    BOOL_T  ret=g_mulziper.ZipFiles(size,srcNames,(char *)drtName);

    jboolean val=JNI_FALSE;
    if(ret==BOOL_TRUE)
        val=JNI_TRUE;
    return val;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_ugex_savelar_filejoinziper_MainActivity_dmzp(JNIEnv *env, jobject thiz, jstring src_file,
                                                      jstring drt_path) {
    const char * srcName=env->GetStringUTFChars(src_file,0);
    const char * drtPath=env->GetStringUTFChars(drt_path,0);
    BOOL_T  ret=g_mulziper.UnzipFiles((char *)srcName,(char *)drtPath);

    jboolean val=JNI_FALSE;
    if(ret==BOOL_TRUE)
        val=JNI_TRUE;
    return val;
}