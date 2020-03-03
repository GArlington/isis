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
package org.apache.isis.applib.services.hint;

import java.util.Set;

import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.bookmark.Bookmark;

// tag::refguide[]
public interface HintStore {

    // end::refguide[]
    // tag::refguide-1[]
    interface HintIdProvider {
        String hintId();
    }
    // end::refguide-1[]
    // tag::refguide[]

    String get(final Bookmark bookmark, String hintKey);

    void set(final Bookmark bookmark, String hintKey, String value);

    void remove(final Bookmark bookmark, String hintKey);

    void removeAll(Bookmark bookmark);

    Set<String> findHintKeys(Bookmark bookmark);

}
// end::refguide[]
