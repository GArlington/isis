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
package org.apache.isis.core.metamodel.spec.interaction;

import java.util.Optional;

import org.apache.isis.core.commons.internal.base._Either;
import org.apache.isis.core.metamodel.consent.Veto;
import org.apache.isis.core.metamodel.spec.ManagedObject;
import org.apache.isis.core.metamodel.spec.feature.ObjectAction;

import lombok.Getter;
import lombok.NonNull;
import lombok.val;

public final class ManagedAction extends ManagedMember {

    // -- FACTORIES
    
    public static final Optional<ManagedAction> lookupAction(
            @NonNull final ManagedObject owner,
            @NonNull final String memberId) {
        
        return ManagedMember.<ObjectAction>lookup(owner, MemberType.ACTION, memberId)
        .map(objectAction -> new ManagedAction(owner, objectAction));
    }
    
    public static final ActionHandle getActionHandle(
            @NonNull final ManagedObject owner,
            @NonNull final String memberId) {
    
        val managedAction = ManagedAction.lookupAction(owner, memberId);
        
        final _Either<ManagedAction, InteractionVeto> chain = managedAction.isPresent()
                ? _Either.left(managedAction.get())
                : _Either.right(InteractionVeto.notFound(new Veto(notFound(MemberType.ACTION, memberId))));
                
        return new ActionHandle(chain);
    }

    // -- IMPLEMENTATION
    
    @Getter private final ObjectAction action;
    
    private ManagedAction(
            final @NonNull ManagedObject owner, 
            final @NonNull ObjectAction action) {
        
        super(owner);
        this.action = action;
    }

    @Override
    public ObjectAction getMember() {
        return getAction();
    }

    @Override
    public MemberType getMemberType() {
        return MemberType.ACTION;
    }

    public String getName() {
        return getAction().getName();
    }

}
