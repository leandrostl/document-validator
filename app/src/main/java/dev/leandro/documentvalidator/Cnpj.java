package dev.leandro.documentvalidator;

public class Cnpj extends CpfCnpjDocument {
  private static final int LENGTH = 14;
  private static final int MAX_WEIGHT = 9;
  private static final String NAME = "CNPJ";

  private String number;

  private Cnpj(String number) {
    this.number = unmask(number);
  }

  public Cnpj() {
  }

  @Override
  public Document of(String value) {
    return new Cnpj(value);
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
