package com.hengthay.ecommercebackendspring.users;

public class UserAccessDeniedException extends RuntimeException {
  public UserAccessDeniedException(String message) {
    super(message);
  }
}
