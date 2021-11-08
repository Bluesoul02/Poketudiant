#include <stdio.h>
#include <memory.h>
#include "server.h"
#include "util_string.h"
#include "error.h"
#include "poketudiant.h"
#include "jeu.h"

int main() {
    int pid = fork();
    if(pid == ERR) perror("Fork :"), exit(1);
    if(pid == 0) {
        server_udp_broadcast();
    } else server_tcp_connexion();

    // readMap("map.txt");
    return 0;
}