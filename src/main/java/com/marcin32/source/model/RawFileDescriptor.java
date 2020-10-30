package com.marcin32.source.model;

import java.util.concurrent.atomic.LongAdder;

public class RawFileDescriptor {

    private final String fileName;

    private LongAdder numberOfLines;

    public RawFileDescriptor(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public LongAdder getNumberOfLines() {
        return this.numberOfLines;
    }

    public void setNumberOfLines(LongAdder numberOfLines) {
        this.numberOfLines = numberOfLines;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof RawFileDescriptor)) return false;
        final RawFileDescriptor other = (RawFileDescriptor) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$fileName = this.getFileName();
        final Object other$fileName = other.getFileName();
        if (this$fileName == null ? other$fileName != null : !this$fileName.equals(other$fileName)) return false;
        final Object this$numberOfLines = this.getNumberOfLines();
        final Object other$numberOfLines = other.getNumberOfLines();
        if (this$numberOfLines == null ? other$numberOfLines != null : !this$numberOfLines.equals(other$numberOfLines))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof RawFileDescriptor;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $fileName = this.getFileName();
        result = result * PRIME + ($fileName == null ? 43 : $fileName.hashCode());
        final Object $numberOfLines = this.getNumberOfLines();
        result = result * PRIME + ($numberOfLines == null ? 43 : $numberOfLines.hashCode());
        return result;
    }

    public String toString() {
        return "RawFileDescriptor(fileName=" + this.getFileName() + ", numberOfLines=" + this.getNumberOfLines() + ")";
    }
}