package sockets;

import model.State;
import model.Player;
import model.Winner;

public record GameControl(TypeState type, Player turn, String board, Winner winner) {

}