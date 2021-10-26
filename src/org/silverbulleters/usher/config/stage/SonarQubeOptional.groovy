/*
 * Vanessa-Usher
 * Copyright (C) 2019-2021 SilverBulleters, LLC - All Rights Reserved.
 * Unauthorized copying of this file in any way is strictly prohibited.
 * Proprietary and confidential.
 */
package org.silverbulleters.usher.config.stage

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonPropertyDescription

/**
 * Настройки этапа статического анализа для SonarQube
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class SonarQubeOptional extends BaseOptional {
  static final EMPTY = new SonarQubeOptional()

  @JsonPropertyDescription("Имя/метки агента для запуска этапа. Например, `sonar-scanner`")
  String agent = "any"

  @JsonPropertyDescription("Id утилиты sonar-scanner (глобальные инструменты Jenkins)")
  String toolId = "sonar-scanner"

  @JsonPropertyDescription("Id настроек сервера SonarQube")
  String serverId = "SonarQube"

  @JsonPropertyDescription("Режим отладки sonar-scanner")
  boolean debug = false

  @JsonPropertyDescription("Использовать ветки при анализе")
  boolean useBranch = false

  SonarQubeOptional() {
    name = 'SonarQube static analysis'
    id = 'sonar-scanner'
    timeout = 100
  }

}
