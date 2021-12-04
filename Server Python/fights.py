import random

class FightPoketudiant:
    def __init__(self, player, poketudiant, poketudiantSauvage):
        self.player = player
        self.poketudiant = poketudiant
        self.poketudiantSauvage = poketudiantSauvage

def calculDamagePoketudiants(attack, defense, power):
    return random.uniform(0.9,1.1) * (attack / defense) * power

def startRivalFight():
    print("Rival Fight\n")

def probaFuite(pvEff, pvMax): # Points de vie qu'il reste au pokétudiant adverse, Point de vie max du pokétudiant adverse
    return 2*max(0.5-(pvEff/pvMax),0)

fights = []