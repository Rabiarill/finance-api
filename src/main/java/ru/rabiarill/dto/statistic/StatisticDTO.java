package ru.rabiarill.dto.statistic;

import java.util.List;

public class StatisticDTO {

   private List<StatisticByCategoryDTO> statistic;

   public StatisticDTO(List<StatisticByCategoryDTO> statistic) {
      this.statistic = statistic;
   }

   public List<StatisticByCategoryDTO> getStatistic() {
      return statistic;
   }

   public void setStatistic(List<StatisticByCategoryDTO> statistic) {
      this.statistic = statistic;
   }

}
