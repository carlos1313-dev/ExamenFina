package com.instagram.perfil.service;

import com.instagram.perfil.model.Perfil;
import com.instagram.perfil.repository.PerfilRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PerfilService {
    
    private static final Logger log = LoggerFactory.getLogger(PerfilService.class);
    
    private final PerfilRepository perfilRepository;
    
    @Autowired
    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }
    
    /**
     * Crea un perfil básico para un usuario con username
     */
    public Perfil createPerfil(Long userId, String username) {
        log.info("Creando perfil para usuario ID: {} con username: {}", userId, username);
        
        // Verificar que no exista ya un perfil para este usuario
        if (perfilRepository.existsByUserId(userId)) {
            throw new RuntimeException("Ya existe un perfil para este usuario");
        }
        
        Perfil nuevoPerfil = new Perfil(userId,username);
        Perfil perfilGuardado = perfilRepository.save(nuevoPerfil);
        
        log.info("Perfil creado exitosamente para usuario ID: {}", userId);
        return perfilGuardado;
    }
    
    /**
     * Crea un perfil para un usuario (método original mantenido para compatibilidad)
     */
//    public Perfil createPerfil(Long userId) {
//        log.info("Creando perfil para usuario ID: {}", userId);
//        
//        // Verificar que no exista ya un perfil para este usuario
//        if (perfilRepository.existsByUserId(userId)) {
//            throw new RuntimeException("Ya existe un perfil para este usuario");
//        }
//        
//        Perfil nuevoPerfil = new Perfil(userId);
//        Perfil perfilGuardado = perfilRepository.save(nuevoPerfil);
//        
//        log.info("Perfil creado exitosamente para usuario ID: {}", userId);
//        return perfilGuardado;
//    }
    
    /**
     * Crea un perfil con datos iniciales
     */
    public Perfil createPerfilWithData(Long userId, String username, String imgPerfil, String biografia, Boolean isPrivate) {
        log.info("Creando perfil con datos para usuario ID: {} con username: {}", userId, username);
        
        if (perfilRepository.existsByUserId(userId)) {
            throw new RuntimeException("Ya existe un perfil para este usuario");
        }
        
        Perfil nuevoPerfil = new Perfil(userId,username, imgPerfil, biografia);
        if (isPrivate != null) {
            nuevoPerfil.setIsPrivate(isPrivate);
        }
        
        Perfil perfilGuardado = perfilRepository.save(nuevoPerfil);
        log.info("Perfil con datos creado exitosamente para usuario ID: {}", userId);
        
        return perfilGuardado;
    }
    
    /**
     * Obtiene perfil por ID de usuario
     */
    public Optional<Perfil> getPerfilByUserId(Long userId) {
        log.debug("Buscando perfil para usuario ID: {}", userId);
        return perfilRepository.findByUserId(userId);
    }
    
    /**
     * Obtiene perfil por ID
     */
    public Optional<Perfil> getPerfilById(Long perfilId) {
        log.debug("Buscando perfil ID: {}", perfilId);
        return perfilRepository.findById(perfilId);
    }
    
    /**
     * Actualiza información del perfil
     */
    public Perfil updatePerfil(Long userId, String username, String imgPerfil, String biografia, Boolean isPrivate) {
        log.info("Actualizando perfil para usuario ID: {}", userId);
        
        Optional<Perfil> perfilOpt = perfilRepository.findByUserId(userId);
        if (perfilOpt.isEmpty()) {
            throw new RuntimeException("Perfil no encontrado para el usuario");
        }
        
        Perfil perfil = perfilOpt.get();
        
        if (imgPerfil != null) {
            perfil.setImgPerfil(imgPerfil);
        }
        
        if (biografia != null) {
            perfil.setBiografia(biografia);
        }
        
        if (isPrivate != null) {
            perfil.setIsPrivate(isPrivate);
        }
        
        Perfil perfilActualizado = perfilRepository.save(perfil);
        log.info("Perfil actualizado exitosamente para usuario ID: {}", userId);
        
        return perfilActualizado;
    }
    
    /**
     * Agrega un post al perfil
     */
    public Perfil addPost(Long userId, String postUrl) {
        log.info("Agregando post al perfil del usuario ID: {}", userId);
        
        Optional<Perfil> perfilOpt = perfilRepository.findByUserId(userId);
        if (perfilOpt.isEmpty()) {
            throw new RuntimeException("Perfil no encontrado para el usuario");
        }
        
        Perfil perfil = perfilOpt.get();
        perfil.addPost(postUrl);
        
        Perfil perfilActualizado = perfilRepository.save(perfil);
        log.info("Post agregado exitosamente al perfil del usuario ID: {}", userId);
        
        return perfilActualizado;
    }
    
    /**
     * Elimina un post del perfil
     */
    public Perfil removePost(Long userId, String postUrl) {
        log.info("Eliminando post del perfil del usuario ID: {}", userId);
        
        Optional<Perfil> perfilOpt = perfilRepository.findByUserId(userId);
        if (perfilOpt.isEmpty()) {
            throw new RuntimeException("Perfil no encontrado para el usuario");
        }
        
        Perfil perfil = perfilOpt.get();
        perfil.removePost(postUrl);
        
        Perfil perfilActualizado = perfilRepository.save(perfil);
        log.info("Post eliminado exitosamente del perfil del usuario ID: {}", userId);
        
        return perfilActualizado;
    }
    
    /**
     * Seguir usuario - incrementa seguidos del usuario actual y seguidores del usuario seguido
     */
    public void followUser(Long followerId, Long followedUserId) {
        log.info("Usuario ID: {} siguiendo a usuario ID: {}", followerId, followedUserId);
        
        // Incrementar seguidos del usuario que sigue
        Optional<Perfil> followerPerfilOpt = perfilRepository.findByUserId(followerId);
        if (followerPerfilOpt.isPresent()) {
            Perfil followerPerfil = followerPerfilOpt.get();
            followerPerfil.incrementSeguidos();
            perfilRepository.save(followerPerfil);
        } else {
            log.warn("No se encontró perfil para el usuario seguidor ID: {}", followerId);
        }
        
        // Incrementar seguidores del usuario seguido
        Optional<Perfil> followedPerfilOpt = perfilRepository.findByUserId(followedUserId);
        if (followedPerfilOpt.isPresent()) {
            Perfil followedPerfil = followedPerfilOpt.get();
            followedPerfil.incrementSeguidores();
            perfilRepository.save(followedPerfil);
        } else {
            log.warn("No se encontró perfil para el usuario seguido ID: {}", followedUserId);
        }
        
        log.info("Relación de seguimiento establecida entre usuarios {} y {}", followerId, followedUserId);
    }
    
    /**
     * Dejar de seguir usuario
     */
    public void unfollowUser(Long followerId, Long followedUserId) {
        log.info("Usuario ID: {} dejando de seguir a usuario ID: {}", followerId, followedUserId);
        
        // Decrementar seguidos del usuario que deja de seguir
        Optional<Perfil> followerPerfilOpt = perfilRepository.findByUserId(followerId);
        if (followerPerfilOpt.isPresent()) {
            Perfil followerPerfil = followerPerfilOpt.get();
            followerPerfil.decrementSeguidos();
            perfilRepository.save(followerPerfil);
        } else {
            log.warn("No se encontró perfil para el usuario seguidor ID: {}", followerId);
        }
        
        // Decrementar seguidores del usuario no seguido
        Optional<Perfil> followedPerfilOpt = perfilRepository.findByUserId(followedUserId);
        if (followedPerfilOpt.isPresent()) {
            Perfil followedPerfil = followedPerfilOpt.get();
            followedPerfil.decrementSeguidores();
            perfilRepository.save(followedPerfil);
        } else {
            log.warn("No se encontró perfil para el usuario seguido ID: {}", followedUserId);
        }
        
        log.info("Relación de seguimiento eliminada entre usuarios {} y {}", followerId, followedUserId);
    }
    
    /**
     * Obtiene perfiles públicos más populares
     */
    public List<Perfil> getPublicProfiles() {
        log.debug("Obteniendo perfiles públicos ordenados por seguidores");
        return perfilRepository.findPublicProfilesOrderByFollowers();
    }
    
    /**
     * Obtiene perfiles con posts
     */
    public List<Perfil> getProfilesWithPosts() {
        log.debug("Obteniendo perfiles con posts ordenados por número de posts");
        return perfilRepository.findProfilesWithPostsOrderByPostCount();
    }
}