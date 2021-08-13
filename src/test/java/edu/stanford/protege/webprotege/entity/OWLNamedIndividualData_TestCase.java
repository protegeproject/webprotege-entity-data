
package edu.stanford.protege.webprotege.entity;

import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.common.LocalNameDictionaryLanguage;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static edu.stanford.protege.webprotege.entity.PrimitiveType.NAMED_INDIVIDUAL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@JsonTest
public class OWLNamedIndividualData_TestCase {

    private OWLNamedIndividualData oWLNamedIndividualData;

    @Mock
    private OWLNamedIndividual entity;

    private final String browserText = "The browserText";

    private ImmutableMap<DictionaryLanguage, String> shortForms;

    @BeforeEach
    public void setUp() {
        shortForms = ImmutableMap.of(LocalNameDictionaryLanguage.get(), browserText);
        when(entity.getIRI()).thenReturn(IRI.create("http://example.org/x"));
        oWLNamedIndividualData = OWLNamedIndividualData.get(entity, shortForms);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        assertThrows(NullPointerException.class, () -> {
            OWLNamedIndividualData.get(null, shortForms);
        });
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(oWLNamedIndividualData.getEntity(), is(this.entity));
    }

    @Test
    public void shouldReturnSupplied_browserText() {
        assertThat(oWLNamedIndividualData.getBrowserText(), is(this.browserText));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(oWLNamedIndividualData, is(oWLNamedIndividualData));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(oWLNamedIndividualData.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(oWLNamedIndividualData, is(OWLNamedIndividualData.get(entity, shortForms)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(oWLNamedIndividualData, is(not(OWLNamedIndividualData.get(Mockito.mock(OWLNamedIndividual.class), shortForms))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(oWLNamedIndividualData.hashCode(), is(OWLNamedIndividualData.get(entity, shortForms).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(oWLNamedIndividualData.toString(), startsWith("OWLNamedIndividualData"));
    }

    @Test
    public void should_getType() {
        assertThat(oWLNamedIndividualData.getType(), is(NAMED_INDIVIDUAL));
    }
}
