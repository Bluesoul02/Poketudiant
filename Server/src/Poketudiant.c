#include <stdbool.h>
#include "poketudiant.h"

Poketudiant* Poketudiant__create() {
    Poketudiant *poketudiant = malloc(sizeof(Poketudiant));
    return poketudiant;
}

void Poketudiant__destroy(Poketudiant self) {
    free(self);
}

char* Poketudiant__name(Poketudiant* self) {
    return self->name;
}