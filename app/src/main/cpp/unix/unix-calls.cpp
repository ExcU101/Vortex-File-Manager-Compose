#include "stdlib.h"
#include "dirent.h"
#include "string"
#include "errno.h"
#include "sys/errno.h"
#include "../log.h"
#include "classes.cpp"
#include "dirent.h"
#include "jni.h"
#include "attrs.cpp"
#include "operations.cpp"


static char *fromByteArrayToPath(
        JNIEnv *env,
        jbyteArray path
) {
    jbyte *segments = env->GetByteArrayElements(path, NULL);
    jsize jLength = env->GetArrayLength(path);
    auto length = (size_t) jLength;
    char *cPath = (char *) malloc(length + 1);
    memcpy(cPath, segments, length);
    env->ReleaseByteArrayElements(path, segments, JNI_ABORT);
    cPath[length] = '\0';
    return cPath;
}

static jobject doStat(
        JNIEnv *env,
        char *path,
        bool isLinkStatus
) {
    static jmethodID constructor(0);
    if (!constructor) { constructor = findUnixStatusStructureInitMethod(env); }
    struct stat64 status = {};
    isLinkStatus ? isLinkExists(path, &status) : isExists(path, &status);
    free(path);

    switch (errno) {
        case ENOTDIR:
            break;
        case ENAMETOOLONG:
            UNIX_ERROR(env, strcasestr(path, "Path name is too long"))
            break;
    }

    auto userId = (jint) status.st_uid;
    auto groupId = (jint) status.st_gid;
    auto mode = (jint) status.st_mode;
    auto deviceId = (jlong) status.st_dev;
    auto inode = (jlong) status.st_ino;
    jlong countLinks = status.st_nlink;
    auto otherDeviceId = (jlong) status.st_rdev;
    jlong blocksSize = status.st_blksize;
    auto blocks = (jlong) status.st_blocks;
    jlong size = status.st_size;
    jlong lastModifiedTimeSeconds = status.st_mtim.tv_sec;
    jlong lastAccessTimeSeconds = status.st_atim.tv_sec;
    jlong creationTimeSeconds = status.st_ctim.tv_sec;
    jlong lastModifiedTimeNanos = status.st_mtim.tv_nsec;
    jlong lastAccessTimeNanos = status.st_atim.tv_nsec;
    jlong creationTimeNanos = status.st_ctim.tv_nsec;

    return env->NewObject(
            findUnixStatusStructureStatClass(env),
            constructor,
            userId,
            groupId,
            mode,
            deviceId,
            inode,
            countLinks,
            otherDeviceId,
            blocksSize,
            blocks,
            size,
            lastModifiedTimeSeconds,
            lastAccessTimeSeconds,
            creationTimeSeconds,
            lastModifiedTimeNanos,
            lastAccessTimeNanos,
            creationTimeNanos
    );
}

