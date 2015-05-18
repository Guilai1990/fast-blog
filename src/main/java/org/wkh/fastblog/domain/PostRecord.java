/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package org.wkh.fastblog.domain;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class PostRecord extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"PostRecord\",\"namespace\":\"org.wkh.fastblog.domain\",\"fields\":[{\"name\":\"id\",\"type\":\"string\"},{\"name\":\"created_at\",\"type\":\"long\"},{\"name\":\"published\",\"type\":\"boolean\"},{\"name\":\"published_at\",\"type\":[\"null\",\"long\"]},{\"name\":\"title\",\"type\":\"string\"},{\"name\":\"body\",\"type\":\"string\"},{\"name\":\"summary\",\"type\":\"string\"},{\"name\":\"slug\",\"type\":\"string\"},{\"name\":\"stored_in_rdbms\",\"type\":\"boolean\"},{\"name\":\"soft_deleted\",\"type\":\"boolean\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.lang.CharSequence id;
  @Deprecated public long created_at;
  @Deprecated public boolean published;
  @Deprecated public java.lang.Long published_at;
  @Deprecated public java.lang.CharSequence title;
  @Deprecated public java.lang.CharSequence body;
  @Deprecated public java.lang.CharSequence summary;
  @Deprecated public java.lang.CharSequence slug;
  @Deprecated public boolean stored_in_rdbms;
  @Deprecated public boolean soft_deleted;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public PostRecord() {}

  /**
   * All-args constructor.
   */
  public PostRecord(java.lang.CharSequence id, java.lang.Long created_at, java.lang.Boolean published, java.lang.Long published_at, java.lang.CharSequence title, java.lang.CharSequence body, java.lang.CharSequence summary, java.lang.CharSequence slug, java.lang.Boolean stored_in_rdbms, java.lang.Boolean soft_deleted) {
    this.id = id;
    this.created_at = created_at;
    this.published = published;
    this.published_at = published_at;
    this.title = title;
    this.body = body;
    this.summary = summary;
    this.slug = slug;
    this.stored_in_rdbms = stored_in_rdbms;
    this.soft_deleted = soft_deleted;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    case 1: return created_at;
    case 2: return published;
    case 3: return published_at;
    case 4: return title;
    case 5: return body;
    case 6: return summary;
    case 7: return slug;
    case 8: return stored_in_rdbms;
    case 9: return soft_deleted;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = (java.lang.CharSequence)value$; break;
    case 1: created_at = (java.lang.Long)value$; break;
    case 2: published = (java.lang.Boolean)value$; break;
    case 3: published_at = (java.lang.Long)value$; break;
    case 4: title = (java.lang.CharSequence)value$; break;
    case 5: body = (java.lang.CharSequence)value$; break;
    case 6: summary = (java.lang.CharSequence)value$; break;
    case 7: slug = (java.lang.CharSequence)value$; break;
    case 8: stored_in_rdbms = (java.lang.Boolean)value$; break;
    case 9: soft_deleted = (java.lang.Boolean)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'id' field.
   */
  public java.lang.CharSequence getId() {
    return id;
  }

  /**
   * Sets the value of the 'id' field.
   * @param value the value to set.
   */
  public void setId(java.lang.CharSequence value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'created_at' field.
   */
  public java.lang.Long getCreatedAt() {
    return created_at;
  }

  /**
   * Sets the value of the 'created_at' field.
   * @param value the value to set.
   */
  public void setCreatedAt(java.lang.Long value) {
    this.created_at = value;
  }

  /**
   * Gets the value of the 'published' field.
   */
  public java.lang.Boolean getPublished() {
    return published;
  }

  /**
   * Sets the value of the 'published' field.
   * @param value the value to set.
   */
  public void setPublished(java.lang.Boolean value) {
    this.published = value;
  }

  /**
   * Gets the value of the 'published_at' field.
   */
  public java.lang.Long getPublishedAt() {
    return published_at;
  }

  /**
   * Sets the value of the 'published_at' field.
   * @param value the value to set.
   */
  public void setPublishedAt(java.lang.Long value) {
    this.published_at = value;
  }

  /**
   * Gets the value of the 'title' field.
   */
  public java.lang.CharSequence getTitle() {
    return title;
  }

  /**
   * Sets the value of the 'title' field.
   * @param value the value to set.
   */
  public void setTitle(java.lang.CharSequence value) {
    this.title = value;
  }

  /**
   * Gets the value of the 'body' field.
   */
  public java.lang.CharSequence getBody() {
    return body;
  }

  /**
   * Sets the value of the 'body' field.
   * @param value the value to set.
   */
  public void setBody(java.lang.CharSequence value) {
    this.body = value;
  }

  /**
   * Gets the value of the 'summary' field.
   */
  public java.lang.CharSequence getSummary() {
    return summary;
  }

  /**
   * Sets the value of the 'summary' field.
   * @param value the value to set.
   */
  public void setSummary(java.lang.CharSequence value) {
    this.summary = value;
  }

  /**
   * Gets the value of the 'slug' field.
   */
  public java.lang.CharSequence getSlug() {
    return slug;
  }

  /**
   * Sets the value of the 'slug' field.
   * @param value the value to set.
   */
  public void setSlug(java.lang.CharSequence value) {
    this.slug = value;
  }

  /**
   * Gets the value of the 'stored_in_rdbms' field.
   */
  public java.lang.Boolean getStoredInRdbms() {
    return stored_in_rdbms;
  }

  /**
   * Sets the value of the 'stored_in_rdbms' field.
   * @param value the value to set.
   */
  public void setStoredInRdbms(java.lang.Boolean value) {
    this.stored_in_rdbms = value;
  }

  /**
   * Gets the value of the 'soft_deleted' field.
   */
  public java.lang.Boolean getSoftDeleted() {
    return soft_deleted;
  }

  /**
   * Sets the value of the 'soft_deleted' field.
   * @param value the value to set.
   */
  public void setSoftDeleted(java.lang.Boolean value) {
    this.soft_deleted = value;
  }

  /** Creates a new PostRecord RecordBuilder */
  public static org.wkh.fastblog.domain.PostRecord.Builder newBuilder() {
    return new org.wkh.fastblog.domain.PostRecord.Builder();
  }
  
  /** Creates a new PostRecord RecordBuilder by copying an existing Builder */
  public static org.wkh.fastblog.domain.PostRecord.Builder newBuilder(org.wkh.fastblog.domain.PostRecord.Builder other) {
    return new org.wkh.fastblog.domain.PostRecord.Builder(other);
  }
  
  /** Creates a new PostRecord RecordBuilder by copying an existing PostRecord instance */
  public static org.wkh.fastblog.domain.PostRecord.Builder newBuilder(org.wkh.fastblog.domain.PostRecord other) {
    return new org.wkh.fastblog.domain.PostRecord.Builder(other);
  }
  
  /**
   * RecordBuilder for PostRecord instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<PostRecord>
    implements org.apache.avro.data.RecordBuilder<PostRecord> {

    private java.lang.CharSequence id;
    private long created_at;
    private boolean published;
    private java.lang.Long published_at;
    private java.lang.CharSequence title;
    private java.lang.CharSequence body;
    private java.lang.CharSequence summary;
    private java.lang.CharSequence slug;
    private boolean stored_in_rdbms;
    private boolean soft_deleted;

    /** Creates a new Builder */
    private Builder() {
      super(org.wkh.fastblog.domain.PostRecord.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(org.wkh.fastblog.domain.PostRecord.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.created_at)) {
        this.created_at = data().deepCopy(fields()[1].schema(), other.created_at);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.published)) {
        this.published = data().deepCopy(fields()[2].schema(), other.published);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.published_at)) {
        this.published_at = data().deepCopy(fields()[3].schema(), other.published_at);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.title)) {
        this.title = data().deepCopy(fields()[4].schema(), other.title);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.body)) {
        this.body = data().deepCopy(fields()[5].schema(), other.body);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.summary)) {
        this.summary = data().deepCopy(fields()[6].schema(), other.summary);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.slug)) {
        this.slug = data().deepCopy(fields()[7].schema(), other.slug);
        fieldSetFlags()[7] = true;
      }
      if (isValidValue(fields()[8], other.stored_in_rdbms)) {
        this.stored_in_rdbms = data().deepCopy(fields()[8].schema(), other.stored_in_rdbms);
        fieldSetFlags()[8] = true;
      }
      if (isValidValue(fields()[9], other.soft_deleted)) {
        this.soft_deleted = data().deepCopy(fields()[9].schema(), other.soft_deleted);
        fieldSetFlags()[9] = true;
      }
    }
    
    /** Creates a Builder by copying an existing PostRecord instance */
    private Builder(org.wkh.fastblog.domain.PostRecord other) {
            super(org.wkh.fastblog.domain.PostRecord.SCHEMA$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.created_at)) {
        this.created_at = data().deepCopy(fields()[1].schema(), other.created_at);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.published)) {
        this.published = data().deepCopy(fields()[2].schema(), other.published);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.published_at)) {
        this.published_at = data().deepCopy(fields()[3].schema(), other.published_at);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.title)) {
        this.title = data().deepCopy(fields()[4].schema(), other.title);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.body)) {
        this.body = data().deepCopy(fields()[5].schema(), other.body);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.summary)) {
        this.summary = data().deepCopy(fields()[6].schema(), other.summary);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.slug)) {
        this.slug = data().deepCopy(fields()[7].schema(), other.slug);
        fieldSetFlags()[7] = true;
      }
      if (isValidValue(fields()[8], other.stored_in_rdbms)) {
        this.stored_in_rdbms = data().deepCopy(fields()[8].schema(), other.stored_in_rdbms);
        fieldSetFlags()[8] = true;
      }
      if (isValidValue(fields()[9], other.soft_deleted)) {
        this.soft_deleted = data().deepCopy(fields()[9].schema(), other.soft_deleted);
        fieldSetFlags()[9] = true;
      }
    }

    /** Gets the value of the 'id' field */
    public java.lang.CharSequence getId() {
      return id;
    }
    
    /** Sets the value of the 'id' field */
    public org.wkh.fastblog.domain.PostRecord.Builder setId(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'id' field has been set */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'id' field */
    public org.wkh.fastblog.domain.PostRecord.Builder clearId() {
      id = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'created_at' field */
    public java.lang.Long getCreatedAt() {
      return created_at;
    }
    
    /** Sets the value of the 'created_at' field */
    public org.wkh.fastblog.domain.PostRecord.Builder setCreatedAt(long value) {
      validate(fields()[1], value);
      this.created_at = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'created_at' field has been set */
    public boolean hasCreatedAt() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'created_at' field */
    public org.wkh.fastblog.domain.PostRecord.Builder clearCreatedAt() {
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'published' field */
    public java.lang.Boolean getPublished() {
      return published;
    }
    
    /** Sets the value of the 'published' field */
    public org.wkh.fastblog.domain.PostRecord.Builder setPublished(boolean value) {
      validate(fields()[2], value);
      this.published = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'published' field has been set */
    public boolean hasPublished() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'published' field */
    public org.wkh.fastblog.domain.PostRecord.Builder clearPublished() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'published_at' field */
    public java.lang.Long getPublishedAt() {
      return published_at;
    }
    
    /** Sets the value of the 'published_at' field */
    public org.wkh.fastblog.domain.PostRecord.Builder setPublishedAt(java.lang.Long value) {
      validate(fields()[3], value);
      this.published_at = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'published_at' field has been set */
    public boolean hasPublishedAt() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'published_at' field */
    public org.wkh.fastblog.domain.PostRecord.Builder clearPublishedAt() {
      published_at = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /** Gets the value of the 'title' field */
    public java.lang.CharSequence getTitle() {
      return title;
    }
    
    /** Sets the value of the 'title' field */
    public org.wkh.fastblog.domain.PostRecord.Builder setTitle(java.lang.CharSequence value) {
      validate(fields()[4], value);
      this.title = value;
      fieldSetFlags()[4] = true;
      return this; 
    }
    
    /** Checks whether the 'title' field has been set */
    public boolean hasTitle() {
      return fieldSetFlags()[4];
    }
    
    /** Clears the value of the 'title' field */
    public org.wkh.fastblog.domain.PostRecord.Builder clearTitle() {
      title = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /** Gets the value of the 'body' field */
    public java.lang.CharSequence getBody() {
      return body;
    }
    
    /** Sets the value of the 'body' field */
    public org.wkh.fastblog.domain.PostRecord.Builder setBody(java.lang.CharSequence value) {
      validate(fields()[5], value);
      this.body = value;
      fieldSetFlags()[5] = true;
      return this; 
    }
    
    /** Checks whether the 'body' field has been set */
    public boolean hasBody() {
      return fieldSetFlags()[5];
    }
    
    /** Clears the value of the 'body' field */
    public org.wkh.fastblog.domain.PostRecord.Builder clearBody() {
      body = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    /** Gets the value of the 'summary' field */
    public java.lang.CharSequence getSummary() {
      return summary;
    }
    
    /** Sets the value of the 'summary' field */
    public org.wkh.fastblog.domain.PostRecord.Builder setSummary(java.lang.CharSequence value) {
      validate(fields()[6], value);
      this.summary = value;
      fieldSetFlags()[6] = true;
      return this; 
    }
    
    /** Checks whether the 'summary' field has been set */
    public boolean hasSummary() {
      return fieldSetFlags()[6];
    }
    
    /** Clears the value of the 'summary' field */
    public org.wkh.fastblog.domain.PostRecord.Builder clearSummary() {
      summary = null;
      fieldSetFlags()[6] = false;
      return this;
    }

    /** Gets the value of the 'slug' field */
    public java.lang.CharSequence getSlug() {
      return slug;
    }
    
    /** Sets the value of the 'slug' field */
    public org.wkh.fastblog.domain.PostRecord.Builder setSlug(java.lang.CharSequence value) {
      validate(fields()[7], value);
      this.slug = value;
      fieldSetFlags()[7] = true;
      return this; 
    }
    
    /** Checks whether the 'slug' field has been set */
    public boolean hasSlug() {
      return fieldSetFlags()[7];
    }
    
    /** Clears the value of the 'slug' field */
    public org.wkh.fastblog.domain.PostRecord.Builder clearSlug() {
      slug = null;
      fieldSetFlags()[7] = false;
      return this;
    }

    /** Gets the value of the 'stored_in_rdbms' field */
    public java.lang.Boolean getStoredInRdbms() {
      return stored_in_rdbms;
    }
    
    /** Sets the value of the 'stored_in_rdbms' field */
    public org.wkh.fastblog.domain.PostRecord.Builder setStoredInRdbms(boolean value) {
      validate(fields()[8], value);
      this.stored_in_rdbms = value;
      fieldSetFlags()[8] = true;
      return this; 
    }
    
    /** Checks whether the 'stored_in_rdbms' field has been set */
    public boolean hasStoredInRdbms() {
      return fieldSetFlags()[8];
    }
    
    /** Clears the value of the 'stored_in_rdbms' field */
    public org.wkh.fastblog.domain.PostRecord.Builder clearStoredInRdbms() {
      fieldSetFlags()[8] = false;
      return this;
    }

    /** Gets the value of the 'soft_deleted' field */
    public java.lang.Boolean getSoftDeleted() {
      return soft_deleted;
    }
    
    /** Sets the value of the 'soft_deleted' field */
    public org.wkh.fastblog.domain.PostRecord.Builder setSoftDeleted(boolean value) {
      validate(fields()[9], value);
      this.soft_deleted = value;
      fieldSetFlags()[9] = true;
      return this; 
    }
    
    /** Checks whether the 'soft_deleted' field has been set */
    public boolean hasSoftDeleted() {
      return fieldSetFlags()[9];
    }
    
    /** Clears the value of the 'soft_deleted' field */
    public org.wkh.fastblog.domain.PostRecord.Builder clearSoftDeleted() {
      fieldSetFlags()[9] = false;
      return this;
    }

    @Override
    public PostRecord build() {
      try {
        PostRecord record = new PostRecord();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.created_at = fieldSetFlags()[1] ? this.created_at : (java.lang.Long) defaultValue(fields()[1]);
        record.published = fieldSetFlags()[2] ? this.published : (java.lang.Boolean) defaultValue(fields()[2]);
        record.published_at = fieldSetFlags()[3] ? this.published_at : (java.lang.Long) defaultValue(fields()[3]);
        record.title = fieldSetFlags()[4] ? this.title : (java.lang.CharSequence) defaultValue(fields()[4]);
        record.body = fieldSetFlags()[5] ? this.body : (java.lang.CharSequence) defaultValue(fields()[5]);
        record.summary = fieldSetFlags()[6] ? this.summary : (java.lang.CharSequence) defaultValue(fields()[6]);
        record.slug = fieldSetFlags()[7] ? this.slug : (java.lang.CharSequence) defaultValue(fields()[7]);
        record.stored_in_rdbms = fieldSetFlags()[8] ? this.stored_in_rdbms : (java.lang.Boolean) defaultValue(fields()[8]);
        record.soft_deleted = fieldSetFlags()[9] ? this.soft_deleted : (java.lang.Boolean) defaultValue(fields()[9]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
