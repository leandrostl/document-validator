package dev.leandro.documentvalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DocumentValidatorTest {

  private final DocumentValidator validator = new DocumentValidator();

  @Test
  @DisplayName("Should accept a valid unmasked CPF")
  void shouldAcceptNumberCpf() {
    assertTrue(validator.validate("69052448051"));
  }

  @Test
  @DisplayName("Should accept a valid masked CPF")
  void shouldAcceptAValidMaskedCpf() {
    assertTrue(validator.validate("666.818.440-57"));
  }

  @Test
  @DisplayName("Should accept a valid unmasked CNPJ")
  void shouldAcceptNumberCnpj() {
    assertTrue(validator.validate("18386029000119"));
  }

  @Test
  @DisplayName("Should accept a valid masked CNPJ")
  void shouldAcceptAValidMaskedCnpj() {
    assertTrue(validator.validate("51.793.992/0001-92"));
  }

  @ParameterizedTest(name = "Should throw an exception for blank documents")
  @NullSource
  @EmptySource
  void shouldThrowAnExceptionForBlankDocuments(String document) {
    final var exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(document));
    assertEquals("The document must be provided.", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw an exception for a not valid document length")
  void shouldThrowAnExceptionForNotValidDocumentLength() {
    final var exception = assertThrows(IllegalArgumentException.class, () -> validator.validate("18386029"));
    assertEquals("The document length without mask doesn't match any of accepted documents length: CPF (11), CNPJ (14).",
        exception.getMessage());
  }

  @Test
  @DisplayName("Should throw an exception for a uncheckable CPF")
  void shouldThrowAnExceptionForCpfWithInvalidCheckDigits() {
    final var exception = assertThrows(IllegalArgumentException.class, () -> validator.validate("666.818.440-58"));
    assertEquals("The check digits of the CPF doesn't match to expected.", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw an exception for a uncheckable CNPJ")
  void shouldThrowAnExceptionForCnpjWithInvalidCheckDigits() {
    final var exception = assertThrows(IllegalArgumentException.class, () -> validator.validate("51.793.992/0001-93"));
    assertEquals("The check digits of the CNPJ doesn't match to expected.", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw an exception for invalid CPF content")
  void shouldThrowAnExceptionForCpfWithInvalidContent() {
    final var exception = assertThrows(IllegalArgumentException.class, () -> validator.validate("66b.818.44a-57"));
    assertEquals("The CPF should be numeric only.", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw an exception for invalid CNPJ content")
  void shouldThrowAnExceptionForCnpjWithInvalidContent() {
    final var exception = assertThrows(IllegalArgumentException.class, () -> validator.validate("51.793.9AB/0001-93"));
    assertEquals("The CNPJ should be numeric only.", exception.getMessage());
  }
}
