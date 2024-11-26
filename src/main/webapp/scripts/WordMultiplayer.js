import { useState, useEffect } from 'react';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import Wordle from './Wordle';
import Winner from './Winner';
import MoveResult from './MoveResult';
import NotInWordListError from './NotInWordListError';
import WORD_LIST from './words_pt';

const WordleMultiplayer = () => {
  const [player1Connected, setPlayer1Connected] = useState(false);
  const [player2Connected, setPlayer2Connected] = useState(false);
  const [gameStarted, setGameStarted] = useState(false);
  const [currentPlayer, setCurrentPlayer] = useState(null);
  const [player1Game, setPlayer1Game] = useState(null);
  const [player2Game, setPlayer2Game] = useState(null);
  const [player1Board, setPlayer1Board] = useState(Array(6).fill().map(() => Array(5).fill('')));
  const [player2Board, setPlayer2Board] = useState(Array(6).fill().map(() => Array(5).fill('')));
  const [currentRow, setCurrentRow] = useState(0);
  const [currentWord, setCurrentWord] = useState('');
  const [gameOver, setGameOver] = useState(false);
  const [ws, setWS] = useState(null);

  useEffect(() => {
    const handleWebSocketMessage = (event) => {
      const data = JSON.parse(event.data);
      switch (data.type) {
        case 'OPEN':
          setCurrentPlayer(data.turn);
          setGameStarted(true);
          break;
        case 'MESSAGE':
          updateBoard(data.board);
          setCurrentPlayer(data.turn);
          break;
        case 'ENDGAME':
          updateBoard(data.board);
          endGame(data.winner);
          break;
      }
    };

    const handleWebSocketClose = () => {
      setPlayer1Connected(false);
      setPlayer2Connected(false);
      setGameStarted(false);
      setCurrentPlayer(null);
      setPlayer1Game(null);
      setPlayer2Game(null);
      setPlayer1Board(Array(6).fill().map(() => Array(5).fill('')));
      setPlayer2Board(Array(6).fill().map(() => Array(5).fill('')));
      setCurrentRow(0);
      setCurrentWord('');
      setGameOver(false);
      setWS(null);
    };

    if (player1Connected && player2Connected) {
      const ws = new WebSocket(`ws://${document.location.host}${document.location.pathname}wordle`);
      ws.onmessage = handleWebSocketMessage;
      ws.onclose = handleWebSocketClose;
      setWS(ws);
    } else {
      setWS(null);
    }
  }, [player1Connected, player2Connected]);

  const handlePlayerConnect = (player) => {
    if (player === 1) {
      setPlayer1Connected(!player1Connected);
      if (!player1Connected) {
        setPlayer1Game(new Wordle(WORD_LIST, 6));
      }
    } else {
      setPlayer2Connected(!player2Connected);
      if (!player2Connected) {
        setPlayer2Game(new Wordle(WORD_LIST, 6));
      }
    }
  };

  const handleStartGame = () => {
    if (player1Connected && player2Connected) {
      ws.send(JSON.stringify({ type: 'START' }));
    }
  };

  const handleKeyPress = (key) => {
    if (gameOver) return;

    if (key === '⌫') {
      setCurrentWord(prev => prev.slice(0, -1));
      return;
    }

    if (key === 'ENTER') {
      if (currentWord.length === 5) {
        try {
          const result = currentPlayer === 1 
            ? player1Game.check(currentWord) 
            : player2Game.check(currentWord);

          const updateBoard = currentPlayer === 1 ? player1Board : player2Board;
          const setBoard = currentPlayer === 1 ? setPlayer1Board : setPlayer2Board;

          const newBoard = [...updateBoard];
          newBoard[currentRow] = currentWord.split('').map((letter, index) => ({
            letter,
            status: result.getHint()[index]
          }));

          setBoard(newBoard);
          setCurrentRow(prev => prev + 1);
          setCurrentWord('');

          ws.send(JSON.stringify({
            type: 'MESSAGE',
            board: newBoard
          }));

          if (result.getWinner() === Winner.WIN) {
            endGame('YOU_WIN');
          } else if (result.getWinner() === Winner.LOSE) {
            endGame('YOU_LOSE');
          } else {
            setCurrentPlayer(prev => prev === 1 ? 2 : 1);
          }
        } catch (error) {
          if (error instanceof NotInWordListError) {
            alert(error.message);
          } else {
            console.error(error);
          }
        }
      }
      return;
    }

    if (currentWord.length < 5) {
      setCurrentWord(prev => prev + key);
    }
  };

  const updateBoard = (board) => {
    if (currentPlayer === 1) {
      setPlayer1Board(board);
    } else {
      setPlayer2Board(board);
    }
  };

  const endGame = (winner) => {
    setGameOver(true);
    if (winner === 'YOU_WIN') {
      alert('You win!');
    } else {
      alert('You lose!');
    }
    ws.close(4001, 'Game ended');
  };

  const renderGameBoard = (board) => {
    // Existing renderGameBoard implementation
  };

  const renderKeyboard = () => {
    // Existing renderKeyboard implementation
  };

  return (
    <div className="max-w-md mx-auto p-4">
      <Card>
        <CardHeader>
          <CardTitle>Wordle Multiplayer</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="flex justify-between mb-4">
            <Button 
              variant={player1Connected ? "destructive" : "default"}
              onClick={() => handlePlayerConnect(1)}
            >
              {player1Connected ? "Desconectar J1" : "Conectar J1"}
            </Button>
            <Button 
              variant={player2Connected ? "destructive" : "default"}
              onClick={() => handlePlayerConnect(2)}
            >
              {player2Connected ? "Desconectar J2" : "Conectar J2"}
            </Button>
          </div>

          <Button 
            className="w-full mb-4"
            disabled={!player1Connected || !player2Connected}
            onClick={handleStartGame}
          >
            {gameStarted ? "Jogo em Andamento" : "Iniciar Jogo"}
          </Button>

          <div className="game-boards flex space-x-4">
            <div className="player1-board w-1/2">
              <h3 className="text-center mb-2">Jogador 1 {currentPlayer === 1 && '(Vez)'}</h3>
              {renderGameBoard(player1Board)}
            </div>
            <div className="player2-board w-1/2">
              <h3 className="text-center mb-2">Jogador 2 {currentPlayer === 2 && '(Vez)'}</h3>
              {renderGameBoard(player2Board)}
            </div>
          </div>

          {gameStarted && renderKeyboard()}
        </CardContent>
      </Card>
    </div>
  );
};

export default WordleMultiplayer;