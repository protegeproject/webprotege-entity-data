package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.common.ShortForm;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntityVisitorEx;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
@AutoValue
@JsonTypeName("ClassData")
public abstract class OWLClassData extends OWLEntityData {



    public static OWLClassData get(@Nonnull OWLClass cls,
                                   @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms) {
        return get(cls, shortForms, false);
    }

    public static OWLClassData get(@Nonnull OWLClass cls,
                                   @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms,
                                   boolean deprecated) {
        return get(cls, toShortFormList(shortForms), deprecated);
    }

    public static OWLClassData get(@JsonProperty("entity") @Nonnull OWLClass cls,
                                   @JsonProperty("shortForms") @Nonnull ImmutableList<ShortForm> shortForms,
                                   @JsonProperty("deprecated") boolean deprecated) {
        return new AutoValue_OWLClassData(shortForms, deprecated, cls);
    }

    /**
     * For deserialization from JSON only
     * @param iri The lexical representation of the IRI
     * @param shortForms A nullable list of short forms
     * @param deprecated
     * @return
     */
    @JsonCreator
    protected static OWLClassData get(@JsonProperty("iri") @Nonnull String iri,
                                   @JsonProperty(value = "shortForms") @Nullable ImmutableList<ShortForm> shortForms,
                                   @JsonProperty("deprecated") boolean deprecated) {
        return new AutoValue_OWLClassData(Objects.requireNonNullElse(shortForms, ImmutableList.of()), deprecated, new OWLClassImpl(IRI.create(iri)));
    }

    @JsonIgnore
    @Override
    public OWLClass getEntity() {
        return getObject();
    }

    /**
     * For more compact JSON Serialization
     */
    @JsonProperty("iri")
    protected String getIri() {
        return getEntity().getIRI().toString();
    }

    @Nonnull
    @Override
    public abstract OWLClass getObject();

    @JsonIgnore
    @Override
    public PrimitiveType getType() {
        return PrimitiveType.CLASS;
    }

    @Override
    public <R, E extends Throwable> R accept(OWLPrimitiveDataVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public <R> R accept(OWLEntityVisitorEx<R> visitor, R defaultValue) {
        return visitor.visit(getEntity());
    }

    @Override
    public <R> R accept(OWLEntityDataVisitorEx<R> visitor) {
        return visitor.visit(this);
    }
}
