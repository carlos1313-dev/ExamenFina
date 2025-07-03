INSERT INTO perfiles (user_id, img_perfil, biografia, num_posts, seguidores, seguidos, is_private, created_at, updated_at) VALUES 
(1, 'https://ejemplo.com/avatar1.jpg', 'Hola! Soy un usuario de prueba 📸', 0, 120, 85, false, NOW(), NOW()),
(2, 'https://ejemplo.com/avatar2.jpg', 'Fotógrafo profesional 📷✨', 0, 340, 150, false, NOW(), NOW()),
(3, 'https://ejemplo.com/avatar3.jpg', 'Viajero por el mundo 🌍', 0, 89, 200, true, NOW(), NOW());

-- Posts de ejemplo para los perfiles
INSERT INTO perfil_posts (perfil_id, post_url) VALUES 
(1, 'https://ejemplo.com/post1.jpg'),
(1, 'https://ejemplo.com/post2.jpg'),
(2, 'https://ejemplo.com/post3.jpg'),
(2, 'https://ejemplo.com/post4.jpg'),
(2, 'https://ejemplo.com/post5.jpg'),
(3, 'https://ejemplo.com/post6.jpg');

-- Actualizar el número de posts basado en los posts insertados
UPDATE perfiles p SET num_posts = (
    SELECT COUNT(*) FROM perfil_posts pp WHERE pp.perfil_id = p.id
) WHERE p.id IN (1, 2, 3);