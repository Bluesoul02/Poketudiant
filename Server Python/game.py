from map import *
from fights import *

import _thread, select

class Game:
    def __init__(self, name):
        self.name = name
        self.map = ""
        self.players = []
    
    def getTypeCase(self, player):
        return self.map.map.split("\n")[player.y][player.x]
    
    def playersPos(self, player):
        for p in player:
            if (p != player) and (player.x == p.x) and (player.y == p.y):
                return p
        return False

    def checkPosition(self, player): # check if the player is on the poketudiant center, maybe start a fight with a poketudiant (percentage) or a player
        pos = self.getTypeCase(player)
        p=self.playersPos(player)
        if p:
            CaseType.RIVAL
        elif pos == "+":
            player.healPoketudiants()
            return CaseType.HEALTH
        elif pos == "*":
            if random.randint(0,9) <= 3:
                return CaseType.POKETUDIANT
            return CaseType.GRASS
        return CaseType.NEUTRAL
    
    def sendMap(self, player):
        map = self.map
        player.client.send(("map " + str(map.width) + " " + str(map.height) + "\n").encode('utf-8'))
        mapSplit = map.map.split("\n")
        for p in self.players:
            if p.client == player.client:
                mapSplit[p.x] = charReplacer(mapSplit[p.x], str(0), p.y)
            else:
                mapSplit[p.x] = charReplacer(mapSplit[p.x], str(p.nbRival), p.y)
        for line in mapSplit:
        	player.client.send((line + "\n").encode('utf-8'))

    def __str__(self):
        return "%d %s" % (len(self.players), self.name)

games = []
maxPlayer = 4

def gameNameExists(name):
    for g in games:
        if str(g.name) == str(name):
            return True
    return False

def printGames(client):
    client.send(("number of games " + str(len(games)) + "\n").encode('utf-8'))
    for g in games:
        client.send((str(g) + "\n").encode('utf-8'))

def createGame(client, data):
    datas = data.split(" ",2)
    if len(datas) != 3: # [create, game, game data]
        return False
    gameName = datas[2]
    if (not gameNameExists(gameName)) and len(games)<4: # The game does not exists and the parameters are correct
        game = Game(gameName)
        initializeMap(game)
        games.append(game)
        joinGame(client,gameName, 1)
        _thread.start_new_thread(startGame, (len(games) - 1,))
        return True
    else:
        client.send(("cannot create game\n").encode('utf-8'))
        return False

def joinGame(client, gameName, justCreated = 0):
    for g in games:
        if g.name == str(gameName) and maxPlayer > len(g.players): # the name specified is the same and the game is not full
            player = createPlayer(client,g)
            g.players.append(player)
            if not justCreated:
                client.send(("game joined\n").encode('utf-8'))
            else:
                client.send(("game created\n").encode('utf-8'))
            g.sendMap(player) # send the map to the player
            player.sendPoketudiants()
            return True
    client.send(("cannot join game\n").encode('utf-8'))
    return False

def startGame(indice):
    game = games[indice]
    clients = list(map(lambda player: player.client, game.players))
    while(len(clients)):
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
                dataDecoded = data.decode('utf-8')
                dataDecoded = dataDecoded[:-1] if dataDecoded.endswith("\n") else dataDecoded
                player = getPlayer(game,readable[0])
                if (dataDecoded.startswith('map move')):
                    if movement(dataDecoded,player,game):
                        sendMapForAll(game,clients)
                        pos = game.checkPosition(player)
                        if pos == CaseType.POKETUDIANT:
                            startPoketudiantFight()
                        elif pos == CaseType.RIVAL:
                            startRivalFight()
                elif (dataDecoded.startswith('send message')):
                    message = dataDecoded.split(" ",2)
                    player.sendMsgChat(clients, message[2])
                elif (dataDecoded.startswith('poketudiant')):
                    datas = dataDecoded.split(" ", 2)
                    if poketudiantManage(player, int(datas[1]), datas[2]):
                        player.sendPoketudiants()
                elif (dataDecoded.startswith('exit game')):
                    game.players.remove(getPlayer(readable[0]))
                    listenToClient(readable[0])
        clients = list(map(lambda player: player.client, game.players))
    for c in clients: # no player in the game = close the game and connections
        c.close()
    games.remove(game)

def listenToClient(client):
    inGame = False
    while(not inGame):
        try:
            readable,_,_= select.select([client], [], [], 60)
            data = client.recv(4096)
            if len(data) == 0:
                print("Client %s déconnecté\n" % client)
                return False
            dataDecoded = data.decode('utf-8')
            dataDecoded = dataDecoded[:-1] if dataDecoded.endswith("\n") else dataDecoded # remove the \n at the end of the line
            print('Données reçues : ', dataDecoded)
            if (dataDecoded.startswith('require game list')): # print games to client
                printGames(client)
            elif (dataDecoded.startswith('create game')): # the client wants to create a game
                if createGame(client, dataDecoded):
                    inGame = True
            elif (dataDecoded.startswith('join game')): # the client wants to join a game
                gameDatas = dataDecoded.split(" ",2)
                if len(gameDatas) != 3:
                    client.send(("cannot join game\n").encode('utf-8'))
                else:
                    if joinGame(client, gameDatas[2]):
                        inGame = True
        except:
            client.close()
            return False