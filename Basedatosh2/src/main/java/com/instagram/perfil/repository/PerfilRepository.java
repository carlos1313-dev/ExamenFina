package com.instagram.perfil.repository;

import com.instagram.perfil.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    
    /**
     * Busca perfil por ID de usuario
     */
    Optional<Perfil> findByUserId(Long userId);
    
    /**
     * Busca perfil por username
     */
    Optional<Perfil> findByUsername(String username);
    
    /**
     * Verifica si existe un perfil para el usuario
     */
    boolean existsByUserId(Long userId);
    
    /**
     * Verifica si existe un username
     */
    boolean existsByUsername(String username);
    
    /**
     * Busca perfiles públicos ordenados por seguidores (para explorar)
     */
    @Query("SELECT p FROM Perfil p WHERE p.isPrivate = false ORDER BY p.seguidores DESC")
    List<Perfil> findPublicProfilesOrderByFollowers();
    
    /**
     * Busca perfiles con más posts
     */
    @Query("SELECT p FROM Perfil p WHERE p.numPosts > 0 ORDER BY p.numPosts DESC")
    List<Perfil> findProfilesWithPostsOrderByPostCount();
    
    /**
     * Busca perfiles por número mínimo de seguidores
     */
    @Query("SELECT p FROM Perfil p WHERE p.seguidores >= :minFollowers ORDER BY p.seguidores DESC")
    List<Perfil> findBySeguidoresGreaterThanEqual(@Param("minFollowers") Integer minFollowers);
}