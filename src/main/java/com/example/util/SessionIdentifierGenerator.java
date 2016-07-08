package com.example.util;

import java.security.SecureRandom;
import java.math.BigInteger;

public final class SessionIdentifierGenerator {
  private SecureRandom random = new SecureRandom();

  public String nextSessionId() {
    return new BigInteger(256, random).toString(256);
  }
}