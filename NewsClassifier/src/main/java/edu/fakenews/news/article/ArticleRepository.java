package edu.fakenews.news.article;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> { 
	List<Article> findBySourceOrderByPubTimestampDesc(String source);
	List<Article> findAllByOrderByClassificationTimestampDesc();
}