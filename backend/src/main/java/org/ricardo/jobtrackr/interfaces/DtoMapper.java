package org.ricardo.jobtrackr.interfaces;

public interface DtoMapper<T, D> {
    T toModel(D dto);
}
