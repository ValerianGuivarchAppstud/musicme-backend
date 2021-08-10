package com.vguivarc.musicme.backend.config.properties

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.context.annotation.Configuration
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Configuration
class ProjectProperties {

    @JsonIgnore
    @NotNull
    @Valid
    @NotBlank
    var name: String = "Swag"

    @NotNull
    @Valid
    @NotBlank
    var contact: String = "contact@swag.com"

    @JsonIgnore
    @NotNull
    @Valid
    @NotBlank
    var administrationUrl: String = "https://admin.swag.com"
}
