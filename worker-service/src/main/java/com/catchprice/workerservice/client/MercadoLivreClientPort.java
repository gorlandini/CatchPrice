package com.catchprice.workerservice.client;



import java.util.Optional;

public interface MercadoLivreClientPort {
    Optional<MlProductResult> findCheapestWithShipping(String query, String cep);
}
