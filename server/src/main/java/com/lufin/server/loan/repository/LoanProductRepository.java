package com.lufin.server.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lufin.server.loan.domain.LoanProduct;

@Repository
public interface LoanProductRepository extends JpaRepository<LoanProduct, Integer> {
}
