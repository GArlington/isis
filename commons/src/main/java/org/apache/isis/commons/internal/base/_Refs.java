/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.isis.commons.internal.base;

import java.util.function.IntUnaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.function.UnaryOperator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * <h1>- internal use only -</h1>
 * <p>
 * Not thread-safe, primitive and object references.
 * </p>
 * <p>
 * <b>WARNING</b>: Do <b>NOT</b> use any of the classes provided by this package! <br/>
 * These may be changed or removed without notice!
 * </p>
 *
 * @since 2.0
 */
public final class _Refs {

    // -- FACTORIES
    
    public static BooleanReference booleanRef(final boolean value) {
        return new BooleanReference(value);
    }
    
    public static IntReference intRef(final int value) {
        return new IntReference(value);
    }
    
    public static LongReference longRef(final int value) {
        return new LongReference(value);
    }
    
    public static <T> ObjectReference<T> objectRef(final T value) {
        return new ObjectReference<>(value);
    }
    
    // -- IMPLEMENTATIONS
    
    @FunctionalInterface
    public static interface BooleanUnaryOperator {
        boolean applyAsBoolean(boolean value);
    }
    
    
    @Data @AllArgsConstructor
    public static final class BooleanReference {
        private boolean value;
        
        public boolean update(final @NonNull BooleanUnaryOperator operator) {
            return value=operator.applyAsBoolean(value);
        }
        
        public boolean isTrue() {
            return value;
        }
        
        public boolean isFalse() {
            return !value;
        }
    }
    
    @Data @AllArgsConstructor
    public static final class IntReference {
        private int value;
        
        public int update(final @NonNull IntUnaryOperator operator) {
            return value = operator.applyAsInt(value);
        }
        
        public boolean isSet(int other) {
            return value==other;
        }
        
    }
    
    @Data @AllArgsConstructor
    public static final class LongReference {
        private long value;
        
        public long update(final @NonNull LongUnaryOperator operator) {
            return value = operator.applyAsLong(value);
        }
        
        public boolean isSet(long other) {
            return value==other;
        }
    }
    
    @Data @AllArgsConstructor
    public static final class ObjectReference<T> {
        private T value;
        
        public T update(final @NonNull UnaryOperator<T> operator) {
            return value = operator.apply(value);
        }
        
        public boolean isSet(T other) {
            return value==other;
        }
    }
    
}
