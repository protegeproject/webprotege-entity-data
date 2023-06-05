
package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.common.WebProtegeCommonConfiguration;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.util.Optional;

import static edu.stanford.protege.webprotege.entity.PrimitiveType.LITERAL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JsonTest
@Import({WebProtegeJacksonApplication.class, WebProtegeCommonConfiguration.class})
public class OWLLiteralData_TestCase {

    private static final String THE_LANG = "thelang";

    private static final String THE_LITERAL = "TheLiteral";

    @Autowired
    private JacksonTester<OWLLiteralData> tester;

    @Autowired
    private OWLDataFactory dataFactory;

    private OWLLiteralData data;

    private OWLLiteral object;

    @BeforeEach
    public void setUp() {
        object = dataFactory.getOWLLiteral(THE_LITERAL, THE_LANG);
        data = OWLLiteralData.get(object);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldThrowNullPointerExceptionIf_object_IsNull() {
        assertThrows(NullPointerException.class, () -> {
            OWLLiteralData.get(null);
        });
    }

    @Test
    public void shouldReturnSupplied_object() {
        assertThat(data.getObject(), is(this.object));
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
        assertThat(data, is(OWLLiteralData.get(object)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_object() {
        assertThat(data, is(not(OWLLiteralData.get(Mockito.mock(OWLLiteral.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(data.hashCode(), is(OWLLiteralData.get(object).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(data.toString(), Matchers.startsWith("OWLLiteralData"));
    }

    @Test
    public void should_getType() {
        assertThat(data.getType(), is(LITERAL));
    }

    @Test
    public void should_getLiteral() {
        assertThat(data.getLiteral(), is(object));
    }

    @Test
    public void should_getBrowserText() {
        assertThat(data.getBrowserText(), is(THE_LITERAL));
    }

    @Test
    public void should_getUnquotedBrowserText() {
        assertThat(data.getUnquotedBrowserText(), is(THE_LITERAL));
    }

    @Test
    public void should_getLexicalForm() {
        assertThat(data.getLexicalForm(), is(THE_LITERAL));
    }

    @Test
    public void shouldReturn_true_For_hasLang() {
        assertThat(data.hasLang(), is(true));
    }

    @Test
    public void should_getLang() {
        assertThat(data.getLang(), is(THE_LANG));
    }

    @Test
    public void should_asAnnotationValue() {
        assertThat(data.asAnnotationValue(), is(Optional.of(object)));
    }



    @Test
    public void shouldSerializeToJson() throws IOException {
        var json = tester.write(data);
        System.out.println(json.getJson());
        Assertions.assertThat(json).extractingJsonPathStringValue("lang").isEqualTo(THE_LANG);
        Assertions.assertThat(json).extractingJsonPathStringValue("value").isEqualTo(THE_LITERAL);
    }

    @Test
    public void shouldDeserializeFromJson() throws IOException {
        var json = """
                {
                    "@type"   : "LiteralData",
                    "lang"   : "en",
                    "value"  : "Hello"
                }
                """;
        var parsedContent = tester.parse(json);
        var parsedEntityData = parsedContent.getObject();
        Assertions.assertThat(parsedEntityData.getLexicalForm()).isEqualTo("Hello");
    }
}
