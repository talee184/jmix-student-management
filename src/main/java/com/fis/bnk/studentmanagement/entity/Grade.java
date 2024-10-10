package com.fis.bnk.studentmanagement.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.util.UUID;

@JmixEntity
@Table(name = "GRADE", indexes = {
    @Index(name = "IDX_GRADE_STUDENT", columnList = "STUDENT_ID"),
    @Index(name = "IDX_GRADE_SUBJECT", columnList = "SUBJECT_ID")
})
@Entity
public class Grade {

  @JmixGeneratedValue
  @Column(name = "ID", nullable = false)
  @Id
  private UUID id;

  @JoinColumn(name = "STUDENT_ID")
  @ManyToOne(fetch = FetchType.LAZY)
  private Student student;

  @JoinColumn(name = "SUBJECT_ID")
  @ManyToOne(fetch = FetchType.LAZY)
  private Subject subject;

  @Column(name = "SCORE", precision = 19, scale = 0)
  private BigDecimal score;

  @Column(name = "VERSION", nullable = false)
  @Version
  private Integer version;

  public void setScore(BigDecimal score) {
    this.score = score;
  }

  public BigDecimal getScore() {
    return score;
  }

  public Subject getSubject() {
    return subject;
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
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
