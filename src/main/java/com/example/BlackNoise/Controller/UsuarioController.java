package com.example.blacknoise.controller;

import com.example.blacknoise.model.Usuario;
import com.example.blacknoise.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuario")

public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/{id}")
    public String obtenerUsuarioPorId(@PathVariable String id, Model model) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            
            switch(usuario.getRol()) {
                case ADMINISTRADOR:
                    List<Usuario> todosUsuarios = usuarioService.obtenerTodosLosUsuarios();
                    model.addAttribute("usuarios", todosUsuarios);
                    return "admin_dashboard";
                case EMPLEADO:
                    List<Usuario> usuariosEmpleado = usuarioService.obtenerTodosLosUsuarios();
                    model.addAttribute("usuarios", usuariosEmpleado);
                    return "empleado_dashboard";
                default:
                    return "catalogo";
            }
        } else {
            return "error";
        }
    }

    @GetMapping("/{idAdmin}/editar/{idEditado}")
public String editarFormulario(@PathVariable String idAdmin, @PathVariable String idEditado, Model model) {
    // Obtén el usuario que está siendo editado
    Usuario usuarioEditar = usuarioService.obtenerUsuarioPorId(idEditado);
    
    if (usuarioEditar != null) {
        model.addAttribute("usuario", usuarioEditar);
        return "editar_usuario";  // Página donde se muestra el formulario de edición
    } else {
        return "error";
    }
}

@PostMapping("/{idAdmin}/editar/{idEditado}")
public String editarUsuario(@PathVariable String idAdmin, @PathVariable String idEditado, 
                             @RequestParam String nombre, @RequestParam String correo, Model model) {
    try {
        // Obtener el usuario que está siendo editado
        Usuario usuarioEditar = usuarioService.obtenerUsuarioPorId(idEditado);
        
        // Actualizar los valores del usuario con los nuevos datos del formulario
        usuarioEditar.setNombre(nombre);
        usuarioEditar.setCorreo(correo);
        
        // Guardar los cambios en la base de datos
        usuarioService.editarUsuario(usuarioEditar);
        
        // Redirigir a la vista del usuario editado
        return "redirect:/usuario/" + idAdmin;  // Redirigir al dashboard del administrador o a la vista que desees
    } catch (RuntimeException e) {
        model.addAttribute("error", e.getMessage());
        return "error";  // En caso de error, redirige a una página de error
    }
}


    @PostMapping("/{idAdmin}/eliminar/{idEditado}")
public String eliminarUsuario(@PathVariable String idAdmin, @PathVariable String idEditado, Model model) {
    try {
        // Eliminar el usuario con el ID recibido
        usuarioService.eliminarUsuario(idEditado);
        
        // Redirigir al dashboard del administrador después de eliminar el usuario
        return "redirect:/usuario/" + idAdmin;  // Redirige al panel del administrador (o la página que desees)
    } catch (RuntimeException e) {
        model.addAttribute("error", e.getMessage());
        return "error";  // Si ocurre un error, redirige a una página de error
    }
}
}