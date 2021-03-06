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

package org.apache.isis.core.metamodel.exceptions.persistence;

import org.apache.isis.core.metamodel.adapter.oid.Oid;

/**
 * (Used to?) indicates that the <tt>PojoRecreator</tt> was unable to instantiate a new pojo for the specified
 * {@link Oid}.

 * TODO: v2 - review - doesn't seem to be used?
 *  There is also {@link org.apache.isis.core.metamodel.adapter.oid.ObjectNotFoundException} which may be this class' replacement.
 * @deprecated
 */
@Deprecated
public class ObjectNotFoundException extends ObjectPersistenceException {
    private static final long serialVersionUID = 1L;

    public ObjectNotFoundException(final Oid oid) {
        super("Object not found in store with oid " + oid);
    }

    public ObjectNotFoundException(final Oid oid, final Throwable cause) {
        super("Object not found in store with oid " + oid, cause);
    }
}
