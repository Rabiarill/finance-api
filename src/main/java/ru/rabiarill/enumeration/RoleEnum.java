package ru.rabiarill.enumeration;

public enum RoleEnum {

   ROLE_ADMIN("ROLE_ADMIN"),
   ROLE_USER("ROLE_USER");

   private String value;

   RoleEnum(String value) {
      this.value = value;
   }

   public String getValue() {
      return value;
   }
}
