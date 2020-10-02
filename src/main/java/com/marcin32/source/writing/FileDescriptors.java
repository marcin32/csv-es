package com.marcin32.source.writing;

public class FileDescriptors {

//    private final Map<String, BufferedWriter> fileToDescriptor =
//            new HashMap<>();
//
//    public Set<String> getFileNames() {
//        return fileToDescriptor.keySet();
//    }
//
//    public void closeAllFiles() {
//        fileToDescriptor.values()
//                .forEach(this::closeWriter);
//    }
//
//    synchronized BufferedWriter getWriter(final String fileName,
//                                          final PackageDescriptor databaseInfo) throws IOException {
//        if (fileToDescriptor.containsKey(fileName)) {
//            return fileToDescriptor.get(fileName);
//        }
//
//        final BufferedWriter bufferedWriter = createBufferedWriter(fileName,
//                databaseInfo);
//        fileToDescriptor.put(fileName, bufferedWriter);
//        return bufferedWriter;
//    }
//
//    private BufferedWriter createBufferedWriter(final String fileName,
//                                                final PackageDescriptor databaseInfo) throws IOException {
//        final File directory = Paths.get(databaseInfo.getPath().toString()).toFile();
//        if(!directory.exists()) {
//            directory.mkdirs();
//        }
//        final Path fileNamePath = Paths.get(databaseInfo.getPath().toString(),
//                fileName);
//        //final FileWriter fileWriter = new FileWriter(fileName.toString(), true);
//        //return new BufferedWriter(fileWriter);
//        return Files.newBufferedWriter(fileNamePath, StandardCharsets.UTF_8, StandardOpenOption.APPEND,
//                StandardOpenOption.CREATE);
//    }
//
//    private String updateDirectory(final PackageDescriptor databaseInfo) {
//        return databaseInfo.getTimestamp() + "-" + databaseInfo.getPackageScope().toString();
//    }
//
//    private void closeWriter(final BufferedWriter writer) {
//        try {
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
