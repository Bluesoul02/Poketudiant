#!/usr/bin/python3

import socket, _thread, select, string, random

class Player:
    def __init__(self, client):
        self.client = client
    
    def __str__(self):
        return "%s" % (self.client)

class Game:
    def __init__(self, name,):
        self.name = name
        self.players = []
    
    def __str__(self):
        return "%d %s" % (len(self.players), self.name)

games = []
maxPlayer = 5

def gameNameExists(name):
    for g in games:
        if str(g.name) == str(name):
            return True
    return False

def startGame(indice):
    game = games[indice]
    clients = list(map(lambda player: player.client, game.players))
    while(clients):
        readable,_,_= select.select(clients, [], [], 10)
        if readable:
            data = readable[0].recv(4096)
            if len(data) == 0: # manage the fact that one player can exit the game
                readable[0].close()
                for p in game.players:
                    if p.client.fileno() == -1:
                        game.players.remove(p)
                        break
            else:
                print(data)
        clients = list(map(lambda player: player.client, game.players))
    for c in clients: # no player in the game = close the game and connections
        c.close()
    games.remove(game)

def createGame(client, data):
    datas = data.split(" ",2)
    if len(datas) != 3: # [create, game, game data]
        return False
    gameName = datas[2]
    if not gameNameExists(gameName): # The game does not exists and the parameters are correct
        game = Game(gameName)
        games.append(game)
        # _thread.start_new_thread(startGame, (len(games) - 1,))
        client.send(("game created\n").encode('utf-8'))
        joinGame(client,gameName)
    else:
        client.send(("cannot create game\n").encode('utf-8'))

def joinGame(client, gameName):
    for g in games:
        if g.name == str(gameName) and maxPlayer > len(g.players): # the name specified is the same and the game is not full
            g.players.append(Player(client))
            client.send(("game joined\n").encode('utf-8'))
            return True
    client.send(("cannot join game\n").encode('utf-8'))

def printGames(client):
    client.send(("number of games " + str(len(games)) + "\n").encode('utf-8'))
    for g in games:
        client.send((str(g) + "\n").encode('utf-8'))
    
def listenToClient(client, address):
    try:
        readable,_,_= select.select([client], [], [], 60)
        data = client.recv(4096)
        if len(data) == 0:
            print("Client %s déconnecté\n" % address[0])
            return False
        dataDecoded = data.decode('utf-8')
        dataDecoded = dataDecoded[:-1] if dataDecoded.endswith("\n") else dataDecoded # remove the \n at the end of the line
        print('Données reçues : ', dataDecoded)
        if (dataDecoded.startswith('require game list')): # print games to client
            printGames(client)
        elif (dataDecoded.startswith('create game')): # the client wants to create a game
            createGame(client, dataDecoded)
        elif (dataDecoded.startswith('join game')): # the client wants to join a game
            gameDatas = dataDecoded.split(" ",2)
            if len(gameDatas) != 3:
                client.send(("cannot join game\n").encode('utf-8'))
            else:
                joinGame(client, gameDatas[2])
    except:
        client.close()
        return False

def udpConnexion():
    ip = ""
    port = 9000

    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        s.bind((ip, port)) # listen on the port 9000
        print('Le serveur écoute en UDP')

        while True:
            data, client = s.recvfrom(4096)
            print(f'Données : {data.decode("utf-8")} reçues de {client}')
            s.sendto("i'm a poketudiant server".encode('utf-8'),client)

def tcpConnexion():
    ip = ""
    port = 9002

    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind((ip, port)) # affect the socket to the port 9001
        s.listen() # listen on the socket
        clientid = 1

        try:
            while True:
                print(f'Le serveur écoute en TCP la connection du client n° {clientid}')
                client, address = s.accept() # accept connection with a client
                _thread.start_new_thread(listenToClient, (client,address)) # start a tcp connection with him
                clientid += 1
        except KeyboardInterrupt:
            print('...Ok, c\'est terminé pour cette fois-ci...')

def Main():
    _thread.start_new_thread(udpConnexion, ()) # start an udp connection
    tcpConnexion()

if __name__ == '__main__':
    Main()