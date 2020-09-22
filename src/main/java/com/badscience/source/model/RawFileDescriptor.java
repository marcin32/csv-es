package com.badscience.source.model;

import lombok.Data;

import java.util.concurrent.atomic.LongAdder;

@Data
public class RawFileDescriptor {

    private final String fileName;

    private LongAdder numberOfLines;
}