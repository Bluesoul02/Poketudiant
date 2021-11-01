#include <stdio.h>
#include <memory.h>
#include "server.h"
#include "util_string.h"
#define MAX 500

int main() {
    char buf[MAX];
    char *msg = NULL;
    memset(buf, 0, sizeof(char) * MAX);
    ServerUdp server_udp = server_udp_create();
    server_udp->server_udp_bind(server_udp, 9000);
    for (;;) {
        ssize_t n = server_udp->server_udp_receive(server_udp, buf, MAX);
        buf[n] = '\0';
        printf("message reçu '%s' \n", trim(buf));
        char *str = "looking for poketudiant servers";

        if (strncmp(buf, str, strlen(str)) == 0) msg = "i'm a poketudiant server";
        else msg = "Unknown command";
        
        server_udp->server_udp_send(server_udp, msg);
    }
    return 0;
}