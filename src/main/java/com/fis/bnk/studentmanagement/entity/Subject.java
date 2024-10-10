package com.fis.bnk.studentmanagement.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "SUBJECT")
@Entity
public class Subject {

  @JmixGeneratedValue
  @Column(name = "ID", nullable = false)
  @Id
  private UUID id;

  @Column(name = "CODE")
  private String code;

  @InstanceName
  @Column(name = "NAME")
  private String name;

  @JoinTable(name = "SUBJECT_STUDENT_LINK",
      joinColumns = @JoinColumn(name = "SUBJECT_ID", referencedColumnName = "ID"),
      inverseJoinColumns = @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID"))
  @ManyToMany
  private List<Student> students;

  @Column(name = "VERSION", nullable = false)
  @Version
  private Integer version;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public List<Student> getStudents() {
    return students;
  }

  public void setStudents(List<Student> students) {
    this.students = students;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }
}
