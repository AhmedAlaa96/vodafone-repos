#include <jni.h>

#include <string>

extern "C" JNIEXPORT jstring JNICALL Java_com_ahmed_vodafonerepos_retrofit_HeaderInterceptor_getAuthorizationValue(JNIEnv* env,jobject) {
  std::string tokenValue = "token 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";
  return env->NewStringUTF(tokenValue.c_str());
}