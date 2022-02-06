package com.anurag.pgcryptodemo.controller;

import com.anurag.pgcryptodemo.dao.VendorDAO;
import com.anurag.pgcryptodemo.model.Vendor;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendors")
@AllArgsConstructor
public class VendorController {
    private final VendorDAO vendorDAO;

    @PostMapping
    public ResponseEntity<Vendor> createVendor(@RequestBody Vendor vendor) {
        return ResponseEntity.ok(vendorDAO.save(vendor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getVendorById(@PathVariable Long id) {
        return vendorDAO.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Vendor>> getAllVendors(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return ResponseEntity.ok(vendorDAO.findAll(PageRequest.of(page, size)).stream().toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Vendor> deleteVendorById(@PathVariable Long id) {
        vendorDAO.deleteById(id);
        return ResponseEntity.accepted().build();
    }
}
