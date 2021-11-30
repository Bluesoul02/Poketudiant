from poketudiants import *

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
        if self.x < (game.map.width - 1):
            self.x += 1
            return True
        return False

    def moveUp(self):
        if self.y > 0:
            self.y += 1
            return True
        return False

    def moveDown(self, game):
        if self.x < (game.map.height - 1):
            self.x -= 1
            return True
        return False
    
    def sendPoketudiants(self):
        self.client.send(("team contains " + str(len(self.poketudiants)) + "\n").encode('utf-8'))
        for p in self.poketudiants:
            poketudiant = str(p.variety) + " " + str(p.type) + " " + str(p.level) + " " + str(p.exp) + " " +  str(p.expLevel - p.exp) + " " + str(p.currentHP) + " " + str(p.maxHP) + " " + str(p.attack) + " " + str(p.defence)
            for a in p.attacks:
                poketudiant += " " + str(a.name) + " " + str(a.type)
            self.client.send((poketudiant + "\n").encode('utf-8'))
        
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
        if self.poketudiants[indice].variety != "Enseignant-dresseur":
            self.poketudiants.pop(indice)
        return True

    def sendMsgChat(self, clients, msg):
    	message = "rival message " + str(self.client.getpeername()[0]) + " " + str(self.client.getpeername()[1]) + " : " + str(msg) + "\n"
    	for c in clients:
    		c.send((message + "\n").encode('utf-8'))
    
    def healPoketudiants(self):
        for p in self.poketudiants:
            p.getHealth()

    def __str__(self):
        return "%s" % (self.client)

def createPlayer(client, game):
    player = Player(client, game.map.spawns[len(game.players)][0], game.map.spawns[len(game.players)][1], len(game.players)+1)
    player.poketudiants.append(createPoketudiant("Enseignant-dresseur"))
    return player

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

def getPlayer(game, client):
    for p in game.players:
        if p.client == client:
            return p