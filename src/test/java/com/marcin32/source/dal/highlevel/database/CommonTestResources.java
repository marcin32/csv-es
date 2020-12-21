package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.base.PackageScope;
import com.marcin32.source.base.PackageType;
import com.marcin32.source.model.PackageDescriptor;
import java.io.File;
import java.nio.file.Path;

public final class CommonTestResources {

  static PackageDescriptor get2345Package() {
    final Path path = new File(ClassLoader.getSystemResource("updates1/").getPath()).toPath();
    return new PackageDescriptor(2345L, PackageScope.DELTA_PACKAGE, PackageType.ARCHIVE, path);
  }

  static PackageDescriptor get1234Package() {
    final Path path = new File(ClassLoader.getSystemResource("updates1/").getPath()).toPath();
    return new PackageDescriptor(1234L, PackageScope.FULL_PACKAGE, PackageType.DIRECTORY, path);
  }
}
