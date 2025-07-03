/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.instagram.perfil.model;

import java.util.ArrayList;

/**
 *
 * @author sangr
 */
public class Perfil {
    private ArrayList <String> posts;
    private String imgPerfil;
    private String biografia;
    private int numPosts;
    private int seguidores;
    private int seguidos;

    public Perfil(ArrayList<String> posts, String imgPerfil, String biografia, int numPosts, int seguidores, int seguidos) {
        this.posts = posts;
        this.imgPerfil = imgPerfil;
        this.biografia = biografia;
        this.numPosts = numPosts;
        this.seguidores = seguidores;
        this.seguidos = seguidos;
    }

    public ArrayList<String> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<String> posts) {
        this.posts = posts;
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

    public int getNumPosts() {
        return numPosts;
    }

    public void setNumPosts(int numPosts) {
        this.numPosts = numPosts;
    }

    public int getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(int seguidores) {
        this.seguidores = seguidores;
    }

    public int getSeguidos() {
        return seguidos;
    }

    public void setSeguidos(int seguidos) {
        this.seguidos = seguidos;
    }

    
    
    
            
}
