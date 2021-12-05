import random, enum

from records import *

class Type(enum.Enum):
    NOISY = 1
    LAZY = 2
    MOTIVATED = 3
    TEACHER = 4

class Attack:
    def __init__(self, name, type, power):
        self.name = name
        self.type = type
        self.power = power
    
    def __str__(self):
        return "%s %s %s" % (self.name, self.type, self.power)

class Poketudiant:
    def __init__(self, variety = None, type = None, level = None, expLevel = None, exp = None, currentHP = None, maxHP = None, attack = None, defence = None, attacks = [], isCatchable = False, isReleasable = False, evolution = None):
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
        self.attackFirstLevel = attack
        self.defenceFirstLevel = defence
        self.maxHPFirstLevel = maxHP
    
    def loseXPFight(self):
        totalExp = 0.2 * (self.calculExpTotal() + self.exp)
        while totalExp > 0:
            if (self.exp-totalExp < 0):
                totalExp -= self.exp
                self.level -=1
                self.expLevel = calculExp(self.level)
                self.exp = self.expLevel
            else:
                self.exp -= int(totalExp)
                break

    def getHealth(self):
        self.currentHP = self.maxHP
    
    def gainExp(self, exp, client, index):
        self.exp += exp
        client.send(("encounter poketudiant xp " + str(index) + " " + str(exp) + "\n").encode('utf-8'))
        if self.exp >= self.expLevel:
            self.levelUp(index, client)

    def levelUp(self, index=None, client=None):
        if self.level < 10:
            if client:
                client.send(("encounter poketudiant level " + str(index) + " " + str(1) + "\n").encode('utf-8'))
            self.level += 1
            self.expLevel = calculExp(self.level)
            self.exp = 0
            self.getHealth()
            self.maxHP += int(self.maxHPFirstLevel*0.1)
            self.defence += int(self.defenceFirstLevel*0.1)
            self.attack += int(self.attackFirstLevel*0.1)
            if self.evolution != "None":
                if (self.level == 3) and (random.uniform(0.0,100.0) <= 20.0):
                    if client:
                        client.send(("encounter poketudiant evolution " + str(index) + " " + self.evolution + "\n").encode('utf-8'))
                    self.getEvolution()
                elif (self.level == 4) and (random.uniform(0.0,100.0) <= 37.5):
                    if client:
                        client.send(("encounter poketudiant evolution " + str(index) + " " + self.evolution + "\n").encode('utf-8'))
                    self.getEvolution()
                elif (self.level >= 5):
                    if client:
                        client.send(("encounter poketudiant evolution " + str(index) + " " + self.evolution + "\n").encode('utf-8'))
                    self.getEvolution()
    
    def getEvolution(self):
        self = createAndGain(self.evolution, self.level)
    
    def calculExpTotal(self):
        total = 0
        for i in range(1,self.level):
            total += int(500 * ((1+i) / 2))
        return total
    
    def __str__(self):
        return "%s" % (self.variety)

def calculStatsPoketudiants(stat):
    down = stat * 0.9
    up = stat * 1.1
    if random.uniform(0,1) == 0:
        return int(random.uniform(down,stat))
    else:
        return int(random.uniform(stat,up))

def calculExp(level):
    return int(500 * ((1+level) / 2))

def createPoketudiant(name):
    starter = Poketudiant()
    exists = False
    for p in poketudiants:
        if p["Variété"] == name:
            starter.variety = p["Variété"]
            starter.type = p["Type"]
            starter.isCatchable = p["Capturable"]
            starter.evolution = p["Évolution"]
            exists = True
    if not exists:
        return False
    for s in statistics:
        if s["Variété"] == name:
            starter.attack = calculStatsPoketudiants(int(s["Attaque"]))
            starter.defence = calculStatsPoketudiants(int(s["Défense"]))
            starter.currentHP = calculStatsPoketudiants(int(s["PV max."]))
            starter.maxHP = starter.currentHP
            starter.attackFirstLevel = starter.attack
            starter.defenceFirstLevel = starter.defence
            starter.maxHPFirstLevel = starter.maxHP
    attSameType = []
    attDiffType = []
    for a in attacks:
        if a["Type"] == starter.type:
            attSameType.append(a)
        elif a["Type"] != "Teacher":
            attDiffType.append(a)
    index = random.choice(attSameType)
    starter.attacks.append(Attack(index["Attaque"], index["Type"], int(index["Puissance"])))
    index = random.choice(attDiffType)
    starter.attacks.append(Attack(index["Attaque"], index["Type"], int(index["Puissance"])))
    starter.level = 1
    starter.exp = 0
    starter.expLevel = calculExp(int(starter.level))
    starter.isReleasable = False if name == "Enseignant-dresseur" else True
    return starter

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

def createAndGain(name, level):
    poketudiant = createPoketudiant(name)
    for i in range(1,level):
        poketudiant.levelUp()
    return poketudiant

def poketudiantRandom(level):
    randomName = random.choice(poketudiants)["Variété"]
    while randomName == "Enseignant-dresseur":
        randomName = random.choice(poketudiants)["Variété"]
    poketudiant = createAndGain(randomName, random.randint(1,level))
    poketudiant.exp = random.randint(5,(calculExp(poketudiant.level) - (calculExp(poketudiant.level) // 2)))
    return poketudiant