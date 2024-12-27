/*
 * Vanessa-Usher
 * Copyright (C) 2019-2022 SilverBulleters, LLC - All Rights Reserved.
 * Unauthorized copying of this file in any way is strictly prohibited.
 * Proprietary and confidential.
 */
package org.silverbulleters.usher.config.stage

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import org.silverbulleters.usher.config.additional.ExtensionSource

/**
 * Настройки этапа сборки CF на поставке
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class BuildOptional extends BaseOptional {
  @JsonPropertyDescription("Путь к каталогу сборки (*.cf, *.cfe)")
  String distPath = './build'

  @JsonPropertyDescription("Прерывать этап если статус сборки равен FAILURE")
  boolean errorIfJobStatusOfFailure = false

  BuildOptional() {
    name = 'Build'
    id = 'build'
    timeout = 100
  }

}