extern "C"
JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_lstat(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    char *cPath = fromByteArrayToPath(env, path);
    return doStat(env, cPath, true);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_stat(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    char *cPath = fromByteArrayToPath(env, path);
    return doStat(env, cPath, false);
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_delete(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    char *cPath = fromByteArrayToPath(env, path);
    if (unlink(cPath) == 0) {
        LOGV("File deleted.")
    }

    switch (errno) {
        case EACCES:
            UNIX_ERROR(env, "Cannot get access")
            break;
        case ENOTDIR:
            UNIX_ERROR(env, "This is not directory.")
            break;
    }

    free(cPath);
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_closeDir(
        JNIEnv *env,
        jobject thiz,
        jlong pointer
) {
    DIR *dir = (DIR *) pointer;
    closedir(dir);
}

jlong openDirectory(
        JNIEnv *env,
        char *path
) {
    DIR *pointer = opendir(path);
    free(path);

    switch (errno) {
        case EACCES:
            UNIX_ERROR(env, "Cannot get access")
            break;
        case ENOTDIR:
            UNIX_ERROR(env, "This is not directory.")
            break;
    }

    return (jlong) pointer;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_openDir(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    return openDirectory(env, fromByteArrayToPath(env, path));
}

static jobject newLinuxDirentStruct(
        JNIEnv *env,
        const struct dirent64 *directory
) {
    static jmethodID constructor(0);
    if (!constructor) {
        constructor = findUnixDirentStructureInitMethod(env);
    }

    if (!directory) return nullptr;

    auto inode = (jlong) directory->d_ino;
    jlong offset = directory->d_off;
    jint recordLength = directory->d_reclen;
    jint type = directory->d_type;
    char *name = (char *) (directory->d_name);

    size_t length = strlen(name);
    auto javaLength = (jsize) length;
    jbyteArray bytes = env->NewByteArray(javaLength);
    env->SetByteArrayRegion(bytes, 0, javaLength, (jbyte *) name);

    return env->NewObject(
            findUnixDirentStructureClass(env),
            constructor,
            inode,
            offset,
            recordLength,
            type,
            bytes
    );
}

static int getItemCount(char *path) {
    int count = 0;
    DIR *dir;
    dir = opendir(path);
    if (dir != nullptr) {
        while (readdir64(dir)) { count++; }
        (void) closedir(dir);
    } else return 0;

    return count;
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_linux_LinuxCalls_rename(
        JNIEnv *env,
        jobject thiz,
        jbyteArray source,
        jbyteArray dest
) {
    char *sourcePath = fromByteArrayToPath(env, source);
    char *destPath = fromByteArrayToPath(env, dest);
    rename(sourcePath, destPath);
    free(sourcePath);
    free(destPath);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_readDir(
        JNIEnv *env,
        jobject thiz,
        jlong pointer
) {
    DIR *dir = (DIR *) pointer;

    dirent64 *directory = readdir64(dir);


    switch (errno) {
        case EBADF:
            UNIX_ERROR(env, "Invalid pointer to directory")
            break;
        case EOVERFLOW:
            UNIX_ERROR(env, "Stream is overflowed")
            break;
        case ENOENT:
            UNIX_ERROR(env, "Invalid position in directory stream")
            break;
        case EINTR:
            UNIX_ERROR(env, "Something gone wrong...")
            break;
    }

    return newLinuxDirentStruct(env, directory);
}

static jobject openFileDescriptor(JNIEnv *env, int id) {
    static jmethodID constructor(0);
    if (!constructor) {
        constructor = findJavaFileDescriptorInitMethod(env);
    }

    return env->NewObject(
            findJavaFileDescriptorClass(env),
            constructor,
            id
    );
}

extern "C"
JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_open(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path,
        jint flags,
        jint mode
) {
    char *cPath = fromByteArrayToPath(env, path);
    int cFlags = flags;
    auto cMode = (mode_t) mode;
    int id = openFileDescriptor(cPath, cFlags, cMode, false);
    free(cPath);

    return openFileDescriptor(env, id);
}

extern "C"
JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_mkdir(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path,
        jint mode
) {
    char *cPath = fromByteArrayToPath(env, path);
    auto cMode = (mode_t) mode;
    if (!makeDirectory(cPath, cMode)) {
        UNIX_ERROR(env, "Something gone wrong")
    }
    free(cPath);
    switch (errno) {
        case EEXIST:

            break;
        case EACCES:

            break;
    }
}

static jobject doStatVfs(JNIEnv *env, const struct statvfs64 *statvfs) {
    static jmethodID constructor(0);
    if (!constructor) {
        constructor = findUnixFileSystemStatusStructureInitMethod(env);
    }
    jlong blockSize = statvfs->f_bsize;
    jlong fundamentBlockSize = statvfs->f_frsize;
    jlong totalBlocks = statvfs->f_blocks;
    jlong freeBlocks = statvfs->f_bfree;
    jlong freeNonRootBlocks = statvfs->f_bavail;
    jlong totalFiles = statvfs->f_files;
    jlong freeFiles = statvfs->f_ffree;
    jlong freeNonRootFiles = statvfs->f_favail;
    jlong fileSystemId = statvfs->f_fsid;
    jlong bitMask = statvfs->f_flag;
    jlong maxFileNameLength = statvfs->f_namemax;

    return env->NewObject(
            findUnixFileSystemStatusStructureClass(env),
            constructor,
            blockSize,
            fundamentBlockSize,
            totalBlocks,
            freeBlocks,
            freeNonRootBlocks,
            totalFiles,
            freeFiles,
            freeNonRootFiles,
            fileSystemId,
            bitMask,
            maxFileNameLength
    );
}


extern "C"
JNIEXPORT jobject JNICALL
Java_io_github_excu101_filesystem_linux_LinuxCalls_statvfs(
        JNIEnv *env,
        jobject thiz,
        jbyteArray path
) {
    struct statvfs64 *statvfs = {};
    char *cPath = fromByteArrayToPath(env, path);
    if (!statusFileSystem(cPath, statvfs)) {
        UNIX_ERROR(env, "Cannot get file system status");
    }

    return doStatVfs(env, statvfs);
}
extern "C"
JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_rename(
        JNIEnv *env,
        jobject thiz,
        jbyteArray source,
        jbyteArray dest
) {
    char *cSource = fromByteArrayToPath(env, source);
    char *cDest = fromByteArrayToPath(env, dest);
    renameFile(cSource, cDest);

    if (errno != 0) {
        UNIX_ERROR(env, "Error occurred")
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_unix_UnixCalls_close(
        JNIEnv *env,
        jobject thiz,
        jint descriptor
) {
    closeFileDescriptor(descriptor);
}
extern "C"
JNIEXPORT void JNICALL
Java_io_github_excu101_filesystem_fs_operation_NativeCalls_close(
        JNIEnv *env,
        jobject thiz,
        jint descriptor
) {
    closeFileDescriptor(descriptor);
}