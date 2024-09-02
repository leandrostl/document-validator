package dev.leandro.documentvalidator;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

public abstract class CpfCnpjDocument implements Document {
  private static final int CHECK_DIGITS_LENGTH = 2;
  private static final int MIN_WEIGHT = 2;

  protected abstract int getMaxWeight();

  @Override
  public boolean canValidate() {
    return StringUtils.length(number()) == unmaskedLength();
  }

  @Override
  public boolean validate() {
    final var number = Optional.of(number())
        .filter(StringUtils::isNumeric)
        .orElseThrow(() -> new IllegalArgumentException("The " + name() + " should be numeric only."));
    return Optional.of(number)
        .filter(this::notAllDigitsAreEqual)
        .filter(this::areCheckDigitsValid)
        .map(document -> true)
        .orElseThrow(() -> new IllegalArgumentException("The check digits of the " + name() + " doesn't match to expected."));
  }

  private boolean notAllDigitsAreEqual(String document) {
    return document.chars().distinct().count() != 1;
  }

  private boolean areCheckDigitsValid(String document) {
    return getCheckDigits(document, getMaxWeight()).equals(getDocumentCheckDigits(document));
  }

  private String getDocumentCheckDigits(String document) {
    return document.substring(getBaseDigitsLength(document));
  }

  private int getBaseDigitsLength(String document) {
    return document.length() - CHECK_DIGITS_LENGTH;
  }

  private String getCheckDigits(String document, int maxWeight) {
    final int lengthWithoutCheckDigits = getBaseDigitsLength(document);

    int firstWeightedSum = 0;
    int secondWeightedSum = 0;
    for (int i = 0; i < lengthWithoutCheckDigits; i++) {
      final int digit = Character.getNumericValue(document.charAt(i));
      final int maxIndex = lengthWithoutCheckDigits - 1;
      final int reverseIndex = maxIndex - i;
      firstWeightedSum += digit * calculateWeight(reverseIndex, maxWeight);
      // Index is incremented, starting from 3, skipping first check digit.
      // The first part will be added later as the calculated first check digit times its corresponding weight.
      secondWeightedSum += digit * calculateWeight(reverseIndex + 1, maxWeight);
    }

    final int firstDigit = getCheckDigit(firstWeightedSum);
    // Add the first part as the first check digit times the first weight.
    secondWeightedSum += MIN_WEIGHT * firstDigit;
    final int secondDigit = getCheckDigit(secondWeightedSum);

    return String.valueOf(firstDigit) + secondDigit;
  }

  private int calculateWeight(int complementaryIndex, int maxWeight) {
    return complementaryIndex % (maxWeight - 1) + MIN_WEIGHT;
  }

  private int getCheckDigit(int weightedSum) {
    final var checkDigit = enhanceCollisionAvoidance(weightedSum);
    return checkDigit > 9 ? 0 : checkDigit;
  }

  private int enhanceCollisionAvoidance(int weightedSum) {
    final var weightSumLimit = 11;
    return weightSumLimit - weightedSum % weightSumLimit;
  }

  protected String unmask(String document) {
    return document.replaceAll("[.\\-/]", "");
  }
}
