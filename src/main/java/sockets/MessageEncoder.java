package sockets;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Object> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String encode(Object object) throws EncodeException {
        try {
            // Converte objeto em JSON
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new EncodeException(object, "Erro ao codificar mensagem JSON", e);
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Inicialização do codificador, se necessário
    }

    @Override
    public void destroy() {
        // Limpeza do codificador, se necessário
    }
}
