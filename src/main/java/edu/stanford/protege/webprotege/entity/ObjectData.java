package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.semanticweb.owlapi.model.HasSignature;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 * <p>
 *     Represents data about some object.  This data includes browser text for the object.
 * </p>
 */
public abstract class ObjectData {

    /**
     * Gets the core object that is decorated with data
     */
    @JsonIgnore
    @Nonnull
    public abstract Object getObject();

    /**
     * Gets the signature of this object.
     * @return A (possibly empty) set representing the signature of this object.
     */
    @JsonIgnore
    public Set<OWLEntity> getSignature() {
        if(getObject() instanceof HasSignature) {
            return ((HasSignature) getSignature()).getSignature();
        }
        else {
            return Collections.emptySet();
        }
    }
}
