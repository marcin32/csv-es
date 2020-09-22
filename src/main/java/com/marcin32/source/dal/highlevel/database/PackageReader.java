package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.dal.highlevel.table.TableReader;

public class PackageReader extends AbstractPackageDal {

    private final TableReader tableReader = new TableReader();

    //private final CsvReader csvReader = new CsvReader();

    //private final BloomCache bloomCache = new BloomCache();

    //private final FilesystemDal filesystemDal = new FilesystemDal();

 /*   public Stream<AbstractCsvEntity> readEntities(final String tableName) throws IOException {
        final String fileName = tableName.contains(TABLE_EXTENSION) ? tableName : tableName + TABLE_EXTENSION;
        if (getUpdateType().equals(UpdateType.DIRECTORY)) {
            final Optional<File> databaseFile = filesystemDal.getFile(this, fileName);
            if (databaseFile.isPresent()) {
                return csvReader.readCsv(databaseFile.get());
            }
        }
        return Stream.empty();
    }*/

    /*public <ENTITYTYPE> Stream<ENTITYTYPE> readEntities(final String tableName,
                                                        final Class<ENTITYTYPE> entityClass) throws IOException {
        return readEntitiesWithMetadata(tableName, entityClass)
                .map(AbstractSourceEntity::getEntity);
    }*/

  /*  public <ENTITYTYPE> Stream<AbstractSourceEntity<ENTITYTYPE>> readEntitiesWithMetadata(final String tableName,
                                                                                          final Class<ENTITYTYPE> entityClass) throws IOException {
        return readEntities(tableName)
                .map(abstractEntity -> TableReader.convertEntity(abstractEntity, entityClass))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }*/

   /* public <ENTITYTYPE> boolean isEntityAlreadyPersisted(final String tableName,
                                                  final ENTITYTYPE entity) throws IOException {

        final String content = gson.toJson(entity);

        if (!bloomCache.hasDatabasePopulated(tableName)) {
            synchronized (this) {
                if (!bloomCache.hasDatabasePopulated(tableName)) {
                    System.out.println("Populating cache for table: " + tableName);
                    readEntities(tableName)
                            .peek(element -> System.out.println(element.getShaContentHash()))
                            .forEach(element -> bloomCache.populateCache(tableName, element.getShaContentHash()));
                }
            }
        }

        final String contentHash = ShaUtil.shaHash(content);
        if (!bloomCache.shouldCheck(tableName, contentHash))
            return true;

        return readEntities(tableName)
                .anyMatch(abstractCsvEntity -> abstractCsvEntity.getShaContentHash().equals(contentHash));
    }*/
}
