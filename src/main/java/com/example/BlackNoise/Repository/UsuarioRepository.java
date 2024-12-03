package com.example.BlackNoise.repository;

import com.example.blacknoise.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    // Método para buscar usuario por correo
    Usuario findByCorreo(String correo);
    
    // Método para verificar si existe un usuario con un correo
    boolean existsByCorreo(String correo);
}