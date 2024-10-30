package com.pms.email.repository;

import com.pms.email.model.EmailModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailModel, EmailRepository> {
}
