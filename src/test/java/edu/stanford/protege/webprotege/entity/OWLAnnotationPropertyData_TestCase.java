
package edu.stanford.protege.webprotege.entity;

import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.common.LocalNameDictionaryLanguage;
import edu.stanford.protege.webprotege.common.WebProtegeCommonConfiguration;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;

import java.io.IOException;

import static edu.stanford.protege.webprotege.entity.PrimitiveType.ANNOTATION_PROPERTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@JsonTest
@Import({WebProtegeJacksonApplication.class, WebProtegeCommonConfiguration.class})
public class OWLAnnotationPropertyData_TestCase {

    private OWLAnnotationPropertyData data;

    @Mock
    private OWLAnnotationProperty entity;

    @Autowired
    private JacksonTester<OWLEntityData> tester;

    @Autowired
    private OWLDataFactory dataFactory;

    private final String browserText = "The browserText";

    private ImmutableMap<DictionaryLanguage, String> shortForms;

    @BeforeEach
    public void setUp() {
        shortForms = ImmutableMap.of(LocalNameDictionaryLanguage.get(), browserText);
        when(entity.getIRI()).thenReturn(IRI.create("http://example.org/x"));
        data = OWLAnnotationPropertyData.get(entity, shortForms);
    }

    @SuppressWarnings("ConstantConditions" )
    @Test
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        assertThrows(NullPointerException.class, () -> {
            OWLAnnotationPropertyData.get(null, shortForms);
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
    @SuppressWarnings("ObjectEqualsNull" )
    public void shouldNotBeEqualToNull() {
        assertThat(data.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(data, is(OWLAnnotationPropertyData.get(entity, shortForms)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(data,
                   is(not(OWLAnnotationPropertyData.get(Mockito.mock(OWLAnnotationProperty.class), shortForms))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(data.hashCode(), is(OWLAnnotationPropertyData.get(entity, shortForms).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(data.toString(), Matchers.startsWith("OWLAnnotationPropertyData" ));
    }

    @Test
    public void shouldReturn_true_For_isOWLAnnotationProperty() {
        assertThat(data.isOWLAnnotationProperty(), is(true));
    }

    @Test
    public void should_getType() {
        assertThat(data.getType(), is(ANNOTATION_PROPERTY));
    }



    @Test
    public void shouldSerializeToJson() throws IOException {
        var json = tester.write(data);
        System.out.println(json.getJson());
        Assertions.assertThat(json).extractingJsonPathStringValue("iri").isEqualTo("http://example.org/x");
        Assertions.assertThat(json).hasJsonPath("shortForms");
    }

    @Test
    public void shouldDeserializeFromJson() throws IOException {
        var json = """
                {
                    "@type"   : "AnnotationPropertyData",
                    "iri"     : "http://example.org/p",
                    "shortForms" : [
                        {
                            "dictionaryLanguage" : {
                                "type" : "LocalName"
                            },
                            "shortForm" : "p"
                        }
                    ]
                }
                """;
        var parsedContent = tester.parse(json);
        var parsedEntityData = parsedContent.getObject();
        var expectedClass = dataFactory.getOWLAnnotationProperty(IRI.create("http://example.org/p"));
        Assertions.assertThat(parsedEntityData.getEntity()).isEqualTo(expectedClass);
    }

    @Test
    public void shouldDeserializeFromJsonWithMissingShortForms() throws IOException {
        var json = """
                {
                    "@type"   : "AnnotationPropertyData",
                    "iri"     : "http://example.org/p"
                }
                """;
        var parsedContent = tester.parse(json);
        var parsedEntityData = parsedContent.getObject();
        assertThat(parsedEntityData.isDeprecated(), is(false));
        var expectedClass = dataFactory.getOWLAnnotationProperty(IRI.create("http://example.org/p"));
        Assertions.assertThat(parsedEntityData.getEntity()).isEqualTo(expectedClass);
    }

    @Test
    public void shouldDeserializeAsDeprecated() throws IOException {
        var json = """
                {
                    "@type"      : "AnnotationPropertyData",
                    "iri"        : "http://example.org/p",
                    "deprecated" : true
                }
                """;
        var parsedContent = tester.parse(json);
        var parsedEntityData = parsedContent.getObject();
        assertThat(parsedEntityData.isDeprecated(), is(true));
    }
}
