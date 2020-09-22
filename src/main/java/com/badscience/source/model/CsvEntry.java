package com.badscience.source.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CsvEntry {

    private final Long timestamp;

    private final String uuid;

    private final String shaContentHash;

    private final String content;
}

