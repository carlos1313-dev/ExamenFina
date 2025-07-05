package com.instagram.perfil.controller;

import com.instagram.perfil.model.Perfil;
import com.instagram.perfil.service.PerfilService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/perfiles")
@CrossOrigin(origins = "*")
public class PerfilController {
    
    private static final Logger log = LoggerFactory.getLogger(PerfilController.class);
    
    private final PerfilService perfilService;
    
    @Autowired
    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }
    
    /**
     * Mostrar página de perfil (Frontend Thymeleaf)
     */
    @GetMapping("/ver/{userId}")
    public String mostrarPerfil(@PathVariable Long userId, Model model) {
        try {
            Optional<Perfil> perfilOpt = perfilService.getPerfilByUserId(userId);
            
            if (perfilOpt.isPresent()) {
                model.addAttribute("usuario", perfilOpt.get());
                return "perfil";
            } else {
                model.addAttribute("error", "Perfil no encontrado");
                return "error";
            }
            
        } catch (Exception e) {
            log.error("Error al mostrar perfil para usuario {}: {}", userId, e.getMessage());
            model.addAttribute("error", "Error al cargar el perfil");
            return "error";
        }
    }
    
    /**
     * Crear perfil básico para un usuario - API REST
     */
    @PostMapping("/crear/{userId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> crearPerfil(
            @PathVariable Long userId,
            @RequestParam String username) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Perfil perfil = perfilService.createPerfil(userId, username);
            
            response.put("success", true);
            response.put("message", "Perfil creado exitosamente");
            response.put("perfil", perfil);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            log.error("Error al crear perfil para usuario {}: {}", userId, e.getMessage());
            
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Crear perfil con datos iniciales - API REST
     */
    @PostMapping("/crear")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> crearPerfilConDatos(@RequestBody Map<String, Object> perfilData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long userId = Long.valueOf(perfilData.get("userId").toString());
            String username = (String) perfilData.get("username");
            String imgPerfil = (String) perfilData.get("imgPerfil");
            String biografia = (String) perfilData.get("biografia");
            Boolean isPrivate = (Boolean) perfilData.get("isPrivate");
            
            Perfil perfil = perfilService.createPerfilWithData(userId, username, imgPerfil, biografia, isPrivate);
            
            response.put("success", true);
            response.put("message", "Perfil creado exitosamente con datos");
            response.put("perfil", perfil);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            log.error("Error al crear perfil con datos: {}", e.getMessage());
            
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Obtener perfil por ID de usuario - API REST
     */
    @GetMapping("/usuario/{userId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getPerfilByUserId(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<Perfil> perfilOpt = perfilService.getPerfilByUserId(userId);
        
        if (perfilOpt.isPresent()) {
            response.put("success", true);
            response.put("perfil", perfilOpt.get());
            
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Perfil no encontrado para el usuario");
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    /**
     * Obtener perfil por ID - API REST
     */
    @GetMapping("/{perfilId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getPerfilById(@PathVariable Long perfilId) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<Perfil> perfilOpt = perfilService.getPerfilById(perfilId);
        
        if (perfilOpt.isPresent()) {
            response.put("success", true);
            response.put("perfil", perfilOpt.get());
            
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Perfil no encontrado");
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    /**
     * Actualizar perfil - API REST
     */
    @PutMapping("/actualizar/{userId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> actualizarPerfil(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> updateData) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = (String) updateData.get("username");
            String imgPerfil = (String) updateData.get("imgPerfil");
            String biografia = (String) updateData.get("biografia");
            Boolean isPrivate = (Boolean) updateData.get("isPrivate");
            
            Perfil perfilActualizado = perfilService.updatePerfil(userId, username, imgPerfil, biografia, isPrivate);
            
            response.put("success", true);
            response.put("message", "Perfil actualizado exitosamente");
            response.put("perfil", perfilActualizado);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            log.error("Error al actualizar perfil del usuario {}: {}", userId, e.getMessage());
            
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Actualizar perfil desde formulario (Frontend) - Para el botón "Guardar cambios"
     */
    @PostMapping("/actualizar-frontend/{userId}")
    public String actualizarPerfilFrontend(
            @PathVariable Long userId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String biografia,
            @RequestParam(required = false) String imgPerfil,
            Model model) {
        
        try {
            Perfil perfilActualizado = perfilService.updatePerfil(userId, username, imgPerfil, biografia, null);
            model.addAttribute("usuario", perfilActualizado);
            model.addAttribute("mensaje", "Perfil actualizado exitosamente");
            return "perfil";
            
        } catch (RuntimeException e) {
            log.error("Error al actualizar perfil desde frontend para usuario {}: {}", userId, e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
    
    /**
     * Agregar post al perfil - API REST
     */
    @PostMapping("/posts/{userId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> agregarPost(
            @PathVariable Long userId,
            @RequestBody Map<String, String> postData) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String postUrl = postData.get("postUrl");
            
            if (postUrl == null || postUrl.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "URL del post es requerida");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            Perfil perfilActualizado = perfilService.addPost(userId, postUrl);
            
            response.put("success", true);
            response.put("message", "Post agregado exitosamente");
            response.put("perfil", perfilActualizado);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            log.error("Error al agregar post al perfil del usuario {}: {}", userId, e.getMessage());
            
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Eliminar post del perfil - API REST
     */
    @DeleteMapping("/posts/{userId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> eliminarPost(
            @PathVariable Long userId,
            @RequestBody Map<String, String> postData) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String postUrl = postData.get("postUrl");
            
            if (postUrl == null || postUrl.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "URL del post es requerida");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            Perfil perfilActualizado = perfilService.removePost(userId, postUrl);
            
            response.put("success", true);
            response.put("message", "Post eliminado exitosamente");
            response.put("perfil", perfilActualizado);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            log.error("Error al eliminar post del perfil del usuario {}: {}", userId, e.getMessage());
            
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Seguir usuario - API REST
     */
    @PostMapping("/seguir")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> seguirUsuario(@RequestBody Map<String, Long> followData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long followerId = followData.get("followerId");
            Long followedUserId = followData.get("followedUserId");
            
            if (followerId == null || followedUserId == null) {
                response.put("success", false);
                response.put("message", "IDs de usuario son requeridos");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            if (followerId.equals(followedUserId)) {
                response.put("success", false);
                response.put("message", "No puedes seguirte a ti mismo");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            perfilService.followUser(followerId, followedUserId);
            
            response.put("success", true);
            response.put("message", "Usuario seguido exitosamente");
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            log.error("Error al seguir usuario: {}", e.getMessage());
            
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Dejar de seguir usuario - API REST
     */
    @PostMapping("/no-seguir")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> noSeguirUsuario(@RequestBody Map<String, Long> followData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long followerId = followData.get("followerId");
            Long followedUserId = followData.get("followedUserId");
            
            if (followerId == null || followedUserId == null) {
                response.put("success", false);
                response.put("message", "IDs de usuario son requeridos");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            perfilService.unfollowUser(followerId, followedUserId);
            
            response.put("success", true);
            response.put("message", "Usuario no seguido exitosamente");
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            log.error("Error al dejar de seguir usuario: {}", e.getMessage());
            
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Obtener perfiles públicos populares - API REST
     */
    @GetMapping("/publicos")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getPerfilesPublicos() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Perfil> perfiles = perfilService.getPublicProfiles();
            
            response.put("success", true);
            response.put("perfiles", perfiles);
            response.put("total", perfiles.size());
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            log.error("Error al obtener perfiles públicos: {}", e.getMessage());
            
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}