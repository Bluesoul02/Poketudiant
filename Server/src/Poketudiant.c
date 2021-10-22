#include <stdbool.h>
#include "poketudiant.h"


struct Poketudiant {
    char * name;
    // attacks list
    int attack;
    int defence;
    int maxHP;
    int currentHP;
    // type
    bool isCatchable;
    bool isReleasable;
    //Poketudiant evolution;
    int level;
    int exp;
    int evolutionLevel;
};

