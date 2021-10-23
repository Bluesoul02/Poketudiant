#include <stdlib.h>
#include <memory.h>
#include "server.h"
#include "error.h"
#include <stdio.h>
#define neterr_server_udp(srv, n) server_udp_close_and_free(srv), syserror(n);
#define neterr_server_tcp(srv, n) server_tcp_destroy(srv),syserror(n);
#define MAX 500

// UDP Broadcast

static ssize_t server_udp_receive(struct server_udp *this, char *buf, size_t size) {
    return recvfrom(this->socket, buf, size, 0, (struct sockaddr *)&this->clientAddr, &this->clientLen);
}

static void server_udp_send(struct server_udp *this, char *msg) {
    printf("message envoyé '%s'\n", msg);
    if (sendto(this->socket, msg, strlen(msg), 0, (struct sockaddr *)&this->clientAddr, this->clientLen) == ERR)
    {
        neterr_server_udp(this, SEND_ERR);
    }
}

void server_udp_bind(struct server_udp *this, int port) {
    this->servAddr.sin_family = AF_INET;
    this->servAddr.sin_addr.s_addr = INADDR_ANY;
    this->servAddr.sin_port = htons((uint16_t)port);
    this->clientLen = sizeof(struct sockaddr_in);
    if (bind(this->socket, (struct sockaddr *)&this->servAddr, sizeof(this->servAddr)) < 0) neterr_server_udp(this, BINDING_ERR);
}

ServerUdp server_udp_create() {
    ServerUdp srv = malloc(sizeof(struct server_udp));

    int sfd;
    if ((sfd = socket(AF_INET, SOCK_DGRAM, 0)) == ERR) {
        free(srv);
        syserror(SOCKET_ERR);
    }

    srv->socket = sfd;
    srv->server_udp_bind = &server_udp_bind;
    srv->server_udp_receive = &server_udp_receive;
    srv->server_udp_send = &server_udp_send;
    memset(&srv->servAddr, 0, sizeof(struct sockaddr_in));
    return srv;
}

void server_udp_close_and_free(ServerUdp this) {
    close(this->socket);
    free(this);
}

int server_udp_broadcast() {
    char buf[MAX];
    char *msg = NULL;
    memset(buf, 0, sizeof(char) * MAX);
    ServerUdp server_udp = server_create_udp();
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

// TCP Connexion

static ssize_t server_tcp_receive(struct server_tcp* this,char*buf,size_t size){
    return recv(this->acceptedSocket, buf, size,0);
}
static void server_tcp_send(struct server_tcp* this,char*msg){
    if (send(this->acceptedSocket, msg, strlen(msg), 0) == ERR) neterr_server(this, SEND_ERR);
}


static void server_tcp_bind(struct server_tcp* this,int port){
    this->servAddr.sin_family = AF_INET;
    this->servAddr.sin_addr.s_addr = INADDR_ANY;
    this->servAddr.sin_port = htons((uint16_t) port);
    if (bind(this->socket, (struct sockaddr *) &this->servAddr, sizeof(this->servAddr)) < 0) {
        neterr_server(this, BINDING_ERR);
    }
}

ServerTcp server_tcp_create(){
    ServerTcp srv=malloc(sizeof(struct server_tcp));

    int sfd;
    if((sfd=socket(AF_INET,SOCK_STREAM,0))==ERR) {
        free(srv);
        syserror(SOCKET_ERR);
    }

    srv->socket=sfd;
    srv->server_tcp_bind=&server_tcp_bind;
    srv->server_tcp_receive=&server_tcp_receive;
    srv->server_tcp_send=&server_tcp_send;
    memset(&srv->servAddr,0,sizeof(struct sockaddr_in));
    memset(&srv->clientAddr,0,sizeof(struct sockaddr_in));
    srv->len=sizeof(struct sockaddr_in);
    return srv;
}

void server_tcp_destroy(ServerTcp this){
    close(this->socket);
    free(this);
}

int server_tcp_connexion() {
    char buf[MAX];
    char *msg=NULL;
    memset(buf,0,sizeof(char)*MAX);
    
    ServerTcp server_tcp=server_tcp_create();
    server_tcp->server_tcp_bind(server_tcp,1344);
    
    if(listen(server_tcp->socket,SOMAXCONN)==-1) exit(1);

    printf("Listen OK\n"); 
    server_tcp->acceptedSocket=accept(server_tcp->socket,(struct sockaddr*)&server_tcp->clientAddr,&server_tcp->len);
        
    for(;;) {   
        printf("Accepted\n");

        ssize_t n=server_tcp->server_tcp_receive(server_tcp, buf, MAX);
        buf[n]='\0';

        char* str="require game list";

        if(strncmp(buf,str,strlen(str))==0) msg="number of games N - players in each game";
        else msg="Unknown command";
        server_tcp->server_tcp_send(server_tcp, msg);
    }
    server_tcp_destroy(server_tcp);
    return 0;
}