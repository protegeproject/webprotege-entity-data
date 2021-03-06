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
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntityVisitorEx;
import uk.ac.manchester.cs.owl.owlapi.OWLDataPropertyImpl;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
@AutoValue

@JsonTypeName("DataPropertyData")
public abstract class OWLDataPropertyData extends OWLPropertyData {

    public static OWLDataPropertyData get(@Nonnull OWLDataProperty property,
                                            @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms) {
        return get(property, shortForms, false);
    }


    public static OWLDataPropertyData get(@Nonnull OWLDataProperty property,
                                            @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms,
                                            boolean deprecated) {
        return get(property, toShortFormList(shortForms), deprecated);
    }

    public static OWLDataPropertyData get(@JsonProperty("entity") OWLDataProperty entity,
                                            @JsonProperty("shortForms") ImmutableList<ShortForm> shortForms,
                                            @JsonProperty("deprecated") boolean deprecated) {
        return new AutoValue_OWLDataPropertyData(shortForms, deprecated, entity);
    }

    @JsonCreator
    private static OWLDataPropertyData get(@JsonProperty("iri") String iri,
                                            @JsonProperty(value = "shortForms", defaultValue = "[]") ImmutableList<ShortForm> shortForms,
                                            @JsonProperty("deprecated") boolean deprecated) {
        return new AutoValue_OWLDataPropertyData(Objects.requireNonNullElse(shortForms, ImmutableList.of()), deprecated, new OWLDataPropertyImpl(IRI.create(iri)));
    }

    @Nonnull
    @Override
    public abstract OWLDataProperty getObject();

    @Override
    public PrimitiveType getType() {
        return PrimitiveType.DATA_PROPERTY;
    }

    @Override
    public boolean isOWLAnnotationProperty() {
        return false;
    }

    @JsonIgnore
    @Override
    public OWLDataProperty getEntity() {
        return getObject();
    }

    @JsonProperty("iri")
    private String getIri() {
        return getEntity().getIRI().toString();
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
