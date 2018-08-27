/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * you may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.ballerinalang.model.util.serializer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Use sun.misc.Unsafe.allocateInstance to allocate empty object of {@code clazz}.
 * Notice that this attempt may fail due to security restrictions or maybe due to
 * {@link sun.misc.UnSafe} module not being available in JAVA9 plus, or due to many
 * other reasons, failing to create the instance will return {@code null}.
 */
class UnsafeObjectAllocator {
    private UnsafeObjectAllocator() {}

    static Object allocateFor(Class<?> clazz) throws
            ClassNotFoundException,
            NoSuchFieldException,
            NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException {

        Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
        Field f = unsafeClass.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Object unsafe = f.get(null);
        Method allocateInstance = unsafeClass.getMethod("allocateInstance", Class.class);
        return clazz.cast(allocateInstance.invoke(unsafe, clazz));
    }
}
