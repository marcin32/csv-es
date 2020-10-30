package com.marcin32.source;

public class TestEntity1 {

    private String uuid;

    private String content;

    public TestEntity1(String uuid, String content) {
        this.uuid = uuid;
        this.content = content;
    }

    public TestEntity1() {
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getContent() {
        return this.content;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TestEntity1)) return false;
        final TestEntity1 other = (TestEntity1) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$uuid = this.getUuid();
        final Object other$uuid = other.getUuid();
        if (this$uuid == null ? other$uuid != null : !this$uuid.equals(other$uuid)) return false;
        final Object this$content = this.getContent();
        final Object other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TestEntity1;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $uuid = this.getUuid();
        result = result * PRIME + ($uuid == null ? 43 : $uuid.hashCode());
        final Object $content = this.getContent();
        result = result * PRIME + ($content == null ? 43 : $content.hashCode());
        return result;
    }

    public String toString() {
        return "TestEntity1(uuid=" + this.getUuid() + ", content=" + this.getContent() + ")";
    }
}
