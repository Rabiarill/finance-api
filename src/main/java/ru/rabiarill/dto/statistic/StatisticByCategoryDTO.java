package ru.rabiarill.dto.statistic;

import java.math.BigDecimal;

public class StatisticByCategoryDTO {

   private String category;
   private BigDecimal total;
   private BigDecimal average;
   private BigDecimal max;
   private BigDecimal min;

   public StatisticByCategoryDTO(String category, BigDecimal total, BigDecimal average, BigDecimal max, BigDecimal min) {
      this.category = category;
      this.total = total;
      this.average = average;
      this.max = max;
      this.min = min;
   }

   public String getCategory() {
      return category;
   }

   public void setCategory(String category) {
      this.category = category;
   }

   public BigDecimal getTotal() {
      return total;
   }

   public void setTotal(BigDecimal total) {
      this.total = total;
   }

   public BigDecimal getAverage() {
      return average;
   }

   public void setAverage(BigDecimal average) {
      this.average = average;
   }

   public BigDecimal getMax() {
      return max;
   }

   public void setMax(BigDecimal max) {
      this.max = max;
   }

   public BigDecimal getMin() {
      return min;
   }

   public void setMin(BigDecimal min) {
      this.min = min;
   }

}
