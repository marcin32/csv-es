package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.base.Constants;
import com.marcin32.source.dal.highlevel.table.TableReader;
import com.marcin32.source.model.PackageDescriptor;
import com.marcin32.source.model.PackedTableMetadata;
import com.marcin32.source.model.SourceEntry;
import com.marcin32.source.model.csv.MetadataAdapter;
import com.marcin32.source.model.file.AbstractFile;
import com.marcin32.source.model.file.PackedFile;

import java.io.IOException;
import java.util.stream.Stream;

public class PackageReader implements IPackageDal {

    private final PackageDescriptor packageDescriptor;

    private final TableReader tableReader = new TableReader();
    public static final MetadataAdapter METADATA_FORMAT_ADAPTER = new MetadataAdapter();

    public PackageReader(final PackageDescriptor packageDescriptor) {
        this.packageDescriptor = packageDescriptor;
    }

    @Override
    public <ENTITYTYPE> Stream<SourceEntry<ENTITYTYPE>> readEntities(final Class<ENTITYTYPE> entityType) throws IOException {
        final AbstractFile tableFile = getTableFile(entityType);
        return tableReader.readEntities(tableFile, entityType);
    }

    @Override
    public <ENTITYTYPE> Stream<String> readUuidsOfTimestampedEntities(final Class<ENTITYTYPE> entityType) {
        return null;
    }

    @Override
    public <ENTITYTYPE> boolean checkWhetherPackageContainsEntity(final ENTITYTYPE entity) {
        return false;
    }

    @Override
    public <ENTITYTYPE> boolean numberOfEntities(final Class<ENTITYTYPE> entity) {
        return false;
    }

    @Override
    public Stream<PackedTableMetadata> listTables() {
        try {
            final AbstractFile metadataFile = packageDescriptor.getMetadataFile();
            return tableReader.readEntities(metadataFile, METADATA_FORMAT_ADAPTER);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }

    public <ENTITYTYPE> AbstractFile getTableFile(final Class<ENTITYTYPE> entityType) {
        return listTables()
                .filter(tableMetadata -> tableMetadata.getClassName().equals(entityType.getSimpleName()+ Constants.TABLE_EXTENSION))
                .findFirst()
                .map(this::mapTableMetadataFile)
                .orElseThrow();
    }

    private AbstractFile mapTableMetadataFile(final PackedTableMetadata tableMetadataFile) {
        final PackedFile packedFile = new PackedFile(tableMetadataFile.getClassName(), packageDescriptor.getBasePathWithArchiveName());
        return packedFile;
    }

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
