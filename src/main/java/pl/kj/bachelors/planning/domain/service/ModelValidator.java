package pl.kj.bachelors.planning.domain.service;

import pl.kj.bachelors.planning.domain.exception.ApiError;

import java.util.Collection;

public interface ModelValidator {
    <T> Collection<ApiError> validateModel(T model);
}
