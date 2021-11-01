#include "jeu.h"
#include "error.h"

void readMap(char *fileName) {
    int fd;
    if((fd = open(fileName, O_RDONLY)) == ERR) {
        perror("Cannot open: ");
        exit(1);
    }
    map = (char *)malloc(sizeof(char) * BUFFER_SIZE);
    if ((read(fd, map, BUFFER_SIZE)) == ERR) {
        perror("Cannot read: ");
        free(map);
        exit(2);
    }
}