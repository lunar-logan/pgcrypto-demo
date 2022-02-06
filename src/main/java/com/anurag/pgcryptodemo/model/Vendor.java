package com.anurag.pgcryptodemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import java.util.Date;

/*
create table vendor
(
    id      bigserial primary key,
    name    text,
    email   text unique,
    phone   text unique,
    address text,
    created timestamp
);

 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "vendor")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "email", unique = true,columnDefinition = "bytea")
    @ColumnTransformer(forColumn = "email",
            read = "pgp_sym_decrypt(email, 'pswd')",
            write = "pgp_sym_encrypt(?, 'pswd')")
    private String email;
    @Column(name = "phone", unique = true)
    private String phone;
    @Column(name = "address")
    private String address;
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
}
