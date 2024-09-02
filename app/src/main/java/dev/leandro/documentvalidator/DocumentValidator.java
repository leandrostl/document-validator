package dev.leandro.documentvalidator;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class DocumentValidator {
  private static final String EXISTENCE_ERROR = "The document must be provided.";
  private static final List<Document> ACCEPTED_DOCUMENTS = List.of(new Cpf(), new Cnpj());
  private static final String ACCEPTED_DOCUMENTS_LENGTH = ACCEPTED_DOCUMENTS.stream()
      .map(doc -> doc.name() + " (" + doc.unmaskedLength() + ")")
      .collect(Collectors.joining(", ")) + ".";
  private static final String LENGTH_ERROR = "The document length without mask doesn't match any of accepted documents length: "
      + ACCEPTED_DOCUMENTS_LENGTH;

  public boolean validate(String document) {
    if (StringUtils.isBlank(document)) {
      throw new IllegalArgumentException(EXISTENCE_ERROR);
    }

    return ACCEPTED_DOCUMENTS.stream()
        .map(doc -> doc.of(document))
        .filter(Document::canValidate)
        .map(Document::validate)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(LENGTH_ERROR));
  }
}
