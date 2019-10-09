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

package org.apache.isis.metamodel.specloader.validator;

import java.util.Collection;
import java.util.List;

import org.apache.isis.commons.internal.collections._Lists;
import org.apache.isis.metamodel.MetaModelContext;
import org.apache.isis.metamodel.spec.ObjectSpecification;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(staticName = "of")
public class MetaModelValidatorVisiting extends MetaModelValidatorAbstract {

    @FunctionalInterface
    public interface Visitor {
        /**
         * @return <tt>true</tt> continue visiting specs.
         */
        boolean visit(ObjectSpecification objectSpec, MetaModelValidator validator);
    }
    
    public interface SummarizingVisitor extends Visitor {
        void summarize(MetaModelValidator validator);
    }
    
    @NonNull private final Visitor visitor;

    @Override
    public void collectFailuresInto(@NonNull ValidationFailures validationFailures) {
        validateAll();
        summarize();
        super.collectFailuresInto(validationFailures);
    }

    private void validateAll() {

        val specsValidated = _Lists.<ObjectSpecification>newArrayList();

        while(validateSpecs(specsValidated)) {
            // validate in a loop, because the act of validating might cause additional specs to be uncovered
        }

    }

    private boolean validateSpecs(List<ObjectSpecification> specsAlreadyValidated) {

        val specLoader = MetaModelContext.current().getSpecificationLoader();
        
        // all currently known specs
        // (previously we took a protective copy to avoid a concurrent modification exception,
        // but this is now done by SpecificationLoader itself)
        final Collection<ObjectSpecification> specsToValidate = 
                specLoader.snapshotSpecifications();

        // don't validate any specs already processed
        specsToValidate.removeAll(specsAlreadyValidated);
        if(specsToValidate.isEmpty()) {
            // don't call us again
            return false;
        }

        // validate anything new
        for (val objSpec : specsToValidate) {
            if(!visitor.visit(objSpec, this)) {
                break;
            }
        }

        // add the new specs just validated to the list (for next time)
        specsAlreadyValidated.addAll(specsToValidate);

        // go round the loop again
        return true;
    }

    private void summarize() {
        if(visitor instanceof SummarizingVisitor) {
            SummarizingVisitor summarizingVisitor = (SummarizingVisitor) visitor;
            summarizingVisitor.summarize(this);
        }
    }

}
