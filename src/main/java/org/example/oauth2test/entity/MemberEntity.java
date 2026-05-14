package org.example.oauth2test.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.oauth2test.entity.num.ROLE;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberNo;
    private String password;
    @Enumerated(EnumType.STRING)
    private ROLE role;
}
