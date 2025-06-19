package org.addy.swingboot.repository;

import org.addy.swingboot.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Short> {
}
