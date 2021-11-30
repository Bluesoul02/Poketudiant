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

    def getHealth(self):
        self.currentHP = self.maxHP

    def levelUp(self):
        self.level += 1
        self.expLevel = self.calculExp(self.level)
        self.exp = 0
        self.getHealth()
        if self.evolution:
            if (self.level == 3) and (random.uniform(0.0,100.0) <= 20.0):
                self.evolution()
            elif (self.level == 4) and (random.uniform(0.0,100.0) <= 37.5):
                self.evolution()
            elif (self.level >= 5):
                self.evolution()
    
    def evolution(self):
        print("J'évolue")
    
    def calculExpTotal(level):
        total = 0
        for i in range(1,level):
            total += int(500 * ((1+i) / 2))
        return total

    def calculExp(level):
        return int(500 * ((1+level) / 2))
    
    def __str__(self):
        return "%s" % (self.variety)

def calculStatsPoketudiants(stat):
    down = stat * 0.9
    up = stat * 1.1
    if random.uniform(0,1) == 0:
        return int(random.uniform(down,stat))
    else:
        return int(random.uniform(stat,up))

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
    attSameType = []
    attDiffType = []
    for a in attacks:
        if a["Type"] == starter.type:
            attSameType.append(a)
        elif a["Type"] != "Teacher":
            attDiffType.append(a)
    index = random.choice(attSameType)
    starter.attacks.append(Attack(index["Attaque"], index["Type"], index["Puissance"]))
    index = random.choice(attDiffType)
    starter.attacks.append(Attack(index["Attaque"], index["Type"], index["Puissance"]))
    starter.level = 1
    starter.exp = 0
    starter.expLevel = Poketudiant.calculExp(int(starter.level))
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