package sockets;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.DecodeException;
import jakarta.websocket.EndpointConfig;

public class MessageDecoder implements jakarta.websocket.Decoder.Text<Object> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object decode(String s) throws DecodeException {
        try {
            // Decodifica JSON em um objeto genérico
            return objectMapper.readValue(s, Object.class);
        } catch (Exception e) {
            throw new DecodeException(s, "Erro ao decodificar mensagem JSON", e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        // Testa se a mensagem é um JSON válido
        return s != null && !s.isBlank();
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Inicialização do decodificador, se necessário
    }

    @Override
    public void destroy() {
        // Limpeza do decodificador, se necessário
    }
}