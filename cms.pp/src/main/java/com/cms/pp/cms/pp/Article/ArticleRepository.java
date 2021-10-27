package com.cms.pp.cms.pp.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {


    //@Query(value = "SELECT * FROM articles as a inner join ARTICLE_CONTENT as  ac on  a.id=ac.artiCLE_ID inner join article_languages as al on ac.LANGUAGES_ID=al.id", nativeQuery = true)
   // @Override
   // @Query(value = "SELECT a.id, a.article_date, a.article_published, a.user_id, ac.content, al.language_name FROM ARTICLES as a  join ARTICLE_CONTENT as  ac on  a.id=ac.artiCLE_ID  join article_languages as al on ac.LANGUAGES_ID =al.id join user u on a.user_id=u.idSELECT a.id, a.article_date, a.article_published, a.user_id, ac.content, al.language_name FROM ARTICLES as a  join ARTICLE_CONTENT as  ac on  a.id=ac.artiCLE_ID  join article_languages as al on ac.LANGUAGES_ID =al.id join user u on a.user_id=u.id", nativeQuery = true)
   // List<Article> findAll();

}
/*
SELECT * FROM ARTICLES as a inner join ARTICLE_CONTENT as  ac on  a.id=ac.artiCLE_ID inner join article_languages as al on ac.LANGUAGES_ID =al.id


SELECT DISTINCT FROM ARTICLES as a inner join ARTICLE_CONTENT as  ac on  a.id=ac.artiCLE_ID inner join article_languages as al on ac.LANGUAGES_ID =al.id




SELECT a.id, a.article_date, a.article_published, a.user_id, ac.content, al.language_name FROM ARTICLES as a  join ARTICLE_CONTENT as  ac on  a.id=ac.artiCLE_ID  join article_languages as al on ac.LANGUAGES_ID =al.id join user u on a.user_id=u.id
 */