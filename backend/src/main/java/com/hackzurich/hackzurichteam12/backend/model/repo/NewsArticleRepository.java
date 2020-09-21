package com.hackzurich.hackzurichteam12.backend.model.repo;

import com.hackzurich.hackzurichteam12.backend.model.entity.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NewsArticleRepository extends JpaRepository<NewsArticle, UUID> {
    long countByLocationId(UUID locationId);
}
