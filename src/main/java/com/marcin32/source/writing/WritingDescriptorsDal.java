package com.marcin32.source.writing;

public class WritingDescriptorsDal {

//    public WritingPackageDescriptor getDescriptorForWriting(final Path baseDirectoryPath) {
//        final Long currentTime = System.currentTimeMillis();
//        return getWritingUpdateDescriptorInternal(currentTime, baseDirectoryPath, PackageScope.FULL_PACKAGE);
//    }
//
//    WritingPackageDescriptor getDescriptorForWritingDelta(final WritingPackageDescriptor currentWritingDescriptor) {
//        final Path parent = currentWritingDescriptor.getPath().getParent();
//        return getWritingUpdateDescriptorInternal(currentWritingDescriptor.getTimestamp(),
//                parent, PackageScope.DELTA_PACKAGE);
//    }
//
//    WritingPackageDescriptor getWritingUpdateDescriptorInternal(final Long timestamp,
//                                                                final Path baseDirectoryPath,
//                                                                final PackageScope packageScope) {
//        final Path base = baseDirectoryPath.resolve(timestamp + "-" + packageScope.toString());
//        return new WritingPackageDescriptor(timestamp, base, packageScope, PackageType.DIRECTORY);
//    }
}
