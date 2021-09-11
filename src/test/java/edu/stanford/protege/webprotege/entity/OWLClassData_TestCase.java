
package edu.stanford.protege.webprotege.entity;

import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.common.LocalNameDictionaryLanguage;
import edu.stanford.protege.webprotege.common.WebProtegeCommonConfiguration;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;

import java.io.IOException;

import static edu.stanford.protege.webprotege.entity.PrimitiveType.CLASS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@JsonTest
@Import({WebProtegeJacksonApplication.class, WebProtegeCommonConfiguration.class})
public class OWLClassData_TestCase {

    private OWLClassData clsData;

    private OWLClass entity;

    private final String browserText = "The browserText";

    private ImmutableMap<DictionaryLanguage, String> shortForms;

    @Autowired
    OWLDataFactory dataFactory;

    @Autowired
    JacksonTester<OWLClassData> tester;

    @BeforeEach
    public void setUp() {
        entity = TestUtils.newOWLClass();
        shortForms = ImmutableMap.of(LocalNameDictionaryLanguage.get(), browserText);
        clsData = OWLClassData.get(entity, shortForms);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        assertThrows(NullPointerException.class, () -> {
            OWLClassData.get(null, shortForms);
        });
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(clsData.getEntity(), is(this.entity));
    }

    @Test
    public void shouldReturnSupplied_browserText() {
        assertThat(clsData.getBrowserText(), is(this.browserText));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(clsData, is(clsData));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(clsData.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(clsData, is(OWLClassData.get(entity, shortForms)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(clsData, is(not(OWLClassData.get(TestUtils.newOWLClass(), shortForms))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(clsData.hashCode(), is(OWLClassData.get(entity, shortForms).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(clsData.toString(), startsWith("OWLClassData"));
    }

    @Test
    public void should_getType() {
        assertThat(clsData.getType(), equalTo(CLASS));
    }

    @Test
    public void shouldSerializeToJson() throws IOException {
        var json = tester.write(clsData);
        Assertions.assertThat(json).hasJsonPath("entity");
        Assertions.assertThat(json).hasJsonPath("shortForms");
    }

    @Test
    public void shouldDeserializeFromJson() throws IOException {
        var json = """
                {
                    "@type"   : "OWLClassData",
                    "entity" : {
                        "iri" : "http://example.org/A",
                        "@type" : "Class"
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
        var expectedClass = dataFactory.getOWLClass(IRI.create("http://example.org/A"));
        Assertions.assertThat(parsedEntityData.getEntity()).isEqualTo(expectedClass);
    }
}
