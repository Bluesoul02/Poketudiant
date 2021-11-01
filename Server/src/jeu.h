#include <string.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#define BUFFER_SIZE 65536

char *map;

void readMap(char *fileName);