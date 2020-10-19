package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.model.PackageDescriptor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PackageReaderWrapper {

    private final PackageReader packageReader;

    private final PackageDescriptor packageDescriptor;
}
