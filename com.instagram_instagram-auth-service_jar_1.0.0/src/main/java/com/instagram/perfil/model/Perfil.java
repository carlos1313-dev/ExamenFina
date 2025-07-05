package com.instagram.perfil.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "perfiles")
public class Perfil {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", unique = true, nullable = false)
    @NotNull(message = "ID de usuario es obligatorio")
    private Long userId;
    
    @Column(name = "username", unique = true, nullable = false)
    @NotNull(message = "Username es obligatorio")
    private String username;
    
    @Column(name = "img_perfil", length = 500)
    private String imgPerfil;
    
    @Column(name = "biografia", length = 500)
    @Size(max = 500, message = "La biografía no puede exceder 500 caracteres")
    private String biografia;
    
    @Column(name = "num_posts", nullable = false)
    private Integer numPosts = 0;
    
    @Column(name = "seguidores", nullable = false)
    private Integer seguidores = 0;
    
    @Column(name = "seguidos", nullable = false)
    private Integer seguidos = 0;
    
    @Column(name = "is_private", nullable = false)
    private Boolean isPrivate = false;
    
    @ElementCollection
    @CollectionTable(name = "perfil_posts", joinColumns = @JoinColumn(name = "perfil_id"))
    @Column(name = "post_url")
    private List<String> posts = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructores
    public Perfil() {
        this.numPosts = 0;
        this.seguidores = 0;
        this.seguidos = 0;
        this.isPrivate = false;
        this.posts = new ArrayList<>();
    }
    
    public Perfil(Long userId, String username) {
        this();
        this.userId = userId;
        this.username = username;
    }
    
    public Perfil(Long userId, String username, String imgPerfil, String biografia) {
        this(userId, username);
        this.imgPerfil = imgPerfil;
        this.biografia = biografia;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getImgPerfil() {
        return imgPerfil;
    }
    
    public void setImgPerfil(String imgPerfil) {
        this.imgPerfil = imgPerfil;
    }
    
    public String getBiografia() {
        return biografia;
    }
    
    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }
    
    public Integer getNumPosts() {
        return numPosts;
    }
    
    public void setNumPosts(Integer numPosts) {
        this.numPosts = numPosts;
    }
    
    public Integer getSeguidores() {
        return seguidores;
    }
    
    public void setSeguidores(Integer seguidores) {
        this.seguidores = seguidores;
    }
    
    public Integer getSeguidos() {
        return seguidos;
    }
    
    public void setSeguidos(Integer seguidos) {
        this.seguidos = seguidos;
    }
    
    public Boolean getIsPrivate() {
        return isPrivate;
    }
    
    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
    
    public List<String> getPosts() {
        return posts;
    }
    
    public void setPosts(List<String> posts) {
        this.posts = posts;
        this.numPosts = posts != null ? posts.size() : 0;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Métodos para compatibilidad con el frontend
    public String getFotoPerfil() {
        return this.imgPerfil;
    }
    
    public void setFotoPerfil(String fotoPerfil) {
        this.imgPerfil = fotoPerfil;
    }
    
    public String getBio() {
        return this.biografia;
    }
    
    public void setBio(String bio) {
        this.biografia = bio;
    }
    
    public Integer getFollowers() {
        return this.seguidores;
    }
    
    public void setFollowers(Integer followers) {
        this.seguidores = followers;
    }
    
    public Integer getFollowing() {
        return this.seguidos;
    }
    
    public void setFollowing(Integer following) {
        this.seguidos = following;
    }
    
    // Métodos de utilidad
    public void addPost(String postUrl) {
        if (this.posts == null) {
            this.posts = new ArrayList<>();
        }
        this.posts.add(postUrl);
        this.numPosts = this.posts.size();
    }
    
    public void removePost(String postUrl) {
        if (this.posts != null) {
            this.posts.remove(postUrl);
            this.numPosts = this.posts.size();
        }
    }
    
    public void incrementSeguidores() {
        this.seguidores++;
    }
    
    public void decrementSeguidores() {
        if (this.seguidores > 0) {
            this.seguidores--;
        }
    }
    
    public void incrementSeguidos() {
        this.seguidos++;
    }
    
    public void decrementSeguidos() {
        if (this.seguidos > 0) {
            this.seguidos--;
        }
    }
    
    @Override
    public String toString() {
        return "Perfil{" +
                "id=" + id +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", biografia='" + biografia + '\'' +
                ", numPosts=" + numPosts +
                ", seguidores=" + seguidores +
                ", seguidos=" + seguidos +
                ", isPrivate=" + isPrivate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Perfil perfil = (Perfil) o;
        return Objects.equals(id, perfil.id) && 
               Objects.equals(userId, perfil.userId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }
}