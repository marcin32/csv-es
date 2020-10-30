package com.marcin32.source.model;

public class CsvEntry {

    private final Long timestamp;

    private final String uuid;

    private final String shaContentHash;

    private final String content;

    public CsvEntry(Long timestamp, String uuid, String shaContentHash, String content) {
        this.timestamp = timestamp;
        this.uuid = uuid;
        this.shaContentHash = shaContentHash;
        this.content = content;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getShaContentHash() {
        return this.shaContentHash;
    }

    public String getContent() {
        return this.content;
    }
}

