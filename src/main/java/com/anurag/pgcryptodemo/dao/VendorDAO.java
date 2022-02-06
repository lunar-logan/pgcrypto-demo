package com.anurag.pgcryptodemo.dao;

import com.anurag.pgcryptodemo.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorDAO extends JpaRepository<Vendor, Long> {
}
