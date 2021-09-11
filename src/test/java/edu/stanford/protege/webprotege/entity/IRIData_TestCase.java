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
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-09-11
 */
@JsonTest
@Import({WebProtegeJacksonApplication.class, WebProtegeCommonConfiguration.class})
public class IRIData_TestCase {


    private IRIData data;

    private IRI iri = IRI.create("http://example.org/i");

    @Autowired
    private JacksonTester<IRIData> tester;

    @Autowired
    private OWLDataFactory dataFactory;

    private final String browserText = "i";

    private ImmutableMap<DictionaryLanguage, String> shortForms;

    @BeforeEach
    public void setUp() {
        shortForms = ImmutableMap.of(LocalNameDictionaryLanguage.get(), browserText);
        data = IRIData.get(iri, shortForms);
    }


    @Test
    public void shouldSerializeToJson() throws IOException {
        var json = tester.write(data);
        System.out.println(json.getJson());
        Assertions.assertThat(json).extractingJsonPathStringValue("iri").isEqualTo("http://example.org/i");
        Assertions.assertThat(json).hasJsonPath("shortForms");
    }

    @Test
    public void shouldDeserializeFromJson() throws IOException {
        var json = """
                {
                    "@type"   : "IRIData",
                    "iri"     : "http://example.org/i",
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
        var parsedIriData = parsedContent.getObject();
        Assertions.assertThat(parsedIriData.getIri().toString()).isEqualTo("http://example.org/i");
        Assertions.assertThat(parsedIriData.getShortFormsMap()).isEqualTo(shortForms);
    }

    @Test
    public void shouldDeserializeFromJsonWithMissingShortForms() throws IOException {
        var json = """
                {
                    "@type"   : "IRIData",
                    "iri"     : "http://example.org/i"
                }
                """;
        var parsedContent = tester.parse(json);
        var parsedIriData = parsedContent.getObject();
        Assertions.assertThat(parsedIriData.getIri().toString()).isEqualTo("http://example.org/i");
    }

    @Test
    public void shouldDeserializeAsDeprecated() throws IOException {
        var json = """
                {
                    "@type"      : "IRIData",
                    "iri"        : "http://example.org/i",
                    "deprecated" : true
                }
                """;
        var parsedContent = tester.parse(json);
        var parsedIriData = parsedContent.getObject();
        Assertions.assertThat(parsedIriData.getIri().toString()).isEqualTo("http://example.org/i");
        Assertions.assertThat(parsedIriData.isDeprecated()).isTrue();
    }
}
