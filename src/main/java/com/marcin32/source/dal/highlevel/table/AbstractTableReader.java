package com.marcin32.source.dal.highlevel.table;

import com.google.gson.Gson;
import com.marcin32.source.model.file.AbstractFile;
import com.marcin32.source.utils.ShaUtil;

public abstract class AbstractTableReader implements ITableReader {
    final static Gson gson = new Gson();

    @Override
    public <ENTITYTYPE> boolean checkWhetherTableContainsEntity(final ENTITYTYPE entity, final AbstractFile file) {
        final String shaHash = getEntityHash(entity);
        return checkWhetherTableContainsEntity(shaHash, entity.getClass(), file);
    }

    <ENTITYTYPE> String getEntityHash(final ENTITYTYPE entity) {
        return ShaUtil.shaHash(gson.toJson(entity));
    }
}
