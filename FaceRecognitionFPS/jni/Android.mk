# Copyright (C) 2010 Huan Erdao
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

LOCAL_PATH:= $(call my-dir)

include $(LOCAL_PATH)/imgutils.mk

# this module
#
include $(CLEAR_VARS)

LOCAL_MODULE    := snapface-jni
LOCAL_SRC_FILES := snapface-jni.c
LOCAL_C_INCLUDES := $(LOCAL_PATH)/imgutils

LOCAL_STATIC_LIBRARIES := imgutils

include $(BUILD_SHARED_LIBRARY)
