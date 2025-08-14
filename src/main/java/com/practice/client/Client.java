package com.practice.client;

import java.io.IOException;

import com.practice.client.dto.AbstractDTO;

public interface Client<T extends AbstractDTO> {
    void connect() throws IOException;
    void send() throws IOException;
    void receive() throws IOException;
    void close() throws IOException;
    void setDTO(T dto);
    T getDTO();
}
