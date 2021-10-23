#ifndef UDP_SERVER_H
#define UDP_SERVER_H

#include <zconf.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

struct server_udp{
    int socket;
    struct sockaddr_in servAddr;
    struct sockaddr_in clientAddr;
    socklen_t clientLen;

    ssize_t (*server_udp_receive)(struct server_udp* this,char*buf,size_t size);
    void (*server_udp_send)(struct server_udp* this,char*msg);
    void (*server_udp_bind)(struct server_udp* this,int port);
};
typedef struct server_udp* ServerUdp;

ServerUdp server_udp_create();
void server_udp_close_and_free(ServerUdp this);
int server_udp_broadcast();

struct server_tcp{
    int socket;
    int acceptedSocket;
    struct sockaddr_in servAddr;
    struct sockaddr_in clientAddr;
    socklen_t len;

    ssize_t (*server_tcp_receive)(struct server_tcp* this,char*buf,size_t size);
    void (*server_tcp_send)(struct server_tcp* this,char*msg);
    void (*server_tcp_bind)(struct server_tcp* this,int port);
};
typedef struct server_tcp* ServerTcp;

ServerTcp server_tcp_create();
void server_tcp_destroy(ServerTcp this);
int server_tcp_connexion();
#endif
