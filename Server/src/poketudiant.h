#include <stdbool.h>
#include <stdlib.h>

typedef struct _poketudiant {
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
} Poketudiant;

Poketudiant* Poketudiant__create();
void Poketudiant__destroy(Poketudiant self);
// getters
char* Poketudiant__name(Poketudiant* self);
//...