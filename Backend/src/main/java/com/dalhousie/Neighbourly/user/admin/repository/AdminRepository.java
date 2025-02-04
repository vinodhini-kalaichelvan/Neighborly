package com.dalhousie.Neighbourly.user.admin.repository;

import com.dalhousie.Neighbourly.user.admin.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
}
