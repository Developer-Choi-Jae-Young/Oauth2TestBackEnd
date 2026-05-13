package org.example.oauth2test.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MemberEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberNo;
    private String memberId;
    private String password;
}
