package org.larik.three.domain.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SelicValue(@JsonProperty("valor") String value) {}
