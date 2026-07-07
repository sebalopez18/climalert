package ar.utn.ba.ddsi.mailing.services;

import reactor.core.publisher.Mono;

public interface IClimaService {
    Mono<Void> actualizarClimaCiudad();
} 