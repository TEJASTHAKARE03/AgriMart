package com.example.demo.repository;

import com.example.demo.model.Recommendation;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {

    List<Recommendation> findByUser(User user);
}
