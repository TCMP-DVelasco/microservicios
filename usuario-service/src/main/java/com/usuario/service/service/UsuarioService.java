package com.usuario.service.service;

import com.usuario.service.feignclients.CarroFeignClient;
import com.usuario.service.feignclients.MotoFeignClient;
import com.usuario.service.models.Carro;
import com.usuario.service.models.Moto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usuario.service.repository.UsuarioRepository;
import com.usuario.service.entity.Usuario;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarroFeignClient carroFeignClient;

    @Autowired
    private MotoFeignClient motoFeignClient;

    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    public Usuario getUserById(int id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario saveUser(Usuario user) {
        return usuarioRepository.save(user);
    }

    public List<Carro> getCarros(int usuarioId) {
        List<Carro> carros = restTemplate.getForObject("http://localhost:8002/carro/usuario/" + usuarioId, List.class);
        return carros;
    }

    public List<Moto> getMotos(int usuarioId) {
        List<Moto> motos = restTemplate.getForObject("http://localhost:8003/moto/usuario/" + usuarioId, List.class);
        return motos;
    }

    public Carro saveCarro(int usuarioId, Carro carro) {
        carro.setUsuarioId(usuarioId);
        Carro nuevoCarro = carroFeignClient.save(carro);
        return nuevoCarro;
    }

    public Moto saveMoto(int usuarioId, Moto moto) {
        moto.setUsuarioId(usuarioId);
        Moto nuevaMoto = motoFeignClient.save(moto);
        return nuevaMoto;
    }

    public Map<String, Object> getUsuarioAndVehiculo(int usuarioId){
        Map<String, Object> resultado = new HashMap<>();
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if (usuario == null){
            resultado.put("Mensaje: ", "Usuario no existe");
        }

        resultado.put("Usuario: ", usuario);

        List<Carro> carros = carroFeignClient.getCarros(usuarioId);
        if (carros.isEmpty()){
            resultado.put("Mensaje: ", "El usuario no tiene carros");
        }else {
            resultado.put("Carros: ", carros);
        }

        List<Moto> motos = motoFeignClient.getMotos(usuarioId);
        if (motos.isEmpty()){
            resultado.put("Mensaje: ", "El usuario no tiene carros");
        }else {
            resultado.put("Motos: ", motos);
        }
        return resultado;
    }
}

