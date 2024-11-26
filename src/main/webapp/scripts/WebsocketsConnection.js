import GUI from "./GUI.js";

class WebSocketsConnection {
    constructor() {
         // Estabelece uma conexão WebSocket com o servidor do jogo
        this.websocket = new WebSocket("ws://localhost:8080/wordleGame/wordle")
        // Armazena o número do jogador no jogo
        this.player = null;
        // Configura os listeners de eventos do WebSocket
        this.setupWebSocketListeners();
    }

    setupWebSocketListeners() {
        // Trata conexão bem-sucedida
        this.websocket.onopen = (event) => {
            console.log("Conexão WebSocket estabelecida");
        };

        // Trata mensagens recebidas do servidor
        this.websocket.onmessage = (event) => {
            const message = JSON.parse(event.data);
            this.handleServerMessage(message);
        };

        // Trata erros de conexão
        this.websocket.onerror = (error) => {
            console.error("Erro no WebSocket:", error);
        };

        // Trata fechamento da conexão
        this.websocket.onclose = (event) => {
            console.log("Conexão WebSocket fechada");
        };
    }

    // Método para tratar diferentes tipos de mensagens do servidor
    handleServerMessage(message) {
        switch(message.type) {
            case 'PLAYER_ASSIGNED':
                // Armazena o número do jogador quando atribuído
                this.player = message.player;
                break;
            case 'GAME_STATE':
                break;
        }
    }

    // Envia uma palavra para o servidor
    sendWord(currentWord) {
        // Prepara uma mensagem JSON com a palavra
        let json = {
            "word": currentWord,
            "type": "SEND_WORD"
        };
        
        // Envia a mensagem como string JSON
        this.websocket.send(JSON.stringify(json));
    }

    // Método para iniciar o jogo
    startGame() {
        // Cria uma nova instância da GUI, passando esta conexão
        let gui = new GUI(this);
        
        // Registra eventos da GUI
        gui.registerEvents();
        
        // Prepara e envia mensagem para entrar no jogo
        const json = {
           "type": "ENTER_GAME"
        };
        this.websocket.send(JSON.stringify(json));
        
        // Atualiza texto do botão de iniciar/parar
        this.startStopButton.innerHTML = 'Parar Jogo';
    }

    // Registra evento de clique para botão de iniciar/parar
    registerEvents() {
        // Encontra o botão de iniciar/parar no DOM
        this.startStopButton = document.querySelector("#start-stop-button");
        
        // Vincula o método startGame à instância atual
        this.startStopButton.onclick = this.startGame.bind(this);
    }
}

// Cria uma instância de WebsocketsConnection e registra seus eventos
let websocketsConnection = new WebSocketsConnection();
websocketsConnection.registerEvents();

export default WebSocketsConnection;