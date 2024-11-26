# Wordle Competitivo 🏆

Bem-vindo ao **Wordle Competitivo**, uma versão do famoso jogo de palavras para **dois jogadores**, onde ambos competem para adivinhar a palavra secreta do outro. O jogo utiliza **WebSockets** para comunicação em tempo real e **JSON** como formato para as mensagens trocadas entre os jogadores e o servidor.

---

## 🚀 Funcionalidades

### 1. **Tela Inicial** 🖥️
- Quando o jogo é iniciado, **dois tabuleiros vazios** são apresentados.
- Um botão **"Conectar ao servidor"** permite que os jogadores se conectem ao jogo.
- O jogo só começa quando **ambos os jogadores pressionam o botão**. Após isso, o texto do botão muda para **"Sair"** e a partida é iniciada.

### 2. **Escolha de Palavras** 🔠
- Cada jogador escolhe uma **palavra secreta** de **cinco caracteres** e a envia ao servidor.
- Ambos os jogadores podem **ver os tabuleiros** do outro, com o histórico de tentativas feitas.

### 3. **Avaliação das Tentativas** 🔍
- Cada tentativa é comparada à palavra secreta do adversário. A avaliação das tentativas é feita da seguinte forma:
  - Se a letra estiver na **posição correta**, ela terá o **fundo verde**.
  - Se a letra estiver na **posição incorreta**, ela terá o **fundo amarelo**.
  - Se a letra **não existir na palavra**, o fundo será **cinza**.

### 4. **Fim do Jogo** 🎉
- **O jogador que adivinhar sua palavra secreta primeiro vence o jogo**.
- Uma **mensagem de vitória ou derrota** é enviada a ambos os jogadores, informando quem ganhou e quem perdeu.
- Após o término do jogo, **nenhuma interação adicional é permitida**. A conexão WebSocket é **fechada** para ambos os jogadores.

### 5. **Desistência** ❌
- Caso um jogador desista do jogo pressionando o botão **"Sair"** ou fechando a aba do navegador, a conexão é automaticamente encerrada.
- O jogador que continuar no jogo receberá uma **mensagem informando que venceu**.

---

## 🛠️ Tecnologias Utilizadas

- **WebSockets**: Para comunicação em tempo real entre os jogadores e o servidor.
- **JSON**: Para troca de mensagens entre o cliente e o servidor.
- **HTML, CSS e JavaScript**: Para construção do front-end interativo.
- **Servidor WebSocket**: Para gerenciar as interações e o fluxo do jogo.

---

## 🎮 Como Jogar

1. **Conecte-se ao Servidor**: Ao abrir o jogo, ambos os jogadores precisam pressionar o botão **"Conectar ao servidor"** para começar.
2. **Escolha Sua Palavra**: Cada jogador escolhe uma palavra secreta de 5 letras e a envia ao servidor.
3. **Tente Adivinhar a Palavra do Outro**: Durante o jogo, você verá os tabuleiros de ambos os jogadores, com as tentativas feitas e suas respectivas avaliações.
4. **Observe as Cores**:
   - **Verde**: A letra está na **posição correta**.
   - **Amarelo**: A letra está na **posição errada**.
   - **Cinza**: A letra **não existe** na palavra.
5. **Vença o Jogo**: O jogo termina quando um dos jogadores adivinha sua palavra antes do outro. Uma mensagem de vitória/derrota será exibida.
6. **Desistência**: Se um jogador desistir, o outro é automaticamente declarado vencedor.

---

## 📜 Regras

- **Palavras de 5 letras**: Cada palavra escolhida deve ter exatamente 5 letras.
- **Modo Competitivo**: Ambos os jogadores podem ver as tentativas feitas um pelo outro.
- **Vencedor**: O primeiro jogador a adivinhar a palavra do adversário é o vencedor.
- **Desistência**: Se um jogador desistir, o outro é declarado vencedor.

---

## 🎉 Divirta-se!

Desafie um amigo e veja quem consegue adivinhar a palavra primeiro. Boa sorte e divirta-se jogando **Wordle Competitivo**! 🏆
