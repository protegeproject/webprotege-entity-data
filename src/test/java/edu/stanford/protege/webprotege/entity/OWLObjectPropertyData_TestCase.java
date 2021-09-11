
package edu.stanford.protege.webprotege.entity;

import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.common.LocalNameDictionaryLanguage;
import edu.stanford.protege.webprotege.common.WebProtegeCommonConfiguration;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;

import java.io.IOException;

import static edu.stanford.protege.webprotege.entity.PrimitiveType.OBJECT_PROPERTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@JsonTest
@Import({WebProtegeJacksonApplication.class, WebProtegeCommonConfiguration.class})
public class OWLObjectPropertyData_TestCase {

    private OWLObjectPropertyData data;

    @Autowired
    private JacksonTester<OWLEntityData> tester;

    @Autowired
    private OWLDataFactory dataFactory;

    @Mock
    private OWLObjectProperty entity;

    private final String browserText = "The browserText";

    private ImmutableMap<DictionaryLanguage, String> shortForms;

    @BeforeEach
    public void setUp() {
        shortForms = ImmutableMap.of(LocalNameDictionaryLanguage.get(), browserText);
        when(entity.getIRI()).thenReturn(IRI.create("http://example.org/x"));
        data = OWLObjectPropertyData.get(entity, shortForms);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        assertThrows(NullPointerException.class, () -> {
            OWLObjectPropertyData.get(null, shortForms);
        });
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(data.getEntity(), is(this.entity));
    }

    @Test
    public void shouldReturnSupplied_browserText() {
        assertThat(data.getBrowserText(), is(this.browserText));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(data, is(data));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(data.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(data, is(OWLObjectPropertyData.get(entity, shortForms)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(data, is(not(OWLObjectPropertyData.get(Mockito.mock(OWLObjectProperty.class), shortForms))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(data.hashCode(), is(OWLObjectPropertyData.get(entity, shortForms).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(data.toString(), startsWith("OWLObjectPropertyData"));
    }

    @Test
    public void should_getType() {
        assertThat(data.getType(), is(OBJECT_PROPERTY));
    }

    @Test
    public void shouldReturn_false_For_isOWLAnnotationProperty() {
        assertThat(data.isOWLAnnotationProperty(), is(false));
    }

    @Test
    public void shouldSerializeToJson() throws IOException {
        var json = tester.write(data);
        Assertions.assertThat(json).hasJsonPath("entity");
        Assertions.assertThat(json).hasJsonPath("shortForms");
    }

    @Test
    public void shouldDeserializeFromJson() throws IOException {
        var json = """
                {
                    "@type"   : "OWLObjectPropertyData",
                    "entity" : {
                        "iri" : "http://example.org/A",
                        "@type" : "owl:ObjectProperty"
                    },
                    "shortForms" : [
                        {
                            "dictionaryLanguage" : {
                                "type" : "LocalName"
                            },
                            "shortForm" : "A"
                        }
                    ]
                }
                """;
        var parsedContent = tester.parse(json);
        var parsedEntityData = parsedContent.getObject();
        var expectedClass = dataFactory.getOWLObjectProperty(IRI.create("http://example.org/A"));
        Assertions.assertThat(parsedEntityData.getEntity()).isEqualTo(expectedClass);
    }
}
