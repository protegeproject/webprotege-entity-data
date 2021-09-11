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
import org.semanticweb.owlapi.model.OWLEntityVisitorEx;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import uk.ac.manchester.cs.owl.owlapi.OWLNamedIndividualImpl;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
@AutoValue

@JsonTypeName("NamedIndividualData")
public abstract class OWLNamedIndividualData extends OWLEntityData {


    public static OWLNamedIndividualData get(@Nonnull OWLNamedIndividual individual,
                                            @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms) {
        return get(individual, shortForms, false);
    }


    public static OWLNamedIndividualData get(@Nonnull OWLNamedIndividual individual,
                                            @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms,
                                            boolean deprecated) {
        return get(individual, toShortFormList(shortForms), deprecated);
    }

    public static OWLNamedIndividualData get(@JsonProperty("entity") OWLNamedIndividual individual,
                                            @JsonProperty("shortForms") ImmutableList<ShortForm> shortForms,
                                            @JsonProperty("deprecated") boolean deprecated) {
        return new AutoValue_OWLNamedIndividualData(shortForms, deprecated, individual);
    }

    @JsonCreator
    private static OWLNamedIndividualData get(@JsonProperty("iri") String iri,
                                            @JsonProperty(value = "shortForms", defaultValue = "[]") ImmutableList<ShortForm> shortForms,
                                            @JsonProperty(value = "deprecated", defaultValue = "false") boolean deprecated) {
        return new AutoValue_OWLNamedIndividualData(Objects.requireNonNullElse(shortForms, ImmutableList.of()), deprecated, new OWLNamedIndividualImpl(IRI.create(iri)));
    }

    @Nonnull
    @Override
    public abstract OWLNamedIndividual getObject();

    @JsonIgnore
    @Override
    public OWLNamedIndividual getEntity() {
        return getObject();
    }

    @JsonProperty("iri")
    private String getIri()  {
        return getEntity().getIRI().toString();
    }

    @Override
    public PrimitiveType getType() {
        return PrimitiveType.NAMED_INDIVIDUAL;
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
