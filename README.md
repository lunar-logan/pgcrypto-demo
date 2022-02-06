pgcrypto demo
===


## Encrypting entire database using rds
1. https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/Overview.Encryption.html#Overview.Encryption.Overview
2. https://stackoverflow.com/questions/57512657/enable-encryption-on-existing-database-aws-rds-postgresql

## Encrypting a particular column(s)

We'll be using a postgres extension called [pgcrypto](https://www.postgresql.org/docs/8.3/pgcrypto.html). The pgcrypto module provides cryptographic functions for PostgreSQL.

### Install pgcrypto extension in postgres

```sql
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
```

Say we want to encrypt `email` column of following table:

```sql
create table vendor
(
    id      bigserial primary key,
    name    text,
    email   text unique,  -- we want' to encrypt this column  
    phone   text unique,
    address text,
    created timestamp
);
```

The data model which was initially looking like: 

```java
@Entity
@Table(name = "vendor")
public class Vendor {
    ... 
    
    @Column(name = "email", unique = true)
    private String email;
    ...
```

will now be updated to:

```java
@Entity
@Table(name = "vendor")
public class Vendor {
    ...
    @Column(name = "email", unique = true, columnDefinition = "bytea")
    @ColumnTransformer(forColumn = "email",
            read = "pgp_sym_decrypt(email, 'pswd')",
            write = "pgp_sym_encrypt(?, 'pswd')")
    private String email;
    ...
```

The annotation `@ColumnTransformer` was added to the field `email`
which we wanted to encrypt. 

Postgres invoked `pgp_sym_decrypt(...)` function to decrypt data while reading from the database into the application and 
called    `pgp_sym_encrypt(...)` function to encrypt data while persisting the data.

`"pswd"` is the key used to encrypt and decrypt. It could be anything. 

The more appropriate place to store the key would be `postgresql.conf` file. 

Then the key can be fetched using the `current_setting(...)` function. Something like this: 

```java
@Entity
@Table(name = "vendor")
public class Vendor {
    ...
    @Column(name = "email", unique = true, columnDefinition = "bytea")
    @ColumnTransformer(forColumn = "email",
            read = "pgp_sym_decrypt(email, current_setting('key'))",
            write = "pgp_sym_encrypt(?, current_setting('key'))")
    private String email;
    ...
```

### References 
1. 
2. https://faun.pub/spring-boot-jpa-data-encryption-a8e7cacfa8e8


