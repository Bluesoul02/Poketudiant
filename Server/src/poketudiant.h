#include <stdbool.h>
#include <stdlib.h>

typedef enum {NOISY, LAZY, MOTIVATED, TEACHER} Type;

typedef struct attack {
    char *name;
    Type type;
    int power;
} Attack;

typedef struct _poketudiant {
    char * name;
    Attack attacks[2];
    int attack;
    int defence;
    int maxHP;
    int currentHP;
    Type type;
    bool isCatchable;
    bool isReleasable;
    struct _poketudiant *evolution;
    int level;
    int exp;
    int evolutionLevel;
} Poketudiant;

Poketudiant* Poketudiant__create();
void Poketudiant__destroy(Poketudiant self);
// getters
char* Poketudiant__name(Poketudiant* self);
//...