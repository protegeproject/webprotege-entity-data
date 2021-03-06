
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
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;

import java.io.IOException;

import static edu.stanford.protege.webprotege.entity.PrimitiveType.DATA_TYPE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@JsonTest
@Import({WebProtegeJacksonApplication.class, WebProtegeCommonConfiguration.class})
public class OWLDatatypeData_TestCase {

    private OWLDatatypeData data;

    @Mock
    private OWLDatatype entity;

    @Autowired
    private JacksonTester<OWLEntityData> tester;

    @Autowired
    private OWLDataFactory dataFactory;

    private final String browserText = "The browserText";

    private ImmutableMap<DictionaryLanguage, String> shortForms;

    @BeforeEach
    public void setUp()
        throws Exception {
        shortForms = ImmutableMap.of(LocalNameDictionaryLanguage.get(), browserText);
        when(entity.getIRI()).thenReturn(IRI.create("http://example.org/x"));
        data = OWLDatatypeData.get(entity, shortForms);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        assertThrows(NullPointerException.class, () -> {
            OWLDatatypeData.get(null, shortForms);
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
        assertThat(data, is(OWLDatatypeData.get(entity, shortForms)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(data, is(Matchers.not(OWLDatatypeData.get(Mockito.mock(OWLDatatype.class), shortForms))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(data.hashCode(), is(OWLDatatypeData.get(entity, shortForms).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(data.toString(), startsWith("OWLDatatypeData"));
    }

    @Test
    public void should_getType() {
        assertThat(data.getType(), is(DATA_TYPE));
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
                    "@type"   : "DatatypeData",
                    "iri"     : "http://example.org/d",
                    "shortForms" : [
                        {
                            "dictionaryLanguage" : {
                                "type" : "LocalName"
                            },
                            "shortForm" : "i"
                        }
                    ]
                }
                """;
        var parsedContent = tester.parse(json);
        var parsedEntityData = parsedContent.getObject();
        var expectedClass = dataFactory.getOWLDatatype(IRI.create("http://example.org/d"));
        Assertions.assertThat(parsedEntityData.getEntity()).isEqualTo(expectedClass);
    }

    @Test
    public void shouldDeserializeFromJsonWithMissingShortForms() throws IOException {
        var json = """
                {
                    "@type"   : "DatatypeData",
                    "iri"     : "http://example.org/d"
                }
                """;
        var parsedContent = tester.parse(json);
        var parsedEntityData = parsedContent.getObject();
        assertThat(parsedEntityData.isDeprecated(), is(false));
        var expectedClass = dataFactory.getOWLDatatype(IRI.create("http://example.org/d"));
        Assertions.assertThat(parsedEntityData.getEntity()).isEqualTo(expectedClass);
    }

    @Test
    public void shouldDeserializeAsDeprecated() throws IOException {
        var json = """
                {
                    "@type"      : "DatatypeData",
                    "iri"        : "http://example.org/d",
                    "deprecated" : true
                }
                """;
        var parsedContent = tester.parse(json);
        var parsedEntityData = parsedContent.getObject();
        assertThat(parsedEntityData.isDeprecated(), is(true));
    }
}
