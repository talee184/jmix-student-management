package com.fis.bnk.studentmanagement.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "STUDENT", indexes = {
    @Index(name = "IDX_STUDENT_STUDENT_CLASS", columnList = "STUDENT_CLASS_ID")
})
@Entity
public class Student {

  @JmixGeneratedValue
  @Column(name = "ID", nullable = false)
  @Id
  private UUID id;

  @Column(name = "CODE")
  private String code;

  @InstanceName
  @Column(name = "NAME")
  private String name;

  @Column(name = "BIRTH_DATE")
  @Temporal(TemporalType.DATE)
  private Date birthDate;

  @JoinTable(name = "STUDENT_SUBJECT_LINK",
      joinColumns = @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID"),
      inverseJoinColumns = @JoinColumn(name = "SUBJECT_ID", referencedColumnName = "ID"))
  @ManyToMany
  private List<Subject> subjects;

  @Column(name = "ADDRESS")
  private String address;

  @Column(name = "PHONE_NUMBER")
  private String phoneNumber;

  @JoinColumn(name = "STUDENT_CLASS_ID")
  @ManyToOne(fetch = FetchType.LAZY)
  private Class studentClass;

  @Column(name = "VERSION", nullable = false)
  @Version
  private Integer version;

  public List<Subject> getSubjects() {
    return subjects;
  }

  public void setSubjects(List<Subject> subjects) {
    this.subjects = subjects;
  }

  public Class getStudentClass() {
    return studentClass;
  }

  public void setStudentClass(Class studentClass) {
    this.studentClass = studentClass;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
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
