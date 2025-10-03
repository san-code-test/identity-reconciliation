package com.santhosh.identity.reconciliation;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity // This tells Hibernate to make a table out of this class
@Getter
@Setter
public class Contact {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Integer id;

  private String phoneNumber;
  private String email;

  private Integer linkedId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LinkPrecedence linkPrecedence;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  private Date createdAt;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = true)
  private Date updatedAt;
  private Date deletedAt;

  public Contact() {
    this.createdAt = new Date();
    this.updatedAt = createdAt;
  }

  public Contact(String phoneNumber, String email) {
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.createdAt = new Date();
    this.updatedAt = createdAt;
  }

  @Override
  public String toString() {
    return "Contact{" +
        "id=" + id +
        ", phoneNumber='" + phoneNumber + '\'' +
        ", email='" + email + '\'' +
        ", linkedId=" + linkedId +
        ", linkPrecedence=" + linkPrecedence +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", deletedAt=" + deletedAt +
        '}';
  }
}
