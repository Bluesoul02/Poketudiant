#!/usr/bin/python3

import socket
import threading
  
def udpConnexion():
    ip = ""
    port = 9000

    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        # Ecoute sur le port 9000
        s.bind((ip, port))
        print('Le serveur écoute en UDP')

        while True:
            print('-- j\'attends --')
            data, client = s.recvfrom(4096)
            print(f'Données : {data.decode("utf-8")} reçues de {client}')
            s.sendto("i'm a poketudiant server.".encode('utf-8'),client)

def tcpConnexion():
    ip = ""
    port = 9001

    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        # Ecoute sur le port 9001
        s.bind((ip, port))
        s.listen()
        clientid = 1

        try:
            while True:
                print(f'Le serveur écoute en TCP la connection du client n° {clientid}')
                client, address = s.accept()
                while True:
                    print('-- j\'attends --')
                    data = client.recv(4096)
                    if len(data) == 0:
                        print('Le client s\'est déconnecté, j\'arrête')
                        break
                    print('Données reçues : ', data.decode('utf-8'))
                    client.send(("Number of games N - players in each game").encode('utf-8'))
                clientid += 1
        except KeyboardInterrupt:
            print(' ... Ok, c\'est terminé pour cette fois-ci ...')


def Main():
    threading.Thread(target=udpConnexion).start()

if __name__ == '__main__':
    Main()