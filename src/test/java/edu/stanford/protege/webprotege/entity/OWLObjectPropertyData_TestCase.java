
package edu.stanford.protege.webprotege.entity;

import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.common.LocalNameDictionaryLanguage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static edu.stanford.protege.webprotege.entity.PrimitiveType.OBJECT_PROPERTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@JsonTest
public class OWLObjectPropertyData_TestCase {

    private OWLObjectPropertyData oWLObjectPropertyData;

    @Mock
    private OWLObjectProperty entity;

    private final String browserText = "The browserText";

    private ImmutableMap<DictionaryLanguage, String> shortForms;

    @BeforeEach
    public void setUp() {
        shortForms = ImmutableMap.of(LocalNameDictionaryLanguage.get(), browserText);
        when(entity.getIRI()).thenReturn(IRI.create("http://example.org/x"));
        oWLObjectPropertyData = OWLObjectPropertyData.get(entity, shortForms);
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
        assertThat(oWLObjectPropertyData.getEntity(), is(this.entity));
    }

    @Test
    public void shouldReturnSupplied_browserText() {
        assertThat(oWLObjectPropertyData.getBrowserText(), is(this.browserText));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(oWLObjectPropertyData, is(oWLObjectPropertyData));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(oWLObjectPropertyData.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(oWLObjectPropertyData, is(OWLObjectPropertyData.get(entity, shortForms)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(oWLObjectPropertyData, is(not(OWLObjectPropertyData.get(Mockito.mock(OWLObjectProperty.class), shortForms))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(oWLObjectPropertyData.hashCode(), is(OWLObjectPropertyData.get(entity, shortForms).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(oWLObjectPropertyData.toString(), startsWith("OWLObjectPropertyData"));
    }

    @Test
    public void should_getType() {
        assertThat(oWLObjectPropertyData.getType(), is(OBJECT_PROPERTY));
    }

    @Test
    public void shouldReturn_false_For_isOWLAnnotationProperty() {
        assertThat(oWLObjectPropertyData.isOWLAnnotationProperty(), is(false));
    }

}
