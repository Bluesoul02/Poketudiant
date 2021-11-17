#!/usr/bin/python3

import socket, _thread, select, enum, random, os

class Player:
    def __init__(self, client, x, y, nbRival):
        self.client = client
        self.x = x
        self.y  = y
        self.nbRival = nbRival
        self.poketudiants = []

    def moveLeft(self):
        if self.x > 0:
            self.x -= 1
            return True
        return False

    def moveRight(self, game):
        if self.x < (game.width - 1):
            self.x += 1
            return True
        return False

    def moveUp(self):
        if self.y > 0:
            self.y += 1
            return True
        return False

    def moveDown(self, game):
        if self.x < (game.height - 1):
            self.x -= 1
            return True
        return False
    
    def sendPoketudiants(self):
        self.client.send("team contains " + str(len(self.poketudiants))).encode('utf-8')
        for p in self.poketudiants:
            poketudiant = str(p.variety) + " " + str(p.type) + " " + str(p.level) + " " + str(p.exp) + " " +  str(p.expLevel - p.exp) + " " + str(p.currentHP) + " " + str(p.maxHP) + " " + str(p.attack) + " " + str(p.defence)
            for a in p.attacks:
                poketudiant += " " + str(a.name) + " " + str(a.type)
            self.client.send(poketudiant).encode('utf-8')
        
    def poketudiantMoveUp(self, indice):
        if indice >= len(self.poketudiants) or indice == 0:
            return False
        temp = self.poketudiants[indice-1]
        self.poketudiants[indice-1] = self.poketudiants[indice]
        self.poketudiants[indice] = temp
        return True

    def poketudiantMoveDown(self, indice):
        if indice >= len(self.poketudiants) or indice == 2:
            return False
        temp = self.poketudiants[indice+1]
        self.poketudiants[indice+1] = self.poketudiants[indice]
        self.poketudiants[indice] = temp
        return True

    def poketudiantFree(self, indice):
        if len(self.poketudiants) == 0 or indice > (len(self.poketudiants) - 1):
            return False
        self.poketudiants.pop(indice)
        return True

    def sendMsgChat(self, clients, msg):
        message = "rival message " + self.client.getsockname()[0] + " " + self.client.getsockname()[1] + " : " + msg
        for c in clients:
            c.send((message).encode('utf-8'))
    
    def healPoketudiants(self):
        for p in self.poketudiants:
            p.getHealth()

    def __str__(self):
        return "%s" % (self.client)
 
class Type(enum.Enum):
    NOISY = 1
    LAZY = 2
    MOTIVATED = 3
    TEACHER = 4

class CaseType(enum.Enum):
    NEUTRAL = 1
    HEALTH = 2
    GRASS = 3
    POKETUDIANT = 4
    RIVAL = 5

class Map:
    def __init__(self, map):
        self.map = map
        self.width = 0
        self.height = 0
        self.spawns = []

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
        print("sendMap")
        player.client.send("map " + str(map.width) + " " + str(map.height)).encode('utf-8')
        mapSplit = map.map.split("\n")
        for p in self.players:
            if p.player.client == player.client:
                mapSplit[p.x] = charReplacer(mapSplit[p.x], str(0), p.y)
            else:
                mapSplit[p.x] = charReplacer(mapSplit[p.x], str(p.nbRival), p.y)
        for line in mapSplit:
            player.client.send((line + "\n").encode('utf-8'))
            print("map sended")
            
    def __str__(self):
        return "%d %s" % (len(self.players), self.name)

class Attack:
    def __init__(self, name, type, power):
        self.name = name
        self.type = type
        self.power = power
    
    def __str__(self):
        return "%s %s %s" % (self.name, self.type, self.power)

class Poketudiant:
    def __init__(self, variety, type, level, expLevel, exp, currentHP, maxHP, attack, defence, attacks, isCatchable, isReleasable, evolution, evolutionLevel):
        self.variety = variety
        self.type = type
        self.level = level
        self.expLevel = expLevel
        self.exp = exp
        self.currentHP = currentHP
        self.maxHP = maxHP
        self.attack = attack
        self.defence = defence
        self.attacks = attacks
        self.isCatchable = isCatchable
        self.isReleasable = isReleasable
        self.evolution = evolution
        self.evolutionLevel = evolutionLevel

    def getHealth(self):
        self.currentHP = self.maxHP
    
    def __str__(self):
        return "%s" % (self.variety, self.type, self.level, self.exp, self.expLevel - self.exp, self.currentHP, self.maxHP, self.attack, self.defence)

