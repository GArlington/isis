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
package demoapp.dom.annotDomain.DomainObject;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.SemanticsOf;

import lombok.RequiredArgsConstructor;
import lombok.val;

import demoapp.dom.annotDomain.DomainObject.entityChangePublishing.DomainObjectEntityChangePublishingVm;
import demoapp.dom.annotDomain.DomainObject.nature.viewmodels.jaxbrefentity.StatefulVmJaxbRefsEntity;
import demoapp.dom.annotDomain.DomainObject.nature.viewmodels.usingjaxb.StatefulVmUsingJaxb;

@DomainService(nature=NatureOfService.VIEW, objectType = "demo.DomainObjectMenu")
//@Log4j2
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class DomainObjectMenu {

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(cssClassFa="fa-book", describedAs = "Entity changed events as XML")
    public DomainObjectEntityChangePublishingVm entityChangePublishing(){
        return new DomainObjectEntityChangePublishingVm();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(cssClassFa = "gamepad")
    public StatefulVmUsingJaxb natureStateful(final String message) {
        val viewModel = new StatefulVmUsingJaxb();
        viewModel.setMessage(message);
        return viewModel;
    }
    public String default0NatureStateful() {
        return "Some initial state";
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(cssClassFa = "gamepad")
    public StatefulVmJaxbRefsEntity natureStatefulRefsEntity(final String message) {
        val viewModel = new StatefulVmJaxbRefsEntity();
        viewModel.setMessage(message);
        return viewModel;
    }
    public String default0NatureStatefulRefsEntity() {
        return "Some initial state";
    }


}
