#include <jni.h>
#include <string>
#include"FormulaCalculator.h"


static FormulaCalculator calculator;
static bool isSuccess=false;

extern "C" JNIEXPORT jdouble JNICALL
Java_com_ugex_savelar_formulacomputer_MainActivity_compute(
        JNIEnv* env,
        jobject _this,
        jstring formula) {
    const char * str=env->GetStringUTFChars(formula,NULL);

    isSuccess=false;
    double result=calculator.calculate(str,&isSuccess);

    return result;
}

extern "C" JNIEXPORT jdouble JNICALL
Java_com_ugex_savelar_formulacomputer_MainActivity_getLastResult(
        JNIEnv* env,
        jobject _this) {
    double result=calculator.getLastResult();
    return result;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_ugex_savelar_formulacomputer_MainActivity_getLastError(
        JNIEnv* env,
        jobject _this) {
    const char * str=calculator.getLastErrStr();
    return env->NewStringUTF(str);
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_ugex_savelar_formulacomputer_MainActivity_transferHexBase(
        JNIEnv* env,
        jobject _this,
        jstring _num, jint src_hex, jint dst_hex) {
    const char * str=env->GetStringUTFChars(_num,NULL);

    double num=calculator.hex2Number((char *)str,src_hex);

    char buf[300]={0};

    calculator.number2Hex(num,buf,dst_hex,12);

    return env->NewStringUTF(buf);
}