games = []
maxPlayer = 4

def sendMapForAll(game, clients):
    for c in clients:
        game.sendMap(getPlayer(game, c))

def getPlayer(game, client):
    for p in game.players:
        if p.client == client:
            return p

def movement(data, player, game):
    move = data.split(" ")[2]
    if move == "left":
        return player.moveLeft()
    elif move == "right":
        return player.moveRight(game)
    elif move == "down":
        return player.moveDown(game)
    elif move == "up":
        return player.moveUp()
    return False

def gameNameExists(name):
    for g in games:
        if str(g.name) == str(name):
            return True
    return False

def charReplacer(s, newstring, index, nofail=False):
    if not nofail and index not in range(len(s)): # raise an error if index is outside of the string
        raise ValueError("index outside given string")
    if index < 0:  # add it to the beginning
        return newstring + s
    if index > len(s):  # add it to the end
        return s + newstring
    return s[:index] + newstring + s[index + 1:]

def initializeMap(game):
    script_dir = os.path.dirname(__file__) # absolute directory the file is in
    rel_path = "../map.txt"
    abs_file_path = os.path.join(script_dir, rel_path)
    fd = open(abs_file_path, 'r') # initialize the game map
    game.map = Map(fd.read())
    mapSplit = game.map.map.split("\n")
    width = len(mapSplit)-1
    height = len(mapSplit[0])
    game.map.width = width
    game.map.height = height
    game.map.spawns = [(0,0), (width-1,0), (0,height-1), (width-1,height-1)]
    fd.close()

def poketudiantManage(player, indice, text):
    if (indice < 0) or (indice > 2):
        return False
    if text == "move up":
        return player.poketudiantMoveUp(indice)
    elif text == "move down":
        return player.poketudiantMoveDown(indice)
    elif text == "free":
        return player.poketudiantFree(indice)
    return False

def startPoketudiantFight():
    print("Poketudiant Fight\n")

def startRivalFight():
    print("Rival Fight\n")

def startGame(indice):
    game = games[indice]
    clients = list(map(lambda player: player.client, game.players))
    while(True):
        while(not clients):
            clients = list(map(lambda player: player.client, game.players))
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
                    if poketudiantManage(player, datas[1], datas[2]):
                        player.sendPoketudiants()
                elif (dataDecoded.startswith('exit game')):
                    game.players.remove(getPlayer(readable[0]))
                    listenToClient(readable[0])
        clients = list(map(lambda player: player.client, game.players))
    # for c in clients: # no player in the game = close the game and connections
    #     c.close()
    # games.remove(game)

def createGame(client, data):
    datas = data.split(" ",2)
    if len(datas) != 3: # [create, game, game data]
        return False
    gameName = datas[2]
    if (not gameNameExists(gameName)) and len(games)<4: # The game does not exists and the parameters are correct
        game = Game(gameName)
        initializeMap(game)
        games.append(game)
        # joinGame(client,gameName)
        _thread.start_new_thread(startGame, (len(games) - 1,))
        client.send(("game created\n").encode('utf-8'))
        return True
    else:
        client.send(("cannot create game\n").encode('utf-8'))
        return False

def joinGame(client, gameName):
    for g in games:
        if g.name == str(gameName) and maxPlayer > len(g.players): # the name specified is the same and the game is not full
            g.players.append(Player(client, g.map.spawns[len(g.players) + 1][0], g.map.spawns[len(g.players) + 1][1], len(g.players) + 1))
            client.send(("game joined\n").encode('utf-8'))
            g.sendMap(getPlayer(g, client)) # send the map to the player
            return True
    client.send(("cannot join game\n").encode('utf-8'))
    return False

def printGames(client):
    client.send(("number of games " + str(len(games)) + "\n").encode('utf-8'))
    for g in games:
        client.send((str(g) + "\n").encode('utf-8'))
    
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
    port = 9001

    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind((ip, port)) # affect the socket to the port 9001
        s.listen() # listen on the socket
        clientid = 1

        try:
            while True:
                print(f'Le serveur écoute en TCP la connection du client n° {clientid}')
                client, address = s.accept() # accept connection with a client
                _thread.start_new_thread(listenToClient, (client,)) # start a tcp connection with him
                clientid += 1
        except KeyboardInterrupt:
            print('...Ok, c\'est terminé pour cette fois-ci...')

def Main():
    _thread.start_new_thread(udpConnexion, ()) # start an udp connection
    tcpConnexion()

if __name__ == '__main__':
    Main()