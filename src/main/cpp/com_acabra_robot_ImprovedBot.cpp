#include "com_acabra_robot_ImprovedBot.h"
#include <iostream>

JNIEXPORT jstring JNICALL Java_com_acabra_robot_ImprovedBot_getKeyboardLanguage
  (JNIEnv* env, jobject thisObject) {
  std::string hello = "Hello from C++ !!";
  std::cout << hello << std::endl;
  return env->NewStringUTF(hello.c_str());
}
