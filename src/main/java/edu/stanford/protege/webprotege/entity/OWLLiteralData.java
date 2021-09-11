package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.ShortForm;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLDatatypeImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImpl;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
@AutoValue

@JsonTypeName("LiteralData")
public abstract class OWLLiteralData extends OWLPrimitiveData {

    public static OWLLiteralData get(@Nonnull OWLLiteral literal) {
        return new AutoValue_OWLLiteralData(literal);
    }

    @JsonCreator
    private static OWLLiteralData get(@JsonProperty("value") String value,
                                      @JsonProperty("lang") String lang,
                                      @JsonProperty(value = "datatype") String iri) {
        return get(new OWLLiteralImpl(value, lang, Optional.ofNullable(iri).map(IRI::create).map(OWLDatatypeImpl::new).orElse(null)));
    }

    @JsonIgnore
    @Nonnull
    @Override
    public abstract OWLLiteral getObject();

    @JsonIgnore
    @Override
    public PrimitiveType getType() {
        return PrimitiveType.LITERAL;
    }


    @JsonIgnore
    public OWLLiteral getLiteral() {
        return getObject();
    }

    @JsonProperty("value")
    private String getValue() {
        return getLiteral().getLiteral();
    }

    @JsonProperty("lang")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String getLangTag() {
        return getLiteral().getLang();
    }

    @JsonProperty("datatype")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String getDatatype() {
        var datatype = getLiteral().getDatatype();
        if(datatype.isRDFPlainLiteral()) {
            return "";
        }
        return datatype.getIRI().toString();
    }

    @JsonIgnore
    @Override
    public String getBrowserText() {
        OWLLiteral literal = getLiteral();
        return literal.getLiteral();
    }

    @JsonIgnore
    public String getUnquotedBrowserText() {
        return getBrowserText();
    }

    @JsonIgnore
    public String getLexicalForm() {
        return getLiteral().getLiteral();
    }

    @JsonIgnore
    public boolean hasLang() {
        return getLiteral().hasLang();
    }

    @JsonIgnore
    @Nonnull
    public String getLang() {
        return getLiteral().getLang();
    }

    @Override
    public <R, E extends Throwable> R accept(OWLPrimitiveDataVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public <R> R accept(OWLEntityVisitorEx<R> visitor, R defaultValue) {
        return defaultValue;
    }

    @Override
    public Optional<OWLAnnotationValue> asAnnotationValue() {
        return Optional.of(getLiteral());
    }

    @Override
    public Optional<OWLEntity> asEntity() {
        return Optional.empty();
    }

    @Override
    public Optional<OWLLiteral> asLiteral() {
        return Optional.of(getLiteral());
    }

    @JsonIgnore
    @Override
    public boolean isDeprecated() {
        return super.isDeprecated();
    }

    @JsonIgnore
    @Override
    public ImmutableList<ShortForm> getShortForms() {
        return ImmutableList.of();
    }
}
