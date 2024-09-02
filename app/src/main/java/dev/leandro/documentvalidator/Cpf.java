package dev.leandro.documentvalidator;

public class Cpf extends CpfCnpjDocument {
  private static final int LENGTH = 11;
  private static final int MAX_WEIGHT = 11;
  private static final String NAME = "CPF";

  private String number;

  public Cpf() {}

  private Cpf(String number) {
    this.number = unmask(number);
  }

  @Override
  public Document of(String value) {
    return new Cpf(value);
  }

  @Override
  public int unmaskedLength() {
    return LENGTH;
  }

  @Override
  public String number() {
    return number;
  }

  @Override
  public String name() {
    return NAME;
  }

  @Override
  protected int getMaxWeight() {
    return MAX_WEIGHT;
  }
}
