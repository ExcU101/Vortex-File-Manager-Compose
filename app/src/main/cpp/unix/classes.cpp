#include "jni.h"

static jclass findJavaFileDescriptorClass(JNIEnv *env) {
    static jclass clazz = nullptr;
    if (!clazz) {
        clazz = env->FindClass("java/io/FileDescriptor");
    }
    return clazz;
}

static _jmethodID *findJavaFileDescriptorInitMethod(JNIEnv *env) {
    return env->GetMethodID(findJavaFileDescriptorClass(env), "<init>", "(I)V");
}

static jclass findUnixExceptionClass(
        JNIEnv *env
) {
    jclass local = env->FindClass("io/github/excu101/filesystem/unix/error/UnixException");

    auto global = (jclass) env->NewGlobalRef(local);
    return global;
}

static jmethodID findUnixExceptionInitMethodClass(
        JNIEnv *env
) {
    return env->GetMethodID(findUnixExceptionClass(env), "<init>", "(ILjava/lang/String;)V");
}

static jthrowable findUnixExceptionThrowable(JNIEnv *env, jstring message, int errno) {
    env->NewObject(
            findUnixExceptionClass(env),
            findUnixExceptionInitMethodClass(env),
            (jint) errno,
            message
    );
}


static jclass findUnixCallErrorClass(
        JNIEnv *env
) {
    jclass local = env->FindClass(
            "java/lang/Throwable"
    );

    auto global = (jclass) env->NewGlobalRef(local);
    return global;
}

static jmethodID findLinuxCallErrorInitMethod(
        JNIEnv *env
) {
    return env->GetMethodID(
            findUnixCallErrorClass(env),
            "<init>",
            "(Ljava/lang/String;)V"
    );
}

static void __throwLinuxCallError(JNIEnv *env, char *message) {
    env->ThrowNew(findUnixCallErrorClass(env), message);
}

#define UNIX_ERROR(env, ...) __throwLinuxCallError(env, __VA_ARGS__);

static jclass findUnixStatusStructureStatClass(
        JNIEnv *env
) {
    jclass local = env->FindClass(
            "io/github/excu101/filesystem/unix/attr/UnixStatusStructure"
    );
    auto global = (jclass) env->NewGlobalRef(local);
    return global;
}

static jmethodID findUnixStatusStructureInitMethod(
        JNIEnv *env
) {
    return env->GetMethodID(
            findUnixStatusStructureStatClass(env),
            "<init>",
            "(IIIJJJJJJJJJJJJJ)V"
    );
}

static jclass findUnixDirentStructureClass(JNIEnv *env) {
    jclass local = env->FindClass(
            "io/github/excu101/filesystem/unix/attr/UnixDirentStructure"
    );
    auto global = (jclass) env->NewGlobalRef(local);
    return global;
}

static jmethodID findUnixDirentStructureInitMethod(JNIEnv *env) {
    return env->GetMethodID(
            findUnixDirentStructureClass(env),
            "<init>", "(JJII[B)V"
    );
}

static jclass findUnixFileSystemStatusStructureClass(JNIEnv *env) {
    jclass local = env->FindClass(
            "io/github/excu101/filesystem/unix/attr/UnixStructureFileSystemStatus"
    );
    auto global = (jclass) env->NewGlobalRef(local);
    return global;
}

static jmethodID findUnixFileSystemStatusStructureInitMethod(JNIEnv *env) {
    return env->GetMethodID(
            findUnixFileSystemStatusStructureClass(env),
            "<init>",
            "(JJJJJJJJJJJ)V"
    );
}