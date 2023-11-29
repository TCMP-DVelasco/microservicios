package com.moto.service.motoservice.service;

import com.moto.service.motoservice.repository.MotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.moto.service.motoservice.entity.Moto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotoService {
    @Autowired
    private MotoRepository motoRepository;

    public List<Moto> getAll(){
        return motoRepository.findAll();
    }

    public Moto getMotoById(int id){
        return motoRepository.findById(id).orElse(null);
    }

    public Moto saveMoto(Moto moto){
        return motoRepository.save(moto);
    }

    public List<Moto> byUsuarioId(int usuarioId){
        return motoRepository.findByUsuarioId(usuarioId);
    }
}
