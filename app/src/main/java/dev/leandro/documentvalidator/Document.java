package dev.leandro.documentvalidator;

public interface Document {

  String name();

  String number();

  int unmaskedLength();

  Document of(String value);

  boolean canValidate();

  boolean validate();

}
